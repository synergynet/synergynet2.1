package synergynet.table.apps.lightbox;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ConcurrentLinkedQueue;

import synergynet.services.ServiceManager;
import synergynet.services.exceptions.CouldNotStartServiceException;
import synergynet.services.net.networkcontent.NetworkContentClientService;

public class ImageLoader implements Runnable {

	private ConcurrentLinkedQueue<URL> urlsToLoad = new ConcurrentLinkedQueue<URL>();
	private ConcurrentLinkedQueue<URL> imagesToShow = new ConcurrentLinkedQueue<URL>();
	private boolean running;
	private NetworkContentClientService cs;

	public ImageLoader() throws CouldNotStartServiceException {
		cs = (NetworkContentClientService) ServiceManager.getInstance().get(NetworkContentClientService.class);
		Thread t = new Thread(this);
		t.start();
	}

	public void addPhotoToLoad(URL url) {
		urlsToLoad.add(url);	
	}

	@Override
	public void run() {
		running = true;
		while(running) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			URL url = urlsToLoad.poll();
			if(url != null) {
				try {
					load(url);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private void load(URL url) throws IOException {
		InputStream in = url.openStream();
		File tmp = File.createTempFile("snnil", "tmp");
		FileOutputStream fos = new FileOutputStream(tmp);
		byte[] buf = new byte[4 * 1024]; // 4K buffer
		int bytesRead;
		while ((bytesRead = in.read(buf)) != -1) {
			fos.write(buf, 0, bytesRead);
		}
		fos.close();
		
		URL newURL = cs.publishFile(tmp);
		imagesToShow.add(newURL);
		
		tmp.delete();
	}

	public URL getNextPhotoToShow() {
		URL next = imagesToShow.poll();
		return next;
	}
}
