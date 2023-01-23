package project1phase3.HGA.TabuSearch;

import java.util.HashMap;
import java.util.Map;

public class TabuList {
    /**
     * This class is used in the tabu search algorithm.
     * Stores <code>Move</code> objects so that tabu search does not perform previously performed moves.
     */
    
    HashMap<Move, TabuTenure> tabuMap;
    public TabuList() { 
        tabuMap = new HashMap<Move, TabuTenure>();
    }

    public void add(Move move) {
        TabuTenure tenure = new TabuTenure(move.getConflicts());
        tabuMap.put(move, tenure);
    }

    /**
     * Decreases the tabu tenure of each move in the tabu list.
     */
    public void update() {
        HashMap<Move, TabuTenure> tempMap = new HashMap<>();
        tabuMap.replaceAll((move, tenure) -> tenure.decrease());
        for (Map.Entry<Move, TabuTenure> entry : tabuMap.entrySet()) {
            if (!entry.getValue().hasExpired()) {
                tempMap.put(entry.getKey(), entry.getValue());
            }
        }
        tabuMap = tempMap;
    }

    public boolean contains(Move move) {
        return tabuMap.keySet().stream().anyMatch(element -> element.v == move.v && element.i == move.i);
    }

}
