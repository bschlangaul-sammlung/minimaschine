/*
 * Decompiled with CFR 0.150.
 */
package MODEL.MINISPRACHE;

import MODEL.MINISPRACHE.AssemblerText;
import MODEL.MINISPRACHE.Attribut;

class AttributVariable
extends Attribut {
    private String name;

    AttributVariable(String string) {
        this.name = string;
    }

    @Override
    void Laden(AssemblerText assemblerText) {
        assemblerText.BefehlEintragen(null, "LOAD", this.name);
    }

    @Override
    void Operation(AssemblerText assemblerText, String string) {
        assemblerText.BefehlEintragen(null, string, this.name);
    }
}

