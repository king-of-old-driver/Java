package tank;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;

/**
 * Created by lu on 16-11-21.
 */
public class Block implements WinSize {
	private int x,y;
	private int width,high;
	private Color color=Color.BLACK;
	private Line[] lineArray=new Line[4];
	public Block(int x,int y,int width,int high,Color color){
		this.x=x;
		this.y=y;
		this.width=width;
		this.high=high;
		this.color=color;
		lineArray[0]=new Line(x,y,x+width,y);
		lineArray[1]=new Line(x,y+high,x+width,y+high);
		lineArray[2]=new Line(x,y,x,y+high);
		lineArray[3]=new Line(x+width,y,x+width,y+high);
	}
	public Block(int x,int y,int width,int high){
		this.x=x;
		this.y=y;
		this.width=width;
		this.high=high;
		lineArray[0]=new Line(x,y,x+width,y);
		lineArray[1]=new Line(x,y+high,x+width,y+high);
		lineArray[2]=new Line(x,y,x,y+high);
		lineArray[3]=new Line(x+width,y,x+width,y+high);
	}
	public void paintBlock(Graphics g){
		g.setColor(color);
		g.fillRect(x,y,width,high);
	}
	public static void setDefaultBlocks(List<Block>blockList) {
		final int blockBarWidth = 200,
				blockBarHigh = 50;
		blockList.add(new Block(150+blockBarWidth, 150-blockBarHigh, blockBarHigh, blockBarWidth));
		blockList.add(new Block(150, 150+blockBarWidth-2*blockBarHigh, blockBarWidth, blockBarHigh));
		blockList.add(new Block(WIN_WIDTH - 150 - blockBarWidth, WIN_HIGH - 150 - blockBarWidth, blockBarWidth, blockBarHigh));
		blockList.add(new Block(WIN_WIDTH - 150 - blockBarWidth, WIN_HIGH - 150 - blockBarWidth, blockBarHigh, blockBarWidth));
	}
	public Line[] getLineArray(){
		return lineArray;
	}
}
