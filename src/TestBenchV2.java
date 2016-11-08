import dispatcher.NaiveDispatcher;
import generator.CallGenerator;
import instance.Ambulance;
import instance.Call;
import instance.Instance;
import instance.Site;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import assigner.Assigner;
import assigner.NaiveAssigner;
import simulation.DiscreteEventSimulator;
import simulation.ObjectiveFunction;
import dispatcher.Dispatcher;
import distributions.Distribution;
import distributions.Normal;
import distributions.PrioDist;
import distributions.Uniform;


public class TestBenchV2 {
	private static Distribution prioDist = new PrioDist(0.53, 0.33, 0.14);
	private static Distribution timeDist = new Uniform(0,8);
	private static int react = 10;
	private static ObjectiveFunction of = new ObjectiveFunction(react, react+10, react+15, 3, 2, 1, 5, 4, 3);
	
	
	public static void main(String[] args) {
		
		
		// Create three lists, each containing the same number of elements: Assigner, dispatcher and numbe rof ambulances to use
		ArrayList<Assigner> assigners = new ArrayList<Assigner>();
		assigners.add(new NaiveAssigner());
		assigners.add(new NaiveAssigner());
		
		ArrayList<Dispatcher> dispatchers = new ArrayList<Dispatcher>();
		dispatchers.add(new NaiveDispatcher());
		dispatchers.add(new NaiveDispatcher());
		
		ArrayList<Integer> nAmbulances = new ArrayList<Integer>();
		nAmbulances.add(10);
		nAmbulances.add(100);
		
		// How many tests should be run
		int nTests = 10;
		
		// Get results and write them to a file. If you need more than cost, instructions can be found in the runTests function
		HashMap<String,ArrayList<ArrayList<Double>>> results = runTests(nTests, assigners, dispatchers, nAmbulances);
		print(results);
	}
	
	
	public static HashMap<String,ArrayList<ArrayList<Double>>> runTests(int nTests, ArrayList<Assigner> assigners, ArrayList<Dispatcher> dispatchers,
			ArrayList<Integer> nAmbulances){
		
		// Cost arrayList to return.
		HashMap<String, ArrayList<ArrayList<Double>>> results = new HashMap<String, ArrayList<ArrayList<Double>>>();
		
		// Create the required number of test instances
		ArrayList<Instance> instances = new ArrayList<Instance>();
		for(int i=0; i<nTests; i++){
			instances.add(getScenarioA());
		}
		
		// Check that the size of the lists match
		if(assigners.size()!=dispatchers.size() || assigners.size()!= nAmbulances.size()){
			System.err.println("The number of assigners, dispatchers and ambulance counts must be equal");
			System.exit(1);
		}
		
		// Run through all configurations
		for(int config=0; config<assigners.size(); config++){
			Assigner a = assigners.get(config);
			Dispatcher d = dispatchers.get(config);
			int nAmbulance = nAmbulances.get(config);
			
			// Run through all instances
			for(int instance=0; instance<instances.size(); instance++){
				System.out.println("Testing config: "+config+"\tinstance: "+instance);
				
				// Generate ambulances (As they are the solution, they must be generated each time)
				ArrayList<Ambulance> ambulances = new ArrayList<Ambulance>();
				for(int j=0; j<nAmbulance; j++){
					ambulances.add(new Ambulance());
				}
				
				instances.get(instance).unassign();
				
				// For convenience, create a new simulator
				DiscreteEventSimulator des = new DiscreteEventSimulator(instances.get(instance), a, d, ambulances, of);
				// Get the cost
				double cost = des.simulate();
				
				/* You may add more results by copying the line below and replacing "cost" and cost. Any new result type will
				 * be written to a file with the same name (fx. cost)				
				 */
				addResult(results, config, "cost", cost);
				
			}
		}
		return results;
	}
	
	/**
	 * Adds a result to a set (HashMap) of result tables, adding tables rows and columns as necessary
	 * @param results The set of result tables
	 * @param config The index of the configuration
	 * @param name The name of the result type (fx. cost)
	 * @param value The value to add
	 */
	public static void addResult(HashMap<String, ArrayList<ArrayList<Double>>> results, int config, String name, double value){
		// Check if we have a table for the name of the result, add one if not
		if(!results.containsKey(name))
			results.put(name, new ArrayList<ArrayList<Double>>());
		
		ArrayList<ArrayList<Double>> table = results.get(name);
		
		// Check that the table has a row for the config
		if(table.size() < (config+1)){
			table.add(new ArrayList<Double>());
			if(table.size()<config+1){
				System.err.println("Table size: "+table.size()+" config: "+config);
			}
		}

		table.get(config).add(value);
	}

	/**
	 * Write results to files, one for each result type. Columns are configs, rows are instances
	 * @param results
	 */
	public static void print(HashMap<String, ArrayList<ArrayList<Double>>> results){
		// For each result type
		for(String name : results.keySet()){
			try{
				ArrayList<ArrayList<Double>> table = results.get(name);
				PrintWriter pw = new PrintWriter(name+".csv", "UTF-8");
				int nConfigs = table.size();
				
				// Write the result table
				for(int i=0; i<table.get(0).size();i++){
					for(int c=0; c<nConfigs; c++){
						ArrayList<Double> column = table.get(c);
					
						 pw.write(column.get(i)+"");
						 
						 if(c<nConfigs-1)
							 pw.write(",");
					}
					pw.println();
				}
				pw.close();
			} catch (Exception e){
				e.printStackTrace();
				System.exit(1);
			}
		}
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
