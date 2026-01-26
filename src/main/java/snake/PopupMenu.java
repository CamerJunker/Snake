package snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;

public class PopupMenu extends JFrame implements ActionListener{
    JLabel titleLabel = new JLabel("SNAKE MENU");
    JLabel ChangeGameSizelabel = new JLabel("Board Size");
    JLabel rowLabel = new JLabel("Rows (5-100)");
    JLabel colLabel = new JLabel("Columns (5-100)");
    JLabel difficultyLabel = new JLabel("Difficulty");
    JLabel optionsLabel = new JLabel("Options");

    JTextField rowEntry = new  JTextField();
    JTextField colEntry = new  JTextField();
    JComboBox<Difficulty> difficultyBox = new JComboBox<>(Difficulty.values());
    JCheckBox wallsBox = new JCheckBox("Mure");
    JCheckBox wormholesBox = new JCheckBox("Ormehuller");

    GameView gview;

    JButton ExitButton = new JButton("Exit to Game");

    JButton ChangeGridSize = new JButton("Apply Size");
    JButton ApplyDifficulty = new JButton("Apply Difficulty");
    JButton ApplyOptions = new JButton("Apply Options");
    JButton RestartButton = new JButton("Restart Game");

    JPanel mainPanel = new JPanel();
    JPanel sizePanel = new JPanel();
    JPanel difficultyPanel = new JPanel();
    JPanel optionsPanel = new JPanel();
    JPanel actionsPanel = new JPanel();

    private int TextSize = 15;
    private int spacing = 10;
    private int windowWidth = 420;
    private int windowHeight = 520;

