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

package synergynet.contentsystem.jme.items;

import java.nio.FloatBuffer;

import com.jme.scene.Spatial;
import com.jme.scene.TriMesh;
import com.jme.util.geom.BufferUtils;

import synergynet.contentsystem.items.ContentItem;
import synergynet.contentsystem.items.implementation.interfaces.shapes.ITwoDShape;
import synergynet.contentsystem.items.implementation.interfaces.shapes.TwoDShapeGeometry;
import synergynet.contentsystem.items.implementation.interfaces.shapes.TwoDShapeGeometry.ColourRGBA;
import synergynet.contentsystem.jme.items.utils.ShapeTriMesh;

public class JMETwoDShape extends JMEOrthoContentItem implements ITwoDShape {

	private ShapeTriMesh shapeTriMesh;

	public JMETwoDShape(ContentItem contentItem) {
		this(contentItem, new ShapeTriMesh());
	}
	
	public JMETwoDShape(ContentItem contentItem, Spatial spatial) {
		super(contentItem, spatial);
		this.shapeTriMesh = (ShapeTriMesh) this.spatial;
	}

	@Override
	public void setShapeGeometry(TwoDShapeGeometry geom) {
		shapeTriMesh.updateGeometry(geom);
		setColours(shapeTriMesh, geom.getColors());
		
		shapeTriMesh.updateGeometricState(0f, true);
	}
	
    public void setColours(TriMesh triMesh, ColourRGBA[] colours) {
    	FloatBuffer colorBuf = triMesh.getColorBuffer();
    	
        if (colorBuf == null)
            colorBuf = BufferUtils.createColorBuffer(triMesh.getVertexCount());

        colorBuf.rewind();
        int index = 0;
        ColourRGBA current;
        for (int x = 0, cLength = colorBuf.remaining(); x < cLength; x += 4) {
        	current = colours[index];
            colorBuf.put(current.r);
            colorBuf.put(current.g);
            colorBuf.put(current.b);
            colorBuf.put(current.a);
            index++;
        }
        colorBuf.flip();
    }


}


