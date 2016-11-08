package distributions;

import instance.Instance;

public class PrioDist implements Distribution {
	
	double d1;
	double d2;
	
	/**
	 * 
	 * @param p1 The probability of having priority 1
	 * @param p2 The probability of having priority 2
	 * @param p3 The probability of having priority 3
	 */
	public PrioDist(double p1, double p2, double p3){
		if(p1+p2+p3!=1.0){
			throw new RuntimeException("The sum of priorities must be 1");
		}
		d1 = p1;
		d2 = p1+p2;
	}
	
	@Override
	public double sample() {
		double sample = Instance.rand.nextDouble();
		if(sample<d1)
			return 1;
		if(sample<d2)
			return 2;
		return 3;
	}
	
	@Override
	public String toString(){
		return "<Prio:["+d1+","+(d2-d1)+","+(1-d2)+"]>";
	}
	
}
