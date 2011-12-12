/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.util;

/**
 * Representation of simple tree structure.
 */
public interface TreeStructure {

    /**
     * get a string expression of this object.
     */
    public String getTreeNodeExpression();

    /**
     * return child tree nodes.
     * 
     * @return children nodes. if this object is a terminal node, return null.
     */
    public TreeStructure[] getTreeNodes();
}