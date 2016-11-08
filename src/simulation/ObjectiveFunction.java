package simulation;

import java.util.ArrayList;

import instance.Ambulance;
import instance.Call;

public class ObjectiveFunction {
	
	// Deadline for response / max response time
	private double d1;
	private double d2;
	private double d3;
	
	// Penalty for exceeding deadline
	private double t1;
	private double t2;
	private double t3;
	
	// The rate (degree of polynomial) of growth of tardiness penalty
	private int p1;
	private int p2;
	private int p3;
	
	/**
	 * 
	 * @param d1 The time to react on priority 1 calls in minutes
	 * @param d2 The time to react on priority 2 calls in minutes
	 * @param d3 The time to react on priority 3 calls in minutes
	 * @param t1 The cost of tardiness for priority 1 calls
	 * @param t2 The cost of tardiness for priority 2 calls
	 * @param t3 The cost of tardiness for priority 3 calls
	 * @param p1 The degree of growth of tardiness cost for priority 1 calls
	 * @param p2 The degree of growth of tardiness cost for priority 2 calls
	 * @param p3 The degree of growth of tardiness cost for priority 3 calls
	 */
	public ObjectiveFunction(double d1, double d2, double d3, double t1, double t2, double t3,
			int p1, int p2, int p3){
		this.d1=d1;
		this.d2=d2;
		this.d3=d3;
		this.t1=t1;
		this.t2=t2;
		this.t3=t3;
		this.p1=p1;
		this.p2=p2;
		this.p3=p3;
	}
	
	public double getCost(ArrayList<Ambulance> ams){
		double mins1 = 0;
		double mins2 = 0;
		double mins3 = 0;
		double tc1 = 0;
		double tc2 = 0;
		double tc3 = 0;
		
		for(Ambulance a : ams){
			ArrayList<Call> calls = a.getCalls();
			ArrayList<Double> arriveAt = a.getArriveAt();
			for(int i=0; i<calls.size(); i++){
				Call c = calls.get(i);
				double arriveTime = arriveAt.get(i);
				double reactTime = arriveTime-c.getCallTime();
				reactTime = reactTime*60;
				
				if(c.getPrio()==1){
					mins1 += reactTime;
					double tardy = Math.max(0, reactTime-d1);
					tc1 += t1*pow(tardy, p1);
							
				}else if(c.getPrio()==2){
					mins2 += reactTime;
					double tardy = Math.max(0, reactTime-d2);
					tc2 += t2*pow(tardy, p2);
					
				}else if(c.getPrio()==3){
					mins3 += reactTime;
					double tardy = Math.max(0, reactTime-d3);
					tc3 += t3*pow(tardy, p3);
					
				}
				
			}
		}
		
		/*System.out.println("Cost:\n\t"+mins1+" mins (prio 1)\n\t"+mins2+" mins (prio 2)\n\t"+
		mins3+" mins (prio 3)\n\t"+tc1+" tardiness cost (prio 1)\n\t"+tc2+
		" tardiness cost (prio 2)\n\t"+tc3+" tardiness cost (prio 3)\n\t");
		*/
		return mins1+mins2+mins3+tc1+tc2+tc3;
	}
	
	private double pow(double val,int power){
		if(power<1){
			System.err.println("Attempted to use a negative power. Not supported.");
			System.exit(1);
		}
		if(power==1)
			return val;
		return val*pow(val,power-1); 
	}
	
}
