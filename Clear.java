package tank;

import java.util.List;

/**
 * Created by lu on 16-11-14.
 */

/*
java.util.ConcurrentModificationException 抛出此异常时 无法命中坦克
*/

public class Clear extends Thread implements WinSize{
	final private int SLEEP_TIME=200;
	private Win win;

	public Clear(Win win){
		this.win=win;
	}
	
	private void clearTank(){
		synchronized (win.tankList) {
			for (int i = 0; i < win.tankList.size(); ) {
				Tank nowTank = win.tankList.get(i);
				if (nowTank.getHP() <= 0 &&nowTank.noBullet()) {
					win.tankList.remove(i);
				} else {
					++i;
				}
			}
		}
	}

	public void run(){
		while (true) {//迭代时 若list被修改 抛出java.util.ConcurrentModificationException 导致无法命中坦克
			clearTank();
			win.myTank.clearBullet();
			for(Tank tank:win.tankList){
				tank.clearBullet();
			}
			try {
				sleep(SLEEP_TIME);
			} catch (InterruptedException e) {

			}
		}
	}
}