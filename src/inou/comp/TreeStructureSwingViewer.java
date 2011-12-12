/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp;

import inou.util.TreeStructure;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/** TreeStructure data viewer implemented by JTree */
public class TreeStructureSwingViewer {

    public static DefaultMutableTreeNode translate(TreeStructure root) {
        String name = root.getTreeNodeExpression();
        DefaultMutableTreeNode tn = new DefaultMutableTreeNode(name);
        TreeStructure[] children = root.getTreeNodes();
        if (children != null && children.length > 0) {
            for (int i = 0; i < children.length; i++)
                tn.add(translate(children[i]));
        }
        return tn;
    }

    public static void show(DefaultMutableTreeNode root) {
        JFrame frame = SwingUtils.getTestFrame("Tree Viewer");
        frame.setSize(500, 600);
        JTree tree = new JTree(root);
        frame.getContentPane().add(new JScrollPane(tree));
        frame.show();
    }

}
