package org.bschlangaul.minimaschine.MODEL.MINISPRACHE;

import org.bschlangaul.minimaschine.MODEL.FehlerVerwaltung;
import java.util.HashMap;
import java.util.Iterator;

public class ParserErweitert extends Parser {
  private HashMap<String, Bezeichner> globaleBezeichner = new HashMap();
  private HashMap<String, Bezeichner> lokaleBezeichner = new HashMap();
  private StackVerwaltung stack = new StackVerwaltung();
  private int markenNummer = 0;
  private String prozedurname = null;
  private boolean istFunktion = false;

  public ParserErweitert(String string, FehlerVerwaltung fehlerVerwaltung) {
    super(string, fehlerVerwaltung, true);
  }

  @Override
  public String Parse() {
    this.Program();
    this.VariableAusgeben();
    return this.ausgabe.AssemblerGeben();
  }

  private Bezeichner BezeichnerSuchen(String string) {
    if (this.lokaleBezeichner.containsKey(string)) {
      return this.lokaleBezeichner.get(string);
    }
    if (this.globaleBezeichner.containsKey(string)) {
      return this.globaleBezeichner.get(string);
    }
    this.fehler.FehlerEintragen("Bezeichner nicht gefunden: " + string, this.scanner.PositionGeben());
    return null;
  }

  private void Program() {
    boolean bl = false;
    if (this.aktToken == 10) {
      this.aktToken = this.scanner.NächstesToken();
      if (this.aktToken == 2) {
        this.programmname = this.scanner.BezeichnerGeben();
        this.aktToken = this.scanner.NächstesToken();
      } else {
        this.fehler.FehlerEintragen("Bezeichner erwartet", this.scanner.PositionGeben());
      }
      if (this.aktToken == 30) {
        this.aktToken = this.scanner.NächstesToken();
      } else {
        this.fehler.FehlerEintragen("';' erwartet", this.scanner.PositionGeben());
      }
      while (this.aktToken == 13) {
        this.Variablenvereinbarung(null);
      }
      while (this.aktToken == 103 || this.aktToken == 102) {
        if (!bl) {
          bl = true;
          this.ausgabe.BefehlEintragen(null, "JMP", this.programmname + "$Start");
        }
        this.Prozedurvereinbarung();
      }
      if (bl) {
        this.ausgabe.BefehlEintragen(this.programmname + "$Start", null, null);
      }
      if (this.aktToken == 11) {
        this.aktToken = this.scanner.NächstesToken();
        this.Block();
        if (this.aktToken == 12) {
          this.aktToken = this.scanner.NächstesToken();
          if (this.aktToken == 2) {
            if (!this.programmname.equals(this.scanner.BezeichnerGeben())) {
              this.fehler.FehlerEintragen("Programmname erwartet", this.scanner.PositionGeben());
            }
            this.aktToken = this.scanner.NächstesToken();
            if (this.aktToken == 31) {
              this.aktToken = this.scanner.NächstesToken();
              if (this.aktToken != 0) {
                this.fehler.FehlerEintragen("Unzulässige Zeichen am Programmende", this.scanner.PositionGeben());
              }
            } else {
              this.fehler.FehlerEintragen("'.' erwartet", this.scanner.PositionGeben());
            }
          } else {
            this.fehler.FehlerEintragen("Bezeichner erwartet", this.scanner.PositionGeben());
          }
        } else {
          this.fehler.FehlerEintragen("'END' erwartet", this.scanner.PositionGeben());
        }
      } else {
        this.fehler.FehlerEintragen("'BEGIN' erwartet", this.scanner.PositionGeben());
      }
    } else {
      this.fehler.FehlerEintragen("'PROGRAM' erwartet", this.scanner.PositionGeben());
    }
    this.ausgabe.BefehlEintragen(this.programmname + "$Ende", "HOLD", null);
  }

