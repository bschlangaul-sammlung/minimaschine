package org.bschlangaul.minimaschine.MODEL;

import org.bschlangaul.minimaschine.MODEL.MINISPRACHE.ParserEinfach;
import org.bschlangaul.minimaschine.MODEL.MINISPRACHE.ParserErweitert;

import java.util.ArrayList;

public abstract class Cpu implements CpuMeldungsErzeuger {
  static final int noop = 0;
  static final int reset = 1;
  static final int jsr = 5;
  static final int rts = 6;
  static final int rsv = 7;
  static final int rel = 8;
  static final int add = 10;
  static final int sub = 11;
  static final int mul = 12;
  static final int div = 13;
  static final int mod = 14;
  static final int cmp = 15;
  static final int load = 20;
  static final int store = 21;
  static final int push = 25;
  static final int pop = 26;
  static final int jgt = 30;
  static final int jge = 31;
  static final int jlt = 32;
  static final int jle = 33;
  static final int jeq = 34;
  static final int jne = 35;
  static final int jmp = 36;
  static final int jov = 37;
  static final int and = 40;
  static final int or = 41;
  static final int xor = 42;
  static final int shl = 43;
  static final int shr = 44;
  static final int shra = 45;
  static final int not = 46;
  static final int halt = 99;
  static final int ill = -1;
  public static final int keineadr = 0;
  public static final int absadr = 1;
  public static final int immedadr = 2;
  public static final int indadr = 3;
  public static final int offsetadr = 4;
  public static final int offsetindadr = 5;
  public static final int offsetimmedadr = 6;
  protected Register pc;
  protected Register a;
  protected Register sp;
  protected boolean ltflag;
  protected boolean eqflag;
  protected boolean ovflag;
  protected Speicher speicher;
  protected AssemblerBefehle mnemos;
  private ArrayList<CpuBeobachter> beobachter;
  protected int befehlscode;
  protected int adressmodus;
  protected int adresse;
  private String[] progadr = new String[4];
  private String[] progmem = new String[4];
  private String[] dataadr = new String[2];
  private String[] datamem = new String[2];
  private String[] stackadr = new String[3];
  private String[] stackmem = new String[3];
  private int progAdrAlt;
  private int dataAdrAlt;
  private int stackAdrAlt;
  private long schranke;
  private boolean hexaAnzeige;
  private String datenWert_letzter;
  private String adressWert_letzter;
  private String alu1_letzter;
  private String alu2_letzter;
  private String alu3_letzter;
  private String mikro_letzter;
  private boolean opMnemo_letzter;
  private boolean letzterDa;
  protected boolean erweitert;

  Cpu(Speicher speicher) {
    this.speicher = speicher;
    this.pc = new Register();
    this.a = new Register();
    this.sp = new Register();
    this.sp.WertSetzen(-2);
    this.mnemos = AssemblerBefehle.AssemblerbefehleGeben();
    this.beobachter = new ArrayList();
    this.ZurückSetzen();
    this.schranke = 5L;
    this.hexaAnzeige = false;
    this.letzterDa = false;
    this.erweitert = true;
  }

  @Override
  public void Registrieren(CpuBeobachter cpuBeobachter) {
    this.beobachter.add(cpuBeobachter);
  }

  @Override
  public void Abmelden(CpuBeobachter cpuBeobachter) {
    this.beobachter.remove(cpuBeobachter);
  }

  public void HexaSetzen(boolean bl) {
    this.hexaAnzeige = bl;
  }

  public void ErweitertSetzen(boolean bl) {
    this.erweitert = bl;
  }

  private String HexaString(String string) {
    if (string.length() > 0 && !" ".equals(string)) {
      int n = Integer.parseInt(string);
      if (n < 0) {
        n = 65536 + n;
      }
      string = "0000" + Integer.toHexString(n);
      string = string.substring(string.length() - 4);
    }
    return string;
  }

