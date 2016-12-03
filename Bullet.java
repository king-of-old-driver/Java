package tank;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.applet.*;
import java.util.List;

/**
 * Created by lu on 16-11-14.
 */
public class Bullet extends Thread{
	final private int step=25;
	private int r=16;
	private int dir=0;//01234567
	private int x,y;
	private int harm=10;
	private static int id=0;
	private boolean valid=true;
	private Color color=Color.black;
	private Win win;

	public Bullet(int dir,int x,int y,int r,Color color,Win win){
		super("Bullet"+id++);
		this.dir=dir;
		this.x=x;
		this.y=y;
		this.color=color;
		this.win=win;
		this.r=r;
	}

	public boolean isValid(){
		return valid;
	}

	public void unValid(){
		valid=false;
	}

	public void run(){
		while(true) {
			if(1<=dir&&dir<=3){
				x+=step;
			}
			if(5<=dir&&dir<=7){
				x-=step;
			}
			if(3<=dir&&dir<=5){
				y+=step;
			}
			if(dir%7<=1){
				y-=step;
			}
			checkHit();
			try {
				sleep(100);
			} catch (InterruptedException e) {
				System.out.println(e.getStackTrace());
			}
		}
	}

	protected void paintBullet(Graphics g){
		if(valid){
			g.setColor(color);
			g.fillOval(x-r,y-r,2*r,2*r);
		}
	}

	protected void paintBullet(Graphics2D g){
		if(valid){
			int k=r/2;
			g.setColor(color);
			g.rotate(dir*Math.PI/4,x,y);
			g.fillRect(x-k,y-k,2*k,2*k);
			int X[]={x,x+2*k,x-2*k,x};
			int Y[]={y-2*k,y-k,y-k,y-2*k};
			g.fillPolygon(X,Y,X.length);
			g.rotate(-dir*Math.PI/4,x,y);
		}
	}

	protected void checkHit(){
		if(valid==false)
			return;
		for(Block block:win.blockList){
			for(Line line:block.getLineArray()){
				if(line.isHit(x,y,r)){
					unValid();
					return;
				}
			}
		}
		synchronized (win.tankList) {
			for (Tank nowTank : win.tankList) {
				if (nowTank.getHP() <= 0)
					continue;
				check(nowTank);
			}
		}
		if (win.myTank.getHP() > 0) {
			check(win.myTank);
		}
	}

	private void check(Tank nowTank){
		int tankR=2*nowTank.getMeasure();
		int tankX=nowTank.getX();
		int tankY=nowTank.getY();

		Circle circle1=new Circle(x,y,r);
		Circle circle2=new Circle(tankX,tankY,tankR);

		if (circle1.relation(circle2)) {
			synchronized (this) {
				nowTank.setHP(nowTank.getHP() - getHarm());
			}
			unValid();
		}
	}

	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getR(){
		return r;
	}
	public int getDir(){
		return dir;
	}
	public int getHarm(){
		return harm;
	}
}