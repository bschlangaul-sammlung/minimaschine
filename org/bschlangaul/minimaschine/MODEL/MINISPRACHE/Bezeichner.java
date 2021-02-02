package org.bschlangaul.minimaschine.MODEL.MINISPRACHE;

abstract class Bezeichner {
  private String name;

  Bezeichner(String string) {
    this.name = string;
  }

  String NamenGeben() {
    return this.name;
  }
}
