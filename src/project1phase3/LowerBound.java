package project1phase3;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;


public class LowerBound{
    // now BronKerBosch will return an arrylist of sets

    private int maximumCliqueSizeCorrect = 0; // initializes the lower bound
    private int maximumCliqueSizeWithoutPivot = 0; // this is for the algorithm without pivot
    private LinkedHashMap<Integer, Vertex> vertexMap = new LinkedHashMap<>(); // initializes a hashmap where the key
    private LinkedHashSet<Integer> vertexSet;
    private Set<Integer> setForIntegers = new HashSet<Integer>(); // this is an empty set that is used for the union and intersection operations
    static ArrayList< LinkedHashSet<Integer>> cliquesList = new ArrayList<LinkedHashSet<Integer>>();
    // run lowerbound then type lowerBound.cliquelist to get all the maximal cliques

    //below is the algorithm with pivot
    //the inputs are linkedHashSets of Integers where the Integer is the vertex id
    //call with mode 0 to return the arraylist of sets and with mode 1 to call for the lower bound
    public void BronKerboschWithPivot (LinkedHashSet<Integer> growingClique, LinkedHashSet<Integer> possibleNodes, LinkedHashSet<Integer> exploredNodes, int mode){
        if(possibleNodes.size() == 0 && exploredNodes.size() == 0){
            if(mode == 0){
                cliquesList.add(growingClique);
            } else {
                if(growingClique.size()>maximumCliqueSizeCorrect){
                    maximumCliqueSizeCorrect = growingClique.size();
                }
            }
        } else{
            // choose a pivot from the set P U X as the vertex with the most neighbours
            // since the input is sorted on the amount of neighbours the first element is the biggest one
            Integer pivotNode = 0;
            if(vertexMap.get(possibleNodes.stream().findFirst().get()).getAmountOfNeighbours() > vertexMap.get(exploredNodes.stream().findFirst().get()).getAmountOfNeighbours()){
                pivotNode = possibleNodes.stream().findFirst().get();
            } else {
                pivotNode = exploredNodes.stream().findFirst().get();
            }
            // loop through all the nodes in possiblenodes without the neighbours of the pivot
            LinkedHashSet<Integer> nodesForLooping = new LinkedHashSet<>(possibleNodes);
            nodesForLooping.removeAll(vertexMap.get(pivotNode).getNeighboursAsLinkedHashSet());
            for(Integer node:nodesForLooping){
                // this below is the union of of node and growingClique, do we need to create a new one here or is it fine to just update
                LinkedHashSet<Integer> updatedGrowingClique = new LinkedHashSet<Integer>(growingClique);
                updatedGrowingClique.add(node);
                // this below is the intersect of possiblenodes and the neighbours of the node added to the clique
                LinkedHashSet<Integer> updatedPossibleNodes = new LinkedHashSet<Integer>(possibleNodes);
                updatedPossibleNodes.retainAll(vertexMap.get(node).getNeighboursAsLinkedHashSet());
                // this is the intersect of explorednodes and the neighbours of the node added to the clique
                LinkedHashSet<Integer> updatedExploredNodes = new LinkedHashSet<Integer>(exploredNodes);
                updatedExploredNodes.retainAll(vertexMap.get(node).getNeighboursAsLinkedHashSet());
                BronKerboschWithPivot(updatedGrowingClique,updatedPossibleNodes,updatedExploredNodes,mode);
                possibleNodes.remove(node);
                exploredNodes.add(node);
                // above is the backtracking step
                // if a node is explored it adds it to the explored nodes and removed from possible nodes so that it doesnt get explored again
            }
        }
    }


    public int getLowerBound(Vertex[] v){
        // put the arraylist into a hashmap and hashset of the graph
        for(Vertex vertex:v){
            Integer a = Integer.valueOf(vertex.i);
            vertexMap.put(a, vertex);
        }

        //MergeSort.mergesort(vertexMap);
        vertexSet = new LinkedHashSet<>(vertexMap.keySet());
        Integer[] vertexArray = new Integer[vertexSet.size()];
        vertexSet.toArray(vertexArray);
        Arrays.sort(vertexArray);
        vertexSet.clear();
        vertexSet.addAll(Arrays.asList(vertexArray));

        BronKerboschWithPivot(null, vertexSet, null, 1);
        int lowerBound = maximumCliqueSizeCorrect;
        return lowerBound;
    }
}