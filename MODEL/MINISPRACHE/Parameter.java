/*
 * Decompiled with CFR 0.150.
 */
package MODEL.MINISPRACHE;

import MODEL.MINISPRACHE.LokaleVariable;

class Parameter
extends LokaleVariable {
    private boolean istVarParamter;

    Parameter(String string, boolean bl) {
        super(string, 0);
        this.istVarParamter = bl;
    }

    Parameter(String string, int n, boolean bl) {
        super(string, n, 0);
        this.istVarParamter = bl;
    }

    boolean IstVarParam() {
        return this.istVarParamter;
    }

    void OffsetSetzen(int n) {
        this.offset = n;
    }
}

