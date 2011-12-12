/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.util;

/**
 * Representation of tree structure.
 */
public class TreeStructureClass implements TreeStructure {

    private String name;

    private TreeStructure[] children;

    public TreeStructureClass(String name) {
        this(name, null);
    }

    public TreeStructureClass(String name, TreeStructure[] children) {
        this.name = name;
        this.children = children;
    }

    /**
     * get a string expression of this object.
     */
    public String getTreeNodeExpression() {
        return name;
    }

    /**
     * return child tree node.
     * 
     * @return children nodes. if this object is terminal node, return null.
     */
    public TreeStructure[] getTreeNodes() {
        return children;
    }

    // ====================================

    public static String toString(TreeStructure root) {
        StringBuffer sb = new StringBuffer();
        toString(root, sb, 0);
        return sb.toString();
    }

    private static String makeNode(String content, int tab) {
        String tt = "";
        for (int i = 0; i < tab; i++) {
            tt += "  ";
        }
        if (content.indexOf("\n") == -1) {
            return tt + content + "\n";
        }
        StringBuffer sb = new StringBuffer();
        String[] lines = content.split("\n");
        for (int i = 0; i < lines.length; i++) {
            sb.append(tt).append(lines[i]).append("\n");
        }
        return sb.toString();
    }

    private static void toString(TreeStructure node, StringBuffer sb, int tab) {
        sb.append(makeNode(node.getTreeNodeExpression(), tab));
        TreeStructure[] children = node.getTreeNodes();
        if (children != null && children.length > 0) {
            for (int i = 0; i < children.length; i++) {
                toString(children[i], sb, tab + 1);
            }
        }
    }

}
