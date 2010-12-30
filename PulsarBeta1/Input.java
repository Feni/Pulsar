package PulsarBeta1;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.*;
import java.awt.event.*;


public class Input implements KeyListener, MouseListener {
	
     public static boolean up,down,left,right,space,exit, enter, mouseClicked;
     int clickX, clickY;
     
     public Input(){
     	up = false;
     	down = false;
     	left = false;
     	right = false;
     	space = false;
     	enter = false;
     	clickX = 0;
     	clickY = 0;
     }
     
     public void keyTyped(KeyEvent e) {}
  
     public void keyPressed(KeyEvent e) {
         if (e.getKeyCode() == KeyEvent.VK_ESCAPE){  exit = true; }
         else if (e.getKeyCode() == KeyEvent.VK_LEFT) { left = true; }
         else if (e.getKeyCode() == KeyEvent.VK_RIGHT) { right = true; }
         else if (e.getKeyCode() == KeyEvent.VK_DOWN) {  down = true; }
         else if (e.getKeyCode() == KeyEvent.VK_UP) { up = true; }
         else if (e.getKeyCode() == KeyEvent.VK_SPACE) { space = true;}
         else if (e.getKeyCode() == KeyEvent.VK_ENTER) { enter = true;}
     }
     public void keyReleased(KeyEvent e) {
         if (e.getKeyCode() == KeyEvent.VK_ESCAPE){  exit = false; }
         else if (e.getKeyCode() == KeyEvent.VK_LEFT) { left = false; }
         else if (e.getKeyCode() == KeyEvent.VK_RIGHT) { right = false; }
         else if (e.getKeyCode() == KeyEvent.VK_DOWN) {  down = false; }
         else if (e.getKeyCode() == KeyEvent.VK_UP) { up = false; }
         else if (e.getKeyCode() == KeyEvent.VK_SPACE) { space = false;}
         else if (e.getKeyCode() == KeyEvent.VK_ENTER) { enter = false;}
     }
     
     
    public void mouseEntered( MouseEvent e ) { }
	public void mouseExited( MouseEvent e ) { }
    public void mouseClicked( MouseEvent e ){
   		System.out.println("Mouse Clicked at "+e.getX()+" "+e.getY());
    }
    
    int offsetX, offsetY;
    public void mousePressed( MouseEvent e ) {// called after a button is pressed down
      	mouseClicked = true;
   	  	clickX = e.getX();
   	  	clickY = e.getY();
   	  
		offsetX = (GameDisplay.screenWidth / 2) - Game.p.getX() - 32;
		offsetX = Math.min(offsetX, 0);
		offsetX = Math.max(offsetX, GameDisplay.screenWidth - Game.map.width * 32);
		offsetX*=-1;
		offsetY = (GameDisplay.screenHeight / 2) - Game.p.getY() - 32;
		offsetY = Math.min(offsetY, 0);
		offsetY = Math.max(offsetY, GameDisplay.screenHeight - Game.map.height * 32);
		offsetY*=-1;
		
		clickX+=offsetX;
		clickY+=offsetY;
   	  
   	  
   	  
      e.consume();// "Consume" the event so it won't be processed in the default manner

    }
    public void mouseReleased( MouseEvent e ){  // called after a button is released
      mouseClicked = false;
      e.consume();
    }

 }
