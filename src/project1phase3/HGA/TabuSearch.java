package project1phase3.HGA;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

import project1phase3.Vertex;

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
        long sum_time = 0;
        int iterations_complete = 0;
        int count = 0;
        while (iterations > 0) {
            if (currentConfig.getTotalConflictCount() == 0) {
                return currentConfig;
            }
            long start_time = System.nanoTime();
            List<Move> neighbours = getNeighbourMoves(currentConfig);

            /* 
            List<Move> neighbours2 = new ArrayList<>();
            for (Move move : neighbours) {
               neighbours2.add(move.clone());
            }
            */

            /* 
            long regSortStart = System.nanoTime();
            neighbours.sort(new Comparator<Move>() {
                @Override
                public int compare(Move m1, Move m2) {
                    return Integer.compare(m1.getConflicts(), m2.getConflicts());
                }
            });
            long regSortTime = System.nanoTime() - regSortStart;
            */

            //long quicksortStart = System.nanoTime();
            quicksort(neighbours, 0, neighbours.size()-1);
            //long quicksortTime = System.nanoTime() - quicksortStart;

            //System.out.println(iterations + "--> regular sort:" + regSortTime + "\nquick sort: " + quicksortTime);

            Move bestMove = neighbours.get(0);
            for (Move move : neighbours) {
                if (tabuList.contains(move)) {
                    if (move.getConflicts() < bestConfig.getTotalConflictCount()) {
                        bestMove = move;
                        System.out.println("Used aspiration criteria");
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
                sum_time += end_time;
                iterations_complete++;
                System.out.println("found in "+ count + " iterations");
                System.out.println("Average tabu search time = " + (sum_time/iterations_complete));
                return currentConfig;
            }
            if (currentConfig.getTotalConflictCount() <= bestConfig.getTotalConflictCount()) {
                bestConfig = currentConfig;
            }
            
            //System.out.println(currentConfig.getTotalConflictCount());
            iterations--;
            count++;
            long end_time = System.nanoTime() - start_time;
            sum_time += end_time;
            iterations_complete++;
        }

        System.out.println("Average tabu search time = " + (sum_time / iterations_complete));
        return bestConfig;
    }

    private List<Move> getNeighbourMoves(Configuration c) {
        List<Move> neighbouringMoves = new ArrayList<>();
        for (int i = 0; i < c.partition.size(); i++) {
            for (int j = 0; j < c.partition.get(i).size(); j++) {
                for (int id : c.partition.get(i).get(j).getNeighboursAsIntList()) {
                    if (c.partition.get(i).stream().anyMatch(element -> element.identification() == id))  {
                        int[] vPos = {i,j};
                        for (int k = 0; k < c.partition.size(); k++) {
                            if (c.partition.get(i).get(j).identification()==26) {
                                //System.out.print(i+ " ");
                            }
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
        //System.out.println();
        return neighbouringMoves;
    }

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

    private void quicksort(List<Move> list, int low, int high) {
        if (low < high) {
            int pi = partition(list, low, high);

            quicksort(list, low, pi-1);
            quicksort(list, pi + 1, high);
        }
    }
}
