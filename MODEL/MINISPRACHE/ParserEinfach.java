/*
 * Decompiled with CFR 0.150.
 */
package MODEL.MINISPRACHE;

import MODEL.FehlerVerwaltung;
import MODEL.MINISPRACHE.Attribut;
import MODEL.MINISPRACHE.AttributGeladen;
import MODEL.MINISPRACHE.AttributKonstant;
import MODEL.MINISPRACHE.AttributVariable;
import MODEL.MINISPRACHE.Parser;
import java.util.HashSet;

public class ParserEinfach
extends Parser {
    private HashSet<String> variable = new HashSet();
    private int akthilfsplatz = 0;
    private int maxhilfsplatz = 0;
    private int markenNummer = 0;

    public ParserEinfach(String string, FehlerVerwaltung fehlerVerwaltung) {
        super(string, fehlerVerwaltung, false);
    }

    @Override
    public String Parse() {
        this.Program();
        this.VariableAusgeben();
        return this.ausgabe.AssemblerGeben();
    }

    private void Program() {
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
            if (this.aktToken == 13) {
                this.Variablenvereinbarung();
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
        this.ausgabe.BefehlEintragen(null, "HOLD", null);
    }

    private void Variablenvereinbarung() {
        this.aktToken = this.scanner.NächstesToken();
        if (this.aktToken != 2) {
            this.fehler.FehlerEintragen("Bezeichner erwartet", this.scanner.PositionGeben());
            this.SkipBisStrichpunkt();
            this.aktToken = this.scanner.NächstesToken();
        } else {
            do {
                String string;
                if (this.variable.contains(string = this.scanner.BezeichnerGeben())) {
                    this.fehler.FehlerEintragen("Bezeichner schon vereinbart", this.scanner.PositionGeben());
                } else {
                    this.variable.add(string);
                }
                this.aktToken = this.scanner.NächstesToken();
                if (this.aktToken != 32) continue;
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

    private void Block() {
        while (!this.BlockendeTesten() && this.aktToken != 0) {
            if (this.aktToken == 2) {
                this.Zuweisung();
            } else if (this.aktToken == 14) {
                this.BedingteAnweisung();
            } else if (this.aktToken == 17) {
                this.WiederholungEingang();
            } else if (this.aktToken == 21) {
                this.WiederholungZaehl();
            } else if (this.aktToken == 19) {
                this.WiederholungEnde();
            }
            if (this.aktToken == 30) {
                this.aktToken = this.scanner.NächstesToken();
                continue;
            }
            if (this.BlockendeTesten()) continue;
            this.fehler.FehlerEintragen("';' erwartet", this.scanner.PositionGeben());
            if (this.aktToken == 2 || this.aktToken == 14 || this.aktToken == 17 || this.aktToken == 21 || this.aktToken == 19) continue;
            this.aktToken = this.scanner.NächstesToken();
        }
    }

    private void Zuweisung() {
        String string = this.scanner.BezeichnerGeben();
        this.aktToken = this.scanner.NächstesToken();
        if (this.aktToken == 33) {
            this.aktToken = this.scanner.NächstesToken();
        } else {
            this.fehler.FehlerEintragen("':=' erwartet", this.scanner.PositionGeben());
            if (this.aktToken == 39 || this.aktToken != 1) {
                this.aktToken = this.scanner.NächstesToken();
            }
        }
        Attribut attribut = this.AusdruckStrich();
        attribut.Laden(this.ausgabe);
        this.ausgabe.BefehlEintragen(null, "STORE", string);
    }

    private Attribut AusdruckStrich() {
        int n;
        boolean bl = false;
        if (this.aktToken == 34) {
            this.aktToken = this.scanner.NächstesToken();
        } else if (this.aktToken == 35) {
            this.aktToken = this.scanner.NächstesToken();
            bl = true;
        }
        Attribut attribut = this.AusdruckPunkt();
        if (bl) {
            n = 0;
            if (attribut instanceof AttributGeladen) {
                ++this.akthilfsplatz;
                if (this.akthilfsplatz > this.maxhilfsplatz) {
                    this.maxhilfsplatz = this.akthilfsplatz;
                }
                this.ausgabe.BefehlEintragen(null, "STORE", "hi$" + this.akthilfsplatz);
                attribut = new AttributVariable("hi$" + this.akthilfsplatz);
                n = 1;
            }
            this.ausgabe.BefehlEintragen(null, "LOADI", "0");
            attribut.Operation(this.ausgabe, "SUB");
            attribut = new AttributGeladen();
            this.akthilfsplatz -= n;
        }
        while (this.aktToken == 34 || this.aktToken == 35) {
            String string = "";
            if (this.aktToken == 34) {
                string = "ADD";
            } else if (this.aktToken == 35) {
                string = "SUB";
            }
            n = 0;
            if (attribut instanceof AttributGeladen) {
                ++this.akthilfsplatz;
                if (this.akthilfsplatz > this.maxhilfsplatz) {
                    this.maxhilfsplatz = this.akthilfsplatz;
                }
                this.ausgabe.BefehlEintragen(null, "STORE", "hi$" + this.akthilfsplatz);
                attribut = new AttributVariable("hi$" + this.akthilfsplatz);
                n = 1;
            }
            this.aktToken = this.scanner.NächstesToken();
            Attribut attribut2 = this.AusdruckPunkt();
            if (attribut2 instanceof AttributGeladen) {
                ++this.akthilfsplatz;
                if (this.akthilfsplatz > this.maxhilfsplatz) {
                    this.maxhilfsplatz = this.akthilfsplatz;
                }
                this.ausgabe.BefehlEintragen(null, "STORE", "hi$" + this.akthilfsplatz);
                attribut2 = new AttributVariable("hi$" + this.akthilfsplatz);
                ++n;
            }
            attribut.Laden(this.ausgabe);
            attribut2.Operation(this.ausgabe, string);
            attribut = new AttributGeladen();
            this.akthilfsplatz -= n;
        }
        return attribut;
    }

    private Attribut AusdruckPunkt() {
        Attribut attribut = this.Faktor();
        while (this.aktToken == 36 || this.aktToken == 37 || this.aktToken == 38) {
            String string = "";
            if (this.aktToken == 36) {
                string = "MUL";
            } else if (this.aktToken == 37) {
                string = "DIV";
            } else if (this.aktToken == 38) {
                string = "MOD";
            }
            int n = 0;
            if (attribut instanceof AttributGeladen) {
                ++this.akthilfsplatz;
                if (this.akthilfsplatz > this.maxhilfsplatz) {
                    this.maxhilfsplatz = this.akthilfsplatz;
                }
                this.ausgabe.BefehlEintragen(null, "STORE", "hi$" + this.akthilfsplatz);
                attribut = new AttributVariable("hi$" + this.akthilfsplatz);
                n = 1;
            }
            this.aktToken = this.scanner.NächstesToken();
            Attribut attribut2 = this.Faktor();
            if (attribut2 instanceof AttributGeladen) {
                ++this.akthilfsplatz;
                if (this.akthilfsplatz > this.maxhilfsplatz) {
                    this.maxhilfsplatz = this.akthilfsplatz;
                }
                this.ausgabe.BefehlEintragen(null, "STORE", "hi$" + this.akthilfsplatz);
                attribut2 = new AttributVariable("hi$" + this.akthilfsplatz);
                ++n;
            }
            attribut.Laden(this.ausgabe);
            attribut2.Operation(this.ausgabe, string);
            attribut = new AttributGeladen();
            this.akthilfsplatz -= n;
        }
        return attribut;
    }

    private Attribut Faktor() {
        Attribut attribut;
        if (this.aktToken == 2) {
            attribut = new AttributVariable(this.scanner.BezeichnerGeben());
            this.aktToken = this.scanner.NächstesToken();
        } else if (this.aktToken == 3) {
            attribut = new AttributKonstant(this.scanner.ZahlGeben());
            this.aktToken = this.scanner.NächstesToken();
        } else if (this.aktToken == 45) {
            this.aktToken = this.scanner.NächstesToken();
            attribut = this.AusdruckStrich();
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
            attribut = new AttributKonstant(0);
            if (!this.BlockendeTesten()) {
                this.aktToken = this.scanner.NächstesToken();
            }
        }
        return attribut;
    }

    private void Bedingung(int n) {
        Attribut attribut = this.AusdruckStrich();
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
        int n2 = 0;
        if (attribut instanceof AttributGeladen) {
            ++this.akthilfsplatz;
            if (this.akthilfsplatz > this.maxhilfsplatz) {
                this.maxhilfsplatz = this.akthilfsplatz;
            }
            this.ausgabe.BefehlEintragen(null, "STORE", "hi$" + this.akthilfsplatz);
            attribut = new AttributVariable("hi$" + this.akthilfsplatz);
            n2 = 1;
        }
        this.aktToken = this.scanner.NächstesToken();
        Attribut attribut2 = this.AusdruckStrich();
        if (attribut2 instanceof AttributGeladen) {
            ++this.akthilfsplatz;
            if (this.akthilfsplatz > this.maxhilfsplatz) {
                this.maxhilfsplatz = this.akthilfsplatz;
            }
            this.ausgabe.BefehlEintragen(null, "STORE", "hi$" + this.akthilfsplatz);
            attribut2 = new AttributVariable("hi$" + this.akthilfsplatz);
            ++n2;
        }
        attribut.Laden(this.ausgabe);
        attribut2.Operation(this.ausgabe, "CMP");
        this.akthilfsplatz -= n2;
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
        String string;
        ++this.markenNummer;
        int n2 = this.markenNummer++;
        int n3 = this.markenNummer;
        ++this.akthilfsplatz;
        if (this.akthilfsplatz > this.maxhilfsplatz) {
            this.maxhilfsplatz = this.akthilfsplatz;
        }
        String string2 = "hi$" + this.akthilfsplatz;
        this.aktToken = this.scanner.NächstesToken();
        if (this.aktToken == 2) {
            string = this.scanner.BezeichnerGeben();
            this.aktToken = this.scanner.NächstesToken();
        } else {
            this.fehler.FehlerEintragen("Bezeichner erwartet", this.scanner.PositionGeben());
            string = "dummy";
        }
        if (this.aktToken == 33) {
            this.aktToken = this.scanner.NächstesToken();
        } else {
            this.fehler.FehlerEintragen("':=' erwartet", this.scanner.PositionGeben());
            if (this.aktToken == 39 || this.aktToken != 1) {
                this.aktToken = this.scanner.NächstesToken();
            }
        }
        Attribut attribut = this.AusdruckStrich();
        attribut.Laden(this.ausgabe);
        this.ausgabe.BefehlEintragen(null, "STORE", string);
        if (this.aktToken == 22) {
            this.aktToken = this.scanner.NächstesToken();
        } else {
            this.fehler.FehlerEintragen("'TO' erwartet", this.scanner.PositionGeben());
        }
        attribut = this.AusdruckStrich();
        attribut.Laden(this.ausgabe);
        this.ausgabe.BefehlEintragen(null, "STORE", string2);
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
        this.ausgabe.BefehlEintragen(null, "LOAD", string);
        this.ausgabe.BefehlEintragen(null, "CMP", string2);
        if (n > 0) {
            this.ausgabe.BefehlEintragen(null, "JMPP", "M$" + n3);
        } else {
            this.ausgabe.BefehlEintragen(null, "JMPN", "M$" + n3);
        }
        this.Block();
        this.ausgabe.BefehlEintragen(null, "LOAD", string);
        this.ausgabe.BefehlEintragen(null, "ADDI", "" + n);
        this.ausgabe.BefehlEintragen(null, "JMPV", "M$" + n3);
        this.ausgabe.BefehlEintragen(null, "STORE", string);
        this.ausgabe.BefehlEintragen(null, "JMP", "M$" + n2);
        this.ausgabe.BefehlEintragen("M$" + n3, null, null);
        if (this.aktToken == 12) {
            this.aktToken = this.scanner.NächstesToken();
        } else {
            this.fehler.FehlerEintragen("'END' erwartet", this.scanner.PositionGeben());
        }
        --this.akthilfsplatz;
    }

    private void VariableAusgeben() {
        for (String string : this.variable) {
            this.ausgabe.BefehlEintragen(string, "WORD", "0");
        }
        for (int i = 1; i <= this.maxhilfsplatz; ++i) {
            this.ausgabe.BefehlEintragen("hi$" + i, "WORD", "0");
        }
    }
}
