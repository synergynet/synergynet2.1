package synergynet.table.apps.projectmanagement.component.ganttchart;

import java.awt.Color;

import com.jmex.awt.swingui.ImageGraphics;

import synergynet.contentsystem.items.utils.IDrawableContent;

public class TasksDrawableContent implements IDrawableContent{

	int rows=20;
	int length = 70*15;
	int height = 30;
	
	
	public TasksDrawableContent(int rows, int length, int height) {
		this.rows = rows;
		this.length = length;
		this.height = height;
	}

	
	@Override
	public void drawContent(Object drawingHandle) {
		
		ImageGraphics gfx = (ImageGraphics) drawingHandle;
		
		gfx.setColor(Color.WHITE);
		gfx.fillRect(0, 0, length, height*rows);
		
		gfx.setColor(Color.BLACK);
		
		for (int i=0; i<rows; i++){
			
			gfx.drawLine(0, 0+i*height-1, length, 0+i*height-1);
			
		}
		
	}

}
