package synergynet.contentsystem.items;


import javax.swing.JDesktopPane;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.implementation.interfaces.ISwingContainerImplementation;

public class SwingContainer extends Window implements ISwingContainerImplementation{

	private static final long serialVersionUID = -8809882603380004248L;

	public SwingContainer(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
	}

	@Override
	public JDesktopPane getJDesktopPane() {
		return ((ISwingContainerImplementation)this.contentItemImplementation).getJDesktopPane();
	}

}
