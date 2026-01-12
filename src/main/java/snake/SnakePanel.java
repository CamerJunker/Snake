package snake;


import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class SnakePanel extends JPanel {
    // Laver en GameModel object
    private final GameModel model;
    // Laver baggrundsfarven, som er mørkegrøn
    public static final Color DARK_GREEN = new Color(0,102,0);

    //Celle Størrelsen
    private static final int CELL_SIZE = 10;

     public SnakePanel(GameModel model) {
        this.model = model;

        this.setPreferredSize(new Dimension(model.getCols() * CELL_SIZE, model.getRows() * CELL_SIZE));
        this.setBackground(DARK_GREEN);

        // View må ikke håndtere input, men panelet skal gerne kunne få fokus
        this.setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // tegner mad
        Cell food = model.getFood();
        if (food != null) {
            g.setColor(Color.ORANGE);
            g.fillRect(
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
                g.setColor(Color.GREEN.darker()); // hoved
                head = false;
            } else {
                g.setColor(Color.GREEN); // krop
            }

            g.fillRect(
                c.c() * CELL_SIZE,
                c.r() * CELL_SIZE,
                CELL_SIZE,
                CELL_SIZE
            );
        }
    }

}