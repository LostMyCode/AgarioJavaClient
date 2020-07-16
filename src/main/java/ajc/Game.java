package ajc;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.extensions.Frame;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

public class Game {
    // static is needed?? (err in server packets)
    public static List<Cell> cells = new CopyOnWriteArrayList<Cell>();
    public static List<Cell> myCells = new CopyOnWriteArrayList<Cell>();
    public static long syncUpdStamp, syncAppStamp;
    public static SocketHandler socket;
    public static boolean mapCenterSet = false;
    public static Session session;
    public static boolean spawned = false;

    Game() {
        WebSocketClient client = new WebSocketClient();
        this.socket = new SocketHandler();
        try {
        	client.start();
        	URI serverURI = new URI("ws://127.0.0.1:9456");
        	ClientUpgradeRequest request = new ClientUpgradeRequest();
        	request.setHeader("Origin", "http://agar.io");
        	client.connect(socket, serverURI, request);
        	System.out.println("Connecting to server...");
        	this.socket.awaitClose(7, TimeUnit.DAYS);
        } catch (Throwable t) {}
        syncUpdStamp = syncAppStamp = System.currentTimeMillis();

    }
    
    public static void log(String ... args) {
    	String c = "";
    	for (String s : args) {
    		c += " " + s;
    	}
    	System.out.println(c);
    }

    static int indexOfCell(long id) {
        for (int i = 0; i < cells.size(); i++) {
            Cell cell = cells.get(i);
            if (cell.id == id) return i;
        }
        return -1;
    }
    
    public static void pressMouse(int x, int y, int button) {

    }
    
    public static void releaseMouse(int x, int y, int button) {

    }
    
    public static void mouseWheelMove(int d) {
    	if (camera.scale > 0.03 && d > 0) camera.userZoom = 0.8;
    	else if (camera.scale < 2 && d < 0) camera.userZoom = 1.2;
    	//camera.userZoom = Math.max(camera.userZoom, 0.1);
    	//camera.userZoom = Math.min(camera.userZoom, 4);
    }
    
    public static class camera {
    	public static double x, y;
    	public static int viewportScale = 1;
    	public static double userZoom = 1;
    	public static int sizeScale = 1;
    	public static double scale = 1;
    	public static class target {
    		public static double x, y;
    		public static double scale = 1;
    	}
    }
    
    public static class border {
    	public static double left = -2000;
    	public static double right = 2000;
    	public static double top = -2000;
    	public static double bottom = 2000;
    	public static double width = 4000;
    	public static double height = 4000;
    	public static double centerX = -1, centerY = -1;
    }
    
}