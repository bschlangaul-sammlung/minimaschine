package org.bschlangaul.minimaschine.MODEL.MINISPRACHE;

abstract class Ablage {
  protected StackVerwaltung stack;
  protected boolean istFeld;

  Ablage(StackVerwaltung stackVerwaltung, boolean bl) {
    this.stack = stackVerwaltung;
    this.istFeld = bl;
  }

  boolean IstFeld() {
    return this.istFeld;
  }

  abstract void Laden(AssemblerText var1);

  abstract void Operation(AssemblerText var1, String var2);

  abstract void AdresseLaden(AssemblerText var1, boolean var2);

  void AblageFreigeben(AssemblerText assemblerText) {
  }

  static Ablage HilfsplatzAnlagen(AssemblerText assemblerText, StackVerwaltung stackVerwaltung, boolean bl) {
    assemblerText.BefehlEintragen(null, "PUSH", null);
    return new AblageStackHilf(stackVerwaltung, bl, false);
  }

  static Ablage AblageFürVariableGeben(Variable variable, StackVerwaltung stackVerwaltung) {
    if (variable instanceof Parameter) {
      return new AblageStack(stackVerwaltung, ((Parameter) variable).OffsetGeben(),
          ((Parameter) variable).IstVarParam(), variable.IstFeld());
    }
    if (variable instanceof LokaleVariable) {
      return new AblageStack(stackVerwaltung, ((LokaleVariable) variable).OffsetGeben(), false, variable.IstFeld());
    }
    if (variable instanceof GlobaleVariable) {
      return new AblageGlobal(variable.NamenGeben(), variable.IstFeld());
    }
    return null;
  }
}
