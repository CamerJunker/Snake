package snake;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;

public class GameController extends JPanel implements ActionListener{
    // Laver en GameModel object
    private GameModel GM;
    // Laver baggrundsfarven, som er mørkegrøn
    public static final Color DARK_GREEN = new Color(0,102,0);

    // Laver en GameController object
    GameController(int n, int m){
        this.GM = new GameModel(n, m);
        this.setPreferredSize(new Dimension(n*10,m*10));
        this.setBackground(DARK_GREEN);
        this.setFocusable(true);
        this.addKeyListener(new SnakeKeyAdapter());
    }

    // TO BE CONSTRUCTED
    @Override
    public void actionPerformed(ActionEvent e) {
        
    }

    // KeyAdapter for reaktion til keyboard knapper
    public class SnakeKeyAdapter extends KeyAdapter{
        
        // Reagerer på keyboard knapper for op, ned, højre og venstre.
        @Override
        public void keyPressed(KeyEvent e){
            // Initialiserer en variable for retning.
            Direction dir;
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    dir = Direction.LEFT;
                    GM.step(dir);
                case KeyEvent.VK_RIGHT:
                    dir = Direction.RIGHT;
                    GM.step(dir);
                case KeyEvent.VK_UP:
                    dir = Direction.UP;
                    GM.step(dir);
                case KeyEvent.VK_DOWN:
                    dir = Direction.DOWN;
                    GM.step(dir);
            }
        }
    }
}


