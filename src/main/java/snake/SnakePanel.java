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
    private static final int HUD_HEIGHT = 24;
    private static final int HUD_PADDING = 6;
    private static final int MIN_BOARD_SIZE = 400;

    private int boardWidth;
    private int boardHeight;

    public SnakePanel(GameModel model) {
        this.model = model;

        int baseCellSize = 13;
        int minCellSizeForWidth = MIN_BOARD_SIZE / model.getCols();
        int minCellSizeForHeight = MIN_BOARD_SIZE / model.getRows();
        CellSize = Math.max(baseCellSize, Math.max(minCellSizeForWidth, minCellSizeForHeight));

        boardWidth = model.getCols() * CellSize;
        boardHeight = model.getRows() * CellSize;

        this.setPreferredSize(new Dimension(boardWidth, boardHeight + HUD_HEIGHT));
        this.setBackground(DARK_GREEN);
        this.setFocusable(true);
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

        // HUD-baggrunden
        g2d.setColor(new Color(30, 30, 30));
        g2d.fillRect(0, 0, boardWidth, HUD_HEIGHT);

        // HUD-teksten
        g2d.setColor(Color.WHITE);
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 12f));
        String hudText = "Score: " + model.getScore() + "   Tid: " + model.getElapsedSeconds() + "s   State: " + model.getState();
        int hudTextY = (HUD_HEIGHT + g2d.getFontMetrics().getAscent()) / 2;
        g2d.drawString(hudText, HUD_PADDING, hudTextY);

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
                wall.c() * CELL_SIZE,
                wall.r() * CELL_SIZE + HUD_HEIGHT,
                CELL_SIZE,
                CELL_SIZE
            );
        }

        // tegner ormehuller
        g2d.setColor(Color.MAGENTA);
        for (Cell wh : model.getWormholes().keySet()) {
            g2d.fillRect(
                wh.c() * CELL_SIZE,
                wh.r() * CELL_SIZE + HUD_HEIGHT,
                CELL_SIZE,
                CELL_SIZE
            );
        }

        float alpha = getStepAlpha();
        List<Cell> currentSnake = toList(model.getSnake());
        List<Cell> previousSnake = toList(model.getPrevSnake());

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

    private List<Cell> toList(Iterable<Cell> cells) {
        List<Cell> list = new ArrayList<>();
        for (Cell c : cells) {
            list.add(c);
        }
        return list;
    }
}
