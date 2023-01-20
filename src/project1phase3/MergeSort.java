package project1phase3;

public class MergeSort {

    private static void merge(int[] left, int[] right, int[] initial){
        int leftsize = initial.length / 2;
        int rightsize = initial.length - leftsize;
        int i = 0, l = 0, r = 0;

        while(l < leftsize && r < rightsize){
            if(LowerBound.vertexMap.get(left[l]).getAmountOfNeighbours() > LowerBound.vertexMap.get(right[r]).getAmountOfNeighbours()){
                initial[i] = left[l];
                i++;
                l++;
            } else {
                initial[i] = right[r];
            }
        }
        while(l < leftsize){
            initial[i] = left[l];
            i++;
            l++;
        }
        while(r < rightsize){
            initial[i] = right[r];
            i++;
            r++;
        }

    }

    public static void mergesort(int[] s){
        int length = s.length;
        if(length <= 1) return;
        int midpoint = length/2;
        int[] left = new int[midpoint];
        int[] right = new int[length-midpoint];
        int count  = 0;
        for(; count < midpoint; count++){
            left[count] = s[count];
        }
        count = 0;
        for(; count < right.length; count++){
            right[count] = s[count + midpoint];
        }

        mergesort(left);
        mergesort(right);
        merge(left,right,s);
    }
}
