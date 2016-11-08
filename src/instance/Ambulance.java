package instance;

import java.util.ArrayList;

public class Ambulance {
	private static int maxId=0;
	private int id;
	private Site site;
	private ArrayList<Call> calls = new ArrayList<Call>();
	private ArrayList<Site> fromSite = new ArrayList<Site>();
	private ArrayList<Double> arriveAt = new ArrayList<Double>();
	private ArrayList<Double> doneBy = new ArrayList<Double>();
	
	private final double speed = 80;
	private final double cleanTime = 0.25;
	private final double serviceTime = 0.1;
	private final double relocateDelay = 0.0/60.0;
	private double relocatedBy = -1;
	
	public Ambulance(){
		id = maxId++;
	}
	
	/**
	 * Assign this ambulance to service the call
	 * @param c The call to service
	 * @param time The current time
	 */
	public void serviceCall(Call c, double time){
		calls.add(c);
		double done = time;
		if(doneBy.size()>0 && doneBy.get(doneBy.size()-1)>time)
			done = doneBy.get(doneBy.size()-1);
		
		// Arrival at accident
		arriveAt.add(done + site.distanceTo(c)/speed);
		
		// Out, back, and cleaned
		done += cleanTime+serviceTime+(site.distanceTo(c)/speed)*2;
		
		this.doneBy.add(done);
		// Remember from which site for visualization
		fromSite.add(site);
	}
	
	/**
	 * 
	 * @return The list of calls serviced by the ambulance
	 */
	public ArrayList<Call> getCalls(){
		return calls;
	}
	
	/**
	 * 
	 * @return The list of sites the calls were served from
	 */
	public ArrayList<Site> getSites(){
		return fromSite;
	}
	
	/**
	 * 
	 * @return A list of when the ambulance arrived at the location specified in the calls
	 */
	public ArrayList<Double> getArriveAt(){
		return arriveAt;
	}
	
	/**
	 * Set the site the ambulance is parked at
	 * @param s
	 */
	public void setSite(Site s){
		site = s;
	}
	
	/**
	 * 
	 * @return The site the ambulance is currently at
	 */
	public Site getSite(){
		return site;
	}
	
	/**
	 * 
	 * @return The earliest time the ambulance is available
	 */
	public double getReadyTime(){
		if(calls.size()==0)
			return -1;
		return Math.max(doneBy.get(doneBy.size()-1), relocatedBy);
	}
	
	/**
	 * Relocate an ambulance
	 * @param s The new site
	 * @param time The current time
	 */
	public void relocate(Site s, double time){
		// Different destination?
		if(s==site)
			return;
		// When can the ambulance be used again?
		double arrival = Math.max(time, getReadyTime());
		arrival += site.distanceTo(s)/speed+relocateDelay;
		relocatedBy = arrival;
		// Move the ambulance
		site.removeAmbulance(this);
		s.addAmbulance(this);
		site = s;
	}
	
	/**
	 * 
	 * @return The ambulance id
	 */
	public int getId(){
		return id;
	}
}
