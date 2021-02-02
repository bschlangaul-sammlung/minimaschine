package org.bschlangaul.minimaschine;

import javax.swing.JLabel;
import javax.swing.border.LineBorder;

class CpuAnzeigeErweitert extends CpuAnzeige {
  private JLabel labelSP;
  private JLabel labelAdr4;
  private JLabel labelAdr5;
  private JLabel labelMem4;
  private JLabel labelMem5;
  private JLabel spLabel;

  CpuAnzeigeErweitert(KontrolleurInterface kontrolleurInterface) {
    super(kontrolleurInterface);
  }

  @Override
  protected void OberflächeAufbauen() {
    super.OberflächeAufbauen();
    this.ausfuehrenButton.setLocation(100, 190);
    this.einzelButton.setLocation(350, 190);
    this.registerpanel.setSize(200, 140);
    this.spLabel = new JLabel();
    this.spLabel.setText("SP:");
    this.spLabel.setSize(40, 20);
    this.spLabel.setLocation(20, 110);
    this.registerpanel.add(this.spLabel);
    this.labelSP = new JLabel();
    this.labelSP.setText("");
    this.labelSP.setSize(60, 20);
    this.labelSP.setLocation(60, 110);
    this.labelSP.setBorder(LineBorder.createGrayLineBorder());
    this.registerpanel.add(this.labelSP);
    this.speicherpanel.setSize(200, 160);
    this.labelAdr4 = new JLabel();
    this.labelAdr4.setText("65534");
    this.labelAdr4.setSize(80, 20);
    this.labelAdr4.setLocation(20, 100);
    this.speicherpanel.add(this.labelAdr4);
    this.labelMem4 = new JLabel();
    this.labelMem4.setText("");
    this.labelMem4.setSize(80, 21);
    this.labelMem4.setLocation(100, 100);
    this.labelMem4.setBorder(LineBorder.createGrayLineBorder());
    this.speicherpanel.add(this.labelMem4);
    this.labelAdr5 = new JLabel();
    this.labelAdr5.setText("65535");
    this.labelAdr5.setSize(80, 20);
    this.labelAdr5.setLocation(20, 120);
    this.speicherpanel.add(this.labelAdr5);
    this.labelMem5 = new JLabel();
    this.labelMem5.setText("");
    this.labelMem5.setSize(80, 20);
    this.labelMem5.setLocation(100, 120);
    this.labelMem5.setBorder(LineBorder.createGrayLineBorder());
    this.speicherpanel.add(this.labelMem5);
  }

  @Override
  protected void AnzeigegrößeSetzen(boolean bl) {
    super.AnzeigegrößeSetzen(bl);
    if (bl) {
      this.registerpanel.setSize(300, 210);
      this.registerpanel.setLocation(15, 30);
      this.spLabel.setFont(this.fgross);
      this.spLabel.setSize(60, 30);
      this.spLabel.setLocation(30, 165);
      this.labelSP.setFont(this.fgross);
      this.labelSP.setSize(90, 30);
      this.labelSP.setLocation(90, 165);
      this.speicherpanel.setSize(300, 235);
      this.speicherpanel.setLocation(555, 30);
      this.labelAdr4.setFont(this.fgross);
      this.labelAdr4.setSize(120, 30);
      this.labelAdr4.setLocation(30, 150);
      this.labelMem4.setFont(this.fgross);
      this.labelMem4.setSize(120, 31);
      this.labelMem4.setLocation(150, 150);
      this.labelAdr5.setFont(this.fgross);
      this.labelAdr5.setSize(120, 30);
      this.labelAdr5.setLocation(30, 180);
      this.labelMem5.setFont(this.fgross);
      this.labelMem5.setSize(120, 30);
      this.labelMem5.setLocation(150, 180);
      this.ausfuehrenButton.setLocation(150, 280);
      this.einzelButton.setLocation(525, 280);
      this.fenster.setSize(900, 375);
    } else {
      this.registerpanel.setSize(200, 140);
      this.registerpanel.setLocation(10, 20);
      this.spLabel.setFont(this.f);
      this.spLabel.setSize(40, 20);
      this.spLabel.setLocation(20, 110);
      this.labelSP.setFont(this.f);
      this.labelSP.setSize(60, 20);
      this.labelSP.setLocation(60, 110);
      this.speicherpanel.setSize(200, 160);
      this.speicherpanel.setLocation(370, 20);
      this.labelAdr4.setFont(this.f);
      this.labelAdr4.setSize(80, 20);
      this.labelAdr4.setLocation(20, 100);
      this.labelMem4.setFont(this.f);
      this.labelMem4.setSize(80, 21);
      this.labelMem4.setLocation(100, 100);
      this.labelAdr5.setFont(this.f);
      this.labelAdr5.setSize(80, 20);
      this.labelAdr5.setLocation(20, 120);
      this.labelMem5.setFont(this.f);
      this.labelMem5.setSize(80, 20);
      this.labelMem5.setLocation(100, 120);
      this.ausfuehrenButton.setLocation(100, 190);
      this.einzelButton.setLocation(350, 190);
      this.fenster.setSize(600, 250);
    }
  }

  @Override
  public void Befehlsmeldung(String string, String string2, String string3, String string4, String string5,
      String string6, String string7, boolean bl, boolean bl2, boolean bl3, String string8, String string9,
      String string10, String[] arrstring, String[] arrstring2, String[] arrstring3, String[] arrstring4,
      String[] arrstring5, String[] arrstring6, String string11) {
    super.Befehlsmeldung(string, string2, string3, string4, string5, string6, string7, bl, bl2, bl3, string8, string9,
        string10, arrstring, arrstring2, arrstring3, arrstring4, arrstring5, arrstring6, string11);
    this.labelSP.setText(string7);
    this.labelAdr4.setText(arrstring5[0]);
    this.labelAdr5.setText(arrstring5[1]);
    this.labelMem4.setText(arrstring6[0]);
    this.labelMem5.setText(arrstring6[1]);
  }
}
