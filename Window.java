package tank;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by lu on 16-10-31.
 */

public class Window extends JFrame{
	final private int width=1500;
	final private int high=800;
	final private int otherTankNumber=5;
	private int gameFlag=0;
	private Tank myTank;
	private LinkedList<Bullet> bulletList=new LinkedList<Bullet>();
	private LinkedList<Tank>tankList=new LinkedList<Tank>();
	private boolean stopGame;

	private WinThread winThread;
	private TankAi tanAi;
	private CheckHit checkHit;

	public Window(){
		super("tank");
		setBounds(100,100,width,high);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent keyEvent) {
				super.keyPressed(keyEvent);
				char key = Character.toUpperCase(keyEvent.getKeyChar());
				myTank.getOrder(key,width,high,bulletList);
			}
		});

		startGame();
	}

	private void startGame(){
		Random random=new Random();
		for(int i=0;i<otherTankNumber;++i){
			tankList.add(new Tank(random.nextInt(width),random.nextInt(high)));
		}
		myTank=new Tank(width/2,high/2);
		myTank.setBodyColor(Color.green); //set myTank's color
		myTank.setGunBarrelColor(Color.BLUE);
		myTank.setWheelColor(Color.MAGENTA);
		myTank.setBulletColor(Color.pink);

		tanAi=new TankAi(tankList,bulletList,width,high);//control the other tanks
		checkHit=new CheckHit(tankList,bulletList,myTank,width,high);//check the bullet had hitted the tank?
		winThread=new WinThread();//check game is over and repaint

		setBoss();

		winThread.start();
		checkHit.start();
		tanAi.start();
		stopGame=false;
	}

	private void setBoss(){
		//set BOSS
		Tank boss=tankList.get(0);
		boss.setHP(500);
		boss.setBodyColor(Color.RED);
		boss.setBulletColor(Color.RED);
		boss.setWheelColor(Color.RED);
		boss.setGunBarrelColor(Color.RED);
		boss.smaller();
	}

	private synchronized void reStartGame(){
		tankList.clear();
		bulletList.clear();
		Random random=new Random();
		for(int i=0;i<otherTankNumber;++i){
			tankList.add(new Tank(random.nextInt(width),random.nextInt(high)));
		}
		myTank.setHP(100);
		//set BOSS
		Tank boss=tankList.get(0);
		boss.setHP(500);
		boss.setBodyColor(Color.RED);
		boss.setBulletColor(Color.RED);
		boss.setWheelColor(Color.RED);
		boss.setGunBarrelColor(Color.RED);
		boss.smaller();
		stopGame=false;
	}

	class WinThread extends Thread{
		public void run(){
			while(true){
				checkGameOver();
				repaint();
				try {
					Thread.sleep(100);
				}
				catch (InterruptedException e){

				}
			}
		}
	}

	public void paint(Graphics g){
		super.paint(g);
		g.setColor(Color.darkGray);
		g.fillRect(0,0,width,high);
		myTank.paintTank(g);
		for(Tank nowTank:tankList){
			nowTank.paintTank(g);
		}
		for(Bullet nowBullet:bulletList){
			nowBullet.paintBullet(g);
		}
	}

	private synchronized void checkGameOver(){
		if(myTank.getHP()<=0){
			gameFlag=-1;
		}
		else
		if(tankList.size()==0){
			gameFlag=1;
		}

		if(gameFlag!=0&&stopGame==false) {
			stopGame=true;
			int option=0;
			if (gameFlag < 0) {
				option = JOptionPane.showConfirmDialog(this, "恭喜!你输了!重来一遍?", "提示 ", JOptionPane.YES_OPTION);
			}
			if (gameFlag > 0) {
				option = JOptionPane.showConfirmDialog(this, "恭喜!你赢了!重来一遍?", "提示 ", JOptionPane.YES_OPTION);
			}
			if(option==0){
				gameFlag=0;
				reStartGame();
				return;
			}
			else
			{
				System.exit(0);
			}
		}

	}

	public static void main(String[]args){
		Window w1=new Window();
	}
}