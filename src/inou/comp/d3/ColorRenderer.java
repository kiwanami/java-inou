/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.d3;

import inou.math.vector.VectorQD;

import java.awt.Color;
import java.awt.Graphics;

public class ColorRenderer extends MonotoneRenderer {

    public ColorRenderer(SceneContext sceneContext) {
        super(sceneContext);
    }

    public ColorRenderer(SceneContext sceneContext, Camera camera,
            RendererComponent rc) {
        super(sceneContext, camera, rc);
    }

    final protected void drawSurface(Graphics g, SurfaceBuffer surfaceBuffer) {
        for (int i = 0; i < surfaceBuffer.bufferIndex.length; i++) {
            VectorQD vec = vertexBuffer[surfaceBuffer.bufferIndex[i]];
            if (vec.z < 0)
                return;
            int px = (int) vec.x;
            int py = (int) vec.y;
            if (px < (-paintRegion.width) || px > paintRegion.width
                    || py < (-paintRegion.height) || py > paintRegion.height)
                return;
            surfaceBuffer.polygonX[i] = px + centerPosition.x;
            surfaceBuffer.polygonY[i] = py + centerPosition.y;
        }
        g.setColor(surfaceBuffer.surface.getColor());
        g.fillPolygon(surfaceBuffer.polygonX, surfaceBuffer.polygonY,
                surfaceBuffer.bufferIndex.length);
        Color borderColor = ((IWireObject) surfaceBuffer.object)
                .getLinePareById(0).color;// ###
        if (!surfaceBuffer.surface.getColor().equals(borderColor)) {
            g.setColor(borderColor);// ###
            g.drawPolygon(surfaceBuffer.polygonX, surfaceBuffer.polygonY,
                    surfaceBuffer.bufferIndex.length);
        }
    }

    final protected void drawWire(Graphics g, WireBuffer wireBuffer) {
        VectorQD vec = vertexBuffer[wireBuffer.startId];
        if (vec.z < 0)
            return;
        int sx = (int) vec.x;
        int sy = (int) vec.y;
        if (sx < (-paintRegion.width) || sx > paintRegion.width
                || sy < (-paintRegion.height) || sy > paintRegion.height)
            return;
        sx = sx + centerPosition.x;
        sy = sy + centerPosition.y;

        vec = vertexBuffer[wireBuffer.endId];
        if (vec.z < 0)
            return;
        int ex = (int) vec.x;
        int ey = (int) vec.y;
        if (ex < (-paintRegion.width) || ex > paintRegion.width
                || ey < (-paintRegion.height) || ey > paintRegion.height)
            return;
        ex = ex + centerPosition.x;
        ey = ey + centerPosition.y;

        g.setColor(wireBuffer.getColor());
        g.drawLine(sx, sy, ex, ey);
    }

    final protected void drawCircle(Graphics g, NonSurfaceBuffer nonBuffer) {
        VectorQD vec = vertexBuffer[nonBuffer.vertexId];
        if (vec.z < 0)
            return;
        int px = (int) vec.x;
        int py = (int) vec.y;
        if (px < (-paintRegion.width) || px > paintRegion.width
                || py < (-paintRegion.height) || py > paintRegion.height)
            return;
        CircleObject co = (CircleObject) nonBuffer.object;
        double perse = persepectiveRatio(vec.z);
        int size = (int) (co.getDiameter() * perse);
        if (size > paintRegion.width)
            return;
        int radious = size / 2;
        px = px + centerPosition.x;
        py = py + centerPosition.y;
        if (co.getSurfaceColor() != null) {
            g.setColor(co.getSurfaceColor());
            g.fillOval(px - radious, py - radious, size, size);
        }
        if (co.getSurfaceColor() == null
                || !co.getSurfaceColor().equals(co.getBorderColor())) {
            g.setColor(co.getBorderColor());
            g.drawOval(px - radious, py - radious, size, size);
        }
    }

}