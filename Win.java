package tank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by lu on 16-10-31.
 */

public class Win extends JFrame implements WinSize{

	private boolean OPEN_AI=true;
	private boolean OPEN_BLOCKS=true;

	final static int otherTankNumber=3;
	private int gameFlag=0;
	Tank myTank=new Tank(0,0,this);
	LinkedList<Tank>tankList=new LinkedList<Tank>();
	ArrayList<Block>blockList=new ArrayList<Block>();
	private boolean stopGame;

	private WinThread winThread;
	private TankAi tanAi;
	private Clear checkHit;
	private Control control;

	public Win(){
		super("tank");
		setBounds(100,100,WIN_WIDTH,WIN_HIGH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

		this.addKeyListener(new KeyAdapter() {
			private HashMap<Character,Boolean>order=new HashMap<Character, Boolean>();
			{
				order.put('W', false);
				order.put('A',false);
				order.put('S',false);
				order.put('D',false);
				order.put('J',false);
			}
			@Override
			public void keyPressed(KeyEvent keyEvent) {
				super.keyPressed(keyEvent);
				char key = Character.toUpperCase(keyEvent.getKeyChar());
				order.replace(key,true);
				if(key=='J'&&control.canFire) {
					control.canFire=false;
					myTank.getOrder(order);
				}
				if(key!='J'){
					myTank.getOrder(order);
				}
			}

			@Override
			public void keyReleased(KeyEvent keyEvent) {
				super.keyReleased(keyEvent);
				char key=Character.toUpperCase(keyEvent.getKeyChar());
				order.replace(key,false);
			}
		});

		startGame();
	}

	private void startGame(){
		Tank.setDefaultTank(tankList,this);
		myTank.setMyTank();
		if(OPEN_BLOCKS) {
			Block.setDefaultBlocks(blockList);
		}
		tanAi=new TankAi(this);//control the other tanks
		checkHit=new Clear(this);//check the bullet had hitted the tank?
		winThread=new WinThread();//check game is over and repaint
		control=new Control();

		control.start();
		winThread.start();
		checkHit.start();
		if(OPEN_AI) {
			tanAi.start();
		}
		stopGame=false;
	}

	private synchronized void reStartGame(){
		tankList.clear();
		Tank.setDefaultTank(tankList,this);
		myTank.setHP(100);
		stopGame=false;
	}

	private class Control extends Thread{
		final private int SLEEP_TIME=250;
		private boolean canFire=true;
		public void run(){
			while(true){
				try {
					sleep(SLEEP_TIME);
				} catch (InterruptedException e) {}
				canFire=true;
			}
		}
	}

	private class WinThread extends Thread{
		final private int SLEEP_TIME=150;
		public void run(){
			while(true){
				checkGameOver();
				repaint();
				try {
					Thread.sleep(SLEEP_TIME);
				}
				catch (InterruptedException e){

				}
			}
		}
	}

	public void paint(Graphics g){
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.darkGray);
		g2d.fillRect(0, 0, WIN_WIDTH, WIN_HIGH);
		for (Block block : blockList) {
			block.paintBlock(g);
		}

		myTank.paint(g2d);
		synchronized (tankList) {
			for (Tank nowTank : tankList) {
				nowTank.paint(g2d);
			}
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
		Window w1=new Win();
	}
}