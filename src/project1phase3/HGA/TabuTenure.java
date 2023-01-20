package project1phase3.HGA;

public class TabuTenure {

    final int A = 10;
    final double alpha = 0.6;
    int tenure;
    private int numConflictingVertices;

    public TabuTenure(int numConflictingVertices) {
        this.numConflictingVertices = numConflictingVertices;
        tenure = (int)((Math.random() * A) + (alpha * numConflictingVertices));
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
