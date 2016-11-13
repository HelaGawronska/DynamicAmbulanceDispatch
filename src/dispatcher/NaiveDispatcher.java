package dispatcher;

import instance.Ambulance;
import instance.Call;
import instance.Site;

import java.util.ArrayList;

public class NaiveDispatcher implements Dispatcher {
    public static int jupi;
    public static int jupiMaybe = 0;
    public static int jupiTwice = 0;
    public static int Rune = 0;

    public NaiveDispatcher() {

    }

    /**
     * A simple dispatcher sending the first available ambulance
     */
    public double dispatch(ArrayList<Call> calls, ArrayList<Site> sites, double time) {


        for (Site s : sites) {
            for (int i = 0; i <= s.ambulances.size() - 1; i++) {
                s.ambulances.get(i).setIsFree(time);
//				if (!s.ambulances.get(i).isFree) {
//					System.out.println("ATT");
//				}
            }
        }

        for (Call c : calls) {

            //compute distances from sites and check if the call can be reached on time
            c.setRelationToSites(sites);

            //Rune's stuff
            double firstAvailable = Double.MAX_VALUE;
            int index = -1;
            boolean assigned = false;

            Site s;

            //assign to the closest that can be reach in time
            for (int i = 0; i <= c.relationToSites.size() - 1; i++) {
                s = sites.get(c.relationToSites.get(i).siteId);
                if (s.getNAmbulancesready(time) > 2 && c.relationToSites.get(i).reachedOnTime) {
                    if (s.getFirstAvailableTime() < firstAvailable) {
                        firstAvailable = s.getFirstAvailableTime();
                        index = c.relationToSites.get(i).siteId;
//                        System.out.println("jupiiii " + s + " index "+index);
                        assigned = true;
                        jupi++;
                        break;
                    }
                }

            }

            //if priority one, assign to the first available that can be reach on time and if this is impossible
            // assign to the best available in future
            if (!assigned && c.getPrio() == 1) {
                for (int i = 0; i <= c.relationToSites.size() - 1; i++) {
                    s = sites.get(c.relationToSites.get(i).siteId);
                    if (s.getNAmbulancesready(time) > 0 && c.relationToSites.get(i).reachedOnTime) {

                        firstAvailable = s.getFirstAvailableTime();
//                        System.out.println("jupiiii maybe " + c);
                        index = c.relationToSites.get(i).siteId;
//                        System.out.println("jupiiii maybe " + s + " index "+index);
                        assigned = true;
                        jupiMaybe++;
                        break;

                    } else {
                        s = sites.get(c.findBestAmbulance(sites));
                        firstAvailable = s.getFirstAvailableTime();
                        index = c.relationToSites.get(i).siteId; // TODO: 11/13/16 changes
//                        System.out.println("jupiiii twice " + s + " index "+index);

                        assigned = true;
                        jupiTwice++;
                        break;
                    }

                }
            }


            if (!assigned) {
                for (int i = 0; i <= sites.size() - 1; i++) {
//                    s = sites.get(i);
                    s = sites.get(c.findBestAmbulance(sites));

                    if (s.getFirstAvailableTime() < firstAvailable) {
                        firstAvailable = s.getFirstAvailableTime();
                        index = i;
//						System.out.println("done with Runes " + s + " index "+index);
                        Rune++;
                    }
                }

            }

//            System.out.println("last line index "+index);
            sites.get(index).getFirstAvailable().serviceCall(c, time);

        }

        calls.clear();


//		// Remove all calls from the list to signify that they are serviced
//		calls.clear();

        // Request to be recalled... Never
        return Double.MAX_VALUE;
    }
}
