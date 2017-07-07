package synergynet.services.net.netservicediscovery;

import java.net.UnknownHostException;

import synergynet.services.ServiceManager;
import synergynet.services.exceptions.CouldNotStartServiceException;
import synergynet.services.net.filestore.FileStoreServer;
import synergynet.services.net.webserver.WebServerService;
import synergyspace.net.landiscovery.ServiceDescriptor;
import synergyspace.net.landiscovery.ServiceDiscoveryListener;
import synergyspace.net.peer.ServerStatusMonitor;

public class TestNetServiceDiscovery {
	public static void main(String[] args) throws CouldNotStartServiceException, UnknownHostException {
		NetworkServiceDiscoveryService nsds = (NetworkServiceDiscoveryService) ServiceManager.getInstance().get(NetworkServiceDiscoveryService.class);		
		
		nsds.getServiceDiscovery().registerListener(new ServiceDiscoveryListener() {
			@Override
			public void serviceAvailable(ServiceDescriptor descriptor) {
				System.out.println("Service available: " + descriptor.getServiceName());
				System.out.println("user data: " + descriptor.getUserData());
			}

			@Override
			public void serviceRemoved(ServiceDescriptor descriptor) {
				System.out.println("Service removed: " + descriptor.getServiceName());				
			}			
		});
		nsds.getServiceDiscovery().registerServiceForListening(WebServerService.SERVICE_TYPE, WebServerService.SERVICE_NAME);
		nsds.getServiceDiscovery().registerServiceForListening(FileStoreServer.SERVICE_TYPE, FileStoreServer.SERVICE_NAME);
		ServerStatusMonitor smon = new ServerStatusMonitor(WebServerService.SERVICE_TYPE, WebServerService.SERVICE_NAME, 3000);
		nsds.getServiceDiscovery().registerListener(smon);
		nsds.getServiceDiscovery().registerServiceForListening(WebServerService.SERVICE_TYPE, WebServerService.SERVICE_NAME);
		try {
			smon.connect();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(smon.serverFound());
	}
}
