package org.bschlangaul.minimaschine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import javax.swing.JComponent;

class CpuBild extends JComponent {
  private static final long serialVersionUID = -3192295499442985896L;
  protected int breiteCpu = 380;
  protected int höheCpu = 180;
  protected int breiteSpeicher = 160;
  protected int höheSpeicher = 240;
  protected int breiteKasten = 80;
  protected int höheKasten = 20;
  protected int minBreite;
  protected int minHöhe;
  protected int[] xKoordinaten;
  protected int[] yKoordinaten;
  protected boolean erweitert = false;
  private String datenWert = "";
  private String adressWert = "";
  private String alu1 = "0";
  private String alu2 = "0";
  private String alu3 = "0";
  private String ac = "0";
  private String sr = "Z:  N:  V:";
  private String ir1 = "0";
  private String ir2 = "0";
  private String pc = "0";
  private String sp = "0";
  private String[] progadr = new String[] { "", "", "", "" };
  private String[] progmem = new String[] { "", "", "", "" };
  private String[] dataadr = new String[] { "", "" };
  private String[] datamem = new String[] { "", "" };
  private String[] stackadr = new String[] { "", "", "" };
  private String[] stackmem = new String[] { "", "", "" };
  private String mikroschritt = "mikro";

  CpuBild() {
    this.minBreite = this.breiteCpu + this.breiteSpeicher + 40;
    this.minHöhe = this.höheSpeicher + 120;
    this.xKoordinaten = new int[] { 0, 40, 80, 120, 80, 60, 40 };
    this.yKoordinaten = new int[] { 0, 60, 60, 0, 0, 30, 0 };
  }

  void DatenSetzen(String string, String string2, String string3, String string4, String string5, String string6,
      String string7, String string8, String string9, String string10, String string11, String[] arrstring,
      String[] arrstring2, String[] arrstring3, String[] arrstring4, String[] arrstring5, String[] arrstring6,
      boolean bl, String string12) {
    this.datenWert = string;
    this.adressWert = string2;
    this.alu1 = string3;
    this.alu2 = string4;
    this.alu3 = string5;
    this.ac = string6;
    this.sr = string7;
    this.ir1 = string8;
    this.ir2 = string9;
    this.pc = string10;
    this.sp = string11;
    this.progadr = arrstring;
    this.progmem = arrstring2;
    this.dataadr = arrstring3;
    this.datamem = arrstring4;
    this.stackadr = arrstring5;
    this.stackmem = arrstring6;
    this.erweitert = bl;
    this.mikroschritt = string12;
    this.invalidate();
    this.repaint();
  }

  @Override
  public Dimension getMinimumSize() {
    return new Dimension(this.minBreite, this.minHöhe);
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(this.minBreite + 10, this.minHöhe + 20);
  }

