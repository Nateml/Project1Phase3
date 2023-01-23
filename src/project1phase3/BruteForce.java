package project1phase3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class BruteForce {
    HashSet<Integer> set1 = new HashSet<Integer>();
    ArrayList<Vertex> v = new ArrayList<>();
    HashMap<Integer, Vertex> vertexMap = new HashMap<>(); // initializes a hashmap
    
    
    public int chromaticNumber(Graph g, int lowerbound, boolean runOnce) { 
        // e is the array of the same name generated in the ReadGraph class
        // this method should return the chromatic number
        
            for (Vertex vertex : g.getVertices()) {
                this.v.add(vertex.clone());
            }
        

            for (int j = 0; j < v.size(); j++) {
                v.get(j).i = j+1; 
            }

            for(Vertex vertex: v){
                vertexMap.put(vertex.i, vertex);
            }
    
            int [][] adjMatrix = new int [v.size()][v.size()];
            for (Vertex vertex1 : v) {
                int[] neighbours = vertexMap.get(vertex1.i).getNeighboursAsIntArray();
                for (Vertex vertex2 : v) {
                    if (ArrayMethods.isInArray(neighbours, vertex2.i)>=0){
                        adjMatrix[vertex1.i-1][vertex2.i-1] = 1;
                    }else{
                        adjMatrix[vertex1.i-1][vertex2.i-1] = 0;
                    }
                }
            }
    

        int upperbound = adjMatrix.length;
        int chromaticNumber = values(upperbound, adjMatrix, lowerbound, runOnce);
        return chromaticNumber;
    }

    //checks that no two adjacent vertices of the graph are colored with the same color
    public static boolean isValid(int[][] adjMatrix, int[] color, int n, int color1, int vertex){
        for (int i = 0; i < n; i++) {
            if(adjMatrix[vertex][i] == 1 && color1 == color[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean colorGraph(int[][] adjMatrix, int chromaticNumber, int index, int[] color, int n, int vertex){
        if(vertex == n) {//base case all vertices are colored.
            return true;
        }

        for (int color1 = 1; color1 <= chromaticNumber; color1++) { //works until base case
            if(isValid(adjMatrix, color, n, color1, vertex)==true) {
                color[vertex] = color1;//assign same color                
                if(colorGraph(adjMatrix, chromaticNumber, index, color, n, vertex + 1)==true) {//recursive call
                    return true;//all vertices are colored
                } else {
                    if (vertex == 0 && color[vertex] != 0) {
                        break;
                    }
                    color[vertex] = 0;
                }
                
            }
        }
        return false;
    }

    //adds a new color its called in values method
    public static boolean addColor(int[][] adjMatrix, int chromaticNumber) {
        int n = adjMatrix.length;
        int color[] = new int[n];
        return colorGraph(adjMatrix, chromaticNumber, 0, color, n, 0);
    }

    public static int values(int upperbound, int[][] adjMatrix, int lowerBound, boolean runOnce){
        for (int i=lowerBound;i<=upperbound;i++){
            if(addColor(adjMatrix, i)==true){//all vertices are colored
                return i;
            } else {
                if (runOnce) {
                    return -1;
                }
                //return -1;
            }
        }
        return upperbound;
    }
}


