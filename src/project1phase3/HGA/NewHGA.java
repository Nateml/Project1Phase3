package project1phase3.HGA;
import java.io.ObjectInputFilter.Config;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;


import project1phase3.HGA.Configuration;

import project1phase3.Vertex;

class SaturationComparator implements Comparator<Vertex> {
    // this class is used to sort the vertices by their saturation degree

    @Override
    public int compare(Vertex v1, Vertex v2) {
        return Integer.compare(v2.getSaturationDegree(), v1.getSaturationDegree());
    }
}

class conflictCountComparator implements Comparator<Entry<Vertex, Integer>> {
    // this class is used to sort vertices by the amount of neighbours they have in their colour class

    @Override
    public int compare(Entry<Vertex, Integer> e1, Entry<Vertex, Integer> e2) {
        return Integer.compare(e2.getValue(), e1.getValue());
    }
}

public class NewHGA {

    ArrayList<Vertex> v;
    int[][] p;
    int k;

    static long sum_time_crossover = 0;
    static int crossover_iterations = 0;

    static long sum_time_tabu = 0;
    static int tabu_iterations = 0;

    static long sum_total_time = 0;

    // parameters for tabu tenure
    int A = 10;
    double alpha = 0.6;

    public NewHGA(Vertex[] vertices) {
        this.v = new ArrayList<>();
        // create array of vertices using the information from the edges.
        // Also notes down that the vertices which are connected by the edge are neighbours.
        for (Vertex vertex : vertices) {
            v.add(vertex);
        }
    }

    // this method is the "main" method of the class
    public int upperBound(int kcolours, int max_iterations) {
        long start_time = System.nanoTime();
        this.k = kcolours;
        boolean solutionFound = true;
        Configuration[] population = new Configuration[0];
        // the following loops until no solution is found with the k value, otherwise the k value gets decremented each iteration
        while (solutionFound) {
            population = initPopulation(10, k);
            for (Configuration configuration : population) {
                if (configuration.getTotalConflictCount()==0) {
                    solutionFound = true;
                    k = configuration.partition.size()-1;
                    System.out.println("Found a solution for " + (k+1) + " colours.");
                    break;
                } else {
                    solutionFound = false;
                    if (k == 1) return k + 1;
                }
            }
            // if initiliazing the population did not find a solution, then perform the genetic operation:
            if (!solutionFound) {
                for (int i = 0; i < max_iterations; i++) {
                    //System.out.println(i);
                    //System.out.println("Genetic diversity: " + getGeneticDiversity(population));
                    Configuration[] parents = chooseParents(population);
                    Configuration child = crossover_and_replace(parents[0], parents[1], population);
                    //System.out.println(child.compare(parents[0]));
                    int childConflictCount = child.getTotalConflictCount();
                    if (childConflictCount == 0) {
                        System.out.println("Found a solution for "  + k + " colours, in " + i + " evolutionary iterations.");
                        k -=1;
                        solutionFound = true;
                        break;
                    }
                }
            }
        }
        long end_time = System.nanoTime() - start_time;
        sum_total_time += end_time;
        return k+1;

    }

    private int getGeneticDiversity(Configuration[] population) {
        int diversity = 0;
        int totalConflicts = 0;
        for (Configuration configuration : population) {
            totalConflicts += configuration.getTotalConflictCount();
            for (Configuration configuration2 : population) {
                if (configuration == configuration2) continue;
                diversity += configuration.compare(configuration2);
            }
        }
        System.out.println("Total conflict count: " + totalConflicts);
        return diversity;
    }

