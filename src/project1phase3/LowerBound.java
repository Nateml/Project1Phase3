package project1phase3;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;


public class LowerBound{

    private int maximumCliqueSizeCorrect = 0; // initializes the lower bound
    public static HashMap<Integer, Vertex> vertexMap = new HashMap<>(); // initializes a hashmap where the key
    private LinkedHashSet<Integer> vertexSet;
    private int[] vertexIdArray;

    //below is the algorithm with pivot
    //the inputs are linkedHashSets of Integers where the Integer is the vertex id
    //call with mode 0 to return the arraylist of sets and with mode 1 to call for the lower bound
    public void BronKerboschWithPivot (LinkedHashSet<Integer> growingClique, LinkedHashSet<Integer> possibleNodes, LinkedHashSet<Integer> exploredNodes){
        if(possibleNodes.size() == 0 && exploredNodes.size() == 0){
            if(growingClique.size()>maximumCliqueSizeCorrect){
                maximumCliqueSizeCorrect = growingClique.size();
            }
        } else{
            // choose a pivot from the set P U X as the vertex with the most neighbours
            // since the input is sorted on the amount of neighbours the first element is the biggest one
            Integer pivotNode = 0;
            if (exploredNodes.isEmpty()) {
                pivotNode = possibleNodes.stream().findFirst().get();
            }
            else if (possibleNodes.isEmpty()) {
                pivotNode = exploredNodes.stream().findFirst().get();
            }
            else if(vertexMap.get(possibleNodes.stream().findFirst().get()).getAmountOfNeighbours() > vertexMap.get(exploredNodes.stream().findFirst().get()).getAmountOfNeighbours()){
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
                BronKerboschWithPivot(updatedGrowingClique,updatedPossibleNodes,updatedExploredNodes);
                possibleNodes.remove(node);
                exploredNodes.add(node);
                // above is the backtracking step
                // if a node is explored it adds it to the explored nodes and removed from possible nodes so that it doesnt get explored again
            }
        }
    }

    public int getLowerBound(Graph graph){
        // put the arraylist into a hashmap and hashset of the graph
        vertexIdArray = new int[graph.getVertices().size()];
        int count = 0;
        for(Vertex vertex: graph.getVertices()){
            Integer a = Integer.valueOf(vertex.identification());
            vertexMap.put(a, vertex);
            vertexIdArray[count] = vertex.identification();
            count++;
        }
        MergeSort.mergesort(vertexIdArray);
        vertexSet = Arrays.stream(vertexIdArray).boxed().collect(Collectors.toCollection(LinkedHashSet::new));
        LinkedHashSet<Integer> initialGrowingClique = new LinkedHashSet<Integer>();
        LinkedHashSet<Integer> initialExploredNodes = new LinkedHashSet<Integer>();
        BronKerboschWithPivot(initialGrowingClique, vertexSet, initialExploredNodes);
        int lowerBound = maximumCliqueSizeCorrect;
        return lowerBound;
    }
}