  private void Variablenvereinbarung(StackVerwaltung stackVerwaltung) {
    this.aktToken = this.scanner.NächstesToken();
    if (this.aktToken != 2) {
      this.fehler.FehlerEintragen("Bezeichner erwartet", this.scanner.PositionGeben());
      this.SkipBisStrichpunkt();
      this.aktToken = this.scanner.NächstesToken();
    } else {
      do {
        String string = this.scanner.BezeichnerGeben();
        if (stackVerwaltung == null && this.globaleBezeichner.containsKey(string)
            || stackVerwaltung != null && this.lokaleBezeichner.containsKey(string)) {
          this.fehler.FehlerEintragen("Bezeichner schon vereinbart", this.scanner.PositionGeben());
        }
        this.aktToken = this.scanner.NächstesToken();
        if (this.aktToken == 100) {
          this.aktToken = this.scanner.NächstesToken();
          if (this.aktToken == 3) {
            int n = this.scanner.ZahlGeben();
            if (n <= 0) {
              this.fehler.FehlerEintragen("Feldlänge muss größer 0 sein.", this.scanner.PositionGeben());
              n = 1;
            }
            if (stackVerwaltung == null) {
              this.globaleBezeichner.put(string, new GlobaleVariable(string, n));
            } else {
              this.lokaleBezeichner.put(string,
                  new LokaleVariable(string, n, stackVerwaltung.OffsetFürNeueVariableGeben(n)));
            }
            this.aktToken = this.scanner.NächstesToken();
          } else {
            this.fehler.FehlerEintragen("Zahl erwartet", this.scanner.PositionGeben());
          }
          if (this.aktToken == 101) {
            this.aktToken = this.scanner.NächstesToken();
          } else {
            this.fehler.FehlerEintragen("] erwartet", this.scanner.PositionGeben());
          }
        } else if (stackVerwaltung == null) {
          this.globaleBezeichner.put(string, new GlobaleVariable(string));
        } else {
          this.lokaleBezeichner.put(string, new LokaleVariable(string, stackVerwaltung.OffsetFürNeueVariableGeben(1)));
        }
        if (this.aktToken != 32)
          continue;
        this.aktToken = this.scanner.NächstesToken();
      } while (this.aktToken == 2);
      if (this.aktToken == 30) {
        this.aktToken = this.scanner.NächstesToken();
      } else {
        this.fehler.FehlerEintragen("';' erwartet", this.scanner.PositionGeben());
        this.SkipBisStrichpunkt();
        this.aktToken = this.scanner.NächstesToken();
      }
    }
  }

  private int ParamterVereinbarung(Prozedur prozedur) {
    Parameter parameter;
    boolean bl = false;
    if (this.aktToken == 13) {
      bl = true;
      this.aktToken = this.scanner.NächstesToken();
    }
    if (this.aktToken != 2) {
      this.fehler.FehlerEintragen("Bezeichner erwartet", this.scanner.PositionGeben());
      this.aktToken = this.scanner.NächstesToken();
      parameter = new Parameter("Dummy", bl);
    } else {
      String string = this.scanner.BezeichnerGeben();
      this.aktToken = this.scanner.NächstesToken();
      if (this.aktToken == 100) {
        int n;
        this.aktToken = this.scanner.NächstesToken();
        if (this.aktToken == 3) {
          n = this.scanner.ZahlGeben();
          if (n <= 0) {
            this.fehler.FehlerEintragen("Feldlänge muss größer 0 sein.", this.scanner.PositionGeben());
            n = 1;
          }
          this.aktToken = this.scanner.NächstesToken();
        } else {
          this.fehler.FehlerEintragen("Zahl erwartet", this.scanner.PositionGeben());
          n = 1;
        }
        if (this.aktToken == 101) {
          this.aktToken = this.scanner.NächstesToken();
        } else {
          this.fehler.FehlerEintragen("] erwartet", this.scanner.PositionGeben());
        }
        parameter = new Parameter(string, n, bl);
      } else {
        parameter = new Parameter(string, bl);
      }
    }
    prozedur.ParameterAnfügen(parameter);
    if (this.aktToken == 32) {
      this.aktToken = this.scanner.NächstesToken();
      int n = this.ParamterVereinbarung(prozedur);
      parameter.OffsetSetzen(n + 1);
      return bl || !parameter.IstFeld() ? n + 1 : n + parameter.FeldlängeGeben();
    }
    parameter.OffsetSetzen(1);
    return bl || !parameter.IstFeld() ? 1 : parameter.FeldlängeGeben();
  }

