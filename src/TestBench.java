import generator.CallGenerator;
import instance.Ambulance;
import instance.Call;
import instance.Instance;
import instance.Site;

import java.util.ArrayList;

import assigner.Assigner;
import assigner.NaiveAssigner;
import simulation.DiscreteEventSimulator;
import simulation.ObjectiveFunction;
import visualization.SolutionVisualizer;
import dispatcher.Dispatcher;
import dispatcher.NaiveDispatcher;
import distributions.Distribution;
import distributions.Normal;
import distributions.PrioDist;
import distributions.Uniform;


public class TestBench {
	private static Distribution prioDist = new PrioDist(0.53, 0.33, 0.14);
	private static Distribution timeDist = new Uniform(0,8);
	
	public static void main(String[] args) {
		
		Instance inst = getScenarioA();
		
		// Assigner and dispatcher: Create your own or modify
		Assigner assigner = new NaiveAssigner();
		//Dispatcher dispatcher = new NaiveDispatcher();
		Dispatcher dispatcher = new NaiveDispatcher();
		
		
		// Reaction time in hours for 1, 2 and 3.
		// A multiplier for tardiness penalty for 1, 2 and 3
		// The degree of the polynomial describing growth in tardiness cost
		double react = 10;
		ObjectiveFunction of = new ObjectiveFunction(react, react+10, react+15, 3, 2, 1, 5, 4, 3);
		
		// For now, try 31 ambulances
		ArrayList<Ambulance> ams = new ArrayList<Ambulance>();
		for(int i=0; i<70; i++){
			ams.add(new Ambulance());
		}

        System.out.println("n of calls before: " +inst.getCalls().size());
		// Do the discrete event simulation
		DiscreteEventSimulator des = new DiscreteEventSimulator(inst, assigner, dispatcher, ams, of);
		des.simulate();

        int counter =0;
        for (Ambulance a: ams) {
            counter+= a.getCalls().size();
        }
        System.out.println("n of calls after: " +counter);
        System.out.println("done with Runes " + NaiveDispatcher.Rune);
        System.out.println("jupi maybe " + NaiveDispatcher.jupiMaybe);
        System.out.println("jupiTwice " + NaiveDispatcher.jupiTwice);
        System.out.println("jupi " + NaiveDispatcher.jupi);

        System.out.println("cost: " +of.getCost(ams));
        System.out.println("solution: " +des.getSolution());
		SolutionVisualizer.visualize(inst, des.getSolution(), "Solution");
	}

	public static Instance getScenarioA(){
		Instance inst = new Instance();
		int nScale = 1;
		
		// The central city
		Distribution nDist = new Normal(nScale*100,nScale*25);
		inst.getCalls().addAll(getTown(nDist, 0, 0, 6.666666));
		
		// Big satellites
		nDist = new Normal(nScale*30,nScale*10);
		inst.getCalls().addAll(getTown(nDist, 13, 0, 4.4));
		inst.getCalls().addAll(getTown(nDist, 0, 13, 4.4));
		inst.getCalls().addAll(getTown(nDist, -10, -10, 4.4));
		
		// Smaller satellites
		nDist = new Normal(nScale*15,nScale*5);
		inst.getCalls().addAll(getTown(nDist, 26, 0, 4.4));
		inst.getCalls().addAll(getTown(nDist, 20, 10, 4.4));
		inst.getCalls().addAll(getTown(nDist, -10, -16, 4.4));
		
		// Add sites
		inst.addSite(new Site(0, 0));
		inst.getSites().get(inst.getSites().size()-1).setStdDev(6.6);
		
		inst.addSite(new Site(13, 0));
		inst.getSites().get(inst.getSites().size()-1).setStdDev(4.4);
		
		inst.addSite(new Site(0, 13));
		inst.getSites().get(inst.getSites().size()-1).setStdDev(4.4);
		
		inst.addSite(new Site(-10, -10));
		inst.getSites().get(inst.getSites().size()-1).setStdDev(4.4);
		
		inst.addSite(new Site(26,0));
		inst.getSites().get(inst.getSites().size()-1).setStdDev(4.4);
		
		inst.addSite(new Site(20,10));
		inst.getSites().get(inst.getSites().size()-1).setStdDev(4.4);
		
		inst.addSite(new Site(-10,-16));
		inst.getSites().get(inst.getSites().size()-1).setStdDev(4.4);
		return inst;
	}
	
	public static ArrayList<Call> getTown(Distribution nDist, double x, double y, double stdDev){
		int nCalls = ((Double)nDist.sample()).intValue();
		
		Distribution xDist = new Normal(x,stdDev);
		Distribution yDist = new Normal(y,stdDev);
		CallGenerator callGen = new CallGenerator(timeDist, xDist, yDist, prioDist);
		
		return callGen.getCalls(nCalls);
	}
}
