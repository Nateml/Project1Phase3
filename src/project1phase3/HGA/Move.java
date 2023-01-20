package project1phase3.HGA;

public class Move {
    
    int v, i, prevClass, conflicts;

    public Move(int v, int i, int conflicts, int prevClass) {
        this.v = v;
        this.i = i;
        this.conflicts = conflicts;
        this.prevClass = prevClass;
    }

    public Move(int v, int i, int conflicts) {
        this.v = v;
        this.i = i;
        this.conflicts = conflicts;
    }

    public Move clone() {
        return new Move(this.v, this.i, this.conflicts, this.prevClass);
    }

    public int getPrevClass() {
        return prevClass;
    }

    public int getConflicts() {
        return conflicts;
    }

}
