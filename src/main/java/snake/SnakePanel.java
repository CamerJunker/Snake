package snake;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
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

    private int columnSize;
    private int rowSize;

    public SnakePanel(GameModel model) {
        columnSize = model.getCols() * CELL_SIZE;
        rowSize = model.getRows() * CELL_SIZE;

        this.model = model;

        this.setPreferredSize(new Dimension(columnSize, rowSize));
        this.setBackground(DARK_GREEN);

        // View må ikke håndtere input, men panelet skal gerne kunne få fokus
        this.setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Buffered Image er super vigtigt for at slangen ikke tegnes langsomt efter få sekunder!
        BufferedImage bufferedImage = new BufferedImage(columnSize, rowSize, BufferedImage.TYPE_INT_ARGB);
        Graphics bufferedGraphics = bufferedImage.getGraphics();

        // Alt skal helst tegnes med dette!
        Graphics2D g2d = (Graphics2D) bufferedGraphics;

        // tegner mad
        Cell food = model.getFood();
        if (food != null) {
            g2d.setColor(Color.ORANGE);
            g2d.fillRect(
                food.c() * CELL_SIZE,
                food.r() * CELL_SIZE,
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
                c.r() * CELL_SIZE,
                CELL_SIZE,
                CELL_SIZE
            );
        }

        // Her tegnes det endeligt!
        g.drawImage(bufferedImage, 0, 0, this);

    }

}