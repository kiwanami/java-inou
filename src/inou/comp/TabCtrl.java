/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** Tab component (used in inou.comp.TabPanel) */
public class TabCtrl extends Panel {

    private ArrayList actionListeners = new ArrayList();

    private List items = new ArrayList();

    private String activeTab = "";

    private boolean isRepaint = false;

    private TabParam param = new TabParam();

    // ====== constructor

    public TabCtrl() {
        super();
        initLayout();
        setBackground(param.getBackground());
    }

    /** called by this.<init> */
    private void initLayout() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        setBackground(param.getBackground());
        isRepaint = true;
    }

    public TabCtrl(ActionListener ms) {
        this();
        addActionListener(ms);
    }

    public void addActionListener(ActionListener m) {
        actionListeners.add(m);
    }

    public void setFont(Font f) {
        param.setTabFont(f);
        updateImmediate();
    }

    public void setColor(Color fc, Color bc) {
        param.setColor(fc, bc);
        setBackground(bc);
        updateImmediate();
    }

    public void setTabName(String key, String newName) {
        TabItem tb = getItemByKey(key);
        if (tb == null)
            return;
        tb.setTabName(newName);
        isRepaint = true;
        updateImmediate();
    }

    public void addTab(String name, String key) {
        if (getItemByKey(key) != null) {
            System.out.println("TabCtrl warning : the same key[" + key + "]");
            return;
        }
        TabItem item = new TabItem(name, key, this, param);
        add(item);
        items.add(item);
        if (items.size() == 1) {
            item.setActive(true);
            activeTab = key;
            setActive(key);
        }
        isRepaint = true;
        TabItem pt = getItemByKey(getActiveTabKey());
        if (pt == null) {
            getParent().doLayout();
        } else {
            updateImmediate();
        }
    }

    private TabItem getItemByKey(String key) {
        if (key == null)
            return null;
        for (Iterator it = items.iterator(); it.hasNext();) {
            TabItem i = (TabItem) it.next();
            if (i.getKey().equals(key)) {
                return i;
            }
        }
        return null;
    }

    public String getActiveTabKey() {
        return activeTab;
    }

    public void removeTab(String key) {
        TabItem tb = getItemByKey(key);
        if (tb != null) {
            remove(tb);
            items.remove(tb);
            setActive(null);
            isRepaint = true;
        }
    }

    public void removeAllTabs() {
        items.clear();
        removeAll();
        isRepaint = true;
    }

    public void setActive(String key) {
        if (items.isEmpty())
            return;
        TabItem tb = getItemByKey(key);
        if (tb == null) {
            tb = (TabItem) items.get(0);
        }
        selectTab(tb);
    }

    public void addNotify() {
        super.addNotify();
        int x = 0, y = 0;
        if (!items.isEmpty()) {
            for (Iterator it = items.iterator(); it.hasNext();) {
                Component tb = (Component) (it.next());
                Dimension sz = tb.getSize();
                x += sz.width;
                y = Math.max(y, sz.height);
            }
        } else {
            x = 120;
            y = 40;
        }
        setSize(x + 2, y);
    }

    public void update(Graphics g) {
        if (isRepaint) {
            isRepaint = false;
        }
        super.update(g);
    }

    /**
     * change the focus to given TabItem. called by this.setActive,TabItem.<init>
     */
    public void selectTab(TabItem tb) {
        TabItem pt = getItemByKey(getActiveTabKey());
        if (tb == pt) {
            return;
        }
        tb.setActive(true);
        if (pt != null) {
            pt.setActive(false);
        }
        updateImmediate();
        activeTab = tb.getKey();
        fireActionEvent(tb);
    }

    /**
     * send a CHANGE_TAB message to parent. called by this.selectTab
     */
    private void fireActionEvent(TabItem tb) {
        if (actionListeners.isEmpty())
            return;
        ActionEvent ae = new ActionEvent(this, 0, tb.getKey());
        for (Iterator e = actionListeners.iterator(); e.hasNext();) {
            ((ActionListener) e.next()).actionPerformed(ae);
        }
    }

    /** repaint this component. */
    private void updateImmediate() {
        invalidate();
        doLayout();
        repaint();
    }

    /**
     * It is called automatically by the system the application is started.
     */
    public static void main(String args[]) {
        Frame frame = new Frame("test tab");
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        Label sh = new Label("Tab Component");
        frame.add(sh, "Center");

        TabCtrl tb = new TabCtrl();
        frame.add(tb, "North");
        tb.addTab("first", "first");
        tb.addTab("tab", "message");
        tb.addTab("This", "this");
        tb.addTab("test TabCtrl.", "tab tab");
        tb.addTab("Ok?", "this is key");
        tb.setColor(Color.blue.darker(), Color.red.brighter());
        // test
        final TextField tx = new TextField();
        frame.add(tx, "South");
        tb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tx.setText((String) e.getActionCommand());
            }
        });
        frame.pack();
        frame.show();
    }
}

