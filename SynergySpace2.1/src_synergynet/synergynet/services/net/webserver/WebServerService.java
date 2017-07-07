package synergynet.services.net.webserver;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;

import synergynet.services.ServiceManager;
import synergynet.services.SynergyNetService;
import synergynet.services.exceptions.CouldNotStartServiceException;
import synergynet.services.exceptions.ServiceNotRunningException;
import synergynet.services.net.netservicediscovery.NetworkServiceDiscoveryService;
import synergynet.table.config.server.ServerConfigPrefsItem;
import synergyspace.net.landiscovery.ServiceAnnounceSystem;
import synergyspace.net.landiscovery.ServiceDescriptor;

public class WebServerService extends SynergyNetService {

	private static final Logger log = Logger.getLogger(WebServerService.class.getName());
	
	public static final String SERVICE_TYPE = "SynergyNet";
	public static final String SERVICE_NAME = "webserver";
	private Server server;
	private String directory;
	private ResourceHandler resource_handler = new ResourceHandler();
	private int port;

	public WebServerService() {
		ServerConfigPrefsItem serverConfig = new ServerConfigPrefsItem();
		this.directory = serverConfig.getWebDirectory();
		this.port = serverConfig.getPort();
	}
	
	@Override
	public boolean hasStarted() {
		return server.isRunning();
	}

	@Override
	public void shutdown() {
		try {
			stop();
		} catch (ServiceNotRunningException e) {
			log.warning(e.toString());
		}		
	}

	@Override
	public void start() throws CouldNotStartServiceException {
        server = new Server();
        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(port);
        server.addConnector(connector);
 
        resource_handler.setDirectoriesListed(true);
        resource_handler.setWelcomeFiles(new String[]{ "index.html" });
 
        resource_handler.setResourceBase(this.directory);
 
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { resource_handler, new DefaultHandler() });
        server.setHandler(handlers);
 
        try {
			server.start();
			log.info("WebServerService server started.");
			advertiseService();
		} catch (Exception e) {
			throw new CouldNotStartServiceException(this);
		}		
	}
	
	private void advertiseService() throws CouldNotStartServiceException {
		NetworkServiceDiscoveryService nsds = (NetworkServiceDiscoveryService) ServiceManager.getInstance().get(NetworkServiceDiscoveryService.class);				
		ServiceAnnounceSystem sa = nsds.getServiceAnnouncer();
		ServiceDescriptor s = new ServiceDescriptor();
		s.setServiceType(SERVICE_TYPE);
		s.setServiceName(SERVICE_NAME);
		try {
			s.setServiceAddress(InetAddress.getLocalHost());
			s.setUserData("http://" + InetAddress.getLocalHost().getHostAddress() + ":" + port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		s.setServicePort(port);		
		sa.registerService(s);
		log.info("WebServerService advertised.");
	}

	@Override
	public void stop() throws ServiceNotRunningException {
		try {
			server.stop();
			log.info("WebServerService server stopped");
		} catch (Exception e) {
			log.warning(e.toString());
		}
	}

	@Override
	public void update() {}

}
