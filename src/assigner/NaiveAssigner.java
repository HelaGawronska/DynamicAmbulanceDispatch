package assigner;

import instance.Ambulance;
import instance.Instance;
import instance.Site;

import java.util.ArrayList;

public class NaiveAssigner implements Assigner{

    private int a1 = 10;//11; // number of ambulances in a site, defined by the statistics
    private int a2 = 10;//4;
    private int a3 = 10;//5;
    private int a4 = 10;//4;
    private int a5 = 10;//2;
    private int a6 = 10;//2;
    private int a7 = 10;//3;

    public ArrayList<Integer> ambulanceAssign = new ArrayList<>();

    public NaiveAssigner() {
        ambulanceAssign.add(a1);
        ambulanceAssign.add(a2);
        ambulanceAssign.add(a3);
        ambulanceAssign.add(a4);
        ambulanceAssign.add(a5);
        ambulanceAssign.add(a6);
        ambulanceAssign.add(a7);

    }

	
//	/**
//	 * A simple assigner, spreading the ambulances approximately evenly.
//	 */
//	public void assign(Instance inst, ArrayList<Ambulance> ambulances){
//		ArrayList<Site> sites = inst.getSites();
//		for(int i=0; i<ambulances.size(); i++){
//			sites.get(i % sites.size()).addAmbulance(ambulances.get(i));
//		}
//	}

    //Assigner that spreads according to the initial statistics
    public void assign(Instance inst, ArrayList<Ambulance> ambulances) {
        ArrayList<Site> sites = inst.getSites();
        int a = ambulances.size()-1;
        for (int i = 0; i < ambulanceAssign.size(); i++) {
            int counter = ambulanceAssign.get(i);
            while (counter != 0) {
                sites.get(i).addAmbulance(ambulances.get(a));
                System.out.println(ambulances.get(a)); // TODO: 11/13/16 changes
                a--;
                counter--;
            }
        }
    }
}