  @Override
  protected void paintComponent(Graphics graphics) {
    int n;
    int n2 = this.getWidth();
    int n3 = this.getHeight();
    if (this.isOpaque()) {
      graphics.setColor(this.getBackground());
      graphics.fillRect(0, 0, n2, n3);
      graphics.setColor(this.getForeground());
    }
    graphics.setColor(Color.red);
    graphics.drawLine(10, this.höheKasten + 10, n2 - 10, this.höheKasten + 10);
    graphics.drawLine(10, this.höheKasten + 11, n2 - 10, this.höheKasten + 11);
    graphics.drawString("Datenbus", 15, this.höheKasten);
    graphics.drawLine(190, this.höheKasten + 10, 190, n3 / 2 - this.höheCpu / 2 - 1);
    graphics.drawLine(191, this.höheKasten + 10, 191, n3 / 2 - this.höheCpu / 2 - 1);
    graphics.drawLine(n2 - 90, this.höheKasten + 10, n2 - 90, n3 / 2 - this.höheSpeicher / 2 - 1);
    graphics.drawLine(n2 - 89, this.höheKasten + 10, n2 - 89, n3 / 2 - this.höheSpeicher / 2 - 1);
    if (!"".equals(this.datenWert)) {
      graphics.drawString(this.datenWert, this.breiteCpu - 10, this.höheKasten);
    }
    graphics.setColor(Color.black);
    if (!this.mikroschritt.equals("")) {
      graphics.drawString("Mikroschritt: " + this.mikroschritt, 200,
          (this.höheKasten + n3 / 2 - this.höheCpu / 2) / 2 + 10);
    }
    graphics.drawRect(10, n3 / 2 - this.höheCpu / 2, this.breiteCpu, this.höheCpu);
    graphics.drawRect(20, n3 / 2 - this.höheCpu / 2 + this.höheKasten + 10, this.breiteKasten, this.höheKasten);
    graphics.drawString(this.ac, 25, n3 / 2 - this.höheCpu / 2 + this.höheKasten * 2 + 5);
    graphics.drawRect(this.breiteCpu - 2 * this.breiteKasten, n3 / 2 - this.höheCpu / 2 + this.höheKasten + 10,
        this.breiteKasten, this.höheKasten);
    if (this.ir1.length() > 0 && this.ir1.charAt(0) >= '0' && this.ir1.charAt(0) <= '9') {
      graphics.drawString(this.ir1, this.breiteCpu - 2 * this.breiteKasten + 5,
          n3 / 2 - this.höheCpu / 2 + this.höheKasten * 2 + 5);
    } else {
      graphics.setColor(new Color(255, 127, 0));
      graphics.drawString(this.ir1, this.breiteCpu - 2 * this.breiteKasten + 5,
          n3 / 2 - this.höheCpu / 2 + this.höheKasten * 2 + 5);
      graphics.setColor(Color.black);
    }
    graphics.drawRect(this.breiteCpu - this.breiteKasten, n3 / 2 - this.höheCpu / 2 + this.höheKasten + 10,
        this.breiteKasten, this.höheKasten);
    graphics.drawString(this.ir2, this.breiteCpu - this.breiteKasten + 5,
        n3 / 2 - this.höheCpu / 2 + this.höheKasten * 2 + 5);
    graphics.drawRect(this.breiteCpu - 2 * this.breiteKasten, n3 / 2 - this.höheCpu / 2 + 3 * this.höheKasten + 30,
        this.breiteKasten, this.höheKasten);
    graphics.drawString(this.pc, this.breiteCpu - 2 * this.breiteKasten + 5,
        n3 / 2 - this.höheCpu / 2 + 4 * this.höheKasten + 25);
    if (this.erweitert) {
      graphics.drawRect(this.breiteCpu - 2 * this.breiteKasten, n3 / 2 - this.höheCpu / 2 + 5 * this.höheKasten + 50,
          this.breiteKasten, this.höheKasten);
      graphics.drawString(this.sp, this.breiteCpu - 2 * this.breiteKasten + 5,
          n3 / 2 - this.höheCpu / 2 + 6 * this.höheKasten + 45);
    }
    Polygon polygon = new Polygon(this.xKoordinaten, this.yKoordinaten, this.xKoordinaten.length);
    polygon.translate(40, n3 / 2 - this.höheCpu / 2 + 2 * this.höheKasten + 20);
    graphics.drawPolygon(polygon);
    graphics.drawRect(20, n3 / 2 + this.höheCpu / 2 - (this.höheKasten + 10), this.breiteKasten, this.höheKasten);
    graphics.drawString(this.sr, 25, n3 / 2 + this.höheCpu / 2 - 15);
    graphics.setColor(new Color(255, 0, 255));
    graphics.drawString(this.alu1, 40 + this.xKoordinaten[1] / 4, n3 / 2 - this.höheCpu / 2 + 3 * this.höheKasten + 15);
    graphics.drawString(this.alu2, 40 + this.xKoordinaten[4], n3 / 2 - this.höheCpu / 2 + 3 * this.höheKasten + 15);
    graphics.drawString(this.alu3, 40 + this.xKoordinaten[1],
        n3 / 2 - this.höheCpu / 2 + 2 * this.höheKasten + this.yKoordinaten[1] + 15);
    graphics.setColor(Color.black);
    graphics.drawRect(n2 - (this.breiteSpeicher + 10), n3 / 2 - this.höheSpeicher / 2, this.breiteSpeicher,
        this.höheSpeicher);
    for (n = 0; n < this.progmem.length; ++n) {
      graphics.drawRect(n2 - (this.breiteKasten + 20), n3 / 2 - this.höheSpeicher / 2 + 5 + n * this.höheKasten,
          this.breiteKasten, this.höheKasten);
      graphics.drawString(this.progmem[n], n2 - (this.breiteKasten + 15),
          n3 / 2 - this.höheSpeicher / 2 + this.höheKasten + n * this.höheKasten);
    }
    if (this.erweitert) {
      for (n = 0; n < this.datamem.length; ++n) {
        graphics
            .drawRect(n2 - (this.breiteKasten + 20),
                n3 / 2 + this.höheKasten * (this.progmem.length - this.stackmem.length) / 2
                    - this.höheKasten * this.datamem.length / 2 + n * this.höheKasten,
                this.breiteKasten, this.höheKasten);
        graphics.drawString(this.datamem[n], n2 - (this.breiteKasten + 15),
            n3 / 2 + this.höheKasten * (this.progmem.length - this.stackmem.length) / 2
                - this.höheKasten * this.datamem.length / 2 + this.höheKasten - 5 + n * this.höheKasten);
      }
      graphics.drawString(". . .", n2 - (this.breiteKasten + 10),
          n3 / 2 + this.höheKasten * (this.progmem.length - this.stackmem.length) / 4 - this.höheSpeicher / 4 + 5
              + (this.progmem.length * this.höheKasten - this.höheKasten * this.datamem.length / 2) / 2);
      graphics.drawString(". . .", n2 - (this.breiteKasten + 10),
          n3 / 2 + this.höheSpeicher / 4 + this.höheKasten * (this.progmem.length - this.stackmem.length) / 4
              + this.höheKasten * this.datamem.length / 4 - this.höheKasten * this.stackmem.length / 2);
      for (n = 0; n < this.stackmem.length; ++n) {
        graphics.drawRect(n2 - (this.breiteKasten + 20),
            n3 / 2 + this.höheSpeicher / 2 - this.höheKasten * this.stackmem.length - 5 + n * this.höheKasten,
            this.breiteKasten, this.höheKasten);
        graphics.drawString(this.stackmem[n], n2 - (this.breiteKasten + 15), n3 / 2 + this.höheSpeicher / 2
            - this.höheKasten * this.stackmem.length + this.höheKasten - 10 + n * this.höheKasten);
      }
    } else {
      for (n = 0; n < this.datamem.length; ++n) {
        graphics.drawRect(n2 - (this.breiteKasten + 20),
            n3 / 2 + this.höheSpeicher / 2 - this.höheKasten * this.datamem.length - 5 + n * this.höheKasten,
            this.breiteKasten, this.höheKasten);
        graphics.drawString(this.datamem[n], n2 - (this.breiteKasten + 15), n3 / 2 + this.höheSpeicher / 2
            - this.höheKasten * this.datamem.length + this.höheKasten - 10 + n * this.höheKasten);
      }
      graphics.drawString(". . .", n2 - (this.breiteKasten + 10), n3 / 2 + this.höheKasten);
    }
    graphics.setColor(Color.blue);
    graphics.drawLine(10, n3 - 30, n2 - 10, n3 - 30);
    graphics.drawLine(10, n3 - 29, n2 - 10, n3 - 29);
    graphics.drawString("Adressbus", 15, n3 - 40);
    if (!"".equals(this.adressWert)) {
      graphics.drawString(this.adressWert, this.breiteCpu - 10, n3 - 40);
    }
    graphics.drawLine(190, n3 - 30, 190, n3 / 2 + this.höheCpu / 2 + 1);
    graphics.drawLine(191, n3 - 30, 191, n3 / 2 + this.höheCpu / 2 + 1);
    graphics.drawLine(n2 - 90, n3 - 30, n2 - 90, n3 / 2 + this.höheSpeicher / 2 + 1);
    graphics.drawLine(n2 - 89, n3 - 30, n2 - 89, n3 / 2 + this.höheSpeicher / 2 + 1);
    graphics.drawString("Akkumulator", 25, n3 / 2 - this.höheCpu / 2 + this.höheKasten);
    graphics.drawString("Status", 25, n3 / 2 + this.höheCpu / 2 - (this.höheKasten + 20));
    graphics.drawString("Befehlsregister", this.breiteCpu - 2 * this.breiteKasten + 5,
        n3 / 2 - this.höheCpu / 2 + this.höheKasten);
    graphics.drawString("Programmzähler", this.breiteCpu - 2 * this.breiteKasten + 5,
        n3 / 2 - this.höheCpu / 2 + 3 * this.höheKasten + 20);
    if (this.erweitert) {
      graphics.drawString("Stackpointer", this.breiteCpu - 2 * this.breiteKasten + 5,
          n3 / 2 - this.höheCpu / 2 + 5 * this.höheKasten + 40);
    }
    for (n = 0; n < this.progmem.length; ++n) {
      graphics.drawString(this.progadr[n], n2 - (this.breiteSpeicher + 5),
          n3 / 2 - this.höheSpeicher / 2 + this.höheKasten + n * this.höheKasten);
    }
    if (this.erweitert) {
      for (n = 0; n < this.datamem.length; ++n) {
        graphics.drawString(this.dataadr[n], n2 - (this.breiteSpeicher + 5),
            n3 / 2 + this.höheKasten * (this.progmem.length - this.stackmem.length) / 2
                - this.höheKasten * this.datamem.length / 2 + this.höheKasten - 5 + n * this.höheKasten);
      }
      for (n = 0; n < this.stackmem.length; ++n) {
        graphics.drawString(this.stackadr[n], n2 - (this.breiteSpeicher + 5), n3 / 2 + this.höheSpeicher / 2
            - this.höheKasten * this.stackmem.length + this.höheKasten - 10 + n * this.höheKasten);
      }
    } else {
      for (n = 0; n < this.datamem.length; ++n) {
        graphics.drawString(this.dataadr[n], n2 - (this.breiteSpeicher + 5), n3 / 2 + this.höheSpeicher / 2
            - this.höheKasten * this.datamem.length + this.höheKasten - 10 + n * this.höheKasten);
      }
    }
    graphics.setColor(this.getForeground());
  }
}
