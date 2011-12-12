/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.ngraph;

import inou.math.vector.Vector2D;

public class AdditiveData2D extends AbstractFixedData2D {

    private int bufferSize = 0;

    private Vector2D[] buffer; // first buffer, buffer[currentPos] <= given
                                // data

    private Vector2D[] dataSet; // sorted buffer

    private int currentPos;

    public AdditiveData2D() {
        this(200);
    }

    public AdditiveData2D(int bufferSize) {
        setBufferSize(bufferSize);
        currentPos = 0;
        init(dataSet);
    }

    public void resetBuffer() {
        buffer = null;
        currentPos = 0;
        for (int i = 0; i < bufferSize; i++) {
            dataSet[i].x = 0;
            dataSet[i].y = 0;
        }
        init(dataSet);
    }

    public void initPosition(double x, double value) {
        for (int i = 0; i < bufferSize; i++) {
            buffer[i].set(x, value);
        }
        updateData();
    }

    public void setBufferSize(int size) {
        bufferSize = size;
        buffer = null;
        currentPos = 0;
        dataSet = new Vector2D[bufferSize];
        for (int i = 0; i < bufferSize; i++) {
            dataSet[i] = new Vector2D();
        }
        init(dataSet);
    }

    private void initBuffer(double x, double value) {
        buffer = new Vector2D[bufferSize];
        for (int i = 0; i < bufferSize; i++) {
            buffer[i] = new Vector2D(x, value);
        }
    }

    public void add(double x, double value) {
        if (buffer == null) {
            initBuffer(x, value);
        } else {
            buffer[currentPos].set(x, value);
        }
        for (int i = 0; i < bufferSize; i++) {
            int idx = ((i + currentPos + 1) % bufferSize);
            dataSet[i] = buffer[idx];
        }
        currentPos++;
        if (currentPos >= bufferSize) {
            currentPos = 0;
        }
    }

}