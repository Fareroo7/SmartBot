package at.htl.EngineControl;

import java.io.IOException;

/**
 * This class contains methods to control the movement of SmartRobot.
 * 
 * @author Dominik Simon
 * @version 1.0
 */
public class EngineControl {
	
	/**
	 * Width of the SmartRobot in meters.
	 */
	public static final double ROBOT_WIDTH = 0.20;
	
	/**
	 * One of the five speed steps in meters per second.<br>
	 * Maximum speed.
	 */
	public static final double SPEED_MAX = 3.25; 
	
	/**
	 * One of the five speed steps in meters per second.<br>
	 */
	public static final double SPEED_FAST = SPEED_MAX * 4 / 5;
	
	/**
	 * One of the five speed steps in meters per second.
	 * Normal speed.
	 */
	public static final double SPEED_NORMAL = SPEED_MAX * 3 / 5;
	
	/**
	 * One of the five speed steps in meters per second.
	 */
	public static final double SPEED_SLOW = SPEED_MAX * 2 / 5;
	
	/**
	 * One of the five speed steps in meters per second.
	 * Minimum speed.
	 */
	public static final double SPEED_MIN = SPEED_MAX / 5;
	
	/**
	 * Diameter of the wheels from SmartRobot in meter.
	 */
	public static final double WHEEL_DIAMETER = 0.12;
	
	/**
	 * Maximum RPM of the SmartRobot's engines.
	 */
	public static final double MAX_RPM = 293;
	
	/**
	 * Gear ratio of the engines in 1:x.
	 */
	public static final double GEAR_RATIO = 34;
	
	/**
	 * Defines the maximum duty cycle of the hardware (arduino => 8bit).
	 */
	public static final int MAX_DUTY_CYCLE = 0xFF;
	
	private static double curveRadius = 0.5;
	private static double innerCurveRadius = curveRadius - (ROBOT_WIDTH / 2);
	private static double outerCurveRadius = curveRadius + (ROBOT_WIDTH / 2);
	
	private static double speed = SPEED_NORMAL;
	
	/**
	 * Calculates the RPM needed for committed speed.
	 * @param speed Speed in meters per second.
	 * @return Calculated RPM.
	 */
	private static double speedToRPM(double speed){
		return speed / (WHEEL_DIAMETER * Math.PI) * GEAR_RATIO;
	}
	
	/**
	 * Calculates the PWM duty cycle (8bit) for arduino.
	 * @param rpm Rotation per minute.
	 * @return Duty cycle (max 255).
	 */
	private static int rpmToDutyCycle(double rpm){
		return (int) Math.round((MAX_DUTY_CYCLE/MAX_RPM)*rpm);
	}
	
	/**
	 * Calculates the PWM duty cycle (8bit) for the arduino.
	 * @param speed Speed in meter per second.
	 * @return Duty cycle (max 255).
	 */
	public static int speedToDutyCycle(double speed){
		return speed <= SPEED_MAX ? rpmToDutyCycle(speedToRPM(speed)) : (int) SPEED_MAX;
	}
	
	/**
	 * Calculates the needed time for the given distance
	 * @param distance Distance in mm.
	 * @return The needed time in milliseconds.
	 */
	private static int getTimeToDrive(int distance){
//		v = s / t => t = s / v
		return (int) Math.round(distance / speed);
	}
	
	public void driveForward(final int distance){
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				System.out.println(speedToDutyCycle(speed));
				int mills = getTimeToDrive(distance);
				try {
					Thread.sleep(mills);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("stop! " + "sek.: " + ((double)mills / 1000));
			}
		};
		new Thread(r).start();
	}
	
	public static void main(String args[]){
		EngineControl controller = new EngineControl();
		controller.driveForward(1000);
	}
	
}