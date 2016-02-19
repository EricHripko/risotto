package comp2541.bison.restaurant;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;

/**
 * Main class for the server. Here the server is launched and connected
 * with the HTTP data handler and the log handler.
 * 
 * @author Michele Cipriano
 *
 */
public class Main {
	
	/**
	 * The program starts from here.
	 * @param args Standard arguments.
	 */
	public static void main(String[] args) {
		// TODO Test the code
		try {
			RestaurantServer restaurantServer = new RestaurantServer(8080, new ExecutorThreadPool());
			
			HandlerCollection handlers = new HandlerCollection();
			//Handler to log requests
			RequestLogHandler requestLogHandler = new RequestLogHandler();
			RestaurantHandler restaurantHandler = new RestaurantHandler("restaurantdatabase.db");
			handlers.setHandlers(new Handler[]{requestLogHandler, restaurantHandler });
			restaurantServer.setHandler(handlers);
			
			//Creates log file for requests to the server
			NCSARequestLog requestLog = new NCSARequestLog("./restaurantserver.log");
			requestLog.setRetainDays(90);
			requestLog.setAppend(true);
			requestLog.setExtended(false);
			requestLog.setLogTimeZone("GMT");
			requestLogHandler.setRequestLog(requestLog);
			
			restaurantServer.start();
			restaurantServer.join();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}