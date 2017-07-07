/* Copyright (c) 2008 University of Durham, England
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


package synergynet.contentsystem.items;

import java.util.ArrayList;
import java.util.List;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.implementation.interfaces.IRoundListContainerImplementation;

public class RoundListContainer extends RoundWindow implements IRoundListContainerImplementation {
	
	private static final long serialVersionUID = 4456222243914896154L;

	protected List<RoundContentItem> listItems = new ArrayList<RoundContentItem>();
	
	public RoundListContainer(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
	}
	
	public void addSubItem(int index, RoundContentItem item){
		if (!listItems.contains(item)){
			listItems.add(index, item);
			super.addSubItem(item);
		}
		
		((IRoundListContainerImplementation)this.contentItemImplementation).addSubItem(index, item);
	}
	
	public void addSubItem(RoundContentItem item){
		if (!listItems.contains(item)){
			listItems.add(item);
			super.addSubItem(item);
		}
		
		((IRoundListContainerImplementation)this.contentItemImplementation).addSubItem(item);
	}
	
	public void removeSubItem(RoundContentItem item){
		if (listItems.contains(item)){
			listItems.remove(item);
			super.removeSubItem(item);
		}
		
		((IRoundListContainerImplementation)this.contentItemImplementation).removeSubItem(item);
	}
	
	public void removeSubItem(int index){
		if (listItems.size()>index){		
			super.removeSubItem(listItems.get(index));
			((IRoundListContainerImplementation)this.contentItemImplementation).removeSubItem(listItems.get(index));
			listItems.remove(index);			
		}
	}
	
	public List<RoundContentItem> getListItems(){
		return listItems;
	}
	
	public void run(){
		((IRoundListContainerImplementation)this.contentItemImplementation).run();
	}

}
