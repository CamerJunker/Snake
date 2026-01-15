package snake;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class PopupMenu extends JFrame implements ActionListener{
    JLabel label = new JLabel("New Window");

    JTextField rowEntry = new  JTextField();
    JTextField colEntry = new  JTextField();

    JButton ChangeGridSize = new JButton("Change Grid Size");

    PopupMenu() {
        // Create window
        
        this.setLayout((new FlowLayout()));

        label.setFont(new Font(null,Font.PLAIN, 25));

        rowEntry.setPreferredSize(new Dimension(250, 40));
        colEntry.setPreferredSize(new Dimension(250, 40));

        ChangeGridSize.addActionListener(this);

        this.add(rowEntry);
        this.add(colEntry);
        this.add(ChangeGridSize);
        this.add(label);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(400,400);
        this.setVisible(true);

        

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ChangeGridSize){
            System.out.println(rowEntry.getText() + " " + colEntry.getText());
        }
    }

}
