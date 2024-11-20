package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Handler implements KeyListener {
	public static boolean rotateButton;
	public static boolean downButton;
	public static boolean rightButton;
	public static boolean leftButton;
	public static boolean pauseButton; // Use Space key
	
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		
		if(code == KeyEvent.VK_R) {
			rotateButton = true;
		}
		
		if(code == KeyEvent.VK_S) {
			downButton = true;
		}
		
		if(code == KeyEvent.VK_D) {
			rightButton = true;
		}

		if(code == KeyEvent.VK_A) {
			leftButton = true;
		}
		
		if(code == KeyEvent.VK_SPACE) {
			if(pauseButton) {
				pauseButton = false;
			} else {
				pauseButton = true;
			}
		}
		
	}

	
}
