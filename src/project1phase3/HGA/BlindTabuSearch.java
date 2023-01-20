package project1phase3.HGA;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import project1phase3.Vertex;

public class BlindTabuSearch {
    
    Configuration config;
    TabuList tabuList;

    public BlindTabuSearch(Configuration config) {
        this.config = config;
        tabuList = new TabuList();
    }

    public Configuration run(int iterations) {
        Configuration bestConfig = config.clone();
        Configuration currentConfig = config.clone();

        while (iterations > 0) {

            List<Entry<Vertex, Integer>> conflictCounts = currentConfig.getVertexConflictCounts();
            int i = (int) (Math.random() * currentConfig.partition.size());

            Vertex v = conflictCounts.get(0).getKey();
            int[] k = currentConfig.getVertexPos(v.identification());
            Move move = new Move(v.identification(), i, k[0]);
            if (!tabuList.contains(move)) {
                currentConfig.moveVertex(k, i);
                tabuList.add(move);

                if (currentConfig.getTotalConflictCount() == 0) {
                    return currentConfig;
                }

                if (currentConfig.getTotalConflictCount() < bestConfig.getTotalConflictCount())  {
                    bestConfig = currentConfig;
                }
            }

            //System.out.println("b: " + currentConfig.recalculateConflicts());
            tabuList.update();
            iterations--;
        }

        return bestConfig;
    }

}
