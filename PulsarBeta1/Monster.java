package PulsarBeta1;

import java.awt.Image;

public class Monster extends GameObject{
	public Monster(int xCoord, int yCoord){
	   	super();
	   	dx = getMaxSpeed();
    	x = xCoord;
    	y = yCoord;
    	
    	Image[] walk = new Image[3];
		Image[] fly = new Image[1];
		Image[] stand = new Image[1];
    	Image[] fire = new Image[1];    	
    	
    	fire[0] = GameDisplay.gule1;
    	stand[0] = GameDisplay.gule1;
    	walk[0] = GameDisplay.gule1;
        walk[1] = GameDisplay.gule2;
       	walk[2] = GameDisplay.gule3;
       	fly[0] = GameDisplay.gule1;

		anim = new Animation(this, walk, fly, stand,fire);
		
		maxHealth = 10;
		maxBooster = 20;
		maxAmmo = 6;
		health = 5;
		booster = 50;
	    ammo = 5;
	}
	int counter = 0;
	
	public void makeMove(){
		if(collideX){
			onXBump();
		}
		else{
			if(right){
				dx = getMaxSpeed();
			}
			else{
				dx = -1*getMaxSpeed();
			}
		}
		if(collideY){
			onYBump();
		}
	}
	
	// When it collides an object
	public void onXBump(){
		dx *= -1;
		collideX = false;
		
		if((booster/maxBooster)*100 > 50){	// atleast 50% booster
			if(dy == 0){ dy = getMaxSpeed();	}
			else{ dy*=-1;}			
		}

		
		
		right = !right;
	}
	
	public void onYBump(){
		dy *= -1;
		
		collideY = false;
		up = !up;		// Sets to opposite
	}
	
	public int getMaxSpeed() {return 3;}
	
	// When it gets hit by an attack
	public void onHit(Attack atk){
		addHealth(-1);
//		System.out.println("HIT! "+health);
		dx *= -1;
	}
	
	// When an attack has been launched against it
	public void inLineOfFire(Attack atk){

		if((booster/maxBooster)*100 > 50){	// atleast 50% booster
			if(dy == 0){ dy = getMaxSpeed();	}
			else{ dy*=-1;}			
		}
		
		if(Game.p.x > x){	// Faces the attack/attacker
			dx = getMaxSpeed();
			right = true;
		}
		else{
			dx = -1* getMaxSpeed();
			right = false;
		}
		
		Attack temp = initiateAttack();
		if(temp != null){
			Game.attacks.add(temp);
			temp.sendWarnings();
		}
		
		
	}
	
	// When an attack has successfully hit the enemy(The player)
	public void onAttackSuccess(Attack atk){}
	
	// When an attack has failed to hit the player
	public void onAttackMiss(Attack atk){}
	
	// When the player enters the monster's line of sight
	public void onTargetSited(){}
}