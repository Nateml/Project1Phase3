package project1phase3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import project1phase3.HGA.NewHGA;

public class Tester {
    
    public static void main(String[] args) {
        runWithPruning();
    }

    private static void runWithPruning() {
        File graphFolder = new File("./Tournament_TestSuite");
        File[] graphFiles = graphFolder.listFiles();
        long[] pruneTimes = new long[graphFiles.length];
        long[] lowerboundTimes = new long[graphFiles.length];
        long[] upperboundTimes = new long[graphFiles.length];
        int index = 0;
        for (File file : graphFiles) {
            Graph graph = null;
            long pruneTime = 0;
            long lowerboundTime = 0;
            long upperboundTime = 0;
            try {
                for (int i = 0; i < 100; i++) {
                    graph = new Graph(file.getPath());
                    long pruneStartTime = System.nanoTime();
                    graph.analyseAndPrune();
                    pruneTime += System.nanoTime() - pruneStartTime;
                }
                pruneTime = pruneTime / 100;
            } catch (FileNotFoundException e) {
                System.out.println("File not found");
                System.exit(0);
            }

            LowerBound lb = new LowerBound();
            NewHGA hga = new NewHGA(graph);

            for (int i = 0; i < 10; i++) {
                long lowerboundStartTime = System.nanoTime();
                int lowerbound = lb.getLowerBound(graph);
                lowerboundTime += System.nanoTime() - lowerboundStartTime;
                System.out.println("LOWERBOUND = " + lowerbound);
            }
            lowerboundTime = lowerboundTime / 10;

            for (int i = 0; i < 10; i++) {
                long upperboundStartTime = System.nanoTime();
                int upperbound = hga.upperBound(graph.getVertices().size(), 2);
                upperboundTime += System.nanoTime() - upperboundStartTime;
                System.out.println("UPPERBOUND = " + upperbound);
            } 
            upperboundTime = upperboundTime / 10;

            pruneTimes[index] = pruneTime;
            lowerboundTimes[index] = lowerboundTime;
            upperboundTimes[index] = upperboundTime;

            System.out.println("COMPLETE");
            index++;

            try {
                FileWriter fw = new FileWriter("pruning_tests.csv", true);
                fw.write(file.getName() + "," + pruneTime + "," + lowerboundTime + "," + upperboundTime);
                fw.write(System.lineSeparator());
                fw.close();
            } catch (IOException e) {
                System.out.println("ERROR WRITING TO FILE");
                System.exit(0);
            }

        }

        /* 
        try {
            FileWriter fw = new FileWriter("pruning_tests.csv", true);
            fw.write("graph,pruning time (ns),lowerbound time (ns),upperbound time (ns)");
            fw.write(System.lineSeparator());
            for (int i = 0; i < graphFiles.length; i++) {
                fw.write(graphFiles[i].getName() + "," + pruneTimes[i] + "," + lowerboundTimes[i] + "," + upperboundTimes[i]);
            }
            fw.close();
            System.out.println("written data to file");
        } catch (IOException e) {
            System.out.println("FILE CANNOT BE CREATED");
        }
        */
    }
}
