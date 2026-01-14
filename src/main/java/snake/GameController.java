package snake;

import javax.swing.Timer;
import java.awt.event.KeyAdapter; // React to keys
import java.awt.event.KeyEvent; // Create key event

public class GameController extends KeyAdapter {
    // GameModel object
    private GameModel model;

    // SnakePanel object
    // private SnakePanel Spanel;
    private Timer timer;

    // Latest requested direction (applied on the next tick)
    private Direction requestedDirection;

    // Initialize GameController object
    // Receive GameModel and SnakePanel object as parameters
    public GameController(GameModel model, SnakePanel panel, Timer timer) {
        // Assign GameModel object to variable
        this.model = model;

        // Assign timer parameter to variable
        this.timer = timer;

        // Start with no pending input; model direction drives default movement
        this.requestedDirection = null;
    }

    // Every time one of the arrow keys are pressed, the direction variable is changed
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
            default -> (requestedDirection != null ? requestedDirection : model.getDirection());
        };

        // Check if the key is opposite to the current (or pending) direction
        Direction base = (requestedDirection != null) ? requestedDirection : model.getDirection();
        if (isOpposite(next, base)) return;

        // Store direction request to be applied on the next move tick
        requestedDirection = next;
    }        

    // Function to move snake
    public void move() {
        // Call GameModel step() function with the pending direction
        model.step(requestedDirection);
        requestedDirection = null;

        // Check if the game is over
        Boolean isGameOver = model.isGameOver();

        // If the game is over, change panel to 
        if (isGameOver) {
            // Stop timer
            timer.stop();
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

}
