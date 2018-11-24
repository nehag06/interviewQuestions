import java.util.Arrays;
import java.util.Scanner;

class Maze
{
    public static final String delimiter = ",";
    public static final int unknown = 0;

    private int row;
    private int col;
    private int mazeLength;
    private int countSolvedSinceLastPrint;
    private MazeCell[][] maze;

    public Maze(int row, int col) {
        this.row = row;
        this.col = col;
        mazeLength = this.row * this.col;
        maze = new MazeCell[mazeLength][mazeLength];
    }

    public boolean isSolved() {
        boolean solved = true;
        for (int i = 0; i < mazeLength; i++) {
            for (int j = 0; j < mazeLength; j++) {
                solved = maze[i][j].isSolved() && solved;
            }
        }
        return solved;
    }

    public MazeCell getMazeCell(int rowNum, int colNum) {
        return maze[rowNum][colNum];
    }

    public int getMazeLength() {
        return mazeLength;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
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
        for (int row = 0; row < mazeLength; row++) {
            boolean[] rowNum = boolMarkerArr();
            for (int col = 0; col < mazeLength; col++) {
                if (maze[row][col].isSolved()) {
                    if (rowNum[maze[row][col].getValue()]) {
                        return true;
                    } else {
                        rowNum[maze[row][col].getValue()] = true;
                    }
                }
            }
        }
        for (int col = 0; col < mazeLength; col++) {
            boolean[] colNum = boolMarkerArr();
            for (int row = 0; row < mazeLength; row++) {
                if (maze[row][col].isSolved()) {
                    if (colNum[maze[row][col].getValue()]) {
                        return true;
                    } else {
                        colNum[maze[row][col].getValue()] = true;
                    }
                }
            }
        }
        for (int rowGrid = 0; rowGrid < getCol(); rowGrid++) {
            for (int colGrid = 0; colGrid < getRow(); colGrid++) {
                boolean[] gridNum = boolMarkerArr();
                for (
                    int row = rowGrid * getRow();
                    row < (rowGrid + 1) * getRow();
                    row++) {
                    for (
                        int col = colGrid * getCol();
                        col < (colGrid + 1) * getCol();
                        col++) {
                        if (maze[row][col].isSolved()) {
                            if (gridNum[maze[row][col].getValue()]) {
                                return true;
                            } else {
                                gridNum[maze[row][col].getValue()] = true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public void takeInput() {
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
                        maze[i][j] = new MazeCell(value, this);
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
        char[] spaceChars = new char[Main.lenSize + 1];
        Arrays.fill(spaceChars, ' ');
        String spaces = new String(spaceChars);
        char[] dashChars = new char[(mazeLength + Main.col) * 2 + 1];
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
                        (Main.lenSize + 1) + "d", maze[i][j].getValue()) +
                        WHITE);
                }
                if ((j+1) % Main.col == 0) {
                    System.out.print(RED + " |" + WHITE);
                }
            }
            System.out.println("");
            if ((i+1) % Main.row == 0) {
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
                            k < maze[i][j].getPotentialValues().size();
                            k++) {
                            System.out.print(" " + 
                                maze[i][j].getPotentialValues().get(k));
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
