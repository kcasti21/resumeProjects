import java.util.Comparator;
import java.util.PriorityQueue;
/*
 * used to store a queue of potential next moves when the user asks for the computer to solve the problem
 */
public class Moves
{
    public PriorityQueue<Node> moves;  //node works here too!

    public Moves()
    {
        moves = new PriorityQueue<Node>(4, new Comparator<Node>()
        {
            public int compare(Node first, Node second)
            {
                if( first.getHeuristic() > second.getHeuristic() )
                    return 1;
                return -1;
            }});
    }
    public void addMove(Node move)// might be worth it to keep track of indices, not sure yet
    {

        moves.add(move);
    }
    /* checks if list is empty or not
     * returns false if there is not a move to be made
     * used as a means of checking whehter a puzzle is soluable or not
     * if movelist is empty, no new moves to be made
     */
    public boolean isEmpty()
    {
        return moves.isEmpty();
    }
    public Node getNextMove()
    {
        return moves.poll();
    }
    public int getSize()
    {
        return moves.size();
    }
    public void printMoves()
    {
        Node[] arr = moves.toArray(new Node[0]);
        int size = arr.length;
        for(int i = 0; i<size; i++)
            arr[i].printNode();
    }
}//end Moves