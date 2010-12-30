package PulsarBeta1;

import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

public class Attack extends GameObject {	
    public Rectangle r1 = new Rectangle();
    public Rectangle r2 = new Rectangle();
	
    public Attack(int strength, int xCoord, int yCoord, int velocityX, int velocityY) {
    	super();
		//Image[] img = new Image[1];   	
    	//img[0] = GameDisplay.fire1;
    	Image[] img = new Image[4];
    	img[0] = GameDisplay.star1;
    	img[1] = GameDisplay.star2;
    	img[2] = GameDisplay.star3;
    	img[3] = GameDisplay.star4;
    	
		anim = new Animation(img);
		
    	width = 68;
    	height = 33;
    	x = xCoord;
    	y = yCoord;
    	dx = velocityX;
    	dy = velocityY;
    }
    
	public boolean collidesWith(GameObject obj, Attack a) {
		r1.setBounds((int)obj.getX(),(int)obj.getY(),obj.getWidth(),obj.getHeight());
		r2.setBounds(x,y,width,height);
		return r1.intersects(r2);
	}
	public void updateAttack(){
		x+=dx;
		y+=dy;
		
		if(Game.map.isColliding(this,x, y)){alive = false;}
		
		if(Game.p.collidesWith(this)){
			Game.p.addHealth(-1);
			alive = false;
		}
		
		Monster tempM;
		for(int k = 0; k < Game.enemies.size(); k++){
			tempM = (Monster) Game.enemies.get(k);
			if(tempM.collidesWith(this)){
				alive = false;
				tempM.onHit(this);
			}
		}

	}
	public void sendWarnings(){
		int tempX = x;
		int tempY = y;
		boolean done = false;
		
		while(!done){
			if(Game.map.isColliding(this,tempX,tempY)){
				done = true;
			}
			
			r1.setBounds(Game.p.x,Game.p.y,Game.p.getWidth(),Game.p.getHeight());
			r2.setBounds(tempX,tempY,width,height);
			if(r1.intersects(r2)){ done = true; }
			
			Monster tempM;
			for(int k = 0; k < Game.enemies.size(); k++){
				tempM = (Monster) Game.enemies.get(k);
				r1.setBounds(tempM.x,tempM.y,tempM.getWidth(),tempM.getHeight());
				r2.setBounds(tempX,tempY,width,height);
				if(r1.intersects(r2)){
					done = true;
					tempM.inLineOfFire(this);
				}
			}
			
			tempX+=dx;
			tempY+=dy;
		}
		if(Game.p.collidesWith(this)){Game.p.addHealth(-1);}
	}
	
	// BETA TESTING METHOD. 
	public boolean willCollide(GameObject obj){
		int tempX = x;
		int tempY = y;
		boolean done = false;
		
		while(!done){
			if(Game.map.isColliding(this,tempX,tempY)){
				done = true;
			}
			r1.setBounds(Game.p.x,Game.p.y,Game.p.getWidth(),Game.p.getHeight());
			r2.setBounds(tempX,tempY,width,height);
			if(r1.intersects(r2)){ done = true; }
			
			Monster tempM;
			for(int k = 0; k < Game.enemies.size(); k++){
				tempM = (Monster) Game.enemies.get(k);
				r1.setBounds(tempM.x,tempM.y,tempM.getWidth(),tempM.getHeight());
				r2.setBounds(tempX,tempY,width,height);
				if(r1.intersects(r2)){
					done = true;
					tempM.inLineOfFire(this);
				}
			}
			tempX+=dx;
			tempY+=dy;
		}
		return false;
	}
		
//    public static int getMaxSpeed() {return 64;}
}