package PulsarBeta1;

/***********************************************
 * Pulsar Version 1.0
 * 18 Classes
 * 1500 lines
 ************************************************
 *
 *Game Class: The primary driver class of the program.
 *It starts out the program and calls the GameLoop
 *
 ***********************************************/
 
 import java.awt.Canvas;
 import java.awt.Dimension;
 import java.awt.Graphics;
 import java.awt.event.*;
 import java.awt.image.BufferStrategy;
 import javax.swing.JFrame;
 import javax.swing.JPanel;
 import java.awt.Color;
 import java.io.IOException;
 import java.util.ArrayList;
 import javax.swing.ImageIcon;
 import java.io.IOException;
 import java.awt.*;
 import javax.swing.*;
 import java.awt.*;
 import javax.swing.JFrame;
 import java.io.*;

 
public class Game extends Canvas{
	/* Using the main to construct the object and call the game loop
	** allows control over the execution sequence withou	t creating an infinite loop*/
	
	
	public static void init(){
		System.out.println("INIT");
	}
	
	static File outFile = new File("OutputLog.txt");
	static PrintStream outStream;

	// It's Get OutStream NOT GetOut Stream!
	
	public static PrintStream getOutStream(){
		try{
			if(outStream == null){
				if(outFile.exists()){
					outFile.delete();
				}
				outFile.createNewFile();
				outStream = new PrintStream(outFile);
			}
			return outStream;			
		}
		catch(Exception e){
			System.out.println(e);
		}
		return System.out;
	}
	
    public static void main(String[] args){
    	try{
			System.setErr(getOutStream());
			System.setOut(getOutStream());
    		
    		Game g =new Game();    		
	    	g.gameLoop();
    	}
    	catch(Exception e){
    		JOptionPane.showMessageDialog(null, e.toString());
    	}
    }
    static Input keyBoard;

    public static GameDisplay gd = new GameDisplay();
    static GameMap map = new GameMap();
    static Player p = new Player();    
    static ArrayList attacks = new ArrayList<Attack>();
    static ArrayList enemies = new ArrayList<Monster>();
    
    BufferStrategy strategy;
    boolean running = true;
    static boolean fullscreen;
    static boolean gameOver = false;
    static GameMenu startingMenu = new GameMenu();
    static GameMenu gameSettings = new GameMenu();
    
    static GameMenu activeMenu;
    
	static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	static DisplayMode displayMode = new DisplayMode(800, 600, -1,DisplayMode.REFRESH_RATE_UNKNOWN);
	static JFrame frame;
    
