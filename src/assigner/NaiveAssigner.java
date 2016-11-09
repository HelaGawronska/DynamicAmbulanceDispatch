package assigner;

import instance.Ambulance;
import instance.Instance;
import instance.Site;

import java.util.ArrayList;

public class NaiveAssigner implements Assigner{

	public NaiveAssigner(){
		
	}
	
	/**
	 * A simple assigner, spreading the ambulances approximately evenly.
	 */
	public void assign(Instance inst, ArrayList<Ambulance> ambulances){
		ArrayList<Site> sites = inst.getSites();
		for(int i=0; i<ambulances.size(); i++){
			sites.get(i % sites.size()).addAmbulance(ambulances.get(i));
		}
	}
}
