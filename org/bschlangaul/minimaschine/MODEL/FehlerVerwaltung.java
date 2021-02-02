package org.bschlangaul.minimaschine.MODEL;

import java.util.ArrayList;

public class FehlerVerwaltung {
    private ArrayList<String> meldungen = new ArrayList();
    private ArrayList<Integer> positionen = new ArrayList();

    public void FehlerEintragen(String string, int n) {
        this.meldungen.add(string);
        this.positionen.add(n);
    }

    public boolean FehlerAufgetreten() {
        return this.meldungen.size() != 0;
    }

    public String FehlertextMelden() {
        return this.meldungen.get(0);
    }

    public int FehlerpositionMelden() {
        return this.positionen.get(0);
    }
}