    public Game() throws IOException{
    	fullscreen = false;
    	// Setup the Window
        frame = new JFrame("-PULSAR-");
        frame.setPreferredSize(new Dimension(800,600));
        frame.setBounds(0,0,800,600);
        frame.setLocation(0,0);
        frame.setIgnoreRepaint(true);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {System.exit(0);}
        });
        // Setup Keyboard as Input
        keyBoard = new Input();
        frame.addKeyListener(keyBoard);
        frame.addMouseListener(keyBoard);
        frame.requestFocus();
        // Set up Double Buffering
        frame.createBufferStrategy(2);
        strategy = frame.getBufferStrategy();     
		
		// Setup the 2 GameMenus
        GameMenu.setBackground(new ImageIcon("images/backgroundMaple.jpg").getImage());
        startingMenu.addOption(new GameOption(1));
        startingMenu.addOption(new GameOption(2));
        startingMenu.addOption(new GameOption(3));
        startingMenu.addOption(new GameOption(4));
        startingMenu.addOption(new GameOption(5));

        gameSettings.addOption(new booleanOption(true, 1));
        gameSettings.addOption(new booleanOption(false, 2));
        gameSettings.addOption(new intOption(2,5,2,1));
        gameSettings.addOption(new intOption(0,9,5,2));
        gameSettings.addOption(new GameOption(6));
        
        // Make the StartingMenu the starting screen
        activeMenu = startingMenu;
        startingMenu.isDisplaying = true;
		
		// Load level 1
        map.setLevel("001");
    }
    public void gameLoop()throws IOException{
    	if(fullscreen){	setFullscreen();}
    	Graphics g;
    	while(running){
    		try { Thread.sleep(75); } catch (Exception e) {}
    		if(activeMenu.isDisplaying){
	    		g = strategy.getDrawGraphics();
    			g.setColor(Color.black);
				g.fillRect(0,0,800,600);
				activeMenu.listen();
				activeMenu.drawOptions(g);
	    		g.dispose();
				strategy.show();
    		}
    		else{
	  			for(int k = 0; k < 13; k++){
	  		   		g = strategy.getDrawGraphics();
			  		g.setColor(Color.black);
					g.fillRect(0,0,800,600);
					updateObjects();
					gd.draw(g);
					g.dispose();
					strategy.show();
				}
    		}
    		if(gameOver){
    			for(int k = 0; k < 500; k++){
	  				g = strategy.getDrawGraphics();
	  				g.setColor(Color.black);
					g.fillRect(0,0,800,600);
					g.setColor(Color.white);
					g.setFont(new Font("TimesRoman",Font.BOLD,72));
					g.drawString("Game Over", 228,328);
					g.setFont(new Font("Arial",Font.PLAIN,12));
		    		g.dispose();
		    		strategy.show();
				}

				gameOver = false;
				exit();
    		}
    	}
    }
    int counter = 0;
	Attack tempAtk;
	Monster tempM;
    public void updateObjects()throws IOException{
    	// Takes the user to the main menu during the game
    	if(keyBoard.exit){
    		startingMenu.isDisplaying = true;
    		activeMenu = startingMenu;
    	}
    	
    	// Initiate straight line horizontal attack
    	if (keyBoard.space){
			Attack temp = p.initiateAttack();
			if(temp != null){
				attacks.add(temp);
				temp.sendWarnings();
			}
		}
		
		// Intitiate a still-testing 360 attack based on 
    	if (keyBoard.mouseClicked){
			Attack temp = p.initiateAttack(keyBoard.clickX, keyBoard.clickY);
			if(temp != null){
				attacks.add(temp);
				temp.sendWarnings();
			}
		}
		
		// Recharge
    	if(counter % 4 == 0){	// Once every 4 counters
			p.addBooster(1);
			for(int k = 0; k < enemies.size(); k++){
    			tempM = (Monster) Game.enemies.get(k);
    			tempM.addBooster(1);
    		}
    	}
    	if(counter % 30 == 0){
    		p.addAmmo(1);
    		for(int k = 0; k < enemies.size(); k++){
    			tempM = (Monster) Game.enemies.get(k);
    			tempM.addAmmo(1);
    		}
    	}
    	if(counter >= 100){
    		counter = 0;
    		p.addHealth(1);
    		for(int k = 0; k < enemies.size(); k++){
    			tempM = (Monster) Game.enemies.get(k);
    			tempM.addHealth(1);
    		}
    	}
    	counter++;
    	
    	// Player update based on Keyboard Input
		int vX = 0;
		int vY = 0;
    	if (keyBoard.left) {vX-=p.getMaxSpeed();}
		if (keyBoard.right){vX+=p.getMaxSpeed();}
		if (keyBoard.up){vY-=p.getMaxSpeed()*2;}
		if (keyBoard.down){vY+=p.getMaxSpeed();}
		p.setVelocityX(vX);
		p.setVelocityY(vY);
		p.updateObject();
		p.updatePlayerInfo();
		map.updateMap();
		
		// Update all attacks and remove inactive ones
		for(int k = 0; k < attacks.size(); k++){
			tempAtk = (Attack) Game.attacks.get(k);
			if(tempAtk.alive){tempAtk.updateAttack();}
			else{attacks.remove(k);}
		}
		// Updates all enemies and removes inactive ones
		for(int k = 0; k < enemies.size(); k++){
			tempM = (Monster) Game.enemies.get(k);
			if(tempM.alive){
				tempM.updateObject();
				tempM.makeMove();
			}
			else{enemies.remove(k);}
		}
		p.sendApprochWarnings();
		// Sets the game state to Game Over if the player is dead
		if(p.health <= 0){	gameOver = true;}
		// Resets the temperory variables for Garbage Collection
		tempAtk = null;
		tempM = null;
    }
	
	// Exit to windows
	// This method works only on Microsoft Windows.
	// Linux needs other things with it
    public static void exit(){
    	System.out.println("Kills: "+p.kills);
    	Window window = device.getFullScreenWindow();
    	// Disposes the contents of the current window
	    if (window != null) { window.dispose(); }
    	System.exit(0); 
    }
    
    // Sets up fullscreen
	public static void setFullscreen(){
		if (!fullscreen){
	        device.setFullScreenWindow(null);// Set to default style window
		}
		if (fullscreen){
	   	    DisplayMode newDisplayMode=new DisplayMode(800,600,16,DisplayMode.REFRESH_RATE_UNKNOWN);
		    device.setFullScreenWindow(frame);
	        if(device.isDisplayChangeSupported()) {
	            try {device.setDisplayMode(newDisplayMode);}
	            catch(IllegalArgumentException exception) {}
	            frame.setSize(800, 600);
	        }
	        else {exit();} 
		}
	}
}