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

package synergynet.table.apps.conceptmap.graphcomponents.link;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.LineItem;
import synergynet.contentsystem.items.OrthoContainer;
import synergynet.contentsystem.items.SimpleButton;
import synergynet.contentsystem.items.listener.SimpleButtonAdapter;
import synergynet.contentsystem.items.utils.Location;
import synergynet.table.apps.conceptmap.graphcomponents.OptionMessage;
import synergynet.table.apps.conceptmap.graphcomponents.GraphComponent.OptionMessageListener;
import synergynet.table.apps.conceptmap.graphcomponents.nodes.GraphNode;
import synergynet.table.apps.conceptmap.graphcomponents.nodes.KeyboardNode;
import synergynet.table.apps.conceptmap.graphcomponents.nodes.KeyboardNode.KeyboardListener;
import synergynet.table.apps.conceptmap.utility.GraphManager;
import synergynet.table.apps.conceptmap.utility.MessageFactory;

public class LinkMenu{
	
	private OrthoContainer container;
	private float location_x, location_y;
	private int fontSize = 10;
	private int borderSize = 4;
	private Color bgColour = Color.black;
	private Color textColour = Color.white;
	private Color borderColour = Color.LIGHT_GRAY;
	private Font font = new Font("Arial", Font.PLAIN, fontSize);
	private int itemWidth = 100;
	private int itemHeight = 20;
	private int distanceBetweenItems = 0;
	private boolean isVisible;
	private GraphLink link;
	private ArrayList<SimpleButton> menuButtons = new ArrayList<SimpleButton>();

	protected ContentSystem contentSystem;
	protected GraphManager graphManager;

	
	public LinkMenu(ContentSystem contentSystem, GraphManager gManager, GraphLink link){
		this.contentSystem = contentSystem;
		this.graphManager = gManager;
		this.link = link;
		container = (OrthoContainer)contentSystem.createContentItem(OrthoContainer.class);
		menuButtons = createLinkMenu(link);
		if(menuButtons != null){
			for(SimpleButton btn: menuButtons){
				container.addSubItem(btn);
			}
		}
	}
	
	private ArrayList<SimpleButton> createLinkMenu(GraphLink item) {
		final GraphLink link = item;
		final SimpleButton closeBtn = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);
		closeBtn.setText("close");

