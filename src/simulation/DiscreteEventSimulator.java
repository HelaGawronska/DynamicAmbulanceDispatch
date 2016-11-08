package simulation;

import java.util.ArrayList;
import java.util.Collections;

import assigner.Assigner;
import dispatcher.Dispatcher;
import instance.Ambulance;
import instance.Call;
import instance.Instance;

public class DiscreteEventSimulator {
	private ObjectiveFunction of;
	private Assigner assigner;
	private Dispatcher dispatcher;
	private ArrayList<Ambulance> ambulances;
	private Instance inst;
	private double time;
	
	
	public DiscreteEventSimulator(Instance inst, Assigner a, Dispatcher d, ArrayList<Ambulance> ams, ObjectiveFunction of){
		assigner = a;
		ambulances = new ArrayList<Ambulance>(ams);
		assigner.assign(inst, ams);
		dispatcher = d;
		this.inst = inst;
		this.of = of;
		time = -1;
	}
	
	public double simulate(){
		// Create a copy of the list of calls and sort it
		ArrayList<Call> calls = new ArrayList<Call>(inst.getCalls());
		Collections.sort(calls);
		
		double nextRequest = Double.MAX_VALUE;
		
		ArrayList<Call> revealedCalls = new ArrayList<Call>();
		while(revealedCalls.size()+calls.size()>0){
			// Reveal next call if needed and progress time
			if(calls.size()>0 && calls.get(0).getCallTime()<nextRequest){
				nextRequest = calls.get(0).getCallTime();
				revealedCalls.add(calls.remove(0));
			}
			time = nextRequest;
			
			nextRequest = dispatcher.dispatch(revealedCalls, inst.getSites(), time);
		}
		
		return of.getCost(ambulances);
	}
	
	public ArrayList<Ambulance> getSolution(){
		return ambulances;
	}
}
