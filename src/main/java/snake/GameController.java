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

    // Acceleration: gør spillet hurtigere når man spiser 
    private int lastScore = 0;                 // husker sidste score
    private static final int SPEEDUP_MS = 5;   // hvor meget hurtigere pr. mad
    private static final int MIN_DELAY_MS = 40; // laveste delay (max hastighed)
 

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
        if (next == base) return;
        if (next == base.opposite()) return;

        // Store direction request to be applied on the next move tick
        requestedDirection = next;
    }        

    // Function to move snake
    public void move() {
        if (model.isGameOver()) {
        timer.stop();
        return;
        }

        // Call GameModel step() function with the current direction
        model.step(requestedDirection);
        // Call GameModel step() function with the pending direction
        model.step(requestedDirection);
        requestedDirection = null;

        // Hvis vi har spist en prik, så øg hastigheden én gang
        int scoreNow = model.getScore();
        if (scoreNow > lastScore) {
            lastScore = scoreNow;

        int newDelay = Math.max(MIN_DELAY_MS, timer.getDelay() - SPEEDUP_MS);
        timer.setDelay(newDelay);  
        }

        // If the game is over, change panel to 
        if (model.isGameOver()) {
            // Stop timer
            timer.stop();
        }
    }

}
