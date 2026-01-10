package snake;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener{

    GamePanel(){

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }

    public class SnakeKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
        
        }
    }
}