    private Configuration crossover_and_replace(Configuration parent1, Configuration parent2, Configuration[] population) {
        // this method is responsible for creating a child from two parent configurations, and then replacing one of the parents with the child after a local search operation on the child
        long start_time = System.nanoTime();
        Configuration child = new Configuration(v, k);
        Configuration originalParent1 = parent1.clone();
        Configuration originalParent2 = parent2.clone();
        ArrayList<ArrayList<Vertex>> newParent1Partition = new ArrayList<>();
        ArrayList<ArrayList<Vertex>> newParent2Partition = new ArrayList<>();
        for (int index = 0; index < parent1.partition.size(); index++) {
            newParent1Partition.add(new ArrayList<>());
            for (Vertex vertex : parent1.partition.get(index)) {
                newParent1Partition.get(index).add(vertex);
            }
        }
        for (int index = 0; index < parent2.partition.size(); index++) {
            newParent2Partition.add(new ArrayList<>());
            for (Vertex vertex : parent2.partition.get(index)) {
                newParent2Partition.get(index).add(vertex);
            }
        }
        Configuration newParent1 = parent1.clone();
        Configuration newParent2 = parent2.clone();

        for (int l = 0; l < parent1.partition.size(); l++) {
            Configuration currentParent;
            if (l % 2 == 0) { // if l is even
              currentParent = newParent1;
            } else { // if l is odd
              currentParent = newParent2;
            }
            ArrayList<Vertex> largestColourClass = currentParent.partition.get(0);
            for (ArrayList<Vertex> colourClass : currentParent.partition) {
                if (colourClass.size() > largestColourClass.size()) {
                    largestColourClass = colourClass;
                }
            }
            ArrayList<Vertex> newLargestColourClass = new ArrayList<>();
            for (int i = 0; i < largestColourClass.size(); i++) {
                newLargestColourClass.add(largestColourClass.get(i));
            }

            child.partition.add(newLargestColourClass);
            // remove vertices from parents
            newParent1.partition = newParent1.removeVertices(largestColourClass);
            newParent2.partition = newParent2.removeVertices(largestColourClass);
        }

        ArrayList<Vertex> unassignedVertices = new ArrayList<>();
        for (int i = 0; i < newParent1.partition.size(); i++) {
            final int i2 = i;
            for (int index = 0; index < newParent1.partition.get(i).size(); index++) {
                final int index2 = index;
                if (!unassignedVertices.stream().anyMatch(element -> element.identification() == newParent1.partition.get(i2).get(index2).identification())) {
                    unassignedVertices.add(newParent1.partition.get(i).get(index));
                }
                /* 
                if (!unassignedVertices.contains(newParent1.partition.get(i).get(index))) {
                    unassignedVertices.add(newParent1.partition.get(i).get(index));
                }
                */
            }
        }
        for (int i = 0; i < newParent2.partition.size(); i++) {
            final int i2 = i;
            for (int index = 0; index < newParent2.partition.get(i).size(); index++) {
                final int index2 = index;
                if (!unassignedVertices.stream().anyMatch(element -> element.identification() == newParent2.partition.get(i2).get(index2).identification())) {
                    unassignedVertices.add(newParent1.partition.get(i).get(index));
                }
                /* 
                if (!unassignedVertices.contains(newParent2.partition.get(i).get(index))) {
                    unassignedVertices.add(newParent2.partition.get(i).get(index));
                }
                */
            }
        }
        for (Vertex v : unassignedVertices) {
            int rNum = (int) (Math.random() * originalParent1.partition.size());
            child.partition.get(rNum).add(v);
        }

        parent1.getTotalConflictCount();

        // tabu search child
        //child = localSearch(child, 500);
        System.out.println("Started tabu search");
        BlindTabuSearch blindts = new BlindTabuSearch(child);
        child = blindts.run(300);
        TabuSearch ts = new TabuSearch(child);
        child = ts.run(5000);
        if (child.getTotalConflictCount() == 0) {
            return child;
        }
        
        // replace worse parent with child:
        int conflictcount1 = originalParent1.getTotalConflictCount();
        int conflictcount2 = originalParent2.getTotalConflictCount();
        if (conflictcount1 > conflictcount2){
            for(int i = 0; i < population.length;i++){
                if(population[i].partition.equals(originalParent1.partition)){
                    population[i] = child;
                    break;
                }}}
        if (conflictcount1 < conflictcount2){
            for(int i = 0; i < population.length;i++){
                if(population[i].partition.equals(originalParent2.partition)){
                    population[i] = child;
                    break;
                }}}
        if (conflictcount1 == conflictcount2){
            for(int i = 0; i < population.length;i++){
                if(population[i].partition.equals(originalParent2.partition)){
                    population[i] = child;
                    break;
                }}}


        long end_time = System.nanoTime() - start_time;
        sum_time_crossover += end_time;
        crossover_iterations++;

        //System.out.println(end_time);
        //System.out.println(child.getTotalConflictCount());
        return child;

    }

