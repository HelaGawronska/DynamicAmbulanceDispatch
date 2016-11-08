package generator;

import instance.Call;

import java.util.ArrayList;

import distributions.Distribution;

public class CallGenerator {

	private Distribution timeDist;
	private Distribution xDist;
	private Distribution yDist;
	private Distribution prioDist;
		
	public CallGenerator(Distribution timeDist, Distribution xDist, Distribution yDist, Distribution prioDist){
		this.timeDist = timeDist;
		this.xDist = xDist;
		this.yDist = yDist;
		this.prioDist = prioDist;
	}
	
	/**
	 * Get a number of calls
	 * @param n The number of calls to generate
	 * @return A list of calls
	 */
	public ArrayList<Call> getCalls(int n){
		ArrayList<Call> calls = new ArrayList<Call>();
		for(int i=0; i<n; i++){
			double time = timeDist.sample();
			double x = xDist.sample();
			double y = yDist.sample();
			int prio = ((Double)prioDist.sample()).intValue();
			calls.add(new Call(x, y, time, prio));
		}
		return calls;
	}
}
