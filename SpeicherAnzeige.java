
/*
 * Decompiled with CFR 0.150.
 */
import MODEL.AssemblerBefehle;
import MODEL.SpeicherBeobachter;
import MODEL.SpeicherLesen;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

class SpeicherAnzeige extends Anzeige implements SpeicherBeobachter {
  private JScrollPane scroll;
  private TableModel dataModel;
  private JTable table;
  private JTable tableGross;
  private JScrollPane scrollpane;
  private JScrollPane scrollpaneGross;
  private int geändert = -1;
  private boolean editierbar = false;
  private JCheckBoxMenuItem editItem;
  private JCheckBoxMenuItem hexaItem;
  private JCheckBoxMenuItem opcodeItem;
  private boolean hexaDarstellung = false;
  private boolean opcodeDarstellung = false;
  private JPanel content;

  SpeicherAnzeige(KontrolleurInterface kontrolleurInterface) {
    super(kontrolleurInterface);
  }

  @Override
  protected void OberflächeAufbauen() {
    int n;
    this.fenster = new JFrame("Speicheranzeige");
    this.fenster.setJMenuBar(this.menüZeile);
    this.fenster.setVisible(true);
    this.content = (JPanel) this.fenster.getContentPane();
    this.content.setLayout(new BorderLayout());
    this.dataModel = new AbstractTableModel() {

      @Override
      public String getColumnName(int n) {
        return n == 0 ? "" : "" + (n - 1);
      }

      @Override
      public int getColumnCount() {
        return 11;
      }

      @Override
      public int getRowCount() {
        return 6555;
      }

      @Override
      public Object getValueAt(int n, int n2) {
        if (n2 == 0) {
          return new Integer(n * 10);
        }
        int n3 = n * 10 + (n2 - 1);
        if (n3 < 65536) {
          if (SpeicherAnzeige.this.opcodeDarstellung && n2 % 2 != 0) {
            int n4 = SpeicherLesen.WortOhneVorzeichenGeben(n3);
            int n5 = n4 / 256;
            String string = AssemblerBefehle.AssemblerbefehleGeben().MnemonicGeben(n4 % 256);
            if (n5 == 2) {
              string = string + "I";
            } else if (n5 == 3) {
              string = string + " @";
            }
            return string;
          }
          if (SpeicherAnzeige.this.hexaDarstellung) {
            String string = "0000" + Integer.toHexString(SpeicherLesen.WortOhneVorzeichenGeben(n3)).toUpperCase();
            return string.substring(string.length() - 4);
          }
          return new Integer(SpeicherLesen.WortMitVorzeichenGeben(n3));
        }
        return "";
      }

      @Override
      public boolean isCellEditable(int n, int n2) {
        return SpeicherAnzeige.this.editierbar && n2 != 0;
      }

      @Override
      public void setValueAt(Object object, int n, int n2) {
        if (object instanceof String) {
          try {
            int n3;
            String string = (String) object;
            if (SpeicherAnzeige.this.hexaDarstellung) {
              if (string.startsWith("0x")) {
                string = string.substring(2);
              }
              n3 = Integer.parseInt(string, 16);
            } else {
              n3 = Integer.parseInt(string);
            }
            if (-32768 <= n3 && n3 <= 65535) {
              SpeicherAnzeige.this.geändert = -1;
              SpeicherLesen.WortSetzen(n * 10 + (n2 - 1), n3);
              SpeicherAnzeige.this.geändert = -1;
            }
          } catch (Exception exception) {
            // empty catch block
          }
        }
      }
    };
    this.table = new JTable(this.dataModel);
    for (n = 0; n < 11; ++n) {
      this.table.getColumnModel().getColumn(n).setPreferredWidth(50);
    }
    this.table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
      private Font f = null;
      private Font fbold = null;

      @Override
      public Component getTableCellRendererComponent(JTable jTable, Object object, boolean bl, boolean bl2, int n,
          int n2) {
        Component component = super.getTableCellRendererComponent(jTable, object, bl, bl2, n, n2);
        if (this.f == null) {
          this.f = component.getFont();
          this.fbold = this.f.deriveFont(1);
        }
        if (n2 == 0) {
          component.setBackground(Color.gray);
          component.setFont(this.fbold);
        } else {
          component.setBackground(Color.lightGray);
          component.setFont(this.f);
          if (n * 10 + (n2 - 1) == SpeicherAnzeige.this.geändert) {
            component.setForeground(Color.red);
          } else {
            component.setForeground(Color.black);
          }
        }
        return component;
      }
    });
    this.table.getTableHeader().setFont(this.table.getTableHeader().getFont().deriveFont(1));
    this.scrollpane = new JScrollPane(this.table);
    this.tableGross = new JTable(this.dataModel);
    this.tableGross.setRowHeight(30);
    for (n = 0; n < 11; ++n) {
      this.tableGross.getColumnModel().getColumn(n).setPreferredWidth(100);
    }
    this.tableGross.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
      private Font f = null;
      private Font fbold = null;

      @Override
      public Component getTableCellRendererComponent(JTable jTable, Object object, boolean bl, boolean bl2, int n,
          int n2) {
        Component component = super.getTableCellRendererComponent(jTable, object, bl, bl2, n, n2);
        if (this.f == null) {
          this.f = component.getFont();
          this.f = new Font(this.f.getName(), this.f.getStyle(), 24);
          this.fbold = this.f.deriveFont(1);
        }
        if (n2 == 0) {
          component.setBackground(Color.gray);
          component.setFont(this.fbold);
        } else {
          component.setBackground(Color.lightGray);
          component.setFont(this.f);
          if (n * 10 + (n2 - 1) == SpeicherAnzeige.this.geändert) {
            component.setForeground(Color.red);
          } else {
            component.setForeground(Color.black);
          }
        }
        return component;
      }
    });
    Font font = this.tableGross.getTableHeader().getFont();
    this.tableGross.getTableHeader().setFont(new Font(font.getName(), 1, 24));
    this.scrollpaneGross = new JScrollPane(this.tableGross);
    this.content.add((Component) this.scrollpane, "Center");
    this.fenster.setSize(new Dimension(800, 500));
    this.fenster.setLocation(600, 50);
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
    jMenuItem = new JMenuItem("Speicher löschen", 65);
    jMenuItem.setEnabled(true);
    jMenuItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        SpeicherAnzeige.this.kontrolleur.SpeicherLöschen();
      }
    });
    this.werkzeugMenü.add(jMenuItem);
    this.werkzeugMenü.addSeparator();
    this.hexaItem = new JCheckBoxMenuItem("Darstellung hexadezimal");
    this.hexaItem.setEnabled(true);
    this.hexaItem.setSelected(false);
    this.hexaItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        SpeicherAnzeige.this.hexaDarstellung = SpeicherAnzeige.this.hexaItem.isSelected();
        SpeicherAnzeige.this.scrollpane.invalidate();
        SpeicherAnzeige.this.scrollpane.repaint();
        SpeicherAnzeige.this.scrollpaneGross.invalidate();
        SpeicherAnzeige.this.scrollpaneGross.repaint();
        SpeicherAnzeige.this.kontrolleur.CpuHexaSetzen(SpeicherAnzeige.this.hexaDarstellung);
        SpeicherAnzeige.this.kontrolleur.CpuInvalidieren();
      }
    });
    this.opcodeItem = new JCheckBoxMenuItem("Opcodes anzeigen");
    this.opcodeItem.setEnabled(true);
    this.opcodeItem.setSelected(false);
    this.opcodeItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        SpeicherAnzeige.this.opcodeDarstellung = SpeicherAnzeige.this.opcodeItem.isSelected();
        SpeicherAnzeige.this.scrollpane.invalidate();
        SpeicherAnzeige.this.scrollpane.repaint();
        SpeicherAnzeige.this.scrollpaneGross.invalidate();
        SpeicherAnzeige.this.scrollpaneGross.repaint();
      }
    });
    this.werkzeugMenü.add(this.hexaItem);
    this.werkzeugMenü.add(this.opcodeItem);
    this.werkzeugMenü.addSeparator();
    this.editItem = new JCheckBoxMenuItem("Speicher editieren");
    this.editItem.setEnabled(true);
    this.editItem.setSelected(false);
    this.editItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        SpeicherAnzeige.this.editierbar = SpeicherAnzeige.this.editItem.isSelected();
      }
    });
    this.werkzeugMenü.add(this.editItem);
  }

  @Override
  protected void DarstellungsgrößeSetzen(boolean bl) {
    if (bl) {
      this.content.remove(this.scrollpane);
      this.content.add((Component) this.scrollpaneGross, "Center");
      this.scrollpaneGross.invalidate();
      this.scrollpaneGross.repaint();
    } else {
      this.content.remove(this.scrollpaneGross);
      this.content.add((Component) this.scrollpane, "Center");
      this.scrollpane.invalidate();
      this.scrollpane.repaint();
    }
    this.content.revalidate();
  }

  @Override
  public void SpeicherGeändertMelden(int n) {
    this.geändert = n;
    this.scrollpane.invalidate();
    this.scrollpane.repaint();
    this.scrollpaneGross.invalidate();
    this.scrollpaneGross.repaint();
  }
}
