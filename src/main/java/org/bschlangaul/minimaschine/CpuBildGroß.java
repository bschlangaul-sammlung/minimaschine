package org.bschlangaul.minimaschine;

import java.awt.Font;
import java.awt.Graphics;

class CpuBildGroß extends CpuBild {

  private static final long serialVersionUID = 6899616626691105780L;

  CpuBildGroß() {
    this.breiteCpu = 570;
    this.höheCpu = 240;
    this.breiteSpeicher = 240;
    this.höheSpeicher = 370;
    this.minBreite = this.breiteCpu + this.breiteSpeicher + 40;
    this.minHöhe = this.höheSpeicher + 120;
    this.breiteKasten = 120;
    this.höheKasten = 30;
    this.xKoordinaten = new int[] { 0, 60, 120, 180, 120, 90, 60 };
    this.yKoordinaten = new int[] { 0, 90, 90, 0, 0, 45, 0 };
  }

  @Override
  protected void paintComponent(Graphics graphics) {
    graphics.setFont(new Font(graphics.getFont().getName(), graphics.getFont().getStyle(), 24));
    super.paintComponent(graphics);
  }
}
