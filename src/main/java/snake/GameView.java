package snake;

import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
//import javax.swing.Timer; // Create a timer

public class GameView extends JFrame implements ActionListener{

    private GameController controller;
    private SnakePanel panel;

    // Timer instead of while-loop for game
    private Timer timer;

    // Delay variable for timer
    private final int DELAY = 100; // Speed of snake

    public GameView(int n, int m) {
        GameModel model = new GameModel(n, m);
        panel = new SnakePanel(model);
        controller = new GameController(model, panel, timer);

        this.add(panel);

        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        panel.addKeyListener(controller); 

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        // fokus efter vinduet er synligt
        panel.requestFocusInWindow();

        // Create a timer
        timer = new Timer(DELAY,this);        

        // Start timer, useful instead of a while-loop
        timer.start();

    }

    // Function to move snake repeated according to timer
    @Override
    public void actionPerformed(ActionEvent e) {
        // Move snake
        controller.move(); 
        // Repaint the panel
        this.repaint();
    }

}
