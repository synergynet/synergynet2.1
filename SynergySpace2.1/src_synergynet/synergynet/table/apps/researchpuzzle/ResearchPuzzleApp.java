package synergynet.table.apps.researchpuzzle;

import java.io.File;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.Frame;
import synergynet.contentsystem.items.HQPDFViewer;
import synergynet.table.appregistry.ApplicationInfo;
import synergynet.table.appregistry.groupcontrol.GroupController;
import synergynet.table.apps.DefaultSynergyNetApp;
import synergynet.table.apps.researchpuzzle.synergycomponent.SynergyComponent;

public class ResearchPuzzleApp extends DefaultSynergyNetApp{

	private ContentSystem contentSystem;
	
	public ResearchPuzzleApp(ApplicationInfo info) {
		super(info);
	}

	@Override
	public void addContent() {
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);
		contentSystem.removeAllContentItems();
		new GroupController(this);
		
		HQPDFViewer pdf = (HQPDFViewer) contentSystem.createContentItem(HQPDFViewer.class);
		pdf.setPdfFile(new File("assets/resources/pdfs/WeSearch.pdf"));
		pdf.setWidth(350);
		pdf.setLocation(400, 300);
		new SynergyComponent(pdf);
		
		Frame mapFrame = (Frame) contentSystem.createContentItem(Frame.class);
		mapFrame.setWidth(350);
		mapFrame.setHeight(400);
		mapFrame.drawImage(ResearchPuzzleApp.class.getResource("images/Map.jpg"));
		mapFrame.setBorderSize(0);
		mapFrame.setLocation(300, 500);
		new SynergyComponent(mapFrame);
	}

	@Override
	public void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		if(contentSystem != null) contentSystem.update(tpf);
	}

}
