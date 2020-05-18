package ajc;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class MouseEventListener implements MouseListener, MouseWheelListener
{
	@Override
	public void mousePressed(MouseEvent e)
	{
		Game.pressMouse(e.getX(), e.getY(), e.getButton());				
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
		Game.mouseWheelMove(e.getWheelRotation());
	}
}