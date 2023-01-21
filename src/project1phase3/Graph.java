package project1phase3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Graph {
    
    private int numVertices, numEdges;
    private Vertex[] vertices;

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
        
        this.vertices = v.toArray(new Vertex[0]);
        this.numEdges = numE;
        this.numVertices = numV;
    }

    public int getNumVertices() {
        return numVertices;
    }

    public int getNumEdges() {
        return numEdges;
    }

    public Vertex[] getVertices() {
        return vertices;
    }
}
