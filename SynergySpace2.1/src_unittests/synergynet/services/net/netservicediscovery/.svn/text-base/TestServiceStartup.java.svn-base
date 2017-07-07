package synergynet.services.net.netservicediscovery;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

import synergynet.services.ServiceManager;
import synergynet.services.exceptions.CouldNotStartServiceException;
import synergyspace.net.landiscovery.ServiceDescriptor;

public class TestServiceStartup {
	public static void main(String[] args) throws CouldNotStartServiceException, IOException {
		NetworkServiceDiscoveryService nsds = (NetworkServiceDiscoveryService) ServiceManager.getInstance().get(NetworkServiceDiscoveryService.class);
		
		ServiceDescriptor s1 = new ServiceDescriptor();
		s1.setServiceAddress(InetAddress.getLocalHost());
		s1.setServicePort(1234);
		s1.setServiceType("SynergyNet");
		s1.setServiceName("WebServer");
		s1.setUserData("");

		ServiceDescriptor s2 = new ServiceDescriptor();
		s2.setServiceAddress(InetAddress.getLocalHost());
		s2.setServicePort(1235);
		s2.setServiceType("SynergyNet");
		s2.setServiceName("FileServer");
		s2.setUserData("");
		
		boolean running = true;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while(running) {
			String cmd = br.readLine();
			if(cmd.length() < 1) {
				System.exit(0);
			}
			
			if(cmd.equals("s1")) {
				nsds.getServiceAnnouncer().registerService(s1);
			}else if(cmd.equals("s2")) {
				nsds.getServiceAnnouncer().registerService(s2);
			}else if(cmd.equals("t1")) {
				nsds.getServiceAnnouncer().unregisterService(s1);
			}else if(cmd.equals("t2")) {
				nsds.getServiceAnnouncer().unregisterService(s2);
			}
		}
		
	}
}
