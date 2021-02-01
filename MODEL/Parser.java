/*
 * Decompiled with CFR 0.150.
 */
package MODEL;

import MODEL.AssemblerBefehle;
import MODEL.FehlerVerwaltung;
import MODEL.Scanner;
import MODEL.Speicher;
import java.util.HashMap;
import java.util.Map;

class Parser {
    private Scanner scanner;
    private Speicher speicher;
    private FehlerVerwaltung fehler;
    private AssemblerBefehle befehle;
    private int aktToken;
    private int pc;
    private HashMap<String, Integer> marken;
    private HashMap<Integer, String> fixierungen;
    private boolean erweitert;

    Parser(Scanner scanner, Speicher speicher, FehlerVerwaltung fehlerVerwaltung, boolean bl) {
        this.scanner = scanner;
        this.speicher = speicher;
        this.fehler = fehlerVerwaltung;
        this.erweitert = bl;
        this.pc = 0;
        this.befehle = AssemblerBefehle.AssemblerbefehleGeben();
        this.marken = new HashMap(40);
        this.fixierungen = new HashMap(80);
        this.aktToken = this.scanner.NächstesToken();
    }

    void Parse() {
        while (this.aktToken != 0) {
            while (this.aktToken == 5) {
                this.aktToken = this.scanner.NächstesToken();
            }
            if (this.aktToken == 2) {
                String string = this.scanner.BezeichnerGeben();
                int n = this.scanner.PositionGeben();
                this.aktToken = this.scanner.NächstesToken();
                if (this.aktToken == 4) {
                    if (this.marken.containsKey(string)) {
                        this.fehler.FehlerEintragen("Marke doppelt vereinbart", this.scanner.PositionGeben());
                    } else if (this.erweitert && (string.equals("SP") || string.equals("sp"))) {
                        this.fehler.FehlerEintragen("unzulässiger Name für Marke: " + string, this.scanner.PositionGeben());
                    } else {
                        this.marken.put(string, this.pc);
                    }
                    this.aktToken = this.scanner.NächstesToken();
                    if (this.aktToken == 2) {
                        string = this.scanner.BezeichnerGeben();
                        n = this.scanner.PositionGeben();
                        this.aktToken = this.scanner.NächstesToken();
                    } else {
                        if (this.aktToken == 0) continue;
                        if (this.aktToken == 5) continue;
                        this.fehler.FehlerEintragen("Bezeichner erwartet", this.scanner.PositionGeben());
                    }
                }
                if (this.befehle.BezeichnerTesten(string)) {
                    int n2;
                    int n3;
                    int n4 = this.befehle.OpcodeGeben(string);
                    int n5 = 0;
                    if (n4 < 0) {
                        n3 = 1;
                        if (this.aktToken == 7) {
                            n3 = -1;
                            this.aktToken = this.scanner.NächstesToken();
                        } else if (this.aktToken == 6) {
                            this.aktToken = this.scanner.NächstesToken();
                        }
                        if (this.aktToken == 3) {
                            this.speicher.WortSetzen(this.pc, n3 * this.scanner.ZahlGeben());
                            ++this.pc;
                            this.aktToken = this.scanner.NächstesToken();
                        } else {
                            this.speicher.WortSetzen(this.pc, 0);
                            ++this.pc;
                            this.fehler.FehlerEintragen("Zahl erwartet", this.scanner.PositionGeben());
                        }
                    } else if (n4 >= 300) {
                        n2 = 0;
                        n5 = 2;
                        n4 -= 300;
                        n3 = 1;
                        if (this.aktToken == 2) {
                            string = this.scanner.BezeichnerGeben();
                            if (this.marken.containsKey(string)) {
                                n2 = this.marken.get(string);
                            } else {
                                this.fixierungen.put(this.pc + 1, string);
                            }
                            this.aktToken = this.scanner.NächstesToken();
                        } else {
                            if (this.aktToken == 7) {
                                n3 = -1;
                                this.aktToken = this.scanner.NächstesToken();
                            } else if (this.aktToken == 6) {
                                this.aktToken = this.scanner.NächstesToken();
                            }
                            if (this.aktToken == 3) {
                                n2 = n3 * this.scanner.ZahlGeben();
                                this.aktToken = this.scanner.NächstesToken();
                            } else {
                                this.fehler.FehlerEintragen("Zahl erwartet", this.scanner.PositionGeben());
                            }
                        }
                        this.speicher.WortSetzen(this.pc, n5 * 256 + n4);
                        ++this.pc;
                        this.speicher.WortSetzen(this.pc, n2);
                        ++this.pc;
                    } else {
                        n2 = 0;
                        n5 = 0;
                        if (this.aktToken == 9) {
                            this.aktToken = this.scanner.NächstesToken();
                            n5 = 3;
                            if (this.aktToken == 2) {
                                string = this.scanner.BezeichnerGeben();
                                if (this.erweitert && (string.equals("SP") || string.equals("sp"))) {
                                    n5 = 4;
                                    n2 = 0;
                                } else if (this.marken.containsKey(string)) {
                                    n2 = this.marken.get(string);
                                } else {
                                    this.fixierungen.put(this.pc + 1, string);
                                }
                                this.aktToken = this.scanner.NächstesToken();
                            } else if (this.aktToken == 3) {
                                n2 = this.scanner.ZahlGeben();
                                this.aktToken = this.scanner.NächstesToken();
                            } else {
                                this.fehler.FehlerEintragen("Zahl oder Bezeichner erwartet", this.scanner.PositionGeben());
                            }
                            if (this.aktToken == 10) {
                                this.aktToken = this.scanner.NächstesToken();
                            } else {
                                this.fehler.FehlerEintragen("')' erwartet", this.scanner.PositionGeben());
                            }
                        } else if (this.aktToken == 2) {
                            n5 = 1;
                            string = this.scanner.BezeichnerGeben();
                            if (this.marken.containsKey(string)) {
                                n2 = this.marken.get(string);
                            } else {
                                this.fixierungen.put(this.pc + 1, string);
                            }
                            this.aktToken = this.scanner.NächstesToken();
                        } else if (this.aktToken == 3) {
                            n5 = 1;
                            n2 = this.scanner.ZahlGeben();
                            this.aktToken = this.scanner.NächstesToken();
                            if (this.erweitert) {
                                if (this.aktToken == 9) {
                                    n5 = 4;
                                    this.aktToken = this.scanner.NächstesToken();
                                    if (this.aktToken == 2) {
                                        if (!this.scanner.BezeichnerGeben().equals("SP") && !this.scanner.BezeichnerGeben().equals("sp")) {
                                            this.fehler.FehlerEintragen("'SP' erwartet", this.scanner.PositionGeben());
                                        }
                                        this.aktToken = this.scanner.NächstesToken();
                                    } else if (this.aktToken == 3) {
                                        this.fehler.FehlerEintragen("'SP' erwartet", this.scanner.PositionGeben());
                                        this.aktToken = this.scanner.NächstesToken();
                                    } else {
                                        this.fehler.FehlerEintragen("'SP' erwartet", this.scanner.PositionGeben());
                                    }
                                    if (this.aktToken == 10) {
                                        this.aktToken = this.scanner.NächstesToken();
                                    } else {
                                        this.fehler.FehlerEintragen("')' erwartet", this.scanner.PositionGeben());
                                    }
                                }
                            }
                        } else if (this.aktToken == 8) {
                            n5 = 2;
                            this.aktToken = this.scanner.NächstesToken();
                            n3 = 1;
                            if (this.aktToken == 2) {
                                string = this.scanner.BezeichnerGeben();
                                if (this.marken.containsKey(string)) {
                                    n2 = this.marken.get(string);
                                } else {
                                    this.fixierungen.put(this.pc + 1, string);
                                }
                                this.aktToken = this.scanner.NächstesToken();
                            } else {
                                if (this.aktToken == 7) {
                                    n3 = -1;
                                    this.aktToken = this.scanner.NächstesToken();
                                } else if (this.aktToken == 6) {
                                    this.aktToken = this.scanner.NächstesToken();
                                }
                                if (this.aktToken == 3) {
                                    n2 = n3 * this.scanner.ZahlGeben();
                                    this.aktToken = this.scanner.NächstesToken();
                                    if (this.erweitert) {
                                        if (this.aktToken == 9) {
                                            n5 = 6;
                                            this.aktToken = this.scanner.NächstesToken();
                                            if (this.aktToken == 2) {
                                                if (!this.scanner.BezeichnerGeben().equals("SP") && !this.scanner.BezeichnerGeben().equals("sp")) {
                                                    this.fehler.FehlerEintragen("'SP' erwartet", this.scanner.PositionGeben());
                                                }
                                                this.aktToken = this.scanner.NächstesToken();
                                            } else {
                                                this.fehler.FehlerEintragen("'SP' erwartet", this.scanner.PositionGeben());
                                            }
                                            if (this.aktToken == 10) {
                                                this.aktToken = this.scanner.NächstesToken();
                                            } else {
                                                this.fehler.FehlerEintragen("')' erwartet", this.scanner.PositionGeben());
                                            }
                                        }
                                    }
                                } else {
                                    this.fehler.FehlerEintragen("Zahl erwartet", this.scanner.PositionGeben());
                                }
                            }
                        } else if (this.erweitert) {
                            if (this.aktToken == 11) {
                                n5 = 5;
                                this.aktToken = this.scanner.NächstesToken();
                                if (this.aktToken == 3) {
                                    n2 = this.scanner.ZahlGeben();
                                    this.aktToken = this.scanner.NächstesToken();
                                } else if (this.aktToken == 2) {
                                    this.fehler.FehlerEintragen("Zahl erwartet", this.scanner.PositionGeben());
                                    this.aktToken = this.scanner.NächstesToken();
                                }
                                if (this.aktToken == 9) {
                                    this.aktToken = this.scanner.NächstesToken();
                                    if (this.aktToken == 2) {
                                        if (!this.scanner.BezeichnerGeben().equals("SP") && !this.scanner.BezeichnerGeben().equals("sp")) {
                                            this.fehler.FehlerEintragen("'SP' erwartet", this.scanner.PositionGeben());
                                        }
                                        this.aktToken = this.scanner.NächstesToken();
                                    } else if (this.aktToken == 3) {
                                        this.fehler.FehlerEintragen("'SP' erwartet", this.scanner.PositionGeben());
                                        this.aktToken = this.scanner.NächstesToken();
                                    } else {
                                        this.fehler.FehlerEintragen("'SP' erwartet", this.scanner.PositionGeben());
                                    }
                                    if (this.aktToken == 10) {
                                        this.aktToken = this.scanner.NächstesToken();
                                    } else {
                                        this.fehler.FehlerEintragen("')' erwartet", this.scanner.PositionGeben());
                                    }
                                } else {
                                    this.fehler.FehlerEintragen("(", this.scanner.PositionGeben());
                                }
                            }
                        }
                        this.speicher.WortSetzen(this.pc, n5 * 256 + n4);
                        ++this.pc;
                        this.speicher.WortSetzen(this.pc, n2);
                        ++this.pc;
                    }
                    switch (n4) {
                        case 1:
                        case 99: {
                            if (n5 == 0) break;
                            this.fehler.FehlerEintragen("Unzulässige Adressteile", this.scanner.PositionGeben());
                            break;
                        }
                        case 10:
                        case 11:
                        case 12:
                        case 13:
                        case 15:
                        case 20: {
                            if (n5 != 0) break;
                            this.fehler.FehlerEintragen("Fehlender Adressteil", this.scanner.PositionGeben());
                            break;
                        }
                        case 21:
                        case 30:
                        case 31:
                        case 32:
                        case 33:
                        case 34:
                        case 35:
                        case 36: {
                            if (n5 == 0) {
                                this.fehler.FehlerEintragen("Fehlender Adressteil", this.scanner.PositionGeben());
                                break;
                            }
                            if (n5 != 2) break;
                            this.fehler.FehlerEintragen("Unzulässige Adressart", this.scanner.PositionGeben());
                            break;
                        }
                        case 6:
                        case 25:
                        case 26: {
                            if (n5 != 0) {
                                this.fehler.FehlerEintragen("Unzulässige Adressteile", this.scanner.PositionGeben());
                            }
                            if (this.erweitert) break;
                            this.fehler.FehlerEintragen("Erweiterte Befehle nicht zulässig", this.scanner.PositionGeben());
                            break;
                        }
                        case 7:
                        case 8: {
                            if (n5 == 2) break;
                            this.fehler.FehlerEintragen("Unzulässige Adressteile", this.scanner.PositionGeben());
                            break;
                        }
                        case 5: {
                            if (n5 == 0) {
                                this.fehler.FehlerEintragen("Fehlender Adressteil", this.scanner.PositionGeben());
                            } else if (n5 == 2) {
                                this.fehler.FehlerEintragen("Unzulässige Adressart", this.scanner.PositionGeben());
                            }
                            if (this.erweitert) break;
                            this.fehler.FehlerEintragen("Erweiterte Befehle nicht zulässig", this.scanner.PositionGeben());
                        }
                    }
                    if (this.aktToken == 0 || this.aktToken == 5) continue;
                    this.fehler.FehlerEintragen("Überflüssige Adressteile", this.scanner.PositionGeben());
                    continue;
                }
                this.fehler.FehlerEintragen("Kein gültiger Befehl: " + string, n);
                this.Überspringen();
                continue;
            }
            if (this.aktToken == 0) continue;
            this.fehler.FehlerEintragen("Bezeichner erwartet", this.scanner.PositionGeben());
            this.Überspringen();
        }
        if (this.pc > 65536) {
            this.fehler.FehlerEintragen("Programm zu lang", this.scanner.PositionGeben());
        }
        for (Map.Entry<Integer, String> entry : this.fixierungen.entrySet()) {
            if (this.marken.containsKey(entry.getValue())) {
                this.speicher.WortSetzen(entry.getKey(), this.marken.get(entry.getValue()));
                continue;
            }
            this.fehler.FehlerEintragen("Marke nicht definiert: " + entry.getValue(), this.scanner.PositionGeben());
        }
        if (this.fehler.FehlerAufgetreten()) {
            if (this.pc > 65536) {
                this.pc = 65534;
                this.speicher.WortSetzen(65534, -1);
                this.speicher.WortSetzen(65535, -1);
            }
            for (int i = 0; i < this.pc; ++i) {
                this.speicher.WortSetzen(i, 0);
            }
        }
    }

    private void Überspringen() {
        while (this.aktToken != 0 && this.aktToken != 5) {
            this.aktToken = this.scanner.NächstesToken();
        }
    }
}