  protected void Melden(String string, String string2, String string3, String string4, String string5, boolean bl,
      int n, int n2, int n3, String string6) {
    int n4;
    this.letzterDa = true;
    this.datenWert_letzter = string;
    this.adressWert_letzter = string2;
    this.alu1_letzter = string3;
    this.alu2_letzter = string4;
    this.alu3_letzter = string5;
    this.mikro_letzter = string6;
    this.opMnemo_letzter = bl;
    String string7 = "" + this.a.WertGeben();
    String string8 = "" + this.adresse;
    String string9 = "" + (this.befehlscode + this.adressmodus * 256);
    if (this.hexaAnzeige) {
      string = this.HexaString(string);
      string3 = this.HexaString(string3);
      string4 = this.HexaString(string4);
      string5 = this.HexaString(string5);
      string7 = this.HexaString(string7);
      string8 = this.HexaString(string8);
      string9 = this.HexaString(string9);
      if (string2.length() > 0 && !" ".equals(string2)) {
        string2 = string2 + " [" + this.HexaString(string2) + "]";
      }
    }
    if (n != -1) {
      this.progAdrAlt = n;
    }
    if (n2 != -1) {
      this.dataAdrAlt = n2;
    }
    if (n3 != -1) {
      this.stackAdrAlt = n3;
    }
    for (n4 = 0; n4 < this.progmem.length; ++n4) {
      this.progadr[n4] = "" + (this.progAdrAlt + n4 < 0 ? 65536 + (this.progAdrAlt + n4) : this.progAdrAlt + n4);
      if (this.hexaAnzeige) {
        this.progmem[n4] = "0000" + Integer.toHexString(this.speicher.WortOhneVorzeichenGeben(this.progAdrAlt + n4));
        this.progmem[n4] = this.progmem[n4].substring(this.progmem[n4].length() - 4);
        continue;
      }
      this.progmem[n4] = "" + this.speicher.WortMitVorzeichenGeben(this.progAdrAlt + n4);
    }
    for (n4 = 0; n4 < this.datamem.length; ++n4) {
      this.dataadr[n4] = "" + (this.dataAdrAlt + n4 < 0 ? 65536 + (this.dataAdrAlt + n4) : this.dataAdrAlt + n4);
      if (this.hexaAnzeige) {
        this.datamem[n4] = "0000" + Integer.toHexString(this.speicher.WortOhneVorzeichenGeben(this.dataAdrAlt + n4));
        this.datamem[n4] = this.datamem[n4].substring(this.datamem[n4].length() - 4);
        continue;
      }
      this.datamem[n4] = "" + this.speicher.WortMitVorzeichenGeben(this.dataAdrAlt + n4);
    }
    for (n4 = 0; n4 < this.stackmem.length; ++n4) {
      this.stackadr[n4] = "" + (this.stackAdrAlt + n4 < 0 ? 65536 + (this.stackAdrAlt + n4) : this.stackAdrAlt + n4);
      if (this.hexaAnzeige) {
        this.stackmem[n4] = "0000" + Integer.toHexString(this.speicher.WortOhneVorzeichenGeben(this.stackAdrAlt + n4));
        this.stackmem[n4] = this.stackmem[n4].substring(this.stackmem[n4].length() - 4);
        continue;
      }
      this.stackmem[n4] = "" + this.speicher.WortMitVorzeichenGeben(this.stackAdrAlt + n4);
    }
    String string10 = this.mnemos.MnemonicGeben(this.befehlscode);
    for (CpuBeobachter cpuBeobachter : this.beobachter) {
      cpuBeobachter.Befehlsmeldung(string, string2, string3, string4, string5, string7,
          this.sp.WertGeben() < 0 ? "" + (this.sp.WertGeben() + 65536) : "" + this.sp.WertGeben(), this.eqflag,
          this.ltflag, this.ovflag, bl ? (this.adressmodus == 2 ? string10 + "I" : string10) : string9, string8,
          "" + this.pc.WertGeben(), this.progadr, this.progmem, this.dataadr, this.datamem, this.stackadr,
          this.stackmem, string6);
    }
  }

  public void AnzeigeWiederholen() {
    if (this.letzterDa) {
      this.Melden(this.datenWert_letzter, this.adressWert_letzter, this.alu1_letzter, this.alu2_letzter,
          this.alu3_letzter, this.opMnemo_letzter, -1, -1, -1, this.mikro_letzter);
    }
  }

  protected void Fehlermeldung(String string) {
    for (CpuBeobachter cpuBeobachter : this.beobachter) {
      cpuBeobachter.Fehlermeldung(string);
    }
  }

  protected void OpcodeTesten() {
    block0: switch (this.befehlscode) {
      case 0:
      case 1:
      case 46:
      case 99: {
        if (this.adressmodus == 0 && this.adresse == 0)
          break;
        this.befehlscode = -1;
        break;
      }
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      case 20:
      case 40:
      case 41:
      case 42:
      case 43:
      case 44:
      case 45: {
        if (this.adressmodus != 0)
          break;
        this.befehlscode = -1;
        break;
      }
      case 21: {
        if (this.adressmodus != 0 && this.adressmodus != 2 && this.adressmodus != 6)
          break;
        this.befehlscode = -1;
        break;
      }
      case 30:
      case 31:
      case 32:
      case 33:
      case 34:
      case 35:
      case 36:
      case 37: {
        if (this.adressmodus == 1)
          break;
        this.befehlscode = -1;
        break;
      }
      default: {
        if (this.erweitert) {
          switch (this.befehlscode) {
            case 6:
            case 25:
            case 26: {
              if (this.adressmodus == 0 && this.adresse == 0)
                break block0;
              this.befehlscode = -1;
              break;
            }
            case 7:
            case 8: {
              if (this.adressmodus == 2)
                break block0;
              this.befehlscode = -1;
              break;
            }
            case 5: {
              if (this.adressmodus != 0)
                break block0;
              this.befehlscode = -1;
              break;
            }
            default: {
              this.befehlscode = -1;
              break;
            }
          }
          break;
        }
        this.befehlscode = -1;
      }
    }
  }

