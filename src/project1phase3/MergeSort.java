package project1phase3;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MergeSort {

    private static void merge(LinkedHashMap<Integer,Vertex> left, LinkedHashMap<Integer,Vertex> right, LinkedHashMap<Integer,Vertex> initial){
        // create arrays with the key values of left and right
        int leftSize = initial.size()/2;
        int rightSize = initial.size()-leftSize;
        initial.clear();
        int[] leftKeys = left.entrySet().stream().mapToInt(Map.Entry::getKey).toArray(); // these two arrays are created so we can acces the 
        int[] rightKeys = right.entrySet().stream().mapToInt(Map.Entry::getKey).toArray(); // hashmap by index cause we can get the key by index of the array
        int l = 0, r = 0;
        while(l < leftSize && r < rightSize){
            // here i have to compare the neighbours of the verteces at l and r index
            if(left.get(leftKeys[l]).getAmountOfNeighbours() > right.get(rightKeys[r]).getAmountOfNeighbours()){
                initial.put(leftKeys[l],left.get(leftKeys[l]));
                l++;
            } else {
                initial.put(rightKeys[r],right.get(rightKeys[r]));
                r++;
            }
        }
        while(l < leftSize){
            initial.put(leftKeys[l],left.get(leftKeys[l]));
            l++;
        }
        while(r < rightSize){
            initial.put(rightKeys[r],right.get(rightKeys[r]));
        }

    }

    public static void mergesort(LinkedHashMap<Integer,Vertex> s){
        int length = s.size();
        if(length <= 1) return;
        int midpoint = length/2;
        LinkedHashMap<Integer,Vertex> leftMap = new LinkedHashMap<Integer,Vertex>();
        LinkedHashMap<Integer,Vertex> rightMap = new LinkedHashMap<Integer,Vertex>();
        int count  = 0;
        for(HashMap.Entry<Integer, Vertex> entry : s.entrySet()){
            if(count <= midpoint){
                leftMap.put(entry.getKey(), entry.getValue());
            } else {
                rightMap.put(entry.getKey(), entry.getValue());
            }
            count ++;
        }
        mergesort(leftMap);
        mergesort(rightMap);
        merge(leftMap,rightMap,s);
    }
}
