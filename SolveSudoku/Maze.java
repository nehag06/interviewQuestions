import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

class Maze
{
    public static final String delimiter = ",";
    public static final int unknown = 0;

    public final List<List<MazeCell>> rows;
    public final List<List<MazeCell>> cols;
    public final List<List<MazeCell>> grids;
    public final int row;
    public final int col;
    public final int mazeLength;

    private int lenSize;
    private int countSolvedSinceLastPrint;
    private MazeCell[][] maze;

    public Maze(int row, int col) {
        this.row = row;
        this.col = col;
        mazeLength = this.row * this.col;
        maze = new MazeCell[mazeLength][mazeLength];
        lenSize = String.valueOf(row * col).length();
        takeInput();
        List<List<MazeCell>> mazeByRow = new ArrayList<>();
        for (int i = 0; i < mazeLength; i++) {
            List<MazeCell> rowCells = new ArrayList<>();
            for (int j = 0; j < mazeLength; j++) {
                rowCells.add(maze[i][j]);
            }
            mazeByRow.add(Collections.unmodifiableList(rowCells));
        }
        rows = Collections.unmodifiableList(mazeByRow);
        List<List<MazeCell>> mazeByCol = new ArrayList<>();
        for (int i = 0; i < mazeLength; i++) {
            List<MazeCell> colCells = new ArrayList<>();
            for (int j = 0; j < mazeLength; j++) {
                colCells.add(maze[j][i]);
            }
            mazeByCol.add(Collections.unmodifiableList(colCells));
        }
        cols = Collections.unmodifiableList(mazeByCol);
        List<List<MazeCell>> mazeByGrid = new ArrayList<>();
        for (int rowIndex = 0; rowIndex < col; rowIndex++) {
            for (int colIndex = 0; colIndex < row; colIndex++) {
                List<MazeCell> gridCells = new ArrayList<>();
                for (int i = rowIndex * row; i < (rowIndex + 1) * row; i++) {
                    for (
                        int j = colIndex * col;
                        j < (colIndex + 1) * col;
                        j++) {
                        gridCells.add(maze[i][j]);
                    }
                }
                mazeByGrid.add(Collections.unmodifiableList(gridCells));
            }
        }
        grids = Collections.unmodifiableList(mazeByGrid);
    }
    
    MazeCell get(int row, int col) {
        return maze[row][col];
    }

