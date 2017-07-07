/*
 * Copyright (c) 2008 University of Durham, England
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

package synergyspace.mtinput;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import synergynet.contentsystem.items.ContentItem;
import synergynet.contentsystem.items.listener.ItemListener;

public class LongHoldDetector {
	
	protected float distance=2f;
	protected long time = 2000;
	protected Map<Long,CursorDownRecord> records = new HashMap<Long,CursorDownRecord>();
	protected List<ItemListener> itemListeners;
	protected ContentItem item;

	public LongHoldDetector(long time, float distance, List<ItemListener> itemListeners, ContentItem item) {
		setSensitivity(time, distance);
		this.itemListeners = itemListeners;
	}
	
	public void setSensitivity(long time, float distance) {
		this.time = time;
		this.distance = distance;
	}
	
	public float getDistanceSensitivity() {
		return this.distance;
	}
	
	public long getTimeSensitivity() {
		return this.time;
	}
	
	public void update(float tpf){
		
		for (Long key: records.keySet()){
			
			CursorDownRecord record = records.get(key);
			if (record.isLongHeld()){
				for (ItemListener l: this.itemListeners)
					l.cursorLongHeld(item, key, record.currentPosition.x, record.currentPosition.y, 0f);						
			}
		}
		
	}
	
	public void updateCursorPostion(long id, Point2D.Float position){
		if (records.containsKey(id)){
			records.get(id).currentPosition.x = position.x;
			records.get(id).currentPosition.y = position.y;
		}
	}
	
	public void registerCursor(long id,  Point2D.Float position){
		if (!records.containsKey(id)){
			records.put(id, new CursorDownRecord(id, time, distance, position));
		}
	}
	
	public void unregisterCursor(long id){
		if (records.containsKey(id))
			records.remove(id);
	}
	
	private class CursorDownRecord {
		public long id;
		public long pressTime;
		public long currentTime;
		public Point2D.Float pressPosition;
		public Point2D.Float currentPosition;
		public long time;
		public float distance;
		
		public CursorDownRecord(long id, long time, float distance, Point2D.Float position) {
			this.id = id;
			this.pressTime = System.nanoTime();
			this.pressPosition = position;
			this.currentPosition = (Point2D.Float)this.pressPosition.clone();
			
			this.time = time;
			this.distance = distance;
		}
		
		public void reset(){
			this.pressTime = System.nanoTime();
			this.pressPosition.x = currentPosition.x;
			this.pressPosition.y = currentPosition.y;
		}
		
		private boolean isMoveAwayFromOrigin(){
			return !this.isCloseEnough(this.currentPosition, this.pressPosition);
		}
		
		public boolean isLongHeld(){
			this.currentTime = System.nanoTime();
			if (isMoveAwayFromOrigin()){
				this.reset();
				return false;
			}
			else{
				if (currentTime-pressTime>=time*1000000){
					this.reset();
					return true;
				}
				else{				
					return false;
				}
			}
		}
		
		private boolean isCloseEnough(Point2D.Float a, Point2D.Float b) {
			return Point2D.distance(a.x, a.y, b.x, b.y) < distance;
		}
		
		public String toString() {
			return this.getClass().getName() + ":" + id;
		}
	}
}
