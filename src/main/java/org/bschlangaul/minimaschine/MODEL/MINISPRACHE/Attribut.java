package org.bschlangaul.minimaschine.MODEL.MINISPRACHE;

abstract class Attribut {
  Attribut() {
  }

  abstract void Laden(AssemblerText var1);

  abstract void Operation(AssemblerText var1, String var2);
}
