package tank;

import java.util.List;
import java.util.Random;

/**
 * Created by lu on 16-11-14.
 */
public class TankAi extends Thread implements WinSize{
	final int FIRE=8,UP=0,UP_RIGHT=1,RIGHT=2,RIGHT_DOWN=3,DOWN=4,LEFT_DOWN=5,LEFT=6,UP_LEFT=7;
	final Random random=new Random();
	final private int SLEEP_TIME=500;
	private Win win;
	public TankAi(Win win){
		this.win=win;
	}
	public void run(){
		while(true) {
			for (Tank nowTank : win.tankList) {
				//nowTank.getOrder(getRandomOrder());
				nowTank.getOrder(getOrder(nowTank));
			}
			try {
				sleep(SLEEP_TIME);
			}
			catch (InterruptedException e){

			}
		}
	}

	private int getRandomOrder(){
		return random.nextInt(9);
	}

	private int getOrder(Tank tank){
		if(win.myTank==null)
			return getRandomOrder();
		final int DT=50;
		int x=tank.getX(),y=tank.getY(),dir=tank.getDir();
		int tarX=win.myTank.getX(),tarY=win.myTank.getY();
		int dtX=Math.abs(tarX-x),dtY=Math.abs(tarY-y);
		//double dtTmp=(double)(y-tarY)/(x-tarY);

		if(dtX<=DT){//和目标处于同一x水平线上
			if(tarY>y){
				return dir==DOWN?FIRE:DOWN;
			}
			else{
				return dir==UP?FIRE:UP;
			}
		}
		if(dtY<=DT){
			if(tarX>x){//目标在右边
				return dir==RIGHT?FIRE:RIGHT;
			}
			else{//左边
				return dir==LEFT?FIRE:LEFT;
			}
		}
		if(dtX<dtY){
			return tarX>x?RIGHT:LEFT;
		}
		else{
			return tarY>y?DOWN:UP;
		}
	}

}
