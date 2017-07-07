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

package synergynet.contentsystem.contentloader.contentitemcreator;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.contentloader.config.AttributeConstants;
import synergynet.contentsystem.items.ContentItem;

public class ContentItemCreator {
	public static final String CONTENT_ITEM_FOLDER = "synergynet.contentsystem.items";
	private static final Logger log = Logger.getLogger(ContentItemCreator.class.getName());
		
	public static Map<ContentItem, Map<String, String>> create(Map<String, Map<String, String>> items, ContentSystem contentSystem) {
			
		Map<ContentItem, Map<String, String>> contentItems = new HashMap <ContentItem, Map<String, String>>();
		
		String contentItemTypeName;
		String type;
		
		for (String key:items.keySet()){
			
			type = items.get(key).get(AttributeConstants.ITEM_CONTENTITEMTYPE);
			contentItemTypeName = CONTENT_ITEM_FOLDER+"."+type;	
			
			try {
				Class<? extends ContentItem> itemClass = getItemClass(contentItemTypeName);
				ContentItem contentItem = contentSystem.createContentItem(itemClass);
				contentItems.put(contentItem, items.get(key));
			} catch (ClassNotFoundException e) {
				log.severe(e.toString());
			}
		}
		
		return contentItems;
		
	}

	@SuppressWarnings("unchecked")
	private static Class<? extends ContentItem> getItemClass(String contentItemTypeName) throws ClassNotFoundException {
		return (Class<? extends ContentItem>) Class.forName(contentItemTypeName);
	}
}
