/*
 * Decompiled with CFR 0.150.
 */
package MODEL;

import MODEL.CpuBeobachter;

public interface CpuMeldungsErzeuger {
    public void Registrieren(CpuBeobachter var1);

    public void Abmelden(CpuBeobachter var1);
}

