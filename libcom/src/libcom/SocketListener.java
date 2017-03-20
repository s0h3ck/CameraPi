package libcom;

/**
 * Recoit les messages envoyés par un socket
 */
public interface SocketListener{
	public void MessageReceived(String msg);
}
