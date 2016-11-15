package tank;

import java.util.List;
import java.util.Random;

/**
 * Created by lu on 16-11-14.
 */
public class TankAi extends Thread{
	final char[]keys={'A','D','S','W','J'};
	final Random random=new Random();
	private List<Tank>tankList;
	private List<Bullet>bulletList;
	private int width,high;
	final private int SLEEP_TIME=500;
	public TankAi(List<Tank> tankList,List<Bullet>bulletList,int width,int high){
		this.tankList=tankList;
		this.bulletList=bulletList;
		this.width=width;
		this.high=high;
	}
	public void run(){
		while(true) {
			for (Tank nowTank : tankList) {
				char key = getKey();
				nowTank.getOrder(key,width,high,bulletList);
			}
			try {
				sleep(SLEEP_TIME);
			}
			catch (InterruptedException e){

			}
		}
	}
	private char getKey(){
		return keys[random.nextInt(keys.length)];
	}
}
