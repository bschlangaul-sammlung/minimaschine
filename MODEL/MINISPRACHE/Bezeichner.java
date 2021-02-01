/*
 * Decompiled with CFR 0.150.
 */
package MODEL.MINISPRACHE;

abstract class Bezeichner {
    private String name;

    Bezeichner(String string) {
        this.name = string;
    }

    String NamenGeben() {
        return this.name;
    }
}

