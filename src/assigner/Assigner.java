package assigner;

import java.util.ArrayList;

import instance.Ambulance;
import instance.Instance;

public interface Assigner {
	/**
	 * Assign a set of ambulances to the sites in an instance
	 * @param inst The instance to assign ambulances to
	 * @param ambulances The ambulances to assign to the instance
	 */
	public void assign(Instance inst, ArrayList<Ambulance> ambulances);
}
