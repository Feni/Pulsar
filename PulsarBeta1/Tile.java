package PulsarBeta1;

public class Tile{
	public boolean collidable = false;
	public boolean movable= false;
	public boolean destroyable = false;
	public Tile(boolean c){
		collidable = c;
		movable = false;
		destroyable = false;
	}
}