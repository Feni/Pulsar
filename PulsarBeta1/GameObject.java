package PulsarBeta1;

/*******************************************************************************
 *
 *The Game Object Class:
 *	The Game Object Class provides a layout for all game objects (such as the 
 *player, monsters and items) to follow. It also comes with some of the basic 
 *methods that all Objects of the Game will have. 
 * 
 *******************************************************************************/

 import java.awt.Image;
 import java.awt.Rectangle;
 import java.util.ArrayList;

 public abstract class GameObject{
     
    public Rectangle player = new Rectangle();  
    public Rectangle creature = new Rectangle();
//    public static GameDisplay gd = new GameDisplay();
    
	Animation anim;
    private int status;
 	public boolean onGround, bump, up, right, firing, collideX, collideY;
 	
 	public int x, y, dx, dy, lastdx, lastdy, width, height;
         
    public static int gravity = 5;
     
	public int maxHealth = 10;
	public int maxBooster = 50;
	public int maxAmmo = 25;
	public int health;
	public int booster;
    public int ammo;
    
    public int percentHealth = 100;
    public int percentBooster = 100;
    public int percentAmmo = 100;
    
   	boolean alive; 
	
	// History of all of the previous actions...
	ArrayList timeLine = new ArrayList();
	int currentPointer = 0;
    
    public GameObject() {
    	alive = true;
        status = 1;
        onGround = false;
        bump = false;
         
        collideX = false; 
        collideY = false;
         
        width = 25;
  		height = 100;
        health = maxHealth;
        booster = maxBooster;
        ammo = maxAmmo; 
        timeLine.add("Start");
        for(int k = 0; k < 24; k++){
        	timeLine.add("noAction");
        }
        currentPointer++;
     }
     public int getStatus() {  return status; }
     public void setStatus(int status) {
         if (this.status != status) {
             this.status = status;
             if (status== 1){ wakeUp(); }
             else if (status == 0) {
                 setVelocityX(0);
                 setVelocityY(0);
             }
         }
     }
     int atkdX, atkdY;
     int atkX;
     public Attack initiateAttack(){
		
     	String lastAction = (String) timeLine.get(currentPointer-1);
//     	if(canDoEvent("Attack",5)){
     		addEventToLine("Attack");
     		
	     	if(ammo > 0){
     			ammo--;
		     	if(right){
		     		atkdX = 5;
		     		atkX = x+getWidth()+5;
		     	}
		     	else{
		     		atkdX = -5;
		     		atkX = x-(getWidth()/2)-5;
		     	}
		     	return new Attack(1, atkX, y+(getHeight()/2), atkdX, 0);
	     	}
     		
//     	}
//     	else{
  //   		addEventToLine("AttemptAttack");
    // 	}
     	
		return null;
     }
     
     public void addEventToLine(String occurance){
     	if(timeLine.size() >= 25){
     		timeLine.remove(0);
     		timeLine.add(currentPointer,occurance);
     	}
     	else{
     		timeLine.add(currentPointer,occurance);
     		currentPointer++;
     	}
     }
     
     // returns true if the action wasn't done in the given range
     public boolean canDoEvent(String action, int range){
     	for(int k = timeLine.size(); k >timeLine.size()-range;k--){
     		if(((String)timeLine.get(k)).equals(action)){	return true;	}
     	}
     	
     	return false;
     }
     
     int slopeAmountX = 5;
     int mapWidth, mapHeight, offsetX, offsetY;
     public Attack initiateAttack(int targetX, int targetY){
     	if(ammo > 0){
     		ammo--;

     		atkdX = (targetX)/100;
	     	if(targetX > x){atkX = x+getWidth()+5; 	}
	     	else {
	     		atkdX *= -1;
	     		atkX = x-(getWidth()/2)-5;
	     	}
	     	atkdY = (GameDisplay.screenHeight/100)-(targetY/100);
	     	if(targetY < y){ atkdY *= -1; }
	     	
	     	return new Attack(1, atkX, y+(getHeight()/2), atkdX, atkdY);
     	}
		return null;
    }
    int lastVeX, lastVeY;
      
	public void updateObject(){
		
		lastVeX = getlastdx();
    	lastVeY = getlastdy();
    	if (lastVeY>getVelocityY()){onGround=true;}
    	if (lastVeY<getVelocityY()){onGround=false;}
		
		if(dy < 0){addBooster(-1);}
		
		if(booster > 0 && dy < 0){
			dy-=gravity;	// Undo the Gravity while flying
		}
		else if (dy < 0){
			dy = 1;
		}
		
		dy+=gravity;

		if(dx > 0){	right = true; }
		else if(dx<0){right = false;}
		
		if(dy < 0){	up = true; }
		else if(dy > 0){up = false;	}
		
		int oldX = x;
		int newX = oldX + dx ;
		
		if(!Game.map.isColliding(this,newX, y)){collideX = false;x = newX;}
		else{collideX = true;}
		
		int oldY = y;
		int newY = oldY + dy;
		
		if(!Game.map.isColliding(this,x, newY)){collideY = false;y = newY;onGround = false;}
		else{
			collideVertical();
			collideY = true;
			if(Game.map.isColliding(this,x,y+16)){onGround = true;}
			else{onGround = false;}
		}
	}
     // Checks for collisions with the player by creating two rectangles 
     // representing the player and the crature and seeing if they collide.
     public boolean collidesWith(Player p,GameObject c) {      
         player.setBounds((int)p.getX(),(int)p.getY(),p.getWidth(),p.getHeight());
         creature.setBounds((int)c.getX(),(int)c.getY(),c.getWidth(),c.getHeight());
         return player.intersects(creature);
     }    
	 // Creature's maximum speed
     public int getMaxSpeed() {  return 2; }

     public void wakeUp() {
        setVelocityX(-getMaxSpeed());
        setVelocityY(getMaxSpeed());
     }

     public boolean isFlying() { return false; }
     
     /**
         Called before update() if the creature collided with a
         tile horizontally.
     */
     public void collideHorizontal() { setVelocityX(0); }
     public void collideVertical() { setVelocityY(0); } 
     public int getX() { return x; }
     public int getY() { return y; }
     public void setX(int newX) {  x = newX; }
     public void setY(int newY) {  y = newY; }
  	 
     /**
         Gets this Sprite's width, based on the size of the
         current image.
     */
     public int getWidth() { return anim.lastframe.getWidth(null); }
     public int getHeight() { return anim.lastframe.getHeight(null); }
     
     public int getVelocityX() { return dx; }
     public int getVelocityY() { return dy; }
  
     public void setVelocityX(int newDX) {
         if (getVelocityX()!=0){ lastdx=dx; }
         dx = newDX;
     }
     
     public int getlastdx() { return lastdx;  }
     public void setVelocityY(int newDY) {
         lastdy=dy;
         dy = newDY;
     }
     public int getlastdy() { return lastdy; }
     
    public void setMaxBooster(int i) { maxBooster = i;  }
    public void addBooster(int i) {
    	if (booster+i <= maxBooster && booster+i >= 0){booster+=i;}
	}
	
    public void setMaxAmmo(int i) { maxAmmo = i;  }
    public void addAmmo(int i) {
    	if (ammo +i <= maxAmmo && ammo+i >= 0){ammo+=i;}
	}
	
	public void setMaxHealth(int i) { maxHealth = i;  }
    public void addHealth(int i) {
    	if (health+i <= maxHealth){health+=i;}
    	if(health == 0){alive = false;	// NOTE: add capability to check if it's player
    		Game.p.kills++;
    	}
	}
	Rectangle i;
	public boolean collidesWith(GameObject obj){
		i = new Rectangle(x,y,getWidth(), getHeight());
		return i.intersects(new Rectangle(obj.x,obj.y, obj.getWidth(), obj.getHeight()));
	}
 }