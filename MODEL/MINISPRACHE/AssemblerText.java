/*
 * Decompiled with CFR 0.150.
 */
package MODEL.MINISPRACHE;

class AssemblerText {
    private String ausgabe = "";

    AssemblerText() {
    }

    void BefehlEintragen(String string, String string2, String string3) {
        String string4;
        boolean bl = false;
        if (string != null) {
            if (string.startsWith("#")) {
                string4 = string;
                bl = true;
            } else {
                string4 = string + ":";
            }
        } else {
            string4 = "";
        }
        if (!bl) {
            string4 = string4 + "\t";
        }
        if (string2 != null) {
            string4 = string4 + string2;
            if (string3 != null) {
                string4 = string4 + "\t" + string3;
            }
        }
        string4 = string4 + "\n";
        this.ausgabe = this.ausgabe + string4;
    }

    String AssemblerGeben() {
        return this.ausgabe;
    }
}

