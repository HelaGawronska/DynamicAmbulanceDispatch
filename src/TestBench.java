import generator.CallGenerator;
import instance.Ambulance;
import instance.Call;
import instance.Instance;
import instance.Site;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

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
    private static Distribution timeDist = new Uniform(0, 8);

    public static void main(String[] args) throws IOException {


        String outputfilename = "statistics.txt";
        File file = new File(outputfilename);
        FileWriter writer = new FileWriter(file);
        file.createNewFile();

        String outputfilename2 = "finalVal.txt";
        File file2 = new File(outputfilename2);
        FileWriter writer2 = new FileWriter(file2);
        file2.createNewFile();

        //first line of text file
        writer.write("call number \t priority \t X \t Y \t distance site 1 \t distance site 2 " +
                "\t distance site 3 \t distance site 4 \t distance site 5 \t distance site 6 \t distance site 7 \t" +
                "s1 ring nr \t s2 ring nr \t s3 ring nr \t s4 ring nr \t s5 ring nr \t s6 ring nr \t s7 ring nr " +
                "\t number of sites \t sp1 \t sp2 \t sp3 \t sp4 \t sp5 \t sp6 \t sp7 \r\n");

        writer2.write("ambulance per h. \t ambulance 1 \t ambulance 2 \t ambulance 3 \t ambulance 4 \t ambulance 5" +
                " \t ambulance 6 \t ambulance 7 \t \n");

        for (int q = 0; q < 3670; q++) {
            Instance inst = getScenarioA();


            ArrayList<Call> calls = new ArrayList<Call>(inst.getCalls());
            ArrayList<Site> sites = new ArrayList<Site>(inst.getSites());

            Call c;
            ArrayList<Integer> ring = new ArrayList<>();
            ArrayList<Double> sharingPerc = new ArrayList<>();
            ArrayList<Double> accSharingPerc = new ArrayList<>();

            for (int n = 0; n <= sites.size()-1; n++){
                accSharingPerc.add(0.0);
            }


            System.out.println(calls.size());
            int counter = 0;

            for (int i = 0; i < calls.size(); i++) {
                c = calls.get(i);
                for (int j = 0; j <= sites.size() - 1; j++) {
                    ring.add(ringNumber(sites.get(j), c));
                }

                counter = numbSite(c, sites);
                if (counter != 0) {
                    for (int j = 0; j <= ring.size() - 1; j++) {
                        sharingPerc.add((double) ring.get(j) / counter / c.getPrio());
                    }
                } else {
                    for (int j = 0; j <= sites.size() - 1; j++) {
                        sharingPerc.add(0.0);
                    }
                }

                for (int j = 0; j <= sites.size() - 1; j++) {
                    double temporaryVariable=accSharingPerc.get(j)+sharingPerc.get(j);
                    accSharingPerc.set(j, temporaryVariable);
                }




                writer.write(i + "\t" + c.getPrio() + "\t" + c.getX() + "\t" + c.getY() + "\t" + sites.get(0).distanceTo(c)
                        + "\t" + sites.get(1).distanceTo(c) + "\t" + sites.get(2).distanceTo(c) + "\t" + sites.get(3).distanceTo(c) +
                        "\t" + sites.get(4).distanceTo(c) + "\t" + sites.get(5).distanceTo(c) + "\t" + sites.get(6).distanceTo(c) +
                        "\t" + ring.get(0) + "\t" + ring.get(1) + "\t" + ring.get(2) + "\t" + ring.get(3) + "\t" + ring.get(4) +
                        "\t" + ring.get(5) + "\t" + ring.get(6) + "\t" + counter + "\t" + sharingPerc.get(0) + "\t" + sharingPerc.get(1)
                        + "\t" + sharingPerc.get(2) + "\t" + sharingPerc.get(3) + "\t" + sharingPerc.get(4) +
                        "\t" + sharingPerc.get(5) + "\t" + sharingPerc.get(6) + "\r\n");

                ring.clear();
                sharingPerc.clear();
            }

            writer.flush();

            int ambH = (int)calls.size()/8;
            double totPercent = 0.0;
            for (int n=0; n<=accSharingPerc.size()-1; n++){
                totPercent+=accSharingPerc.get(n);
            }

            for (int n=0; n<=accSharingPerc.size()-1; n++){
                accSharingPerc.set(n, accSharingPerc.get(n)/totPercent*ambH);
            }




        // Assigner and dispatcher: Create your own or modify
        Assigner assigner = new NaiveAssigner();
        //Dispatcher dispatcher = new NaiveDispatcher();
        Dispatcher dispatcher = new NaiveDispatcher();


        // Reaction time in hours for 1, 2 and 3.
        // A multiplier for tardiness penalty for 1, 2 and 3
        // The degree of the polynomial describing growth in tardiness cost
        double react = 10;
        ObjectiveFunction of = new ObjectiveFunction(react, react + 10, react + 15, 3, 2, 1, 5, 4, 3);

        // For now, try 100 ambulances
        ArrayList<Ambulance> ams = new ArrayList<Ambulance>();
        for (int i = 0; i < 2; i++) {
            ams.add(new Ambulance());
        }

        // Do the discrete event simulation
        DiscreteEventSimulator des = new DiscreteEventSimulator(inst, assigner, dispatcher, ams, of);
        des.simulate();

//		SolutionVisualizer.visualize(inst, des.getSolution(), "Solution");


            writer2.write(ambH + "\t" + accSharingPerc.get(0)  + "\t" + accSharingPerc.get(1)  + "\t" + accSharingPerc.get(2)
                    + "\t"+ accSharingPerc.get(3)  +"\t"+ accSharingPerc.get(4)  + "\t"+ accSharingPerc.get(5)
                    +"\t"+ accSharingPerc.get(6) + "\r\n");
            accSharingPerc.clear();
        }
        writer2.flush();
    }

    public static Instance getScenarioA() {
        Instance inst = new Instance();
        int nScale = 1;

        // The central city
        Distribution nDist = new Normal(nScale * 100, nScale * 25);
        inst.getCalls().addAll(getTown(nDist, 0, 0, 6.666666));

        // Big satellites
        nDist = new Normal(nScale * 30, nScale * 10);
        inst.getCalls().addAll(getTown(nDist, 13, 0, 4.4));
        inst.getCalls().addAll(getTown(nDist, 0, 13, 4.4));
        inst.getCalls().addAll(getTown(nDist, -10, -10, 4.4));

        // Smaller satellites
        nDist = new Normal(nScale * 15, nScale * 5);
        inst.getCalls().addAll(getTown(nDist, 26, 0, 4.4));
        inst.getCalls().addAll(getTown(nDist, 20, 10, 4.4));
        inst.getCalls().addAll(getTown(nDist, -10, -16, 4.4));

        // Add sites
        inst.addSite(new Site(0, 0));
        inst.getSites().get(inst.getSites().size() - 1).setStdDev(6.6);

        inst.addSite(new Site(13, 0));
        inst.getSites().get(inst.getSites().size() - 1).setStdDev(4.4);

        inst.addSite(new Site(0, 13));
        inst.getSites().get(inst.getSites().size() - 1).setStdDev(4.4);

        inst.addSite(new Site(-10, -10));
        inst.getSites().get(inst.getSites().size() - 1).setStdDev(4.4);

        inst.addSite(new Site(26, 0));
        inst.getSites().get(inst.getSites().size() - 1).setStdDev(4.4);

        inst.addSite(new Site(20, 10));
        inst.getSites().get(inst.getSites().size() - 1).setStdDev(4.4);

        inst.addSite(new Site(-10, -16));
        inst.getSites().get(inst.getSites().size() - 1).setStdDev(4.4);
        return inst;
    }

    public static ArrayList<Call> getTown(Distribution nDist, double x, double y, double stdDev) {
        int nCalls = ((Double) nDist.sample()).intValue();

        Distribution xDist = new Normal(x, stdDev);
        Distribution yDist = new Normal(y, stdDev);
        CallGenerator callGen = new CallGenerator(timeDist, xDist, yDist, prioDist);

        return callGen.getCalls(nCalls);
    }

    public static int ringNumber(Site s, Call c) {
        double scale = s.getStdDev();
        double dist = s.distanceTo(c);
        if (dist > scale * 3) {
            return 0;
        } else if (dist <= scale * 3 && dist > scale * 2) {
            return 1;
        } else if (dist <= scale * 2 && dist > scale) {
            return 2;
        } else if (dist == 0) {
            return 4;
        } else {
            return 3;
        }
    }

    public static int numbSite(Call c, ArrayList<Site> sites) {
        int counter = 0;
        for (Site s : sites) {
            if (ringNumber(s, c) > 0) {
                counter++;
            }
        }
        return counter;
    }


}
