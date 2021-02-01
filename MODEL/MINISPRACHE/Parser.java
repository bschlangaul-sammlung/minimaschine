/*
 * Decompiled with CFR 0.150.
 */
package MODEL.MINISPRACHE;

import MODEL.FehlerVerwaltung;
import MODEL.MINISPRACHE.AssemblerText;
import MODEL.MINISPRACHE.Scanner;

public abstract class Parser {
    protected Scanner scanner;
    protected FehlerVerwaltung fehler;
    protected int aktToken;
    protected String programmname;
    protected AssemblerText ausgabe = new AssemblerText();

    public Parser(String string, FehlerVerwaltung fehlerVerwaltung, boolean bl) {
        this.scanner = new Scanner(string, bl, this.ausgabe);
        this.fehler = fehlerVerwaltung;
        this.aktToken = this.scanner.NächstesToken();
        this.programmname = "";
    }

    public abstract String Parse();

    protected boolean BlockendeTesten() {
        return this.aktToken == 12 || this.aktToken == 16 || this.aktToken == 20;
    }

    protected void SkipBisStrichpunkt() {
        while (this.aktToken != 30 && this.aktToken != 30) {
            this.aktToken = this.scanner.NächstesToken();
        }
    }
}
