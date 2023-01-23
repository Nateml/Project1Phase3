package project1phase3;

import java.io.FileNotFoundException;

import project1phase3.HGA.NewHGA;
import project1phase3.HGA.Configuration;

public class App {
    
    public static void main(String[] args) {

        Graph graph = null;
        boolean chromaticNumberIsFound = false;
        
        try {
            graph = new Graph("./Tournament_TestSuite/phase3_2022_graph15.txt");
            graph.analyseAndPrune();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.exit(0);
        }

        BruteForce bf = new BruteForce();
        LowerBound lb = new LowerBound();
        NewHGA hga = new NewHGA(graph);

        if (graph.getVertices().size() < 100) {
            int lowerbound = lb.getLowerBound(graph);
            System.out.println(Math.pow(graph.getVertices().size(), lowerbound/10.0));
            System.out.println("NEW LOWERBOUND = " + lowerbound);
            if (lowerbound < 5) {
                System.out.println("running brute force because vertices are less than 100 and lowerbound is less than 5");
                int k = bf.chromaticNumber(graph, lowerbound, false);
                System.out.println("CHROMATIC NUMBER = " + k);
                chromaticNumberIsFound = true;
            } 
        } 
        if (!chromaticNumberIsFound) {
            int k = -1;
            if (graph.hasOddCycles == false) {
                k = bf.chromaticNumber(graph, 2, true);
            }
            System.out.println(k);
            if (k == 2) {
                System.out.println("CHROMATIC NUMBER = " + k);
            } else {
                
                // DSatur
                Configuration config = new Configuration(graph.getVertices(), graph.getVertices().size());
                config.initDsatur();
                System.out.println("UPPERBOUND = " + config.partition.size());

                // Lowerbound
                int lowerBound = lb.getLowerBound(graph);
                System.out.println("NEW LOWERBOUND = " + lowerBound);

                // Upperbound
                int upperBound = hga.upperBound(config.partition.size()-1, 100);
                System.out.println("NEW UPPERBOUND = " + upperBound);

            }
        }


    }
}
