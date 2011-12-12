/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp;

import inou.util.TreeStructure;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.ScrollPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * TreeStructure data viewer implemented by Graphics tree. Simple example:
 * 
 * <pre>
 * // TreeStructure tree
 * TreeStructureViewer tsv = new TreeStructureViewer(tree);
 * tsv.show();
 * </pre>
 * 
 * TODO: rewrite smartly.
 */
public class TreeStructureViewer {

    private TreeStructure treeRoot;

    private BufferedCanvas treeCanvas;

    private Frame frame;

    private TreeViewer treeViewer;

    private boolean vertical = true;

    public TreeStructureViewer(TreeStructure tree) {
        setTree(tree);
    }

    /**
     * set the tree direction. default is true.
     */
    public void setVertical(boolean t) {
        vertical = t;
    }

    /**
     * update tree objects
     */
    public void setTree(TreeStructure tree) {
        this.treeRoot = tree;
    }

    /**
     * Make new frame and show the contents.
     */
    public Frame show() {
        if (frame == null) {
            frame = new Frame("Tree Viewer");
            frame.addWindowListener(new WindowAdapter() {
                public void windowClosed(WindowEvent e) {
                    System.exit(0);
                }
            });
            ScrollPane sc = new ScrollPane();
            sc.add(getComponent());
            frame.add(sc);
            frame.setSize(600, 500);
        }
        frame.show();
        return frame;
    }

    public void update(TreeStructure tr) {
        if (treeViewer == null)
            return;
        treeViewer.setModel(tr);
    }

    /**
     * Make the content component.
     */
    public Component getComponent() {
        if (treeViewer == null) {
            treeViewer = new TreeViewer(treeRoot);
        }
        return treeViewer;
    }

    class TreeViewer extends BufferedCanvas {

        private int vSpace = 15;

        private int hSpace = 15;

        private Font font = new Font("Serif", Font.PLAIN, 16);

        TreeViewer(TreeStructure t) {
            setModel(t);
            setHeavyContents(true);
        }

        public void setModel(TreeStructure t) {
            root = t;
        }

        private TreeStructure root;

        public void bpaint(Graphics g) {
            PaintNode node = adjustSize(g);
            if (vertical) {
                painter_v(g, node, new Point(hSpace, vSpace), null);
            } else {
                painter_h(g, node, new Point(hSpace, vSpace), null);
            }
        }

        private PaintNode adjustSize(Graphics g) {
            int width = 0;
            int height = 0;
            PaintNode tree = null;
            if (vertical) {
                tree = calcSize_v(g, root);
            } else {
                tree = calcSize_h(g, root);
            }
            int ww = tree.w + hSpace * 2;
            int hh = tree.h + vSpace * 2;
            setSize(ww, hh);
            return tree;
        }

        private PaintNode calcSize_v(Graphics g, TreeStructure node) {
            RichString name = new RichString(node.getTreeNodeExpression(),
                    Color.black, Color.lightGray, font);
            Dimension tdim = name.getSize(g);

            TreeStructure[] children = node.getTreeNodes();
            Dimension dim = new Dimension(0, 0);
            PaintNode[] nodes = null;
            if (children != null && children.length > 0) {
                nodes = new PaintNode[children.length];
                for (int i = 0; i < children.length; i++) {
                    PaintNode r = calcSize_v(g, children[i]);
                    nodes[i] = r;
                    dim.width += r.w + hSpace;
                    if (dim.height < r.h) {
                        dim.height = r.h;
                    }
                }
                dim.height += tdim.height + vSpace;
            } else {
                dim = new Dimension(tdim.width, tdim.height);
            }
            return new PaintNode(name, dim.width, dim.height, nodes);
        }

        private PaintNode calcSize_h(Graphics g, TreeStructure node) {
            RichString name = new RichString(node.getTreeNodeExpression(),
                    Color.black, Color.lightGray, font);
            Dimension tdim = name.getSize(g);

            TreeStructure[] children = node.getTreeNodes();
            Dimension dim = new Dimension(0, 0);
            PaintNode[] nodes = null;
            if (children != null && children.length > 0) {
                nodes = new PaintNode[children.length];
                for (int i = 0; i < children.length; i++) {
                    PaintNode r = calcSize_h(g, children[i]);
                    nodes[i] = r;
                    dim.height += r.h + vSpace;
                    if (dim.width < r.w) {
                        dim.width = r.w;
                    }
                }
                dim.width += tdim.width + hSpace;
            } else {
                dim = new Dimension(tdim.width, tdim.height);
            }
            return new PaintNode(name, dim.width, dim.height, nodes);
        }

        /**
         * @param node
         *            current node
         * @param ps
         *            current tree left-upper position
         */
        private void painter_v(Graphics g, PaintNode node, Point ps, Point con) {
            Dimension tdim = node.name.getSize(g);
            // current node left-upper position
            int mx = ps.x + node.w / 2 - tdim.width / 2;
            int my = ps.y;
            if (node.children != null) {
                // modulation
                int avx = 0;
                int xx = 0;
                for (int i = 0; i < node.children.length; i++) {
                    avx += ps.x + xx + node.children[i].w / 2;
                    xx += node.children[i].w + hSpace;
                }
                mx = avx / node.children.length - tdim.width / 2;
            }
            if (con != null)
                g.drawLine(mx + tdim.width / 2, my, con.x, con.y);
            node.name.drawContents(g, mx, my);
            Point myCon = new Point(mx + tdim.width / 2, my + tdim.height);
            if (node.children != null) {
                int xx = 0;
                for (int i = 0; i < node.children.length; i++) {
                    int cx = ps.x + xx;
                    int cy = ps.y + tdim.height + vSpace;
                    painter_v(g, node.children[i], new Point(cx, cy), myCon);
                    xx += node.children[i].w + hSpace;
                }
            }
        }

        /**
         * @param node
         *            current node
         * @param ps
         *            current tree left-upper position
         */
        private void painter_h(Graphics g, PaintNode node, Point ps, Point con) {
            Dimension tdim = node.name.getSize(g);
            // current node left-upper position
            int mx = ps.x;
            int my = ps.y + node.h / 2 - tdim.height / 2;
            if (node.children != null) {
                // modulation
                int avy = 0;
                int yy = 0;
                for (int i = 0; i < node.children.length; i++) {
                    avy += ps.y + yy + node.children[i].h / 2;
                    yy += node.children[i].y + vSpace;
                }
                my = avy / node.children.length - tdim.height / 2;
            }
            if (con != null) {
                g.drawLine(mx, my + tdim.height / 2, con.x, con.y);
            }
            node.name.drawContents(g, mx, my);
            Point myCon = new Point(mx + tdim.width, my + tdim.height / 2);
            if (node.children != null) {
                int yy = 0;
                for (int i = 0; i < node.children.length; i++) {
                    int cx = ps.x + tdim.width + hSpace;
                    int cy = ps.y + yy;
                    painter_h(g, node.children[i], new Point(cx, cy), myCon);
                    yy += node.children[i].h + vSpace;
                }
            }
        }

        class PaintNode {
            PaintNode(RichString n, int w, int h, PaintNode[] ns) {
                this.name = n;
                this.w = w;
                this.h = h;
                this.children = ns;
            }

            RichString name;

            int x, y;

            int w, h;

            PaintNode[] children;
        }

    }
}
