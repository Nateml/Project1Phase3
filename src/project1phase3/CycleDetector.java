package project1phase3;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

public class CycleDetector {

    private ArrayList<Integer> removableVertices = new ArrayList<Integer>();

    public static void main(String[] args) throws FileNotFoundException{
        
    }

    public void removeCycles(Graph g){
        Random randomGenerator = new Random();
        int startingVertex = randomGenerator.nextInt(0, g.getNumVertices());
        if(LowerBound.vertexMap.get(Integer.valueOf(startingVertex)).getAmountOfNeighbours() == 2){
            removableVertices.add(Integer.valueOf(startingVertex));
            ArrayList<Integer> startingNeighbours = LowerBound.vertexMap.get(startingVertex).getNeighboursAsIntList();
            removeVertex(startingVertex, startingNeighbours.get(0));
            removeVertex(startingVertex, startingNeighbours.get(1));
        }
    }

    private void removeVertex(Integer lastVertex, Integer currentVertex){
        while(LowerBound.vertexMap.get(currentVertex).getAmountOfNeighbours() == 2){
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
    
    
}
