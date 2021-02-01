/*
 * Decompiled with CFR 0.150.
 */
package MODEL.MINISPRACHE;

import java.util.HashMap;

class TextToken {
    private HashMap<String, Integer> tokentabelle = new HashMap(20);
    private static TextToken dasObjekt = new TextToken();

    private TextToken() {
        this.tokentabelle.put("PROGRAM", 10);
        this.tokentabelle.put("BEGIN", 11);
        this.tokentabelle.put("END", 12);
        this.tokentabelle.put("VAR", 13);
        this.tokentabelle.put("IF", 14);
        this.tokentabelle.put("THEN", 15);
        this.tokentabelle.put("ELSE", 16);
        this.tokentabelle.put("WHILE", 17);
        this.tokentabelle.put("DO", 18);
        this.tokentabelle.put("REPEAT", 19);
        this.tokentabelle.put("UNTIL", 20);
        this.tokentabelle.put("FOR", 21);
        this.tokentabelle.put("TO", 22);
        this.tokentabelle.put("BY", 23);
        this.tokentabelle.put("PROCEDURE", 102);
        this.tokentabelle.put("FUNCTION", 103);
        this.tokentabelle.put("RETURN", 104);
    }

    static TextToken TokenlisteGeben() {
        return dasObjekt;
    }

    boolean BezeichnerTesten(String string, boolean bl) {
        return this.tokentabelle.containsKey(string) && (bl || this.tokentabelle.get(string) < 102);
    }

    int TokenwertGeben(String string) {
        return this.tokentabelle.get(string);
    }
}

