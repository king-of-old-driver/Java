package tank;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.applet.*;

/**
 * Created by lu on 16-11-14.
 */
public class Bullet extends Thread{
	final private int step=25,r=15;
	private int dir=0;//0上1下2左3右
	private int x,y;
	private int harm=10;
	private static int id=0;
	private boolean valid=true;
	private Color color=Color.black;

	public Bullet(int dir,int x,int y){
		super("Bullet"+id++);
		this.dir=dir;
		this.x=x;
		this.y=y;
	}

	public Bullet(int dir,int x,int y,Color color){
		super("Bullet"+id++);
		this.dir=dir;
		this.x=x;
		this.y=y;
		this.color=color;
	}

	public boolean isValid(){
		return valid;
	}

	public void unValid(){
		valid=false;
	}

	public void run(){
		while(true) {
			switch (dir) {
				case 0:
					y -= step;
					break;
				case 1:
					y += step;
					break;
				case 2:
					x -= step;
					break;
				case 3:
					x += step;
					break;
				default:
					break;
			}
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
			g.fillOval(x,y,2*r,2*r);
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