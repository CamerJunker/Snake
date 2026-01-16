package snake;

import javax.swing.Timer;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameController extends KeyAdapter {
    private GameModel model;
    private Timer timer;
    private int baseDelayMs;
    private GameView view;
    private final SoundManager soundManager = new SoundManager();

    // det seneste input, som så bliver brugt ved næste tick
    private Direction requestedDirection;

    // Acceleration: gør spillet hurtigere når man spiser 
    private int lastScore = 0;                 // husker sidste score
    private int speedupMs = 2;   // hvor meget hurtigere slangen bliver hver gang mad bliver spist
    private static final int MIN_DELAY_MS = 50; // laveste delay (max hastighed som slangen kan opnå)
 

    // det her binder model og timer sammen
    public GameController(GameModel model, SnakePanel panel, Timer timer, GameView view) {
        this.model = model;
        this.timer = timer;
        this.baseDelayMs = timer.getDelay();
        this.requestedDirection = null;
        this.view = view;
        soundManager.load("eat", "/sounds/eat.wav");
        soundManager.load("gameover", "/sounds/gameover.wav");
    }

    // reagerer på piletaster og gemmer den gyldige retning
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            view.toggleMenu();
            return;
        }

        if (model.isGameOver()) return;

        Direction next = switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> Direction.UP;
            case KeyEvent.VK_DOWN -> Direction.DOWN;
            case KeyEvent.VK_LEFT -> Direction.LEFT;
            case KeyEvent.VK_RIGHT -> Direction.RIGHT;
            default -> (requestedDirection != null ? requestedDirection : model.getDirection());
        };

        //forhindrer en 180 graders vending
        Direction base = (requestedDirection != null) ? requestedDirection : model.getDirection();
        if (next == base) return;
        if (next == base.opposite()) return;

        requestedDirection = next;
    }        

    // flytter slangen og justerer hastigheden ved tick
    public void move() {
        if (model.isGameOver()) {
            timer.stop();
            return;
        }

        boolean wasGameOver = model.isGameOver();
        model.step(requestedDirection);
        requestedDirection = null;

        // Hvis vi har spist en prik, så øg hastigheden én gang
        int scoreNow = model.getScore();
        if (scoreNow > lastScore) {
            lastScore = scoreNow;
            soundManager.play("eat");

            int newDelay = Math.max(MIN_DELAY_MS, timer.getDelay() - speedupMs);
            timer.setDelay(newDelay);  
        }

        if (!wasGameOver && model.isGameOver()) {
            soundManager.play("gameover");
        }

        if (model.isGameOver()) {
            timer.stop();
        }
    }

    // Pause Game
    public void pause() {
        model.pause();
        timer.stop();
    }

    // Unpause Game
    public void unPause() {
        model.resume();
        timer.start();
    }

    // Restart game and reset speed
    public void resetGame() {
        model.reset();
        requestedDirection = null;
        lastScore = 0;
        timer.setDelay(baseDelayMs);
        timer.start();
    }

    public void setDifficulty(Difficulty difficulty) {
        if (difficulty == null) return;
        baseDelayMs = difficulty.getBaseDelayMs();
        speedupMs = difficulty.getSpeedupMs();
        lastScore = model.getScore();
        timer.setDelay(baseDelayMs);
    }

}
