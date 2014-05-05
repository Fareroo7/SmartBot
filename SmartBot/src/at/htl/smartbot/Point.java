package at.htl.smartbot;

/**
 * Stores coordinates of a single Point that provides custom toString and equals
 * @author Jakob Ecker & Dominik Simon
 *
 */
public class Point {
	private double pos_x;
	private double pos_y;
	
	public Point(double x, double y){
		pos_x=x;
		pos_y=y;
	}
	public Point(){
		
	}
	
	public double getX() {
		return pos_x;
	}
	public void setX(double pos_x) {
		this.pos_x = pos_x;
	}
	public double getY() {
		return pos_y;
	}
	public void setY(double pos_y) {
		this.pos_y = pos_y;
	}
	@Override
	public String toString() {
		return "Point ("+Utils.round(pos_x)+"|"+Utils.round(pos_y)+")";
	}
	/**
	 * Returns true if x and y coordinates are equal
	 * @param p second point to compare
	 * @return true if equal else false
	 */
	public boolean equals(Point p){
		return this.pos_x==p.getX()&&this.pos_y==p.getY();
	}

}