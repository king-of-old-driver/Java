package tank;

import java.util.List;
import java.util.Random;

/**
 * Created by lu on 16-11-14.
 */
public class TankAi extends Thread{
	final char FIRE='J',UP='W',DOWN='S',LEFT='A',RIGHT='D';
	final char[]keys={'A','D','S','W','J'};
	final Random random=new Random();
	private List<Tank>tankList;
	private List<Bullet>bulletList;
	private int width,high;
	final private int SLEEP_TIME=500;
	private Tank myTank;
	public TankAi(List<Tank> tankList,List<Bullet>bulletList,int width,int high){
		this.tankList=tankList;
		this.bulletList=bulletList;
		this.width=width;
		this.high=high;
	}
	public TankAi(List<Tank> tankList,List<Bullet>bulletList,Tank myTank,int width,int high){
		this.tankList=tankList;
		this.bulletList=bulletList;
		this.width=width;
		this.high=high;
		this.myTank=myTank;
	}
	public void run(){
		while(true) {
			for (Tank nowTank : tankList) {
				char key = getKey(nowTank);
				nowTank.getOrder(key,width,high,bulletList);
			}
			try {
				sleep(SLEEP_TIME);
			}
			catch (InterruptedException e){

			}
		}
	}

	private char getKey(Tank tank){
		if(myTank==null)
			return getRandomKey();
		final int DT=50;
		int x=tank.getX(),y=tank.getY(),dir=tank.getDir();
		int tarX=myTank.getX(),tarY=myTank.getY();
		int dtX=Math.abs(tarX-x),dtY=Math.abs(tarY-y);
		if(dtX<=DT){//和目标处于同一x水平线上
			if(tarY>y){
				return dir==1?FIRE:DOWN;
			}
			else{
				return dir==0?FIRE:UP;
			}
		}
		if(dtY<=DT){
			if(tarX>x){//目标在右边
				return dir==3?FIRE:RIGHT;
			}
			else{//左边
				return dir==2?FIRE:LEFT;
			}
		}
		if(dtX<dtY){
			return tarX>x?RIGHT:LEFT;
		}
		else{
			return tarY>y?DOWN:UP;
		}
	}
	private char getRandomKey(){
		return keys[random.nextInt(keys.length)];
	}
}
