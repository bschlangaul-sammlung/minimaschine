package org.bschlangaul.minimaschine;

import org.bschlangaul.minimaschine.MODEL.CpuBeobachter;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

class CpuAnzeige extends Anzeige implements CpuBeobachter {
  private JLabel labelA;
  private JLabel labelPC;
  private JLabel labelIR1;
  private JLabel labelIR2;
  private JLabel labelEq;
  private JLabel labelLt;
  private JLabel labelOv;
  private JLabel labelAdr1;
  private JLabel labelAdr2;
  private JLabel labelAdr3;
  private JLabel labelMem1;
  private JLabel labelMem2;
  private JLabel labelMem3;
  protected JPanel registerpanel;
  protected JPanel flagpanel;
  protected JPanel speicherpanel;
  protected Font f;
  protected Font fgross;
  private JLabel pcLabel;
  private JLabel irLabel;
  private JLabel acLabel;
  private JLabel zLabel;
  private JLabel nLabel;
  private JLabel vLabel;
  protected JButton ausfuehrenButton;
  protected JButton einzelButton;
  JCheckBoxMenuItem erweiterungenItem;

  CpuAnzeige(KontrolleurInterface kontrolleurInterface) {
    super(kontrolleurInterface);
  }

  @Override
  protected void OberflächeAufbauen() {
    this.fenster = new JFrame("CPU-Kontrolle");
    this.fenster.setJMenuBar(this.menüZeile);
    this.fenster.setLayout(null);
    JPanel jPanel = (JPanel) this.fenster.getContentPane();
    jPanel.setLayout(null);
    this.f = jPanel.getFont();
    this.fgross = new Font(this.f.getName(), this.f.getStyle(), 24);
    this.registerpanel = new JPanel();
    this.registerpanel.setLayout(null);
    this.registerpanel.setSize(200, 110);
    this.registerpanel.setLocation(10, 20);
    jPanel.add(this.registerpanel);
    this.registerpanel.setBorder(new TitledBorder(LineBorder.createGrayLineBorder(), "Register"));
    this.pcLabel = new JLabel();
    this.pcLabel.setText("PC:");
    this.pcLabel.setSize(40, 20);
    this.pcLabel.setLocation(20, 20);
    this.registerpanel.add(this.pcLabel);
    this.labelPC = new JLabel();
    this.labelPC.setText("");
    this.labelPC.setSize(60, 20);
    this.labelPC.setLocation(60, 20);
    this.labelPC.setBorder(LineBorder.createGrayLineBorder());
    this.registerpanel.add(this.labelPC);
    this.irLabel = new JLabel();
    this.irLabel.setText("IR:");
    this.irLabel.setSize(40, 20);
    this.irLabel.setLocation(20, 50);
    this.registerpanel.add(this.irLabel);
    this.labelIR1 = new JLabel();
    this.labelIR1.setText("");
    this.labelIR1.setSize(60, 20);
    this.labelIR1.setLocation(60, 50);
    this.labelIR1.setBorder(LineBorder.createGrayLineBorder());
    this.registerpanel.add(this.labelIR1);
    this.labelIR2 = new JLabel();
    this.labelIR2.setText("");
    this.labelIR2.setSize(60, 20);
    this.labelIR2.setLocation(119, 50);
    this.labelIR2.setBorder(LineBorder.createGrayLineBorder());
    this.registerpanel.add(this.labelIR2);
    this.acLabel = new JLabel();
    this.acLabel.setText("AC:");
    this.acLabel.setSize(40, 20);
    this.acLabel.setLocation(20, 80);
    this.registerpanel.add(this.acLabel);
    this.labelA = new JLabel();
    this.labelA.setText("");
    this.labelA.setSize(60, 20);
    this.labelA.setLocation(60, 80);
    this.labelA.setBorder(LineBorder.createGrayLineBorder());
    this.registerpanel.add(this.labelA);
    this.flagpanel = new JPanel();
    this.flagpanel.setLayout(null);
    this.flagpanel.setSize(140, 110);
    this.flagpanel.setLocation(220, 20);
    jPanel.add(this.flagpanel);
    this.flagpanel.setBorder(new TitledBorder(LineBorder.createGrayLineBorder(), "Flags"));
    this.zLabel = new JLabel();
    this.zLabel.setText("Z:");
    this.zLabel.setSize(40, 20);
    this.zLabel.setLocation(20, 20);
    this.flagpanel.add(this.zLabel);
    this.labelEq = new JLabel();
    this.labelEq.setText("");
    this.labelEq.setHorizontalAlignment(0);
    this.labelEq.setSize(20, 20);
    this.labelEq.setLocation(60, 20);
    this.labelEq.setBorder(LineBorder.createGrayLineBorder());
    this.flagpanel.add(this.labelEq);
    this.nLabel = new JLabel();
    this.nLabel.setText("N:");
    this.nLabel.setSize(40, 20);
    this.nLabel.setLocation(20, 50);
    this.flagpanel.add(this.nLabel);
    this.labelLt = new JLabel();
    this.labelLt.setText("");
    this.labelLt.setHorizontalAlignment(0);
    this.labelLt.setSize(20, 20);
    this.labelLt.setLocation(60, 50);
    this.labelLt.setBorder(LineBorder.createGrayLineBorder());
    this.flagpanel.add(this.labelLt);
    this.vLabel = new JLabel();
    this.vLabel.setText("V:");
    this.vLabel.setSize(40, 20);
    this.vLabel.setLocation(20, 80);
    this.flagpanel.add(this.vLabel);
    this.labelOv = new JLabel();
    this.labelOv.setText("");
    this.labelOv.setHorizontalAlignment(0);
    this.labelOv.setSize(20, 20);
    this.labelOv.setLocation(60, 80);
    this.labelOv.setBorder(LineBorder.createGrayLineBorder());
    this.flagpanel.add(this.labelOv);
    this.speicherpanel = new JPanel();
    this.speicherpanel.setLayout(null);
    this.speicherpanel.setSize(200, 110);
    this.speicherpanel.setLocation(370, 20);
    jPanel.add(this.speicherpanel);
    this.speicherpanel.setBorder(new TitledBorder(LineBorder.createGrayLineBorder(), "Speicherauszug"));
    this.labelAdr1 = new JLabel();
    this.labelAdr1.setText("0");
    this.labelAdr1.setSize(80, 20);
    this.labelAdr1.setLocation(20, 20);
    this.speicherpanel.add(this.labelAdr1);
    this.labelMem1 = new JLabel();
    this.labelMem1.setText("");
    this.labelMem1.setSize(80, 21);
    this.labelMem1.setLocation(100, 20);
    this.labelMem1.setBorder(LineBorder.createGrayLineBorder());
    this.speicherpanel.add(this.labelMem1);
    this.labelAdr2 = new JLabel();
    this.labelAdr2.setText("1");
    this.labelAdr2.setSize(80, 20);
    this.labelAdr2.setLocation(20, 40);
    this.speicherpanel.add(this.labelAdr2);
    this.labelMem2 = new JLabel();
    this.labelMem2.setText("");
    this.labelMem2.setSize(80, 20);
    this.labelMem2.setLocation(100, 40);
    this.labelMem2.setBorder(LineBorder.createGrayLineBorder());
    this.speicherpanel.add(this.labelMem2);
    this.labelAdr3 = new JLabel();
    this.labelAdr3.setText("100");
    this.labelAdr3.setSize(80, 20);
    this.labelAdr3.setLocation(20, 70);
    this.speicherpanel.add(this.labelAdr3);
    this.labelMem3 = new JLabel();
    this.labelMem3.setText("");
    this.labelMem3.setSize(80, 20);
    this.labelMem3.setLocation(100, 70);
    this.labelMem3.setBorder(LineBorder.createGrayLineBorder());
    this.speicherpanel.add(this.labelMem3);
    this.ausfuehrenButton = new JButton("Ausführen");
    this.ausfuehrenButton.setSize(150, 30);
    this.ausfuehrenButton.setLocation(100, 150);
    this.fenster.add(this.ausfuehrenButton);
    this.ausfuehrenButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        CpuAnzeige.this.kontrolleur.Ausführen();
      }
    });
    this.einzelButton = new JButton("Einzelschritt");
    this.einzelButton.setSize(150, 30);
    this.einzelButton.setLocation(350, 150);
    this.fenster.add(this.einzelButton);
    this.einzelButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        CpuAnzeige.this.kontrolleur.EinzelSchritt();
      }
    });
    this.fenster.setSize(600, 250);
    this.fenster.setVisible(true);
    this.fenster.setResizable(false);
  }

  protected void AnzeigegrößeSetzen(boolean bl) {
    if (bl) {
      ((TitledBorder) this.registerpanel.getBorder()).setTitleFont(this.fgross);
      this.registerpanel.setSize(300, 165);
      this.registerpanel.setLocation(15, 30);
      this.pcLabel.setFont(this.fgross);
      this.pcLabel.setSize(60, 30);
      this.pcLabel.setLocation(30, 30);
      this.labelPC.setFont(this.fgross);
      this.labelPC.setSize(90, 30);
      this.labelPC.setLocation(90, 30);
      this.irLabel.setFont(this.fgross);
      this.irLabel.setSize(60, 30);
      this.irLabel.setLocation(30, 75);
      this.labelIR1.setFont(this.fgross);
      this.labelIR1.setSize(90, 30);
      this.labelIR1.setLocation(90, 75);
      this.labelIR2.setFont(this.fgross);
      this.labelIR2.setSize(90, 30);
      this.labelIR2.setLocation(179, 75);
      this.acLabel.setFont(this.fgross);
      this.acLabel.setSize(60, 30);
      this.acLabel.setLocation(30, 120);
      this.labelA.setFont(this.fgross);
      this.labelA.setSize(90, 30);
      this.labelA.setLocation(90, 120);
      ((TitledBorder) this.flagpanel.getBorder()).setTitleFont(this.fgross);
      this.flagpanel.setSize(210, 165);
      this.flagpanel.setLocation(330, 30);
      this.zLabel.setFont(this.fgross);
      this.zLabel.setSize(60, 30);
      this.zLabel.setLocation(30, 30);
      this.labelEq.setFont(this.fgross);
      this.labelEq.setSize(30, 30);
      this.labelEq.setLocation(90, 30);
      this.nLabel.setFont(this.fgross);
      this.nLabel.setSize(60, 30);
      this.nLabel.setLocation(30, 75);
      this.labelLt.setFont(this.fgross);
      this.labelLt.setSize(30, 30);
      this.labelLt.setLocation(90, 75);
      this.vLabel.setFont(this.fgross);
      this.vLabel.setSize(60, 30);
      this.vLabel.setLocation(30, 120);
      this.labelOv.setFont(this.fgross);
      this.labelOv.setSize(30, 30);
      this.labelOv.setLocation(90, 120);
      ((TitledBorder) this.speicherpanel.getBorder()).setTitleFont(this.fgross);
      this.speicherpanel.setSize(300, 165);
      this.speicherpanel.setLocation(555, 30);
      this.labelAdr1.setFont(this.fgross);
      this.labelAdr1.setSize(120, 30);
      this.labelAdr1.setLocation(30, 30);
      this.labelMem1.setFont(this.fgross);
      this.labelMem1.setSize(120, 31);
      this.labelMem1.setLocation(150, 30);
      this.labelAdr2.setFont(this.fgross);
      this.labelAdr2.setSize(120, 30);
      this.labelAdr2.setLocation(30, 60);
      this.labelMem2.setFont(this.fgross);
      this.labelMem2.setSize(120, 30);
      this.labelMem2.setLocation(150, 60);
      this.labelAdr3.setFont(this.fgross);
      this.labelAdr3.setSize(120, 30);
      this.labelAdr3.setLocation(30, 105);
      this.labelMem3.setFont(this.fgross);
      this.labelMem3.setSize(120, 30);
      this.labelMem3.setLocation(150, 105);
      this.ausfuehrenButton.setLocation(150, 225);
      this.einzelButton.setLocation(525, 225);
      this.fenster.setSize(900, 375);
    } else {
      ((TitledBorder) this.registerpanel.getBorder()).setTitleFont(this.f);
      this.registerpanel.setSize(200, 110);
      this.registerpanel.setLocation(10, 20);
      this.pcLabel.setFont(this.f);
      this.pcLabel.setSize(40, 20);
      this.pcLabel.setLocation(20, 20);
      this.labelPC.setFont(this.f);
      this.labelPC.setSize(60, 20);
      this.labelPC.setLocation(60, 20);
      this.irLabel.setFont(this.f);
      this.irLabel.setSize(40, 20);
      this.irLabel.setLocation(20, 50);
      this.labelIR1.setFont(this.f);
      this.labelIR1.setSize(60, 20);
      this.labelIR1.setLocation(60, 50);
      this.labelIR2.setFont(this.f);
      this.labelIR2.setSize(60, 20);
      this.labelIR2.setLocation(119, 50);
      this.acLabel.setFont(this.f);
      this.acLabel.setSize(40, 20);
      this.acLabel.setLocation(20, 80);
      this.labelA.setFont(this.f);
      this.labelA.setSize(60, 20);
      this.labelA.setLocation(60, 80);
      ((TitledBorder) this.flagpanel.getBorder()).setTitleFont(this.f);
      this.flagpanel.setSize(140, 110);
      this.flagpanel.setLocation(220, 20);
      this.zLabel.setFont(this.f);
      this.zLabel.setSize(40, 20);
      this.zLabel.setLocation(20, 20);
      this.labelEq.setFont(this.f);
      this.labelEq.setSize(20, 20);
      this.labelEq.setLocation(60, 20);
      this.nLabel.setFont(this.f);
      this.nLabel.setSize(40, 20);
      this.nLabel.setLocation(20, 50);
      this.labelLt.setFont(this.f);
      this.labelLt.setSize(20, 20);
      this.labelLt.setLocation(60, 50);
      this.vLabel.setFont(this.f);
      this.vLabel.setSize(40, 20);
      this.vLabel.setLocation(20, 80);
      this.labelOv.setFont(this.f);
      this.labelOv.setSize(20, 20);
      this.labelOv.setLocation(60, 80);
      ((TitledBorder) this.speicherpanel.getBorder()).setTitleFont(this.f);
      this.speicherpanel.setSize(200, 110);
      this.speicherpanel.setLocation(370, 20);
      this.labelAdr1.setFont(this.f);
      this.labelAdr1.setSize(80, 20);
      this.labelAdr1.setLocation(20, 20);
      this.labelMem1.setFont(this.f);
      this.labelMem1.setSize(80, 21);
      this.labelMem1.setLocation(100, 20);
      this.labelAdr2.setFont(this.f);
      this.labelAdr2.setSize(80, 20);
      this.labelAdr2.setLocation(20, 40);
      this.labelMem2.setFont(this.f);
      this.labelMem2.setSize(80, 20);
      this.labelMem2.setLocation(100, 40);
      this.labelAdr3.setFont(this.f);
      this.labelAdr3.setSize(80, 20);
      this.labelAdr3.setLocation(20, 70);
      this.labelMem3.setFont(this.f);
      this.labelMem3.setSize(80, 20);
      this.labelMem3.setLocation(100, 70);
      this.ausfuehrenButton.setLocation(100, 150);
      this.einzelButton.setLocation(350, 150);
      this.fenster.setSize(600, 250);
    }
  }

  @Override
  public void Befehlsmeldung(String string, String string2, String string3, String string4, String string5,
      String string6, String string7, boolean bl, boolean bl2, boolean bl3, String string8, String string9,
      String string10, String[] arrstring, String[] arrstring2, String[] arrstring3, String[] arrstring4,
      String[] arrstring5, String[] arrstring6, String string11) {
    this.labelA.setText(string6);
    this.labelPC.setText(string10);
    this.labelIR1.setText(string8);
    this.labelIR2.setText(string9);
    this.labelEq.setText(bl ? "*" : " ");
    this.labelLt.setText(bl2 ? "*" : " ");
    this.labelOv.setText(bl3 ? "*" : " ");
    this.labelAdr1.setText(arrstring[0]);
    this.labelAdr2.setText(arrstring[1]);
    this.labelAdr3.setText(arrstring3[0]);
    this.labelMem1.setText(arrstring2[0]);
    this.labelMem2.setText(arrstring2[1]);
    this.labelMem3.setText(arrstring4[0]);
  }

  @Override
  public void Fehlermeldung(String string) {
    JOptionPane.showMessageDialog(this.fenster, string, "CPU-Fehler", 0);
  }

  @Override
  protected void MenüsErzeugen() {
    super.MenüsErzeugen();
    this.schließenItem.setEnabled(false);
    this.sichernItem.setEnabled(false);
    this.sichernUnterItem.setEnabled(false);
    this.druckenItem.setEnabled(false);
    JMenuItem jMenuItem = new JMenuItem("Widerrufen", 90);
    jMenuItem.setAccelerator(KeyStroke.getKeyStroke(90, kommando));
    jMenuItem.setEnabled(false);
    this.bearbeitenMenü.add(jMenuItem);
    jMenuItem = new JMenuItem("Wiederholen");
    jMenuItem.setAccelerator(KeyStroke.getKeyStroke(90, 64 + kommando));
    jMenuItem.setEnabled(false);
    this.bearbeitenMenü.add(jMenuItem);
    this.bearbeitenMenü.addSeparator();
    jMenuItem = new JMenuItem("Ausschneiden", 88);
    jMenuItem.setAccelerator(KeyStroke.getKeyStroke(88, kommando));
    jMenuItem.setEnabled(false);
    this.bearbeitenMenü.add(jMenuItem);
    jMenuItem = new JMenuItem("Kopieren", 67);
    jMenuItem.setAccelerator(KeyStroke.getKeyStroke(67, kommando));
    jMenuItem.setEnabled(false);
    this.bearbeitenMenü.add(jMenuItem);
    jMenuItem = new JMenuItem("Einfügen", 86);
    jMenuItem.setAccelerator(KeyStroke.getKeyStroke(86, kommando));
    jMenuItem.setEnabled(false);
    this.bearbeitenMenü.add(jMenuItem);
    jMenuItem = new JMenuItem("Alles auswählen", 65);
    jMenuItem.setAccelerator(KeyStroke.getKeyStroke(65, kommando));
    jMenuItem.setEnabled(false);
    this.bearbeitenMenü.add(jMenuItem);
    this.werkzeugMenü.addSeparator();
    jMenuItem = new JMenuItem("Einfache Darstellung");
    jMenuItem.setAccelerator(KeyStroke.getKeyStroke(69, kommando + 512));
    jMenuItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        CpuAnzeige.this.kontrolleur.EinfacheDarstellungAnzeigen();
      }
    });
    this.werkzeugMenü.add(jMenuItem);
    jMenuItem = new JMenuItem("Detaildarstellung");
    jMenuItem.setAccelerator(KeyStroke.getKeyStroke(68, kommando + 512));
    jMenuItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        CpuAnzeige.this.kontrolleur.DetailDarstellungAnzeigen();
      }
    });
    this.werkzeugMenü.add(jMenuItem);
    this.werkzeugMenü.addSeparator();
    jMenuItem = new JMenuItem("Abbruchschranke setzen");
    jMenuItem.setAccelerator(KeyStroke.getKeyStroke(65, kommando + 512));
    jMenuItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        Zeitschranke.Zeigen(CpuAnzeige.this.kontrolleur);
      }
    });
    this.werkzeugMenü.add(jMenuItem);
    this.werkzeugMenü.addSeparator();
    jMenuItem = new JMenuItem("CPU rücksetzen");
    jMenuItem.setAccelerator(KeyStroke.getKeyStroke(82, kommando + 512));
    jMenuItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        CpuAnzeige.this.kontrolleur.ZurückSetzen();
      }
    });
    this.werkzeugMenü.add(jMenuItem);
    this.werkzeugMenü.addSeparator();
    this.erweiterungenItem = new JCheckBoxMenuItem("Erweiterungen");
    this.erweiterungenItem.setEnabled(true);
    this.erweiterungenItem.setSelected(false);
    this.erweiterungenItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        CpuAnzeige.this.kontrolleur.ErweiterungenEinschalten(CpuAnzeige.this.erweiterungenItem.isSelected());
      }
    });
    this.werkzeugMenü.add(this.erweiterungenItem);
  }

  @Override
  protected void DarstellungsgrößeSetzen(boolean bl) {
    this.AnzeigegrößeSetzen(bl);
  }
}
