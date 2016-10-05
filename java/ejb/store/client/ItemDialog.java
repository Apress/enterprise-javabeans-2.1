package ejb.store.client;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridLayout;
import java.awt.Point;

import ejb.store.Item;


public class ItemDialog extends JDialog implements ActionListener {


    public ItemDialog(JFrame frame, Item item) {
        super(frame, "Show Item", true);

        this.getContentPane().setLayout(new GridLayout(2, 1));
        JLabel label = new JLabel(item.toString());
        JButton b = new JButton("Close");
        b.addActionListener(this);
        JPanel p = new JPanel();
        p.add(b);
        this.getContentPane().add(label);
        this.getContentPane().add(p);

        this.pack();

        this.setLocation(new Point(frame.getX() + (frame.getWidth() / 2) - (this.getWidth() / 2),
                                   frame.getY() + (frame.getHeight() / 2) - this.getHeight() / 2));

        this.show();
    }


    public void actionPerformed(ActionEvent evt) {
        this.hide();
        this.dispose();
    }

}
