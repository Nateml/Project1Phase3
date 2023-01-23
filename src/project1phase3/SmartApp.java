package project1phase3;

import java.io.FileNotFoundException;

import project1phase3.HGA.HGA;
import project1phase3.LowerBound.LowerBound;
import project1phase3.Shared.Graph;
import project1phase3.BruteForce.BruteForce;
import project1phase3.HGA.Configuration;

public class SmartApp {
    
    /**
     * The main method of the program.
     * @param args should contain the path to the graph file as the first element
     */
    public static void main(String[] args) {

        Graph graph = null;
        boolean chromaticNumberIsFound = false;

        String filename = "./Tournament_TestSuite/phase3_2022_graph01.txt";
        
        try {
            graph = new Graph(filename);
            graph.analyseAndPrune();
        } catch (FileNotFoundException e) {
            System.out.println("Error, file not found.");
            System.exit(0);
        }

        BruteForce bf = new BruteForce();
        LowerBound lb = new LowerBound();
        HGA hga = new HGA(graph);

        if (graph.getVertices().size() < 100) {
            int lowerbound = lb.getLowerBound(graph);
            System.out.println("NEW BEST LOWER BOUND = " + lowerbound);
            // run brute force if chromatic number if the number of vertices is less than 100 and the lowerbound is less than 5
            if (lowerbound < 4) {
                int k = bf.chromaticNumber(graph, lowerbound, false);
                System.out.println("CHROMATIC NUMBER = " + k);
                chromaticNumberIsFound = true;
            } 
        } 
        if (!chromaticNumberIsFound) {
            int k = -1;
            // check if graph is bipartite (bipartite graphs cannot have odd cycles)
            if (graph.hasOddCycles == false) {
                k = bf.chromaticNumber(graph, 2, true);
            }
            if (k == 2) {
                System.out.println("CHROMATIC NUMBER = " + k);
            } else { 
                // DSatur
                Configuration config = new Configuration(graph.getVertices(), graph.getVertices().size());
                config.initDsatur();
                System.out.println("NEW BEST UPPER BOUND = " + config.partition.size());

                int lowerBound = 1;
                try {
                    graph = new Graph(filename);
                    graph.analyseAndPrune();
                    // Lowerbound
                    lowerBound = lb.getLowerBound(graph);
                    System.out.println("NEW BEST LOWER BOUND = " + lowerBound);
                } catch (FileNotFoundException e) {
                    System.out.println("Error, unable to read file.");
                    System.exit(0);
                }

                // Upperbound
                int upperBound = hga.upperBound(config.partition.size()-1, 100, lowerBound);
                System.out.println("NEW BEST UPPER BOUND = " + upperBound);

            }
        }


    }
}
