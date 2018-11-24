import java.util.ArrayList;
import java.util.List;

class MazeCell {
    private int value;
    private List<Integer> values;
    private boolean setInitial;

    public MazeCell(int num, Maze maze) {
        value = num;
        values = new ArrayList<>();
        if (!isSolved()) {
            for (int i = 0; i < maze.getMazeLength(); i++) {
                values.add(i+1);
            }
            setInitial = false;
        } else {
            setInitial = true;
        }
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

    public List<Integer> getPotentialValues() {
        return values;
    }

    public boolean removeAll(List<Integer> toBeRemoved) {
        if (isSolved()) return false;
        return values.removeAll(toBeRemoved);
    }
    
    public boolean setValueIfPossible() {
        if (values.size() == 1) {
            setValue(values.get(0));
            return true;
        }
        return false;
    }
    
    public void setValue(int num) {
        value = num;
        values.clear();
    }

    public boolean hasSameValues(MazeCell mazeCell) {
        if (this.isSolved() && mazeCell.isSolved()) {
            return this.getValue() == mazeCell.getValue();
        } else if ((!this.isSolved()) && (!mazeCell.isSolved())) {
            return this.getPotentialValues().equals(
                mazeCell.getPotentialValues());
        } else {
            return false;
        }
    }
}
