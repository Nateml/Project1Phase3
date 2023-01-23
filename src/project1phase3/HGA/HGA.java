package project1phase3.HGA;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map.Entry;

import project1phase3.HGA.TabuSearch.BlindTabuSearch;
import project1phase3.HGA.TabuSearch.TabuSearch;
import project1phase3.Shared.Graph;
import project1phase3.Shared.Vertex;

class SaturationComparator implements Comparator<Vertex> {
    // this class is used to sort the vertices by their saturation degree

    @Override
    public int compare(Vertex v1, Vertex v2) {
        return Integer.compare(v2.getSaturationDegree(), v1.getSaturationDegree());
    }
}

class NeighboursComparator implements Comparator<Vertex> {

    @Override
    public int compare(Vertex v1, Vertex v2) {
        return Integer.compare(v2.getAmountOfNeighbours(), v1.getAmountOfNeighbours());
    }
}

class conflictCountComparator implements Comparator<Entry<Vertex, Integer>> {
    // this class is used to sort vertices by the amount of neighbours they have in their colour class

    @Override
    public int compare(Entry<Vertex, Integer> e1, Entry<Vertex, Integer> e2) {
        return Integer.compare(e2.getValue(), e1.getValue());
    }
}

public class HGA {

    ArrayList<Vertex> v;
    int[][] p;
    int k;

    // parameters for tabu tenure
    int A = 10;
    double alpha = 0.6;

    public HGA(Graph graph) {
        this.v = graph.getVertices();
    }

    // this method is the "main" method of the class
    public int upperBound(int kcolours, int max_iterations, int lowerBound) {
        this.k = kcolours;
        boolean solutionFound = true;
        boolean bestSolutionFound = false;
        Configuration[] population = new Configuration[0];
        // the following loops until no solution is found with the k value, otherwise the k value gets decremented each iteration
        while (solutionFound && !bestSolutionFound) {
            population = initPopulation(10, k);
            for (Configuration configuration : population) {
                if (configuration.getTotalConflictCount()==0) {
                    solutionFound = true;
                    if (k == lowerBound) {
                        bestSolutionFound = true;
                    }
                    k = configuration.partition.size()-1;
                    System.out.println("NEW BEST UPPER BOUND = " + (k+1));
                    break;
                } else {
                    solutionFound = false;
                    if (k == 1) return k + 1;
                }
            }
            // if initiliazing the population did not find a solution, then perform the genetic operation:
            if (!solutionFound) {
                for (int i = 0; i < max_iterations; i++) {
                    Configuration[] parents = chooseParents(population);
                    Configuration child = crossover_and_replace(parents[0], parents[1], population);
                    int childConflictCount = child.getTotalConflictCount();
                    if (childConflictCount == 0) {
                        System.out.println("NEW BEST UPPER BOUND = "  + k);
                        if (k == lowerBound) {
                            bestSolutionFound = true;
                        }
                        k -=1;
                        solutionFound = true;
                        break;
                    }
                }
            }
        }
        return k+1;

    }

    private Configuration crossover_and_replace(Configuration parent1, Configuration parent2, Configuration[] population) {
        // this method is responsible for creating a child from two parent configurations, and then replacing one of the parents with the child after a local search operation on the child
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
            }
        }
        for (int i = 0; i < newParent2.partition.size(); i++) {
            final int i2 = i;
            for (int index = 0; index < newParent2.partition.get(i).size(); index++) {
                final int index2 = index;
                if (!unassignedVertices.stream().anyMatch(element -> element.identification() == newParent2.partition.get(i2).get(index2).identification())) {
                    unassignedVertices.add(newParent1.partition.get(i).get(index));
                }
            }
        }
        // randomnly assign unassigned vertices
        for (Vertex v : unassignedVertices) {
            int rNum = (int) (Math.random() * originalParent1.partition.size());
            child.partition.get(rNum).add(v);
        }

        // tabu search child
        BlindTabuSearch blindts = new BlindTabuSearch(child);
        child = blindts.run(100);
        TabuSearch ts = new TabuSearch(child);
        child = ts.run(500);
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
