package snake;

import javax.swing.JFrame;

public class GameView extends JFrame {
    
    GameView(int n, int m){
        this.add(new GameController(n, m));
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

}