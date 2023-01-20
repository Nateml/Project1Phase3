package project1phase3;


import java.util.ArrayList;

public class Vertex {
    /*
     * This class represents a vertex of the graph.
     * It stores the saturation degree of the vertex and its number.
     * Create a vertex by writing: Vertex v = new Vertex(<saturation_degree>, <i>)
     */
    public int saturationDegree;
    protected int i;
    public ArrayList<Vertex> neighbours = new ArrayList<>();
    
    /**
     * Creates a Vertex object.
     * @param saturationDegree can be used as an equivalent for the number of neighbours the vertex has
     * @param i the vertex number/id
     */
    public Vertex(int saturationDegree, int i) {
        this.saturationDegree = saturationDegree;
        this.i = i;
    }

    @Override
    public Vertex clone() {
        Vertex newVertex = new Vertex(this.saturationDegree, this.i);
        newVertex.neighbours = this.neighbours;
        return newVertex;
    }

    /**
     * Adds a vertex as a neighbour to the vertex that this method is called with
     * @param neighbour the neighbouring vertex
     */
    public void add_neighbour(Vertex neighbour) {
        this.saturationDegree++;
        neighbours.add(neighbour);
    }

    public ArrayList<Vertex> getNeighbours() {
        return neighbours;
    }

    /**
     * Returns an array of neighbouring <code>Vertex</code> vertex objects.
     * @return an array of neighbouring vertices.
     */
    public Vertex[] getNeighboursAsVertexArray() {
        Vertex[] neighbourArray = new Vertex[neighbours.size()];
        for (int i = 0; i < neighbours.size(); i++) {
            neighbourArray[i] = neighbours.get(i);
        }
        return neighbourArray;
    }

    /**
     * Returns an array containing the ids if the neighbouring vertices.
     * @return an array containing the ids of the neighbouring vertices.
     */
    public int[] getNeighboursAsIntArray(){
        int[] neighbourArray = new int[neighbours.size()];
        ArrayList<Integer> neighboursAsInts = new ArrayList<>();
        for (Vertex v : neighbours) {
            neighboursAsInts.add(v.identification());
        }
        neighbourArray = neighboursAsInts.stream().mapToInt(i -> i).toArray();
        return neighbourArray;
    }    

    /**
     * Tests for vertex equality by comparing ids.
     * @param v
     * @return true if <code>v.identification()</code> equals the id of the <code>Vertex</code> object that this method is called with.
     * @see graphvis.group30.Vertex#identification()
     */
    public boolean equals(Vertex v) {
        if (v.identification() == i) return true;
        return false;
    }

    /**
     * @return the id of the <code>Vertex</code> object.
     */
    public int identification() {
        return i;
    }

    @Override
    public String toString() {
        return "" + i;
    }

    public ArrayList<Integer> getNeighboursAsIntList() {
        ArrayList<Integer> neighbourIDs = new ArrayList<>();
        for (Vertex vertex : neighbours) {
            neighbourIDs.add(vertex.identification());
        }
        return neighbourIDs;
    }

}
