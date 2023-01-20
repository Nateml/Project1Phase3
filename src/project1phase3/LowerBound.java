package project1phase3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class LowerBound{

    private ArrayList<Vertex> vertices = new ArrayList<>();// creates the arraylist of the class vertex
    private int maximumCliqueSizeCorrect = 0; // initializes the lower bound
    private int maximumCliqueSizeWithoutPivot = 0; // this is for the algorithm without pivot
    private HashMap<Integer, Vertex> vertexMap = new HashMap<>(); // initializes a hashmap where the key 

    // the below method takes in a matrix as input and outputs the size of the largest array in the matrix
    // this is used to find the pivot in  possible nodes
    public static int getPivot(int[][] matrix){
        int[] newArray = new int[matrix.length];
        for(int i = 0; i < matrix.length; i ++){
            newArray[i] = matrix[i].length;
        }
        Arrays.sort(newArray);
        int pivot = newArray[newArray.length-1];
        return pivot;
    }
    //below is the algorithm with pivot
    public void BronKerboschWithPivot (int[] growingClique, int[] possibleNodes, int[]exploredNodes){
        if(possibleNodes.length == 0 && exploredNodes.length == 0){
            if(growingClique.length>maximumCliqueSizeCorrect){
                maximumCliqueSizeCorrect = growingClique.length;
            }
            // this is the base case in recursion, if all nodes are explored and it has found a maximal clique
            // it checks if this clique is bigger then the largest one found up to this clique
            // if yes then it updates this variable
        } else{
            // choose a pivot from the set P U X as the vertex with the most neighbours
            int pivotNode = ArrayMethods.union(possibleNodes, exploredNodes)[0]; // initialize the pivot
            for(int node: ArrayMethods.union(possibleNodes, exploredNodes)){ // loop through all the nodes and save the node that has the most neighbours
                if(vertexMap.get(node).getNeighboursAsIntArray().length > vertexMap.get(pivotNode).getNeighboursAsIntArray().length){
                    pivotNode = node;
                }
            }
            for(int node:ArrayMethods.subtractArrays(possibleNodes, vertexMap.get(pivotNode).getNeighboursAsIntArray())){
                int[] updatedGrowingClique = ArrayMethods.addToArray(growingClique, node); // this is the union of of node and growingClique
                int[] updatedPossibleNodes = ArrayMethods.intersectArrays(possibleNodes, vertexMap.get(node).getNeighboursAsIntArray()); // this is the intersect of possiblenodes and the neighbours of the node added to the clique
                int[] updatedExploredNodes = ArrayMethods.intersectArrays(exploredNodes, vertexMap.get(node).getNeighboursAsIntArray()); // this is the intersect of explorednodes and the neighbours of the node added to the clique
                BronKerboschWithPivot(updatedGrowingClique,updatedPossibleNodes,updatedExploredNodes);
                possibleNodes = ArrayMethods.removeElement(possibleNodes, node);
                exploredNodes = ArrayMethods.addToArray(exploredNodes, node);
                // above is the backtracking step
                // if a node is explored it adds it to the explored nodes and removed from possible nodes so that it doesnt get explored again
            }
        }
    }
    // below is the algorithm without pivot
    // this is done to compare the time between the two algorithms
    public void BronKerbosch (int[] growingClique, int[] possibleNodes, int[]exploredNodes){
        if(possibleNodes.length == 0 && exploredNodes.length == 0){
            if(growingClique.length>maximumCliqueSizeWithoutPivot){
                maximumCliqueSizeWithoutPivot = growingClique.length;
            }
            // this is the base case in recursion, if all nodes are explored and it has found a maximal clique
            // it checks if this clique is bigger then the largest one found up to this clique
            // if yes then it updates this variable
        } else{
            for(int node:possibleNodes){
                int[] updatedGrowingClique = ArrayMethods.addToArray(growingClique, node); // this is the union of of node and growingClique
                int[] updatedPossibleNodes = ArrayMethods.intersectArrays(possibleNodes, vertexMap.get(node).getNeighboursAsIntArray()); // this is the intersect of possiblenodes and the neighbours of the node added to the clique
                int[] updatedExploredNodes = ArrayMethods.intersectArrays(exploredNodes, vertexMap.get(node).getNeighboursAsIntArray()); // this is the intersect of explorednodes and the neighbours of the node added to the clique
                BronKerboschWithPivot(updatedGrowingClique,updatedPossibleNodes,updatedExploredNodes);
                possibleNodes = ArrayMethods.removeElement(possibleNodes, node);
                exploredNodes = ArrayMethods.addToArray(exploredNodes, node);
                // above is the backtracking step
                // if a node is explored it adds it to the explored nodes and removed from possible nodes so that it doesnt get explored again
            }
        }
    }
    // the below method is the one called in readgraph
    // mode is with or without pivot, use 0 for with pivot 1 for without pivot
    public int getLowerBound(Vertex[] v, int mode){
        // the below for loop is copied from hybridalgorithm, it populates the vertex arraylist with vertexobjects
        for (Vertex vertex : v) {
            vertices.add(vertex);
        }
        // below i create a hasmap with the key as the node and the value as the vertex object of that node
        for(Vertex vertex: vertices){
            vertexMap.put(vertex.i, vertex);
        }
        // below I initialize an array with just the nodes because this is the starting parameter for bron kerbosch
        int[] nodesArray = vertexMap.keySet().stream().mapToInt(Integer::intValue).toArray();
        int lowerBound = 0;
        if(mode == 0){
            BronKerboschWithPivot(new int[0] , nodesArray, new int[0]);
            lowerBound = maximumCliqueSizeCorrect;
        } else {
            BronKerbosch(new int[0] , nodesArray, new int[0]);
            lowerBound = maximumCliqueSizeWithoutPivot;
        }
        return lowerBound;
    }
}