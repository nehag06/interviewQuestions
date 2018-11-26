/******************************************************************************
Solves any given Sudoku.

 row: Total number of rows in sub-grid
 col: Total number of cols in sub-grid
 
 For e.g., for a 12*12 grid, with a sub-grid of 3 rows and 4 cols,
 row = 3, col = 4.
 
 Sample data is in data.txt.

*******************************************************************************/
import java.util.Scanner;

public final class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the number of rows in sub-grid: ");
        int row = input.nextInt();
        System.out.println("Enter the number of cols in sub-grid: ");
        int col = input.nextInt();
        Maze maze = new Maze(row, col);
        SolveSudoku.solve(maze);
    }
}
