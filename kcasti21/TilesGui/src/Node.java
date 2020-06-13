import java.util.ArrayList;

public class Node
{
    int[][] board;
    int heuristic;
    String boardString;
    int cost;
    String parent;
    boolean hasParent;

    public Node(int[][] currBoard, int heuristic, String boardString, int parentHeuristic, String par)
    {
        hasParent = false;
        this.heuristic = heuristic;
        board = new int[4][4];
        parent = par;
        for(int i = 1; i< 4; i++)
        {
            for(int j = 1; j< 4; j++)
            {
                board[i][j] = currBoard[i][j];
            }
        }
        this.boardString = boardString;
        cost = heuristic + parentHeuristic;

    }

    public Node(int[][] currBoard, int heuristic, String boardString, String parent)
    {
        board = new int[4][4];
        for(int i = 1; i< 4; i++)
        {
            for(int j = 1; j< 4; j++)
            {
                board[i][j] = currBoard[i][j];
            }
        }
        this.parent = parent;
        this.heuristic = heuristic;
        hasParent = true;
    }




    public int getHeuristic()
    {
        return heuristic;
    }
    public String getBoardString()
    {
        return boardString;
    }
    public int[][] getBoard()
    {
        return board;
    }
    public String getParent()
    {
        return parent;
    }
    public void printNode()
    {
        System.out.println(" ** ** ** BEGIN NODE ** ** ** ** ");
        System.out.println("heuristic is " + heuristic);
        System.out.println("boardString is" + boardString);
        System.out.println("board: ");
        for(int i = 1; i < 4; i++){
            System.out.print("    ");
            for(int j = 1; j < 4; j++){
                if(board[i][j] == 9)
                    System.out.print("  ");//two spaces printed in lieu of 9
                else
                    System.out.print(board[i][j] + " ");
                if( j % 3 == 0)
                {
                    System.out.println();
                }
            }
        }
        System.out.println("** ** ** END NODE ** ** **");
    }
    public void printNode2()
    {
        System.out.println("heuristic is: " + getHeuristic());
        for(int i = 1; i < 4; i++){
            System.out.print("    ");
            for(int j = 1; j < 4; j++){
                if(board[i][j] == 9)
                    System.out.print("  ");//two spaces printed in lieu of 9
                else
                    System.out.print(board[i][j] + " ");
                if( j % 3 == 0)
                {
                    System.out.println();
                }
            }
        }
    }
}