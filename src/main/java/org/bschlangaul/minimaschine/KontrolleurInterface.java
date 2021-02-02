package org.bschlangaul.minimaschine;

interface KontrolleurInterface {
  public void CpuHexaSetzen(boolean var1);

  public void CpuInvalidieren();

  public boolean Assemblieren(String var1, Editor var2);

  public boolean Übersetzen(String var1, Editor var2);

  public boolean AssemblertextZeigen(String var1, Editor var2);

  public void SpeicherLöschen();

  public void Ausführen();

  public void EinzelSchritt();

  public void MikroSchritt();

  public void ZurückSetzen();

  public void NeuAusführen();

  public void ÖffnenAusführen();

  public void ÖffnenAusführen(String var1);

  public void SchließenAusführen(Anzeige var1);

  public void FensterTitelÄndernWeitergeben(Anzeige var1);

  public void CpuFensterAuswählen();

  public void SpeicherFensterAuswählen();

  public void EinfacheDarstellungAnzeigen();

  public void DetailDarstellungAnzeigen();

  public void ErweiterungenEinschalten(boolean var1);

  public void ZeitschrankeSetzen(int var1);

  public void BeendenAusführen();
}