  private void Prozedurvereinbarung() {
    StackVerwaltung stackVerwaltung = this.stack;
    this.stack = new StackVerwaltung();
    this.istFunktion = this.aktToken == 103;
    this.aktToken = this.scanner.NächstesToken();
    if (this.aktToken == 2) {
      this.prozedurname = this.scanner.BezeichnerGeben();
      this.aktToken = this.scanner.NächstesToken();
    } else {
      this.fehler.FehlerEintragen("Bezeichner erwartet", this.scanner.PositionGeben());
      this.prozedurname = "Dummy";
    }
    if (this.globaleBezeichner.containsKey(this.prozedurname)) {
      this.fehler.FehlerEintragen("Bezeichner schon vereinbart", this.scanner.PositionGeben());
    }
    Prozedur prozedur = new Prozedur(this.prozedurname, this.istFunktion);
    this.globaleBezeichner.put(this.prozedurname, prozedur);
    if (this.aktToken == 45) {
      this.aktToken = this.scanner.NächstesToken();
      if (this.aktToken != 46) {
        int n = this.ParamterVereinbarung(prozedur);
      }
      if (this.aktToken == 46) {
        this.aktToken = this.scanner.NächstesToken();
      } else {
        this.fehler.FehlerEintragen("')' erwartet", this.scanner.PositionGeben());
        this.SkipBisStrichpunkt();
      }
    } else {
      this.fehler.FehlerEintragen("'(' erwartet", this.scanner.PositionGeben());
    }
    if (this.aktToken == 30) {
      this.aktToken = this.scanner.NächstesToken();
    } else {
      this.fehler.FehlerEintragen("';' erwartet", this.scanner.PositionGeben());
    }
    for (Bezeichner bezeichner : prozedur.ParameterGeben()) {
      this.lokaleBezeichner.put(bezeichner.NamenGeben(), bezeichner);
    }
    while (this.aktToken == 13) {
      this.Variablenvereinbarung(this.stack);
    }
    int n = -this.stack.OffsetGeben();
    if (n > 0) {
      this.ausgabe.BefehlEintragen(this.prozedurname, "RSV", "$" + n);
    } else {
      this.ausgabe.BefehlEintragen(this.prozedurname, null, null);
    }
    if (this.aktToken == 11) {
      this.aktToken = this.scanner.NächstesToken();
      this.Block();
      if (this.aktToken == 12) {
        this.aktToken = this.scanner.NächstesToken();
        if (this.aktToken == 2) {
          if (!this.prozedurname.equals(this.scanner.BezeichnerGeben())) {
            this.fehler.FehlerEintragen("Prozedurname erwartet", this.scanner.PositionGeben());
          }
          this.aktToken = this.scanner.NächstesToken();
          if (this.aktToken == 30) {
            this.aktToken = this.scanner.NächstesToken();
          } else {
            this.fehler.FehlerEintragen("';' erwartet", this.scanner.PositionGeben());
          }
        } else {
          this.fehler.FehlerEintragen("Bezeichner erwartet", this.scanner.PositionGeben());
        }
      } else {
        this.fehler.FehlerEintragen("'END' erwartet", this.scanner.PositionGeben());
      }
    } else {
      this.fehler.FehlerEintragen("'BEGIN' erwartet", this.scanner.PositionGeben());
    }
    if (n > 0) {
      this.ausgabe.BefehlEintragen(this.prozedurname + "$Ende", "REL", "$" + n);
      this.ausgabe.BefehlEintragen(null, "RTS", null);
    } else {
      this.ausgabe.BefehlEintragen(this.prozedurname + "$Ende", "RTS", null);
    }
    this.stack = stackVerwaltung;
    this.lokaleBezeichner.clear();
    this.prozedurname = null;
  }

  private void Block() {
    while (!this.BlockendeTesten() && this.aktToken != 0) {
      Object object;
      if (this.aktToken == 2) {
        object = this.BezeichnerSuchen(this.scanner.BezeichnerGeben());
        this.aktToken = this.scanner.NächstesToken();
        if (this.aktToken == 45) {
          this.aktToken = this.scanner.NächstesToken();
          this.Prozeduraufruf((Bezeichner) object);
        } else {
          this.Zuweisung((Bezeichner) object);
        }
      } else if (this.aktToken == 14) {
        this.BedingteAnweisung();
      } else if (this.aktToken == 17) {
        this.WiederholungEingang();
      } else if (this.aktToken == 21) {
        this.WiederholungZaehl();
      } else if (this.aktToken == 19) {
        this.WiederholungEnde();
      } else if (this.aktToken == 104) {
        this.aktToken = this.scanner.NächstesToken();
        if (this.istFunktion) {
          if (this.aktToken == 30) {
            this.fehler.FehlerEintragen("Funktionsergebnis erwartet", this.scanner.PositionGeben());
          } else {
            object = this.AusdruckStrich();
            ((Ablage) object).Laden(this.ausgabe);
          }
        } else if (this.aktToken != 30) {
          this.fehler.FehlerEintragen("Ausdruck nach RETURN nicht erlaubt", this.scanner.PositionGeben());
        }
        if (this.prozedurname == null) {
          this.ausgabe.BefehlEintragen(null, "JMP", this.programmname + "$Ende");
        } else {
          this.ausgabe.BefehlEintragen(null, "JMP", this.prozedurname + "$Ende");
        }
      }
      if (this.aktToken == 30) {
        this.aktToken = this.scanner.NächstesToken();
        continue;
      }
      if (this.BlockendeTesten())
        continue;
      this.fehler.FehlerEintragen("';' erwartet", this.scanner.PositionGeben());
      if (this.aktToken == 2 || this.aktToken == 14 || this.aktToken == 17 || this.aktToken == 21
          || this.aktToken == 19)
        continue;
      this.aktToken = this.scanner.NächstesToken();
    }
  }

