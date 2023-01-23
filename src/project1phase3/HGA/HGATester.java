package project1phase3.HGA;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import project1phase3.Vertex;
import project1phase3.BruteForce;
import project1phase3.CycleDetector;
import project1phase3.Graph;
import project1phase3.GraphCleaner;
import project1phase3.LowerBound;

public class HGATester {
    static Vertex[] v;
    public static void main(String[] args) throws FileNotFoundException {
        //readGraph("C:/Users/natem/Projects/graph-colouring-group30_2022/GraphColouring/src/example_graphs/graph06_2022.txt");
        //readGraph("C:/Users/natem/Downloads/Tournament_TestSuite/phase3_2022_graph04.txt");
        //readGraph("C:/Users/natem/Downloads/DIMACS/queen13_13.col");
        //readGraph("C:/Users/natem/Downloads/DIMACS/le450_15c.col.txt");
        Graph graph = new Graph("./Tournament_TestSuite/phase3_2022_graph02.txt");
        GraphCleaner gc = new GraphCleaner();
        CycleDetector cycleDetector = new CycleDetector();
        cycleDetector.removeCycles(graph);
        //gc.removeCycles(graph);
        //gc.reduceNodes(graph);
        BruteForce bf = new BruteForce();
        //int chromatic = bf.chromaticNumber(graph, 2);

        //System.out.println("CHROMATIC NUMBER = " + chromatic);
        LowerBound lb = new LowerBound();
        int lowerbound = lb.getLowerBound(graph);
        System.out.println("LOWERBOUND = " + lowerbound);
        NewHGA hga = new NewHGA(graph);
        System.out.println(hga.upperBound(graph.getVertices().size(), 100));
        try {
            long average_crossover_time = hga.sum_time_crossover / hga.crossover_iterations;
            long average_tabu_time = hga.sum_time_tabu / hga.tabu_iterations;
            System.out.println("Average crossover time: " + average_crossover_time);
            System.out.println("Average tabu search time: " + average_tabu_time);
            System.out.println("Total time: " + hga.sum_total_time);
        } catch (ArithmeticException e) {
        }

    }

    private static void readGraph(String filename) throws FileNotFoundException {
        ArrayList<Vertex> vertices = new ArrayList<Vertex>();
        int numVertices = 0;
        int numEdges = 0;
        Scanner fileScanner = new Scanner(new File(filename));
        int lineNumber = 1;
        while (fileScanner.hasNext()) {
            if (lineNumber == 1) {
                String line = fileScanner.nextLine();
                System.out.println(line);
                numVertices = Integer.parseInt(line.split(" = ")[1]);
            } else if (lineNumber == 2) {
                numEdges = Integer.parseInt(fileScanner.nextLine().split(" = ")[1]);
            } else {
                String line = fileScanner.nextLine();
                int vertex1ID = Integer.parseInt(line.split(" ")[0]);
                int vertex2ID = Integer.parseInt(line.split(" ")[1]);
                Vertex vertex1 = new Vertex(0, vertex1ID);
                Vertex vertex2 = new Vertex(0, vertex2ID);
                boolean containsVertex1 = false;
                boolean containsVertex2 = false;
                for (Vertex vertex : vertices) {
                    if (vertex.equals(vertex1)) {
                        containsVertex1 = true;
                        vertex1 = vertex;
                    } else if (vertex.equals(vertex2)) {
                        containsVertex2 = true;
                        vertex2 = vertex;
                    }
                    if (containsVertex1 && containsVertex2) break;
                }
                vertex1.add_neighbour(vertex2);
                vertex2.add_neighbour(vertex1);
                if (!containsVertex1) {
                    vertices.add(vertex1);
                }
                if (!containsVertex2) {
                    vertices.add(vertex2);
                }

            }
            lineNumber++;
        }
        fileScanner.close();
        System.out.println("Finished reading in graph");
        
        HGATester.v = vertices.toArray(new Vertex[0]);
    }
}