		closeBtn.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x,	float y, float pressure) {
				if(LinkMenu.this.isVisible()){	
					System.out.println("Button clicked : close!");
					link.removeMenu();
				}
			}
		});
		
		final SimpleButton btn1 = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);
		btn1.setText("Annotate");
		btn1.setBorderSize(borderSize);
		btn1.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x,	float y, float pressure) {
				if(LinkMenu.this.isVisible()){	
					System.out.println("Button clicked : Annotate!");
					LinkMenu.this.showAndLinkKeyboard(link);
				}
			}
		});
		final SimpleButton btn2 = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);
		if(link.getLinkMode() == LineItem.CONNECTED_LINE)
			btn2.setText("Segment Line");
		if(link.getLinkMode() == LineItem.SEGMENT_LINE)
			btn2.setText("Connected Line");		
		btn2.setBorderSize(borderSize);
		btn2.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x,	float y, float pressure) {
				if(LinkMenu.this.isVisible()){	
					System.out.println("Button clicked : Connected!");
					if(link.getLinkMode() == LineItem.CONNECTED_LINE){
						link.setLinkMode(LineItem.SEGMENT_LINE);
						btn2.setText("Connected Line");
					}
					else if(link.getLinkMode() == LineItem.SEGMENT_LINE){
						link.setLinkMode(LineItem.CONNECTED_LINE);
						btn2.setText("Segment Line");
					}
					LinkMenu.this.setOrder(link.getOrder()+1);
				}
			}
		});
		final SimpleButton btn3 = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);
		if(link.getArrowMode() == LineItem.BIDIRECTIONAL_ARROWS)	
			btn3.setText("Arrow To Source");
		else if(link.getArrowMode() == LineItem.ARROW_TO_SOURCE)	
			btn3.setText("Arrow To Target");
		else if(link.getArrowMode() == LineItem.ARROW_TO_TARGET)	
			btn3.setText("No Arrows");
		else if(link.getArrowMode() == LineItem.NO_ARROWS)	
			btn3.setText("Bidirectional Arrows");
		
		btn3.setBorderSize(borderSize);
		btn3.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x,	float y, float pressure) {
				if(LinkMenu.this.isVisible()){	
					System.out.println("Button clicked : Arrows!");
					if(link.getArrowMode() == LineItem.BIDIRECTIONAL_ARROWS){
						link.setArrowMode(LineItem.ARROW_TO_SOURCE);
						btn3.setText("Arrow to Target");
					}
					else if(link.getArrowMode() == LineItem.ARROW_TO_SOURCE){
						link.setArrowMode(LineItem.ARROW_TO_TARGET);
						btn3.setText("No Arrows");
					}
					else if(link.getArrowMode() == LineItem.ARROW_TO_TARGET){
						link.setArrowMode(LineItem.NO_ARROWS);
						btn3.setText("Bidirectional Arrows");
					}
					else if(link.getArrowMode() == LineItem.NO_ARROWS){
						link.setArrowMode(LineItem.BIDIRECTIONAL_ARROWS);
						btn3.setText("Arrow to Source");
					}
					LinkMenu.this.setOrder(link.getOrder() + 1);
				}
			}
		});
		final SimpleButton btn4 = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);
		btn4.setText("Delete");

		btn4.setBorderSize(borderSize);
		btn4.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x,	float y, float pressure) {
				if(LinkMenu.this.isVisible()){	
					System.out.println("Button clicked : Delete!");	
					
					OptionMessage msg = MessageFactory.getInstance().createOptionMessage(contentSystem, graphManager,link, "Are you sure you want to delete this link?", MessageFactory.OK_CANCEL_MESSAGE);
					msg.setLocation(link.getLinkPoint().getLocation().x, link.getLinkPoint().getLocation().y);
					msg.setOrder(LinkMenu.this.getOrder()+1);
					link.addOptionMessageListener(new OptionMessageListener(){
	
						@Override
						public void messageProcessed(OptionMessage msg) {
							if(msg.getParentComponent().getName().equals(link.getName())){
								if(msg.getSelectedOption() == 0){
									msg.remove();
									graphManager.detachGraphNode(msg);
									link.remove();
									graphManager.detachGraphLink(link);
									LinkMenu.this.remove();
									if(link.getKeyboardNode() != null){
										link.getKeyboardNode().remove();
										graphManager.detachGraphNode(link.getKeyboardNode());										
									}
								}
								else if(msg.getSelectedOption() == 1){
									msg.remove();
									graphManager.detachGraphNode(msg);
								}
							}
							
						}});
				}
			}
		});
		menuButtons.add(closeBtn);
		menuButtons.add(btn1);
		menuButtons.add(btn2);
		menuButtons.add(btn3);
		menuButtons.add(btn4);
		
		this.setLocation(location_x, location_y);
		this.setTextColour(textColour);
		this.setBorderColour(borderColour);
		this.setBgColour(bgColour);
		this.setFont(font);
		this.setMenuItemSize(itemWidth, itemHeight);
		this.setVisible(false);
	
		return menuButtons;
	}
	
	public void setLocation(float x, float y){
		location_x = x;
		location_y = y;
		float shift = 0;
		for(SimpleButton btn: menuButtons){
			btn.setLocalLocation(x, y - shift);
			shift+= itemHeight + distanceBetweenItems;
		}
	}
	
	public Location getLocation(){
		return new Location(location_x, location_y, 0);
	}
	
	public void setBgColour(Color bgColour){
		this.bgColour = bgColour;
		for(SimpleButton btn: menuButtons)
			btn.setBackgroundColour(bgColour);
	}
	
	public void setBorderColour(Color borderColour){
		this.borderColour = borderColour;
		for(SimpleButton btn: menuButtons)
			btn.setBorderColour(borderColour);
	}
	
	public void setFont(Font font){
		this.font = font;
		for(SimpleButton btn: menuButtons)
			btn.setFont(font);
	}	
	
	public void setTextColour(Color textColour){
		this.textColour = textColour;
		for(SimpleButton btn: menuButtons)
			btn.setTextColour(textColour);
	}
	
	public void setMenuItemSize(int itemWidth, int itemHeight){
		this.itemWidth = itemWidth;
		this.itemHeight = itemHeight;
		for(SimpleButton btn: menuButtons){
			btn.setAutoFitSize(false);
			btn.setWidth(itemWidth);
			btn.setHeight(itemHeight);
		}
	}
	
	public void setVisible(boolean isVisible){
		this.isVisible = isVisible;
		if(menuButtons != null){
			for(SimpleButton btn: menuButtons){
				btn.setVisible(isVisible);
			}
		if(isVisible) setOrder(link.getOrder()+1);
		}
	}
	
	public boolean isVisible(){
		return isVisible;
	}
	
	
	public void remove(){
		contentSystem.removeContentItem(container);
	}
	
	private void showAndLinkKeyboard(final GraphLink link){
		
		if(link.getKeyboardNode() == null){
			final KeyboardNode keyboardNode = new KeyboardNode(contentSystem, graphManager);
			link.setKeyboardNode(keyboardNode);
			keyboardNode.setLinkButtonLocation(GraphNode.MIDDLE);
			keyboardNode.setCloseButtonLocation(GraphNode.TOP_LEFT_CORNER);
			keyboardNode.setLocation(link.getLinkPoint().getLocation().x, link.getLinkPoint().getLocation().y);
			keyboardNode.getLinkButton().setVisible(false);
			keyboardNode.setLinkable(false);
			keyboardNode.setScale(0.5f);
			keyboardNode.setOrder(link.getOrder()+1);
			keyboardNode.getCloseButton().addButtonListener(new SimpleButtonAdapter(){
				public void buttonClicked(SimpleButton b, long id, float x,	float y, float pressure) {
					link.setKeyboardNode(null);
				}				
			});	
			final GraphLink linkToKeyboard = new GraphLink(contentSystem, graphManager);
			GraphNode linkPoint = link.getLinkPoint();
			linkToKeyboard.setSourceNode(linkPoint);
			linkToKeyboard.setTargetNode(keyboardNode);
			linkToKeyboard.setLinkMode(LineItem.SEGMENT_LINE);
			linkToKeyboard.setArrowMode(LineItem.NO_ARROWS);
			linkToKeyboard.setMenuEnabled(false);
			LinkMenu.this.setVisible(false);
			keyboardNode.addKeyListener(new KeyboardListener(){
	
				public void keyPressed(KeyEvent evt) {
				}
	
				public void keyReleased(KeyEvent evt) {
					String text = link.getText();
					if(text == null) text ="";
					if(evt.getKeyChar() == KeyEvent.VK_ENTER){
						keyboardNode.remove();
						linkToKeyboard.remove();
						link.setMenuEnabled(true);
					}
					
					if(evt.getKeyChar() == KeyEvent.VK_BACK_SPACE){
						if(text.length() >0){
							text = text.substring(0,text.length()-1);
							link.setText(text);
						}
					}
					else if(evt.getKeyChar() != KeyEvent.VK_CAPS_LOCK){
						link.setText(text + evt.getKeyChar());
					}
				}});
				link.removeMenu();
			}
	}

	public String getName() {
		return container.getName();
	}

	public int getOrder() {
		return container.getOrder();
	}
	
	public void setOrder(int zOrder){
		container.setOrder(zOrder);
	}
}
