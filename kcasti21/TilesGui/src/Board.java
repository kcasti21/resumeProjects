import java.util.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/* Board class, contains logic for the board
 *  additionally this is where the best first search algorithm is implemented
 *
 */
public class Board
{
    private volatile int[][]  board; //using to represent the blank tile
    private ArrayList boardVals;  //used to help generate random board states
    private SearchTree moveList;  //hashmap containing all the moves already made
    private volatile int counter;
    public volatile int[][] best;
    public Board()
    {
        //solution = new ArrayList<Node>();
        boardVals = new ArrayList();
        counter = 1;
        board = new int[4][4]; //having irrelevant 0 - row  and 0 column to correct for off by 1
        for(int i = 1; i <= 9; i++) //populate arraylist with all possible board values
            boardVals.add(i);         //9 represents the blank space


        for(int i = 1 ; i < 4; i++)
        {
            for(int j = 1; j< 4; j++)
            {
                board[i][j] = valueGetter();
            }
        }
    } //end constructor
    public Board(int[][] board)
    {
        this.board = new int[4][4];
        for(int i = 1; i < 4; i++) {
            for(int j = 1; j < 4; j++) {
                this.board[i][j] = board[i][j];
            }
        }

    }

    public int[][] getBoard()
    {
        return board;
    }

    /*
     *  helper function that ensures random boards
     */
    private int valueGetter()
    {
        Random randomGenerator = new Random();
        randomGenerator.setSeed( System.currentTimeMillis());
        int index = randomGenerator.nextInt() % boardVals.size(); //used to randomly generate an index into the array list so the next value added to the board will be random
        index = (index > 0) ? index : (-1 * index);
        int tile = (int) boardVals.get(index);
        boardVals.remove(index);
        return tile;
    } //end valueGetter


