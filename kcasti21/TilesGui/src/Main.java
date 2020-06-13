/*  Kevin Castilla
 *  kcasti21
 *  program 4
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * main:
 * Creates gui and timer thread
 */
public class Main {
    public static void main( String[] args){
        SwingUtilities.invokeLater(new Runnable() {
Timer t;
            int counter;
            Thread timeThread;
            @Override
            public void run() {
               Controller c = new Controller(); //gui
               timeThread = new Thread(){ //timer to update board
                    public void run()
                    {
                       t = new Timer(300,  new ActionListener() { //do something every 300 ms
                           public void actionPerformed(ActionEvent evt) { //updates heuristic, counter and buttons
                               if (! c.customMode)
                               {
                                   c.setHeuristicText(c.game.getHeuristic());
                               if (c.solveMode) {
                                   counter = c.game.getSolverCount();
                                   c.setSolveCount(counter);
                               } else {
                                   counter = c.getCounter();
                               }
                               c.setCounterText(c.getCounter());
                               c.setButton(c.game.getBoard());
                               if (c.game.getHeuristic() == 0 || counter == 181422) {
                                   c.solveMode = false;
                                   t.stop();
                                   if (c.game.getHeuristic() == 0) {

                                       c.setButton(c.game.getBoard());
                                       System.out.println("best");
                                       c.youWin();
                                   } else {
                                       c.setButton(c.game.board.best);
                                       c.setHeuristicText(c.game.board.generateHeuristic(c.game.board.best));
                                       c.youLose();
                                   }
                                   c.setCounterText(c.getCounter());

                                   timeThread.interrupt();

                               }
                           }
                       }
                        });
                        t.start(); //start timer
                    }

                };

                timeThread.start(); //start thread

            }
        });
    }
}
