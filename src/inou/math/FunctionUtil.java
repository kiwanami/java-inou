/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

import inou.math.exp.ScalarParameter;

/** This class provides functional utilities. */
public class FunctionUtil {

    /** make a function of constant value with 1D */
    public static ScalarFunction constant(double a) {
        if (a == 0)
            return new Zero(1);
        if (a == 1)
            return new Unit(1);
        return new Constant(a, 1);
    }

    /** make a function of constant value */
    public static ScalarFunction constant(double a, int d) {
        if (a == 0)
            return new Zero(d);
        if (a == 1)
            return new Unit(d);
        return new Constant(a, d);
    }

    /**
     * make a function of variable "x" with 1D
     */
    public static ScalarFunction variable() {
        return variable(0, 1, 1);
    }

    /**
     * make a function of variable "x" with 1D
     */
    public static ScalarFunction variable(int power) {
        return variable(0, 1, power);
    }

    /**
     * make a function of variable from column and dimension
     * 
     * @param col
     *            variable column
     * @param dim
     *            diension
     */
    public static ScalarFunction variable(int col, int dim) {
        return variable(col, dim, 1);
    }

    /**
     * make a function of variable from column and dimension
     * 
     * @param col
     *            variable column
     * @param dim
     *            diension
     * @param pow
     *            power
     */
    public static ScalarFunction variable(int col, int dim, int pow) {
        return new Variable(col, dim, pow);
    }

    public static ScalarFunction parameter(Parameter p, int d) {
        return new ScalarParameter(p, d);
    }

    /** make a linear connected function */
    public static ScalarFunction linearConnection(ScalarFunction a, double an,
            ScalarFunction b, double bn) {
        // zero check
        if (a instanceof Zero && b instanceof Zero)
            return new Zero(a.getDimension());
        if (an == 0 && bn == 0)
            return new Zero(a.getDimension());
        //
        if (a instanceof Zero || an == 0)
            return multiple(b, bn);
        if (b instanceof Zero || bn == 0)
            return multiple(a, an);
        // constant check
        if (a instanceof Constant && b instanceof Constant)
            return constant(an * a.f(null) + bn * b.f(null), a.getDimension());
        // variable check
        if (a instanceof Variable && b instanceof Variable) {
            Variable aa = (Variable) a;
            Variable bb = (Variable) b;
            if (aa.getColumn() == bb.getColumn()
                    && aa.getPower() == bb.getPower())
                return multiple(a, an + bn);
        }

        if (bn > 0)
            return add(multiple(a, an), multiple(b, bn));
        return sub(multiple(a, an), multiple(b, -bn));
    }

    /**
     * Add two functions. (a+b)
     */
    public static ScalarFunction add(ScalarFunction a, ScalarFunction b) {
        // zero check
        if (a instanceof Zero && b instanceof Zero)
            return new Zero(a.getDimension());
        //
        if (a instanceof Zero)
            return b;
        if (b instanceof Zero)
            return a;
        // constant check
        if (a instanceof Constant && b instanceof Constant)
            return constant(a.f(null) + b.f(null), a.getDimension());
        // variable check
        if (a instanceof Variable && b instanceof Variable) {
            Variable aa = (Variable) a;
            Variable bb = (Variable) b;
            if (aa.getColumn() == bb.getColumn()
                    && aa.getPower() == bb.getPower())
                return multiple(a, 2);
        }
        return new AddFunction(a, b);
    }

    /**
     * Add two functions. (a-b)
     */
    public static ScalarFunction sub(ScalarFunction a, ScalarFunction b) {
        // zero check
        if (a instanceof Zero && b instanceof Zero)
            return new Zero(a.getDimension());
        //
        if (a instanceof Zero)
            return multiple(b, -1);
        if (b instanceof Zero)
            return a;
        // constant check
        if (a instanceof Constant && b instanceof Constant)
            return constant(a.f(null) - b.f(null), a.getDimension());
        // variable check
        if (a instanceof Variable && b instanceof Variable) {
            Variable aa = (Variable) a;
            Variable bb = (Variable) b;
            if (aa.getColumn() == bb.getColumn()
                    && aa.getPower() == bb.getPower())
                return new Zero(aa.getDimension());
        }
        return new SubFunction(a, b);
    }

    /**
     * multiple functions by scalar value. (a*b)
     */
    public static ScalarFunction multiple(ScalarFunction a, double bn) {
        return multiple(a, constant(bn, a.getDimension()));
    }

