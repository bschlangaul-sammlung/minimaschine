package org.bschlangaul.minimaschine.MODEL;

public interface CpuMeldungsErzeuger {
    public void Registrieren(CpuBeobachter var1);

    public void Abmelden(CpuBeobachter var1);
}
