package instance;

/**
 * Created by hela on 11/11/16.
 */
public class Tabu implements Comparable<Tabu>{

    public int siteId;
    public double distToSite;
    public boolean reachedOnTime;


    public Tabu(Site s, Call c) {
        this.siteId = s.getId();
        this.distToSite = s.distanceTo(c);

        if ((c.getPrio() == 1 && distToSite / 80 <= 10) || (c.getPrio() == 2 && distToSite / 80 <= 20) || (c.getPrio() == 3 && distToSite / 80 <= 25)) {
            this.reachedOnTime =  true;
        } else {
            this.reachedOnTime = false;
        }

    }

    public int compareTo(Tabu t) {
        return this.distToSite < t.distToSite ?-1:  this.distToSite > t.distToSite ?1:0;
    }


}