    /**
     * multiple functions. (a*b)
     */
    public static ScalarFunction multiple(ScalarFunction a, ScalarFunction b) {
        // zero check
        if (a instanceof Zero || b instanceof Zero)
            return new Zero(a.getDimension());
        if (a instanceof Unit)
            return b;
        if (b instanceof Unit)
            return a;
        // constant check
        if (a instanceof Constant && b instanceof Constant)
            return constant(a.f(null) * b.f(null), a.getDimension());
        // variable check
        if (a instanceof Variable && b instanceof Variable) {
            Variable aa = (Variable) a;
            Variable bb = (Variable) b;
            if (aa.getColumn() == bb.getColumn())
                return variable(aa.getColumn(), aa.getDimension(), aa
                        .getPower()
                        + bb.getPower());
        }
        // reduction
        if (a instanceof BinaryFunction && b instanceof BinaryFunction) {
            ScalarReductionSet set = new ScalarReductionSet(null, 1);
            set = reduction(a, set);
            set = reduction(b, set);
            b = set.f;
            a = constant(set.c, a.getDimension());
        }
        // multiple
        return multiple_gen(a, b);
    }

    private static ScalarFunction multiple_gen(ScalarFunction a,
            ScalarFunction b) {
        return new MultipleFunction(a, b);
    }

    private static ScalarReductionSet reduction(ScalarFunction src,
            ScalarReductionSet set) {
        if (src instanceof MultipleFunction) {
            BinaryFunction aa = (BinaryFunction) src;
            ScalarFunction a1 = aa.getLeft();
            set = reduction(a1, set);
            ScalarFunction a2 = aa.getRight();
            set = reduction(a2, set);
            return set;
        }
        if (src instanceof Constant) {
            set.c *= ((Constant) src).f(null);
            return set;
        }
        // can not reduce
        if (set.f == null)
            set.f = src;
        else
            set.f = multiple_gen(set.f, src);
        return set;
    }

    /**
     * Divide a by b. (a/b)
     */
    public static ScalarFunction divide(ScalarFunction a, ScalarFunction b) {
        // dividing by zero
        if (b instanceof Zero)
            throw new ArithmeticException("Dividing by zero");
        // zero check
        if (a instanceof Zero)
            return new Zero(a.getDimension());
        if (b instanceof Unit)
            return a;
        // constant check
        if (a instanceof Constant && b instanceof Constant)
            return constant(a.f(null) / b.f(null), a.getDimension());
        // variable check
        if (a instanceof Variable && b instanceof Variable) {
            Variable aa = (Variable) a;
            Variable bb = (Variable) b;
            if (aa.getColumn() == bb.getColumn()) {
                if (aa.getPower() == bb.getColumn())
                    return new Unit(a.getDimension());
                return variable(aa.getColumn(), aa.getDimension(), aa
                        .getPower()
                        - bb.getPower());
            }
        }
        // dividing
        return new DivideFunction(a, b);
    }

    /**
     * power (a**b)
     */
    public static ScalarFunction power(ScalarFunction a, double b) {
        return power(a, constant(b, a.getDimension()));
    }

    /**
     * power (a^b)
     */
    public static ScalarFunction power(ScalarFunction a, ScalarFunction b) {
        // zero check
        if (b instanceof Zero)
            return new Unit(a.getDimension());
        // zero check
        if (a instanceof Zero)
            return new Zero(a.getDimension());
        // constant check
        if (a instanceof Constant && b instanceof Constant)
            return constant(Math.pow(a.f(null), b.f(null)), a.getDimension());
        // unit check
        if (a instanceof Unit)
            return new Unit(a.getDimension());
        if (b instanceof Unit)
            return a;
        // variable check
        if (a instanceof Variable && b instanceof Constant
                && !(b instanceof ScalarParameter)) {
            Variable aa = (Variable) a;
            Constant bb = (Constant) b;
            double c = aa.getPower() * bb.f(null);
            if (((int) c) == c)
                return variable(aa.getColumn(), aa.getDimension(), (int) c);
        }
        // power
        return new PowerFunction(a, b);
    }

    /**
     * return fundamental function, whichs need zero or one functional argument.
     * <br>
     * This method supports the same as functions of [getPrimitiveFunction]
     * 
     * @param name
     *            function name
     * @param in
     *            argument function
     * @return ScalarFunction object
     */
    public static ScalarFunction getPrimitiveFunctional(String name,
            ScalarFunction in) {
        PrimitiveFunction f = getPrimitiveFunction(name);
        if (f == null)
            return null;
        Functional a = new Functional(f, in);
        return a;
    }

