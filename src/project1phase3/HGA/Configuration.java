package project1phase3.HGA;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import project1phase3.Shared.Vertex;

public class Configuration{
    /*
     * A class responsible for creating and storing an individual
     * "configuration", which is a partition of k subsets of the
     * set of vertices.
     */

    public ArrayList<ArrayList<Vertex>> partition;
    int conflictCount = -1;
    int k;
    ArrayList<Vertex> vertices;

    /**
     * @param vertices an array of all the vertices belonging to the graph
     * @param k the target chromatic number, i.e. the number of subsets of v in the partition
     */
    public Configuration(ArrayList<Vertex> vertices, int k){
        partition = new ArrayList<>();
        this.k = k;
        this.vertices = vertices;

    }

    public Configuration(ArrayList<Vertex> vertices, int k, ArrayList<ArrayList<Vertex>> partition) {
        this.partition = partition;
        this.k = k;
        this.vertices = vertices;
    }

    /**
     * Checks for equality by comparing vertices in the same position in both configurations
     * @param c
     * @return true if equal, false otherwise
     */
    public boolean equals(Configuration c) {
        if (c.partition.size() != this.partition.size()) return false;
        for (int i = 0; i < c.partition.size(); i++) {
            if (c.partition.get(i).size() != this.partition.get(i).size()) return false;
            for (int j = 0; j < c.partition.get(i).size(); j++) {
                if (c.partition.get(i).get(j) != this.partition.get(i).get(j)) return false;
            }
        }
        return true;
    }

    @Override
    public Configuration clone() {
        ArrayList<Vertex> newVertices = new ArrayList<>();
        for (Vertex vertex : vertices) {
            newVertices.add(vertex.clone());
        }
        ArrayList<ArrayList<Vertex>> newPartition = new ArrayList<>();
        for (ArrayList<Vertex> vertexClass : partition) {
            ArrayList<Vertex> newVertexClass = new ArrayList<>();
            for (Vertex vertex : vertexClass) {
                newVertexClass.add(vertex.clone());
            }
            newPartition.add(newVertexClass);
        }

        return new Configuration(newVertices, k, newPartition);
    }

    /**
     * Performs the DSatur algorithm to fill the partition.
     */
    public void initDsatur() {
        // sorts V by saturation degree:
        vertices.sort(new SaturationComparator());

        for (int i = 0; i < vertices.size(); i++) { // looping through every vertex in graph
            boolean vertexAssigned = false;
            for (int j = 0; j < k; j++) { // looping through each colour class (each element to be in the partition)
                if (j >= partition.size()) {
                    partition.add(new ArrayList<Vertex>());
                }

                // check if colour class contains a neighbour of the current vertex
                boolean isConflictingSet = false;
                for (int l = 0; l < partition.get(j).size(); l++) { // looping through every vertex in the colour class
                    if (vertices.get(i).getNeighboursAsIntList().contains(partition.get(j).get(l).identification())) {
                        isConflictingSet = true;
                        break;
                    }
                }

                // add vertex to colour class if the class does not contain a neighbour
                if (!isConflictingSet) { 
                    partition.get(j).add(vertices.get(i));
                    vertexAssigned = true;
                    break;
                }
            }

            // randomnly assign remaining vertices
            if (!vertexAssigned) { 
                int rNum = (int) (Math.random() * k);
                partition.get(rNum).add(vertices.get(i));
            }
        }
    }

    public ArrayList<Entry<Vertex, Integer>> getVertexConflictCounts() {
        // this method returns an arraylist of map entries with vertices as keys and their corresponding amount of conflicts (amount of neighbours in same colour class) as values.
        ArrayList<Entry<Vertex, Integer>> conflictCounts = new ArrayList<>();
        for (int i = 0; i < partition.size(); i++) {
            final int i2 = i;
            for (int j = 0; j < partition.get(i).size(); j++) {
                int conflicts = 0;
                final int j2 = j;
                for (int neighbour = 0; neighbour < partition.get(i).get(j).getAmountOfNeighbours(); neighbour++) {
                    final int neighbour2 = neighbour;
                    if (partition.get(i).stream().anyMatch(element -> element.identification() == partition.get(i2).get(j2).getNeighboursAsIntList().get(neighbour2))) {
                        conflicts++;
                    }
                }
                Entry<Vertex, Integer> vertexConflictCountPair = new AbstractMap.SimpleEntry<>(partition.get(i).get(j), conflicts);
                conflictCounts.add(vertexConflictCountPair);
            }
        }

        // sort conflict count array:
        conflictCounts.sort(new conflictCountComparator());

        return conflictCounts;
    }

    public int getVertexColourClass(Vertex v) {
        for (int i = 0; i < partition.size(); i++) {
            if (partition.get(i).contains(v)) {
                return i;
            } 
        }
        return 0;
    }

    public int getTotalConflictCount() {
        // this method returns the amount of total conflicts (vertices present in the same colour class as a neighbour) present in the configuration
        if (conflictCount != -1) {
            return conflictCount;
        }
        int conflictCount = 0;
        for (int i = 0; i < partition.size(); i++) {
            for (int j = 0; j < partition.get(i).size(); j++) {
                for (int neighbour = 0; neighbour < partition.get(i).get(j).getNeighbours().size(); neighbour++) {
                    for (int k = 0; k < partition.get(i).size(); k++) {
                        if (partition.get(i).get(k).equals(partition.get(i).get(j).getNeighbours().get(neighbour))) {
                            conflictCount++;
                        }
                    }
                }
            }
        }
        this.conflictCount = conflictCount;
        return conflictCount;
    }


