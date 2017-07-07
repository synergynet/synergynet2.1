package synergynet.services.net.netservicediscovery;

import synergynet.services.ServiceManager;
import synergynet.services.exceptions.CouldNotStartServiceException;
import synergynet.services.net.webserver.WebServerService;
import synergyspace.net.landiscovery.ServiceDiscoverySystem;
import synergyspace.net.peer.ServerStatusMonitor;

public class NetworkContentClientTest {
	public static void main(String[] args) throws CouldNotStartServiceException {
		NetworkServiceDiscoveryService nsds = (NetworkServiceDiscoveryService) ServiceManager.getInstance().get(NetworkServiceDiscoveryService.class);
		ServiceDiscoverySystem serviceDiscovery = nsds.getServiceDiscovery();
		ServerStatusMonitor smon = new ServerStatusMonitor(WebServerService.SERVICE_TYPE, WebServerService.SERVICE_NAME, 3000);
		serviceDiscovery.registerListener(smon);
		serviceDiscovery.registerServiceForListening(WebServerService.SERVICE_TYPE, WebServerService.SERVICE_NAME);

		try {
			smon.connect();
			boolean serverFound = smon.serverFound();
			if(!serverFound) {
				System.out.println("server not found");
			}else{
				System.out.println("server found");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
