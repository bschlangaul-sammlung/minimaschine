package org.bschlangaul.minimaschine.MODEL.MINISPRACHE;

class Variable extends Bezeichner {
  private int länge;

  Variable(String string) {
    super(string);
    this.länge = -1;
  }

  Variable(String string, int n) {
    super(string);
    this.länge = n;
  }

  boolean IstFeld() {
    return this.länge > -1;
  }

  int FeldlängeGeben() {
    return this.länge;
  }
}
