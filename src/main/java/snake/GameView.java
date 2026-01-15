package snake;

import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.event.ActionEvent;

public class GameView extends JFrame implements ActionListener {

    private GameController controller;
    private SnakePanel panel;

    // timeren bruges til at atyre spillets tick
    private Timer timer;

    // starthastigheden for slangen
    private final int DELAY = 100;

    public GameView(int n, int m) {
        GameModel model = new GameModel(n, m);
        panel = new SnakePanel(model);
        

        this.add(panel);

        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);


        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        // fokus efter vinduet er synligt
        panel.requestFocusInWindow();

        timer = new Timer(DELAY, this);        

        // Opret controller EFTER timeren findes
        controller = new GameController(model, panel, timer);
        panel.addKeyListener(controller);

        timer.start();

    }

    // flytter slangen og bruger repaint til at opdatere visdningen
    @Override
    public void actionPerformed(ActionEvent e) {
        controller.move(); 
        this.repaint();
    }

}