  void ParameterSetzen(Prozedur prozedur) {
    Parameter parameter;
    Iterator<Parameter> iterator = prozedur.ParameterGeben().iterator();
    Parameter parameter2 = parameter = iterator.hasNext() ? iterator.next() : null;
    while (true) {
      if (parameter == null) {
        this.fehler.FehlerEintragen("Mehr aktuelle als formale Paramter", this.scanner.PositionGeben());
      }
      Bezeichner bezeichner = this.aktToken == 2 ? this.BezeichnerSuchen(this.scanner.BezeichnerGeben()) : null;
      Ablage ablage = this.AusdruckStrich();
      if (parameter != null && parameter.IstVarParam()) {
        if (!(ablage instanceof AblageGlobal) && !(ablage instanceof AblageStack)) {
          this.fehler.FehlerEintragen("Variable erwartet", this.scanner.PositionGeben());
        } else if (parameter.IstFeld() && !ablage.IstFeld()) {
          this.fehler.FehlerEintragen("Feld erwartet", this.scanner.PositionGeben());
        } else if (!parameter.IstFeld() && ablage.IstFeld()) {
          this.fehler.FehlerEintragen("Einfachen Wert erwartet", this.scanner.PositionGeben());
        } else if (parameter.IstFeld() && ablage.IstFeld() && bezeichner != null && bezeichner instanceof Variable
            && parameter.FeldlängeGeben() != ((Variable) bezeichner).FeldlängeGeben()) {
          this.fehler.FehlerEintragen("Felder haben unterschiedliche Längen", this.scanner.PositionGeben());
        }
        ablage.AdresseLaden(this.ausgabe, false);
        this.ausgabe.BefehlEintragen(null, "PUSH", null);
        this.stack.Dekrementieren();
      } else if (parameter != null && parameter.IstFeld()) {
        if (!ablage.IstFeld()) {
          this.fehler.FehlerEintragen("Einfachen Wert erwartet", this.scanner.PositionGeben());
        } else if (bezeichner != null && bezeichner instanceof Variable
            && parameter.FeldlängeGeben() != ((Variable) bezeichner).FeldlängeGeben()) {
          this.fehler.FehlerEintragen("Felder haben unterschiedliche Längen", this.scanner.PositionGeben());
        }
        this.ausgabe.BefehlEintragen(null, "RSV", "$" + parameter.FeldlängeGeben());
        this.stack.Dekrementieren(parameter.FeldlängeGeben());
        this.ausgabe.BefehlEintragen(null, "LOAD", "$0(SP)");
        this.ausgabe.BefehlEintragen(null, "PUSH", null);
        AblageStackHilf ablageStackHilf = new AblageStackHilf(this.stack, true, true);
        ablage.AdresseLaden(this.ausgabe, false);
        this.ausgabe.BefehlEintragen(null, "PUSH", null);
        AblageStackHilf ablageStackHilf2 = new AblageStackHilf(this.stack, true, true);
        for (int i = 0; i < parameter.FeldlängeGeben(); ++i) {
          this.ausgabe.BefehlEintragen(null, "LOAD", "@0(SP)");
          this.ausgabe.BefehlEintragen(null, "STORE", "@1(SP)");
          if (i == parameter.FeldlängeGeben() - 1)
            continue;
          this.ausgabe.BefehlEintragen(null, "LOAD", "1(SP)");
          this.ausgabe.BefehlEintragen(null, "ADDI", "1");
          this.ausgabe.BefehlEintragen(null, "STORE", "1(SP)");
          this.ausgabe.BefehlEintragen(null, "LOAD", "0(SP)");
          this.ausgabe.BefehlEintragen(null, "ADDI", "1");
          this.ausgabe.BefehlEintragen(null, "STORE", "0(SP)");
        }
        this.ausgabe.BefehlEintragen(null, "REL", "$2");
        this.stack.Inkrementieren(2);
      } else {
        if (ablage.IstFeld()) {
          this.fehler.FehlerEintragen("Einfachen Wert erwartet", this.scanner.PositionGeben());
        }
        ablage.Laden(this.ausgabe);
        this.ausgabe.BefehlEintragen(null, "PUSH", null);
        this.stack.Dekrementieren();
      }
      if (this.aktToken != 32)
        break;
      this.aktToken = this.scanner.NächstesToken();
      parameter = iterator.hasNext() ? iterator.next() : null;
    }
    if (iterator.hasNext()) {
      this.fehler.FehlerEintragen("Mehr formale als aktuelle Paramter", this.scanner.PositionGeben());
    }
  }

