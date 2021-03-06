package ajc;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.RenderingHints;
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
	long lastTimeRender = System.currentTimeMillis();
	boolean painting = false;
	BasicStroke defaultStroke = new BasicStroke(1);
	BasicStroke borderLength = new BasicStroke(50);

    public GameCanvas() {
    	addMouseListener(this);
    	addMouseWheelListener(this);
    	addMouseMotionListener(this);
    	setBackground(Color.black);
    	
    	cImage = new BufferedImage(MainFrame.size.width, MainFrame.size.height, BufferedImage.TYPE_INT_RGB);
    	ctx = (Graphics2D) cImage.getGraphics();
    	
    	ctx.setRenderingHint(
    			RenderingHints.KEY_ANTIALIASING,
    			RenderingHints.VALUE_ANTIALIAS_ON
        );
    	
    	ctx.setColor(Color.black);
    	ctx.fillRect(0, 0, MainFrame.size.width, MainFrame.size.height);
    	
    	repaint();
    	
    	Timer time = new Timer();
    	time.scheduleAtFixedRate(new DrawTask(), 100, 50);
    }
    
    public void update(Graphics g) { // override to fix flickering
		paint(g);
	}
    
    public void paint(Graphics g) {
    	ctx.clearRect(0, 0, MainFrame.size.width, MainFrame.size.height); // clear canvas
    	
    	ctx.setColor(new Color(77, 0, 0));
    	ctx.fillRect(0, 0, MainFrame.size.width, MainFrame.size.height);

    	//Game.cells.removeIf(i -> { return i.destroyed; });
    	for (Cell cell : Game.cells) {
    		cell.update(System.currentTimeMillis());
    	}
    	
    	cameraUpdate();
    	
    	ctx.setColor(Color.cyan);
    	
    	toCamera();
    	drawBackground();
    	drawBorders();
    	
    	for (Cell cell : Game.cells) {
    		cell.draw(ctx);
    	}
    	
    	fromCamera();
    	
    	g.drawImage(cImage, 0, 0, null);
    }
    
    public class DrawTask extends TimerTask {
    	public void run() {
            Point mouseP = getMouseLocation();
            double mouseX = mouseP.getX();
            double mouseY = mouseP.getY();
            
    		SocketHandler.send(
    			ClientPackets.mouseMove(
    				(int)((mouseX - MainFrame.size.width / 2) / Game.camera.scale + Game.camera.x),
    				(int)((mouseY - MainFrame.size.height / 2) / Game.camera.scale + Game.camera.y)
    			)
    		);
    		if (!Game.spawned) SocketHandler.send(ClientPackets.spawn("none"));
    		
    	}
    	
        private Point getMouseLocation() {
            Point location = MouseInfo.getPointerInfo().getLocation();
            Point locationOnScreen = getLocationOnScreen();
            int x = location.x - locationOnScreen.x;
            int y = location.y - locationOnScreen.y;
            return new Point(x, y);
        }
    }
    
    public void cameraUpdate() {
    	if (Game.myCellsIds.size() > 0) {
    		int x = 0, 
    			y = 0, 
    			s = 0, 
    			score = 0,
    			l = Game.myCellsIds.size();
    		boolean noIndex = false;
    		for (int id : Game.myCellsIds) {
    			int index = Game.indexOfCell((long) id);
    			if (index == -1) {
    				noIndex = true;
    				continue;
    			}
    			Cell cell = Game.cells.get(index);
    			score += (int)(cell.ns * cell.ns / 100);
    			x += cell.x;
    			y += cell.y;
    			s += cell.s;
    		}
    		if (!noIndex) {
    			camera.target.x = x / l;
        		camera.target.y = y / l;
        		camera.sizeScale = (int) Math.pow(Math.min(64 / s, 1), 0.4);
    		}
    		Game.camera.target.scale *= Game.camera.viewportScale;
        	Game.camera.target.scale *= Game.camera.userZoom;
        	Game.camera.x = (Game.camera.target.x + Game.camera.x) / 2;
        	Game.camera.y = (Game.camera.target.y + Game.camera.y) / 2;
    	} else {
    		//Game.camera.x += (Game.camera.target.x - Game.camera.x) / 20;
    		//Game.camera.y += (Game.camera.target.y - Game.camera.y) / 20; 
    		Game.camera.target.scale *= Game.camera.viewportScale;
        	Game.camera.target.scale *= Game.camera.userZoom;
    	}
    	
    	
    	Game.camera.scale += (Game.camera.target.scale - Game.camera.scale) / 9;
    	if (Game.camera.userZoom != 1) Game.camera.userZoom = 1;
    }
    
    public void drawBorders() {
    	ctx.setColor(new Color(200, 0, 0));
    	ctx.setStroke(borderLength);
    	ctx.drawRect(Game.border.left, Game.border.top, Game.border.width, Game.border.height);
    	ctx.setStroke(defaultStroke);
    }
    
    public void drawBackground() {
    	ctx.setColor(new Color(46, 46, 46));
    	ctx.fillRect(Game.border.left, Game.border.top, Game.border.width, Game.border.height);
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
