/*
 * Decompiled with CFR 0.150.
 */
package MODEL.MINISPRACHE;

import MODEL.MINISPRACHE.AblageStack;
import MODEL.MINISPRACHE.AssemblerText;
import MODEL.MINISPRACHE.StackVerwaltung;

class AblageStackHilf
extends AblageStack {
    AblageStackHilf(StackVerwaltung stackVerwaltung, boolean bl, boolean bl2) {
        super(stackVerwaltung, stackVerwaltung.OffsetFürNeueVariableGeben(1), bl, bl2);
    }

    @Override
    void AblageFreigeben(AssemblerText assemblerText) {
        assemblerText.BefehlEintragen(null, "REL", "$1");
        this.stack.Inkrementieren();
    }
}