  private void Prozeduraufruf(Bezeichner bezeichner) {
    if (bezeichner == null) {
      this.fehler.FehlerEintragen("Unbekannte Variable", this.scanner.PositionGeben());
      bezeichner = new Prozedur("Dummy", false);
    } else if (!(bezeichner instanceof Prozedur)) {
      this.fehler.FehlerEintragen("Prozedur / Funktion erwartet", this.scanner.PositionGeben());
      bezeichner = new Prozedur("Dummy", false);
    }
    if (this.aktToken == 46) {
      if (((Prozedur) bezeichner).ParameterGeben().size() != 0) {
        this.fehler.FehlerEintragen("Parameter erwartet", this.scanner.PositionGeben());
      }
      this.aktToken = this.scanner.NächstesToken();
    } else {
      this.ParameterSetzen((Prozedur) bezeichner);
      if (this.aktToken == 46) {
        this.aktToken = this.scanner.NächstesToken();
      } else {
        this.fehler.FehlerEintragen("')' erwartet", this.scanner.PositionGeben());
      }
    }
    this.ausgabe.BefehlEintragen(null, "JSR", bezeichner.NamenGeben());
    int n = ((Prozedur) bezeichner).ParameterLängeGeben();
    if (n != 0) {
      this.ausgabe.BefehlEintragen(null, "REL", "$" + n);
      this.stack.Inkrementieren(n);
    }
  }

  private void Zuweisung(Bezeichner bezeichner) {
    Ablage ablage;
    if (bezeichner == null) {
      this.fehler.FehlerEintragen("Unbekannte Variable", this.scanner.PositionGeben());
      bezeichner = new GlobaleVariable("Dummy", this.aktToken == 100 ? 1 : -1);
    } else if (!(bezeichner instanceof Variable)) {
      this.fehler.FehlerEintragen("Variable erwartet", this.scanner.PositionGeben());
      bezeichner = new GlobaleVariable("Dummy", this.aktToken == 100 ? 1 : -1);
    }
    Variable variable = (Variable) bezeichner;
    boolean bl = false;
    Ablage ablage2 = Ablage.AblageFürVariableGeben(variable, this.stack);
    if (this.aktToken == 100) {
      bl = true;
      if (!variable.IstFeld()) {
        this.fehler.FehlerEintragen("Feldbezeichner erwartet", this.scanner.PositionGeben());
      }
      this.aktToken = this.scanner.NächstesToken();
      ablage = this.AusdruckStrich();
      ablage.Laden(this.ausgabe);
      ablage.AblageFreigeben(this.ausgabe);
      ablage2.AdresseLaden(this.ausgabe, true);
      ablage2 = Ablage.HilfsplatzAnlagen(this.ausgabe, this.stack, true);
      if (this.aktToken == 101) {
        this.aktToken = this.scanner.NächstesToken();
      } else {
        this.fehler.FehlerEintragen("']' erwartet", this.scanner.PositionGeben());
      }
    }
    if (this.aktToken == 33) {
      this.aktToken = this.scanner.NächstesToken();
    } else {
      this.fehler.FehlerEintragen("':=' erwartet", this.scanner.PositionGeben());
      if (this.aktToken == 39 || this.aktToken != 1) {
        this.aktToken = this.scanner.NächstesToken();
      }
    }
    Variable variable2 = null;
    if (this.aktToken == 2 && (bezeichner = this.BezeichnerSuchen(this.scanner.BezeichnerGeben())) != null
        && bezeichner instanceof Variable) {
      variable2 = (Variable) bezeichner;
    }
    ablage = this.AusdruckStrich();
    if (!bl && variable.IstFeld()) {
      if (!ablage.IstFeld()) {
        this.fehler.FehlerEintragen("Feld erwartet", this.scanner.PositionGeben());
      } else if (variable.FeldlängeGeben() != variable.FeldlängeGeben()) {
        this.fehler.FehlerEintragen("Felder müssen gleiche Länge haben", this.scanner.PositionGeben());
      }
      ablage.AdresseLaden(this.ausgabe, false);
      ablage = Ablage.HilfsplatzAnlagen(this.ausgabe, this.stack, true);
      ablage2.AdresseLaden(this.ausgabe, false);
      ablage2 = Ablage.HilfsplatzAnlagen(this.ausgabe, this.stack, true);
      for (int i = 0; i < variable.FeldlängeGeben(); ++i) {
        this.ausgabe.BefehlEintragen(null, "LOAD", "@1(SP)");
        this.ausgabe.BefehlEintragen(null, "STORE", "@0(SP)");
        if (i == variable.FeldlängeGeben() - 1)
          continue;
        this.ausgabe.BefehlEintragen(null, "LOAD", "1(SP)");
        this.ausgabe.BefehlEintragen(null, "ADDI", "1");
        this.ausgabe.BefehlEintragen(null, "STORE", "1(SP)");
        this.ausgabe.BefehlEintragen(null, "LOAD", "0(SP)");
        this.ausgabe.BefehlEintragen(null, "ADDI", "1");
        this.ausgabe.BefehlEintragen(null, "STORE", "0(SP)");
      }
      this.ausgabe.BefehlEintragen(null, "REL", "$2");
      this.stack.Inkrementieren(2);
    } else {
      if (ablage.IstFeld()) {
        this.fehler.FehlerEintragen("einfachen Wert erwartet", this.scanner.PositionGeben());
      }
      ablage.Laden(this.ausgabe);
      ablage.AblageFreigeben(this.ausgabe);
      ablage2.Operation(this.ausgabe, "STORE");
      ablage2.AblageFreigeben(this.ausgabe);
    }
  }

