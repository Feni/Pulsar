package PulsarBeta1;

/*******************************************************************************
 *
 *Game Display:
 *	The Game Display handles almost all of the drawing that happens in the game.
 *It accesses all of the objects through GameDriver and determines what the
 *image in the animation it needs to draw
 *
 ******************************************************************************/


import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.io.InputStream;
import java.io.*;
import java.net.URL;
import javax.swing.ImageIcon;


class GameDisplay{
	static Image lastframe, coalescence1280, fire1, gule1, gule2, gule3;
	static Image ironfire, ironjump1, ironjump2, ironjump3, ironstand,ironwalk1,ironwalk2,ironwalk3;
	static Image star1,star2,star3,star4;
	static Image invisibleTile;
	public static ArrayList <Image>tileSet;

	public static int screenWidth = 800;
	public static int screenHeight = 600;
	public static int offsetX, offsetY;

	public GameDisplay(){

		System.setOut(Game.getOutStream());
		System.setErr(Game.getOutStream());

		System.out.println("GameDisplay created");

		if(coalescence1280 == null){
			System.out.println("Loading all of the images at once ");
			coalescence1280 = new ImageIcon("images/coalescence1280.jpg").getImage();
			fire1 = new ImageIcon("images/fire1.png").getImage();
			gule1 = new ImageIcon("images/gule1.png").getImage();
			gule2 = new ImageIcon("images/gule2.png").getImage();
			gule3 = new ImageIcon("images/gule3.png").getImage();
			ironfire = new ImageIcon("images/ironfire.png").getImage();
			ironjump1 = new ImageIcon("images/ironjump1.png").getImage();
			ironjump2 = new ImageIcon("images/ironjump2.png").getImage();
			ironjump3 = new ImageIcon("images/ironjump3.png").getImage();
			ironstand = new ImageIcon("images/ironstand.png").getImage();
			ironwalk1 = new ImageIcon("images/ironwalk1.png").getImage();
			ironwalk2 = new ImageIcon("images/ironwalk2.png").getImage();
			ironwalk3 = new ImageIcon("images/ironwalk3.png").getImage();
			star1 = new ImageIcon("images/star1.png").getImage();
			star2 = new ImageIcon("images/star2.png").getImage();
			star3 = new ImageIcon("images/star3.png").getImage();
			star4 = new ImageIcon("images/star4.png").getImage();
			invisibleTile = new ImageIcon("images/invisibleTile.png").getImage();
		}
	}

	public void loadTileImages(int start, int end) {
		tileSet = new ArrayList<Image>();
		while (true) {
			File f = new File("images/"+"tile_"+start+".png");
			if (!f.exists() || start > end) {break;}
			tileSet.add(loadImage("tile_"+start+".png"));
			start++;
		}
	}
	static Color transparentRed = new Color(200, 0, 0, 150);
	static Color transparentGreen = new Color(0, 200, 0, 150);
	static Color transparentBlue = new Color(0, 0, 200, 150);
	public static void draw(Graphics g){
		int mapWidth = Game.map.width * 32;
		int mapHeight = Game.map.height * 32;

		// get the scrolling position of the map based on player's position
		// Player is on right side of map, then offsetX is neg
		offsetX = (screenWidth / 2) - Game.p.getX() - 32;
		offsetX = Math.min(offsetX, 0);
		offsetX = Math.max(offsetX, screenWidth - mapWidth);
		offsetY = (screenHeight / 2) - Game.p.getY() - 32;
		offsetY = Math.min(offsetY, 0);
		offsetY = Math.max(offsetY, screenHeight - mapHeight);

		if (Game.map.background == null ||	screenHeight > Game.map.background.getHeight(null)){// draw black background, if needed
			g.setColor(Color.black);
			g.fillRect(0, 0, screenWidth, screenHeight);
		}
		else if (Game.map.background != null) {// draw parallax background image
			int x = offsetX * (screenWidth - Game.map.background.getWidth(null)) / (screenWidth - mapWidth*2);
			int y = offsetY * (screenHeight - Game.map.background.getHeight(null)) / (screenHeight - mapHeight*2);
			g.drawImage(Game.map.background, x, y, null);
		}


		Theme theme = Game.map.theme;
		for(int k = 0; k < theme.extras.size(); k++){
			Extra e = (Extra) theme.extras.get(k);
			g.drawImage(e.anim.simpleChanger(),e.x+offsetX,e.y+offsetY,null);
		}

		int firstTileX = (int)Math.floor(-1*offsetX / 32);// draw the visible tiles
		int lastTileX = firstTileX + (int) Math.floor(screenWidth / 32) +4;
		for (int y=0; y<Game.map.height; y++) {
			for (int x=firstTileX; x <= lastTileX; x++) {
				Image t = Game.map.getTile(x, y);
				if (t != null) {g.drawImage(t,(x*32) + offsetX, (y * 32) + offsetY,null);}
			}
		}
		g.drawImage(Game.p.anim.changer(), Game.p.getX() + offsetX, Game.p.getY()+offsetY, null);

		Game.p.percentAmmo = 100*Game.p.ammo/Game.p.maxAmmo;
		Game.p.percentBooster = 100*Game.p.booster/Game.p.maxBooster;
		Game.p.percentHealth = 100*Game.p.health/Game.p.maxHealth;

		g.setColor(transparentGreen);
		g.fillRect(125,25,Game.p.percentHealth,15);

		g.setColor(transparentBlue);
		g.fillRect(250,25,Game.p.percentBooster,15);

		g.setColor(transparentRed);
		g.fillRect(375,25,Game.p.percentAmmo,15);

		g.setColor(Color.white);
		g.drawString(""+Game.p.kills,500,50);

		g.setColor(Color.black);

		g.drawRect(125,25,100,15);
		g.drawString("Health",130,35);
		g.drawRect(250,25,100,15);
		g.drawString("Booster",250,35);
		g.drawRect(375,25,100,15);
		g.drawString("Ammo",375,35);

		Attack tempAtk;	// You only have to call the Get method of ArrayList once.
		for(int k = 0; k < Game.attacks.size(); k++){
			tempAtk = (Attack) Game.attacks.get(k);
			g.drawImage(tempAtk.anim.simpleChanger(), tempAtk.getX()+offsetX, tempAtk.getY()+offsetY, null);
		}

		Monster tempM;
		for(int k = 0; k < Game.enemies.size(); k++){
			tempM = (Monster) Game.enemies.get(k);
			g.drawImage(tempM.anim.changer(), tempM.getX()+offsetX, tempM.getY()+offsetY, null);
		}
	}

	public Image loadImage(String name) {return new ImageIcon("images/"+name).getImage();}

    public static Image getMirrorImage(Image image) {return getScaledImage(image, -1, 1);}

    public static Image getFlippedImage(Image image) {return getScaledImage(image, 1, -1);}

    private static Image getScaledImage(Image image, int x, int y) {
    	GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
    	.getDefaultScreenDevice().getDefaultConfiguration();
        AffineTransform transform = new AffineTransform();
        transform.scale(x, y);
        transform.translate((x-1) * image.getWidth(null) / 2,(y-1) * image.getHeight(null) / 2);
        Image newImage = gc.createCompatibleImage(image.getWidth(null),image.getHeight(null),Transparency.BITMASK);
        Graphics2D g = (Graphics2D)newImage.getGraphics();
        g.drawImage(image, transform, null);
        g.dispose();
        return newImage;
    }
}