
/*
 * Decompiled with CFR 0.150.
 */
import java.util.ArrayList;

class FensterVerwaltung {
  private ArrayList<Anzeige> offen = new ArrayList();
  private Anzeige cpuanzeige;
  private Anzeige cpuanzeigeEinfach;
  private Anzeige cpuanzeigeDetail;
  private Anzeige cpuanzeigeEinfachErweitert;
  private Anzeige speicheranzeige;

  FensterVerwaltung(Anzeige anzeige, Anzeige anzeige2, Anzeige anzeige3, Anzeige anzeige4) {
    this.cpuanzeige = anzeige;
    this.cpuanzeigeEinfach = anzeige;
    this.cpuanzeigeDetail = anzeige2;
    this.cpuanzeigeEinfachErweitert = anzeige3;
    anzeige2.Ausblenden();
    anzeige3.Ausblenden();
    this.speicheranzeige = anzeige4;
  }

  void EditorEintragen(Anzeige anzeige) {
    int n;
    for (n = 0; n < this.offen.size(); ++n) {
      anzeige.FenstereintragHinzufügen(n, this.offen.get(n));
    }
    this.offen.add(anzeige);
    n = this.offen.indexOf(anzeige);
    for (Anzeige anzeige2 : this.offen) {
      anzeige2.FenstereintragHinzufügen(n, anzeige);
    }
    this.cpuanzeige.FenstereintragHinzufügen(n, anzeige);
    this.speicheranzeige.FenstereintragHinzufügen(n, anzeige);
  }

  void EditorAustragen(Anzeige anzeige) {
    int n = this.offen.indexOf(anzeige);
    this.offen.remove(anzeige);
    for (Anzeige anzeige2 : this.offen) {
      anzeige2.FenstereintragEntfernen(n);
    }
    this.cpuanzeige.FenstereintragEntfernen(n);
    this.speicheranzeige.FenstereintragEntfernen(n);
  }

  void EditorTitel\u00c4ndern(Anzeige anzeige) {
    int n = this.offen.indexOf(anzeige);
    for (Anzeige anzeige2 : this.offen) {
      anzeige2.Fenstereintrag\u00c4ndern(n, anzeige);
    }
    this.cpuanzeige.Fenstereintrag\u00c4ndern(n, anzeige);
    this.speicheranzeige.Fenstereintrag\u00c4ndern(n, anzeige);
  }

  void CpuFensterAuswählen() {
    this.cpuanzeige.Aktivieren();
  }

  void SpeicherFensterAuswählen() {
    this.speicheranzeige.Aktivieren();
  }

  void CpuAnzeigeWählen(boolean bl, boolean bl2) {
    if (bl) {
      this.cpuanzeige = this.cpuanzeigeDetail;
      this.cpuanzeigeDetail.Aktivieren();
      this.cpuanzeigeEinfach.Ausblenden();
      this.cpuanzeigeEinfachErweitert.Ausblenden();
    } else {
      if (bl2) {
        this.cpuanzeige = this.cpuanzeigeEinfachErweitert;
        this.cpuanzeigeEinfachErweitert.Aktivieren();
        this.cpuanzeigeEinfach.Ausblenden();
      } else {
        this.cpuanzeige = this.cpuanzeigeEinfach;
        this.cpuanzeigeEinfach.Aktivieren();
        this.cpuanzeigeEinfachErweitert.Ausblenden();
      }
      this.cpuanzeigeDetail.Ausblenden();
      ((CpuAnzeige) this.cpuanzeige).erweiterungenItem.setSelected(bl2);
    }
  }

  void BeendenMitteilen() {
    for (Anzeige anzeige : this.offen) {
      anzeige.BeendenMitteilen();
    }
  }
}
