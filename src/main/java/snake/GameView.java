package snake;

import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.Timer;

import java.awt.event.ActionEvent;
import javax.swing.JButton;

public class GameView extends JFrame implements ActionListener{

    private GameController controller;
    private SnakePanel panel;

    // Timer instead of while-loop for game
    private Timer timer;

    // Delay variable for timer
    private final int DELAY = 100; // Speed of snake

    // Menu Button
    private JButton MenuButton = new JButton("...");

    private PopupMenu popupMenu = null;

    public GameView(int n, int m) {
        GameModel model = new GameModel(n, m);
        panel = new SnakePanel(model);

        // Get HUD height and CellSize
        int HUDheight = panel.getHUDheight();
        int CellSize = panel.getCellSize();

        // Add Menu Button
        this.add(MenuButton);
        // Set location and size of button
        MenuButton.setBounds(n*(CellSize-1)-4, 4, n, HUDheight-8);
        
        MenuButton.addActionListener(this);

        this.add(panel);

        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);


        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        // fokus efter vinduet er synligt
        panel.requestFocusInWindow();

        // Create a timer
        timer = new Timer(DELAY, this);        

        // Opret controller EFTER timeren findes
        controller = new GameController(model, panel, timer);
        panel.addKeyListener(controller);

        // Start timer, useful instead of a while-loop
        timer.start();

    }

    // Function to move snake repeated according to timer
    // This function is placed here, so the frame can be repainted.
    @Override
    public void actionPerformed(ActionEvent e) {        
        // Move snake
        controller.move(); 
        // Repaint the panel
        this.repaint();
        

        // If Menu Button pressed
        if (e.getSource() == MenuButton) {

            // If the popup menu isn't already made
            if (popupMenu == null) {
                // Create window
                popupMenu = new PopupMenu(panel, this);


            // If the popup menu is already made
            } else {
                // Close window
                popupMenu.dispose();
                // Set variable to null
                setMenuNull();
                panel.requestFocusInWindow();
            }
        }
    }

    // For at sætte variablen til null i PopupMenu klassen
    public void setMenuNull(){
        popupMenu = null;
    }

}
