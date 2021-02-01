/*
 * Decompiled with CFR 0.150.
 */
package MODEL.MINISPRACHE;

import MODEL.MINISPRACHE.AssemblerText;

abstract class Attribut {
    Attribut() {
    }

    abstract void Laden(AssemblerText var1);

    abstract void Operation(AssemblerText var1, String var2);
}

