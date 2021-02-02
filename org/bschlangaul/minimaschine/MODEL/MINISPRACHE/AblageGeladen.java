package org.bschlangaul.minimaschine.MODEL.MINISPRACHE;

class AblageGeladen extends Ablage {
  AblageGeladen() {
    super(null, false);
  }

  @Override
  void Laden(AssemblerText assemblerText) {
  }

  @Override
  void Operation(AssemblerText assemblerText, String string) {
    assemblerText.BefehlEintragen(null, string + "ill", null);
  }

  @Override
  void AdresseLaden(AssemblerText assemblerText, boolean bl) {
    assemblerText.BefehlEintragen(null, "ill", null);
  }
}
