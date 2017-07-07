package synergynet.services.net.filestore;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import synergynet.services.ServiceManager;
import synergynet.services.SynergyNetService;
import synergynet.services.exceptions.CouldNotStartServiceException;
import synergynet.services.exceptions.ServiceNotRunningException;
import synergynet.services.net.filestore.messages.EndFileTransfer;
import synergynet.services.net.filestore.messages.FilePart;
import synergynet.services.net.filestore.messages.FileTransferComplete;
import synergynet.services.net.filestore.messages.StartFileTransfer;
import synergynet.services.net.netservicediscovery.NetworkServiceDiscoveryService;
import synergyspace.net.landiscovery.ServiceDescriptor;
import synergyspace.net.landiscovery.ServiceDiscoverySystem;
import synergyspace.net.peer.ServerStatusMonitor;
import synergyspace.utils.crypto.CryptoUtils;

public class FileStoreClient extends SynergyNetService implements Runnable {

	private static final Logger log = Logger.getLogger(FileStoreClient.class.getName());
	
	private Socket socket;

	private InetAddress address;

	public FileStoreClient() {

	}

	public void start() throws CouldNotStartServiceException {		
		NetworkServiceDiscoveryService nsds = (NetworkServiceDiscoveryService) ServiceManager.getInstance().get(NetworkServiceDiscoveryService.class);
		ServiceDiscoverySystem serviceDiscovery = nsds.getServiceDiscovery();
		ServerStatusMonitor smon = new ServerStatusMonitor(FileStoreServer.SERVICE_TYPE, FileStoreServer.SERVICE_NAME, 3000);
		serviceDiscovery.registerListener(smon);
		serviceDiscovery.registerServiceForListening(FileStoreServer.SERVICE_TYPE, FileStoreServer.SERVICE_NAME);

		try {
			smon.connect();
			boolean serverFound = smon.serverFound();
			if(!serverFound) {
				log.severe("Could not find file store server!");
			}else{
				ServiceDescriptor found = smon.getServerServiceDescriptor();
				address = found.getServiceAddress();
				
				Thread t = new Thread(this);
				t.start();
				log.info("Ready.");
			}
		} catch (InterruptedException e) {
			log.warning(e.toString());
		}
	}
	
	public String sendFile(File file) throws IOException {
		socket = new Socket(address, FileStoreServer.TCP_PORT);
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		try {
			StartFileTransfer sft = new StartFileTransfer();
			sft.setFileName(file.getName());
			String md5 = CryptoUtils.md5(file);
			sft.setMD5(md5);
			oos.writeObject(sft);
			
			byte[] buf = new byte[8 * 1024];
			FileInputStream fis = new FileInputStream(file);
			int read;
			while((read = fis.read(buf)) != -1) {
				FilePart fp = new FilePart();
				fp.setBytes(buf, read);
				oos.writeObject(fp);
			}
			oos.writeObject(new EndFileTransfer());
			
			try {
				FileTransferComplete ftc = (FileTransferComplete) ois.readObject();
				if(ftc != null) {
					log.info("File transfer completed.");
				}
				socket.close();
				log.info("Disconnected from file store server");
			} catch (ClassNotFoundException e) {
				log.warning(e.toString());
			}
			return md5;
		} catch (NoSuchAlgorithmException e) {
			log.warning(e.toString());
		}
		return null;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean hasStarted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() throws ServiceNotRunningException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

}
