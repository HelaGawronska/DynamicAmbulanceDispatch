package instance;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

public class Call implements Comparable<Call>{
	private double x;
	private double y;
	private double callTime;
	private int prio;
	private static final DecimalFormat myFormatter = new DecimalFormat("###.##");

	public ArrayList<Tabu> relationToSites = new ArrayList<>();


	
	public Call(double x, double y, double time, int prio){
		this.x = x;
		this.y = y;
		this.callTime = time;
		if(prio>3 || prio<1){
			System.err.println("Tried to set priority to "+prio+"\nOnly values between 1 and 3 included are accepted.");
			System.exit(1);
		}
		this.prio = prio;
        for (int i = 0; i <= 6; i++) {

        }
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



///////////////// 11.11 ///////////////////////////////////////////


    public void setRelationToSites(ArrayList<Site> sites) {
        for (Site s: sites) {
            this.relationToSites.add(new Tabu(s,this));
        }
        Collections.sort(relationToSites);
    }

	public int findBestAmbulance(ArrayList<Site> sites) {
        double bestTime = 10000;
        int bestId = 0;
        for (Site s: sites) {
            if (s.distanceTo(this)/80 + s.getFirstAvailableTime() < bestTime) {
                bestTime = s.distanceTo(this)/80 + s.getFirstAvailableTime();
                bestId = s.getId();
            }
        }
        return bestId;
    }

}
