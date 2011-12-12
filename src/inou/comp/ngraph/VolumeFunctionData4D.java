/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import inou.math.MathVector;
import inou.math.RealRange;
import inou.math.ScalarFunction;
import inou.math.vector.Vector3D;
import inou.math.vector.Vector4D;

public class VolumeFunctionData4D extends VolumeData4D implements
        FunctionDataModel {

    protected ScalarFunction function;

    protected int div = 10, div2 = div * div;

    private Vector4D[] vertices;

    private RealSolid[] solids;

    // ==========================
    // contructor
    // ==========================

    /**
     * function data
     * 
     * @param fs
     *            input functions
     */
    public VolumeFunctionData4D(ScalarFunction sf) {
        setFunction(sf);
    }

    // ==========================
    // access method
    // ==========================

    public void setFunction(ScalarFunction sf) {
        function = sf;
    }

    public ScalarFunction getFunctions() {
        return function;
    }

    public RealRange getReferenceRange(RealRange referenceRange) {
        MathVector[] data = getArray(referenceRange);
        return UPlotData.getPartialRange(3, data);
    }

    public int[] getOutputDimensions() {
        return new int[] { 3 };
    }

    public void setDivision(int d) {
        if (d < 1) {
            return;
        }
        div = d;
        div2 = div * div;
    }

    public int getDivision() {
        return div;
    }

    // ==========================
    // operation
    // ==========================

    protected void updateData() {
        vertices = null;
        solids = null;
    }

    /** make vertices array */
    public MathVector[] getArray(RealRange activeRange) {
        if (activeRange == null)
            return null;

        if (vertices != null) {
            return vertices;
        }

        // prepare
        double dx = activeRange.width() / (div - 1), x;
        double dy = activeRange.height() / (div - 1), y;
        double dz = activeRange.length() / (div - 1), z;
        Vector4D[] rets = new Vector4D[div * div * div];
        Vector3D pos = new Vector3D();

        // make [div]*[div]*[div] matrics data array
        int div2 = div * div;
        boolean first = true;
        for (int k = 0; k < div; k++) {
            for (int j = 0; j < div; j++) {
                for (int i = 0; i < div; i++) {
                    pos.set(activeRange.x() + dx * j, activeRange.y() + dy * i,
                            activeRange.z() + dz * k);
                    double val = function.f(pos);
                    Vector4D csv = new Vector4D(pos.x, pos.y, pos.z, val);
                    rets[address(i, j, k)] = csv;
                }
            }
        }

        vertices = rets;
        return rets;
    }

    private int address(int x, int y, int z) {
        return x + y * div + div2 * z;
    }

    public RealSolid[] getSolids(MathVector[] vertexArray) {
        if (solids != null)
            return solids;
        initSolids();
        return solids;
    }

    private void initSolids() {
        int mdiv = div - 1;
        solids = new RealSolid[mdiv * mdiv * mdiv * 5];
        int count = 0;
        for (int k = 0; k < mdiv; k++) {
            for (int j = 0; j < mdiv; j++) {
                for (int i = 0; i < mdiv; i++) {
                    count = makeSolid(i, j, k, count);
                }
            }
        }
    }

    private int makeSolid(int ix, int iy, int iz, int count) {
        solids[count++] = new RealSolid(new int[] { address(ix, iy, iz),
                address(ix, iy + 1, iz), address(ix + 1, iy, iz),
                address(ix, iy, iz + 1) });
        solids[count++] = new RealSolid(new int[] {
                address(ix + 1, iy + 1, iz), address(ix, iy + 1, iz),
                address(ix + 1, iy, iz), address(ix + 1, iy + 1, iz + 1) });
        solids[count++] = new RealSolid(new int[] {
                address(ix + 1, iy, iz + 1), address(ix, iy, iz + 1),
                address(ix + 1, iy + 1, iz + 1), address(ix + 1, iy, iz) });
        solids[count++] = new RealSolid(new int[] {
                address(ix, iy + 1, iz + 1), address(ix, iy, iz + 1),
                address(ix + 1, iy + 1, iz + 1), address(ix, iy + 1, iz) });
        solids[count++] = new RealSolid(new int[] { address(ix, iy, iz + 1),
                address(ix + 1, iy + 1, iz + 1), address(ix + 1, iy, iz),
                address(ix, iy + 1, iz) });
        return count;
    }

}