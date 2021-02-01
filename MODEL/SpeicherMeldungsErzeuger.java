/*
 * Decompiled with CFR 0.150.
 */
package MODEL;

import MODEL.SpeicherBeobachter;

interface SpeicherMeldungsErzeuger {
    public void Registrieren(SpeicherBeobachter var1);

    public void Abmelden(SpeicherBeobachter var1);
}

