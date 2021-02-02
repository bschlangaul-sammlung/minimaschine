package org.bschlangaul.minimaschine.MODEL;

import java.util.ArrayList;

class Speicher implements SpeicherMeldungsErzeuger {
  private short[] speicher;
  private int geändert;
  private ArrayList<SpeicherBeobachter> beobachter = new ArrayList();
  private static Speicher derSpeicher = new Speicher();

  static Speicher SpeicherGeben() {
    return derSpeicher;
  }

  private Speicher() {
    this.speicher = new short[65536];
    this.SpeicherLöschen();
    this.geändert = -1;
  }

  @Override
  public void Registrieren(SpeicherBeobachter speicherBeobachter) {
    this.beobachter.add(speicherBeobachter);
  }

  @Override
  public void Abmelden(SpeicherBeobachter speicherBeobachter) {
    this.beobachter.remove(speicherBeobachter);
  }

  private void Melden() {
    for (SpeicherBeobachter speicherBeobachter : this.beobachter) {
      speicherBeobachter.SpeicherGeändertMelden(this.geändert);
    }
  }

  void SpeicherLöschen() {
    for (int i = 0; i < this.speicher.length; ++i) {
      this.speicher[i] = 0;
    }
    this.speicher[this.speicher.length - 1] = -1;
    this.speicher[this.speicher.length - 2] = -1;
    this.Melden();
  }

  void WortSetzen(int n, int n2) {
    if (n2 > 32767) {
      n2 -= 65536;
    }
    if (n < 0) {
      n += 65536;
    }
    this.speicher[n] = (short) n2;
    this.geändert = n;
    this.Melden();
  }

  int WortOhneVorzeichenGeben(int n) {
    int n2 = this.WortMitVorzeichenGeben(n);
    if (n2 < 0) {
      n2 += 65536;
    }
    return n2;
  }

  int WortMitVorzeichenGeben(int n) {
    if (n < 0) {
      n += 65536;
    }
    return this.speicher[n];
  }
}
