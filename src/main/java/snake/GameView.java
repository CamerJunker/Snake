package snake;

import java.awt.event.ActionListener;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.KeyStroke;
import javax.swing.JComponent;
import javax.swing.AbstractAction;

import java.awt.event.ActionEvent;
import javax.swing.JButton;

public class GameView extends JFrame implements ActionListener {

    private GameController controller;
    private SnakePanel panel;
    private GameModel model;
    private JLabel scoreLabel;
    private JLabel timeLabel;
    private JLabel stateLabel;
    private JLabel speedLabel;
    private JLabel maxSpeedLabel;
    private JLabel sizeLabel;
    private JLabel difficultyLabel;
    private JLabel optionsLabel;
    private Difficulty currentDifficulty = Difficulty.NORMAL;
    private int lastScore = 0;
    private long scoreFlashUntilMs = 0;
    private JPanel root;
    private JPanel boardHolder;

    private static final Color BG_DARK = new Color(18, 18, 18);
    private static final Color BG_PANEL = new Color(22, 22, 22);
    private static final Color BORDER_DARK = new Color(40, 40, 40);
    private static final Color TEXT_PRIMARY = new Color(230, 230, 230);
    private static final Color TEXT_SECONDARY = new Color(170, 170, 170);
    private static final Color ACCENT = new Color(60, 180, 120);

    // timeren bruges til at atyre spillets tick
    private Timer timer;
    private Timer renderTimer;

    // starthastigheden for slangen
    private final int DELAY = 100;
    private final int RENDER_DELAY = 16;

    // Menu Button
    private JButton MenuButton = new JButton("Menu(ESC)");

    private PopupMenu popupMenu;
    private boolean fullscreen = true;
    private Rectangle windowedBounds;

    public GameView(int n, int m) {
        model = new GameModel(n, m);
        Rectangle usableBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        int maxBoardWidth = Math.max(200, usableBounds.width - 60);
        int maxBoardHeight = Math.max(200, usableBounds.height - 120);
        panel = new SnakePanel(model, maxBoardWidth, maxBoardHeight);
        panel.setLayout(null);

        MenuButton.setToolTipText("åbn menuen");
        MenuButton.addActionListener(this);

        rebuildLayout();

        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setUndecorated(true);
        this.setResizable(false);

        this.setBounds(usableBounds);
        this.setVisible(true);

        setupFullscreenToggle(usableBounds);

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
        renderTimer = new Timer(RENDER_DELAY, e -> {
            updateInfoPanel();
            panel.repaint();
        });
        renderTimer.start();

    }

    // flytter slangen og bruger repaint til at opdatere visdningen
    @Override
    public void actionPerformed(ActionEvent e) {        
        // Move snake
        controller.move(); 
        panel.repaint();
        updateInfoPanel();
        
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
        if (difficulty != null) {
            currentDifficulty = difficulty;
        }
        updateInfoPanel();
    }

    public void setWallsEnabled(boolean enabled) {
        controller.setWallsEnabled(enabled);
        updateInfoPanel();
    }

    public void setWormholesEnabled(boolean enabled) {
        controller.setWormholesEnabled(enabled);
        updateInfoPanel();
    }

    public boolean isWallsEnabled() {
        return controller.getModel().isWallsEnabled();
    }

    public boolean isWormholesEnabled() {
        return controller.getModel().isWormholesEnabled();
    }

