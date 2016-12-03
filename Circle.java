package tank;

/**
 * Created by lu on 16-11-21.
 */
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