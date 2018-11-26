import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class MazeCell {
    public final int row;
    public final int col;
    public final List<Integer> potentialValues;
    
    private int value;
    private List<Integer> values;
    private boolean setInitial;

    public MazeCell(int num, int rowIndex, int colIndex, int mazeLength) {
        value = num;
        row = rowIndex;
        col = colIndex;
        values = new ArrayList<>();
        setInitial = isSolved();
        if (!setInitial) {
            for (int i = 0; i < mazeLength; i++) {
                values.add(i+1);
            }
        }
        potentialValues = Collections.unmodifiableList(values);
    }
    
    public boolean isInitial() {
        return setInitial;
    }

    public boolean isSolved() {
        return value != Maze.unknown;
    }
    
    public boolean isInvalid() {
        return ((!isSolved()) && (values.size() == 0));
    }

    public int getValue() {
        return value;
    }

    public void setValue(int num, Maze maze) {
        int grid = ((int)(row / maze.row)) * maze.row + ((int)(col / maze.col));
        List<Integer> removed = Arrays.asList(new Integer(num));
        for (int index = 0; index < maze.mazeLength; index++) {
            maze.rows.get(row).get(index).values.removeAll(removed);
            maze.cols.get(col).get(index).values.removeAll(removed);
            maze.grids.get(grid).get(index).values.removeAll(removed);
        }
        value = num;
        values.clear();
    }

    public boolean removeAll(List<Integer> toBeRemoved, Maze maze) {
        if (isSolved()) return false;
        boolean returnValue = values.removeAll(toBeRemoved);
        if (values.size() == 1) {
            setValue(values.get(0), maze);
        }
        return returnValue;
    }
    
    public boolean hasSamePlace(MazeCell mazeCell) {
        return row == mazeCell.row && col == mazeCell.col;
    }

    public boolean hasSameValues(MazeCell mazeCell) {
        if (this.isSolved() && mazeCell.isSolved()) {
            return this.getValue() == mazeCell.getValue();
        }
        return 
            !this.isSolved() && 
            !mazeCell.isSolved() && 
            potentialValues.equals(mazeCell.potentialValues);
    }
}
