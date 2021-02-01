/*
 * Decompiled with CFR 0.150.
 */
package MODEL.MINISPRACHE;

import MODEL.MINISPRACHE.Bezeichner;
import MODEL.MINISPRACHE.Parameter;
import java.util.ArrayList;

class Prozedur
extends Bezeichner {
    private boolean istFunktion;
    private ArrayList<Parameter> parameter;
    private int parameterLänge;

    Prozedur(String string, boolean bl) {
        super(string);
        this.istFunktion = bl;
        this.parameter = new ArrayList();
        this.parameterLänge = 0;
    }

    boolean IstFunktion() {
        return this.istFunktion;
    }

    void ParameterAnfügen(Parameter parameter) {
        this.parameter.add(parameter);
        this.parameterLänge += parameter.IstVarParam() || !parameter.IstFeld() ? 1 : parameter.FeldlängeGeben();
    }

    ArrayList<Parameter> ParameterGeben() {
        return this.parameter;
    }

    int ParameterLängeGeben() {
        return this.parameterLänge;
    }
}
