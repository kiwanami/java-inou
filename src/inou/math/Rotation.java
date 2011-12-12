/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

import inou.math.dif.SimpleDifferentiator;
import inou.math.vector.VectorGD;

import java.io.Serializable;

/** Rotation operator for VectorFunction */
public class Rotation implements VectorFunction, Serializable {

    private VectorFunction orginalFunction;

    private ScalarFunction[] scalars;

    private Differentiator diff = new SimpleDifferentiator();

    private ScalarFunction[] elements;

    /** Construct rotation operator */
    public Rotation(VectorFunction vf) {
        orginalFunction = vf;
        int dim = vf.getDimension();

        scalars = new ScalarFunction[dim];
        for (int i = 0; i < dim; i++)
            scalars[i] = VectorFunctionClass.makeScalarFunction(vf, i);

        elements = new ScalarFunction[dim];

        if (getDimension() <= 1) {
            // nothing
            System.out.println("too low dimension [ROT]");
        }

        if (getDimension() == 2) {
            elements[0] = new RotElement(1, 0, 0, 1);
        }

        if (getDimension() == 3) {
            elements[0] = new RotElement(2, 1, 1, 2);
            elements[1] = new RotElement(0, 2, 2, 0);
            elements[2] = new RotElement(1, 0, 0, 1);
        }
    }

    public MathVector f(MathVector a) {
        // not smart method...
        MathVector ret = new VectorGD(getDimension());
        for (int i = 0; i < getDimension(); i++) {
            if (elements[i] == null)
                break;
            ret.v(i, elements[i].f(a));
        }
        return ret;
    }

    public ScalarFunction getFunction(int e) {
        return elements[e];
    }

    public int getDimension() {
        int dim = orginalFunction.getDimension();
        if (dim < 4)
            return dim;
        throw new ArithmeticException(
                "not supported [ROT] more than 4 dimension.");
    }

    public int getArgDimension() {
        return getDimension();
    }

    class RotElement implements ScalarFunction {
        int f1, f2, r1, r2;

        RotElement(int f1, int f2, int r1, int r2) {
            this.f1 = f1;
            this.f2 = f2;
            this.r1 = r1;
            this.r2 = r2;
        }

        public int getDimension() {
            return Rotation.this.getDimension();
        }

        public double f(MathVector a) {
            return diff.point(a, r1).operate(scalars[f1])
                    - diff.point(a, r2).operate(scalars[f2]);
        }
    }

}
