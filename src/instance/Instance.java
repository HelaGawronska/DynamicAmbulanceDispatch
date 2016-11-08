package instance;

import java.util.ArrayList;
import java.util.Random;

public class Instance {
	// The random generator used in all distributions
	public static Random rand = new Random();
	
	// Instance specific data
	private ArrayList<Call> calls = new ArrayList<Call>();
	private ArrayList<Site> sites = new ArrayList<Site>();
	
	public Instance(){
		
	}
	
	public Instance(ArrayList<Call> calls, ArrayList<Site> zones){
		this.calls.addAll(calls);
		this.sites.addAll(zones);
	}
	
	/**
	 * Add a call to the instance
	 * @param c
	 */
	public void addCall(Call c){
		calls.add(c);
	}
	
	
	/**
	 * Add a site for ambulances to stay to the instance
	 * @param z
	 */
	public void addSite(Site z){
		sites.add(z);
	}
	
	/**
	 * 
	 * @return The maximal x coordinate in the instance
	 */
	public double getMaxX(){
		double maxX = Double.MIN_VALUE;
		for(Call c : calls){
			if(maxX<c.getX())
				maxX=c.getX();
		}
		for(Site z : sites){
			if(maxX<z.getX())
				maxX=z.getX();
		}
		return maxX;
	}
	
	/**
	 * 
	 * @return The minimum x coordinate in the instance
	 */
	public double getMinX(){
		double minX = Double.MAX_VALUE;
		for(Call c : calls){
			if(minX>c.getX())
				minX=c.getX();
		}
		for(Site z : sites){
			if(minX>z.getX())
				minX=z.getX();
		}
		return minX;
	}
	
	/**
	 * 
	 * @return The maximum Y coordinate of the instance
	 */
	public double getMaxY(){
		double maxY = Double.MIN_VALUE;
		for(Call c : calls){
			if(maxY<c.getY())
				maxY=c.getY();
		}
		for(Site z : sites){
			if(maxY<z.getY())
				maxY=z.getY();
		}
		return maxY;
	}
	
	/**
	 * 
	 * @return The minimum y coordinate of the instance
	 */
	public double getMinY(){
		double minY = Double.MAX_VALUE;
		for(Call c : calls){
			if(minY>c.getY())
				minY=c.getY();
		}
		for(Site z : sites){
			if(minY>z.getY())
				minY=z.getY();
		}
		return minY;
	}
	
	/**
	 * 
	 * @return The calls in the instance
	 */
	public ArrayList<Call> getCalls(){
		return calls;
	}
	
	/**
	 * 
	 * @return The sites in the instance
	 */
	public ArrayList<Site> getSites(){
		return sites;
	}
	
	/**
	 * Unassign all ambulances from the site. (To reset the instance)
	 */
	public void unassign(){
		for(Site s:sites){
			s.unassign();
		}
	}
}
