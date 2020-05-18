package ajc;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketFrame;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.extensions.Frame;

@WebSocket(maxTextMessageSize = 2^32)
public class SocketHandler {
	private final CountDownLatch closeLatch;
	ServerPackets handler = new ServerPackets();
	
	public SocketHandler() {
		this.closeLatch = new CountDownLatch(1);
	}
	
	public boolean awaitClose(int duration, TimeUnit unit) throws InterruptedException {
		return this.closeLatch.await(duration, unit);
	}
	
	@OnWebSocketClose
	public void onClose(int statusCode, String reason) {
		System.out.println("Websocket closed");
		this.closeLatch.countDown();
	}
	
	@OnWebSocketConnect
	public void onConnect(Session session) throws IOException {
		System.out.println("Connected!");
		session.getRemote().sendBytes(ClientPackets.handShake254());
		session.getRemote().sendBytes(ClientPackets.handShake255());
		Game.session = session;
	}
	
	@OnWebSocketFrame
	public void onPacket(Frame frame) {
		handler.handlePacket(frame.getPayload());
	}
	
	public static void send(ByteBuffer buffer) {
		try {
			if (Game.session != null) Game.session.getRemote().sendBytes(buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
