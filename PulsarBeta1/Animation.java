package PulsarBeta1;


import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Image;



public class Animation{
	Image[] run;
	Image[] fly;
	Image lastframe;
	Image stand[], fire[];
	Image anim[];
	int frameNumber, counter;
	GameObject obj; 
	// Constructor for moving object with animations for their abilities
	public Animation(GameObject go, Image[] r, Image f[], Image stnd[], Image fre[]){		
		obj = go;
		run = r;
		fly = f;
		stand = stnd;
		fire = fre;
		
		lastframe = stand[0];
		frameNumber = 0;
		counter = 0;
	}
	
	public Animation(Image img[]){	// For simple, looping animations
		anim = img;
		lastframe = img[0];
		frameNumber = 0;
		counter = 0;
	}
	
	public Image changer(){
		if(counter < 3){
			counter++;
			return lastframe;
		}
		else{
			counter = 0;
			frameNumber++;
		}
		
		int lastVeX = obj.dx;
    	int lastVeY = obj.dy;

    	boolean onGround = obj.onGround;
		boolean up = obj.up;
		boolean right = obj.right;
		boolean atk = obj.firing;

		if(onGround && lastVeX != 0){
			if(frameNumber >= run.length-1){frameNumber = 0;}
			
			if (lastVeX>0 && right && onGround){
				lastframe = GameDisplay.getMirrorImage(run[frameNumber]);
			}
			else if (lastVeX<0 && !right && onGround){
				lastframe = run[frameNumber];
			}			
		}				
		else if(!onGround){
			if(frameNumber >= fly.length-1){frameNumber = 0;}
			
			if (lastVeX>0 && right && !onGround){
				lastframe = GameDisplay.getMirrorImage(fly[frameNumber]);
			}
			else if (lastVeX<0 && !right && !onGround){
				lastframe = fly[frameNumber];
			}			
		}
		else if(lastVeX == 0 && onGround){
			if(frameNumber >= stand.length-1){frameNumber = 0;}
			
			if (lastVeX==0 && onGround){
				lastframe = stand[frameNumber];
			}
		}
		else if(atk){
			if(frameNumber >= fire.length-1){frameNumber = 0;}
			
			if (lastVeX>0 && atk){
				lastframe = fire[frameNumber];
			} 
			else if (lastVeX<0 && atk){
				lastframe = GameDisplay.getMirrorImage(fire[frameNumber]);
			}			
		}
    	return lastframe;
	}
	
	// Changes frame by frame in a looping manner
	public Image simpleChanger(){
		if(anim.length == 1){
			return anim[0];
		}
		if(counter < 5){
			counter++;
			return lastframe;
		}
		else{
			counter = 0;
			lastframe = anim[frameNumber];
			if(frameNumber >= anim.length-1){frameNumber = 0;}
			else{frameNumber++;}
		}
    	return lastframe;
	}
	public void setFrameNumber(int fn){frameNumber = fn;}
}