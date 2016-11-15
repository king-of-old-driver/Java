package tank;

import java.awt.*;
import java.util.List;

/**
 * Created by lu on 16-10-31.
 */
public class Tank{
	private int x,y,measure;
	private int step=20;
	private int measureStep=10;
	private int dir=3;
	private final int maxmeasure=100;
	private final int minmeasure=10;
	private int HP=100;
	private int HPBarWidth=50;
	private int HPBarHigh=10;

	private Color bodyColor=Color.black;
	private Color gunBarrelColor=Color.cyan;
	private Color wheelColor=Color.ORANGE;
	private Color bulletColor=Color.black;

	protected void getOrder(char key,int width,int high,List<Bullet>bulletList){
		if(key=='J'){
			fireBullet(bulletList);
		}
		else{
			changePos(key,width,high);
		}
	}

	private void changePos(char key,int width,int high){
		switch (key){
			case 'A'://上下左右
				dir = 2;
				x -= step;
				break;
			case 'D':
				dir = 3;
				x += step;
				break;
			case 'S':
				dir = 1;
				y += step;
				break;
			case 'W':
				dir = 0;
				y -= step;
				break;
		}
		x=Math.min(width,x);
		x=Math.max(0,x);
		y=Math.min(high,y);
		y=Math.max(0,y);
	}

	private void fireBullet(List<Bullet> bulletList){
		if(HP<=0)
			return;
		Bullet bullet=null;
		switch (dir){
			case 0:
				bullet=new Bullet(dir,x,y-measure,bulletColor);
				break;
			case 1:
				bullet=new Bullet(dir,x,y+measure,bulletColor);
				break;
			case 2:
				bullet=new Bullet(dir,x-measure,y,bulletColor);
				break;
			case 3:
				bullet=new Bullet(dir,x+measure,y,bulletColor);
				break;
		}

		bullet.start();
		synchronized (bulletList) {
			bulletList.add(bullet);
		}
	}

	public int getHP(){
		return HP;
	}

	public void setHP(int HP){
		this.HP=HP;
	}

	public int getDir(){
		return dir;
	}

	public int getMeasure(){
		return measure;
	}

	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}

	public Tank(int x,int y){
		this.x=x;
		this.y=y;
		measure=30;
	}

	protected void paintTank(Graphics g){
		if(HP<=0)
			return;
		//paint HP bar
		g.setColor(Color.RED);
		int hpX=x-measure/2,hpY=y-measure*3/2;
		g.drawString("HP:",hpX,hpY);
		g.fillRect(hpX,hpY,HPBarWidth*HP/100,HPBarHigh);

		g.setColor(bodyColor);
		g.fillOval(x,y,measure,measure);
		g.setColor(wheelColor);
		if(dir<2){
			g.fillRect(x+measure,y-measure/2,measure/2,measure*2);
			g.fillRect(x-measure/2,y-measure/2,measure/2,measure*2);
			g.setColor(gunBarrelColor);
			if(dir==0){
				g.fillRect(x+measure/4,y-measure,measure/2,measure);
			}
			else{
				g.fillRect(x+measure/4,y+measure,measure/2,measure);
			}
		}
		else{
			g.fillRect(x-measure/2,y+measure,measure*2,measure/2);
			g.fillRect(x-measure/2,y-measure/2,measure*2,measure/2);
			g.setColor(gunBarrelColor);
			if(dir==2){
				g.fillRect(x-measure,y+measure/4,measure,measure/2);
			}
			else {
				g.fillRect(x + measure, y + measure / 4, measure, measure / 2);
			}
		}
	}

	public void setBodyColor(Color color){
		bodyColor=color;
	}
	public void setGunBarrelColor(Color color){
		gunBarrelColor=color;
	}
	public void setWheelColor(Color color){
		wheelColor=color;
	}
	public void setBulletColor(Color color){
		bulletColor=color;
	}

	protected void bigger(){
		measure+=measureStep;
		if (measure > maxmeasure)
			measure = maxmeasure;
	}
	protected void smaller(){
		measure-=measureStep;
		if (measure < minmeasure)
			measure = minmeasure;
	}
}
