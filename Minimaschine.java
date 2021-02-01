
/*
 * Decompiled with CFR 0.150.
 */
import MODEL.Cpu;

class Minimaschine {
  private Minimaschine(String[] arrstring) {
    Cpu cpu = Cpu.CpuErzeugen("einfach");
    Cpu cpu2 = Cpu.CpuErzeugen("detail");
    Kontrolleur kontrolleur = new Kontrolleur(cpu, cpu2);
    CpuAnzeige cpuAnzeige = new CpuAnzeige(kontrolleur);
    CpuAnzeige2 cpuAnzeige2 = new CpuAnzeige2(kontrolleur);
    CpuAnzeigeErweitert cpuAnzeigeErweitert = new CpuAnzeigeErweitert(kontrolleur);
    SpeicherAnzeige speicherAnzeige = new SpeicherAnzeige(kontrolleur);
    FensterVerwaltung fensterVerwaltung = new FensterVerwaltung(cpuAnzeige, cpuAnzeige2, cpuAnzeigeErweitert,
        speicherAnzeige);
    kontrolleur.VerwaltungSetzen(fensterVerwaltung);
    cpu.Registrieren(cpuAnzeige);
    cpu2.Registrieren(cpuAnzeige2);
    cpu.Registrieren(cpuAnzeigeErweitert);
    cpu.SpeicherbeobachterSetzen(speicherAnzeige);
    if (!Anzeige.IstMacOS()) {
      for (int i = 0; i < arrstring.length; ++i) {
        kontrolleur.ÖffnenAusführen(arrstring[i]);
      }
    }
  }

  public static void main(String[] arrstring) {
    new Minimaschine(arrstring);
  }
}
