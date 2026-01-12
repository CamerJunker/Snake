package snake;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter; // React to keys
import java.awt.event.KeyEvent; // Create key event
import javax.swing.Timer;
//import java.util.Timer; // Create a timer

public class GameController extends KeyAdapter implements ActionListener{

    private final GameModel model;
    private final SnakePanel panel;
    private Boolean running = false; 
    private Timer timer;
    private final int DELAY = 75; // Speed of snake

    private Direction currentDirection;

    public GameController(GameModel model, SnakePanel panel) {
        this.model = model;
        this.panel = panel;

        // Make the game start running
        running = true;

        // Get direction from GameModel
        this.currentDirection = model.getDirection();

        // Create a timer
        timer = new Timer(DELAY,this);

        // Start timer, useful instead of a while-loop
        timer.start();
    }

    // Update the current direction to key pressed
    @Override
    public void keyPressed(KeyEvent e) {
        // Return this function if the game is over
        if (model.isGameOver()) return;

        // For each arrow key, the direction variable is changed.
        Direction next = switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> Direction.UP;
            case KeyEvent.VK_DOWN -> Direction.DOWN;
            case KeyEvent.VK_LEFT -> Direction.LEFT;
            case KeyEvent.VK_RIGHT -> Direction.RIGHT;

            // The default is always the current direction
            default -> this.currentDirection;
        };

        // Check if the key is opposite to the current direction
        if (isOpposite(next, currentDirection)) return;

        // Update current direction to the key pressed
        this.currentDirection = next;
    }

    // Function to move snake and repaint canvas
    public void move() {
        if(running) {

            // Call GameModel step() function with the current direction
            model.step(currentDirection);

            // Repaint the panel
            panel.repaint();

            // Check if the game is over
            Boolean isGameOver = model.isGameOver();

            // If the game is over, change panel to 
            if (isGameOver) {
                // Change running to false
                running = false;
            }
        }
    }

    // Function to check if current direction is the opposite
    private boolean isOpposite(Direction a, Direction b) {
        // Returns true if direction a is the opposite to direction b
        return (a == Direction.UP && b == Direction.DOWN)
            || (a == Direction.DOWN && b == Direction.UP)
            || (a == Direction.LEFT && b == Direction.RIGHT)
            || (a == Direction.RIGHT && b == Direction.LEFT);
    }

    // Function running according to timer
    @Override
    public void actionPerformed(ActionEvent e) {

        // If game is still running, move snake
        if(running) {
            move();
        } else {
            // Stop timer
            timer.stop();
        }
    }
}
