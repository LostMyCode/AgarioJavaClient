package ajc;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame {
    private static long startTime = System.currentTimeMillis();
    private static long frames = 0;
    private static final long serialVersionUID = 3637327282806739934L;
    public GameCanvas canvas;
    public static double mouseX, mouseY;
    public static Dimension size = new Dimension(1100, 700);

    public MainFrame() {
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setSize(size);
    	setLocationRelativeTo(null);
    	//addMouseListener(new MouseEventListener());
        //addKeyListener(new KeyboardListener());
    	
    	canvas = new GameCanvas();
    	
    	JPanel pane = new JPanel();
    	getContentPane().add(pane);
    	
    	canvas.setPreferredSize(size);
    	pane.add(canvas);
    	
    	setVisible(true);
    }
    public void MainFrame2() { //unused
        setSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setPreferredSize(size);
        addMouseListener(new MouseEventListener());
        addKeyListener(new KeyboardListener());
        //canvas = new MainCanvas();
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Agario Java Client 2020");
        getContentPane().add(canvas);
        add(canvas);
        //pack();
        setVisible(true);
    }

    public void render() {
    	//System.out.println("Rendering canvas...");
        Point mouseP = getMouseLocation();
        mouseX = mouseP.getX();
        mouseY = mouseP.getY();
        frames++;

        if (System.currentTimeMillis() - startTime > 1000) {

        }
        if (canvas != null) canvas.repaint();
    }

    private Point getMouseLocation() {
        Point location = MouseInfo.getPointerInfo().getLocation();
        Point locationOnScreen = getLocationOnScreen();
        int x = location.x - locationOnScreen.x;
        int y = location.y - locationOnScreen.y - 24;
        return new Point(x, y);
    }
        
}
