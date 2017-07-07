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

package synergyspace.jme.gfx.twod.pdfdocument;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.jmex.awt.swingui.ImageGraphics;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

import synergyspace.jme.gfx.twod.utils.GraphicsImageQuad;

public class PDFDocumentQuad extends GraphicsImageQuad {

	private static final long serialVersionUID = -1917876712882613515L;

	protected ImageGraphics gfx;
	protected PDFFile pdffile; 
	protected PDFPage page;

	private int imgWidth;

	private int imgHeight;
	
	public PDFDocumentQuad(String name, PDFFile pdffile, float height, int imgHeight) {
		super(
				name,
				pdffile.getPage(0).getAspectRatio() * height,
				height,
				(int)(pdffile.getPage(0).getAspectRatio() * imgHeight),
				imgHeight
				);
		
		this.imgWidth = (int)(pdffile.getPage(0).getAspectRatio() * imgHeight);
		this.imgHeight = imgHeight;
		gfx = getImageGraphics();
		gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		this.pdffile = pdffile;
		page = pdffile.getPage(0);	
		draw();
	}

	public PDFDocumentQuad(File f, String name, float width, float height, int imgWidth, int imgHeight) throws IOException {
		super(name, width, height, imgWidth, imgHeight);
		this.imgWidth = imgWidth;
		this.imgHeight = imgHeight;
		gfx = getImageGraphics();
		gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);		
		pdffile = getPDFFile(f);		
		page = pdffile.getPage(0);	
		draw();
	}
	
	public static PDFFile getPDFFile(File f) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(f, "r");
		FileChannel channel = raf.getChannel();
		ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
		return new PDFFile(buf);
	}

	protected float getAspectRatio() {
		return page.getAspectRatio();
	}

	protected void draw() {
		Rectangle rect = new Rectangle(0,0,
				(int)page.getBBox().getWidth(),
				(int)page.getBBox().getHeight());

		//generate the image
		Image img = page.getImage(
				rect.width, rect.height, //width & height
				rect, // clip rect
				null, // null for the ImageObserver
				true, // fill background with white
				true  // block until drawing is done
		);
		
		gfx.drawImage(img, 0, 0, imgWidth, imgHeight, rect.x, rect.y, rect.width, rect.height, null);
		updateGraphics();
	}
	
	public void gotoPage(int pagenum) {
		page = pdffile.getPage(pagenum);
		draw();
	}
	
}
