/*
 * Decompiled with CFR 0.150.
 */
import MODEL.CpuBeobachter;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

class CpuAnzeige2
extends Anzeige
implements CpuBeobachter {
    private CpuBild bild;
    private CpuBildGroß bildgross;
    private JPanel content;
    private JCheckBoxMenuItem erweiterungenItem;

    CpuAnzeige2(KontrolleurInterface kontrolleurInterface) {
        super(kontrolleurInterface);
    }

    @Override
    protected void OberflächeAufbauen() {
        this.fenster = new JFrame("CPU-Kontrolle");
        this.fenster.setJMenuBar(this.menüZeile);
        this.content = (JPanel)this.fenster.getContentPane();
        this.content.setLayout(new BorderLayout());
        this.bildgross = new CpuBildGroß();
        this.bildgross.setOpaque(false);
        this.bild = new CpuBild();
        this.bild.setOpaque(false);
        this.content.add((Component)this.bild, "Center");
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout());
        this.content.add((Component)jPanel, "South");
        JButton jButton = new JButton("Ausführen");
        jPanel.add(jButton);
        jButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                CpuAnzeige2.this.kontrolleur.Ausführen();
            }
        });
        jButton = new JButton("Einzelschritt");
        jPanel.add(jButton);
        jButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                CpuAnzeige2.this.kontrolleur.EinzelSchritt();
            }
        });
        jButton = new JButton("Mikroschritt");
        jPanel.add(jButton);
        jButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                CpuAnzeige2.this.kontrolleur.MikroSchritt();
            }
        });
        this.content.doLayout();
        this.fenster.setSize(600, 400);
        this.fenster.setVisible(true);
    }

    @Override
    public void Befehlsmeldung(String string, String string2, String string3, String string4, String string5, String string6, String string7, boolean bl, boolean bl2, boolean bl3, String string8, String string9, String string10, String[] arrstring, String[] arrstring2, String[] arrstring3, String[] arrstring4, String[] arrstring5, String[] arrstring6, String string11) {
        this.bild.DatenSetzen(string, string2, string3, string4, string5, string6, "Z:" + (bl ? "*" : " ") + " N:" + (bl2 ? "*" : " ") + " V:" + (bl3 ? "*" : " "), string8, string9, string10, string7, arrstring, arrstring2, arrstring3, arrstring4, arrstring5, arrstring6, this.erweiterungenItem.isSelected(), string11);
        this.bildgross.DatenSetzen(string, string2, string3, string4, string5, string6, "Z:" + (bl ? "*" : " ") + " N:" + (bl2 ? "*" : " ") + " V:" + (bl3 ? "*" : " "), string8, string9, string10, string7, arrstring, arrstring2, arrstring3, arrstring4, arrstring5, arrstring6, this.erweiterungenItem.isSelected(), string11);
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
        jMenuItem.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                CpuAnzeige2.this.kontrolleur.EinfacheDarstellungAnzeigen();
            }
        });
        this.werkzeugMenü.add(jMenuItem);
        jMenuItem = new JMenuItem("Detaildarstellung");
        jMenuItem.setAccelerator(KeyStroke.getKeyStroke(68, kommando + 512));
        jMenuItem.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                CpuAnzeige2.this.kontrolleur.DetailDarstellungAnzeigen();
            }
        });
        this.werkzeugMenü.add(jMenuItem);
        this.werkzeugMenü.addSeparator();
        jMenuItem = new JMenuItem("Abbruchschranke setzen");
        jMenuItem.setAccelerator(KeyStroke.getKeyStroke(65, kommando + 512));
        jMenuItem.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Zeitschranke.Zeigen(CpuAnzeige2.this.kontrolleur);
            }
        });
        this.werkzeugMenü.add(jMenuItem);
        this.werkzeugMenü.addSeparator();
        jMenuItem = new JMenuItem("CPU rücksetzen");
        jMenuItem.setAccelerator(KeyStroke.getKeyStroke(82, kommando + 512));
        jMenuItem.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                CpuAnzeige2.this.kontrolleur.ZurückSetzen();
            }
        });
        this.werkzeugMenü.add(jMenuItem);
        this.werkzeugMenü.addSeparator();
        this.erweiterungenItem = new JCheckBoxMenuItem("Erweiterungen");
        this.erweiterungenItem.setEnabled(true);
        this.erweiterungenItem.setSelected(false);
        this.erweiterungenItem.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                CpuAnzeige2.this.kontrolleur.ErweiterungenEinschalten(CpuAnzeige2.this.erweiterungenItem.isSelected());
            }
        });
        this.werkzeugMenü.add(this.erweiterungenItem);
    }

    @Override
    protected void DarstellungsgrößeSetzen(boolean bl) {
        if (bl) {
            this.content.remove(this.bild);
            this.content.add((Component)this.bildgross, "Center");
            this.bildgross.invalidate();
            this.bildgross.repaint();
            this.fenster.setSize(900, 600);
        } else {
            this.content.remove(this.bildgross);
            this.content.add((Component)this.bild, "Center");
            this.bild.invalidate();
            this.bild.repaint();
            this.fenster.setSize(600, 400);
        }
        this.content.doLayout();
        this.content.revalidate();
    }
}
