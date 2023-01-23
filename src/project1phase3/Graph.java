package project1phase3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

public class Graph {
    
    private int numVertices, numEdges;
    private ArrayList<Vertex> vertices;

    private ArrayList<Vertex> removableVertices = new ArrayList<Vertex>();
    private Vertex startingVertex;
    public int removedVertices;
    private boolean hasFoundCompleteCyle;
    private boolean hasPrunedEntireGraph;
    private int biggestCycleColor;
    boolean hasOddCycles = false;

    public Graph(String filename) throws FileNotFoundException {
        ArrayList<Vertex> v = new ArrayList<Vertex>();
        int numV = 0;
        int numE = 0;
        Scanner fileScanner = new Scanner(new File(filename));
        int lineNumber = 1;
        while (fileScanner.hasNext()) {
            if (lineNumber == 1) {
                String line = fileScanner.nextLine();
                numV = Integer.parseInt(line.split(" = ")[1]);
            } else if (lineNumber == 2) {
                numE = Integer.parseInt(fileScanner.nextLine().split(" = ")[1]);
            } else {
                String line = fileScanner.nextLine();
                int vertex1ID = Integer.parseInt(line.split(" ")[0]);
                int vertex2ID = Integer.parseInt(line.split(" ")[1]);
                Vertex vertex1 = new Vertex(0, vertex1ID);
                Vertex vertex2 = new Vertex(0, vertex2ID);
                boolean containsVertex1 = false;
                boolean containsVertex2 = false;
                for (Vertex vertex : v) {
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
                    v.add(vertex1);
                }
                if (!containsVertex2) {
                    v.add(vertex2);
                }

            }
            lineNumber++;
        }
        fileScanner.close();
        
        this.vertices = v;
        this.numEdges = numE;
        this.numVertices = v.size();
    }

    public void analyseAndPrune() {
        //reduceNodes();
        //removeCycles();
        hasOddCycles = false;
    }

    /*
    private boolean checkBipartite() {
        // 1 = red
        // 0 = blue
        Map<Vertex, Integer> colourMap = new HashMap<>();
        Vertex startingVertex = vertices.get(0);
        colourMap.put(startingVertex, 1);

        LinkedList<Vertex> r = new LinkedList<>();
        r.addAll(startingVertex.getNeighbours());
        while (!r.isEmpty()) {

        }

        ArrayList<Vertex> neighbours = startingVertex.getNeighbours();

        for (Vertex n : neighbours) {
            for (Vertex n2 : n.getNeighbours()) {
                if (!colourMap.keySet().contains(n2)) {
                    if (colourMap.get(n) == 0) colourMap.put(n2, 1);
                    else if (colourMap.get(n) == 1) colourMap.put(n2, 0);
                }
            }
        }

    }
    */


    public void removeCycles(){
        int i = 0;
        int startingSize = vertices.size();
        while(i < (vertices.size()*0.1)){
            //startingVertex = (int) (Math.random() * g.getVertices().size());
            startingVertex = vertices.get((int)(Math.random() * vertices.size()));
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
            vertices.remove(vertex);
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

    private void reduceNodes() {
        ArrayList<Vertex> badNodes = new ArrayList<>();
        ArrayList<Vertex> newNodes = new ArrayList<>();
        int edgesRemoved = 0;
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).getAmountOfNeighbours() == 1) {
                badNodes.add(vertices.get(i));
            }
        }
        for (int i = 0; i < badNodes.size(); i++) {
            if (badNodes.get(i).getAmountOfNeighbours() != 0) {
                edgesRemoved++;
                badNodes.get(i).getNeighbours().get(0).getNeighbours().remove(badNodes.get(i));
            }
        }
        for (int i = 0; i < vertices.size(); i++) {
            Vertex v = vertices.get(i);
            if (v.getAmountOfNeighbours() != 1 && v.getAmountOfNeighbours() != 0) {
                newNodes.add(v);
            }
        }
        System.out.print("Removed " + badNodes.size() + " vertices (" + vertices.size() + " --> ");
        setVertices(newNodes);
        setNumVertices(vertices.size() - badNodes.size());
        System.out.println(vertices.size() + ")");
        System.out.print("Removed " + edgesRemoved + " edges (" + numEdges + " --> ");
        setNumEdges(numEdges - edgesRemoved);
        System.out.println(numEdges + ")");
    }

    /*
    public int getNumVertices() {
        return numVertices;
    }
    */

    public int getNumEdges() {
        return numEdges;
    }

    public void setVertices(ArrayList<Vertex> v) {
        vertices = v;
    }

    public void setNumVertices(int numVertices) {
        this.numVertices = numVertices;
    }

    public void setNumEdges(int numEdges) {
        this.numEdges = numEdges;
    }

    public ArrayList<Vertex> getVertices() {
        return vertices;
    }

}
