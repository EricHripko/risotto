package comp2541.bison.restaurant;

import org.eclipse.jetty.util.thread.ExecutorThreadPool;

/**
 * 
 * @author michelecipriano
 *
 */
public class Main {
	
	public static void main(String[] args) {
		// TODO Test the code
		try {
			RestaurantServer restaurantServer = new RestaurantServer(8080, new ExecutorThreadPool());
			restaurantServer.setHandler(new RestaurantHandler("nameofdatabase.db"));
			restaurantServer.start();
			restaurantServer.join();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
