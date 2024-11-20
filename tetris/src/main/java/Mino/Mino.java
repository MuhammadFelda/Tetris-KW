package Mino;

import java.awt.Color;
import java.awt.Graphics2D;

import Main.Handler;
import Main.PlayManager;

public class Mino {
	public Block b[] = new Block[4];
	public Block tempB[] = new Block[4];
	int autoDropCounter = 0;
	public int direction = 2;
	boolean leftCollision;
	boolean rightCollision;
	boolean bottomCollision;
	public boolean activeMino = true;
	public boolean deactivating;
	int deactiveCounter = 0;
	
	public void create(Color c ) {
		b[0] = new Block(c);
		b[1] = new Block(c);
		b[2] = new Block(c);
		b[3] = new Block(c);
		tempB[0] = new Block(c);
		tempB[1] = new Block(c);
		tempB[2] = new Block(c);
		tempB[3] = new Block(c);
	}
	
	public void setXY(int x, int y) {}
	public void updateXY(int direction) {
		
		checkRotateCollision();
		
		if(leftCollision == false && rightCollision == false && bottomCollision == false) {
			this.direction = direction;
			b[0].x = tempB[0].x;
			b[0].y = tempB[0].y;
			b[1].x = tempB[1].x;
			b[1].y = tempB[1].y;
			b[2].x = tempB[2].x;
			b[2].y = tempB[2].y;
			b[3].x = tempB[3].x;
			b[3].y = tempB[3].y;
		}
	}

	public void getDirection1() {}
	public void getDirection2() {}
	public void getDirection3() {}
	public void getDirection4() {}
	
	public void checkMovementCollision() {
		leftCollision = false;
		rightCollision = false;
		bottomCollision = false;

		checkStaticBlockCollision();
		
		// Check Left Collision
		for(int i = 0; i < b.length; i++) {
			if(b[i].x == PlayManager.left_x) {
				leftCollision = true;
			}
		}
		
		// Check Right Collision
		for(int i = 0; i < b.length; i++) {
			if(b[i].x + Block.SIZE == PlayManager.right_x) {
				rightCollision = true;
			}
		}
		
		// Check Bottom Collision
		for(int i = 0; i < b.length; i++) {
			if(b[i].y + Block.SIZE == PlayManager.bottom_y) {
				bottomCollision = true;
			}
		}
	}
	
	public void checkRotateCollision() {
		leftCollision = false;
		rightCollision = false;
		bottomCollision = false;
		
		checkStaticBlockCollision();
		
		// Check Left Collision
		for(int i = 0; i < b.length; i++) {
			if(tempB[i].x < PlayManager.left_x) {
				leftCollision = true;
			}
		}
		
		// Check Right Collision
		for(int i = 0; i < b.length; i++) {
			if(tempB[i].x + Block.SIZE > PlayManager.right_x) {
				rightCollision = true;
			}
		}
		
		// Check Bottom Collision
		for(int i = 0; i < b.length; i++) {
			if(tempB[i].y + Block.SIZE > PlayManager.bottom_y) {
				bottomCollision = true;
			}
		}
	}
	
	private void checkStaticBlockCollision() {
		for(int i = 0; i < PlayManager.staticBlock.size(); i++) {
			int targetX = PlayManager.staticBlock.get(i).x;
			int targetY = PlayManager.staticBlock.get(i).y;
			
			// Check staticBlock Collision (Down)
			for(int j = 0; j < b.length; j++) {
				if(b[j].y + Block.SIZE == targetY && b[j].x == targetX) {
					 bottomCollision = true;
				}

				// Check staticBlock Collision (Left)
				for(int l = 0; l < b.length; l++) {
					if(b[l].x - Block.SIZE == targetX && b[l].y == targetY) {
						leftCollision = true;
					}
				}
				
				// Check staticBlock Collision (Right)
				for(int k = 0; k < b.length; k++) {
					if(b[k].x + Block.SIZE == targetX && b[k].y == targetY) {
						rightCollision = true;
					}
				}
			}
		}
	}
	
	public void update() {
		if(deactivating) {
			deactivating();
		}
		
		if(Handler.rotateButton) {

			switch(direction) {
			case 1: 
				getDirection1(); 
				break;
			case 2: 
				getDirection2(); 
				break;
			case 3:
				getDirection3(); 
				break;
			case 4: 
				getDirection4(); 
				break;
			}
			Handler.rotateButton = false;
		}
		
		checkMovementCollision();

		if(Handler.downButton) {
			if(bottomCollision == false) {
				b[0].y += Block.SIZE;
				b[1].y += Block.SIZE;
				b[2].y += Block.SIZE;
				b[3].y += Block.SIZE;
				
				autoDropCounter = 0;
				Handler.downButton = false;
			}
		}
		
		if(Handler.rightButton) {
			if(rightCollision == false) {
				b[0].x += Block.SIZE;
				b[1].x += Block.SIZE;
				b[2].x += Block.SIZE;
				b[3].x += Block.SIZE;
				
				autoDropCounter = 0;
				Handler.rightButton = false;
			}
		}
		
		if(Handler.leftButton) {
			if(leftCollision == false) {
				b[0].x -= Block.SIZE;
				b[1].x -= Block.SIZE;
				b[2].x -= Block.SIZE;
				b[3].x -= Block.SIZE;
				
				autoDropCounter = 0;
				Handler.leftButton = false;
			}
		}
		
		System.out.println(bottomCollision);
		if(bottomCollision) {
			deactivating = true;
		} else {
			autoDropCounter++;
			if(autoDropCounter == PlayManager.dropInterval) {
				b[0].y += Block.SIZE;
				b[1].y += Block.SIZE;
				b[2].y += Block.SIZE;
				b[3].y += Block.SIZE;
				autoDropCounter = 0;
			}
		}
	}
	
	private void deactivating() {
		deactiveCounter++;
		
		if(deactiveCounter == 45) {
			deactiveCounter = 0;
			checkMovementCollision();
			
			if(bottomCollision) {
				activeMino = false;
			}
		}
	}

	public void draw(Graphics2D g2) {
		int margin = 2;
		g2.setColor(b[0].c);
		g2.fillRect(b[0].x + margin, b[0].y + margin, Block.SIZE - (margin * 2), Block.SIZE - (margin * 2));
		g2.fillRect(b[1].x + margin, b[1].y + margin, Block.SIZE - (margin * 2), Block.SIZE - (margin * 2));
		g2.fillRect(b[2].x + margin, b[2].y + margin, Block.SIZE - (margin * 2), Block.SIZE - (margin * 2));
		g2.fillRect(b[3].x + margin, b[3].y + margin, Block.SIZE - (margin * 2), Block.SIZE - (margin * 2));
	}
}
