/*
 * Decompiled with CFR 0.150.
 */
package MODEL;

class Scanner {
    static final int eof = 0;
    static final int illegal = 1;
    static final int bezeichner = 2;
    static final int zahl = 3;
    static final int doppelpunkt = 4;
    static final int trennung = 5;
    static final int plus = 6;
    static final int minus = 7;
    static final int raute = 8;
    static final int klammerauf = 9;
    static final int klammerzu = 10;
    static final int at = 11;
    private char[] quelle;
    private int pos;
    private char ch;
    private int zahlenwert;
    private String name;

    Scanner(String string) {
        this.quelle = string.toCharArray();
        this.pos = 0;
        this.NächstesZeichen();
    }

    private void NächstesZeichen() {
        this.ch = this.pos < this.quelle.length ? this.quelle[this.pos++] : (char)'\u0000';
    }

    private void Bezeichner() {
        int n = this.pos - 1;
        int n2 = 0;
        while ('a' <= this.ch && this.ch <= 'z' || 'A' <= this.ch && this.ch <= 'Z' || '0' <= this.ch && this.ch <= '9' || this.ch == '_' || this.ch == '$') {
            this.NächstesZeichen();
            ++n2;
        }
        this.name = new String(this.quelle, n, n2);
    }

    private void Zahl() {
        this.zahlenwert = 0;
        while ('0' <= this.ch && this.ch <= '9') {
            this.zahlenwert = this.zahlenwert * 10 + Character.digit(this.ch, 10);
            this.NächstesZeichen();
        }
    }

    private boolean HexZahl() {
        this.zahlenwert = 0;
        if ('0' <= this.ch && this.ch <= '9' || 'A' <= this.ch && this.ch <= 'F' || 'a' <= this.ch && this.ch <= 'f') {
            while ('0' <= this.ch && this.ch <= '9' || 'A' <= this.ch && this.ch <= 'F' || 'a' <= this.ch && this.ch <= 'f') {
                this.zahlenwert = this.zahlenwert * 16 + Character.digit(this.ch, 16);
                this.NächstesZeichen();
            }
            return true;
        }
        return false;
    }

    int NächstesToken() {
        while (this.ch == ' ' || this.ch == '\t') {
            this.NächstesZeichen();
        }
        if (this.ch == '#') {
            this.NächstesZeichen();
            while (this.ch != '\r' && this.ch != '\n' && this.ch != '\u0000') {
                this.NächstesZeichen();
            }
        }
        if (this.ch == '\u0000') {
            this.NächstesZeichen();
            return 0;
        }
        if (this.ch == ':') {
            this.NächstesZeichen();
            return 4;
        }
        if (this.ch == '(') {
            this.NächstesZeichen();
            return 9;
        }
        if (this.ch == ')') {
            this.NächstesZeichen();
            return 10;
        }
        if (this.ch == '@') {
            this.NächstesZeichen();
            return 11;
        }
        if (this.ch == '\r') {
            this.NächstesZeichen();
            if (this.ch == '\n') {
                this.NächstesZeichen();
            }
            return 5;
        }
        if (this.ch == '\n') {
            this.NächstesZeichen();
            return 5;
        }
        if (this.ch == '+') {
            this.NächstesZeichen();
            return 6;
        }
        if (this.ch == '-') {
            this.NächstesZeichen();
            return 7;
        }
        if (this.ch == '$') {
            this.NächstesZeichen();
            return 8;
        }
        if ('1' <= this.ch && this.ch <= '9') {
            this.Zahl();
            return 3;
        }
        if ('0' == this.ch) {
            this.NächstesZeichen();
            if (Character.toLowerCase(this.ch) == 'x') {
                this.NächstesZeichen();
                if (!this.HexZahl()) {
                    return 1;
                }
            } else {
                this.Zahl();
            }
            return 3;
        }
        if ('a' <= this.ch && this.ch <= 'z' || 'A' <= this.ch && this.ch <= 'Z' || this.ch == '_' || this.ch == '$') {
            this.Bezeichner();
            return 2;
        }
        this.NächstesZeichen();
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
}
