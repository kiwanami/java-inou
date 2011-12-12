/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math;

/**
 * This interface defines the regular range. All function that does not
 * implement this interface is treat as the function defined within whole range.
 */

public interface FiniteRange {

    /**
     * return defined range. if return null, one can treat defined within whole
     * range.
     */
    public RealRange getDefinedRange();
}