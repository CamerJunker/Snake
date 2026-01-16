package snake;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;
import javax.swing.JComponent;

public class PopupMenu extends JFrame implements ActionListener{
    JLabel ChangeGameSizelabel = new JLabel("Change Game Size");
    JLabel rowLabel = new JLabel("Row size (5-100)");
    JLabel colLabel = new JLabel("Column size (5-100)");

    JTextField rowEntry = new  JTextField();
    JTextField colEntry = new  JTextField();

    GameView gview;

    JButton ExitButton = new JButton("Exit");

    JButton ChangeGridSize = new JButton("Change Grid Size");
    JButton RestartButton = new JButton("Restart");

    JPanel entryPanel = new JPanel();

    private int TextSize = 15;
    private int spacing = 10;
    private int windowDimension = 400;
    private int objectHeight = 30;

    private MainApp mainApp;

    PopupMenu(GameView gameview, MainApp main) {

        mainApp = main;

        // Create window
        gview = gameview;
        this.setSize(windowDimension,windowDimension);
        this.setLocationRelativeTo(gview);
        this.setVisible(true);
        this.setResizable(false);

        ExitButton.setFont(new Font(null,Font.PLAIN, TextSize));
        ChangeGameSizelabel.setFont(new Font(null,Font.PLAIN, TextSize+2));
        rowEntry.setFont(new Font(null,Font.PLAIN, TextSize));
        colEntry.setFont(new Font(null,Font.PLAIN, TextSize));
        ChangeGridSize.setFont(new Font(null,Font.PLAIN, TextSize));
        RestartButton.setFont(new Font(null,Font.PLAIN, TextSize));
        rowLabel.setFont(new Font(null,Font.PLAIN, TextSize));
        colLabel.setFont(new Font(null,Font.PLAIN, TextSize));

        this.setLayout((new FlowLayout(FlowLayout.CENTER, spacing, spacing)));

        this.setTitle("Menu");

        entryPanel.setLayout(new GridLayout(2,2,spacing,spacing));
        entryPanel.setPreferredSize(new Dimension(windowDimension-30,(objectHeight + spacing)*2));

        // Add actionlisteners to buttons
        ChangeGridSize.addActionListener(this);
        ExitButton.addActionListener(this);
        RestartButton.addActionListener(this);

        // Add objects to window
        this.add(ChangeGameSizelabel);        
        this.add(entryPanel);
        entryPanel.add(rowLabel);
        entryPanel.add(colLabel);
        entryPanel.add(rowEntry);
        entryPanel.add(colEntry);
        this.add(ChangeGridSize);
        this.add(RestartButton);
        this.add(ExitButton);

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
                MainApp.startGame(rowVar, colVar, gview, mainApp);
            }

        } else if (e.getSource() == ExitButton) {
            gview.ClosePopupMenu();
        } else if (e.getSource() == RestartButton) {
            gview.restartGame();
        }
    }

}
