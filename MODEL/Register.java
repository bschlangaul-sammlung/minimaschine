/*
 * Decompiled with CFR 0.150.
 */
package MODEL;

class Register {
    private int wert = 0;

    Register() {
    }

    int WertGeben() {
        return this.wert;
    }

    void WertSetzen(int n) {
        while (n >= 32768) {
            n -= 65536;
        }
        while (n < -32768) {
            n += 65536;
        }
        this.wert = n;
    }

    void Inkrementieren(int n) {
        this.wert += n;
        if (this.wert > 32767) {
            this.wert -= 65536;
        }
    }

    void Dekrementieren(int n) {
        this.wert -= n;
        if (this.wert < -32268) {
            this.wert += 65536;
        }
    }
}