    private JPanel buildTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BG_DARK);
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_DARK));
        topPanel.setPreferredSize(new Dimension(0, 60));

        Font titleFont = new Font("Trebuchet MS", Font.BOLD, 18);
        Font textFont = new Font("Trebuchet MS", Font.PLAIN, 13);

        JLabel title = new JLabel("SNAKE");
        title.setFont(titleFont);
        title.setForeground(TEXT_PRIMARY);
        title.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));

        JPanel topStats = new JPanel();
        topStats.setBackground(BG_DARK);
        topStats.setLayout(new BoxLayout(topStats, BoxLayout.X_AXIS));
        scoreLabel = createInfoLabel("Score: 0", textFont);
        timeLabel = createInfoLabel("Time: 0s", textFont);
        stateLabel = createBadgeLabel("PLAYING", textFont);
        topStats.add(scoreLabel);
        topStats.add(timeLabel);
        topStats.add(stateLabel);

        topPanel.add(title, BorderLayout.WEST);
        topPanel.add(topStats, BorderLayout.EAST);
        return topPanel;
    }

    private void rebuildLayout() {
        root = new JPanel(new BorderLayout());
        root.setBackground(BG_DARK);

        boardHolder = new JPanel(new GridBagLayout());
        boardHolder.setBackground(BG_DARK);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.setBorder(BorderFactory.createLineBorder(new Color(5, 5, 5), 2));
        boardHolder.add(panel, gbc);

        JPanel topPanel = buildTopPanel();
        JPanel leftPanel = buildLeftPanel();
        JPanel rightPanel = buildRightPanel();
        JPanel bottomPanel = buildBottomPanel();

        root.add(boardHolder, BorderLayout.CENTER);
        root.add(topPanel, BorderLayout.NORTH);
        root.add(leftPanel, BorderLayout.WEST);
        root.add(rightPanel, BorderLayout.EAST);
        root.add(bottomPanel, BorderLayout.SOUTH);
        this.setContentPane(root);
        root.revalidate();
        root.repaint();
    }

    private JPanel buildLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(BG_PANEL);
        leftPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER_DARK));
        leftPanel.setPreferredSize(new Dimension(220, 0));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        Font headerFont = new Font("Trebuchet MS", Font.BOLD, 14);
        Font textFont = new Font("Trebuchet MS", Font.PLAIN, 13);

        JLabel title = new JLabel("CONTROLS");
        title.setFont(headerFont);
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(12, 0, 6, 0));

        leftPanel.add(title);
        leftPanel.add(createSmallLabel("Arrow keys to move", textFont));
        leftPanel.add(createSmallLabel("ESC to open menu", textFont));
        leftPanel.add(createSmallLabel("Use menu for options", textFont));
        leftPanel.add(createDivider());
        leftPanel.add(createInfoLabel("Options:", textFont));
        optionsLabel = createInfoLabel("Options: None", textFont);
        leftPanel.add(optionsLabel);

        return leftPanel;
    }

    private JPanel buildRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(BG_PANEL);
        rightPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, BORDER_DARK));
        rightPanel.setPreferredSize(new Dimension(240, 0));
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        Font headerFont = new Font("Trebuchet MS", Font.BOLD, 14);
        Font textFont = new Font("Trebuchet MS", Font.PLAIN, 13);

        JLabel title = new JLabel("STATS");
        title.setFont(headerFont);
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(12, 0, 6, 0));

        speedLabel = createInfoLabel("Speed: 100 ms", textFont);
        maxSpeedLabel = createInfoLabel("Max Speed: 50 ms", textFont);
        sizeLabel = createInfoLabel("Board: " + model.getRows() + " x " + model.getCols(), textFont);
        difficultyLabel = createInfoLabel("Difficulty: " + currentDifficulty, textFont);

        rightPanel.add(title);
        rightPanel.add(speedLabel);
        rightPanel.add(maxSpeedLabel);
        rightPanel.add(sizeLabel);
        rightPanel.add(difficultyLabel);

        return rightPanel;
    }

    private JPanel buildBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(BG_DARK);
        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_DARK));
        bottomPanel.setPreferredSize(new Dimension(0, 64));

        Font textFont = new Font("Trebuchet MS", Font.PLAIN, 13);
        JLabel hint = new JLabel("Tip: Use the menu to change size, difficulty, and options.");
        hint.setFont(textFont);
        hint.setForeground(TEXT_SECONDARY);
        hint.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));

        JPanel buttonRow = createButtonRow();
        bottomPanel.add(hint, BorderLayout.WEST);
        bottomPanel.add(buttonRow, BorderLayout.EAST);
        return bottomPanel;
    }

    private JLabel createInfoLabel(String text, Font font) {
        JLabel label = new JLabel(text, SwingConstants.LEFT);
        label.setFont(font);
        label.setForeground(TEXT_PRIMARY);
        label.setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));
        label.setAlignmentX(LEFT_ALIGNMENT);
        return label;
    }

    private JLabel createSmallLabel(String text, Font font) {
        JLabel label = new JLabel(text, SwingConstants.LEFT);
        label.setFont(font);
        label.setForeground(TEXT_SECONDARY);
        label.setBorder(BorderFactory.createEmptyBorder(2, 18, 2, 10));
        label.setAlignmentX(LEFT_ALIGNMENT);
        return label;
    }

    private JPanel createDivider() {
        JPanel divider = new JPanel();
        divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        divider.setPreferredSize(new Dimension(Integer.MAX_VALUE, 1));
        divider.setBackground(new Color(45, 45, 45));
        divider.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        return divider;
    }

    private void updateInfoPanel() {
        if (scoreLabel == null) return;
        int score = model.getScore();
        if (score > lastScore) {
            lastScore = score;
            scoreFlashUntilMs = System.currentTimeMillis() + 350;
        }
        scoreLabel.setText("Score: " + score);
        timeLabel.setText("Time: " + model.getElapsedSeconds() + "s");
        updateStateBadge();
        speedLabel.setText("Speed: " + model.getStepDelayMs() + " ms");
        maxSpeedLabel.setText("Max Speed: " + controller.getMinDelayMs() + " ms");
        sizeLabel.setText("Board: " + model.getRows() + " x " + model.getCols());
        difficultyLabel.setText("Difficulty: " + currentDifficulty);
        if (System.currentTimeMillis() < scoreFlashUntilMs) {
            scoreLabel.setForeground(ACCENT);
        } else {
            scoreLabel.setForeground(TEXT_PRIMARY);
        }
        String options = "Options: ";
        boolean walls = model.isWallsEnabled();
        boolean wormholes = model.isWormholesEnabled();
        if (!walls && !wormholes) {
            options += "None";
        } else {
            if (walls) options += "Walls";
            if (wormholes) options += (walls ? " + " : "") + "Wormholes";
        }
        optionsLabel.setText(options);
    }

    private void updateStateBadge() {
        GameState state = model.getState();
        String label;
        Color bg;
        switch (state) {
            case PLAYING -> {
                label = "PLAYING";
                bg = new Color(60, 140, 90);
            }
            case PAUSED -> {
                label = "PAUSED";
                bg = new Color(170, 140, 60);
            }
            case GAME_OVER -> {
                label = "GAME OVER";
                bg = new Color(170, 70, 70);
            }
            case WON -> {
                label = "YOU WIN";
                bg = new Color(70, 140, 180);
            }
            default -> {
                label = state.toString();
                bg = new Color(90, 90, 90);
            }
        }
        stateLabel.setText(label);
        stateLabel.setBackground(bg);
    }

    private JPanel createButtonRow() {
        JPanel row = new JPanel();
        row.setBackground(BG_DARK);
        row.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        row.setLayout(new BorderLayout());
        MenuButton.setFocusPainted(false);
        MenuButton.setBackground(ACCENT);
        MenuButton.setForeground(Color.WHITE);
        MenuButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        row.add(MenuButton, BorderLayout.CENTER);
        return row;
    }

    private JLabel createBadgeLabel(String text, Font font) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(font);
        label.setOpaque(true);
        label.setForeground(Color.WHITE);
        label.setBackground(new Color(60, 140, 90));
        label.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        label.setAlignmentX(LEFT_ALIGNMENT);
        return label;
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

    private void setupFullscreenToggle(Rectangle fullscreenBounds) {
        windowedBounds = new Rectangle(
            fullscreenBounds.x + 80,
            fullscreenBounds.y + 60,
            Math.max(800, fullscreenBounds.width - 160),
            Math.max(600, fullscreenBounds.height - 120)
        );

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke("F11"), "toggleFullscreen");
        getRootPane().getActionMap().put("toggleFullscreen", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleFullscreen(fullscreenBounds);
            }
        });
    }

    private void toggleFullscreen(Rectangle fullscreenBounds) {
        fullscreen = !fullscreen;
    
        // Pause the game during transition
        boolean wasPlaying = (model.getState() == GameState.PLAYING);
        if (wasPlaying) {
            controller.pause();
        }
    
        // Remove old panel
        panel.removeKeyListener(controller);
    
        // Update window bounds
        if (fullscreen) {
            setBounds(fullscreenBounds);
        } else {
            setBounds(windowedBounds);
        }
    
        // Calculate new max dimensions based on current window size
        Rectangle currentBounds = getBounds();
        int maxBoardWidth = Math.max(200, currentBounds.width - 60);
        int maxBoardHeight = Math.max(200, currentBounds.height - 120);
    
        // Recreate panel with new dimensions
        panel = new SnakePanel(model, maxBoardWidth, maxBoardHeight);
        panel.setLayout(null);
        panel.addKeyListener(controller);
    
        // Rebuild entire layout with new panel
        rebuildLayout();
        setContentPane(root);
    
        // Revalidate and repaint
        revalidate();
        repaint();
        panel.requestFocusInWindow();
    
        // Resume if it was playing
        if (wasPlaying) {
            controller.unPause();
        }
    }
}
