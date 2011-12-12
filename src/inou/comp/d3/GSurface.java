/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.d3;

import java.awt.Color;

/**
 * This class presents general surface that has more than 5 vertices. The
 * vertices should keep following restrictions. All vertices should be on the
 * same surface. The order of vertices should be anticlockwise if this polygon
 * has invisible side.
 */
public class GSurface extends QSurface {

    //                         
    // - 3-----2
    // / |
    // 4 |
    // \ 1
    // \ __--^
    // 0-^

    public GSurface(int[] vertexIdArray, Color surfaceColor) {
        if (vertexIdArray.length < 4) {
            throw new InternalError("too few vertices. ["
                    + vertexIdArray.length + "]");
        }
        index = new int[vertexIdArray.length];
        for (int i = 0; i < vertexIdArray.length; i++) {
            index[i] = vertexIdArray[i];
        }
        this.color = surfaceColor;
    }

    public Surface getCopy() {
        return new GSurface(index, color);
    }
}