    /**
     * return fundamental function, whichs need one argument. <br>
     * 
     * <pre>
     *  abs : absolute value
     *  ceil : ceil value ( |x|+1 )
     *  floor : floor value ( |x| )
     *  round : round up numbers of five and above and drop anything under five
     *  sin, cos, tan : trigonometric functions
     *  asin, acos, atan : inverse trigonometric functions
     *  sinh, cosh, tanh : hyperbolic trigonometric functions
     *  exp : exponential function
     *  ln : natural logarithm
     *  log : common logarithm
     *  sqrt : square root
     *  random : random number, equal and larger than 0, smaller than x
     * </pre>
     * 
     * @param name
     *            function name
     * @return AFunction object
     */
    public static PrimitiveFunction getPrimitiveFunction(String name) {
        PrimitiveFunction a = (PrimitiveFunction) table.get(name);
        if (a == null) {
            System.err.println("function not found : [" + name + "]");
        }
        return a;
    }

    /**
     * Add the given function to the internal primitive function table
     * 
     * @param in
     *            a function you want to add to the function table.
     * @param name
     *            name of the given function. (It should not have the same name
     *            as previous functions'.)
     */
    public static void addPrimitiveFunction(AFunction in, String name) {
        addPrimitiveFunction(new PrimitiveFunction(name, in));
    }

    /**
     * Add the given function to the internal primitive function table
     * 
     * @param in
     *            a PrimitiveFunction object you want to add to the function
     *            table. (It should not have the same name as previous
     *            functions'.)
     */
    public static void addPrimitiveFunction(PrimitiveFunction in) {
        if (table.get(in.getName()) != null) {
            System.err.println("Override the embedded function.["
                    + in.getName() + "]");
        }
        table.put(in.getName(), in);
    }

