/*
 * Decompiled with CFR 0.150.
 */
package MODEL.MINISPRACHE;

import MODEL.MINISPRACHE.Variable;

class LokaleVariable
extends Variable {
    protected int offset;

    LokaleVariable(String string, int n) {
        super(string);
        this.offset = n;
    }

    LokaleVariable(String string, int n, int n2) {
        super(string, n);
        this.offset = n2;
    }

    int OffsetGeben() {
        return this.offset;
    }
}