    public void printBoard()
    {
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
    /*
     * used for debugging **ignore**
     */
    private void printBoard(int[][] board)
    {
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
    /*
     * takes a 2d array as input and replaces the values of the game board with them
     */
    private void setBoard( int[][] newBoard)
    {
        for(int i = 1; i< 4;i++)
        {
            for(int j =1 ; j< 4; j++)
            {
                board[i][j] = newBoard[i][j];
            }
        }

    }
    /*
     * takes tile and its position within the array as parameters and returns its distance from its desired position   *
     */
    private int  heuristicHelper(int tile, int row, int col) //returns distance from correct position
    {
        int colDistance =0;
        int rowDistance = 0;
        if(tile == 1)
        {
            rowDistance = row -1;
            colDistance = col -1;
        }
        else if(tile == 2)
        {
            rowDistance = row - 1;
            if(col -2 >= 0)
                colDistance = col - 2;
            else
                colDistance = 2 - col;
        }
        else if(tile ==3)
        {
            rowDistance = row -1;
            colDistance = 3 - col;
        }
        else if(tile ==4)
        {
            if(row -2 > 0)
                rowDistance = row -2;
            else
                rowDistance = 2 - row;
            colDistance = col -1;
        }
        else if(tile == 5)
        {
            if(row-2 > 0)
                rowDistance = row -2;
            else
                rowDistance = 2-row;
            if(col -2 >= 0)
                colDistance = col -2;
            else
                colDistance = 2 - col;
        }
        else if(tile == 6)
        {
            if(row -2 >= 0)
                rowDistance = row -2;
            else
                rowDistance = 2 - row;
            colDistance = 3 - col;
        }
        else if(tile == 7)
        {
            rowDistance = 3 - row;
            colDistance = col - 1;
        }
        else if(tile == 8)
        {
            rowDistance = 3 - row;
            if (col -2 >= 0)
                colDistance = col-2;
            else
                colDistance = 2 - col;
        }
        else if(tile == 9)
        {
            rowDistance = 3 - row;
            colDistance = 3 - col;
        }
        return rowDistance + colDistance;
    }
    /*calls heuristicHelper for each element and sums the results.
     *
     */
    public int generateHeuristic()
    {

        int heuristicValue = 0;
        for(int i = 1; i<4; i++)
        {
            for(int j = 1; j<4; j++)
            {
                //System.out.println( "tile: " + board[i][j] + " distance: " + heuristicHelper(board[i][j], i, j));
                heuristicValue += heuristicHelper(board[i][j], i, j);
            }
        }
        return heuristicValue;
    }
    public int generateHeuristic(int[][] board)
    {

        int heuristicValue = 0;
        for(int i = 1; i<4; i++)
        {
            for(int j = 1; j<4; j++)
            {
                heuristicValue += heuristicHelper(board[i][j], i, j);
            }
        }
        return heuristicValue;
    }
    /*swaps the positions of two tiles in the board
     *checks:
     * to see if one of the specified tiles is the tile 9
     * whether the columns and rows are within bounds
     * and whether the inputs are one appart from eachother.
     */
    public void swapTiles(int rowOne, int colOne, int rowTwo, int colTwo, int[][] tmp) throws MoveException
    {

        if(!(colOne >=1 && colOne <= 3 && rowOne >=1 && rowOne <=3 && colTwo >= 1 && colTwo <= 3 && rowTwo >= 1 && rowTwo <= 3)) // add exception
        {
            throw new MoveException();
        }
        int tmpOne = tmp[rowOne][colOne];
        int tmpTwo = tmp[rowTwo][colTwo];
        assert(tmpOne == 9);
        tmp[rowOne][colOne] = tmpTwo;
        tmp[rowTwo][colTwo] = tmpOne;
        assert(tmp[rowTwo][colTwo] == 9);
    }

    /* returns 2 element array = {row, col}
     *  input checked way before it gets here, so no checks performed
     */
    public int[] getIndex(int value)
    {

        int[] rowCol = new int[2];

        for(int i = 1; i < 4; i++)
        {
            for(int j = 1; j<4;j++)
            {
                if(board[i][j] == value)
                {
                    rowCol[0] = i;
                    rowCol[1] = j;
                    return rowCol;
                }
            }
        }
        return rowCol;
    }
    /* helper function
     * takes two arrays as parameters
     * 2-d array from and 1-d array to
     * copies from to to
     * used for hashing
     */
    private void copyArray(int[][] from, int[] to)
    {
        int index = 0;
        for(int i =1 ; i < 4; i++)
        {
            for(int j = 1; j < 4; j++)
            {
                to[index] = from[i][j];
                index++;
            }//end for(int j = 1;...
        }//end for(int i = 1;...
    }//end copyArray


    /* helper function for makeMove
     * creates priority queue (priority is given to configurations with lower heuristic values
     * @param int heuristic, heuristic value of parent
     * @return ArrayLIst<Node> of moves
     */
    public ArrayList<Node> makeMoveList(int heuristic, String parent)
    {
        ArrayList<Node> moves = new ArrayList<Node>();
        int[] index = getIndex(9);
        int i = index[0];
        int j = index[1];
        int[][] tmp1 = new int[4][4], tmp2 = new int[4][4], tmp3 = new int[4][4], tmp4 = new int[4][4];
        int[] arr = new int[9];
        try
        {
            boardCopy(tmp1);
            swapTiles(i, j, i+1, j, tmp1);
            copyArray(tmp1, arr);
            String s = Arrays.toString(arr).replaceAll("\\[|\\]|,|\\s", "");
            Node newMove = new Node(tmp1, generateHeuristic(tmp1), s, generateHeuristic(), parent);
            moves.add(newMove);
        }
        catch(ArrayIndexOutOfBoundsException e){}
        catch(MoveException e){}
        try
        {
            boardCopy(tmp2);
            swapTiles(i, j, i-1, j, tmp2);
            copyArray(tmp2, arr);

            String s = Arrays.toString(arr).replaceAll("\\[|\\]|,|\\s", "");

            Node newMove = new Node(tmp2, generateHeuristic(tmp2),s, generateHeuristic(), parent);
            moves.add(newMove);
        }
        catch(ArrayIndexOutOfBoundsException e){}
        catch(MoveException e){}
        try
        {
            boardCopy(tmp3);
            swapTiles(i, j, i, j+1, tmp3);
            copyArray(tmp3, arr);
            String s = Arrays.toString(arr).replaceAll("\\[|\\]|,|\\s", "");

            Node newMove = new Node(tmp3, generateHeuristic(tmp3),s, generateHeuristic(), parent);
            moves.add(newMove);
        }
        catch(ArrayIndexOutOfBoundsException e){}
        catch(MoveException e){ }
        try
        {
            boardCopy(tmp4);
            swapTiles(i, j, i, j-1, tmp4);
            copyArray(tmp4, arr);
            String s = new String(Arrays.toString(arr).replaceAll("\\[|\\]|,|\\s", ""));
            Node newMove = new Node(tmp4, generateHeuristic(tmp4),s, generateHeuristic(), parent);
            moves.add(newMove);
        }
        catch(ArrayIndexOutOfBoundsException e){}
        catch(MoveException e){}
        return moves;
    }


    /* helper function used in makeMoveNow
     * checks to see that the user is making a valid move
     */
    private boolean checkInput(int[] first, int[] second)
    {
        int thing1 = ((first[0] - second[0]) >= 0 ) ? (first[0] - second[0]) : (-(first[0] - second[0]));
        int thing2 = ((first[1] - second[1]) >= 0) ? (first[1] - second[1]) : (-(first[1] - second[1]));
        if( (thing1 == 1 && thing2 == 0 )|| (thing1 == 0 && thing2 == 1)) //recheck this input
            return true;
        return false;

    }


    /*
     * switches the place of both tiles
     * used to execute a player move
     */
    public void swapTiles(int rowOne, int colOne, int rowTwo, int colTwo)
    {


        int tmpOne = board[rowOne][colOne];
        int tmpTwo = board[rowTwo][colTwo];


        board[rowOne][colOne] = tmpTwo;
        board[rowTwo][colTwo] = tmpOne;
    }

    /* helper function
     * takes a 2-d array as a parameter
     * copies the board into the array
     */
    private void boardCopy( int[][] arr)
    {
        for(int i = 1; i< 4; i++)
        {
            for(int j = 1; j< 4; j++)
            {
                arr[i][j] = board[i][j];
            }
        }
    }
    /*
     * function that executes a user move
     */
    public boolean makeMoveNow(int tile)
    {
        int[] index = getIndex(tile);
        int[] blankIndex = getIndex(9);
        if(checkInput(index, blankIndex))
        {
            swapTiles(index[0], index[1], blankIndex[0], blankIndex[1]);
            return true;
        }
        else {
            return false;
        }


    }

    /*
     * wrapper function for for computer solving board
     */
    public boolean solveIt()
    {
        moveList = new SearchTree();

        BFS();
        if(generateHeuristic() == 0)
        {
            System.out.println("Hey! I was able to solve this one, it took only " + counter + " Moves");
            return true;
        }
        else
        {
            System.out.println("no luck! " + counter + " moves tried");
            return false;
        }
    }
    public String generateBoardString()
    {
        int[] arr = new int[9];
        copyArray(board, arr);
        String s = Arrays.toString(arr).replaceAll("\\[|\\]|,|\\s", "");
        return s;
    }
    public int getCounter()
    {
        return counter;
    }
    public int[][] getBestBoard()
    {
        return best;
    }
    /* A* algorithm
     *
     * @param Node Best // used to capture the best board state
     *
     */
    private synchronized void BFS(  ) {
        boolean first = true;
        Node currentBoard = new Node(board, generateHeuristic(), generateBoardString(), 0, "none");
        SearchTree openMoves = new SearchTree();  //openMoves
        SearchTree madeMoves = new SearchTree(); //madeMoves
        Moves openMovesTree = new Moves();
        openMovesTree.addMove(currentBoard);
        best = new int[4][4];
        for(int i = 1; i < 4; i++) {
            for (int j = 1; j < 4; j++) {
                best[i][j] = board[i][j];
            }
        }
        while (!openMovesTree.isEmpty()) //while move is not in movelist
        {
            counter++; //increment move counter

//delay to give the baord a chance to update
            try {
                Thread.sleep(1);
            }
            catch (InterruptedException ex){}

//end delay for board update

            Node curr = openMovesTree.getNextMove();
            setBoard(curr.getBoard());
            if( generateHeuristic(best) > curr.getHeuristic()) {
                for(int i = 1; i < 4; i++)
                {
                    for(int j = 1; j < 4; j++)
                    {
                        best[i][j] = curr.getBoard()[i][j];
                    }
                }
            }
            if (curr.getHeuristic() == 0) {
                return;
            }
            ArrayList<Node> adjMoveList = makeMoveList(curr.getHeuristic(), curr.getBoardString());
            int loopDriver = adjMoveList.size();
            for (int i = 0; i < loopDriver; i++) {
                Node n = adjMoveList.get(i);
                if (madeMoves.checkIfMoveMade(n.getBoardString()))
                    ; //skip if this move has been made
                else {
                    if (openMoves.checkIfMoveMade(n.getBoardString()))
                        ; //do nothing, move is already part of the open moves tree
                    else {
                        openMoves.addMove(n.getBoardString(), n);
                        openMovesTree.addMove(n);
                    }
                }
            } //end for(int i = 0; i < loopDriver; i++)

        }


    } //end bfs...


}//end Board class
