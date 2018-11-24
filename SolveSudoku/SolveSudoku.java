import java.util.ArrayList;
import java.util.List;

class SolveSudoku {
    public static void solve(Maze maze) {
        boolean progress = false;
        do {
            progress = false;
            boolean rowValuePassProgress = false;
            do {
                rowValuePassProgress = rowValuePass(maze);
                progress = progress || rowValuePassProgress;
            } while (rowValuePassProgress);
            boolean colValuePassProgress = false;
            do {
                colValuePassProgress = colValuePass(maze);
                progress = progress || colValuePassProgress;
            } while (colValuePassProgress);
            boolean gridValuePassProgress = false;
            do {
                gridValuePassProgress = gridValuePass(maze);
                progress = progress || gridValuePassProgress;
            } while (gridValuePassProgress);
            boolean rowPlacePassProgress = false;
            do {
                rowPlacePassProgress = rowPlacePass(maze);
                progress = progress || rowPlacePassProgress;
            } while (rowPlacePassProgress);
            boolean colPlacePassProgress = false;
            do {
                colPlacePassProgress = colPlacePass(maze);
                progress = progress || colPlacePassProgress;
            } while (colPlacePassProgress);
            boolean gridPlacePassProgress = false;
            do {
                gridPlacePassProgress = gridPlacePass(maze);
                progress = progress || gridPlacePassProgress;
            } while (gridPlacePassProgress);
            boolean rowUniquePassProgress = false;
            do {
                rowUniquePassProgress = rowUniquePass(maze);
                progress = progress || rowUniquePassProgress;
            } while (rowUniquePassProgress);
            boolean colUniquePassProgress = false;
            do {
                colUniquePassProgress = colUniquePass(maze);
                progress = progress || colUniquePassProgress;
            } while (colUniquePassProgress);
            boolean gridUniquePassProgress = false;
            do {
                gridUniquePassProgress = gridUniquePass(maze);
                progress = progress || gridUniquePassProgress;
            } while (gridUniquePassProgress);
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

    private static boolean rowValuePass(Maze maze) {
        boolean changed = false;
        for (int row = 0; row < maze.getMazeLength(); row++) {
            List<Integer> toBeRemoved = new ArrayList<>();
            for (int col = 0; col < maze.getMazeLength(); col++) {
                if (maze.getMazeCell(row, col).isSolved()) {
                    toBeRemoved.add(maze.getMazeCell(row, col).getValue());
                }
            }
            for (int col = 0; col < maze.getMazeLength(); col++) {
                changed = maze.getMazeCell(row, col)
                    .removeAll(toBeRemoved) || changed;
                if (maze.getMazeCell(row, col).setValueIfPossible()) {
                    setUniqueValue(
                        maze, maze.getMazeCell(row, col).getValue(), row, col);
                }
            }
        }
        return changed;
    }

    private static boolean colValuePass(Maze maze) {
        boolean changed = false;
        for (int col = 0; col < maze.getMazeLength(); col++) {
            List<Integer> toBeRemoved = new ArrayList<>();
            for (int row = 0; row < maze.getMazeLength(); row++) {
                if (maze.getMazeCell(row, col).isSolved()) {
                    toBeRemoved.add(maze.getMazeCell(row, col).getValue());
                }
            }
            for (int row = 0; row < maze.getMazeLength(); row++) {
                changed = maze.getMazeCell(row, col)
                    .removeAll(toBeRemoved) || changed;
                if (maze.getMazeCell(row, col).setValueIfPossible()) {
                    setUniqueValue(
                        maze, maze.getMazeCell(row, col).getValue(), row, col);
                }
            }
        }
        return changed;
    }

    private static boolean gridValuePass(Maze maze) {
        boolean changed = false;
        for (int rowGrid = 0; rowGrid < maze.getCol(); rowGrid++) {
            for (int colGrid = 0; colGrid < maze.getRow(); colGrid++) {
                List<Integer> toBeRemoved = new ArrayList<>();
                for (
                    int row = rowGrid * maze.getRow();
                    row < (rowGrid + 1) * maze.getRow();
                    row++) {
                    for (
                        int col = colGrid * maze.getCol();
                        col < (colGrid + 1) * maze.getCol();
                        col++) {
                        if (maze.getMazeCell(row, col).isSolved()) {
                            toBeRemoved.add(
                                maze.getMazeCell(row, col).getValue());
                        }
                    }
                }
                for (
                    int row = rowGrid * maze.getRow();
                    row < (rowGrid + 1) * maze.getRow();
                    row++) {
                    for (
                        int col = colGrid * maze.getCol();
                        col < (colGrid + 1) * maze.getCol();
                        col++) {
                        changed = maze.getMazeCell(row, col)
                            .removeAll(toBeRemoved) || changed;
                        if (maze.getMazeCell(row, col).setValueIfPossible()) {
                            setUniqueValue(
                                maze, maze.getMazeCell(row, col).getValue(),
                                row, col);
                        }
                    }
                }
            }
        }
        return changed;
    }

    private static boolean rowPlacePass(Maze maze) {
        boolean changed = false;
        for (int row = 0; row < maze.getMazeLength(); row++) {
            for (int coli = 0; coli < maze.getMazeLength(); coli++) {
                MazeCell thisCell = maze.getMazeCell(row, coli);
                if (thisCell.isSolved()) {
                    continue;
                }
                int setSize = 1;
                for (int colj = 0; colj < maze.getMazeLength(); colj++) {
                    if (coli == colj) {
                        continue;
                    }
                    if (thisCell.hasSameValues(maze.getMazeCell(row, colj))) {
                        setSize++;    
                    }
                }
                if (thisCell.getPotentialValues().size() != setSize) {
                    continue;
                }
                for (int colj = 0; colj < maze.getMazeLength(); colj++) {
                    if (thisCell.hasSameValues(maze.getMazeCell(row, colj))) {
                        continue;    
                    }
                    changed = maze.getMazeCell(row, colj)
                        .removeAll(thisCell.getPotentialValues()) ||
                        changed;
                    if (maze.getMazeCell(row, colj).setValueIfPossible()) {
                        setUniqueValue(
                            maze, maze.getMazeCell(row, colj).getValue(), 
                            row, colj);
                    }
                }
            }
        }
        return changed;
    }

    private static boolean colPlacePass(Maze maze) {
        boolean changed = false;
        for (int col = 0; col < maze.getMazeLength(); col++) {
            for (int rowi = 0; rowi < maze.getMazeLength(); rowi++) {
                MazeCell thisCell = maze.getMazeCell(rowi, col);
                if (thisCell.isSolved()) {
                    continue;
                }
                int setSize = 1;
                for (int rowj = 0; rowj < maze.getMazeLength(); rowj++) {
                    if (rowi == rowj) {
                        continue;
                    }
                    if (thisCell.hasSameValues(maze.getMazeCell(rowj, col))) {
                        setSize++;    
                    }
                }
                if (thisCell.getPotentialValues().size() != setSize) {
                    continue;
                }
                for (int rowj = 0; rowj < maze.getMazeLength(); rowj++) {
                    if (thisCell.hasSameValues(maze.getMazeCell(rowj, col))) {
                        continue;    
                    }
                    changed = maze.getMazeCell(rowj, col)
                        .removeAll(thisCell.getPotentialValues()) ||
                        changed;
                    if (maze.getMazeCell(rowj, col).setValueIfPossible()) {
                        setUniqueValue(
                            maze, maze.getMazeCell(rowj, col).getValue(),
                            rowj, col);
                    }
                }
            }
        }
        return changed;
    }

    private static boolean gridPlacePass(Maze maze) {
        boolean changed = false;
        for (int rowGrid = 0; rowGrid < maze.getCol(); rowGrid++) {
            for (int colGrid = 0; colGrid < maze.getRow(); colGrid++) {
                for (
                    int rowi = rowGrid * maze.getRow();
                    rowi < (rowGrid + 1) * maze.getRow();
                    rowi++) {
                    for (
                        int coli = colGrid * maze.getCol();
                        coli < (colGrid + 1) * maze.getCol();
                        coli++) {
                        MazeCell thisCell = maze.getMazeCell(rowi, coli);
                        if (thisCell.isSolved()) {
                            continue;
                        }
                        int setSize = 1;
                        for (
                            int rowj = rowGrid * maze.getRow();
                            rowj < (rowGrid + 1) * maze.getRow();
                            rowj++) {
                            for (
                                int colj = colGrid * maze.getCol();
                                colj < (colGrid + 1) * maze.getCol();
                                colj++) {
                                if (rowi == rowj && coli == colj) {
                                    continue;
                                }
                                if (thisCell.hasSameValues(
                                    maze.getMazeCell(rowj, colj))) {
                                    setSize++;    
                                }
                            } 
                        }
                        if (thisCell.getPotentialValues().size() != setSize) {
                            continue;
                        }
                        for (
                            int rowj = rowGrid * maze.getRow();
                            rowj < (rowGrid + 1) * maze.getRow();
                            rowj++) {
                            for (
                                int colj = colGrid * maze.getCol();
                                colj < (colGrid + 1) * maze.getCol();
                                colj++) {
                                if (thisCell.hasSameValues(
                                    maze.getMazeCell(rowj, colj))) {
                                    continue;    
                                }
                                changed = maze.getMazeCell(rowj, colj)
                                    .removeAll(thisCell.getPotentialValues()) ||
                                    changed;
                                if (maze.getMazeCell(rowj, colj)
                                    .setValueIfPossible()) {
                                    setUniqueValue(
                                        maze,
                                        maze.getMazeCell(rowj, colj).getValue(),
                                        rowj, colj);
                                }
                            } 
                        }
                    }
                }
            }
        }
        return changed;
    }
    
    private static void setUniqueValue(
        Maze maze, Integer uniqueNum, int rowIndex, int colIndex) {
        for (int index = 0; index < maze.getMazeLength(); index++) {
            maze.getMazeCell(rowIndex, index)
                .getPotentialValues().remove(uniqueNum);
            maze.getMazeCell(index, colIndex)
                .getPotentialValues().remove(uniqueNum);
        }
        int rowGrid = rowIndex / maze.getCol();
        int colGrid = colIndex / maze.getRow();
        for (
            int row = rowGrid * maze.getRow();
            row < (rowGrid + 1) * maze.getRow();
            row++) {
            for (
                int col = colGrid * maze.getCol();
                col < (colGrid + 1) * maze.getCol();
                col++) {
                maze.getMazeCell(row, col)
                    .getPotentialValues().remove(uniqueNum);
            }
        }
        
    }

    private static boolean rowUniquePass(Maze maze) {
        boolean changed = false;
        for (int row = 0; row < maze.getMazeLength(); row++) {
            for (int unique = 1; unique <= maze.getMazeLength(); unique++) {
                Integer uniqueNum = new Integer(unique);
                int lastColIndex = -1;
                int count = 0;
                for (int col = 0; col < maze.getMazeLength(); col++) {
                    MazeCell thisCell = maze.getMazeCell(row, col);
                    if (thisCell.getPotentialValues().contains(uniqueNum)) {
                        count++;
                        lastColIndex = col;
                    }
                }
                if (count == 1) {
                    maze.getMazeCell(row, lastColIndex).setValue(unique);
                    setUniqueValue(maze, uniqueNum, row, lastColIndex);
                    changed = true;
                }
            }
        }
        return changed;
    }

    private static boolean colUniquePass(Maze maze) {
        boolean changed = false;
        for (int col = 0; col < maze.getMazeLength(); col++) {
            for (int unique = 0; unique < maze.getMazeLength(); unique++) {
                Integer uniqueNum = new Integer(unique);
                int lastRowIndex = -1;
                int count = 0;
                for (int row = 0; row < maze.getMazeLength(); row++) {
                    MazeCell thisCell = maze.getMazeCell(row, col);
                    if (thisCell.getPotentialValues().contains(uniqueNum)) {
                        count++;
                        lastRowIndex = row;
                    }
                }
                if (count == 1) {
                    maze.getMazeCell(lastRowIndex, col).setValue(unique);
                    setUniqueValue(maze, uniqueNum, lastRowIndex, col);
                    changed = true;
                }
            }
        }
        return changed;
    }

    private static boolean gridUniquePass(Maze maze) {
        boolean changed = false;
        for (int rowGrid = 0; rowGrid < maze.getCol(); rowGrid++) {
            for (int colGrid = 0; colGrid < maze.getRow(); colGrid++) {
                for (int unique = 0; unique < maze.getMazeLength(); unique++) {
                    Integer uniqueNum = new Integer(unique);
                    int lastRowIndex = -1;
                    int lastColIndex = -1;
                    int count = 0;
                    for (
                        int row = rowGrid * maze.getRow();
                        row < (rowGrid + 1) * maze.getRow();
                        row++) {
                        for (
                            int col = colGrid * maze.getCol();
                            col < (colGrid + 1) * maze.getCol();
                            col++) {
                            MazeCell thisCell = maze.getMazeCell(row, col);
                            if (thisCell.getPotentialValues().contains(
                                uniqueNum)) {
                                count++;
                                lastRowIndex = row;
                                lastColIndex = col;
                            }
                        }
                    }
                    if (count == 1) {
                        maze.getMazeCell(lastRowIndex, lastColIndex)
                            .setValue(unique);
                        setUniqueValue(
                            maze, uniqueNum, lastRowIndex, lastColIndex);
                        changed = true;
                    }
                }
            }
        }
        return changed;
    }
}