class TabParam {

    private int stringHeight = 15;

    private int bottomOffset = 2;

    private int topOffset = 1;

    private int verticalOffset = 2;

    private int horizontalOffset = 3;

    private int delta = 2;

    private int cutSize = 2;

    private double spaceParam = 1.2;

    private Color darkGray, lightGray;

    private Color borderColor, forground, background;

    private Font tabFont;

    TabParam() {
        setTabFont(new Font("SansSerif", Font.PLAIN, 12));
        setColor(Color.black, Color.lightGray);
    }

    void setColor(Color fc, Color bc) {
        forground = fc;
        background = bc;
        borderColor = fc.brighter();
        lightGray = bc.brighter();
        darkGray = bc.darker();
    }

    Color getForeground() {
        return forground;
    }

    Color getBackground() {
        return background;
    }

    Color getBorderColor() {
        return borderColor;
    }

    Color getLightBorder() {
        return lightGray;
    }

    Color getDarkBorder() {
        return darkGray;
    }

    int getCutSize() {
        return cutSize;
    }

    double getSpaceParam() {
        return spaceParam;
    }

    int getDelta() {
        return delta;
    }

    int getStringHeight() {
        return stringHeight;
    }

    int getBottomOffset() {
        return bottomOffset;
    }

    int getTopOffset() {
        return topOffset;
    }

    int getVerticalOffset() {
        return verticalOffset;
    }

    int getHorizontalOffset() {
        return horizontalOffset;
    }

    int getTotalWidth(int ct) {
        return (int) (ct * spaceParam) + horizontalOffset * 2;
    }

    int getTotalHeight(int ct) {
        return ct + verticalOffset * 2 + topOffset + bottomOffset + delta * 2;
    }

    void setTabFont(Font f) {
        tabFont = f;
    }

    Font getTabFont() {
        return tabFont;
    }

}

/**
 * TabItem manages tab information
 */
class TabItem extends Canvas {

    private RichString contents;

    private TabCtrl parent;

    private boolean active, isRepaint;

    private String key;

    private TabParam param;

    // ==== constructor

