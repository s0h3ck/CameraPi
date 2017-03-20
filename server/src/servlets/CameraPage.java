package servlets;


import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import libcom.CameraPiSocketServer;
import libcom.SocketListener;
import libdb.Image;
import libdb.SQLiteJDBC;
import libdb.TableImage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;


/**
 * Servlet implementation class LED
 */
@WebServlet("/camera")
public class CameraPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static int comm_port = 3080;
	private libcom.CameraPiSocketServer socketServer = null;
	
	public libcom.CameraPiSocketServer getSocketServer() {
		return socketServer;
	}

	public void setSocketServer(libcom.CameraPiSocketServer socketServer) {
		this.socketServer = socketServer;
	}

	private Thread thread_cli;
	private SQLiteJDBC db;
	
    public SQLiteJDBC getDb() {
		return db;
	}

	public void setDb(SQLiteJDBC db) {
		this.db = db;
	}

	private TableImage tableImage;
    private static boolean inited = false;
    private boolean isTesting = false;

	public boolean isTesting() {
		return isTesting;
	}

	public void setTesting(boolean isTesting) {
		this.isTesting = isTesting;
	}

	private boolean locked = false; // communique l'etat du systeme pour
									// recevoir des data
	
	public void init(){
		System.out.println("Server starting");
		if(inited == true){
			// Prevent tomcat from initing 2 times
			System.out.println("Server already started");
			return;

		}
		
		if(isTesting){
			inited = true;
			return;
		}
		
		// Init DB connection
		db = new SQLiteJDBC();
		// Connect to database
		db.connect();

		// Create table if it does not exist yet
		tableImage = new TableImage();
		db.createTable(tableImage);

		// Create socket listener
		socketServer = new CameraPiSocketServer(comm_port);
		Thread thread_ser = new Thread(socketServer);
		thread_ser.start();

		// Add a socket listener
		if (socketServer != null) {
			String fromClient;

			SocketListener sl_server = new SocketListener() {
				public void MessageReceived(String fromClient) {
					System.out.println("test server received : " + fromClient);
				}
			};
			socketServer.addMessageListener(sl_server);
		}
		
		inited = true;
	}

	public boolean isDBStarted(){
		if(isTesting){
			return true;
		}
		if(!inited || db == null){
			return false;
		}
		return true;
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response){
		String action = request.getParameter("action");
		
		// Don't accept requests until server is fully started
		if(!isDBStarted()){
			System.out.println("A request was made while the server was not ready.");
			return;
		}
		
		try{
			System.out.println(action);
			if (action == null) 
			{
				response.getWriter().append("no action specified");
				return;
			} else if (action.equals("mv-left")) {
				response.getWriter().append("btn : mv left");
				if (socketServer != null) 
				{
					socketServer.send("--mv-left--");
					System.out.println("mv left");
				}
			} else if (action.equals("mv-right")) {
				response.getWriter().append("btn : mv right");
				if (socketServer != null) 
				{
					socketServer.send("--mv-right--");
					System.out.println("mv right");
				}		
			} else if (action.equals("get-last")) {
				// If not yet connected, return empty string
				if(!db.isConnected()){
					response.getWriter().append("");
				} else {
					Image im = db.selectLast();
					if(im != null){
						JSONObject imobj = buildImageObj(im);
						response.getWriter().append(imobj.toString());
					} else {
						response.getWriter().append("");
					}
				}
			} else if (action.equals("list-date")) {
				// If not yet connected, return empty string
				if(!db.isConnected()){
					response.getWriter().append("");
				} else {
					String dateStr = request.getParameter("date");
					long dateInt = Long.parseLong(dateStr);
					Calendar c = Calendar.getInstance();
					c.setTimeInMillis(dateInt);
					Date date = c.getTime();
					JSONArray jobj = getDatePictures(date);
					response.getWriter().append(jobj.toString());
				}
			} else {
				response.getWriter().append("no path for action " + action);
			}
		} catch (IOException e){
			System.out.println("CameraPage IO exception - perhaps database problems");
		} catch (Exception e){
			System.out.println("CameraPage exception - perhaps database problems");
		}
	}
	
	/**
	 * Get the first moment of a day 
	 */
	public Date getStartOfDate(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		
		// Current day
		Calendar c1 = Calendar.getInstance();
		
		c1.set(
			c.get(Calendar.YEAR),
			c.get(Calendar.MONTH),
			c.get(Calendar.DATE)
		);
		
		// Reset time to start of day
		c1.set(Calendar.HOUR, 0);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.SECOND, 0);
		
		Date d1 = c1.getTime();
		
		return d1;
	}
	
	/**
	 * Get very end of a day
	 */
	public Date getEndOfDate(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		
		Calendar c2 = Calendar.getInstance();
		
		c2.set(
			c.get(Calendar.YEAR),
			c.get(Calendar.MONTH),
			c.get(Calendar.DATE)
		);

		// Reset time to start of day
		c2.set(Calendar.HOUR, 0);
		c2.set(Calendar.MINUTE, 0);
		c2.set(Calendar.SECOND, 0);
		
		// Find next day
		c2.add(Calendar.DATE, 1);
		
		Date d2 = c2.getTime();
		
		return d2;
	}
	
	
	/**
	 * all pictures for one day (UTC)
	 * @param date
	 * @return
	 */
	public JSONArray getDatePictures(Date date){
		// Object that will contain all images data
		JSONArray imagesArr = new JSONArray();
		
		Date d1 = getStartOfDate(date);
		Date d2 = getEndOfDate(date);
		
		Map<Integer, Image> images = db.select(d1, d2);
		
		// Create JSON array
		for(Image im: images.values()){
			JSONObject imobj = buildImageObj(im);
			// Add this object to the images array
			imagesArr.put(imobj);
		}
		
		return imagesArr;
	}
	
	
	/**
	 * 
	 * @param im the image from the database (libdb)
	 * @return a JSON object containing most images
	 */
	public JSONObject buildImageObj(Image im){
		JSONObject imobj = new JSONObject();
		imobj.put("filename", im.getFileName());
		imobj.put("date", im.getReceiveDate().getTime());
		imobj.put("jsonData", new JSONObject(im.getJSONData()));
		return imobj;
	}
	
	public void destroy(){
		System.out.println("Closing connection and socket server.");
		db.closeConnection();
		socketServer.close();
	}

}
