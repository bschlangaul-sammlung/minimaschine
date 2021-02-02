package org.bschlangaul.minimaschine;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

class AssemblerAnzeige extends Anzeige {
  private JTextArea text;
  private JScrollPane scroll;

  AssemblerAnzeige(KontrolleurInterface kontrolleurInterface, String string) {
    super(kontrolleurInterface);
    this.text.setText(string);
  }

  @Override
  protected void OberflächeAufbauen() {
    this.fenster = new JFrame("Assemblertext");
    this.MenüsErzeugen();
    this.fenster.setJMenuBar(this.menüZeile);
    this.fenster.setVisible(true);
    JPanel jPanel = (JPanel) this.fenster.getContentPane();
    jPanel.setLayout(new BorderLayout());
    this.text = new JTextArea();
    this.text.setEditable(false);
    this.scroll = new JScrollPane(this.text, 20, 30);
    jPanel.add((Component) this.scroll, "Center");
    this.fenster.setSize(400, 200);
    this.fenster.addWindowListener(new WindowAdapter() {

      @Override
      public void windowClosing(WindowEvent windowEvent) {
        AssemblerAnzeige.this.kontrolleur.SchließenAusführen(AssemblerAnzeige.this.selbst);
      }
    });
    this.fenster.setDefaultCloseOperation(2);
  }

  @Override
  protected void MenüsErzeugen() {
    super.MenüsErzeugen();
    this.schließenItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        AssemblerAnzeige.this.kontrolleur.SchließenAusführen(AssemblerAnzeige.this.selbst);
        AssemblerAnzeige.this.fenster.dispose();
      }
    });
    this.sichernItem.setEnabled(false);
    this.sichernUnterItem.setEnabled(false);
    this.druckenItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent actionEvent) {
      }
    });
    JMenuItem jMenuItem = new JMenuItem("Ausschneiden", 88);
    jMenuItem.setAccelerator(KeyStroke.getKeyStroke(88, 256));
    jMenuItem.setEnabled(false);
    this.bearbeitenMenü.add(jMenuItem);
    jMenuItem = new JMenuItem("Kopieren", 67);
    jMenuItem.setAccelerator(KeyStroke.getKeyStroke(67, 256));
    jMenuItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        AssemblerAnzeige.this.text.copy();
      }
    });
    this.bearbeitenMenü.add(jMenuItem);
    jMenuItem = new JMenuItem("Einfügen", 86);
    jMenuItem.setAccelerator(KeyStroke.getKeyStroke(86, 256));
    jMenuItem.setEnabled(false);
    this.bearbeitenMenü.add(jMenuItem);
    jMenuItem = new JMenuItem("Alles auswählen", 65);
    jMenuItem.setAccelerator(KeyStroke.getKeyStroke(65, 256));
    jMenuItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        AssemblerAnzeige.this.text.selectAll();
      }
    });
    this.größeItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        if (AssemblerAnzeige.this.größeItem.isSelected()) {
          AssemblerAnzeige.this.FontgrößeSetzen(24);
        } else {
          AssemblerAnzeige.this.FontgrößeSetzen(13);
        }
        AssemblerAnzeige.this.text.invalidate();
        AssemblerAnzeige.this.text.repaint();
      }
    });
  }

  @Override
  protected void DarstellungsgrößeSetzen(boolean bl) {
    if (bl) {
      this.FontgrößeSetzen(24);
    } else {
      this.FontgrößeSetzen(13);
    }
    this.text.invalidate();
    this.text.repaint();
  }

  private void FontgrößeSetzen(int n) {
    Font font = this.text.getFont();
    this.text.setFont(new Font(font.getName(), font.getStyle(), n));
  }
}
