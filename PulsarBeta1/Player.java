package PulsarBeta1;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Player extends GameObject {

	public int lastVeX,lastVeY;
	public int kills = 0;
	
    public Player(){
    	super();
    	x = 200;
    	y = 200;

    	Image[] walk = new Image[3];
		Image[] fly = new Image[3];
		Image[] stand = new Image[1];
    	Image[] fire = new Image[1];

    	fire[0] = GameDisplay.ironfire;
    	stand[0] = GameDisplay.ironstand;
    	//stand[0] = new ImageIcon(getClass().getClassLoader().getResource("images/character1.png")).getImage();
    	walk[0] = GameDisplay.ironwalk1;
        walk[1] = GameDisplay.ironwalk3;
       	walk[2] = GameDisplay.ironwalk2;
       	fly[0] = GameDisplay.ironjump1;
       	fly[1] = GameDisplay.ironjump2;
       	fly[2] = GameDisplay.ironjump3;

    	anim = new Animation(this, walk, fly, stand,fire);
    	
		maxHealth = 200;
		maxBooster = 250;
		maxAmmo = 50;
		health = 75;
		booster = 25;
	    ammo = 15;
	}
	
	public void updatePlayerInfo(){
	   	// Determines if the object is on ground using the previous velocity 
    	// and current velocity
		lastVeX = getlastdx();
    	lastVeY = getlastdy();
    	if (lastVeY>getVelocityY()){onGround=true;}
    	if (lastVeY<getVelocityY()){onGround=false;}
	}
	
	public void sendApprochWarnings(){
		Monster tempM;
		for(int k = 0; k < Game.enemies.size(); k++){
			tempM = (Monster) Game.enemies.get(k);
			if((tempM.y > y-10 || tempM.y < y+10) && (!(tempM.x > x-10) || !(tempM.x < x+10))){
				tempM.onTargetSited();
			}
		}
	}

	// Set high so that you can run through the maps fast and debug w/o wasting time
    public int getMaxSpeed() {return 4;}	
}