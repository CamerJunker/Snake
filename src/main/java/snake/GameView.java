package snake;

import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.Timer;

import java.awt.event.ActionEvent;
import javax.swing.JButton;

public class GameView extends JFrame implements ActionListener {

    private GameController controller;
    private SnakePanel panel;

    // timeren bruges til at atyre spillets tick
    private Timer timer;

    // starthastigheden for slangen
    private final int DELAY = 100;

    // Menu Button
    private JButton MenuButton = new JButton("Menu(ESC)");

    private PopupMenu popupMenu = null;

    private MainApp mainApp;

    public GameView(int n, int m, MainApp main) {
        mainApp = main;

        GameModel model = new GameModel(n, m);
        panel = new SnakePanel(model);

        // Get HUD height and CellSize
        int HUDheight = panel.getHUDheight();
        int CellSize = panel.getCellSize();

        // Add Menu Button
        this.add(MenuButton);
        // Set location and size of button
        int menuWidth = MenuButton.getPreferredSize().width + 10;
        MenuButton.setBounds(n*(CellSize-1)-menuWidth-4, 2, menuWidth, HUDheight-4);
        MenuButton.setToolTipText("åbn menuen");
        
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

        timer = new Timer(DELAY, this);        

        // Opret controller EFTER timeren findes
        controller = new GameController(model, panel, timer, this);
        panel.addKeyListener(controller);

        timer.start();

    }

    // flytter slangen og bruger repaint til at opdatere visdningen
    @Override
    public void actionPerformed(ActionEvent e) {        
        // Move snake
        controller.move(); 
        this.repaint();
        

        // If Menu Button pressed
        if (e.getSource() == MenuButton) {
            toggleMenu();
        }
    }

    // For at sætte variablen til null i PopupMenu klassen
    public void ClosePopupMenu(){
        // First dispose of window
        popupMenu.dispose();

        // Restart GameController
        controller.unPause();

        // Then set variable to null
        popupMenu = null;

        // Then return focus to panel
        panel.requestFocusInWindow();
    }

    // For at lukke gameview fra andre klasser
    public void CloseGameView() {
        this.dispose();
    }

    // Genstart spillet uden at lave et nyt vindue
    public void restartGame() {
        controller.resetGame();
        this.repaint();
        panel.requestFocusInWindow();
    }

    //brugt af både knap og ESC
    public void toggleMenu() {
        if (popupMenu == null) {
            controller.pause();
            popupMenu = new PopupMenu(this, mainApp);
        } else {
            ClosePopupMenu();
        }
    }

}
