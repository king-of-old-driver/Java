package tank;

import exp7.Main;

/**
 * Created by lu on 16-11-21.
 */
public class Line {
	private int stX,stY,endX,endY;
	public Line(int stX,int stY,int endX,int endY){
		this.stX=stX;
		this.stY=stY;
		this.endX=endX;
		this.endY=endY;
	}

	public int getStX() {
		return stX;
	}
	public int getStY(){
		return stY;
	}

	public int getEndX() {
		return endX;
	}

	public int getEndY() {
		return endY;
	}
	public int getVectorX(){
		return endX-stX;
	}
	public int getVectorY(){
		return endY-stY;
	}
	public static double corss(Line a,Line b){
		return Math.abs(a.getVectorX()*b.getVectorY()-a.getVectorY()*b.getVectorX());
	}
	public double getDis(){
		return Math.sqrt((endX-stX)*(endX-stX)+(endY-stY)*(endY-stY));
	}
	public boolean isHit(int x,int y,double r){
		double newR=r;
		Line line1 = new Line(x, y, stX,stY);
		Line line2 = new Line(x, y, endX, endY);
		if((stX<x&&x<endX)||(stY<y&&y<endY)) {
			double cross = corss(line1, line2);
			double dis = getDis();
			double h = cross / dis;
			if (h < newR)
				return true;
		}
		else{
			double dis=Math.min(line1.getDis(),line2.getDis());
			if(dis<newR)
				return true;
		}
		return false;
	}
}
