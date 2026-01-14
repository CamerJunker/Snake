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

    // Variable to hold current direction
    private Direction currentDirection;

    // hvor meget hurtigere pr. mad
    private static final int SPEEDUP_MS = 10;    
    // maks hastighed (laveste delay)  
    private static final int MIN_DELAY_MS = 60;   

    // Initialize GameController object
    // Receive GameModel and SnakePanel object as parameters
    public GameController(GameModel model, SnakePanel panel, Timer timer) {
        // Assign GameModel object to variable
        this.model = model;

        // Assign timer parameter to variable
        this.timer = timer;

        // Get direction from GameModel
        this.currentDirection = model.getDirection();
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
            default -> currentDirection;
        };

        // Check if the key is opposite to the current direction
        if (isOpposite(next, currentDirection)) return;

        // Update current direction to the key pressed
        currentDirection = next;
    }        

    // Function to move snake
    public void move() {
        // Acceleration (NYT): mål score før step 
        int beforeScore = model.getScore();

        // Call GameModel step() function with the current direction
        model.step(currentDirection);

        // --- Acceleration (NYT): hvis score steg, så gør timer hurtigere ---
        int afterScore = model.getScore();
        if (afterScore > beforeScore) {
            int newDelay = Math.max(MIN_DELAY_MS, timer.getDelay() - SPEEDUP_MS);
            timer.setDelay(newDelay);
        
        }

        // If the game is over, change panel to 
        if (model.isGameOver()) {
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