    /** function table */
    static java.util.Hashtable table = new java.util.Hashtable();
    static {
        PrimitiveFunction abs, ceil, floor, round, sin, cos, tan, asin, acos, atan, sinh, cosh, tanh, ln, log, exp, sqrt, random;
        abs = new PrimitiveFunction("abs", new AFunction() {
            public double f(double x) {
                return Math.abs(x);
            }
        }, null, null, null);
        table.put("abs", abs);
        ceil = new PrimitiveFunction("ceil", new AFunction() {
            public double f(double x) {
                return Math.ceil(x);
            }
        }, null, null, null);
        table.put("ceil", ceil);
        floor = new PrimitiveFunction("floor", new AFunction() {
            public double f(double x) {
                return Math.floor(x);
            }
        }, null, null, null);
        table.put("floor", floor);
        round = new PrimitiveFunction("round", new AFunction() {
            public double f(double x) {
                return Math.round(x);
            }
        }, null, null, null);
        table.put("round", round);
        sin = new PrimitiveFunction("sin", new AFunction() {
            public double f(double x) {
                return Math.sin(x);
            }
        }, null, null, null);
        table.put("sin", sin);
        cos = new PrimitiveFunction("cos", new AFunction() {
            public double f(double x) {
                return Math.cos(x);
            }
        }, null, null, null);
        table.put("cos", cos);
        tan = new PrimitiveFunction("tan", new AFunction() {
            public double f(double x) {
                return Math.tan(x);
            }
        }, null, null, null);
        table.put("tan", tan);
        asin = new PrimitiveFunction("asin", new AFunction() {
            public double f(double x) {
                return Math.asin(x);
            }
        }, null, null, new RealRange(-0.999999, 1.9999));
        table.put("asin", asin);
        acos = new PrimitiveFunction("acos", new AFunction() {
            public double f(double x) {
                return Math.acos(x);
            }
        }, null, null, new RealRange(-0.999999, 1.9999));
        table.put("acos", acos);
        atan = new PrimitiveFunction("atan", new AFunction() {
            public double f(double x) {
                return Math.atan(x);
            }
        }, null, null, null);
        table.put("atan", atan);
        sinh = new PrimitiveFunction("sinh", new AFunction() {
            public double f(double x) {
                return 0.5 * (Math.exp(x) - Math.exp(-x));
            }
        }, null, null, null);
        table.put("sinh", sinh);
        cosh = new PrimitiveFunction("cosh", new AFunction() {
            public double f(double x) {
                return 0.5 * (Math.exp(x) + Math.exp(-x));
            }
        }, null, null, null);
        table.put("cosh", cosh);
        tanh = new PrimitiveFunction("tanh", new AFunction() {
            public double f(double x) {
                return (Math.exp(x) - Math.exp(-x))
                        / (Math.exp(x) + Math.exp(-x));
            }
        }, null, null, null);
        table.put("tanh", tanh);
        ln = new PrimitiveFunction("ln", new AFunction() {
            public double f(double x) {
                return Math.log(x);
            }
        }, null, null, new RealRange(0, Double.POSITIVE_INFINITY));
        table.put("ln", ln);
        log = new PrimitiveFunction("log", new AFunction() {
            public double f(double x) {
                return Math.log(x) / Math.log(10);
            }
        }, null, null, new RealRange(0, Double.POSITIVE_INFINITY));
        table.put("log", log);
        exp = new PrimitiveFunction("exp", new AFunction() {
            public double f(double x) {
                return Math.exp(x);
            }
        }, null, null, null);
        table.put("exp", exp);
        sqrt = new PrimitiveFunction("sqrt", new AFunction() {
            public double f(double x) {
                return Math.sqrt(x);
            }
        }, null, null, new RealRange(0, Double.POSITIVE_INFINITY));
        table.put("sqrt", sqrt);
        random = new PrimitiveFunction("random", new AFunction() {
            public double f(double x) {
                return Math.random() * x;
            }
        }, null, null, null);
        table.put("random", random);
        //
        exp.setDerivedFunction(exp);
        exp.setIntegratedFunction(exp);
        ln.setDerivedFunction(power(variable(), -1));
        ln.setIntegratedFunction(multiple(variable(), sub(ln, constant(1))));
        log.setDerivedFunction(power(variable(), -1));
        log.setIntegratedFunction(multiple(variable(), sub(log, constant(1))));
        //
        sin.setDerivedFunction(cos);
        sin.setIntegratedFunction(multiple(cos, -1));
        cos.setDerivedFunction(multiple(sin, -1));
        cos.setIntegratedFunction(sin);
        tan.setDerivedFunction(power(cos, constant(-2)));
        tan.setIntegratedFunction(multiple(new Functional(ln, cos,
                new RealRange(-Math.PI / 2, Math.PI / 2)), -1));
        //
        sinh.setDerivedFunction(cosh);
        sinh.setIntegratedFunction(cosh);
        cosh.setDerivedFunction(sinh);
        cosh.setIntegratedFunction(sinh);
        tanh.setDerivedFunction(power(cosh, constant(-2)));
        tanh
                .setIntegratedFunction(multiple(new Functional(ln, cosh, null),
                        -1));
        //
        sqrt.setDerivedFunction(multiple(power(variable(), -0.5), 0.5));
        sqrt.setIntegratedFunction(multiple(power(variable(), 1.5), 2. / 3.));
        //
        asin.setDerivedFunction(divide(constant(1), new Functional(sqrt, sub(
                constant(1.), variable(2)), new RealRange(-1, 1))));
        asin.setIntegratedFunction(new IntegratedFunction(asin));
        acos.setDerivedFunction(divide(constant(-1), new Functional(sqrt, sub(
                constant(1.), variable(2)), new RealRange(-1, 1))));
        acos.setIntegratedFunction(new IntegratedFunction(acos));
        atan.setDerivedFunction(divide(constant(-1), add(constant(1),
                variable(2))));
        atan.setIntegratedFunction(new IntegratedFunction(atan));
        //
        random.setIntegratedFunction(multiple(variable(), 0.5));
    }

    /**
     * Make partial derived function
     * 
     * @param a
     *            some function
     * @param colm
     *            the column number to derive
     * @return partial derived function (if given function implements
     *         Differentiatable, return the derived function by using the method ,
     *         Differnetiatable.getDerivedFunction(). )
     */
    public static ScalarFunction getDerivedFunction(ScalarFunction a, int colm) {
        ScalarFunction ret = null;
        if (a instanceof Differentiatable) {
            ret = ((Differentiatable) a).getDerivedFunction(colm);
        }
        if (ret == null) {
            ret = new DerivedFunction(a, colm);
        }
        return ret;
    }

    public static ScalarFunction getIntegratedFunction(ScalarFunction a,
            int colm) {
        ScalarFunction ret = null;
        if (a instanceof Integratable) {
            ret = ((Integratable) a).getIntegratedFunction(colm);
        }
        if (ret == null) {
            ret = new IntegratedFunction(a, colm);
        }
        return ret;
    }

    /** using reduction */
    static class ScalarReductionSet {
        ScalarFunction f;

        double c;

        ScalarReductionSet(ScalarFunction f, double c) {
            this.f = f;
            this.c = c;
        }
    }

    // ===========================================

