package org.bschlangaul.minimaschine;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

class Zeitschranke {
  private JDialog dialog;
  private static Zeitschranke schranke = null;
  private KontrolleurInterface kontrolleur;

  private Zeitschranke(KontrolleurInterface kontrolleurInterface) {
    this.kontrolleur = kontrolleurInterface;
    this.dialog = new JDialog((Frame) null);
    this.dialog.setTitle("Prozessorzeitschranke");
    JPanel jPanel = (JPanel) this.dialog.getContentPane();
    jPanel.setLayout(new BorderLayout());
    JPanel jPanel2 = new JPanel();
    jPanel2.setVisible(true);
    jPanel.add((Component) jPanel2, "Center");
    jPanel2.setLayout(new FlowLayout(0));
    JLabel jLabel = new JLabel("Geben Sie die Zeitschranke in Sekunden ein");
    jPanel2.add(jLabel);
    final JTextField jTextField = new JTextField("     ");
    jTextField.setMinimumSize(new Dimension(80, 30));
    jPanel2.add(jTextField);
    jPanel2 = new JPanel();
    jPanel2.setVisible(true);
    jPanel.add((Component) jPanel2, "South");
    jPanel2.setLayout(new FlowLayout(1));
    JButton jButton = new JButton("Ok");
    jPanel2.add(jButton);
    jButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        try {
          int n = Integer.parseInt(jTextField.getText());
          Zeitschranke.this.kontrolleur.ZeitschrankeSetzen(n);
          Zeitschranke.this.dialog.setVisible(false);
        } catch (Exception exception) {
          jTextField.selectAll();
        }
      }
    });
    this.dialog.setModal(true);
    this.dialog.setResizable(false);
    this.dialog.pack();
    this.dialog.setVisible(false);
  }

  static void Zeigen(KontrolleurInterface kontrolleurInterface) {
    if (schranke == null) {
      schranke = new Zeitschranke(kontrolleurInterface);
    }
    Zeitschranke.schranke.dialog.setVisible(true);
  }
}
