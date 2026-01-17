package snake;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class GameView extends JFrame {

    public GameView(SnakePanel panel, GameController controller) {
        this.add(panel);

        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        panel.addKeyListener(controller);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        // fokus efter vinduet er synligt
        SwingUtilities.invokeLater(panel::requestFocusInWindow);
    }
}
