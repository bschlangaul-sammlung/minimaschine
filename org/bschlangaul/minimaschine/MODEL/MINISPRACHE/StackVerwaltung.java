package org.bschlangaul.minimaschine.MODEL.MINISPRACHE;

class StackVerwaltung {
  private int offset = 0;

  StackVerwaltung() {
  }

  void Dekrementieren() {
    --this.offset;
  }

  void Inkrementieren() {
    ++this.offset;
  }

  void Dekrementieren(int n) {
    this.offset -= n;
  }

  void Inkrementieren(int n) {
    this.offset += n;
  }

  int OffsetGeben() {
    return this.offset;
  }

  int OffsetFürNeueVariableGeben(int n) {
    this.offset -= n;
    return this.offset;
  }
}
