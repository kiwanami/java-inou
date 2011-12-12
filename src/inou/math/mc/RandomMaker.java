/*
 * INOU, Integrated Numerical Operation Utility 
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.math.mc;

/**
 * This class makes a random data. Override this class to make your custom
 * random data.
 */

public interface RandomMaker {

    /**
     * make your custom random data.
     * 
     * @param lastData
     *            radomdata at last time.
     * @return your new random data
     */
    public RandomData makeRandom(RandomData lastData);

}