package org.bschlangaul.minimaschine.MODEL;

public abstract class SpeicherLesen {
  public static int WortOhneVorzeichenGeben(int n) {
    return Speicher.SpeicherGeben().WortOhneVorzeichenGeben(n);
  }

  public static int WortMitVorzeichenGeben(int n) {
    return Speicher.SpeicherGeben().WortMitVorzeichenGeben(n);
  }

  public static void WortSetzen(int n, int n2) {
    Speicher.SpeicherGeben().WortSetzen(n, n2);
  }

  public static void SpeicherLöschen() {
    Speicher.SpeicherGeben().SpeicherLöschen();
  }
}
