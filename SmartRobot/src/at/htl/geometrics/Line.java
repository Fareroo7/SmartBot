package at.htl.geometrics;

import at.htl.smartbot.Utils;

/**
 * This class represent a line by storing two points and the length of the line.
 *
 * @author Jakob Ecker
 * @author Domink Simon
 * @version 1.0
 */
public class Line {

	private Point point1;
	private Point point2;

	private double distance;

	/**
	 * Constructs a new Line-Object from two points and calculates the distance between them.
	 * @param point1 Point one as {@link Point}.
	 * @param point2 Point two as {@link Point}.
	 */
	public Line(Point point1, Point point2) {
		this.point1 = point1;
		this.point2 = point2;
		this.distance = Math.sqrt(Math.pow(point2.getX() - point1.getX(), 2) + Math.pow(point2.getY() - point1.getY(), 2));
	}

	/**
	 * Constructs a new empty Line-Object.
	 */
	public Line() {

	}

	/**
	 * Recalculates the distance between the two points after setters.
	 */
	private void refreshDistance() {
		this.distance = Math.sqrt(Math.pow(point2.getX() - point1.getX(), 2) + Math.pow(point2.getY() - point1.getY(), 2));
	}

	/**
	 * Returns the first Point of the Line.
	 * @return First Point of the Line.
	 */
	public Point getPoint1() {
		return point1;

	}

	/**
	 * Sets the first Point of the Line.
	 * @param point1 First Point of the Line.
	 */
	public void setPoint1(Point point1) {
		this.point1 = point1;
		refreshDistance();
	}

	/**
	 * Returns the second Point of the Line.
	 * @return Second Point of the Line.
	 */
	public Point getPoint2() {
		return point2;
	}

	/**
	 * Sets the second Point of the Line.
	 * @param point2 New Point.
	 */
	public void setPoint2(Point point2) {
		this.point2 = point2;
		refreshDistance();
	}

	/**
	 * Returns the length of the Line.
	 * @return Length of the Line.
	 */
	public double getLength() {
		return distance;
	}
	
	/**
	 * Custom toString that returns a formatted Line.
	 */
	@Override
	public String toString() {
		return "Line [" + point1.toString() + ", " + point2.toString() + ", Distance=" + Utils.round(distance) + "]";
	}

	/**
	 * Calculates the {@link Point} of intersection of two lines.
	 * @param line1 Line one.
	 * @param line2 Line two.
	 * @return {@link Point} of intersection.
	 */
	public static Point getPointOfIntersectionLine(Line line1, Line line2) {

		double deltaY1, deltaY2, k_line1, d_line1, k_line2, d_line2, x, y;
		
		double deltaX1 = line1.getPoint2().getX() - line1.getPoint1().getX();
		double deltaX2 = line2.getPoint2().getX() - line2.getPoint1().getX();
		
		if (deltaX1 == 0 || deltaX2 == 0) {

			Point line1A = new Point(line1.getPoint1().getY(), line1.getPoint1().getX());
			Point line1B = new Point(line1.getPoint2().getY(), line1.getPoint2().getX());

			Point line2A = new Point(line2.getPoint1().getY(), line2.getPoint1().getX());
			Point line2B = new Point(line2.getPoint2().getY(), line2.getPoint2().getX());

			deltaX1 = line1B.getX() - line1A.getX();
			deltaY1 = line1B.getY() - line1A.getY();
			deltaX2 = line2B.getX() - line2A.getX();
			deltaY2 = line2B.getY() - line2A.getY();
			k_line1 = deltaY1 / deltaX1;
			d_line1 = line1A.getY() - (k_line1 * line1A.getX());
			k_line2 = deltaY2 / deltaX2;
			d_line2 = line2A.getY() - (k_line2 * line2A.getX());
			x = (d_line2 - d_line1) / (k_line1 - k_line2);
			y = k_line1 * x + d_line1;
			
			return new Point(y, x);
		} else {

			deltaY1 = line1.getPoint2().getY() - line1.getPoint1().getY();
			deltaY2 = line2.getPoint2().getY() - line2.getPoint1().getY();
			k_line1 = deltaY1 / deltaX1;
			d_line1 = line1.getPoint1().getY() - (k_line1 * line1.getPoint1().getX());
			k_line2 = deltaY2 / deltaX2;
			d_line2 = line2.getPoint1().getY() - (k_line2 * line2.getPoint1().getX());
			x = (d_line2 - d_line1) / (k_line1 - k_line2);
			y = k_line1 * x + d_line1;

			return new Point(x, y);
		}
	}
	
	/**
	 * Returns the vector in polar form that represents the line.
	 * @return {@link PolarVector} that is equal to the line.
	 */
	public PolarVector getPolarVektor(){	
		return new CartesianVector(this.point1.getDeltaX(point2), this.point1.getDeltaY(point2)).toPolarVector();
	}


	/**
	 * Returns the vector in cartesian form that represents the line.
	 * @return {@link CartesianVector} that is equal to the line.
	 */
	public CartesianVector getCartesianVector(){
		return new CartesianVector(this.point1.getDeltaX(point2), this.point1.getDeltaY(point2));
	}
}
