package snake;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class SnakePanel extends JPanel {
    // Laver en GameModel object
    private final GameModel model;
    // Laver baggrundsfarven, som er mørkegrøn
    public static final Color DARK_GREEN = new Color(40, 40, 40);

    //Celle Størrelsen
    private static final int CELL_SIZE = 10;
    private static final int HUD_HEIGHT = 24;
    private static final int HUD_PADDING = 6;

    private int boardWidth;
    private int boardHeight;

    public SnakePanel(GameModel model) {
        boardWidth = model.getCols() * CELL_SIZE;
        boardHeight = model.getRows() * CELL_SIZE;

        this.model = model;

        this.setPreferredSize(new Dimension(boardWidth, boardHeight + HUD_HEIGHT));
        this.setBackground(DARK_GREEN);

        // View må ikke håndtere input, men panelet skal gerne kunne få fokus
        this.setFocusable(true);
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
                food.c() * CELL_SIZE,
                food.r() * CELL_SIZE + HUD_HEIGHT,
                CELL_SIZE,
                CELL_SIZE
            );
        }

        // tegner slangen
        boolean head = true;
        for (Cell c : model.getSnake()) {
            if (head) {
                g2d.setColor(Color.GREEN.darker()); // hoved
                head = false;
            } else {
                g2d.setColor(Color.GREEN); // krop
            }

            g2d.fillRect(
                c.c() * CELL_SIZE,
                c.r() * CELL_SIZE + HUD_HEIGHT,
                CELL_SIZE,
                CELL_SIZE
            );
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
    public int getCellSize() {return CELL_SIZE;}
    public int getHUDheight() {return HUD_HEIGHT;}
}
