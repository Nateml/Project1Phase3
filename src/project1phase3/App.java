package project1phase3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import project1phase3.BruteForce.BruteForce;
import project1phase3.HGA.HGA;
import project1phase3.LowerBound.LowerBound;
import project1phase3.Shared.Graph;

public class App {
    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to our graph colouring program!"); 
        System.out.println("First, enter the absolute path of the graph file: ");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String filename = br.readLine();
        //br.close();

        System.out.println("Reading file...");
        Graph graph = new Graph(filename);
        graph.analyseAndPrune();

        System.out.println("Which algorithm would you like to use?"); 
        System.out.println("1. Brute Force (Backtracking)");
        System.out.println("2. Lower Bound (Bronker Bosch)");
        System.out.println("3. Upper Bound (HEA)");

        br = new BufferedReader(new InputStreamReader(System.in));
        String input = br.readLine();
        while (!input.equals("1") && !input.equals("2") && !input.equals("3")) {
            System.out.println("Please input a valid option (1, 2 or 3).");
            br = new BufferedReader(new InputStreamReader(System.in));
            input = br.readLine();
        }


        LowerBound lb = new LowerBound();
        int lowerbound;
        switch(input.charAt(0)) {
            case '1':
                BruteForce bf = new BruteForce();
                lb = new LowerBound();
                lowerbound = lb.getLowerBound(graph);
                int k = bf.chromaticNumber(graph, lowerbound, false);
                System.out.println("CHROMATIC NUMBER = " + k);
                break;
            case '2':
                lb = new LowerBound();
                lowerbound = lb.getLowerBound(graph);
                System.out.println("LOWERBOUND = " + lowerbound);
                break;
            case '3':
                HGA hga = new HGA(graph);
                int upperbound = hga.upperBound(graph.getVertices().size(), 100, 1);
                break;
        }

        br.close();
    }
}
