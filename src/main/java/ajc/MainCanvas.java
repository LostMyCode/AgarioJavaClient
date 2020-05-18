package ajc;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class MainCanvas extends JPanel {
	private static final long serialVersionUID = 5570080027060608254L;
    private BufferedImage screen;
    Graphics2D ctx;
    
    public MainCanvas() {
    	super();
        screen = new BufferedImage(MainFrame.size.width, MainFrame.size.height, BufferedImage.TYPE_INT_RGB);
        setSize(MainFrame.size.width, MainFrame.size.height);
        setVisible(true);
        
        System.out.println(ctx);
    }

    public void render() {
        Graphics gra = screen.getGraphics();
        Graphics2D ctx = (Graphics2D) gra;
    	ctx.setBackground(Color.black);
        ctx.setColor(Color.black);
        ctx.fillRect(0, 0, MainFrame.size.width, MainFrame.size.height);
        
        ctx.setColor(new Color(0, 0, 0));
        ctx.fillRect(0, 0, 500, 500);
        
		repaint();
		//ctx.dispose();
		//System.out.println(1233333);
    }

    /* public int getStringWidth(Graphics2D ctx, String str) {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        
    } */
    
    static class GameCanvas extends Canvas {
    	
    }
}
