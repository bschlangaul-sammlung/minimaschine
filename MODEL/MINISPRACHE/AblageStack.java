/*
 * Decompiled with CFR 0.150.
 */
package MODEL.MINISPRACHE;

import MODEL.MINISPRACHE.Ablage;
import MODEL.MINISPRACHE.AssemblerText;
import MODEL.MINISPRACHE.StackVerwaltung;

class AblageStack
extends Ablage {
    protected int offset;
    private boolean indirekt;

    AblageStack(StackVerwaltung stackVerwaltung, int n, boolean bl, boolean bl2) {
        super(stackVerwaltung, bl2);
        this.offset = n;
        this.indirekt = bl;
    }

    private String AdressteilGeben() {
        return (this.indirekt ? "@" : "") + (this.offset - this.stack.OffsetGeben()) + "(SP)";
    }

    @Override
    void Laden(AssemblerText assemblerText) {
        assemblerText.BefehlEintragen(null, "LOAD", this.AdressteilGeben());
    }

    @Override
    void Operation(AssemblerText assemblerText, String string) {
        assemblerText.BefehlEintragen(null, string, this.AdressteilGeben());
    }

    @Override
    void AdresseLaden(AssemblerText assemblerText, boolean bl) {
        assemblerText.BefehlEintragen(null, bl ? "ADD" : "LOAD", (this.indirekt ? "" : "$") + (this.offset - this.stack.OffsetGeben()) + "(SP)");
    }
}