    /** binary funcitons */
    static abstract class BinaryFunction extends ScalarFunctionClass implements
            CalculationOrder {

        protected ScalarFunction a, b;

        protected int level;

        public BinaryFunction(ScalarFunction a, ScalarFunction b, int level) {
            super(a.getDimension());
            this.a = a;
            this.b = b;
            if (a.getDimension() != b.getDimension())
                System.err.println("Wrong dimensions of arguments.");
            this.level = level;
        }

        public int getLevel() {
            return level;
        }

        public ScalarFunction getLeft() {
            return a;
        }

        public ScalarFunction getRight() {
            return b;
        }

        public abstract String getOpString();

        private String getStringFunction(ScalarFunction scf) {
            String ret = scf.toString();
            if (scf instanceof CalculationOrder) {
                CalculationOrder bf = (CalculationOrder) scf;
                if (bf.getLevel() < getLevel()) {
                    ret = "(" + ret + ")";
                }
            }
            if (ret.trim().charAt(0) == '-') {
                ret = "(" + ret + ")";
            }
            return ret;
        }

        public String toString() {
            String left = getStringFunction(getLeft());
            String right = getStringFunction(getRight());
            return left + getOpString() + right;
        }
    }

    /**
     * Linear connection.
     */
    static class AddFunction extends BinaryFunction {

        /** a+b */
        public AddFunction(ScalarFunction a, ScalarFunction b) {
            super(a, b, CalculationOrder.ADD);
        }

        public double f(MathVector x) {
            return a.f(x) + b.f(x);
        }

        public ScalarFunction getDerivedFunction(int c) {
            return add(FunctionUtil.getDerivedFunction(a, c), FunctionUtil
                    .getDerivedFunction(b, c));
        }

        public String getOpString() {
            return "+";
        }
    }

    /**
     * Linear connection.
     */
    static class SubFunction extends BinaryFunction {

        /** a+b */
        public SubFunction(ScalarFunction a, ScalarFunction b) {
            super(a, b, CalculationOrder.SUB);
        }

        public double f(MathVector x) {
            return a.f(x) - b.f(x);
        }

        public ScalarFunction getDerivedFunction(int c) {
            return sub(FunctionUtil.getDerivedFunction(a, c), FunctionUtil
                    .getDerivedFunction(b, c));
        }

        public String getOpString() {
            return "-";
        }
    }

    static class MultipleFunction extends BinaryFunction {

        /** a * b */
        public MultipleFunction(ScalarFunction a, ScalarFunction b) {
            super(a, b, CalculationOrder.MULTIPLE);
        }

        public double f(MathVector x) {
            return a.f(x) * b.f(x);
        }

        public ScalarFunction getDerivedFunction(int c) {
            return FunctionUtil.add(FunctionUtil.multiple(FunctionUtil
                    .getDerivedFunction(a, c), b), FunctionUtil.multiple(
                    FunctionUtil.getDerivedFunction(b, c), a));
        }

        public String getOpString() {
            return "*";
        }
    }

    static class DivideFunction extends BinaryFunction {

        /** a/b */
        public DivideFunction(ScalarFunction a, ScalarFunction b) {
            super(a, b, CalculationOrder.DIVIDE);
        }

        public double f(MathVector x) {
            return a.f(x) / b.f(x);
        }

        public ScalarFunction getDerivedFunction(int c) {
            return FunctionUtil.multiple(FunctionUtil.sub(FunctionUtil
                    .multiple(FunctionUtil.getDerivedFunction(a, c), b),
                    FunctionUtil.multiple(
                            FunctionUtil.getDerivedFunction(b, c), a)),
                    FunctionUtil.power(b, constant(-2, a.getDimension())));
        }

        public String getOpString() {
            return "/";
        }
    }

    static class PowerFunction extends BinaryFunction {

        /** a ^ b */
        public PowerFunction(ScalarFunction a, ScalarFunction b) {
            super(a, b, CalculationOrder.POWER);
        }

        public double f(MathVector x) {
            return Math.pow(a.f(x), b.f(x));
        }

        public ScalarFunction getDerivedFunction(int c) {
            if (a instanceof Differentiatable && b instanceof Differentiatable) {
                Differentiatable aa = (Differentiatable) a;
                Differentiatable bb = (Differentiatable) b;
                return multiple(this, add(multiple(b, divide(aa
                        .getDerivedFunction(c), a)),
                        multiple(bb.getDerivedFunction(c),
                                getPrimitiveFunctional("ln", a))));
            }
            return new DerivedFunction(this, c);
        }

        public String getOpString() {
            return "**";
        }
    }

}