    PopupMenu(GameView gameview) {
        // 
        MainApp.PopupStateChange(true);

        // Create window
        gview = gameview;
        this.setSize(windowWidth, windowHeight);
        this.setLocationRelativeTo(gview);
        this.setVisible(true);
        this.setResizable(false);

        Font titleFont = new Font("Impact", Font.PLAIN, 28);
        Font headerFont = new Font("Trebuchet MS", Font.BOLD, 14);
        Font textFont = new Font("Trebuchet MS", Font.PLAIN, TextSize);
        Font buttonFont = new Font("Trebuchet MS", Font.BOLD, TextSize);

        ExitButton.setFont(buttonFont);
        ChangeGameSizelabel.setFont(headerFont);
        rowEntry.setFont(textFont);
        colEntry.setFont(textFont);
        ChangeGridSize.setFont(buttonFont);
        ApplyDifficulty.setFont(buttonFont);
        RestartButton.setFont(buttonFont);
        rowLabel.setFont(textFont);
        colLabel.setFont(textFont);
        difficultyLabel.setFont(headerFont);
        optionsLabel.setFont(headerFont);
        difficultyBox.setFont(textFont);
        wallsBox.setFont(textFont);
        wormholesBox.setFont(textFont);
        ApplyOptions.setFont(buttonFont);

        titleLabel.setFont(titleFont);
        titleLabel.setForeground(new Color(240, 240, 240));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        rowEntry.setToolTipText("Antal rækker mellem 5 og 100");
        colEntry.setToolTipText("Antal kolonner mellem 5 og 100");
        difficultyBox.setToolTipText("Vælg base-hastighed og acceleration");
        wallsBox.setToolTipText("Tilføj tilfældige mure");
        wormholesBox.setToolTipText("Tilføj ormehuller");

        difficultyBox.setSelectedItem(Difficulty.NORMAL);
        wallsBox.setSelected(gview.isWallsEnabled());
        wormholesBox.setSelected(gview.isWormholesEnabled());

        this.setTitle("Snake Menu");
        this.getContentPane().setBackground(new Color(24, 24, 24));

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(24, 24, 24));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        sizePanel.setLayout(new GridBagLayout());
        sizePanel.setBackground(new Color(34, 34, 34));
        sizePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 60)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        difficultyPanel.setLayout(new GridBagLayout());
        difficultyPanel.setBackground(new Color(34, 34, 34));
        difficultyPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 60)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        optionsPanel.setLayout(new GridBagLayout());
        optionsPanel.setBackground(new Color(34, 34, 34));
        optionsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 60)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        actionsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, spacing, spacing));
        actionsPanel.setBackground(new Color(24, 24, 24));

        // Add actionlisteners to buttons
        ChangeGridSize.addActionListener(this);
        ApplyDifficulty.addActionListener(this);
        ApplyOptions.addActionListener(this);
        ExitButton.addActionListener(this);
        RestartButton.addActionListener(this);

        // Add objects to panels
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 0.5;
        sizePanel.add(rowLabel, gbc);
        gbc.gridx = 1;
        sizePanel.add(colLabel, gbc);
        gbc.gridy = 1;
        gbc.gridx = 0;
        sizePanel.add(rowEntry, gbc);
        gbc.gridx = 1;
        sizePanel.add(colEntry, gbc);
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        sizePanel.add(ChangeGridSize, gbc);
        gbc.gridwidth = 1;

        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        difficultyPanel.add(difficultyLabel, gbc);
        gbc.gridy = 1;
        difficultyPanel.add(difficultyBox, gbc);
        gbc.gridy = 2;
        difficultyPanel.add(ApplyDifficulty, gbc);
        gbc.gridwidth = 1;

        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        optionsPanel.add(optionsLabel, gbc);
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        optionsPanel.add(wallsBox, gbc);
        gbc.gridx = 1;
        optionsPanel.add(wormholesBox, gbc);
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        optionsPanel.add(ApplyOptions, gbc);
        gbc.gridwidth = 1;

        actionsPanel.add(RestartButton);
        actionsPanel.add(ExitButton);

        // Add objects to window
        mainPanel.add(titleLabel);
        mainPanel.add(createSpacer(8));
        mainPanel.add(sizePanel);
        mainPanel.add(createSpacer(10));
        mainPanel.add(difficultyPanel);
        mainPanel.add(createSpacer(10));
        mainPanel.add(optionsPanel);
        mainPanel.add(createSpacer(14));
        mainPanel.add(actionsPanel);

        this.add(mainPanel);

        // Do nothing on close
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // ESC lukker menuen igen
        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke("ESCAPE"), "closeMenu");
        this.getRootPane().getActionMap().put("closeMenu", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gview.ClosePopupMenu();
            }
        });
    }


    private void applySelectedDifficulty() {
        Difficulty selected = (Difficulty) difficultyBox.getSelectedItem();
        if (selected != null) {
            gview.setDifficulty(selected);
        }
    }

    private void applySelectedOptions(boolean restart) {
        gview.setWallsEnabled(wallsBox.isSelected());
        gview.setWormholesEnabled(wormholesBox.isSelected());
        if (restart) {
            gview.restartGame();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ChangeGridSize){

            String rowEntryVariable = rowEntry.getText();
            String colEntryVariable = colEntry.getText();
            int rowVar;
            int colVar;

            try {
                rowVar = Integer.parseInt(rowEntryVariable);
                colVar = Integer.parseInt(colEntryVariable);
            } catch (NumberFormatException exception){
                rowVar = 0;
                colVar = 0;
            }

            if (rowVar <= 100 && rowVar >= 5 && colVar <= 100 && colVar >= 5) {
                MainApp.startGame(rowVar, colVar);
            }

        } else if (e.getSource() == ApplyDifficulty) {
            applySelectedDifficulty();
        } else if (e.getSource() == ApplyOptions) {
            applySelectedOptions(true);
        } else if (e.getSource() == ExitButton) {
            gview.ClosePopupMenu();
        } else if (e.getSource() == RestartButton) {
            applySelectedDifficulty();
            applySelectedOptions(false);
            gview.restartGame();
        }
    }

    private JPanel createSpacer(int height) {
        JPanel spacer = new JPanel();
        spacer.setPreferredSize(new Dimension(1, height));
        spacer.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        spacer.setBackground(new Color(24, 24, 24));
        return spacer;
    }

}
