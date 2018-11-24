/******************************************************************************

                            Online Java Compiler.
                Code, Compile, Run and Debug java program online.
Write your code in this editor and press "Run" button to execute it.

*******************************************************************************/
public class Main {
    public static int row = 3;
    public static int col = 3;
    public static int lenSize = 1;

    public static void main(String[] args) {
        Maze maze = new Maze(row, col);
        maze.takeInput();
        SolveSudoku.solve(maze);
    }
}

