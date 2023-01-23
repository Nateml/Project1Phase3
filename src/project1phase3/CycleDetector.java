package project1phase3;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

public class CycleDetector {

    private ArrayList<Vertex> removableVertices = new ArrayList<Vertex>();
    private Vertex startingVertex;
    public int removedVertices;
    private boolean hasFoundCompleteCyle;
    private boolean hasPrunedEntireGraph;
    private int biggestCycleColor;
    boolean hasOddCycles = false;

    public CycleDetector() {

    }

    public void removeCycles(Graph g){
        int i = 0;
        int startingSize = g.getVertices().size();
        while(i < (g.getVertices().size()*0.1)){
            //startingVertex = (int) (Math.random() * g.getVertices().size());
            startingVertex = g.getVertices().get((int)(Math.random() * g.getVertices().size()));
            if (startingVertex.getAmountOfNeighbours() == 2) {
                //ArrayList<Integer> startingNeighbours = LowerBound.vertexMap.get(startingVertex).getNeighboursAsIntList();
                ArrayList<Vertex> startingNeighbours = startingVertex.getNeighbours();
                removeVertex(startingVertex, startingNeighbours.get(0));
                if(hasFoundCompleteCyle == false){
                    removeVertex(startingVertex, startingNeighbours.get(1));
                }
                removedVertices += removableVertices.size();
                if (removableVertices.size() % 2 != 0) {
                    hasOddCycles = true;
                }
                // remove all vertices in removablevertices from the graph
                removableVertices.clear();
            }
            if(removedVertices == startingSize){
                // in this case the entire graph consisted of cycles so return 2 or 3
                break;
            }
            i++;
        }

        // remove vertices
        for (Vertex vertex : removableVertices) {
            ArrayList<Vertex> neighbours = vertex.getNeighbours();
            for (Vertex neighbour : neighbours) {
                neighbour.getNeighbours().remove(vertex);
            }
            g.getVertices().remove(vertex);
        }
        System.out.println("removed " + removedVertices + " vertices");
        if (hasOddCycles) {
            System.out.println("graph has odd cycles");
        } else {
            System.out.println("graph does not have odd cycles");
        }
        
    }

    private void removeVertex(Vertex lastVertex, Vertex currentVertex){
        if(currentVertex.equals(startingVertex)){
            if(removableVertices.size() % 2 == 0 && biggestCycleColor != 3){
                biggestCycleColor = 2;
            } else{
                biggestCycleColor = 3;
            }
        } else if(currentVertex.getAmountOfNeighbours() == 2){
            removableVertices.add(currentVertex);
            Vertex nextVertex = null;
            for(Vertex v: currentVertex.getNeighbours()){
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