    public ArrayList<ArrayList<Vertex>> removeVertices(ArrayList<Vertex> largestColourClass) {
        // this method returns a new partition equal to the old partition but with all the vertices from the paramater array removed
        ArrayList<ArrayList<Vertex>> newPartition = new ArrayList<>();
        for (int i = 0; i < partition.size(); i++) {
            final int i2 = i;
            newPartition.add(new ArrayList<Vertex>());

            for (int j = 0; j < partition.get(i).size(); j++) {
                final int j2 = j;
                if (!largestColourClass.stream().anyMatch(element -> partition.get(i2).get(j2).identification() == element.identification())) {
                    newPartition.get(i).add(partition.get(i).get(j));
                }
            }
        }
        return newPartition;
    }

    public void setConflictCount(int conflicts) {
        this.conflictCount = conflicts;
    }

    /**
     * Moves a vertex to a different colour class.
     * @param pos the position of the vertex to be moved
     * @param i the colour class the vertex should be moved to
     */
    public void moveVertex(int[] pos, int i) {
        Vertex v = partition.get(pos[0]).get(pos[1]);
        partition.get(pos[0]).remove(pos[1]);
        partition.get(i).add(v);
    }

    /**
     * @param v
     * @return the position of the vertex in the position
     */
    public int[] getVertexPos(int v) {
        int[] vertexPos = new int[2];
        boolean foundVertex = false;
        for (int j = 0; j < partition.size(); j++) {
            if (foundVertex) break;
            for (int j2 = 0; j2 < partition.get(j).size(); j2++) {
                if (partition.get(j).get(j2).identification() == v) {
                    vertexPos[0] = j;
                    vertexPos[1] = j2;
                    foundVertex = true;
                    break;
                }
            }
        }
        return vertexPos;
    }

    /**
     * Moves a vertex to a colour class.
     * @param v the vertex to be moved
     * @param i the colour class that the vertex should be moved to
     */
    public void moveVertex(int v, int i) {
        int[] vertexPos = new int[2];
        boolean foundVertex = false;
        for (int j = 0; j < partition.size(); j++) {
            if (foundVertex) break;
            for (int j2 = 0; j2 < partition.get(j).size(); j2++) {
                if (partition.get(j).get(j2).identification() == v) {
                    vertexPos[0] = j;
                    vertexPos[1] = j2;
                    foundVertex = true;
                    break;
                }
            }
        }
        moveVertex(vertexPos, i);
    }

    /**
     * Checks if the configuration contains duplicate vertices
     * @return true if the configuration is valid, false otherwise
     */
    public boolean validate() {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < partition.size(); i++) {
           for (int j = 0; j < partition.get(i).size(); j++) {
            if (map.get(partition.get(i).get(j).identification()) == null) {
                map.put(partition.get(i).get(j).identification(), 1);
            } else {
                map.put(partition.get(i).get(j).identification(), map.get(partition.get(i).get(j).identification())+1);
            }
            if (map.get(partition.get(i).get(j).identification()) > 1) {
                return false;
            }
           } 
        }
        return true;
    }

    public int compare(Configuration config) {
        int num = 0;
        for (int i = 0; i < partition.size(); i++) {
            final int i2 = i;
            for (int j = 0; j < partition.get(i).size(); j++) {
                final int j2 = j;
                if (!config.partition.get(i).stream().anyMatch(element -> element.identification() == partition.get(i2).get(j2).identification())) {
                        num++; 
                } 
            }
        }
        return num;
    }

    /**
     * Returns a list of neighbouring configurations.
     * A neighbouring configuration is defined as any configuration produced as a result of moving a conflicting
     *  vertex to a different colour class.
     * @return
     */
    /*
    public List<Configuration> getNeighbours() {
        List<Configuration> neighbours = new ArrayList<>();
        for (int i = 0; i < partition.size(); i++) {
            for (int j = 0; j < partition.get(i).size(); j++) {
                // check if the vertex is a conflicting vertex
                boolean containsNeighbour = false;
                for (int k = 0; k < partition.get(i).size(); k++) {
                    for (int k2 = 0; k2 < partition.get(i).get(j).getNeighbours().size(); k2++) {
                        if (partition.get(i).get(j).getNeighbours().get(k2).equals(partition.get(i).get(k))) {
                            containsNeighbour = true;
                            break;
                        }
                    }
                }

                // if the vertex is a conflicting vertex, generate a neighbouring configuration in which the vertex has been moved to a different colour class.
                if (containsNeighbour) {
                    int rnum = (int) (Math.random() * partition.size());
                    while (rnum != i) {
                        rnum = (int) (Math.random() * partition.size());
                    }
                    int[] vPos = {i, j};
                    for (int c = 0; c < partition.size(); c++) {
                        if (c == i) continue;
                        Configuration newConfiguration = this.clone();
                        newConfiguration.moveVertex(vPos, c);
                        neighbours.add(newConfiguration);
                    }
                }
            }
        }
        return neighbours;
    }
    */

    @Override
    public String toString() {
        return partition.toString();
    }

    public int recalculateConflicts() {
        this.conflictCount = -1;
        return getTotalConflictCount();
    }

    public Vertex getVertexWithMostConflicts() {
        int k = 0;
        while (partition.get(k).size() == 0) {
            k++;
        }
        Vertex v = partition.get(k).get(0);
        int mostConflicts = 0;
        for (ArrayList<Vertex> colourClass : partition) {
            for (Vertex vertex : colourClass) {
                ArrayList<Vertex> neighbours = vertex.getNeighbours();
                int conflicts = 0;
                for (Vertex neighbour : neighbours) {
                    if (colourClass.stream().anyMatch(element -> element.identification() == neighbour.identification())) {
                        conflicts++;
                    }
                }
                if (conflicts > mostConflicts) {
                    mostConflicts = conflicts;
                    v = vertex;
                }
            }
        }
        return v;
    }
    
}