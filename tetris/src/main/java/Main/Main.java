package Main;

import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {
		JFrame window = new JFrame("Tetris Project");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		
		// Game Panel
		GamePanel gp = new GamePanel();
		window.add(gp);
		window.pack(); 
		
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
		gp.launchGame();
	}
}
