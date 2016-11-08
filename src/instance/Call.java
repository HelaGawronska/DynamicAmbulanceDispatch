package instance;

import java.text.DecimalFormat;

public class Call implements Comparable<Call>{
	private double x;
	private double y;
	private double callTime;
	private int prio;
	private static final DecimalFormat myFormatter = new DecimalFormat("###.##");
	
	public Call(double x, double y, double time, int prio){
		this.x = x;
		this.y = y;
		this.callTime = time;
		if(prio>3 || prio<1){
			System.err.println("Tried to set priority to "+prio+"\nOnly values between 1 and 3 included are accepted.");
			System.exit(1);
		}
		this.prio = prio;
	}
	
	/**
	 * 
	 * @return The x coordinate of the call
	 */
	public double getX() {
		return x;
	}

	/**
	 * 
	 * @return The y coordinate of the call
	 */
	public double getY() {
		return y;
	}

	/**
	 * 
	 * @return The time the call was made
	 */
	public double getCallTime() {
		return callTime;
	}

	/**
	 * 
	 * @return The priority p of the call: p in {1,2,3}
	 */
	public int getPrio() {
		return prio;
	}

	public String toString(){
		return "("+myFormatter.format(x)+","+myFormatter.format(y)+")@"+myFormatter.format(callTime)+" with prio "+prio;
	}

	@Override
	public int compareTo(Call o) {
		return ((Double)callTime).compareTo(o.getCallTime());
	}
}
