package org.bschlangaul.minimaschine;

import org.bschlangaul.minimaschine.MODEL.Cpu;
import org.bschlangaul.minimaschine.MODEL.FehlerVerwaltung;
import org.bschlangaul.minimaschine.MODEL.MINISPRACHE.ParserEinfach;
import org.bschlangaul.minimaschine.MODEL.MINISPRACHE.ParserErweitert;
import org.bschlangaul.minimaschine.MODEL.SpeicherLesen;

class Kontrolleur implements KontrolleurInterface {
  private Cpu cpu;
  private Cpu cpuEinfach;
  private Cpu cpuDetail;
  private FensterVerwaltung verwaltung;
  private boolean erweitert;

  Kontrolleur(Cpu cpu, Cpu cpu2) {
    this.cpuEinfach = cpu;
    this.cpuDetail = cpu2;
    this.cpu = cpu2;
    this.verwaltung = null;
    this.erweitert = false;
  }

  void VerwaltungSetzen(FensterVerwaltung fensterVerwaltung) {
    this.verwaltung = fensterVerwaltung;
    fensterVerwaltung.CpuAnzeigeWählen(true, false);
  }

  @Override
  public void CpuHexaSetzen(boolean bl) {
    this.cpuEinfach.HexaSetzen(bl);
    this.cpuDetail.HexaSetzen(bl);
  }

  @Override
  public void CpuInvalidieren() {
    this.cpu.AnzeigeWiederholen();
  }

  @Override
  public boolean Assemblieren(String string, Editor editor) {
    FehlerVerwaltung fehlerVerwaltung = new FehlerVerwaltung();
    this.cpu.Assemblieren(string, fehlerVerwaltung);
    if (fehlerVerwaltung.FehlerAufgetreten()) {
      editor.FehlerAnzeigen(fehlerVerwaltung.FehlertextMelden(), fehlerVerwaltung.FehlerpositionMelden());
    } else {
      this.cpu.ZurückSetzen();
    }
    return !fehlerVerwaltung.FehlerAufgetreten();
  }

  @Override
  public boolean Übersetzen(String string, Editor editor) {
    FehlerVerwaltung fehlerVerwaltung = new FehlerVerwaltung();
    this.cpu.Übersetzen(string, fehlerVerwaltung);
    if (fehlerVerwaltung.FehlerAufgetreten()) {
      editor.FehlerAnzeigen(fehlerVerwaltung.FehlertextMelden(), fehlerVerwaltung.FehlerpositionMelden());
    } else {
      this.cpu.ZurückSetzen();
    }
    return !fehlerVerwaltung.FehlerAufgetreten();
  }

  @Override
  public boolean AssemblertextZeigen(String string, Editor editor) {
    FehlerVerwaltung fehlerVerwaltung = new FehlerVerwaltung();
    String string2 = this.erweitert ? new ParserErweitert(string, fehlerVerwaltung).Parse()
        : new ParserEinfach(string, fehlerVerwaltung).Parse();
    if (fehlerVerwaltung.FehlerAufgetreten()) {
      editor.FehlerAnzeigen(fehlerVerwaltung.FehlertextMelden(), fehlerVerwaltung.FehlerpositionMelden());
    } else {
      AssemblerAnzeige assemblerAnzeige = new AssemblerAnzeige(this, string2);
      this.verwaltung.EditorEintragen(assemblerAnzeige);
    }
    return !fehlerVerwaltung.FehlerAufgetreten();
  }

  @Override
  public void SpeicherLöschen() {
    SpeicherLesen.SpeicherLöschen();
  }

  @Override
  public void Ausführen() {
    this.cpu.Ausführen();
  }

  @Override
  public void EinzelSchritt() {
    this.cpu.Schritt();
  }

  @Override
  public void MikroSchritt() {
    this.cpu.MikroSchritt();
  }

  @Override
  public void ZurückSetzen() {
    this.cpu.ZurückSetzen();
  }

  @Override
  public void NeuAusführen() {
    Editor editor = new Editor(this);
    this.verwaltung.EditorEintragen(editor);
    editor.Aktivieren();
  }

  @Override
  public void ÖffnenAusführen() {
    Editor editor = new Editor(this);
    this.verwaltung.EditorEintragen(editor);
    editor.DateiLesen();
  }

  @Override
  public void ÖffnenAusführen(String string) {
    Editor editor = new Editor(this);
    this.verwaltung.EditorEintragen(editor);
    editor.DateiLesen(string);
  }

  @Override
  public void SchließenAusführen(Anzeige anzeige) {
    this.verwaltung.EditorAustragen(anzeige);
  }

  @Override
  public void FensterTitelÄndernWeitergeben(Anzeige anzeige) {
    this.verwaltung.EditorTitelÄndern(anzeige);
  }

  @Override
  public void CpuFensterAuswählen() {
    this.verwaltung.CpuFensterAuswählen();
  }

  @Override
  public void SpeicherFensterAuswählen() {
    this.verwaltung.SpeicherFensterAuswählen();
  }

  @Override
  public void EinfacheDarstellungAnzeigen() {
    this.cpu.Übertragen(this.cpuEinfach);
    this.cpu = this.cpuEinfach;
    this.verwaltung.CpuAnzeigeWählen(false, this.erweitert);
  }

  @Override
  public void DetailDarstellungAnzeigen() {
    this.cpu.Übertragen(this.cpuDetail);
    this.cpu = this.cpuDetail;
    this.verwaltung.CpuAnzeigeWählen(true, this.erweitert);
  }

  @Override
  public void ErweiterungenEinschalten(boolean bl) {
    this.erweitert = bl;
    this.cpu.ErweitertSetzen(bl);
    this.cpu.ZurückSetzen();
    this.verwaltung.CpuAnzeigeWählen(this.cpu == this.cpuDetail, this.erweitert);
  }

  @Override
  public void ZeitschrankeSetzen(int n) {
    this.cpu.AbbruchSchrankeSetzen(n);
  }

  @Override
  public void BeendenAusführen() {
    this.verwaltung.BeendenMitteilen();
    System.exit(0);
  }
}
