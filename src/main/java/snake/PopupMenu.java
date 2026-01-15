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

public class PopupMenu extends JFrame implements ActionListener{
    JLabel label = new JLabel("Change Game Size");

    JTextField rowEntry = new  JTextField();
    JTextField colEntry = new  JTextField();

    GameView gview;

    JButton ExitButton = new JButton("Exit");

    JButton ChangeGridSize = new JButton("Change Grid Size");

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
        this.setVisible(true);
        this.setResizable(false);

        ExitButton.setFont(new Font(null,Font.PLAIN, TextSize));
        label.setFont(new Font(null,Font.PLAIN, TextSize));
        rowEntry.setFont(new Font(null,Font.PLAIN, TextSize));
        colEntry.setFont(new Font(null,Font.PLAIN, TextSize));
        ChangeGridSize.setFont(new Font(null,Font.PLAIN, TextSize));

        this.setLayout((new FlowLayout(FlowLayout.CENTER, spacing, spacing)));

        this.setTitle("Menu");

        entryPanel.setLayout(new GridLayout(1,2,spacing,spacing));
        entryPanel.setPreferredSize(new Dimension(windowDimension-30,objectHeight));

        // Add actionlisteners to buttons
        ChangeGridSize.addActionListener(this);
        ExitButton.addActionListener(this);

        // Add objects to window
        this.add(label);        
        this.add(entryPanel);
        entryPanel.add(rowEntry);
        entryPanel.add(colEntry);
        this.add(ChangeGridSize);
        this.add(ExitButton);

        // Do nothing on close
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ChangeGridSize){
            System.out.println(rowEntry.getText() + " " + colEntry.getText());
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

            if (rowVar != 0 && colVar != 0) {
                MainApp.startGame(rowVar, colVar, gview, mainApp);
            }

        } else if (e.getSource() == ExitButton) {
            gview.ClosePopupMenu();
        }
    }

}