  public void Ausführen() {
    long l = System.currentTimeMillis() + this.schranke * 1000L;
    do {
      this.Schritt();
    } while (this.befehlscode != 99 && this.befehlscode != -1 && l > System.currentTimeMillis());
    if (this.befehlscode != 99 && this.befehlscode != -1) {
      this.Fehlermeldung("Programmabbruch wegen Zeitüberschreitung");
    }
  }

  public abstract void Schritt();

  public abstract void MikroSchritt();

  protected int OperandenwertGeben(int n, int n2) {
    switch (n2) {
      case 1: {
        return this.speicher.WortMitVorzeichenGeben(n);
      }
      case 2: {
        return n;
      }
      case 3: {
        return this.speicher.WortMitVorzeichenGeben(this.speicher.WortOhneVorzeichenGeben(n));
      }
      case 4: {
        return this.speicher.WortMitVorzeichenGeben(n + this.sp.WertGeben());
      }
      case 5: {
        return this.speicher.WortMitVorzeichenGeben(this.speicher.WortOhneVorzeichenGeben(n + this.sp.WertGeben()));
      }
      case 6: {
        return n + this.sp.WertGeben();
      }
    }
    return 0;
  }

  public void ZurückSetzen() {
    int n;
    this.pc.WertSetzen(0);
    this.a.WertSetzen(0);
    this.sp.WertSetzen(-2);
    this.ltflag = false;
    this.eqflag = false;
    this.ovflag = false;
    this.befehlscode = -1;
    this.adressmodus = 0;
    this.adresse = 0;
    for (n = 0; n < this.progmem.length; ++n) {
      this.progadr[n] = "";
      this.progmem[n] = "";
    }
    for (n = 0; n < this.datamem.length; ++n) {
      this.dataadr[n] = "";
      this.datamem[n] = "";
    }
    this.progAdrAlt = 0;
    this.dataAdrAlt = 0;
    this.stackAdrAlt = -2;
    this.Melden("", "", "", "", "", true, -1, -1, -1, "");
  }

  public void Übertragen(Cpu cpu) {
    if (this != cpu) {
      cpu.pc.WertSetzen(this.pc.WertGeben());
      cpu.a.WertSetzen(this.a.WertGeben());
      cpu.sp.WertSetzen(this.sp.WertGeben());
      cpu.ltflag = this.ltflag;
      cpu.eqflag = this.eqflag;
      cpu.ovflag = this.ovflag;
      cpu.befehlscode = this.befehlscode;
      cpu.adressmodus = this.adressmodus;
      cpu.adresse = this.adresse;
      cpu.progAdrAlt = this.progAdrAlt;
      cpu.dataAdrAlt = this.dataAdrAlt;
      cpu.stackAdrAlt = this.stackAdrAlt;
      cpu.erweitert = this.erweitert;
    }
  }

  public static Cpu CpuErzeugen(String string) {
    if ("einfach" == string) {
      return new CpuEinfach(Speicher.SpeicherGeben());
    }
    if ("detail" == string) {
      return new CpuDetail(Speicher.SpeicherGeben());
    }
    return null;
  }

  public void Assemblieren(String string, FehlerVerwaltung fehlerVerwaltung) {
    new Parser(new Scanner(string), this.speicher, fehlerVerwaltung, this.erweitert).Parse();
  }

  public void Übersetzen(String string, FehlerVerwaltung fehlerVerwaltung) {
    String string2 = this.erweitert ? new ParserErweitert(string, fehlerVerwaltung).Parse()
        : new ParserEinfach(string, fehlerVerwaltung).Parse();
    if (!fehlerVerwaltung.FehlerAufgetreten()) {
      this.speicher.SpeicherLöschen();
      new Parser(new Scanner(string2), this.speicher, fehlerVerwaltung, this.erweitert).Parse();
    }
  }

  public void AbbruchSchrankeSetzen(int n) {
    this.schranke = n;
  }

  public void SpeicherbeobachterSetzen(SpeicherBeobachter speicherBeobachter) {
    this.speicher.Registrieren(speicherBeobachter);
  }
}
