package dispatcher;

import instance.Call;
import instance.Site;

import java.util.ArrayList;

public interface Dispatcher {

	/**
	 * Select an ambulance and dispatch it for each call
	 * @param calls The calls to be serviced. (Usually just one)
	 * @param sites The sites from which the call might be serviced
	 * @param time The current time
	 * @return The time next dispatching should be done if no more calls come in.
	 */
	public double dispatch(ArrayList<Call> calls, ArrayList<Site> sites, double time);
	
}
