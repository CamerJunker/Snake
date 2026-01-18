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
    private Timer renderTimer;

    // starthastigheden for slangen
    private final int DELAY = 100;
    private final int RENDER_DELAY = 16;

    // Menu Button
    private JButton MenuButton = new JButton("Menu(ESC)");

    private PopupMenu popupMenu;

    public GameView(int n, int m) {
        GameModel model = new GameModel(n, m);
        panel = new SnakePanel(model);
        panel.setLayout(null);

        // Get HUD height and CellSize
        int HUDheight = panel.getHUDheight();
        int CellSize = panel.getCellSize();

        // Add Menu Button
        panel.add(MenuButton);
        // Set location and size of button
        int menuWidth = MenuButton.getPreferredSize().width + 10;
        int boardWidth = n * CellSize;
        MenuButton.setBounds(boardWidth - menuWidth - 4, 2, menuWidth, HUDheight - 4);
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

        // If popup menu is open, then pause game. In the case of resizing the game in the menu.
        if (MainApp.getPopupState()){
            controller.pause();
        }

        timer.start();
        renderTimer = new Timer(RENDER_DELAY, e -> panel.repaint());
        renderTimer.start();

    }

    // flytter slangen og bruger repaint til at opdatere visdningen
    @Override
    public void actionPerformed(ActionEvent e) {        
        // Move snake
        controller.move(); 
        panel.repaint();
        
        // Hvis spiltilstanden er WON, ændres vinduets titel for at vise at spilleren har vundet
        if (controller.getModel().getState() == GameState.WON) {
        setTitle("DU VANDT! - yayyyyyy");
}
        // If Menu Button pressed
        if (e.getSource() == MenuButton) {
            toggleMenu();
        }
    }

    // For at sætte variablen til null i PopupMenu klassen
    public void ClosePopupMenu(){
        // If the popupMenu variable is not null
        if (popupMenu != null) {
            popupMenu.dispose();

        // If the popupMenu variable is null
        // If a new game window was made, the reference is in MainApp
        } else {
            popupMenu = MainApp.getRefPopupMenu();
            popupMenu.dispose();
        }
        
        // Restart GameController
        controller.unPause();

        // Change state of PopupMenu in MainApp
        MainApp.PopupStateChange(false);

        // Then return focus to panel
        panel.requestFocusInWindow();
        panel.repaint();
    }

    // For at lukke gameview fra andre klasser
    public void CloseGameView() {
        this.dispose();
    }

    // Genstart spillet uden at lave et nyt vindue
    public void restartGame() {
        controller.resetGame();
        controller.pause();
        panel.repaint();
        panel.requestFocusInWindow();
    }

    //brugt af både knap og ESC
    public void setDifficulty(Difficulty difficulty) {
        controller.setDifficulty(difficulty);
    }

    // Åbn popup menu, hvis åben, luk popup menu
    public void toggleMenu() {
        // If the popupmenu is not open
        if (!MainApp.getPopupState()) {

            // Pause game
            controller.pause();

            // Create new popupmenu
            popupMenu = new PopupMenu(this);

            // Pass reference to popup menu to MainApp
            MainApp.passOnPopupMenu(popupMenu);

            // Set flag in MainApp to true
            MainApp.PopupStateChange(true);

            // Repaint panel
            panel.repaint();
        // If the popupmenu is open
        } else {
            ClosePopupMenu();
        }
    }

}