  private Ablage AusdruckStrich() {
    boolean bl;
    boolean bl2 = false;
    if (this.aktToken == 34) {
      this.aktToken = this.scanner.NächstesToken();
    } else if (this.aktToken == 35) {
      this.aktToken = this.scanner.NächstesToken();
      bl2 = true;
    }
    Ablage ablage = this.AusdruckPunkt();
    if (bl2) {
      bl = false;
      if (ablage instanceof AblageGeladen) {
        ablage = Ablage.HilfsplatzAnlagen(this.ausgabe, this.stack, false);
      }
      this.ausgabe.BefehlEintragen(null, "LOADI", "0");
      ablage.Operation(this.ausgabe, "SUB");
      ablage.AblageFreigeben(this.ausgabe);
      ablage = new AblageGeladen();
    }
    while (this.aktToken == 34 || this.aktToken == 35) {
      String string = "";
      if (this.aktToken == 34) {
        string = "ADD";
      } else if (this.aktToken == 35) {
        string = "SUB";
      }
      bl = false;
      if (ablage instanceof AblageGeladen) {
        ablage = Ablage.HilfsplatzAnlagen(this.ausgabe, this.stack, false);
      }
      this.aktToken = this.scanner.NächstesToken();
      Ablage ablage2 = this.AusdruckPunkt();
      if (ablage2 instanceof AblageGeladen) {
        ablage2 = Ablage.HilfsplatzAnlagen(this.ausgabe, this.stack, false);
      }
      ablage.Laden(this.ausgabe);
      ablage2.Operation(this.ausgabe, string);
      ablage2.AblageFreigeben(this.ausgabe);
      ablage.AblageFreigeben(this.ausgabe);
      ablage = new AblageGeladen();
    }
    return ablage;
  }

  private Ablage AusdruckPunkt() {
    Ablage ablage = this.Faktor();
    while (this.aktToken == 36 || this.aktToken == 37 || this.aktToken == 38) {
      String string = "";
      if (this.aktToken == 36) {
        string = "MUL";
      } else if (this.aktToken == 37) {
        string = "DIV";
      } else if (this.aktToken == 38) {
        string = "MOD";
      }
      if (ablage instanceof AblageGeladen) {
        ablage = Ablage.HilfsplatzAnlagen(this.ausgabe, this.stack, false);
      }
      this.aktToken = this.scanner.NächstesToken();
      Ablage ablage2 = this.Faktor();
      if (ablage2 instanceof AblageGeladen) {
        ablage2 = Ablage.HilfsplatzAnlagen(this.ausgabe, this.stack, false);
      }
      ablage.Laden(this.ausgabe);
      ablage2.Operation(this.ausgabe, string);
      ablage2.AblageFreigeben(this.ausgabe);
      ablage.AblageFreigeben(this.ausgabe);
      ablage = new AblageGeladen();
    }
    return ablage;
  }

  private Ablage Faktor() {
    Ablage ablage;
    if (this.aktToken == 2) {
      Bezeichner bezeichner = this.BezeichnerSuchen(this.scanner.BezeichnerGeben());
      this.aktToken = this.scanner.NächstesToken();
      if (this.aktToken == 45) {
        this.aktToken = this.scanner.NächstesToken();
        this.Prozeduraufruf(bezeichner);
        ablage = new AblageGeladen();
      } else {
        if (!(bezeichner instanceof Variable)) {
          this.fehler.FehlerEintragen("Variablenbezeichner erwartet", this.scanner.PositionGeben());
          bezeichner = new GlobaleVariable("Dummy", this.aktToken == 100 ? 1 : -1);
        }
        ablage = Ablage.AblageFürVariableGeben((Variable) bezeichner, this.stack);
        if (this.aktToken == 100) {
          if (!((Variable) bezeichner).IstFeld()) {
            this.fehler.FehlerEintragen("Feldbezeichner erwartet", this.scanner.PositionGeben());
          }
          this.aktToken = this.scanner.NächstesToken();
          Ablage ablage2 = this.AusdruckStrich();
          ablage2.Laden(this.ausgabe);
          ablage2.AblageFreigeben(this.ausgabe);
          ablage.AdresseLaden(this.ausgabe, true);
          ablage = Ablage.HilfsplatzAnlagen(this.ausgabe, this.stack, true);
          if (this.aktToken == 101) {
            this.aktToken = this.scanner.NächstesToken();
          } else {
            this.fehler.FehlerEintragen("']' erwartet", this.scanner.PositionGeben());
          }
        }
      }
    } else if (this.aktToken == 3) {
      ablage = new AblageKonstante(this.scanner.ZahlGeben());
      this.aktToken = this.scanner.NächstesToken();
    } else if (this.aktToken == 45) {
      this.aktToken = this.scanner.NächstesToken();
      ablage = this.AusdruckStrich();
      if (this.aktToken == 46) {
        this.aktToken = this.scanner.NächstesToken();
      } else {
        this.fehler.FehlerEintragen("')' erwartet", this.scanner.PositionGeben());
        if (!this.BlockendeTesten() && this.aktToken != 30) {
          this.aktToken = this.scanner.NächstesToken();
        }
      }
    } else {
      this.fehler.FehlerEintragen("Bezeichner, Zahl oder '(' erwartet", this.scanner.PositionGeben());
      ablage = new AblageKonstante(0);
      if (!this.BlockendeTesten()) {
        this.aktToken = this.scanner.NächstesToken();
      }
    }
    return ablage;
  }

