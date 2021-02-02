package org.bschlangaul.minimaschine.MODEL.MINISPRACHE;

class AblageKonstante extends Ablage {
  private int wert;

  AblageKonstante(int n) {
    super(null, false);
    this.wert = n;
  }

  @Override
  void Laden(AssemblerText assemblerText) {
    assemblerText.BefehlEintragen(null, "LOADI", "" + this.wert);
  }

  @Override
  void Operation(AssemblerText assemblerText, String string) {
    assemblerText.BefehlEintragen(null, string + "I", "" + this.wert);
  }

  @Override
  void AdresseLaden(AssemblerText assemblerText, boolean bl) {
    assemblerText.BefehlEintragen(null, "ILL", "");
  }
}
