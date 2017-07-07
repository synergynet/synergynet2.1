package synergynet.table.apps.researchpuzzle.synergycomponent.slicewrapper;

import java.awt.Color;

import synergynet.contentsystem.items.ContentItem;
import synergynet.contentsystem.items.QuadContentItem;
import synergynet.contentsystem.items.listener.ItemListener;
import synergynet.table.apps.researchpuzzle.synergycomponent.annotatewrapper.HighlightWrapper;

public class SelectionWrapper extends HighlightWrapper{

	public SelectionWrapper(final QuadContentItem quadItem, QuadContentItem rootItem, int textureId) {
		super(quadItem, rootItem);
		this.setColor(Color.black);
		this.setStrokeWidth(1);
		quadItem.addItemListener(new ItemListener(){

			@Override
			public void cursorChanged(ContentItem item, long id, float x,
					float y, float pressure) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void cursorClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void cursorDoubleClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void cursorLongHeld(ContentItem item, long id, float x,
					float y, float pressure) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void cursorPressed(ContentItem item, long id, float x,
					float y, float pressure) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void cursorReleased(ContentItem item, long id, float x,
					float y, float pressure) {
				lastPoint.clear();
				drawGfx.setBackground(Color.white);
				drawGfx.clearRect(0, 0, (int)quadItem.getWidth(), (int)quadItem.getHeight());
				SelectionWrapper.this.drawingFinished();
			}

			@Override
			public void cursorRightClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				// TODO Auto-generated method stub
				
			}
		});
	}

}
