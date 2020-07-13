package ajc;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import ajc.Game.camera;

public class GameCanvas extends Canvas implements MouseListener, MouseWheelListener, MouseMotionListener, Runnable {
	private BufferedImage cImage;
	private Graphics2D ctx;
	private Thread th = null;
	long lastTimeRender = System.currentTimeMillis();
	boolean painting = false;

    public GameCanvas() {
    	addMouseListener(this);
    	addMouseWheelListener(this);
    	addMouseMotionListener(this);
    	setBackground(Color.black);
    	
    	cImage = new BufferedImage(MainFrame.size.width, MainFrame.size.height, BufferedImage.TYPE_INT_RGB);
    	ctx = (Graphics2D) cImage.getGraphics();
    	
    	ctx.setColor(Color.black);
    	ctx.fillRect(0, 0, MainFrame.size.width, MainFrame.size.height);
    	
    	repaint();
    	
    	Timer time = new Timer();
    	//time.scheduleAtFixedRate(new DrawTask(), 100, 30);

    }
    
    public synchronized void startGameLoop() {
    	if (th == null) {
    		th = new Thread(this);
    		th.start();
    	}
    }
    
    public synchronized void stopGameLoop() {
    	if (th != null) {
    		th = null;
    	}
    }
    	
    public void paint(Graphics g) {
    	if (painting) {
    		return;
    	}
    	painting = true;
    	//ctx = (Graphics2D) cImage.getGraphics();
    	//System.out.println(Game.camera.userZoom);
    	ctx.setColor(Color.black);
    	ctx.clearRect(0, 0, MainFrame.size.width, MainFrame.size.height);
    	ctx.setColor(Color.cyan);
    	//ctx.fillRect(0, 19, 100, 100);
    	Game.cells.removeIf(i -> { return i.destroyed; });
    	for (Cell cell : Game.cells) {
    		cell.update(System.currentTimeMillis());
    	}
    	
    	cameraUpdate();
    	
    	ctx.setColor(Color.cyan);
    	
    	toCamera();
    	drawBorders();
    	
    	//ctx.fillRect(0, 100 + (int)Math.random() * 20, 122, 100);
    	
    	for (Cell cell : Game.cells) {
    		//System.out.println(114514);
    		cell.draw(ctx);
    	}
    	
    	fromCamera();

    	//ctx.scale(Game.camera.userZoom, Game.camera.userZoom);
    	
    	g.drawImage(cImage, 0, 0, null);
    	//g.dispose();
    	painting = false;
    }
    
    public class DrawTask extends TimerTask {
    	public void run() {
    		repaint();
    		SocketHandler.send(ClientPackets.mouseMove((int)Game.border.centerX, (int)Game.border.centerY));
    		//if (!Game.spawned) SocketHandler.send(ClientPackets.spawn("none"));
    		
    	}
    }
    
    public void cameraUpdate() {
    	if (Game.myCells.size() > 0) {
    		
    	} else {
    		//Game.camera.x += (Game.camera.target.x - Game.camera.x) / 20;
    		//Game.camera.y += (Game.camera.target.y - Game.camera.y) / 20; 
    	}
    	Game.camera.target.scale *= Game.camera.viewportScale;
    	Game.camera.target.scale *= Game.camera.userZoom;
    	Game.camera.scale += (Game.camera.target.scale - Game.camera.scale) / 9;
    	if (Game.camera.userZoom != 1) Game.camera.userZoom = 1;
    }
    
    public void drawBorders() {
    	ctx.drawRect((int)Game.border.left, (int)Game.border.top, (int)Game.border.width, (int)Game.border.height);
    }
    
    public void toCamera() {
    	ctx.translate(MainFrame.size.width / 2, MainFrame.size.height / 2);
    	scaleForth();
    	ctx.translate(-Game.camera.x, -Game.camera.y);
    }
    
    public void scaleForth() {
    	ctx.scale(Game.camera.scale, Game.camera.scale);
    }
    
    public void scaleBack() {
    	ctx.scale(1 / Game.camera.scale, 1 / Game.camera.scale);
    }
    
    public void fromCamera() {
    	ctx.translate(Game.camera.x, Game.camera.y);
    	scaleBack();
    	ctx.translate(-MainFrame.size.width / 2, -MainFrame.size.height / 2);
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
    	if (System.currentTimeMillis() - lastTimeRender > 50) {
    		lastTimeRender = System.currentTimeMillis();
    		//repaint();
    	}
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {}
    
    @Override
	public void mousePressed(MouseEvent e)
	{
		Game.pressMouse(e.getX(), e.getY(), e.getButton());	
		//repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		Game.releaseMouse(e.getX(), e.getY(), e.getButton());		
	}
	
	@Override public void mouseClicked(MouseEvent e){}
	@Override public void mouseEntered(MouseEvent e) {}
	@Override public void mouseExited(MouseEvent e) {}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		//System.out.print(e.getScrollAmount());
		Game.mouseWheelMove(e.getWheelRotation());
		//repaint();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
