package PulsarBeta1;

import java.awt.Rectangle;

public class Item{
	Rectangle i;
	public Item(int xCoord, int yCoord, int w, int h){
		i = new Rectangle(xCoord,yCoord,w,h);
	}
	
	public boolean collidesWith(GameObject obj){
		return i.intersects(new Rectangle(obj.x,obj.y, obj.getWidth(), obj.getHeight()));
	}
}

class Door extends Item{
	String location; // Location the door is leading to. Must be a map name
	/*
	 *NOTE:
	 *	   Further capabilities to add doors to "teleport" to other locations on 
	 *the same map can possibly be added in later.
	 **/
	
	public Door(int xCoord, int yCoord, int w, int h, String loc){
		super(xCoord, yCoord, w, h);
		location = loc;
	}
}