    public boolean isSolved() {
        for (MazeCell[] cells: maze) {
            for (MazeCell cell: cells) {
                if (!cell.isSolved()) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean[] boolMarkerArr() {
        boolean[] markers = new boolean[mazeLength + 1];
        Arrays.fill(markers, false);
        return markers;
    }

    public boolean isInvalid() {
        for (int row = 0; row < mazeLength; row++) {
            for (int col = 0; col < mazeLength; col++) {
                if (maze[row][col].isInvalid()) {
                    return true;
                }
            }
        }
        for (List<List<MazeCell>> lists : Arrays.asList(rows, cols, grids)) {
            for (List<MazeCell> list : rows) {
                boolean[] markers = new boolean[mazeLength + 1];
                Arrays.fill(markers, false);
                for (MazeCell cell : list) {
                    if (cell.isSolved()) {
                        if (markers[cell.getValue() - 1]) {
                            return true;
                        } else {
                            markers[cell.getValue() - 1] = true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private void takeInput() {
        Scanner input = new Scanner(System.in);
        boolean valid = false;
        while(!valid) {
            System.out.println("Enter the Sudoku puzzle with each row " +
                "seperated by a space. In each block, enter all the numbers " +
                "seperated by [" + delimiter + "] replacing " + unknown + 
                " for unknowns: ");
            String mazeStr = input.nextLine();
            try {
                String[] rowStr = mazeStr.trim().split(" ");
                if (rowStr.length != mazeLength) {
                    throw new IllegalArgumentException(
                        "Expected number of rows are: " + mazeLength + 
                        " while actually only: " + rowStr.length + 
                        " are provided as input");
                }
                for (int i = 0; i < mazeLength; i++) {
                    String[] inputStr = rowStr[i].trim().split(",");
                    if (inputStr.length != mazeLength) {
                        throw new IllegalArgumentException(
                            "Expected numbers: " + mazeLength + "for row: " +
                            i + " while actual length of numbers are: " + 
                            inputStr.length);
                    }
                    for (int j = 0; j < mazeLength; j++) {
                        int value = Integer.parseInt(inputStr[j]);
                        if (!isValidValue(value)) {
                            throw new IllegalArgumentException("Every number " +
                                "should be between 1 and " + mazeLength +
                                " and " + unknown + " for unknowns. Value " + 
                                "input at row: " + row + " column: " + col + 
                                " is: " + inputStr[j]);
                        }
                        maze[i][j] = new MazeCell(value, i, j, mazeLength);
                        if (maze[i][j].isSolved()) {
                            countSolvedSinceLastPrint++;
                        }
                    }
                }
                valid = true;
            } catch (Exception e) {
                System.out.println(e.getMessage() + 
                    ". Please enter the maze again.");
            }
        }
        this.printState(false);
    }
    
    private boolean isValidValue(int num) {
        return num == unknown || (num >= 1 && num <= mazeLength);
    }

    public void printState(boolean detail) {
        String RED = (char)27 + "[31m";
        String YELLOW = (char)27 + "[33m";
        String BLUE = (char)27 + "[34m";
        String WHITE = (char)27 + "[37m";
        int currentSolved = 0;
        char[] spaceChars = new char[lenSize + 1];
        Arrays.fill(spaceChars, ' ');
        String spaces = new String(spaceChars);
        char[] dashChars = new char[(mazeLength * (lenSize + 1)) +
            (row * 2) + 1];
        Arrays.fill(dashChars, '-');
        String dashes = new String(dashChars);
        System.out.println("\nMaze: ");
        System.out.println(RED + dashes + WHITE);
        boolean solved = true;
        for (int i = 0; i < mazeLength; i++) {
            System.out.print(RED + "|" + WHITE);
            for (int j = 0; j < mazeLength; j++) {
                if (!maze[i][j].isSolved()) {
                    solved = false;
                    System.out.print(spaces);
                } else {
                    currentSolved++;
                    String color = maze[i][j].isInitial() ? BLUE : YELLOW;
                    System.out.print(color + String.format("% " + 
                        (lenSize + 1) + "d", maze[i][j].getValue()) + WHITE);
                }
                if ((j+1) % col == 0) {
                    System.out.print(RED + " |" + WHITE);
                }
            }
            System.out.println("");
            if ((i+1) % row == 0) {
                System.out.println(RED + dashes + WHITE);
            }
        }
        System.out.println("");
        if (!solved && detail) {
            System.out.println("Potential Values:-");
            for (int i = 0; i < mazeLength; i++) {
                for (int j = 0; j < mazeLength; j++) {
                    if (!maze[i][j].isSolved()) {
                        System.out.print("Row: " + (i + 1) + " Col: " + 
                            (j + 1) + " Values:");
                        for (
                            int k = 0;
                            k < maze[i][j].potentialValues.size();
                            k++) {
                            System.out.print(" " + 
                                maze[i][j].potentialValues.get(k));
                        }
                        System.out.println("");
                    }
                }
            }
            System.out.println("");
        }
        System.out.println("Increment since last print: " + 
            (currentSolved - countSolvedSinceLastPrint));
        countSolvedSinceLastPrint = currentSolved;
        System.out.println("Remaining to be solved: " +
            ((mazeLength * mazeLength) - currentSolved));
        System.out.println("Maze solved: " + isSolved());
        System.out.println();
        System.out.println("================================================" +
            "=================================");
        System.out.println();
    }
}
