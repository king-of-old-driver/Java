package tank;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by lu on 16-10-31.
 */
public class Tank implements WinSize{
	private int x,y,measure=8;
	private int step=35;
	private int measureStep=3;
	private int dir=4;
	private final int maxmeasure=20;
	private final int minmeasure=3;
	private int HP=100;
	private int HPBarWidth=50;
	private int HPBarHigh=10;

	private List<Bullet>bulletList=new LinkedList<Bullet>();
	private Win win;

	private final int wheelWidth=measure*8;
	private final int wheelHigh=measure*2;
	private final int gunBarrelWidth=measure*2;
	private final int gunBarrelHigh=measure*5;
	private final int bodyWidth=4*measure;

	private Color bodyColor=Color.black;
	private Color gunBarrelColor=Color.cyan;
	private Color wheelColor=Color.ORANGE;
	private Color bulletColor=Color.black;

	public Tank(int x,int y,Win win){
		this.x=x;
		this.y=y;
		this.win=win;
	}

	protected void getOrder(int order){
		if(order==8){
			fireBullet();
		}else {
			changePos(order);
		}
	}

	protected void getOrder(Map<Character,Boolean>order){
		if(order.get('J')){
			fireBullet();
		}
		int toDir=-1;
		if(order.get('W')){
			if(order.get('A')){
				toDir=7;
			}
			else if (order.get('D')){
				toDir=1;
			}
			else{
				toDir=0;
			}
		}
		else if(order.get('S')){
			if(order.get('A')){
				toDir=5;
			}
			else if(order.get('D')){
				toDir=3;
			}
			else {
				toDir=4;
			}
		}
		else if(order.get('A')){
			toDir=6;
		}
		else if(order.get('D')){
			toDir=2;
		}
		changePos(toDir);
	}

	private void changePos(int toDir){
		if(toDir<0||toDir>8)
			return;
		dir=toDir;//01234567 上 右上 右 右下,下 左下 左,左上
		int newX=x,newY=y;
		if(1<=toDir&&toDir<=3){
			newX+=step;
		}
		if(5<=toDir&&toDir<=7){
			newX-=step;
		}
		if(3<=toDir&&toDir<=5){
			newY+=step;
		}
		if(toDir%7<=1){
			newY-=step;
		}
		if(canMoveTo(newX,newY)){
			x=newX;
			y=newY;
		}
	}

	protected void fireBullet(){
		if(HP<=0)
			return;
		int bulletX=x,bulletY=y;
		if(1<=dir&&dir<=3){
			bulletX+=2*step;
		}
		if(5<=dir&&dir<=7){
			bulletX-=2*step;
		}
		if(3<=dir&&dir<=5){
			bulletY+=2*step;
		}
		if(dir%7<=1){
			bulletY-=2*step;
		}
		Bullet bullet=null;
		bullet=new Bullet(dir,bulletX,bulletY,2*measure,bulletColor,win);
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

	public void setStep(int step){
		this.step=step;
	}

	protected void paint(Graphics2D g){
		if(HP<=0)
			return;
		//血条
		g.setColor(Color.RED);
		int hpX=x-measure*4,hpY=y-measure*8-HPBarHigh;
		g.drawString("HP:",hpX,hpY);
		g.fillRect(hpX,hpY,HPBarWidth*HP/100,HPBarHigh);

		g.rotate(Math.PI/4*dir,x,y);
		g.setColor(bodyColor);
		g.fillOval(x-measure*2,y-measure*2,bodyWidth,bodyWidth);
		g.setColor(wheelColor);
		g.fillRect(x-4*measure,y-4*measure,wheelHigh,wheelWidth);
		g.fillRect(x+2*measure,y-4*measure,wheelHigh,wheelWidth);
		g.setColor(gunBarrelColor);
		g.fillRect(x-measure,y-7*measure,gunBarrelWidth,gunBarrelHigh);
		g.rotate(-Math.PI/4*dir,x,y);

		synchronized (bulletList) {
			for (Bullet nowBullet : bulletList) {
				nowBullet.paintBullet(g);
			}
		}
	}

	private boolean canMoveTo(int x,int y) {
		if (x+bodyWidth > WIN_WIDTH||x-bodyWidth<0||y+bodyWidth>WIN_HIGH||y-bodyWidth<0)
			return false;
		synchronized (win.tankList){//防止穿过其他坦克
			for(Tank tank:win.tankList){
				if(tank==this)
					continue;
				Circle circle1=new Circle(x,y,2*measure);
				Circle circle2=new Circle(tank.x,tank.y,2*tank.bodyWidth);
				if(circle1.relation(circle2)){
					return false;
				}
			}
			if(win.myTank!=this){
				Circle circle1=new Circle(x,y,2*measure);
				Circle circle2=new Circle(win.myTank.x,win.myTank.y,2*win.myTank.bodyWidth);
				if(circle1.relation(circle2)){
					return false;
				}
			}
		}
		for(Block block:win.blockList){//防止穿过障碍
			Line[]lineArry=block.getLineArray();
			for(Line line:lineArry){
				if(line.isHit(x,y,1.5*bodyWidth)==true)
					return false;
			}
		}
		return true;
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
	protected static void setDefaultTank(List<Tank>tankList,Win win){
		Random random=new Random();
		for(int i=0;i<Win.otherTankNumber;++i){
			int toX=random.nextInt(WIN_WIDTH),toY=random.nextInt(WIN_HIGH);
			while(win.myTank.canMoveTo(toX,toY)==false){
				toX=random.nextInt(WIN_WIDTH);
				toY=random.nextInt(WIN_HIGH);
			}
			tankList.add(new Tank(toX,toY,win));
		}
		int bossNum=Win.otherTankNumber/3;
		for(int i=0;i<bossNum;++i){
			win.tankList.get(i).setBoss();
		}
	}
	private void setBoss(){
		//set BOSS
		setStep(50);
		setHP(500);
		setBodyColor(Color.RED);
		setBulletColor(Color.RED);
		setWheelColor(Color.RED);
		setGunBarrelColor(Color.RED);
		//smaller();
	}
	protected void setMyTank(){
		x=WIN_WIDTH-100;
		y=WIN_HIGH-100;
		setBodyColor(Color.green); //set myTank's color
		setGunBarrelColor(Color.BLUE);
		setWheelColor(Color.MAGENTA);
		setBulletColor(Color.pink);
		setStep(20);
	}
	protected void clearBullet(){
		synchronized (bulletList) {
			for (int i = 0; i < bulletList.size(); ) {
				Bullet nowBullet = bulletList.get(i);
				int x = nowBullet.getX(), y = nowBullet.getY();
				if (nowBullet.isValid() == false || x > WIN_WIDTH || x < 0 || y > WIN_HIGH || y < 0) {
					bulletList.remove(i);
				} else {
					++i;
				}
			}
		}
	}
	protected boolean noBullet(){
		return bulletList.isEmpty();
	}
}
