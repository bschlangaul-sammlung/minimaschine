package org.bschlangaul.minimaschine.MODEL.MINISPRACHE;

class Scanner {
  static final int eof = 0;
  static final int illegal = 1;
  static final int bezeichner = 2;
  static final int zahl = 3;
  static final int program_token = 10;
  static final int begin_token = 11;
  static final int end_token = 12;
  static final int var_token = 13;
  static final int if_token = 14;
  static final int then_token = 15;
  static final int else_token = 16;
  static final int while_token = 17;
  static final int do_token = 18;
  static final int repeat_token = 19;
  static final int until_token = 20;
  static final int for_token = 21;
  static final int to_token = 22;
  static final int by_token = 23;
  static final int strichpunkt = 30;
  static final int punkt = 31;
  static final int komma = 32;
  static final int zuweisung = 33;
  static final int plus = 34;
  static final int minus = 35;
  static final int mal = 36;
  static final int geteilt = 37;
  static final int rest = 38;
  static final int gleich = 39;
  static final int ungleich = 40;
  static final int kleiner = 41;
  static final int kleinergleich = 42;
  static final int größer = 43;
  static final int größergleich = 44;
  static final int klammerauf = 45;
  static final int klammerzu = 46;
  static final int eckigeauf = 100;
  static final int eckigezu = 101;
  static final int proc_token = 102;
  static final int func_token = 103;
  static final int return_token = 104;
  private char[] quelle;
  private int pos;
  private int zeile;
  private char ch;
  private int zahlenwert;
  private String name;
  private AssemblerText ausgabe;
  private boolean erweitert;

  Scanner(String string, boolean bl, AssemblerText assemblerText) {
    this.quelle = string.toCharArray();
    this.pos = 0;
    this.zeile = 1;
    this.erweitert = bl;
    this.ausgabe = assemblerText;
    this.NächstesZeichen();
  }

  private void NächstesZeichen() {
    if (this.pos < this.quelle.length) {
      this.ch = this.quelle[this.pos++];
      if (this.ch == '\n') {
        ++this.zeile;
        this.ausgabe.BefehlEintragen("#Zeile " + this.zeile, null, null);
      }
    } else {
      this.ch = '\u0000';
    }
  }

  private int Bezeichner() {
    int n = this.pos - 1;
    int n2 = 0;
    while ('a' <= this.ch && this.ch <= 'z' || 'A' <= this.ch && this.ch <= 'Z' || '0' <= this.ch && this.ch <= '9'
        || this.ch == '_' || this.ch == '$') {
      this.NächstesZeichen();
      ++n2;
    }
    this.name = new String(this.quelle, n, n2);
    TextToken textToken = TextToken.TokenlisteGeben();
    if (textToken.BezeichnerTesten(this.name, this.erweitert)) {
      return textToken.TokenwertGeben(this.name);
    }
    return 2;
  }

  /*
   * Unable to fully structure code Enabled aggressive block sorting Lifted jumps
   * to return sites
   */
  private void Kommentar() {

    do {
      do {
        NächstesZeichen();
      } while (ch != '*');

      NächstesZeichen();
    } while (ch != ')');

    NächstesZeichen();

  }

  private void Zahl() {
    this.zahlenwert = 0;
    while ('0' <= this.ch && this.ch <= '9') {
      this.zahlenwert = this.zahlenwert * 10 + Character.digit(this.ch, 10);
      this.NächstesZeichen();
    }
  }

  int NächstesToken() {
    while (this.ch == ' ' || this.ch == '\t' || this.ch == '\r' || this.ch == '\n') {
      this.NächstesZeichen();
    }
    if (this.ch == '\u0000') {
      this.NächstesZeichen();
      return 0;
    }
    if (this.ch == ';') {
      this.NächstesZeichen();
      return 30;
    }
    if (this.ch == '.') {
      this.NächstesZeichen();
      return 31;
    }
    if (this.ch == ',') {
      this.NächstesZeichen();
      return 32;
    }
    if (this.ch == ':') {
      this.NächstesZeichen();
      if (this.ch == '=') {
        this.NächstesZeichen();
        return 33;
      }
      return 1;
    }
    if (this.ch == '+') {
      this.NächstesZeichen();
      return 34;
    }
    if (this.ch == '-') {
      this.NächstesZeichen();
      return 35;
    }
    if (this.ch == '*') {
      this.NächstesZeichen();
      return 36;
    }
    if (this.ch == '/') {
      this.NächstesZeichen();
      return 37;
    }
    if (this.ch == '%') {
      this.NächstesZeichen();
      return 38;
    }
    if (this.ch == '=') {
      this.NächstesZeichen();
      return 39;
    }
    if (this.ch == '<') {
      this.NächstesZeichen();
      if (this.ch == '=') {
        this.NächstesZeichen();
        return 42;
      }
      if (this.ch == '>') {
        this.NächstesZeichen();
        return 40;
      }
      return 41;
    }
    if (this.ch == '>') {
      this.NächstesZeichen();
      if (this.ch == '=') {
        this.NächstesZeichen();
        return 44;
      }
      return 43;
    }
    if (this.ch == '(') {
      this.NächstesZeichen();
      if (this.ch == '*') {
        this.Kommentar();
        return this.NächstesToken();
      }
      return 45;
    }
    if (this.ch == ')') {
      this.NächstesZeichen();
      return 46;
    }
    if (this.ch == '[') {
      this.NächstesZeichen();
      return this.erweitert ? 100 : 1;
    }
    if (this.ch == ']') {
      this.NächstesZeichen();
      return this.erweitert ? 101 : 1;
    }
    if ('0' <= this.ch && this.ch <= '9') {
      this.Zahl();
      return 3;
    }
    if ('a' <= this.ch && this.ch <= 'z' || 'A' <= this.ch && this.ch <= 'Z' || this.ch == '_' || this.ch == '$') {
      return this.Bezeichner();
    }
    return 1;
  }

  String BezeichnerGeben() {
    return this.name;
  }

  int ZahlGeben() {
    return this.zahlenwert;
  }

  int PositionGeben() {
    return this.pos;
  }

  int ZeileGeben() {
    return this.zeile;
  }
}
