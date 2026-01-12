package snake;

import javax.swing.JFrame;

public class GameView extends JFrame {

    public GameView(int n, int m) {
        GameModel model = new GameModel(n, m);
        SnakePanel panel = new SnakePanel(model);
        GameController controller = new GameController(model, panel);

        this.add(panel);

        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        panel.addKeyListener(controller);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        // fokus efter vinduet er synligt
        panel.requestFocusInWindow();
    }
}
