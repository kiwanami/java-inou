/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.logical;

import inou.math.FunctionUtil;
import inou.math.MathVector;
import inou.math.ParameterMaker;
import inou.math.ScalarFunction;

public class ULogical {

    public static ScalarFunction logic2func(Condition condition, int dimension) {
        return new ConditionFunction(condition, dimension);
    }

    public static final Condition TRUE = new Condition() {
        public boolean accept(MathVector arg) {
            return true;
        }
    };

    public static final Condition FALSE = new Condition() {
        public boolean accept(MathVector arg) {
            return false;
        }
    };

    public static Condition or(Condition left, Condition right) {
        return new OROperator(left, right);
    }

    public static Condition and(Condition left, Condition right) {
        return new ANDOperator(left, right);
    }

    public static Condition not(Condition c) {
        return new NotOperator(c);
    }

    public static Condition xor(Condition left, Condition right) {
        return new XOROperator(left, right);
    }

    public static Condition greaterThan(ScalarFunction left,
            ScalarFunction right) {
        return new GreaterThan(left, right);
    }

    public static Condition greaterEqual(ScalarFunction left,
            ScalarFunction right) {
        return new GreaterEqual(left, right);
    }

    public static Condition lessThan(ScalarFunction left, ScalarFunction right) {
        return new LessThan(left, right);
    }

    public static Condition lessEqual(ScalarFunction left, ScalarFunction right) {
        return new LessEqual(left, right);
    }

    public static Condition equal(ScalarFunction left, ScalarFunction right) {
        return new Equal(left, right);
    }

    public static Condition string2logic(String exp) {
        String var = "x";
        if (exp.indexOf("y") != -1)
            var += " y";
        if (exp.indexOf("z") != -1)
            var += " z";
        String[] vars = var.split(" ");
        return string2logic(exp, vars);
    }

    public static Condition string2logic(String exp, String[] var) {
        return string2logic(exp, var, null);
    }

    public static Condition string2logic(String exp, String[] var,
            ParameterMaker pm) {
        LogicalExpression parser = new LogicalExpression(exp, var, pm);
        return parser.getCondition();
    }

    public static void main(String[] args) {
        testNot();
        testBinary();
        testIneq();
    }

    private static void assertTest(boolean ok, String mes) {
        if (!ok)
            System.out.println("Fault : " + mes);
    }

    private static void testNot() {
        System.out.println("not operator");
        assertTest(not(TRUE).accept(null) == false, "not false");
        assertTest(not(FALSE).accept(null) == true, "not true");
    }

    private static void testBinary() {
        System.out.println("Binary operator");
        Condition[] leftArray = { TRUE, TRUE, FALSE, FALSE, };
        Condition[] rightArray = { TRUE, FALSE, TRUE, FALSE, };
        BinaryOperator[] ops = { new ANDOperator(null, null),
                new OROperator(null, null), new XOROperator(null, null), };
        boolean[][] answers = { { true, false, false, false, },// and
                { true, true, true, false, },// or
                { false, true, true, false, },// xor
        };
        for (int j = 0; j < ops.length; j++) {
            for (int i = 0; i < leftArray.length; i++) {
                ops[j].setLeft(leftArray[i]);
                ops[j].setRight(rightArray[i]);
                assertTest(ops[j].accept(null) == answers[j][i], j + " : " + i);
            }
        }
    }

    private static void testIneq() {
        System.out.println("Inequality");
        ScalarFunction[] lefts = { FunctionUtil.constant(1),
                FunctionUtil.constant(0), FunctionUtil.constant(-1), };
        Relation[] ops = { new GreaterThan(null, null),
                new GreaterEqual(null, null), new LessThan(null, null),
                new LessEqual(null, null), new Equal(null, null), };
        boolean[][] answers = { { true, false, false }, { true, true, false },
                { false, false, true }, { false, true, true },
                { false, true, false }, };
        for (int j = 0; j < ops.length; j++) {
            for (int i = 0; i < lefts.length; i++) {
                ops[j].setLeft(lefts[i]);
                assertTest(ops[j].accept(null) == answers[j][i], j + " : " + i);
            }
        }
    }
}