  private void Bedingung(int n) {
    Ablage ablage = this.AusdruckStrich();
    String string = "";
    if (this.aktToken == 39) {
      string = "JMPNZ";
    } else if (this.aktToken == 40) {
      string = "JMPZ";
    } else if (this.aktToken == 41) {
      string = "JMPNN";
    } else if (this.aktToken == 42) {
      string = "JMPP";
    } else if (this.aktToken == 43) {
      string = "JMPNP";
    } else if (this.aktToken == 44) {
      string = "JMPN";
    } else {
      this.fehler.FehlerEintragen("'=', '<>', '>', '>=', '<' oder '<=' erwartet", this.scanner.PositionGeben());
    }
    if (ablage instanceof AblageGeladen) {
      ablage = Ablage.HilfsplatzAnlagen(this.ausgabe, this.stack, false);
    }
    this.aktToken = this.scanner.NächstesToken();
    Ablage ablage2 = this.AusdruckStrich();
    if (ablage2 instanceof AblageGeladen) {
      ablage2 = Ablage.HilfsplatzAnlagen(this.ausgabe, this.stack, false);
    }
    ablage.Laden(this.ausgabe);
    ablage2.Operation(this.ausgabe, "CMP");
    ablage2.AblageFreigeben(this.ausgabe);
    ablage.AblageFreigeben(this.ausgabe);
    this.ausgabe.BefehlEintragen(null, string, "M$" + n);
  }

  private void BedingteAnweisung() {
    ++this.markenNummer;
    int n = this.markenNummer++;
    int n2 = this.markenNummer;
    this.aktToken = this.scanner.NächstesToken();
    this.Bedingung(n);
    if (this.aktToken == 15) {
      this.aktToken = this.scanner.NächstesToken();
    } else {
      this.fehler.FehlerEintragen("'THEN' erwartet", this.scanner.PositionGeben());
    }
    this.Block();
    if (this.aktToken == 16) {
      this.aktToken = this.scanner.NächstesToken();
      this.ausgabe.BefehlEintragen(null, "JMP", "M$" + n2);
      this.ausgabe.BefehlEintragen("M$" + n, null, null);
      this.Block();
      this.ausgabe.BefehlEintragen("M$" + n2, null, null);
    } else {
      this.ausgabe.BefehlEintragen("M$" + n, null, null);
    }
    if (this.aktToken == 12) {
      this.aktToken = this.scanner.NächstesToken();
    } else {
      this.fehler.FehlerEintragen("'END' erwartet", this.scanner.PositionGeben());
    }
  }

  private void WiederholungEingang() {
    ++this.markenNummer;
    int n = this.markenNummer++;
    int n2 = this.markenNummer;
    this.ausgabe.BefehlEintragen("M$" + n, null, null);
    this.aktToken = this.scanner.NächstesToken();
    this.Bedingung(n2);
    if (this.aktToken == 18) {
      this.aktToken = this.scanner.NächstesToken();
    } else {
      this.fehler.FehlerEintragen("'DO' erwartet", this.scanner.PositionGeben());
    }
    this.Block();
    this.ausgabe.BefehlEintragen(null, "JMP", "M$" + n);
    this.ausgabe.BefehlEintragen("M$" + n2, null, null);
    if (this.aktToken == 12) {
      this.aktToken = this.scanner.NächstesToken();
    } else {
      this.fehler.FehlerEintragen("'END' erwartet", this.scanner.PositionGeben());
    }
  }

  private void WiederholungEnde() {
    int n = ++this.markenNummer;
    this.ausgabe.BefehlEintragen("M$" + n, null, null);
    this.aktToken = this.scanner.NächstesToken();
    this.Block();
    if (this.aktToken == 20) {
      this.aktToken = this.scanner.NächstesToken();
    } else {
      this.fehler.FehlerEintragen("'UNTIL' erwartet", this.scanner.PositionGeben());
    }
    this.Bedingung(n);
  }

