package snake;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameController extends KeyAdapter {

    private final GameModel model;
    private final SnakePanel panel;

    public GameController(GameModel model, SnakePanel panel) {
        this.model = model;
        this.panel = panel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (model.isGameOver()) return;
        Direction next = switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> Direction.UP;
            case KeyEvent.VK_DOWN -> Direction.DOWN;
            case KeyEvent.VK_LEFT -> Direction.LEFT;
            case KeyEvent.VK_RIGHT -> Direction.RIGHT;
            default -> null;
        };

        if (next == null) return;
        Direction currentDirection = model.getDirection();
        if (isOpposite(next, currentDirection)) return;

        model.step(next);
        panel.repaint();
    }

    private boolean isOpposite(Direction a, Direction b) {
        return (a == Direction.UP && b == Direction.DOWN)
            || (a == Direction.DOWN && b == Direction.UP)
            || (a == Direction.LEFT && b == Direction.RIGHT)
            || (a == Direction.RIGHT && b == Direction.LEFT);
    }
}
