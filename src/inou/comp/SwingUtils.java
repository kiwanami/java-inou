/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/** utility for swing */
public class SwingUtils {

    /**
     * magnify the all fonts in swing.
     * 
     * @param mag
     *            magnify parameter <br/> mag > 1. : large <br/> mag < 1. :
     *            small
     */
    public static void magnifyAllFont(float mag) {
        List history = new LinkedList();
        UIDefaults defaults = UIManager.getDefaults();
        Enumeration it = defaults.keys();
        while (it.hasMoreElements()) {
            Object key = it.nextElement();
            Object a = defaults.get(key);
            if (a instanceof Font) {
                if (history.contains(a))
                    continue;
                Font font = (Font) a;
                font = font.deriveFont(font.getSize2D() * mag);
                defaults.put(key, font);
                history.add(font);
            }
        }
    }

    /**
     * create JFrame that has a WindowListener object that was invoked
     * System.exit(0) by pushing close button.
     */
    public static JFrame getTestFrame(String title) {
        JFrame f = new JFrame(title);
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        return f;
    }

    /**
     * GUI utility (change border title)
     * 
     * @param panel
     *            bborder component
     * @param title
     *            new title
     */
    public static void borderTitle(JComponent panel, String title) {
        Border b = panel.getBorder();
        if (b instanceof TitledBorder)
            ((TitledBorder) b).setTitle(title);
    }

    /** GUI utility (set frame on the center of screen) */
    public static void setCenter(Window w) {
        Dimension scSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension wndSize = w.getSize();
        w.setLocation((scSize.width - wndSize.width) / 2,
                (scSize.height - wndSize.height) / 2);
    }
}
