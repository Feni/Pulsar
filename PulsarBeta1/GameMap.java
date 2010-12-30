package PulsarBeta1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.ImageIcon;
import java.awt.Rectangle;


public class GameMap{
	public int width, height;//, offsetX, offsetY;
	public Image background;
	public Theme theme;
	public Image[][] tiles;
	public Tile[][] tileMap;
	
	Door leftDoor, rightDoor;

	public GameMap(){}
	
	public void updateMap()throws IOException{
		if(leftDoor != null && leftDoor.collidesWith(Game.p)){
			setLevel(leftDoor.location);
			Game.p.x = (int)rightDoor.i.getX()-42;
			Game.p.y = (int)rightDoor.i.getY();
		}
		else if(rightDoor != null && rightDoor.collidesWith(Game.p)){
			setLevel(rightDoor.location);
			Game.p.x = (int)leftDoor.i.getX()+42;
			Game.p.y = (int)leftDoor.i.getY();
		}
	}
	
	public void setLevel(String lvl) throws IOException{
		Game.attacks.clear();
    	Game.enemies.clear();
    	leftDoor = null;
    	rightDoor = null;
    	
    	File currentWorkingDirectory = new File (".");
    	
    	
		ArrayList <String>lines = new ArrayList<String>();
		String filename = currentWorkingDirectory.getCanonicalPath()+"/maps/" + lvl + ".txt";
		
		
		
		File mapFile = new File(filename);
		
		if (!mapFile.exists()) {throw new IOException("No such map: " + filename);}
		
		
		BufferedReader reader = new BufferedReader(new FileReader(mapFile));
		// There MUST be an extra line after the last line...
		while (true) {
			String line = reader.readLine();
			if (line == null) {	reader.close();break;}// exits while without going on
			if (line.startsWith("#")) {// add every line except for specil cases which define things
			System.out.println(line);
				if(line.startsWith("#background")){
					setBackground(new ImageIcon("images/"+line.substring(1,line.length() ) ).getImage());
				}
				else if(line.startsWith("#theme ")){
					setTheme(Integer.parseInt(line.substring(7,line.length())));
					System.out.println("Setting Theme");
				}
				else if(line.startsWith("#extra ")){
					String extra = line.substring(7,line.length());
					theme.addExtra(extra);
				}
			}
			else{
				lines.add(line);
				width = line.length();
			}
		}
		height = lines.size();
		System.out.println("New map created "+width+" X "+height);
		tiles = new Image[width][height];
		tileMap = new Tile[width][height];
		for(int y = 0; y < height; y++)
		{
			for(int x = 0; x < width; x++){	tileMap[x][y] = null;}
		}
		
		System.out.println("Setting the data");
		// Sets the data
		for(int y = 0; y < height; y++){
			String line = lines.get(y);
			for (int x=0; x<line.length(); x++) {
				char ch = line.charAt(x);
				int tile = ch - 'A';		// A=0,B=1, C=2 so on...
				
				
				System.out.println("Adding tile: "+ch);

				
				if (ch == 'I') {
					setTile(x, y,(Image)GameDisplay.tileSet.get(0));
					tileMap[x][y] = new Tile(true);
				}
				else if(ch == 'i'){
					setTile(x, y,(Image)GameDisplay.invisibleTile);
					tileMap[x][y] = new Tile(true);
				}
				else if(ch == 'O'){
					setTile(x,y,(Image)GameDisplay.tileSet.get(1));
					tileMap[x][y] = new Tile(true);
				}
				else if (ch == '@') {
					leftDoor = new Door(x*32,y*32,16,64,line.substring(x+1,x+4));
					x+=4;
				}
				else if (ch == '*') {
					rightDoor = new Door(x*32,y*32,16,64,line.substring(x+1,x+4));
					x+=4;
				}
				else if (ch == '%') {
					theme.addEnemy('%', x*32,y*32);
				}
				else if(ch == '!'){
					theme.addEnemy('!',x*32,y*32);
				}
			}
		}
		
		System.out.println("Map Data Set!");
	}
	
	public void setTile(int x, int y, Image tile) {tiles[x][y] = tile;}
    public Image getTile(int x, int y){
        if(x >= 0 && x < width && y >= 0 && y < height){return tiles[x][y];}
        return null;
    }
    
    public void setBackground(Image img){background = img;}
    
    public void setTheme(int themeNum){
    	if(themeNum == 1){theme = new Theme(); 	}
    	else if(themeNum == 2){	theme = new fireTheme();}
    	else if(themeNum == 3){theme = new fortTheme();}
    }
    
    public boolean isColliding(GameObject obj,int newX, int newY) {
    	int fromTileX = (Math.min(obj.getX(),newX))/32;
		int fromTileY = (Math.min(obj.getY(),newY))/32;
		int toTileX = ((Math.max(obj.getX(),newX)) + obj.getWidth()-1)/32;
		int toTileY = ((Math.max(obj.getY(),newY)) + obj.getHeight()-1)/32;
		for (int x=fromTileX; x<=toTileX; x++) {
			for (int y=fromTileY; y<=toTileY; y++) {
				if (x < 0 || x >= width || tileMap[x][y] != null){
					return true;
				}
			}
		}
		return false;
	}
}