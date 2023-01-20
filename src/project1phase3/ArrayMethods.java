package project1phase3;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

class ArrayMethods{
    // this class is made to contain all the methods we have to use to manipulate arrays
    // it is made so we can maniupulate arrays without converting to and from arraylist
    // this is because arrays are more efficient
    // all methods are public static so to use them just type arrayMethods.methodname()

    
    // the below method adds an element to the end of an array
    public static int[] addToArray(int[] startingArray, int element){
        int[] newArray = new int[startingArray.length + 1];
        int n = startingArray.length;
        for(int i = 0; i < n ; i++){
            newArray[i] = startingArray[i];
        }
        newArray[n] = element;
        return newArray;
    }
    
    // this method is to add an array to a matrix
    // this creates a new matrix with lenght n+1 and adds the array as the last element
    public static int[][] addArrayToMatrix(int[][] startingMatrix, int[]array1){
        int[][] finalMatrix = new int[startingMatrix.length+1][];
        for(int i = 0; i < startingMatrix.length; i++){
            finalMatrix[i] = startingMatrix[i];
        }
        finalMatrix[startingMatrix.length] = array1;
        return finalMatrix;
    }


    // the below method removes the first item from an array and returns an array with length n-1
    public static int[] removeFirstFromArray(int[] startingArray){
        int[] newArray = new int[startingArray.length - 1];
        for(int i = 0;i<startingArray.length-1;i++){
            newArray[i] = startingArray[i+1];
        }
        return newArray;
    }
    public static int[] removeElement(int[]startingArray, int Element){
        int[] newArray = new int[startingArray.length-1];
        int a = 0;
        for(int i = 0; i < startingArray.length; i++){
            if(startingArray[i] == Element){
                a = i;
                break;
            }
        }
        System.arraycopy(startingArray, 0, newArray, 0, a);
        System.arraycopy(startingArray, a + 1,newArray, a,startingArray.length - a - 1);
        return newArray;
    }
    // the below method checks wether an element is present in an array
    // returns -n if the element is not and the key if the element is
    // so if you want to check if the element is in an array write
    // if(isInArray(a,b)>=){}
    public static int isInArray(int[]startingArray, int element){
        Arrays.sort(startingArray);
        int result  = Arrays.binarySearch(startingArray, element);
        return result;
    }

    // below method intersects two arrays by using sets
    // the reason for the if statements is so it can work if array2 is bigger then array1
    public static int[] intersectArrays(int[] array1, int[] array2){
        if(array1.length > array2.length){
            Integer[] newArray1 = Arrays.stream(array1).boxed().toArray( Integer[]::new );
            Integer[] newArray2 = Arrays.stream(array2).boxed().toArray( Integer[]::new );
            HashSet<Integer> set1 = new HashSet<Integer>();
            set1.addAll(Arrays.asList(newArray1));
            set1.retainAll(Arrays.asList(newArray2));
            int[] finalArray = set1.stream().mapToInt(Integer::intValue).toArray();
            return finalArray;
        } else {
            Integer[] newArray1 = Arrays.stream(array1).boxed().toArray( Integer[]::new );
            Integer[] newArray2 = Arrays.stream(array2).boxed().toArray( Integer[]::new );
            HashSet<Integer> set2 = new HashSet<Integer>();
            set2.addAll(Arrays.asList(newArray2));
            set2.retainAll(Arrays.asList(newArray1));
            int[] finalArray = set2.stream().mapToInt(Integer::intValue).toArray();
            return finalArray;
        }
    }
    // below method returns the union of 2 arrays
    public static int[] union(int[] array1, int[] array2){
        Set<Integer> newSet = new HashSet<>();
        for(int i = 0; i < array1.length; i++){
            newSet.add(array1[i]);
        }
        for(int j = 0; j < array2.length; j++){
            newSet.add(array2[j]);
        }
        int[] newArray = newSet.stream() .mapToInt(Integer::intValue).toArray();
        return newArray;
    }
    // below method returns the first array without all the elements in the second array
    public static int[] subtractArrays(int[] array1, int[] array2){
        Set<Integer> newSet1 = new HashSet<>();
        for(int i = 0; i < array1.length; i++){
            newSet1.add(array1[i]);
        }
        Set<Integer> newSet2 = new HashSet<>();
        for(int j = 0; j < array2.length; j++){
            newSet2.add(array2[j]);
        }
        newSet1.removeAll(newSet2);
        int[] newArray = newSet1.stream() .mapToInt(Integer::intValue).toArray();
        return newArray;
    }
}