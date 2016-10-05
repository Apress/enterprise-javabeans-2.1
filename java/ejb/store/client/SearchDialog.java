package ejb.store.client;

import javax.rmi.PortableRemoteObject;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Point;
import java.util.Collection;
import java.util.Iterator;

import ejb.store.Store;
import ejb.store.Item;
import ejb.store.Position;


public class SearchDialog extends JDialog {

    public static final String CLOSE  = "Close";
    public static final String SEARCH = " Search ";

    private JFrame frame;
    private JList itemList;
    private JList resultList;
    private ComponentManager cManager;

    public SearchDialog(JFrame frame, ActionListener al, ComponentManager cm) {
        super(frame, "Search", true);

        this.frame    = frame;
        this.cManager = cm;

        this.itemList = new JList();
        this.itemList.setModel(new DefaultListModel());
        this.loadItemList();
        this.resultList = new JList();
        this.resultList.setModel(new DefaultListModel());
        JSplitPane panel = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                                          new JScrollPane(this.itemList),
                                          new JScrollPane(this.resultList));
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(panel, "Center");

        JPanel p = new JPanel();
        JButton s = new JButton(SEARCH);
        s.addActionListener(al);
        p.add(s);
        JButton c = new JButton(CLOSE);
        c.addActionListener(al);
        p.add(c);
        this.getContentPane().add(p, "South");

        this.pack();

    }

    private void loadItemList() {
        Store store = this.cManager.getCurrent();
        Collection col = null;
        try {
            col = store.getAllStoreItems();
        } catch(Exception ex) {
            ex.printStackTrace();
            return;
        }
        Iterator it = col.iterator();
        DefaultListModel model = (DefaultListModel)this.itemList.getModel();
        while(it.hasNext()) {
            Object o = it.next();
            Item i = (Item)PortableRemoteObject.narrow(o, Item.class);
            model.addElement(new StoreItem(i));
        }
    }

    public void show() {
        this.setLocation(new Point(this.frame.getX() + (this.frame.getWidth() / 2) - (this.getWidth() / 2),
                                   this.frame.getY() + (this.frame.getHeight() / 2) - this.getHeight() / 2));
        super.show();
    }


    public void doSearch() {
        DefaultListModel model = (DefaultListModel)this.resultList.getModel();
        model.removeAllElements();
        StoreItem si = (StoreItem)this.itemList.getSelectedValue();
        if(si == null) {
            return;
        }
        Store store = this.cManager.getCurrent();
        Collection col = null;
        try {
            col = store.getPositionsForItem(si.getItem());
        } catch(Exception ex) {
            ex.printStackTrace();
            return;
        }
        Iterator it = col.iterator();
        while(it.hasNext()) {
            Object o = it.next();
            Position p = (Position)PortableRemoteObject.narrow(o, Position.class);
            model.addElement(new PositionItem(p));
        }
    }


    public static class StoreItem {
         
        private Item item;

        public StoreItem(Item item) {
            this.item = item;
        }

        public Item getItem() {
            return this.item;
        }
                                                                       
        public String toString() {
            return this.item.getDescription();
        }

    }

    public static class PositionItem {
         
        private Position pos;
                  
        public PositionItem(Position pos) {
            this.pos = pos;
        }
                                                   
        public Position getPosition() {
            return this.pos;
        }
                                                                                  
        public String toString() {
            return this.pos.getRack().getDescription() + ": " +
                   this.pos.getRow() + " / " + this.pos.getColumn();
        }

    }

}
