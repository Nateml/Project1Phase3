package project1phase3.HGA;

import java.util.HashMap;
import java.util.Map;

public class TabuList {
    
    HashMap<Move, TabuTenure> tabuMap;
    public TabuList() { 
        tabuMap = new HashMap<Move, TabuTenure>();
    }

    public void add(Move move) {
        TabuTenure tenure = new TabuTenure(move.getConflicts());
        tabuMap.put(move, tenure);
    }

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
