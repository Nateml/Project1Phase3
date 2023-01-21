package project1phase3;

import java.util.ArrayList;

public class GraphCleaner {
    
    public GraphCleaner() {
    }

    /**
     * Completely removes nodes with only one neighbour from the graph
     * @param g the graph
     */
    public void reduceNodes(Graph g) {
        ArrayList<Vertex> badNodes = new ArrayList<>();
        ArrayList<Vertex> newNodes = new ArrayList<>();
        int edgesRemoved = 0;
        for (int i = 0; i < g.getNumVertices(); i++) {
            if (g.getVertices().get(i).getAmountOfNeighbours() == 1) {
                badNodes.add(g.getVertices().get(i));
            }
        }
        for (int i = 0; i < badNodes.size(); i++) {
            if (badNodes.get(i).getAmountOfNeighbours() != 0) {
                edgesRemoved++;
                badNodes.get(i).getNeighbours().get(0).getNeighbours().remove(badNodes.get(i));
            }
        }
        for (int i = 0; i < g.getNumVertices(); i++) {
            Vertex v = g.getVertices().get(i);
            if (v.getAmountOfNeighbours() != 1 && v.getAmountOfNeighbours() != 0) {
                newNodes.add(v);
            }
        }
        System.out.print("Removed " + badNodes.size() + " vertices (" + g.getNumVertices() + " --> ");
        g.setVertices(newNodes);
        g.setNumVertices(g.getNumVertices() - badNodes.size());
        System.out.println(g.getNumVertices() + ")");
        System.out.print("Removed " + edgesRemoved + " edges (" + g.getNumEdges() + " --> ");
        g.setNumEdges(g.getNumEdges() - edgesRemoved);
        System.out.println(g.getNumEdges() + ")");

    }
}
