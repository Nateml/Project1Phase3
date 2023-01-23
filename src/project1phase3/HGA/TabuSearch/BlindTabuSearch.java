package project1phase3.HGA.TabuSearch;

import project1phase3.HGA.Configuration;
import project1phase3.Shared.Vertex;

public class BlindTabuSearch {
    /*
     * This class implements a tabu search algorithm.
     * Instead of choosing the "best" neighbour for a configuration, this algorithm saves time
     *  by selecting a "good" neighbour, which is chosen by moving the vertex with 
     *  the most conflicts to a randomn colour class.
     */
    
    Configuration config;
    TabuList tabuList;

    public BlindTabuSearch(Configuration config) {
        this.config = config;
        tabuList = new TabuList();
    }

    /**
     * Runs the tabu search algorithm.
     * @param iterations the amount of iterations that the tabu search will run for
     * @return the improved configuration
     */
    public Configuration run(int iterations) {
        Configuration bestConfig = config.clone();
        Configuration currentConfig = config.clone();

        while (iterations > 0) {

            Vertex v = currentConfig.getVertexWithMostConflicts(); // vertex with most conflicts
            int i = (int) (Math.random() * currentConfig.partition.size()); // index of colour class for the vertex to be moved to

            int[] k = currentConfig.getVertexPos(v.identification());
            Move move = new Move(v.identification(), i, k[0]);
            // check if move is tabu
            if (!tabuList.contains(move)) {
                currentConfig.moveVertex(k, i); // move vertex to different colour class
                tabuList.add(move); // add move to tabu list

                if (currentConfig.recalculateConflicts() == 0) {
                    return currentConfig; // valid solution
                }

                if (currentConfig.getTotalConflictCount() <= bestConfig.getTotalConflictCount())  {
                    bestConfig = currentConfig; // update best configuration
                }

            }

            tabuList.update(); // update tabu list
            iterations--; 
        }

        return bestConfig;
    }

}
