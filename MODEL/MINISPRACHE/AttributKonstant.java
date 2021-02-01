/*
 * Decompiled with CFR 0.150.
 */
package MODEL.MINISPRACHE;

import MODEL.MINISPRACHE.AssemblerText;
import MODEL.MINISPRACHE.Attribut;

class AttributKonstant
extends Attribut {
    private int wert;

    AttributKonstant(int n) {
        this.wert = n;
    }

    @Override
    void Laden(AssemblerText assemblerText) {
        assemblerText.BefehlEintragen(null, "LOADI", "" + this.wert);
    }

    @Override
    void Operation(AssemblerText assemblerText, String string) {
        assemblerText.BefehlEintragen(null, string + "I", "" + this.wert);
    }
}

