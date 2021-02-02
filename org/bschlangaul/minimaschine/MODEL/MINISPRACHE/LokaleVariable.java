package org.bschlangaul.minimaschine.MODEL.MINISPRACHE;

class LokaleVariable extends Variable {
  protected int offset;

  LokaleVariable(String string, int n) {
    super(string);
    this.offset = n;
  }

  LokaleVariable(String string, int n, int n2) {
    super(string, n);
    this.offset = n2;
  }

  int OffsetGeben() {
    return this.offset;
  }
}
