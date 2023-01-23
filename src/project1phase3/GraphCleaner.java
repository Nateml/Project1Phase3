package project1phase3;

import java.util.ArrayList;

public class GraphCleaner {
    
    public GraphCleaner() {
    }

    /* 
    public int removeCycles(Graph g) {
        ArrayList<Vertex> badNodes = new ArrayList<>();
        for (int i = 0; i < g.getVertices().size(); i++) {
            int rNum = (int) (Math.random() * g.getVertices().size());
            if (g.getVertices().get(rNum).getAmountOfNeighbours() == 2) {
                boolean hasDegreeTwo = true;
                ArrayList<Vertex> visitedNodes = new ArrayList<>();
                Vertex currentVertex = g.getVertices().get(rNum);
                visitedNodes.add(currentVertex);

                boolean completeCycle = false;

                // traverse right
                currentVertex = currentVertex.getNeighbours().get(0);
                while (hasDegreeTwo) {
                    System.out.println("first loop");
                    visitedNodes.add(currentVertex);
                    if (!visitedNodes.contains(currentVertex.getNeighbours().get(0))) {
                        currentVertex = currentVertex.getNeighbours().get(0);
                    } else if (!visitedNodes.contains(currentVertex.getNeighbours().get(1))) {
                        currentVertex = currentVertex.getNeighbours().get(1);
                    } else {
                        completeCycle = true;
                        break;
                    }
                    hasDegreeTwo = currentVertex.getAmountOfNeighbours() == 2;
                    if (currentVertex.equals(g.getVertices().get(rNum))) {
                        completeCycle = true;
                        break;
                    }
                }

                // traverse left
                currentVertex = g.getVertices().get(rNum).getNeighbours().get(1);
                while (hasDegreeTwo && !completeCycle) {
                    System.out.println("second loop");
                    visitedNodes.add(currentVertex);
                    if (!visitedNodes.contains(currentVertex.getNeighbours().get(1))) {
                        currentVertex = currentVertex.getNeighbours().get(1);
                    } else if (!visitedNodes.contains(currentVertex.getNeighbours().get(1))) {
                        currentVertex = currentVertex.getNeighbours().get(1);
                    } else {
                        completeCycle = true;
                        break;
                    }
                    hasDegreeTwo = currentVertex.getAmountOfNeighbours() == 2;
                }

                badNodes.addAll(visitedNodes);

                if (visitedNodes.size() == g.getVertices().size()) {
                    if (g.getVertices().size() % 2 == 0) {
                        return 2;
                    } else {
                        return 3;
                    }
                }
            }
        }
        System.out.println("removed " + badNodes.size() + " vertices");
        return 0;

    }
    */

    /**
     * Completely removes nodes with only one neighbour from the graph
     * @param g the graph
     */
    public void reduceNodes(Graph g) {
        ArrayList<Vertex> badNodes = new ArrayList<>();
        ArrayList<Vertex> newNodes = new ArrayList<>();
        int edgesRemoved = 0;
        for (int i = 0; i < g.getVertices().size(); i++) {
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
        for (int i = 0; i < g.getVertices().size(); i++) {
            Vertex v = g.getVertices().get(i);
            if (v.getAmountOfNeighbours() != 1 && v.getAmountOfNeighbours() != 0) {
                newNodes.add(v);
            }
        }
        System.out.print("Removed " + badNodes.size() + " vertices (" + g.getVertices().size() + " --> ");
        g.setVertices(newNodes);
        g.setNumVertices(g.getVertices().size() - badNodes.size());
        System.out.println(g.getVertices().size() + ")");
        System.out.print("Removed " + edgesRemoved + " edges (" + g.getNumEdges() + " --> ");
        g.setNumEdges(g.getNumEdges() - edgesRemoved);
        System.out.println(g.getNumEdges() + ")");

    }
}
