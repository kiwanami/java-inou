/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * This class provides the tabbed panel. You can use tabbed panel controle in
 * JDK1.1 environment. You can see how to use this component to see "main"
 * method.
 */
public class TabPanel extends Panel {

    // ====(contructor)==========================

    public TabPanel() {
        this(null);
    }

    public TabPanel(ActionListener p) {
        super();
        init();
        if (p != null) {
            addActionListener(p);
        }
    }

    // ====(operation)==========================

    /**
     * add a component.
     * 
     * @param a
     *            your component
     * @param title
     *            page title, used on a tab
     * @return the key to access the page.
     */
    public String addPage(Component a, String title) {
        String key = getID();
        client.add(a, key);
        tabs.addTab((String) title, key);
        comps.put(key, a);
        doLayout();
        return key;
    }

    public String add(Component a, String title) {
        return addPage(a, title);
    }

    /**
     * remove the page.
     * 
     * @param key
     *            access key.
     */
    public void remove(String key) {
        try {
            Component cp = (Component) comps.get(key);
            client.remove(cp);
            tabs.removeTab(key);
            comps.remove(key);
            String k = tabs.getActiveTabKey();
            change(k);
        } catch (Exception e) {
            System.out.println("TabPanel:removal error. (" + key + ")");
        }
    }

    /**
     * remove all pages.
     */
    public void removeAll() {
        try {
            client.removeAll();
            tabs.removeAllTabs();
            comps.clear();
        } catch (Exception e) {
            System.out.println("TabPanel:removal error. ");
        }
    }

    /**
     * change the page.
     * 
     * @param key
     *            access key.
     */
    public void change(String key) {
        tabs.setActive(key);
        layout.show(client, key);
        doLayout();
    }

    public String getActive() {
        return tabs.getActiveTabKey();
    }

    public void addActionListener(ActionListener m) {
        tabs.addActionListener(m);
    }

    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        return d;
    }

    // ====(private area)==========================

    public void doLayout() {
        super.doLayout();
        client.doLayout();
    }

    protected TabCtrl tabs;

    protected CardLayout layout;

    protected Panel client;

    protected HashMap comps = new HashMap();

    protected void init() {
        setLayout(new BorderLayout());
        layout = new CardLayout();
        client = new Panel();
        client.setLayout(layout);
        tabs = new TabCtrl();
        tabs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                change(e.getActionCommand());
            }
        });
        super.add(tabs, "North");
        super.add(client, "Center");
    }

    private int address = 0;

    private String getID() {
        address++;
        return "t" + address;
    }

    // ====(test method)==================================

    public static void main(String[] args) {
        Frame f = new Frame("tab panel test");
        TabPanel panel = new TabPanel();
        panel.add(new Label("hello label"), "hello");
        panel.add(new Label("Second Pane"), "second");
        panel.add(new Label("Next generation"), "Next generations!");
        f.add(panel);
        f.setSize(400, 300);
        f.show();
    }
}