    TabItem(String title, String key, TabCtrl p, TabParam pm) {
        super();
        contents = new RichString(title, pm.getForeground(),
                pm.getBackground(), pm.getTabFont());
        parent = p;
        param = pm;
        this.key = key;
        isRepaint = true;
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                parent.selectTab(TabItem.this);
            }
        });
    }

    /**
     * update this information to repaint. called by this.setActive,
     * TabCtrl.setColor.
     */
    void updateInfo() {
        if (isActive()) {
            contents.setFont(param.getTabFont());
        } else {
            contents.setFont(param.getTabFont());
        }
        repaint();
    }

    String getKey() {
        return key;
    }

    void setActive(boolean b) {
        active = b;
        // isRepaint = true;
        // (if "isRepaint" is checked, recalculate component size.)
        updateInfo();
    }

    boolean isActive() {
        return active;
    }

    String getTabName() {
        return contents.getContent();
    }

    void setTabName(String name) {
        contents.setContent(name);
        isRepaint = true;
    }

    public void addNotify() {
        super.addNotify();
        Dimension d = mesureSize();
        setSize(param.getTotalWidth(d.width), param.getTotalHeight(d.height));
    }

    public Dimension getPreferredSize() {
        return getSize();
    }

    protected Dimension mesureSize() {
        Dimension d = contents.getSize(this);
        return d;
    }

    /**
     * Updates the component. This method is called in response for a call to
     * repaint. You can assume that the background is not cleared.
     * 
     * @param g
     *            the specified Graphics window
     * @see java.awt.Component#update
     */
    public void update(Graphics g) {
        if (isActive()) {
            contents.setFont(param.getTabFont());
            contents.setForeground(param.getForeground());
            contents.setBackground(param.getBackground());
        } else {
            contents.setFont(param.getTabFont());
            contents.setForeground(param.getForeground());
            contents.setBackground(param.getBackground());
        }
        paint(g);
    }

    /**
     * Paints the component.
     * 
     * @param g
     *            the specified Graphics window
     * @see java.awt.Component#paint
     */
    public void paint(Graphics g) {
        // check size
        if (isRepaint) {
            Dimension s = contents.getSize(g);
            setSize(param.getTotalWidth(s.width), param
                    .getTotalHeight(s.height));
            isRepaint = false;
            parent.doLayout();
        }
        Dimension size = getSize();
        if (size == null) {
            isRepaint = true;
            return;
        }
        Color bc = param.getBackground();
        if (bc != null) {
            // fill background
            g.setColor(bc);
            g.fillRect(0, 0, size.width, size.height);
        }
        int delta = param.getDelta();
        // translate to terminal position
        size.width--;
        size.height--;
        int sx, sy, ex, ey, activeOffset = param.getDelta();
        if (isActive()) {
            activeOffset = 0;
        }
        sx = activeOffset;// left-top
        sy = param.getTopOffset() + activeOffset;// left-top
        ex = size.width + activeOffset - delta;// left-bottom
        ey = size.height - param.getBottomOffset();// left-bottom

        // draw contents
        contents.drawContents(g, sx + param.getHorizontalOffset(), sy
                + param.getVerticalOffset());
        // border line
        g.setColor(param.getBorderColor());
        // left vartiacal line
        g.drawLine(sx, sy + param.getCutSize(), sx, ey);
        // cut line
        g.drawLine(sx, sy + param.getCutSize(), sx + param.getCutSize(), sy);
        // top horizontal line
        g.drawLine(sx + param.getCutSize(), sy, ex, sy);
        // right vertical line
        g.drawLine(ex - 1, sy + 1, ex - 1, ey);
        // bottom horizotal line
        g.setColor(param.getBackground());
        if (!isActive()) {
            g.fillRect(0, ey, ex + delta, ey + param.getBottomOffset());
        } else {
            g.drawLine(ex, ey, ex + delta, ey);
            g.fillRect(ex + 1, sy + 2, delta, ey - 3 - sy);
        }

        // high-light
        g.setColor(param.getLightBorder());
        // left vartiacal line
        g.drawLine(sx + 1, sy + param.getCutSize(), sx + 1, ey - 1);
        // cut line
        g.drawLine(sx + 1, sy + param.getCutSize(), sx + param.getCutSize(),
                sy + 1);
        // top horizontal line
        g.drawLine(sx + param.getCutSize(), sy + 1, ex - 2, sy + 1);
        // bottom horizotal line
        if (!isActive()) {
            g.drawLine(0, ey, ex + delta, ey);
        } else {
            g.drawLine(ex, ey, ex + delta, ey);
        }

        g.setColor(param.getDarkBorder());
        // right vertical line
        g.drawLine(ex, sy, ex, ey - 1);

    }

}
// end of TabItem class
