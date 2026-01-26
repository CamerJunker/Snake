package snake;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class SnakePanel extends JPanel {
    // Laver en GameModel object
    private final GameModel model;
    // Laver baggrundsfarven, som er mørkegrøn
    public static final Color DARK_GREEN = new Color(40, 40, 40);

    //Celle Størrelsen
    private int CellSize;
    private static final int HUD_HEIGHT = 0;
    private static final int MIN_CELL_SIZE = 8;
    private static final int MAX_CELL_SIZE = 24;

    private int boardWidth;
    private int boardHeight;

    public SnakePanel(GameModel model, int maxBoardWidth, int maxBoardHeight) {
        this.model = model;

        int maxCellWidth = Math.max(1, maxBoardWidth / model.getCols());
        int maxCellHeight = Math.max(1, (maxBoardHeight - HUD_HEIGHT) / model.getRows());
        int fitCellSize = Math.min(maxCellWidth, maxCellHeight);
        CellSize = Math.max(MIN_CELL_SIZE, Math.min(MAX_CELL_SIZE, fitCellSize));

        boardWidth = model.getCols() * CellSize;
        boardHeight = model.getRows() * CellSize;

        this.setPreferredSize(new Dimension(boardWidth, boardHeight + HUD_HEIGHT));
        this.setBackground(DARK_GREEN);
        this.setFocusable(true);
    }

    public SnakePanel(GameModel model) {
        this(model, 400, 400);
    }

    public int getCellSize() {
        return CellSize;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Buffered Image er super vigtigt for at slangen ikke tegnes langsomt efter få sekunder!
        BufferedImage bufferedImage = new BufferedImage(boardWidth, boardHeight + HUD_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics bufferedGraphics = bufferedImage.getGraphics();

        // Alt skal helst tegnes med dette!
        Graphics2D g2d = (Graphics2D) bufferedGraphics;

        //Rydder baggrunden
        g2d.setColor(DARK_GREEN);
        g2d.fillRect(0, 0, boardWidth, boardHeight + HUD_HEIGHT);

        // tegner mad
        Cell food = model.getFood();
        if (food != null) {
            g2d.setColor(Color.ORANGE);
            g2d.fillRect(
                food.c() * CellSize,
                food.r() * CellSize + HUD_HEIGHT,
                CellSize,
                CellSize
            );
        }

        // tegner mure
        g2d.setColor(Color.BLACK);
        for (Cell wall : model.getWalls()) {
            g2d.fillRect(
                wall.c() * CellSize,
                wall.r() * CellSize + HUD_HEIGHT,
                CellSize,
                CellSize
            );
        }

        // tegner ormehuller
        g2d.setColor(Color.MAGENTA);
        for (Cell wh : model.getWormholes().keySet()) {
            g2d.fillRect(
                wh.c() * CellSize,
                wh.r() * CellSize + HUD_HEIGHT,
                CellSize,
                CellSize
            );
        }

        float alpha = getStepAlpha();
        List<Cell> currentSnake = toList(model.getSnake());
        List<Cell> previousSnake = toList(model.getPrevSnake());
        if (model.didTeleportLastStep() || hasAnyJump(currentSnake, previousSnake)) {
            // Skip interpolation on teleport/jumps to avoid drawing across the board.
            alpha = 1.0f;
            previousSnake = currentSnake;
        }

        boolean head = true;
        for (int i = 0; i < currentSnake.size(); i++) {
            Cell current = currentSnake.get(i);
            Cell previous = (i < previousSnake.size()) ? previousSnake.get(i) : current;

            if (head) {
                g2d.setColor(Color.GREEN.darker()); // hoved
                head = false;
            } else {
                g2d.setColor(Color.GREEN); // krop
            }

            float drawRow = interpolateWrapped(previous.r(), current.r(), model.getRows(), alpha);
            float drawCol = interpolateWrapped(previous.c(), current.c(), model.getCols(), alpha);
            int x = Math.round(drawCol * CellSize);
            int y = Math.round(drawRow * CellSize) + HUD_HEIGHT;

            g2d.fillRect(x, y, CellSize, CellSize);
        }

        if (model.isGameOver()) {
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRect(0, HUD_HEIGHT, boardWidth, boardHeight);

            g2d.setColor(Color.WHITE);
            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 18f));
            String gameOver = "Game Over";
            FontMetrics fm = g2d.getFontMetrics();
            int textX = (boardWidth - fm.stringWidth(gameOver)) / 2;
            int textY = HUD_HEIGHT + (boardHeight / 2) - fm.getHeight() / 2 + fm.getAscent();
            g2d.drawString(gameOver, textX, textY);
        } else if (model.getState() == GameState.PAUSED) {
            g2d.setColor(new Color(0, 0, 0, 120));
            g2d.fillRect(0, HUD_HEIGHT, boardWidth, boardHeight);

            g2d.setColor(Color.WHITE);
            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 18f));
            String paused = "Paused";
            FontMetrics fm = g2d.getFontMetrics();
            int textX = (boardWidth - fm.stringWidth(paused)) / 2;
            int textY = HUD_HEIGHT + (boardHeight / 2) - fm.getHeight() / 2 + fm.getAscent();
            g2d.drawString(paused, textX, textY);
        }

        // Her tegnes det endeligt!
        g.drawImage(bufferedImage, 0, 0, this);

    }

    // Getters for visse variabler
    public int getHUDheight() {return HUD_HEIGHT;}

    private float getStepAlpha() {
        int delayMs = model.getStepDelayMs();
        if (delayMs <= 0) return 1.0f;
        long elapsed = System.currentTimeMillis() - model.getLastStepTimeMs();
        float alpha = elapsed / (float) delayMs;
        if (alpha < 0.0f) return 0.0f;
        if (alpha > 1.0f) return 1.0f;
        return alpha;
    }

    private float interpolateWrapped(int prev, int current, int size, float t) {
        int delta = current - prev;
        if (Math.abs(delta) > 1) {
            if (delta > 0) {
                delta -= size;
            } else {
                delta += size;
            }
        }
        float value = prev + delta * t;
        if (value < 0) value += size;
        if (value >= size) value -= size;
        return value;
    }

    private boolean hasAnyJump(List<Cell> currentSnake, List<Cell> previousSnake) {
        if (currentSnake.isEmpty() || previousSnake.isEmpty()) return false;
        int count = Math.min(currentSnake.size(), previousSnake.size());
        for (int i = 0; i < count; i++) {
            Cell current = currentSnake.get(i);
            Cell previous = previousSnake.get(i);
            int dr = current.r() - previous.r();
            int dc = current.c() - previous.c();
            int wrappedDr = adjustWrappedDelta(dr, model.getRows());
            int wrappedDc = adjustWrappedDelta(dc, model.getCols());
            if (Math.abs(wrappedDr) > 1 || Math.abs(wrappedDc) > 1) {
                return true;
            }
        }
        return false;
    }

    private int adjustWrappedDelta(int delta, int size) {
        if (Math.abs(delta) > 1) {
            if (delta > 0) {
                return delta - size;
            }
            return delta + size;
        }
        return delta;
    }

    private List<Cell> toList(Iterable<Cell> cells) {
        List<Cell> list = new ArrayList<>();
        for (Cell c : cells) {
            list.add(c);
        }
        return list;
    }
}
