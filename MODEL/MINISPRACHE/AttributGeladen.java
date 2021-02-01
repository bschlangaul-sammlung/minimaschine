/*
 * Decompiled with CFR 0.150.
 */
package MODEL.MINISPRACHE;

import MODEL.MINISPRACHE.AssemblerText;
import MODEL.MINISPRACHE.Attribut;

class AttributGeladen
extends Attribut {
    AttributGeladen() {
    }

    @Override
    void Laden(AssemblerText assemblerText) {
    }

    @Override
    void Operation(AssemblerText assemblerText, String string) {
        assemblerText.BefehlEintragen(null, string + "ill", null);
    }
}

