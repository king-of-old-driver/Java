package tank;

import java.util.List;

/**
 * Created by lu on 16-11-14.
 */

/*
java.util.ConcurrentModificationException 抛出此异常时 无法命中坦克
*/

public class CheckHit extends Thread {
	private List<Tank>tankList;
	private List<Bullet>bulletList;
	private Tank myTank;
	final private int SLEEP_TIME=200;
	private int width,high;

	public CheckHit(List<Tank>tankList,List<Bullet>bulletList,Tank myTank,int width,int high){
		this.tankList=tankList;
		this.bulletList=bulletList;
		this.myTank=myTank;
		this.width=width;
		this.high=high;
	}

	class Circle{
		int x,y,r;
		private double distance(int x,int y){
			return Math.sqrt((this.x-x)*(this.x-x)+(this.y-y)*(this.y-y));
		}
		public boolean relation(Circle circle){
			double dis=distance(circle.x,circle.y);
			return dis<=circle.r+r;
		}
		public Circle(int x,int y,int r){
			this.x=x;
			this.y=y;
			this.r=r;
		}
	}

	private void check(Tank nowTank,Bullet nowBullet){
		int bulletR=nowBullet.getR();
		int bulletX=nowBullet.getX()+bulletR;
		int bulletY=nowBullet.getY()+bulletR;
		int tankR=nowTank.getMeasure();
		int tankX=nowTank.getX()+tankR/2;
		int tankY=nowTank.getY()+tankR/2;

		Circle circle1=new Circle(bulletX,bulletY,bulletR);
		Circle circle2=new Circle(tankX,tankY,tankR);

		if (circle1.relation(circle2)) {
			synchronized (this) {
				nowTank.setHP(nowTank.getHP() - nowBullet.getHarm());
			}
			nowBullet.unValid();
		}
	}

	private void clearTank(){
		synchronized (tankList) {
			for (int i = 0; i < tankList.size(); ) {
				Tank nowTank = tankList.get(i);
				if (nowTank.getHP() <= 0) {
					tankList.remove(i);
				} else {
					++i;
				}
			}
		}
	}
	private void clearBullet(){
		synchronized (bulletList) {
			for (int i = 0; i < bulletList.size(); ) {
				Bullet nowBullet = bulletList.get(i);
				int x = nowBullet.getX(), y = nowBullet.getY();
				if (nowBullet.isValid() == false || x > width || x < 0 || y > high || y < 0) {
					bulletList.remove(i);
				} else {
					++i;
				}
			}
		}
	}

	public void run(){
		while (true) {//迭代时 若list被修改 抛出java.util.ConcurrentModificationException 导致无法命中坦克
			synchronized (bulletList) {
				for (Bullet nowBullet : bulletList) {
					if (nowBullet.isValid() == false)
						continue;
					synchronized (tankList) {
						for (Tank nowTank : tankList) {
							if (nowTank.getHP() <= 0)
								continue;
							check(nowTank, nowBullet);
						}
					}
					if (myTank.getHP() > 0) {
						check(myTank, nowBullet);
					}
				}
			}
			clearTank();
			clearBullet();
			try {
				sleep(SLEEP_TIME);
			} catch (InterruptedException e) {

			}
		}
	}
}