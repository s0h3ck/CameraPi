package test;

import static org.junit.Assert.*;

import org.junit.Test;

import libcom.CameraPiSocketClient;
import libcom.CameraPiSocketServer;
import libcom.SocketListener;

public class TestCameraPiSocket{
	
	private TEST_STATE step;
	
	private enum TEST_STATE {
		BEGIN,
		SERVER_SEND_MESSAGE,
		CLIENT_RECEIVE_MESSAGE,
		CLIENT_SEND_MESSAGE,
		SERVER_RECEIVE_MESSAGE,
		END
	};
	
	@Test
	public void testConnection() {
		try {
			step = TEST_STATE.BEGIN;
			
			CameraPiSocketServer cs = new CameraPiSocketServer(5004);
			Thread thread_ser = new Thread(cs);
			thread_ser.start();
			
			Thread.sleep(1000);
			
			CameraPiSocketClient cc = new CameraPiSocketClient("localhost",5004);
			Thread thread_cli = new Thread(cc);
			
			SocketListener sl_server = new SocketListener() {
				public void MessageReceived(String msg) {
					System.out.println("test server received : " + msg);
					assertEquals("Server does not receive right message", msg, "--test-serveur--");
					step = TEST_STATE.CLIENT_RECEIVE_MESSAGE;
					cs.send("--test-client--");
				}
			};
			
			SocketListener sl_client = new SocketListener() {
				public void MessageReceived(String msg) {
					System.out.println("test client received : " + msg);
					if(step == TEST_STATE.BEGIN){
						assertEquals("Client does not receive right message",msg, "--begin--");
						step = TEST_STATE.SERVER_SEND_MESSAGE;
						cc.send("--test-serveur--");
						step = TEST_STATE.SERVER_RECEIVE_MESSAGE;
					} else if(step == TEST_STATE.CLIENT_RECEIVE_MESSAGE){
						assertEquals("Client receives wrong message", msg, "--test-client--");
						step = TEST_STATE.END;
					}
				}
			};
			
			cs.addMessageListener(sl_server);			
			cc.addMessageListener(sl_client);
			
			thread_cli.start();
			
			Thread.sleep(500);
			
			System.out.println("Test completed");
		} catch (InterruptedException e) {
			fail("Message is interrupted");
			e.printStackTrace();
		}
	}

}
