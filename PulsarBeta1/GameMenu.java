package PulsarBeta1;

import java.util.ArrayList;
import java.awt.*;
import java.awt.Graphics;


public class GameMenu{
	ArrayList options = new ArrayList<GameOption>();
	int pointer = 0;
	static Image background;
	boolean isDisplaying = false;
	
	public GameMenu(){
	}
	public void addOption(GameOption opt){
		options.add(opt);
	}
	
	GameOption temp;
	public void listen(){
		if(Game.keyBoard.left){
			temp = (GameOption) options.get(pointer);
			temp.onLeft();
		}
		
		if(Game.keyBoard.right){
			temp = (GameOption) options.get(pointer);
			temp.onRight();
		}
		
		if(Game.keyBoard.up){
			pointer--;
			if(pointer < 0){
				pointer = options.size()-1;
			}
		}
		
		if(Game.keyBoard.down){
			pointer++;
			if(pointer > options.size()-1){
				pointer=0;
			}
		}
		
		if(Game.keyBoard.enter){
			temp = (GameOption) options.get(pointer);
			temp.onEnter();
		}
	}
	
	public static void setBackground(Image back){
		background = back;
	}
	
	public void drawOptions(Graphics g){		
		if(background != null){
			g.drawImage(background, 0,0,null);
		}
		g.setColor(new Color(0,0,0,100));
		g.fillRect(250,50, 300, 50+(50*options.size()));
		
		g.setColor(new Color(255,0,0,100));
		g.fillRect(250, 50 * (pointer+1)+25, 300,50);

		
		g.setColor(Color.white);
		int yCoord = 100;
		GameOption temp;
		for(int k = 0; k < options.size(); k++){
			temp = (GameOption)options.get(k);
			temp.drawOption(g,300,yCoord+12);
			yCoord+=50;
		}
		g.setColor(Color.black);
	}
}

// Default option is for a boolean true or false/Yes or No option
class GameOption{
	int option;
	String optionName = "NO OPTION SET";
	
	public GameOption()	{int opt = 0;}
	public GameOption(int opt){
		option = opt;
		if(option == 1){
			optionName = "New Game";
		}
		else if(option == 2){
			optionName = "Load Game";
		}
		else if(option == 3){
			optionName = "High Score";
		}
		else if(option == 4){
			optionName = "Settings";
		}
		else if(option == 5){
			optionName = "Exit";
		}
		else if(option == 6){
			optionName = "Main Screen";
		}
	}
	public void onRight(){}
	public void onLeft(){}
	public void onEnter(){
		if(option == 0){	// Other Option
			System.out.println("Unknown Option Error");
		}
		else if(option == 1){	// New Game
			Game.activeMenu.isDisplaying = false;
		}
		else if(option == 2){	// Load Game
			
		}
		else if(option == 3){	// High Score
			
		}
		else if(option == 4){	// Settings
			Game.activeMenu.isDisplaying = false;
			Game.activeMenu = Game.gameSettings;
			Game.gameSettings.isDisplaying = true;
		}
		else if(option == 5){	// Exit
			Game.exit();
		}
		else if(option == 6){
			Game.activeMenu.isDisplaying = false;
			Game.activeMenu = Game.startingMenu;
			Game.startingMenu.isDisplaying = true;
		}
	}
	
	public void drawOption(Graphics g, int xCoord, int yCoord){
		g.drawString(optionName, xCoord,yCoord);
	}
}

class booleanOption extends GameOption{
	boolean status;

	public booleanOption(boolean s, int opt){
		status = s;
		option = opt;
		
		if(option == 1){
			optionName = "Fullscreen?";
		}
		else if(option == 2){
			optionName = "Debugger?";
		}

	}
	
	public booleanOption(boolean s, String opt){
		status = s;
		option = 0;

		optionName = opt;
	}
	
	public void onRight(){status = !status;onEnter();}
	public void onLeft(){status = !status;onEnter();}
	
	public void onEnter(){
		if(option == 1){
			Game.fullscreen = status;
			Game.setFullscreen();
		}
		else if(option == 2){
			
		}
	}
		
	public void drawOption(Graphics g, int xCoord, int yCoord){
		g.drawString(optionName, xCoord,yCoord);
		int endOfString = optionName.length()*5;
		
		g.drawString("Yes", xCoord+endOfString+20, yCoord);
		g.drawString("No", xCoord+endOfString+75, yCoord);
		
		if(status == true){
			g.drawRect(xCoord+endOfString+10, yCoord-38,50,50);
		}
		else{
			g.drawRect(xCoord+endOfString+60, yCoord-38,50,50);
		}
	}
}


class intOption extends GameOption{
	int selected = 0; 
	int max, min;
	public intOption(int MIN, int MAX, int startingSelection, int opt){
		max = MAX;
		min = MIN;
		selected = startingSelection;
		option = opt;
		
		if(option == 1){
			optionName = "Difficulty";
		}
		else if(option == 2){
			optionName = "Quality";
		}
		
	}
	
	public intOption(int MIN, int MAX, int startingSelection, String str){
		max = MAX;
		min = MIN;
		selected = startingSelection;
		optionName = str;
		option = 0;
	}
	
	public void onRight(){
		selected++;
		if(selected > max){selected = min;}
		onEnter();
	}
	
	public void onLeft(){
		selected--;
		if(selected < min){selected = max;}
		onEnter();
	}
	
	public void onEnter(){
		if(option == 1){
			
		}
		else if(option == 2){
			
		}
	}
	
	public void drawOption(Graphics g, int xCoord, int yCoord){
		g.drawString(optionName, xCoord,yCoord);
		
		int endOfString = optionName.length()*5+10;
		for(int k = min; k <= max; k++){
			g.drawString(""+k, xCoord+endOfString+(10*k), yCoord);
		}
		
		g.drawRect(xCoord+endOfString+(10*selected), yCoord-38, 10, 50);
	}

}