package org.bschlangaul.minimaschine.MODEL.MINISPRACHE;

class AttributGeladen extends Attribut {
  AttributGeladen() {
  }

  @Override
  void Laden(AssemblerText assemblerText) {
  }

  @Override
  void Operation(AssemblerText assemblerText, String string) {
    assemblerText.BefehlEintragen(null, string + "ill", null);
  }
}
