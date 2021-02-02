package org.bschlangaul.minimaschine;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

class Über {
    private JDialog dialog = new JDialog((Frame)null);
    private static Über über = null;

    private Über() {
        this.dialog.setTitle("Über Minimaschine");
        JPanel jPanel = (JPanel)this.dialog.getContentPane();
        jPanel.setLayout(new BorderLayout());
        JPanel jPanel2 = new JPanel();
        jPanel2.setVisible(true);
        jPanel.add((Component)jPanel2, "Center");
        jPanel2.setLayout(new GridLayout(6, 1));
        JLabel jLabel = new JLabel("Minimaschine");
        jLabel.setHorizontalAlignment(0);
        jPanel2.add(jLabel);
        jLabel = new JLabel("V2.0");
        jLabel.setHorizontalAlignment(0);
        jPanel2.add(jLabel);
        jLabel = new JLabel(" ");
        jPanel2.add(jLabel);
        jLabel = new JLabel("Ein Emulationsprogramm für eine einfache CPU  ");
        jLabel.setHorizontalAlignment(0);
        jPanel2.add(jLabel);
        jLabel = new JLabel("\u00a9 2009-2019 Albert Wiedemann");
        jLabel.setHorizontalAlignment(0);
        jPanel2.add(jLabel);
        jLabel = new JLabel(" ");
        jPanel2.add(jLabel);
        jPanel2 = new JPanel();
        jPanel2.setVisible(true);
        jPanel.add((Component)jPanel2, "South");
        jPanel2.setLayout(new FlowLayout(1));
        JButton jButton = new JButton("Ok");
        jPanel2.add(jButton);
        jButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Über.this.dialog.setVisible(false);
            }
        });
        this.dialog.setModal(true);
        this.dialog.setResizable(false);
        this.dialog.pack();
        this.dialog.setVisible(false);
    }

    static void Zeigen() {
        if (über == null) {
            über = new Über();
        }
        Über.über.dialog.setVisible(true);
    }
}
