//required to call this by this name
//should be called hashmap or something

import java.util.HashMap;
import java.util.Iterator;
public class SearchTree
{
    HashMap<String, Node> moveList;


    public SearchTree()
    {
        moveList = new HashMap<String, Node>( 5);
    }
    public void addMove(String board, Node n)
    {
        moveList.put(board, n);
    }
    public boolean checkIfMoveMade(String board)
    {
        return moveList.containsKey(board);
    }
    public void removeMove(String board)
    {
        moveList.remove(board);
    }

    public int getSize()
    {
        return moveList.size();
    }
    public Node getNode(String board)
    {
        return moveList.get(board);
    }
    public void print(){
        Iterator it = moveList.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)it.next();
            String key = (String) pair.getKey();
            Integer val = (Integer) pair.getValue();
            System.out.println("key: " + key + " val: " +  val );
        }

    }

}