  private void WiederholungZaehl() {
    int n;
    Bezeichner bezeichner;
    ++this.markenNummer;
    int n2 = this.markenNummer++;
    int n3 = this.markenNummer;
    this.aktToken = this.scanner.NächstesToken();
    if (this.aktToken == 2) {
      bezeichner = this.BezeichnerSuchen(this.scanner.BezeichnerGeben());
      if (bezeichner == null) {
        this.fehler.FehlerEintragen("Unbekannte Variable", this.scanner.PositionGeben());
        bezeichner = new GlobaleVariable("Dummy");
      } else if (!(bezeichner instanceof Variable)) {
        this.fehler.FehlerEintragen("Variable erwartet", this.scanner.PositionGeben());
        bezeichner = new GlobaleVariable("Dummy");
      }
      this.aktToken = this.scanner.NächstesToken();
    } else {
      this.fehler.FehlerEintragen("Bezeichner erwartet", this.scanner.PositionGeben());
      bezeichner = new GlobaleVariable("Dummy");
    }
    Ablage ablage = Ablage.AblageFürVariableGeben((Variable) bezeichner, this.stack);
    if (this.aktToken == 33) {
      this.aktToken = this.scanner.NächstesToken();
    } else {
      this.fehler.FehlerEintragen("':=' erwartet", this.scanner.PositionGeben());
      if (this.aktToken == 39 || this.aktToken == 1) {
        this.aktToken = this.scanner.NächstesToken();
      }
    }
    Ablage ablage2 = this.AusdruckStrich();
    ablage2.Laden(this.ausgabe);
    ablage.Operation(this.ausgabe, "STORE");
    if (this.aktToken == 22) {
      this.aktToken = this.scanner.NächstesToken();
    } else {
      this.fehler.FehlerEintragen("'TO' erwartet", this.scanner.PositionGeben());
    }
    ablage2 = this.AusdruckStrich();
    ablage2.Laden(this.ausgabe);
    Ablage ablage3 = Ablage.HilfsplatzAnlagen(this.ausgabe, this.stack, false);
    if (this.aktToken == 23) {
      this.aktToken = this.scanner.NächstesToken();
      boolean bl = false;
      if (this.aktToken == 34) {
        this.aktToken = this.scanner.NächstesToken();
      } else if (this.aktToken == 35) {
        this.aktToken = this.scanner.NächstesToken();
        bl = true;
      }
      if (this.aktToken == 3) {
        n = this.scanner.ZahlGeben();
        this.aktToken = this.scanner.NächstesToken();
      } else {
        this.fehler.FehlerEintragen("Zahl erwartet", this.scanner.PositionGeben());
        n = 1;
      }
      if (bl) {
        n = -n;
      }
      if (n == 0) {
        this.fehler.FehlerEintragen("Die Schrittweite darf nicht 0 sein", this.scanner.PositionGeben());
        n = 1;
      }
    } else {
      n = 1;
    }
    if (this.aktToken == 18) {
      this.aktToken = this.scanner.NächstesToken();
    } else {
      this.fehler.FehlerEintragen("'DO' erwartet", this.scanner.PositionGeben());
    }
    this.ausgabe.BefehlEintragen("M$" + n2, null, null);
    ablage.Laden(this.ausgabe);
    ablage3.Operation(this.ausgabe, "CMP");
    if (n > 0) {
      this.ausgabe.BefehlEintragen(null, "JMPP", "M$" + n3);
    } else {
      this.ausgabe.BefehlEintragen(null, "JMPN", "M$" + n3);
    }
    this.Block();
    ablage.Laden(this.ausgabe);
    this.ausgabe.BefehlEintragen(null, "ADDI", "" + n);
    this.ausgabe.BefehlEintragen(null, "JMPV", "M$" + n3);
    ablage.Operation(this.ausgabe, "STORE");
    this.ausgabe.BefehlEintragen(null, "JMP", "M$" + n2);
    this.ausgabe.BefehlEintragen("M$" + n3, null, null);
    if (this.aktToken == 12) {
      this.aktToken = this.scanner.NächstesToken();
    } else {
      this.fehler.FehlerEintragen("'END' erwartet", this.scanner.PositionGeben());
    }
    ablage3.AblageFreigeben(this.ausgabe);
  }

  private void VariableAusgeben() {
    for (Bezeichner bezeichner : this.globaleBezeichner.values()) {
      if (!(bezeichner instanceof GlobaleVariable))
        continue;
      GlobaleVariable globaleVariable = (GlobaleVariable) bezeichner;
      this.ausgabe.BefehlEintragen(globaleVariable.NamenGeben(), "WORD", "0");
      for (int i = 2; i <= globaleVariable.FeldlängeGeben(); ++i) {
        this.ausgabe.BefehlEintragen(null, "WORD", "0");
      }
    }
  }
}
