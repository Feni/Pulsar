package PulsarBeta1;

import java.awt.Image;
import javax.swing.ImageIcon;

public class orangeMushroom extends Monster{
	public orangeMushroom(int xCoord, int yCoord){
		super(xCoord,yCoord);
				
	   	dx = getMaxSpeed();
    	x = xCoord;
    	y = yCoord;

		
    	Image[] walk = new Image[6];
		Image[] fly = new Image[1];
		Image[] stand = new Image[1];
    	Image[] fire = new Image[1];    	
    	
    	fire[0] = new ImageIcon("images/omstand1.png").getImage();
    	stand[0] = new ImageIcon("images/omstand1.png").getImage();
    	walk[0] = new ImageIcon("images/omwalk1.png").getImage();
    	walk[1] = walk[0];
        walk[2] = new ImageIcon("images/omwalk2.png").getImage();
        walk[3] = walk[2];
       	walk[4] = new ImageIcon("images/omwalk3.png").getImage();
       	walk[5] = walk[3];
       	fly[0] = walk[3];

		anim = new Animation(this, walk, fly, stand,fire);
		
		maxHealth = 15;
		maxBooster = 50;
		maxAmmo = 30;
		health = 5;
		booster = 50;
	    ammo = 15;
	}
}