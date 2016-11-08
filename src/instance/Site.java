package instance;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Site {
	private static int maxId=0;
	private int id;
	
	private double x;
	private double y;
	private ArrayList<Ambulance> ambulances = new ArrayList<Ambulance>();
	
	// For visiualization
	private double stdDev = 4.4;
	
	private static final DecimalFormat myFormatter = new DecimalFormat("###.##");
	
	public Site(double x, double y){
		id = maxId++;
		this.x = x;
		this.y = y;
	}

	/**
	 * 
	 * @return The unique ID of the site (mostly for visualization)
	 */
	public int getId(){
		return id;
	}
	
	/**
	 * 
	 * @return The standard deviation of the calls distributed around the site.
	 */
	public double getStdDev(){
		return stdDev;
	}
	
	/**
	 * Set the standard deviation of visits distributed around the site 
	 * @param dev
	 */
	public void setStdDev(double dev){
		stdDev = dev;
	}
	
	/**
	 * 
	 * @return The x coordinate of the site the ambulances can stay
	 */
	public double getX(){
		return x;
	}
	
	/**
	 * 
	 * @return The y coordinate of the site the ambulances can stay
	 */
	public double getY(){
		return y;
	}
	
	/**
	 * Add an ambulance to the site
	 * @param a The ambulance to add
	 */
	public void addAmbulance(Ambulance a){
		ambulances.add(a);
		a.setSite(this);
	}
	
	/**
	 * Remove an ambulance from the site
	 * @param a The ambulance to remove
	 */
	public void removeAmbulance(Ambulance a){
		ambulances.remove(a);
	}
	
	/**
	 * Get the number of ambulances currently ready at the site
	 * @param time The current time
	 * @return The number of ambulances cuurently available.
	 */
	public int getNAmbulancesready(double time){
		int ready = 0;
		for(Ambulance a : ambulances){
			if(a.getReadyTime()<time)
				ready++;
		}
		return ready;
	}
	
	/**
	 * 
	 * @return The earliest time an ambulance will be available
	 */
	public double getFirstAvailableTime(){
		double ready = Double.MAX_VALUE;
		for(Ambulance a : ambulances){
			if(a.getReadyTime()<ready)
				ready = a.getReadyTime();
		}
		return ready;
	}
	
	/**
	 * Compute the distance from a site to a call
	 * @param c The call
	 * @return The distance
	 */
	public double distanceTo(Call c){
		double xd = x-c.getX();
		double yd = y-c.getY();
		return Math.sqrt(xd*xd+yd*yd);
	}

	/**
	 * Compute the distance to another site
	 * @param s The site to compute the distance to
	 * @return The distance
	 */
	public double distanceTo(Site s){
		double xd = x-s.getX();
		double yd = y-s.getY();
		return Math.sqrt(xd*xd+yd*yd);
	}
	
	/**
	 * 
	 * @return The ambulance that will be available the earliest
	 */
	public Ambulance getFirstAvailable(){
		double ready = Double.MAX_VALUE;
		int index = -1;
		for(int i=0; i< ambulances.size(); i++){
			Ambulance a = ambulances.get(i);
			if(a.getReadyTime()<ready){
				ready = a.getReadyTime();
				index = i;
			}
		}
		if(index==-1){
			System.err.println("Attempted to get fist available ambulance from asite without ambulances");
			System.exit(1);
		}
		return ambulances.get(index);
	}
	
	/**
	 * Unassign all ambulances from the site effectively resetting the site.
	 */
	public void unassign(){
		ambulances.clear();
	}
	
	public String toString(){
		return "Zone ("+myFormatter.format(x)+","+myFormatter.format(y)+")";
	}
}
