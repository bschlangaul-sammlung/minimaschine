package org.bschlangaul.minimaschine.MODEL;

interface SpeicherMeldungsErzeuger {
    public void Registrieren(SpeicherBeobachter var1);

    public void Abmelden(SpeicherBeobachter var1);
}
