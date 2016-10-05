package ejb.store.client;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


public class Menubar extends JMenuBar {

    public static final String MENU_FILE   = "File";
    public static final String MENU_VIEW   = "View";
    public static final String MENU_SEARCH = "Search";

    public static final String ITEM_EXIT    = "Exit";
    public static final String ITEM_ITEM    = "Item";
    public static final String ITEM_FORITEM = "for Item";


    public Menubar(ActionListener al) {
        Assert.notNull("actionListener", al);

        JMenu file = new JMenu(MENU_FILE);
        JMenuItem exit = new JMenuItem(ITEM_EXIT);
        exit.addActionListener(al);
        file.add(exit);
        this.add(file);

        JMenu view = new JMenu(MENU_VIEW);
        JMenuItem item = new JMenuItem(ITEM_ITEM);
        item.addActionListener(al);
        view.add(item);
        this.add(view);

        JMenu search = new JMenu(MENU_SEARCH);
        JMenuItem foritem = new JMenuItem(ITEM_FORITEM);
        foritem.addActionListener(al);
        search.add(foritem);
        this.add(search);
    }

}
