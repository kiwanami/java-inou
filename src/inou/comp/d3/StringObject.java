/*
 * INOU, Integrated Numerical Operation Utility
 * Copyright (C) 2005 SAKURAI, Masashi (m.sakurai@kiwanami.net)
 */

package inou.comp.d3;

import inou.comp.RichString;
import inou.math.vector.VectorQD;

public class StringObject extends GeometricObject {

    private RichString text;

    public StringObject(VectorQD position, RichString text) {
        super(new VectorQD[] { position });
        this.text = text;
    }

    public void setText(String content) {
        if (text != null) {
            text.setContent(content);
        }
    }

    public void setText(RichString text) {
        this.text = text;
    }

    public RichString getText() {
        return text;
    }

}
