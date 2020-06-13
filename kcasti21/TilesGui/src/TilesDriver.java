/**
 * TilesDriver
 * main is nolonger contained within this file
 * this file now implements a method that takes a String as a parameter and executes a move that way
 */
public class TilesDriver {
    public Board board;
    public int counter;

    public TilesDriver()
    {
        board = new Board();
        counter = 0;
        board.printBoard();
    }
    public TilesDriver(int[][] startingBoard)
    {
        board = new Board(startingBoard);
        counter = 0;
    }


    public boolean makeMove(String move)
    {
        if(move.equals("q") == true)
        {
            System.exit(0);
        }
        else if(move.equals("s"))
        {
            board.solveIt();
            return true;
        }
        else{
            board.printBoard();
            if(board.makeMoveNow(Integer.parseInt(move))) {
                counter++;
                return true;
            }
            else
            {
                return false;
            }
        }
        return false;//will never reach here, but okay
    }
    public int getHeuristic()
    {
        return board.generateHeuristic();
    }
    public int[][] getBoard()
    {
        return board.getBoard();
    }
    public int getCounter()
    {
        return counter;
    }
    public int getSolverCount()
    {
        return board.getCounter();
    }
    public void printBoard()
    {
        board.printBoard();
    }
    public int[][] getBestBoard()
    {
        return board.getBestBoard();
    }
}
