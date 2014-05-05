package at.htl.smartbot;

import java.util.ArrayList;

/**
 * Test Class
 * @author Jakob Ecker & Dominik Simon
 *
 */
public class Test {

	public static void main(String[] args) {

		Point m1 = new Point(1, 1);
		Point m2 = new Point(3, 1);
		Point m3 = new Point(1.5, 3);
		
		Point t1= new Point(0.15,1.53);
		Point t2= new Point(2,1.06);
		Point t3= new Point(2.25,1);
		Point t4= new Point(2.7,1.4);
		
		Trilateration.trilaterate(m1, m2, m3, 1, 0.5, 2);
		
		System.out.println(m1.equals(m3));
		
		ArrayList<Point> test = new ArrayList<Point>();
		
		test.add(t1);
		test.add(t2);
		test.add(t3);
		test.add(t4);
		
		Utils.getCentroidOfSmallestTriangle(test);

	}

}