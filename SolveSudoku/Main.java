/******************************************************************************
This programs solves any given sudoku.

 row: Total number of rows in sub-grid
 col: Total number of cols in sub-grid
 lenSize: Total number of numbers in row*col
 
 For e.g., for a 12*12 grid, with a sub-grid of 3 rows and 4 cols,
 row = 3, col = 4 and lenSize = 2.
 
 Sample data is in data.txt.

*******************************************************************************/
public class Main {
    public static int row = 3;
    public static int col = 4;
    public static int lenSize = 2;

    public static void main(String[] args) {
        Maze maze = new Maze(row, col);
        maze.takeInput();
        SolveSudoku.solve(maze);
    }
}

