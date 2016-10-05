package ejb.store.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class StoreManager extends JFrame implements ActionListener, ListSelectionListener {

    public static final String VERSION = "$Revision: 1.3 $";

    private ComponentManager cManager;
    private SearchDialog     searchDialog;

    public StoreManager() {
        super("Store Manager " + VERSION);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                shutdown();
            }
        });

        this.setJMenuBar(new Menubar(this));
        this.cManager     = new ComponentManager(this);
        this.searchDialog = new SearchDialog(this, this, this.cManager);

        this.pack();
        this.setSize(400, 300);


        Dimension d = this.getToolkit().getScreenSize();
        this.setLocation(new Point(d.width / 2  - (this.getWidth() / 2),
                                   d.height / 2 - this.getHeight() / 2));

        this.show();
    }


    public void shutdown() {
        setVisible(false);
        dispose();
        System.exit(0);
    }

    //
    // ActionListener interface
    //

    public void actionPerformed(ActionEvent evt) {
        String cmd = evt.getActionCommand();

        if(cmd.equals(Menubar.ITEM_EXIT)) {
            this.shutdown();
        } else if(cmd.equals("comboBoxChanged")) {
            this.cManager.loadRackList();
        } else if(cmd.equals(Menubar.ITEM_FORITEM)) {
            this.searchDialog.show();
        } else if(cmd.equals(Menubar.ITEM_ITEM)) {
            this.cManager.showItem();
        } else if(cmd.equals(SearchDialog.CLOSE)) {
            this.searchDialog.hide();
        } else if(cmd.equals(SearchDialog.SEARCH)) {
            this.searchDialog.doSearch();
        }
    }

    //
    // ListSelectionListener interface
    //

    public void valueChanged(ListSelectionEvent evt) {
        if(evt.getValueIsAdjusting()) {
            return;
        }
        this.cManager.loadPositionsTable();
    }

    //
    // Main Method
    //

    public static void main(String[] args) {
        StoreManager sm = new StoreManager();
    }

}
