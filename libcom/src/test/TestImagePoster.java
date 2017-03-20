package test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;

import org.junit.Test;

import libcom.ImagePoster;

/*
 * Il est normal que ce test retourne une exception, mais les tests devraient passer
 */

public class TestImagePoster {

	public static ServerSocket testSocket;
	
	@Test
	public void testImagePoster() {
	}

	@Test
	public void testPost() {
		int testPort =  3000 + (int) (Math.random() * 1000);
		
		ClassLoader classLoader = getClass().getClassLoader();
	    File file = new File(classLoader.getResource("test.jpg").getFile());
		ImagePoster imp = new ImagePoster(file);
		Thread t = null;
		
		try {
			testSocket = new ServerSocket(testPort);
			
			t = (new Thread() {
				public void run() {
					try {
						Socket s = testSocket.accept();
						InputStream is = s.getInputStream();
						
						byte[] b = new byte[10];

						assertNotEquals(
							"should receive data",
							-1, is.read(b, 0, 10)
						);
						s.close();
						testSocket.close();
					} catch (IOException e) {
						fail("socket should close in test");
						e.printStackTrace();
					}
					Thread.currentThread().interrupt();
				}
			});
			t.start();
			
			// Sleep a bit to allow server to start
			Thread.sleep(200);
		} catch (IOException e1) {
			e1.printStackTrace();
			fail("Should create test socket server");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		try {
			imp.post("http://localhost:"+testPort, "");
		} catch (URISyntaxException e) {
			fail("Should send post request");
		} catch (ConnectException e) {
			// Normal since we auto-reset the connection
			
		}
	}

}