    private Configuration[] initPopulation(int population_size, int k) {
        // this method is responsible for creating the population set
        Configuration[] population = new Configuration[population_size];
        for (int i = 0; i < population_size; i++) {
            Configuration newConfig = new Configuration(v, k);
            newConfig.validate();
            newConfig.initDsatur();
            newConfig.validate();
            int totalConflictCount = newConfig.getTotalConflictCount();
            if (totalConflictCount != 0) {
                population[i] = newConfig;
            } else {
                population[0] = newConfig;
                break;
            }
        }
        return population;
    }

    /** 
    private Configuration localSearch(Configuration config, int iterations) {
        // implements the tabu search algorithm


        TabuList tabuList = new TabuList();
        Configuration bestConfig = config.clone();
        Configuration currentConfig = config.clone();

        while (iterations > 0) {
            long start_time = System.nanoTime();
            //System.out.println(iterations);
            List<Configuration> neighbours = currentConfig.getNeighbours();

            // remove neighbours that are in tabu list
            Predicate<Configuration> notInTabuList = configuration -> !tabuList.contains(configuration);
            neighbours = neighbours.stream().filter(notInTabuList).collect(Collectors.toList());

            // find best neighbour
            Collections.sort(neighbours, new Comparator<Configuration>() {
                @Override
                public int compare(Configuration c1, Configuration c2) {
                    return Integer.compare(c1.getTotalConflictCount(), c2.getTotalConflictCount());
                }
            });
            int lowestConflictCount = neighbours.get(0).getTotalConflictCount();
            Predicate<Configuration> byConflictCount = configuration -> configuration.getTotalConflictCount() <= lowestConflictCount;
            List<Configuration> newNeighbours = neighbours.stream().filter(byConflictCount).collect(Collectors.toList());
            Configuration bestNeighbour = newNeighbours.get((int)(Math.random() * newNeighbours.size()));

            // check if config is a complete solution
            if (bestNeighbour.getTotalConflictCount() == 0) {
                System.out.println("Found solution in " + (300 - iterations) + " iterations of tabu search");
                return bestNeighbour;
            }

            // check if best neighbouring config is better than the best config found
            if (bestNeighbour.getTotalConflictCount() <= bestConfig.getTotalConflictCount()) {
                bestConfig = bestNeighbour;
            }

            // update tabu list
            tabuList.update();

            // add config to tabu list
            tabuList.add(bestNeighbour);

            // update current config
            currentConfig = bestNeighbour;
            
            iterations--;
            long end_time = System.nanoTime() - start_time;
            sum_time_tabu += end_time;
            tabu_iterations++;
        }

        return bestConfig;

    }
    **/

    private Configuration[] chooseParents(Configuration[] population) {
        // return two elements of population 
        Configuration[] parents = new Configuration[2]; 
        Configuration randomParentOne = population[(int)(Math.random()*population.length)];
        Configuration randomParentTwo = population[(int)(Math.random()*population.length)];
        while (randomParentTwo==randomParentOne) {
        randomParentTwo = population[(int)(Math.random()*population.length)];  
        } 
        parents[0] = randomParentOne; 
        parents[1] = randomParentTwo;
        return parents; 
    }

}
