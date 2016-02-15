package comp2541.bison.restaurant;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.thread.ThreadPool;

/**
 * 
 * @author michelecipriano
 *
 */
public class RestaurantServer extends Server {

	/**
	 * 
	 * @param port
	 * @param pool
	 */
	public RestaurantServer(int port, ThreadPool pool) {
		// Uses a ThreadPool allow multiple threads.
		super(pool);
		
		// Add a ServerConnector with specified port to the server.
		ServerConnector serverConnector = new ServerConnector(this);
		serverConnector.setPort(port);
		this.addConnector(serverConnector);
	}
}
