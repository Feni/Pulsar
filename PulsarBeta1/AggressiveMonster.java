package PulsarBeta1;

public class AggressiveMonster extends Monster{
	public AggressiveMonster(int xCoord, int yCoord){
		super(xCoord,yCoord);
	}
	
	public void onTargetSited(){
		Attack temp = initTargetAttack(Game.p);
		if(temp != null){
			Game.attacks.add(temp);
			temp.sendWarnings();
		}
	}
	
	int counter = 0;
	public Attack initTargetAttack(GameObject obj){

		if(counter >= 25){
			counter = 0;
			if(Game.p.x > x){
				dx = getMaxSpeed();
				right = true;
			}
			else{
				dx = -1* getMaxSpeed();
				right = false;
			}
			Attack temp = initiateAttack();
			return temp;			
		}
		counter++;
		return null;
	}
}