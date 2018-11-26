import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class SolveSudoku {
    public static void solve(Maze maze) {
        boolean progress = false;
        List<Strategy> strategies =
            Arrays.asList(valuePass, placePass, uniquePass);
        List<List<List<MazeCell>>> lists =
            Arrays.asList(maze.rows, maze.cols, maze.grids);
        do {
            progress = false;
            for (Strategy strategy : strategies) {
                for (List<List<MazeCell>> list : lists) {
                    boolean strategyProgress = false;
                    do {
                        strategyProgress = strategy.apply(list, maze);
                        progress = progress || strategyProgress;
                    } while (strategyProgress);
                }
            }
        } while (progress);
        if (maze.isInvalid()) {
            System.out.println("Puzzle is impossible or bad. Bad solution:-");
        } else {
            if (maze.isSolved()) {
                System.out.println("Final solution:-");
            } else {
                System.out.println("Puzzle is incomplete. Partial solution:-");
            }
        }
        maze.printState(false);
    }
    
    private interface Strategy {
        boolean apply(List<List<MazeCell>> lists, Maze maze);
    }

    private static Strategy valuePass = new Strategy() {
        public boolean apply(List<List<MazeCell>> lists, Maze maze) {
            boolean changed = false;
            for (List<MazeCell> list : lists) {
                List<Integer> toBeRemoved = new ArrayList<>();
                for (MazeCell cell : list) {
                    if (cell.isSolved()) {
                        toBeRemoved.add(cell.getValue());
                    }
                }
                for (MazeCell cell : list) {
                    changed = cell.removeAll(toBeRemoved, maze) || changed;
                }
            }
            return changed;
        }   
    };

    private static Strategy placePass = new Strategy() {
        public boolean apply(List<List<MazeCell>> lists, Maze maze) {
            boolean changed = false;
            for (List<MazeCell> list : lists) {
                for (MazeCell thisCell : list) {
                    if (thisCell.isSolved()) {
                        continue;
                    }
                    int setSize = 1;
                    for (MazeCell cell : list) {
                        if (!thisCell.hasSamePlace(cell) && 
                            thisCell.hasSameValues(cell)) {
                            setSize++;
                        }
                    }
                    if (thisCell.potentialValues.size() != setSize) {
                        continue;
                    }
                    for (MazeCell cell : list) {
                        if (thisCell.hasSameValues(cell)) {
                            continue;    
                        }
                        changed = 
                            cell.removeAll(thisCell.potentialValues, maze) ||
                            changed;
                    }
                }
            }
            return changed;
        }   
    };

    private static Strategy uniquePass = new Strategy() {
        public boolean apply(List<List<MazeCell>> lists, Maze maze) {
            boolean changed = false;
            for (List<MazeCell> list : lists) {
                for (int unique = 1; unique <= maze.mazeLength; unique++) {
                    MazeCell lastIndex = null;
                    int count = 0;
                    for (MazeCell cell : list) {
                        if (cell.potentialValues.contains(unique)) {
                            count++;
                            lastIndex = cell;
                        }
                    }
                    if (count == 1) {
                        lastIndex.setValue(unique, maze);
                        changed = true;
                    }
                }
            }
            return changed;
        }   
    };
}
