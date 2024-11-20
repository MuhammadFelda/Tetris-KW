package Main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Random;

import Mino.Block;
import Mino.Mino;
import Mino.Mino_Bar;
import Mino.Mino_L1;
import Mino.Mino_L2;
import Mino.Mino_Square;
import Mino.Mino_T;
import Mino.Mino_Z1;
import Mino.Mino_Z2;

public class PlayManager {
	// Play Area
	final int WIDTH = 360;
	final int HEIGHT = 600;
	public static int left_x;
	public static int right_x;
	public static int top_y;
	public static int bottom_y;
	
	// Mino
	Mino currentMino;
	final int MINO_START_X;
	final int MINO_START_Y;
	
	Mino nextMino;
	final int NEXTMINO_X;
	final int NEXTMINO_Y;
	public static ArrayList<Block> staticBlock = new ArrayList<>();
	
	// Other
	public static int dropInterval = 60;
	boolean gameOver;
	
	// Efect
	boolean effectCounterOn;
	int effectCounter;
	ArrayList<Integer> effectY = new ArrayList<>();
	
	// Score and Level
	int level = 1;
	int lines;
	int score;
	
	public PlayManager() {
		left_x = (GamePanel.WIDTH / 2) - (WIDTH / 2);
		right_x = left_x + WIDTH;
		top_y = 50;
		bottom_y = top_y + HEIGHT;
		
		MINO_START_X = left_x + (WIDTH / 2) - Block.SIZE;
		MINO_START_Y = top_y + Block.SIZE;
		
		NEXTMINO_X = right_x + 175;
		NEXTMINO_Y = top_y + 500;
		
		// Set Start Mino
		currentMino = pickMino();
		currentMino.setXY(MINO_START_X, MINO_START_Y);
		nextMino = pickMino();
		nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);
	}
	
	public Mino pickMino() {
		Mino mino = null;
	    int i = new Random().nextInt(7);
	    
		switch(i) {
		case 0:
			mino = new Mino_L1();
			break;
		case 1:
			mino = new Mino_L2();
			break;
		case 2:
			mino = new Mino_Square();
			break;
		case 3:
			mino = new Mino_Bar();
			break;
		case 4:
			mino = new Mino_T();
			break;
		case 5:
			mino = new Mino_Z1();
			break;
		case 6:
			mino = new Mino_Z2();
			break;
		}
		
		return mino;
	}
	
	public void update() {
		if(currentMino.activeMino == false) {
			staticBlock.add(currentMino.b[0]);
			staticBlock.add(currentMino.b[1]);
			staticBlock.add(currentMino.b[2]);
			staticBlock.add(currentMino.b[3]);
			
			// Check if game over
			if(currentMino.b[0].x == MINO_START_X && currentMino.b[0].y == MINO_START_Y) {
				gameOver = true;
			}
			
			currentMino.deactivating = false;;
			
			currentMino = nextMino;
			currentMino.setXY(MINO_START_X, MINO_START_Y);
			nextMino = pickMino();
			nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);
			
			checkDelete();
		} else {
			currentMino.update();
		}
	}
	
	private void checkDelete() {
		int x = left_x;
		int y = top_y;
		int blockCount = 0;
		int lineCount = 0;
		
		while(x < right_x && y < bottom_y) {
			for(int i = 0; i < staticBlock.size(); i++) {
				if(staticBlock.get(i).x == x && staticBlock.get(i).y == y) {
					blockCount++;
				}
			}
			
			x += Block.SIZE;
			
			if(x == right_x) {
				if(blockCount == 12) {
					effectY.add(y);
					
					// Remove Line
					for(int i = staticBlock.size() - 1; i > -1; i--) {
						if(staticBlock.get(i).y == y) {
							staticBlock.remove(i);
						}
					}
					
					lineCount++;
					lines++;
					
					// Drop Speed
					if(lines % 10 == 0 && dropInterval > 1) {
						level++;
						if(dropInterval > 10) {
							dropInterval -= 10;
						} else {
							dropInterval -= 1;
						}
					}
					
					for(int j = 0; j < staticBlock.size(); j++) {
						if(staticBlock.get(j).y < y) {
							staticBlock.get(j).y += Block.SIZE;
						}
					}
				}
				
				blockCount = 0;
				x = left_x;
				y += Block.SIZE;
			}
		}
		
		if(lineCount > 0) {
			int singleLineScore = 10 + level;
			score += singleLineScore + lineCount;
		}
	}
	
	public void draw(Graphics2D g2) {
		// Play Area Frame
		g2.setColor(Color.white);
		g2.setStroke(new BasicStroke(4f));
		g2.drawRect(left_x - 4, top_y - 4, WIDTH + 8, HEIGHT + 8);
		
		// Mino Frame
		int x = right_x + 100;
		int y = bottom_y - 200;
		g2.drawRect(x, y, 200, 200);
		g2.setFont(new Font("Aria", Font.PLAIN, 30));
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.drawString("NEXT", x + 60, y + 60);
		
		// Score & Level
		g2.drawRect(x, top_y, 250, 300);
		x += 40;
		y = top_y + 90;
		g2.drawString("Level : " + level, x, y);
		y += 70;
		g2.drawString("Lines : " + lines, x, y);
		y += 70;
		g2.drawString("Score : " + score, x, y);
		
		
		// currentMino
		if(currentMino != null) {
			currentMino.draw(g2);
		}
		
		// Next Mino
		nextMino.draw(g2);
		
		for(int i = 0; i < staticBlock.size(); i++) {
			staticBlock.get(i).draw(g2);
		}
		
		// Effect
		if(effectCounterOn) {
			effectCounter++;
			
			g2.setColor(Color.red);
			for(int i = 0; i < effectY.size(); i++) {
				g2.fillRect(left_x, effectY.get(i), WIDTH, Block.SIZE);
			}
			
			if(effectCounter == 10) {
				effectCounterOn = false;
				effectCounter = 0;
				effectY.clear();
			}
		}
		
		// Game Over
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(50f));
		if(gameOver) {
			x = left_x + 25;
			y = top_y + 340;
			g2.drawString("GAME OVER", x, y);
		}
			
		// Pause
		g2.setColor(Color.yellow);
		g2.setFont(g2.getFont().deriveFont(50F));
		if(Handler.pauseButton) {
			x = left_x + 70;
			y = top_y + 320;
			g2.drawString("PAUSED", x, y);
		}
	}
}
