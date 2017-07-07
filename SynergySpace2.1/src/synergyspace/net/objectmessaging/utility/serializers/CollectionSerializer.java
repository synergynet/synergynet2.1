/*
 * Copyright (c) 2009 University of Durham, England
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'SynergySpace' nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package synergyspace.net.objectmessaging.utility.serializers;


import java.nio.ByteBuffer;
import java.util.Collection;

import synergyspace.net.objectmessaging.Network;
import synergyspace.net.objectmessaging.connections.ConnectionHandler;

public class CollectionSerializer extends Serializer {
	static private final CollectionSerializer instance = new CollectionSerializer();

	private boolean elementsAreNotNull;
	private Serializer serializer;
	private Class<?> elementClass;

	public CollectionSerializer () {
	}


	public CollectionSerializer (Class<?> elementClass, boolean elementsAreNotNull) {
		this.elementClass = elementClass;
		this.elementsAreNotNull = elementsAreNotNull;
		serializer = elementClass == null ? null : Network.getRegisteredClass(elementClass).serializer;
	}

	public void writeObjectData (ConnectionHandler connectionHandler, ByteBuffer buffer, Object object, boolean lengthKnown)
		throws SerializationException {
		Collection<?> collection = (Collection<?>)object;
		int length = collection.size();
		IntSerializer.put(buffer, length, true);
		if (length == 0) return;
		if (serializer != null) {
			if (elementsAreNotNull) {
				for (Object element : collection)
					serializer.writeObjectData(connectionHandler, buffer, element, false);
			} else {
				for (Object element : collection)
					serializer.writeObject(connectionHandler, element, buffer);
			}
		} else {
			for (Object element : collection)
				Network.writeClassAndObject(connectionHandler, element, buffer);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T readObjectData (ConnectionHandler connectionHandler, ByteBuffer buffer, Class<T> type, boolean lengthKnown)
		throws SerializationException {
		int length = IntSerializer.get(buffer, true);
		Collection collection = (Collection)newInstance(type);
		if (length == 0) return (T)collection;
		if (serializer != null) {
			if (elementsAreNotNull) {
				for (int i = 0; i < length; i++)
					collection.add(serializer.readObjectData(connectionHandler, buffer, elementClass, false));
			} else {
				for (int i = 0; i < length; i++)
					collection.add(serializer.readObject(connectionHandler, buffer, elementClass));
			}
		} else {
			for (int i = 0; i < length; i++)
				collection.add(Network.readClassAndObject(connectionHandler, buffer));
		}
		return (T)collection;
	}

	static public void put (ConnectionHandler connectionHandler, ByteBuffer buffer, Collection<?> value, Class<?> elementClass,
		boolean elementsAreNotNull) throws SerializationException {
		instance.elementClass = elementClass;
		instance.elementsAreNotNull = elementsAreNotNull;
		instance.writeObjectData(connectionHandler, value, buffer);
	}

	static public Collection<?> get (ConnectionHandler connectionHandler, ByteBuffer buffer, Class<?> elementClass, boolean elementsAreNotNull)
		throws SerializationException {
		instance.elementClass = elementClass;
		instance.elementsAreNotNull = elementsAreNotNull;
		return instance.readObjectData(connectionHandler, buffer, null);
	}
}
