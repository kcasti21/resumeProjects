import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Color;

/**
 * Controller class where gui is implemeneted
 *  extends Frame class
 *  implements actionListener
 *  Controls gui and utilizes tile driver to play the game
 */
public class Controller extends JFrame implements ActionListener{
    private volatile JButton[][] button; // game board
    private volatile TextField counterText; // displays the number of moves made
    private volatile TextField heuristicText;  // displays the heuristic value of the current board
    private Button Quit;
    private volatile JPanel labelGrid;
    private Button Solve;
    private Button customBoard;
    private volatile JPanel buttonGrid;
    private int counter;
    private int moveCount;
    private final int BOARD_SIZE = 3;
    private int[][] newGameBoard;
    public volatile TilesDriver game;
    private volatile String s;
    public volatile boolean solveMode;
    private Thread solveThread;
    public boolean customMode;

    public Controller(){
        game = new TilesDriver();
        int heuristic = game.getHeuristic();
        int board[][] = new int[4][4];
        board = game.getBoard();
        buttonGrid = new JPanel();
        buttonGrid.setLayout(new GridLayout(3,3));
        labelGrid = new JPanel();
        labelGrid.setLayout(new GridLayout(1,2));

        super.setLayout(new FlowLayout()); //frmae on which everyhting will be layed
        button = new JButton[BOARD_SIZE][BOARD_SIZE];
        counter = -1;
        for(int i = 0; i<BOARD_SIZE ; i++)
        {
            for(int j = 0; j < BOARD_SIZE; j++)
            {
                button[i][j] = new JButton();
                button[i][j].setBackground(Color.CYAN);
                buttonGrid.add(button[i][j]);
            }
        }
        setButton(board);
        super.add(buttonGrid);
        counterText = new TextField();
        heuristicText = new TextField();
        customBoard = new Button("Custom Board");
        customBoard.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e)
            {
                customMode = true;
                doStartNewGame();
            }
        });
        //quit button
        Quit = new Button("Quit");
        Quit.setActionCommand("q");
        Quit.addActionListener(this);
        //solve button
        Solve = new Button("Solve");
        Solve.setActionCommand("s");
        Solve.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                System.out.println("Starting to solve it");
                solveThread = new Thread()
                {
                  public void run(){
                      solveMode = true;
                      game.makeMove("s");
                  }
                };
                solveThread.start();
                solveThread.interrupt();
                if(counter < 300)
                {
                    setHeuristicText(game.getHeuristic());
                    setCounterText(game.getSolverCount());
                    setButton(game.getBoard());
                }
                //add delay here to give the board a chance to update and for the you win message to appear
                /*
                try {
                    Thread.sleep(500);
                } catch(InterruptedException exx){}
                */
            }
        });
        labelGrid.add(heuristicText);
        labelGrid.add(counterText);
        //add stuff to Frame
        labelGrid.setPreferredSize(new Dimension(300, 50));
        super.add(labelGrid);
        super.add(Quit);
        super.add(Solve);
        super.add(customBoard);
        super.setVisible(true);
        super.pack();
    }
//opens jframe that tells the user that tha auto solver wasn't able to solve the tile-game
    public void youWin()
    {
        JFrame win = new JFrame("Winner!");
        Font f = new Font("SansSerif", Font.BOLD, 50);
        TextField text = new TextField("you Win!");
        text.setFont(f);
        text.setPreferredSize(new Dimension(300, 300));
        win.add(text);
        win.setPreferredSize(new Dimension(300, 300));
        win.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        win.setVisible(true);
        win.pack();
        return;
    }
//open jframe that tells the user that the auto solver wasn't able to solve the tile-game
    public void youLose()
    {
        JFrame win = new JFrame("Unsolvable!");
        Font f = new Font("SansSerif", Font.BOLD, 50);
        TextField text = new TextField("Sorry buddy this one was insoluable!");
        text.setFont(f);
        text.setPreferredSize(new Dimension(300, 300));
        win.add(text);
        win.setPreferredSize(new Dimension(300, 300));
        win.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        win.setVisible(true);
        win.pack();
        return;
    }

    /*  updates Button labels and actionCommands
     *
     *
     */
    public synchronized void setButton(int[][] board) {


        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                button[i][j].setPreferredSize(new Dimension(100, 100));
                if (board[i+1][j+1] == 9) {
                    button[i][j].setText(" ");
                } else {
                    button[i][j].setText("" + board[i+1][j+1] + "");
                }
                button[i][j].setActionCommand("" + board[i+1][j+1]);

            }
        }
        setActionListeners();
    }
    /*  setActionListeners()
     *  removes anonymous actionlistners from
     *  adds actionlistener implemeted in this class
     */
    public void setActionListeners()
    {
        for(JButton but[]: button) { //but is a row of the button array
            for (JButton b : but) { //b is a button from the button array
                for (ActionListener al : b.getActionListeners()) {
                    b.removeActionListener(al);
                }
                b.addActionListener(this);
            }
        }
    }
    /*  updates Counter text Box
     *
     *
     */
    public synchronized void setCounterText(int num)
    {
        s = new String("Move Count : " + num + "");
        counterText.setText(s);
    }
