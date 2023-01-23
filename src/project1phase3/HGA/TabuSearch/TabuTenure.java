package project1phase3.HGA.TabuSearch;

public class TabuTenure {
    /**
     * This class is used by the tabu search algorithm to keep track of the number of iterations
     *  that a move should be stored in the tabu list for.
     */

    final int A = 10;
    final double alpha = 0.6;
    int tenure;
    private int numConflictingVertices;

    public TabuTenure(int numConflictingVertices) {
        this.numConflictingVertices = numConflictingVertices;
        tenure = (int)((Math.random() * A) + (alpha * numConflictingVertices)); // formula for tabu tenure
    }

    @Override
    public TabuTenure clone() {
        TabuTenure newTabuTenure = new TabuTenure(numConflictingVertices);
        newTabuTenure.setTenure(tenure);
        return newTabuTenure;
    }

    public void setTenure(int t) {
        tenure = t;
    }

    public TabuTenure decrease() {
        tenure -= 1;
        return this;
    }

    public boolean hasExpired() {
        return tenure <= 0;
    }

    public int getTenure() {
        return tenure;
    }

}
