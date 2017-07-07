package synergynet.services.net.networkcontent;

import java.io.File;
import java.util.logging.Logger;

import synergynet.services.ServiceManager;
import synergynet.services.SynergyNetService;
import synergynet.services.exceptions.CouldNotStartServiceException;
import synergynet.services.exceptions.ServiceNotRunningException;
import synergynet.services.net.filestore.FileStoreServer;
import synergynet.services.net.webserver.WebServerService;
import synergynet.table.config.server.ServerConfigPrefsItem;

public class NetworkContentServerService extends SynergyNetService {

	private static final Logger log = Logger.getLogger(NetworkContentServerService.class.getName());
	private WebServerService wss;
	private FileStoreServer fss;

	@Override
	public boolean hasStarted() {
		return wss != null || fss != null;
	}

	@Override
	public void shutdown() {
		try {
			stop();
		} catch (ServiceNotRunningException e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void start() throws CouldNotStartServiceException {		
		ServiceManager services = ServiceManager.getInstance();
		ServerConfigPrefsItem srvConfig = new ServerConfigPrefsItem();
		
		String fileStoreDirectory = srvConfig.getWebDirectory();
		fss = (FileStoreServer) services.get(FileStoreServer.class);
		fss.setDirectory(new File(fileStoreDirectory));
		
		wss = (WebServerService) services.get(WebServerService.class);	
		log.info("NetworkContentServer service started");
	}

	@Override
	public void stop() throws ServiceNotRunningException {
		wss.stop();
		fss.stop();	
		log.info("NetworkContentServer service stopped");
	}

	@Override
	public void update() {}

}
