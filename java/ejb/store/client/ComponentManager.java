package ejb.store.client;

import java.awt.event.ActionListener;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Collection;
import java.util.Iterator;

import javax.rmi.PortableRemoteObject;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import ejb.store.*;
import ejb.util.Lookup;


public class ComponentManager {

    private JFrame     frame;
    private JComboBox  storeCombo;
    private JList      rackList;
    private JTable     positionsTable;
    private StoreHome  storeHome;

    public ComponentManager(JFrame frame) {
        Assert.notNull("frame", frame);

        this.frame = frame;

        this.frame.getContentPane().setLayout(new BorderLayout());

        try {
            this.storeHome =
                (StoreHome)Lookup.narrow("ejb.store.Store",
                                         StoreHome.class);
        } catch(Exception ex) {
            System.err.println("ERROR: " + ex.toString());
            return;
        }
        this.createStoreSelector();
        this.createRackList();
        this.loadRackList();
        this.createPositionsTable();
    }


    private void createPositionsTable() {
        Container panel = this.frame.getContentPane();

        this.positionsTable = new JTable(new DefaultTableModel());
        this.positionsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.positionsTable.setRowHeight(this.positionsTable.getRowHeight() * 2);
        this.positionsTable.setCellSelectionEnabled(true);
        JScrollPane p = new JScrollPane(this.positionsTable);

        panel.add(p, "Center");
    }


    public void loadPositionsTable() {
        DefaultTableModel model = (DefaultTableModel)this.positionsTable.getModel();
        model.setColumnCount(0);
        model.setRowCount(0);

        RackItem  rackItem  = (RackItem)this.rackList.getSelectedValue();
        StoreItem storeItem = (StoreItem)this.storeCombo.getSelectedItem();
        if(rackItem == null || storeItem == null) {
            return;
        }
        try {
            Store store = this.storeHome.findByPrimaryKey(storeItem.getId());
            Collection col = store.getStoreRackPositions(rackItem.getRack());
            Iterator it = col.iterator();
            while(it.hasNext()) {
                Position pos =
                    (Position)PortableRemoteObject.narrow(it.next(),
                                                          Position.class);
                if(pos.getColumn().intValue() > model.getColumnCount()) {
                    model.setColumnCount(pos.getColumn().intValue());
                }
                if(pos.getRow().intValue() > model.getRowCount()) {
                    model.setRowCount(pos.getRow().intValue());
                }
                model.setValueAt(new PositionItem(pos),
                                 pos.getRow().intValue() -1,
                                 pos.getColumn().intValue() -1);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }


    private void createRackList() {
        Container panel = this.frame.getContentPane();

        JPanel p = new JPanel();
        p.setLayout(new GridLayout(1, 1));

        this.rackList = new JList();
        this.rackList.setModel(new DefaultListModel());
        p.add(this.rackList);

        panel.add(p, "West");

    }


    public void loadRackList() {
        DefaultListModel model = (DefaultListModel)this.rackList.getModel();
        model.removeAllElements();
        
        StoreItem item = (StoreItem)this.storeCombo.getSelectedItem();
        if(item == null) {
            return;
        }

        try {
            Store store = this.storeHome.findByPrimaryKey(item.getId());
            Collection col = store.getStoreRacks();
            Iterator it = col.iterator();
            while(it.hasNext()) {
                Rack rack =
                    (Rack)PortableRemoteObject.narrow(it.next(),
                                                      Rack.class);
                model.addElement(new RackItem(rack));
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        this.rackList.addListSelectionListener((ListSelectionListener)this.frame);
    }


    private void createStoreSelector() {
        Container panel = this.frame.getContentPane();

        JPanel p = new JPanel();

        JLabel label = new JLabel("Store:");
        p.add(label);

        this.storeCombo = new JComboBox();
        try {
            Collection col = this.storeHome.findAllStores();
            Iterator it = col.iterator();
            while(it.hasNext()) {
                Store store =
                    (Store)PortableRemoteObject.narrow(it.next(),
                                                       Store.class);
                this.storeCombo.addItem(
                    new StoreItem(store.getStoreName(),
                                  store.getStoreId())
                );
            }
            this.storeCombo.setSelectedIndex(0);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        this.storeCombo.addActionListener((ActionListener)this.frame);
        p.add(this.storeCombo);

        panel.add(p, "North");
    }


    public void showItem() {
        DefaultTableModel model = (DefaultTableModel)this.positionsTable.getModel();
        PositionItem item = null;
        for(int i = 0; i < this.positionsTable.getRowCount(); i++) {
            for(int j = 0; j < this.positionsTable.getColumnCount(); j++) {
                if(this.positionsTable.isCellSelected(i, j)) {
                    item = (PositionItem)model.getValueAt(i, j);
                    break;
                }
            }
        }
        if(item == null) {
            return;
        }
        Store store = this.getCurrent();
        Item i = null;
        try {
            Object o = store.getItemInPosition(item.getPosition());
            i = (Item)PortableRemoteObject.narrow(o, Item.class);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        new ItemDialog(this.frame, i);
    }


    public Store getCurrent() {
        StoreItem item = (StoreItem)this.storeCombo.getSelectedItem();
        if(item == null) {
            return null;
        }
        Store store = null;
        try {
            store = this.storeHome.findByPrimaryKey(item.getId());
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return store;
    }


    public static class StoreItem {

        private String  name;
        private Integer id;

        public StoreItem(String name, Integer id) {
            this.name = name;
            this.id   = id;
        }

        public Integer getId() {
            return this.id;
        }

        public String toString() {
            return this.name;
        }

    }


    public static class RackItem {

        private Rack rack;

        public RackItem(Rack rack) {
            this.rack = rack;
        }

        public Rack getRack() {
            return this.rack;
        }

        public String toString() {
            return this.rack.getDescription();
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
            return this.pos.getRow() + " / " + this.pos.getColumn();
        }

    }

}
