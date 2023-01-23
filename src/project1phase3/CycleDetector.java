package project1phase3;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

public class CycleDetector {

    private ArrayList<Integer> removableVertices = new ArrayList<Integer>();
    private int startingVertex;
    public int removedVertices;
    private boolean hasFoundCompleteCyle;
    private boolean hasPrunedEntireGraph;
    private int biggestCycleColor;

    public static void main(String[] args) throws FileNotFoundException{

    }

    public void removeCycles(Graph g){
        Random randomGenerator = new Random();
        int i = 0;
        int startingSize = g.getNumVertices();
        while(i < /* 10 percent of the graph */){
            startingVertex = randomGenerator.nextInt(0, g.getNumVertices());
            if(LowerBound.vertexMap.get(Integer.valueOf(startingVertex)).getAmountOfNeighbours() == 2){
                ArrayList<Integer> startingNeighbours = LowerBound.vertexMap.get(startingVertex).getNeighboursAsIntList();
                removeVertex(startingVertex, startingNeighbours.get(0));
                if(hasFoundCompleteCyle == false){
                    removeVertex(startingVertex, startingNeighbours.get(1));
                }
                removedVertices += removableVertices.size();
                // remove all vertices in removablevertices from the graph
                removableVertices.clear();
            }
            if(removedVertices = startingSize){
                // in this case the entire graph consisted of cycles so return 2 or 3
                break;
            }
        }
    }

    private void removeVertex(Integer lastVertex, Integer currentVertex){
        if(currentVertex == Integer.valueOf(startingVertex)){
            if(removableVertices.size() % 2 == 0 && biggestCycleColor != 3){
                biggestCycleColor = 2;
            } else{
                biggestCycleColor = 3;
            }
        } else if(LowerBound.vertexMap.get(currentVertex).getAmountOfNeighbours() == 2){
            removableVertices.add(currentVertex);
            Integer nextVertex = 0;
            for(Integer v:LowerBound.vertexMap.get(currentVertex).getNeighboursAsIntList()){
                if(v != lastVertex){
                    nextVertex = v;
                }
            }
            removeVertex(currentVertex, nextVertex);
        }
    }
    // have to store the last vertex removed
    // have to check when it loops back to itself

    
}