/*  updates the heuristic text box
 *
 */
    public synchronized void setHeuristicText(int heuristic)
    {
        s = new String("Heuristic : " + heuristic + "");
        heuristicText.setText(s);
    }

/*  removes the button labels
 *  utilized by the customBoard
 *
 */
    private void setButtonToBlank()
    {
        for(int i = 0; i < BOARD_SIZE; i++)
        {
            for(int j = 0; j < BOARD_SIZE; j++)
            {
                button[i][j].setText(" ");
            }
        }
    }
    /* doStartNewGame()
     * allows the user to input a custom board by clicking on them
     *  sets board to all *'s to allow the user to update the board
     *
     */
    public void doStartNewGame()
    {
        setButtonToBlank();
        for(int i = 0; i < BOARD_SIZE ; i++)
        {
            for( int j = 0; j <BOARD_SIZE; j++)
            {
                button[i][j].setActionCommand("*");
                button[i][j].removeActionListener(this); //remove old actionListener
                //add new actionlistener
                button[i][j].addActionListener( new ActionListener() {

                public void actionPerformed( ActionEvent buttonMode)
                {
                    JButton b = (JButton) buttonMode.getSource();
                    if(b.getActionCommand() == "*")
                    {
                        counter++;
                    if( counter != 0) {
                            b.setText("" + counter);
                            b.setActionCommand("" + counter);
                        } else {
                            b.setText(" ");
                            b.setActionCommand("9");
                        } if(counter == 8)
                        {
                            setActionListeners();
                            counter = -1;
                            //create 2-d array representing input board to be passed to new game function
                            setNewGameBoard();
                            game = new TilesDriver(newGameBoard);
                            customMode = false;
                        }
                    }

                }
            });

            }
        }

    }
/*      updates board after user creates a custom board
 *
 *
 */
    private void setNewGameBoard()
    {
        newGameBoard = new int[4][4];
        for(int i = 0; i < BOARD_SIZE; i++)
        {
            for(int j = 0; j < BOARD_SIZE; j++)
            {
                newGameBoard[i+1][j+1] = Integer.parseInt(button[i][j].getActionCommand());
            }
        }
    }
    //used to update count whent he user is solving the puzzle
    public void setSolveCount(int count)
    {
        moveCount = count;
    }
    public int getCounter()
    {
        return moveCount;
    } //returns move counter


    /*  implementation of action performed, utilized by the array of buttons
     *
     *
     */

    @Override
    public void actionPerformed(ActionEvent evt)
    {
        if(game.makeMove(evt.getActionCommand()))
        {
            moveCount++; //incrememnet moveCount
        }
        else
        { //create a jframe that notifies the user that they have made an invalid move;
            JFrame invalidMove = new JFrame("invalid move");
            Font f = new Font("SansSerif", Font.BOLD, 25);
            TextField text = new TextField("Invalid Move");
            text.setFont(f);
            text.setPreferredSize(new Dimension(300, 300));
            invalidMove.add(text);
            invalidMove.setPreferredSize(new Dimension(300, 300));
            invalidMove.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            invalidMove.setVisible(true);
            invalidMove.pack();
        }

        if(game.getHeuristic() == 0) //ccreates a jframe that notifies the user they have won the game
        {
            JFrame win = new JFrame("Winner!");
            Font f = new Font("SansSerif", Font.BOLD, 50);
            TextField text = new TextField("you Win!");
            text.setFont(f);
            text.setPreferredSize(new Dimension(300, 300));
            win.add(text);
            win.setPreferredSize(new Dimension(300, 300));
            win.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            win.setVisible(true);
            win.pack();
            return;
        }
    }
}
