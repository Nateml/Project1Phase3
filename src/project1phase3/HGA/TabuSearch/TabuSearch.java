package project1phase3.HGA.TabuSearch;

import java.util.ArrayList;
import java.util.List;

import project1phase3.HGA.Configuration;
import project1phase3.Shared.Vertex;

public class TabuSearch {

    Configuration config;
    TabuList tabuList;

    public TabuSearch(Configuration config)  {
        this.config = config;
        tabuList = new TabuList();
    }

    public Configuration run(int iterations) {
        Configuration bestConfig = config.clone();
        Configuration currentConfig = config.clone();
        while (iterations > 0) {
            if (currentConfig.getTotalConflictCount() == 0) {
                return currentConfig;
            }
            long start_time = System.nanoTime();
            List<Move> neighbours = getNeighbourMoves(currentConfig);

            quicksort(neighbours, 0, neighbours.size()-1);

            Move bestMove = neighbours.get(0);
            for (Move move : neighbours) {
                if (tabuList.contains(move)) {
                    if (move.getConflicts() < bestConfig.getTotalConflictCount()) {
                        bestMove = move;
                        break;
                    } else {
                        continue;
                    }
                } else {
                    bestMove = move;
                    break;
                }

            }

            
            tabuList.update();

            Configuration newConfig = currentConfig.clone();
            newConfig.moveVertex(bestMove.v, bestMove.i);
            newConfig.setConflictCount(bestMove.conflicts);
            //System.out.println(newConfig.getTotalConflictCount());
            tabuList.add(new Move(bestMove.v, bestMove.getPrevClass(), bestMove.getConflicts()));
            tabuList.add(bestMove);
            currentConfig = newConfig;
            if (bestMove.getConflicts() == 0)  {
                long end_time = System.nanoTime() - start_time;
                return currentConfig;
            }
            if (currentConfig.getTotalConflictCount() <= bestConfig.getTotalConflictCount()) {
                bestConfig = currentConfig;
            }
            
            iterations--;
        }

        return bestConfig;
    }

    /**
     * Returns a list of moves corresponding to neighbouring configurations.
     * A neighbouring configuration is defined as any configuration produced as a result of moving a conflicting
     *  vertex to a different colour class.
     * @return
     */
    private List<Move> getNeighbourMoves(Configuration c) {
        List<Move> neighbouringMoves = new ArrayList<>();
        for (int i = 0; i < c.partition.size(); i++) {
            for (int j = 0; j < c.partition.get(i).size(); j++) {
                for (int id : c.partition.get(i).get(j).getNeighboursAsIntList()) {
                    if (c.partition.get(i).stream().anyMatch(element -> element.identification() == id))  {
                        int[] vPos = {i,j};
                        for (int k = 0; k < c.partition.size(); k++) {
                            if (k == i) continue;
                            Configuration newConfig = c.clone();
                            newConfig.moveVertex(vPos, k);
                            int conflicts = newConfig.getTotalConflictCount();
                            Move move = new Move(c.partition.get(i).get(j).identification(), k, conflicts, i);
                            neighbouringMoves.add(move);
                        }
                    }
                }
            }
        }
        return neighbouringMoves;
    }

    /**
     * Used for quicksort algorithm.
     * @param list
     * @param low
     * @param high
     * @return
     */
    private int partition(List<Move> list, int low, int high) {
        Move pivot = list.get(high);
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (list.get(j).getConflicts() <= pivot.getConflicts()) {
                i++;
                Move temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }
        Move temp = list.get(i+1);
        list.set(i+1, list.get(high));
        list.set(high, temp);

        return (i + 1);
    }

    /**
     * Sorts a list of move objects according to the number of conflicts associated with each move
     * @param list
     * @param low
     * @param high
     */
    private void quicksort(List<Move> list, int low, int high) {
        if (low < high) {
            int pi = partition(list, low, high);

            quicksort(list, low, pi-1);
            quicksort(list, pi + 1, high);
        }
    }
}
