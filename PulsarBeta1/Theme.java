package PulsarBeta1;

import java.net.URL;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.util.ArrayList;


public class Theme{ // Default Theme

	ArrayList extras = new ArrayList();
	
	public Theme(){	Game.gd.loadTileImages(5,6); }
	
	public void addEnemy(char ch, int x, int y){
		if(ch == '%'){
			//Game.enemies.add(new Monster(x,y));
			orangeMushroom om = new orangeMushroom(x,y);
			Game.enemies.add(om);
		}
		else if(ch == '!'){Game.enemies.add(new AggressiveMonster(x,y));}
	}
	
	public void addExtra(String s){
		
		// Processes the input string to gain the necessary info
	    int x = -1;
    	int y = -1;
    	String xStr = "";
    	String yStr = "";
    	String img = "";
    	for(int k = 0; k < s.length(); k++){
    		char c = s.charAt(k);
    		if(c != ','){
        		if(x == -1){	xStr+=c;	}
        		else if(y == -1){yStr +=c;	}
        		else{img+=c;	}
    		}
    		else{
    			if(x == -1){	x = Integer.parseInt(xStr);	}
        		else if(y == -1){y = Integer.parseInt(yStr);   }
    		}
    	}

       	Image[] i = new Image[0];
		
		// If it's not a special case, initialize the Animation using the helper method
		if(img.equals("eye")){
			i = new Image[3];
			i[0] = new ImageIcon("images/eye1.png").getImage();
			i[1] = new ImageIcon("images/eye2.png").getImage();
			i[2] = new ImageIcon("images/eye3.png").getImage();
			Animation anim = new Animation(i);
			extras.add(new Extra(x,y,anim));
		}
		else{
			Animation a = extraHelper(img);
			if(a != null){
				extras.add(new Extra(x,y,a));
			}
		}
	}
	
	public Animation extraHelper(String name){
		try { 
			
			Image[] i = new Image[1];
			i[0] = new ImageIcon("images/"+name+".png").getImage();
			Animation anim = new Animation(i);
			return anim;
		}
		catch (Exception e) {
			System.out.println("Error in ExtraHelper");
		}
		return null;
	}
}

class fireTheme extends Theme{
	public fireTheme(){
		Game.gd.loadTileImages(3,4);
	}

	public void addEnemy(char ch, int x, int y){
		if(ch == '%'){Game.enemies.add(new Monster(x,y));}
		else if(ch == '!'){Game.enemies.add(new AggressiveMonster(x,y));}
	}
}

class fortTheme extends Theme{
	public fortTheme(){}
	
	public void addExtra(String s){}
}

class Extra{
	int x, y;
	Animation anim;
	
	public Extra(int startX, int startY, Animation a){
		anim = a;
		x = startX;
		y = startY;
	}
}


/*
 *	public Animation extraHelper(String name){
		boolean exists = false;
		String[] imageNames = {"tree","barrel","armyCabin1","armyBuilding1","cautionBox",
		"armyTruck","checkpoint","helicopter","mountedGun","crate1","crate2","purpleFlowers",
		"berryBushes1","berryBushes2","bushes2","largeMushrooms","smallMushrooms","bushes1",
		"tepee","statue1","statue2","fence1","tree1","tree2","grassyLayer"};
		for(int k = 0; k < imageNames.length; k++){
			System.out.println("Checking "+imageNames[k]+" with "+name);
			String tempName = (String) imageNames[k];
			if(tempName.equals(name)){
				System.out.println("Requested extra ("+name+") is valid");
				exists = true;
				Image[] i = new Image[1];
				i[0] = new ImageIcon(getClass().getClassLoader().getResource("images/"+name+".png")).getImage();
				Animation anim = new Animation(i);
				return anim;
			}
		}
		System.out.println("Requested extra is not valid");
		return null;		
	}
*/