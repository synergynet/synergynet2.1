package edu.upf.mtg.reactivision;
/*
	TUIO Java backend - part of the reacTIVision project
	http://www.iua.upf.es/mtg/reacTable

	Copyright (c) 2005 Martin Kaltenbrunner <mkalten@iua.upf.es>

	Permission is hereby granted, free of charge, to any person obtaining
	a copy of this software and associated documentation files
	(the "Software"), to deal in the Software without restriction,
	including without limitation the rights to use, copy, modify, merge,
	publish, distribute, sublicense, and/or sell copies of the Software,
	and to permit persons to whom the Software is furnished to do so,
	subject to the following conditions:

	The above copyright notice and this permission notice shall be
	included in all copies or substantial portions of the Software.

	Any person wishing to distribute modifications to the Software is
	requested to send the modifications to the original developer so that
	they can be incorporated into the canonical version.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
	EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
	MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
	IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
	ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
	CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
	WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

import com.illposed.osc.*;
import java.util.*;

public class TuioClient implements OSCListener {
	
	private int port = 3333;
	private OSCPortIn oscPort;
	private Hashtable<Object, Object> objectList = new Hashtable<Object, Object>();
	private Vector<Object> aliveObjectList = new Vector<Object>();
	private Vector<Object> newObjectList = new Vector<Object>();
	private Vector<Object> aliveCursorList = new Vector<Object>();
	private Vector<Object> newCursorList = new Vector<Object>();
	
	private int currentFrame = 0;
	private int lastFrame = 0;
	
	private Vector<TuioListener> listenerList = new Vector<TuioListener>();
	
	public TuioClient(int port) {
		this.port = port;
	}
	
	public TuioClient() {}
	
	public void connect() {
		try {
			oscPort = new OSCPortIn(port);
			oscPort.addListener("/tuio/2Dobj",this);
			oscPort.addListener("/tuio/2Dcur",this);
			oscPort.startListening();
		} catch (Exception e) {
			System.out.println("failed to connect to port "+port);
		}
	}
	
	public void disconnect() {
		oscPort.stopListening();
		try { Thread.sleep(100); }
		catch (Exception e) {};
		oscPort.close();
	}
	
	public void addTuioListener(TuioListener listener) {
		listenerList.addElement(listener);
	}
	
	public void removeTuioListener(TuioListener listener) {	
		listenerList.removeElement(listener);
	}
	
	public void acceptMessage(Date date, OSCMessage message) {
	
		Object[] args = message.getArguments();
		String command = (String)args[0];
		String address = message.getAddress();
		if (address.equals("/tuio/2Dobj")) {

			if ((command.equals("set")) && (currentFrame>=lastFrame)) {
				int s_id  = ((Integer)args[1]).intValue();
				int f_id   = ((Integer)args[2]).intValue();
				float x = ((Float)args[3]).floatValue();
				float y = ((Float)args[4]).floatValue();
				float a = ((Float)args[5]).floatValue();
				float X = ((Float)args[6]).floatValue();
				float Y = ((Float)args[7]).floatValue();
				float A = ((Float)args[8]).floatValue();
				float m = ((Float)args[9]).floatValue();
				float r = ((Float)args[10]).floatValue();
				
				boolean add_object = false;
				if (objectList.get(args[1]) == null) {
					objectList.put(args[1],args[2]);
					add_object = true;
				}
	
				//System.out.println(s_id+" "+f_id+" "+x+" "+y+" "+a+" "+X+" "+Y+" "+A+" "+m+" "+r);
				for (int i=0;i<listenerList.size();i++) {
					TuioListener listener = listenerList.elementAt(i);
					if (listener!=null) {
						if (add_object) listener.addTuioObj(s_id,f_id);
						listener.updateTuioObj(s_id,f_id,x,y,a,X,Y,A,m,r);
					}
				}
				
			} else if ((command.equals("alive")) && (currentFrame>=lastFrame)) {
	
				for (int i=1;i<args.length;i++) {
					// get the message content
					newObjectList.addElement(args[i]);
					// reduce the object list to the lost objects
					if (aliveObjectList.contains(args[i]))
						 aliveObjectList.removeElement(args[i]);
				}
				
				// remove the remaining objects
				for (int i=0;i<aliveObjectList.size();i++) {
					int s_id = ((Integer)aliveObjectList.elementAt(i)).intValue();
					int f_id = ((Integer)objectList.remove(aliveObjectList.elementAt(i))).intValue();
					//System.out.println("remove "+id);
					for (int j=0;j<listenerList.size();j++) {
						TuioListener listener = listenerList.elementAt(j);
						if (listener!=null) listener.removeTuioObj(s_id,f_id);
					}
				}
				
				Vector<Object> buffer = aliveObjectList;
				aliveObjectList = newObjectList;
				
				// recycling of the vector
				newObjectList = buffer;
				newObjectList.clear();
					
			} else if (command.equals("fseq")) {
				lastFrame = currentFrame;
				currentFrame = ((Integer)args[1]).intValue();
				
				if (currentFrame>lastFrame) {
					for (int i=0;i<listenerList.size();i++) {
						TuioListener listener = listenerList.elementAt(i);
						if (listener!=null) listener.refresh();
					}
				}
			}

		} else if (address.equals("/tuio/2Dcur")) {

			if ((command.equals("set")) && (currentFrame>=lastFrame)) {
				int s_id  = ((Integer)args[1]).intValue();
				float x = ((Float)args[2]).floatValue();
				float y = ((Float)args[3]).floatValue();
				float X = ((Float)args[4]).floatValue();
				float Y = ((Float)args[5]).floatValue();
				float m = ((Float)args[6]).floatValue();
				
				for (int i=0;i<listenerList.size();i++) {
					TuioListener listener = listenerList.elementAt(i);
					if (listener!=null) listener.updateTuioCur(s_id,x,y,X,Y,m);
				}
				
			} else if ((command.equals("alive")) && (currentFrame>=lastFrame)) {
	
				for (int i=1;i<args.length;i++) {
					// get the message content
					newCursorList.addElement(args[i]);
					// reduce the object list to the lost objects
					if (aliveCursorList.contains(args[i])) 
						aliveCursorList.removeElement(args[i]);
					else {
						for (int j=0;j<listenerList.size();j++) {
							TuioListener listener = listenerList.elementAt(j);
							if (listener!=null)
								listener.addTuioCur(((Integer)args[i]).intValue());
						}
					}
				}
				
				// remove the remaining objects
				for (int i=0;i<aliveCursorList.size();i++) {
					int s_id = ((Integer)aliveCursorList.elementAt(i)).intValue();
					//System.out.println("remove "+id);
					for (int j=0;j<listenerList.size();j++) {
						TuioListener listener = listenerList.elementAt(j);
						if (listener!=null) listener.removeTuioCur(s_id);
					}
				}
				
				Vector<Object> buffer = aliveCursorList;
				aliveCursorList = newCursorList;
				
				// recycling of the vector
				newCursorList = buffer;
				newCursorList.clear();
			} 

		}
	}
}
