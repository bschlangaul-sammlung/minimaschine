package org.bschlangaul.minimaschine;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.PrintJob;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;
import javax.swing.event.UndoableEditEvent;
import javax.swing.filechooser.FileFilter;
import javax.swing.undo.UndoManager;

class Editor extends Anzeige {
  private static File letzterOrdner = null;
  private JEditorPane editor;
  private JScrollPane scroll;
  private JTextArea zeilenNummern;
  private JLabel status;
  private JMenuItem undoItem;
  private JMenuItem redoItem;
  private JFileChooser fileChooser;
  private File file;
  private UndoManager undo;
  private String sicherungsstand = "";

  Editor(KontrolleurInterface kontrolleurInterface) {
    super(kontrolleurInterface);
  }

  void ZeilenNummernSetzen(boolean bl) {
    String[] arrstring = this.editor.getText().split("\n");
    String string = "";
    for (int i = 1; i <= arrstring.length; ++i) {
      string = string + i + " \n";
    }
    if (bl) {
      string = string + (arrstring.length + 1) + " \n";
    }
    this.zeilenNummern.setText(string);
  }

  @Override
  protected void OberflächeAufbauen() {
    this.undo = new UndoManager() {
      private static final long serialVersionUID = -1696452306423710125L;

      @Override
      public void undoableEditHappened(UndoableEditEvent undoableEditEvent) {
        super.undoableEditHappened(undoableEditEvent);
        Editor.this.undoItem.setEnabled(this.canUndo());
        Editor.this.redoItem.setEnabled(this.canRedo());
      }
    };
    this.fenster = new JFrame("Editor");
    this.fenster.setJMenuBar(this.menüZeile);
    JPanel jPanel = (JPanel) this.fenster.getContentPane();
    jPanel.setLayout(new BorderLayout());
    this.editor = new JEditorPane("text/plain", null) {
      private static final long serialVersionUID = 8538143889816663243L;

      @Override
      public void cut() {
        super.cut();
        Editor.this.ZeilenNummernSetzen(false);
      }

      @Override
      public void paste() {
        super.paste();
        Editor.this.ZeilenNummernSetzen(false);
      }
    };
    this.editor.getDocument().addUndoableEditListener(this.undo);
    this.zeilenNummern = new JTextArea("1 \n");
    this.zeilenNummern.setFont(this.editor.getFont());
    this.zeilenNummern.setBackground(new Color(255, 255, 200));
    this.zeilenNummern.setBorder(LineBorder.createGrayLineBorder());
    this.zeilenNummern.setEditable(false);
    this.editor.addKeyListener(new KeyAdapter() {

      @Override
      public void keyTyped(KeyEvent keyEvent) {
        if (keyEvent.getKeyChar() == '\b' || keyEvent.getKeyChar() == '\u007f' || keyEvent.getKeyChar() == '\n') {
          Editor.this.ZeilenNummernSetzen(keyEvent.getKeyChar() == '\n'
              && Editor.this.editor.getCaretPosition() >= Editor.this.editor.getText().length() - 1);
        }
      }
    });
    this.scroll = new JScrollPane(this.editor, 20, 30);
    this.scroll.setRowHeaderView(this.zeilenNummern);
    jPanel.add((Component) this.scroll, "Center");
    this.status = new JLabel();
    this.status.setBorder(LineBorder.createGrayLineBorder());
    this.status.setBackground(Color.yellow);
    jPanel.add((Component) this.status, "South");
    this.fenster.setSize(400, 200);
    this.fenster.addWindowListener(new WindowAdapter() {

      @Override
      public void windowClosing(WindowEvent windowEvent) {
        Editor.this.SchließenAusführen(false);
      }
    });
    this.fenster.setDefaultCloseOperation(2);
    this.fileChooser = new JFileChooser();
    this.fileChooser.addChoosableFileFilter(new FileFilter() {

      @Override
      public boolean accept(File file) {
        String string = file.getName();
        return string.toLowerCase().endsWith(".mis") || file.isDirectory();
      }

      @Override
      public String getDescription() {
        return "Minimaschine Minisprache";
      }
    });
    this.fileChooser.addChoosableFileFilter(new FileFilter() {

      @Override
      public boolean accept(File file) {
        String string = file.getName();
        return string.toLowerCase().endsWith(".mia") || file.isDirectory();
      }

      @Override
      public String getDescription() {
        return "Minimaschine Assembler";
      }
    });
  }

  private void SichernAusführen(boolean bl) {
    if (this.file == null || bl) {
      if (this.file != null) {
        this.fileChooser.setSelectedFile(this.file);
      } else {
        this.fileChooser.setCurrentDirectory(letzterOrdner);
      }
      int n = this.fileChooser.showSaveDialog(this.fenster);
      if (n == 0) {
        this.file = this.fileChooser.getSelectedFile();
        if (this.fileChooser.getFileFilter().getDescription().equals("Minimaschine Assembler")) {
          if (!this.file.getName().toLowerCase().endsWith(".mia")) {
            this.file = new File(this.file.getPath() + ".mia");
          }
          letzterOrdner = this.file;
        } else if (this.fileChooser.getFileFilter().getDescription().equals("Minimaschine Minisprache")) {
          if (!this.file.getName().toLowerCase().endsWith(".mis")) {
            this.file = new File(this.file.getPath() + ".mis");
          }
          letzterOrdner = this.file;
        }
      } else {
        return;
      }
    }
    try {
      FileWriter fileWriter = new FileWriter(this.file);
      this.editor.write(fileWriter);
      fileWriter.close();
      this.sicherungsstand = this.editor.getText();
      this.fenster.setTitle(this.file.getPath());
      this.kontrolleur.FensterTitel\u00c4ndernWeitergeben(this.selbst);
    } catch (Exception exception) {
      this.file = null;
    }
  }

  private void SchließenAusführen(boolean bl) {
    if (!this.sicherungsstand.equals(this.editor.getText())) {
      int n = JOptionPane.showConfirmDialog(this.fenster,
          new String[] { "Dieses Fenster enthält ungesicherte \u00c4nderungen.", "Sollen sie gesichert werden?" },
          "\u00c4nderungen sichern", bl ? 1 : 0);
      if (n == 0) {
        this.SichernAusführen(false);
      } else if (n != 1) {
        return;
      }
    }
    this.kontrolleur.SchließenAusführen(this.selbst);
    this.fenster.dispose();
  }

  @Override
  void BeendenMitteilen() {
    if (!this.sicherungsstand.equals(this.editor.getText())) {
      int n = JOptionPane.showConfirmDialog(this.fenster,
          new String[] { "Dieses Fenster enthält ungesicherte \u00c4nderungen.", "Sollen sie gesichert werden?" },
          "\u00c4nderungen sichern", 0);
      if (n == 0) {
        this.SichernAusführen(false);
      }
    }
  }

  @Override
  protected void MenüsErzeugen() {
    super.MenüsErzeugen();
    this.schließenItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        Editor.this.SchließenAusführen(true);
      }
    });
    this.sichernItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        Editor.this.SichernAusführen(false);
      }
    });
    this.sichernUnterItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        Editor.this.SichernAusführen(true);
      }
    });
    this.druckenItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        Editor.this.DruckenAusführen();
      }
    });
    this.undoItem = new JMenuItem("Widerrufen", 90);
    this.undoItem.setAccelerator(KeyStroke.getKeyStroke(90, kommando));
    this.undoItem.setEnabled(false);
    this.undoItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        Editor.this.undo.undo();
        Editor.this.undoItem.setEnabled(Editor.this.undo.canUndo());
        Editor.this.redoItem.setEnabled(Editor.this.undo.canRedo());
        Editor.this.ZeilenNummernSetzen(false);
      }
    });
    this.bearbeitenMenü.add(this.undoItem);
    this.redoItem = new JMenuItem("Wiederholen");
    this.redoItem.setAccelerator(KeyStroke.getKeyStroke(90, 64 + kommando));
    this.redoItem.setEnabled(false);
    this.redoItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        Editor.this.undo.redo();
        Editor.this.undoItem.setEnabled(Editor.this.undo.canUndo());
        Editor.this.redoItem.setEnabled(Editor.this.undo.canRedo());
        Editor.this.ZeilenNummernSetzen(false);
      }
    });
    this.bearbeitenMenü.add(this.redoItem);
    this.bearbeitenMenü.addSeparator();
    JMenuItem jMenuItem = new JMenuItem("Ausschneiden", 88);
    jMenuItem.setAccelerator(KeyStroke.getKeyStroke(88, kommando));
    jMenuItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        Editor.this.editor.cut();
      }
    });
    this.bearbeitenMenü.add(jMenuItem);
    jMenuItem = new JMenuItem("Kopieren", 67);
    jMenuItem.setAccelerator(KeyStroke.getKeyStroke(67, kommando));
    jMenuItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        Editor.this.editor.copy();
      }
    });
    this.bearbeitenMenü.add(jMenuItem);
    jMenuItem = new JMenuItem("Einfügen", 86);
    jMenuItem.setAccelerator(KeyStroke.getKeyStroke(86, kommando));
    jMenuItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        Editor.this.editor.paste();
      }
    });
    this.bearbeitenMenü.add(jMenuItem);
    jMenuItem = new JMenuItem("Alles auswählen", 65);
    jMenuItem.setAccelerator(KeyStroke.getKeyStroke(65, kommando));
    jMenuItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        Editor.this.editor.selectAll();
      }
    });
    this.bearbeitenMenü.add(jMenuItem);
    String[] arrstring = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    JMenu jMenu = new JMenu("Fonts");
    ActionListener actionListener = new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        String string = ((JMenuItem) actionEvent.getSource()).getText();
        Font font = Editor.this.editor.getFont();
        Font font2 = new Font(string, font.getStyle(), font.getSize());
        Editor.this.editor.setFont(font2);
        Editor.this.zeilenNummern.setFont(font2);
      }
    };
    for (String string : arrstring) {
      JMenuItem jMenuItem2 = new JMenuItem(string);
      jMenuItem2.addActionListener(actionListener);
      jMenu.add(jMenuItem2);
    }
    this.werkzeugMenü.addSeparator();
    this.werkzeugMenü.add(jMenu);
    this.werkzeugMenü.addSeparator();
    jMenuItem = new JMenuItem("Assemblieren");
    jMenuItem.setAccelerator(KeyStroke.getKeyStroke(65, kommando + 512));
    jMenuItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        Editor.this.status.setText("");
        Editor.this.kontrolleur.Assemblieren(Editor.this.editor.getText(), (Editor) Editor.this.selbst);
      }
    });
    this.werkzeugMenü.add(jMenuItem);
    this.werkzeugMenü.addSeparator();
    jMenuItem = new JMenuItem("Übersetzen");
    jMenuItem.setAccelerator(KeyStroke.getKeyStroke(85, kommando + 512));
    jMenuItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        Editor.this.status.setText("");
        Editor.this.kontrolleur.Übersetzen(Editor.this.editor.getText(), (Editor) Editor.this.selbst);
      }
    });
    this.werkzeugMenü.add(jMenuItem);
    jMenuItem = new JMenuItem("Assemblertext zeigen");
    jMenuItem.setAccelerator(KeyStroke.getKeyStroke(90, kommando + 512));
    jMenuItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        Editor.this.status.setText("");
        Editor.this.kontrolleur.AssemblertextZeigen(Editor.this.editor.getText(), (Editor) Editor.this.selbst);
      }
    });
    this.werkzeugMenü.add(jMenuItem);
  }

  @Override
  protected void DarstellungsgrößeSetzen(boolean bl) {
    if (bl) {
      this.FontgrößeSetzen(24);
    } else {
      this.FontgrößeSetzen(13);
    }
    this.editor.invalidate();
    this.editor.repaint();
  }

  private void FontgrößeSetzen(int n) {
    Font font = this.editor.getFont();
    Font font2 = new Font(font.getName(), font.getStyle(), n);
    this.editor.setFont(font2);
    this.zeilenNummern.setFont(font2);
  }

  void DateiLesen() {
    this.fileChooser.setCurrentDirectory(letzterOrdner);
    int n = this.fileChooser.showOpenDialog(this.fenster);
    if (n == 0) {
      this.file = this.fileChooser.getSelectedFile();
      try {
        FileReader fileReader = new FileReader(this.file);
        this.editor.read(fileReader, null);
        fileReader.close();
        String[] arrstring = this.editor.getText().split("\n");
        String string = "";
        for (int i = 1; i <= arrstring.length; ++i) {
          string = string + i + " \n";
        }
        this.zeilenNummern.setText(string);
        this.sicherungsstand = this.editor.getText();
        this.fenster.setTitle(this.file.getPath());
        letzterOrdner = this.file;
      } catch (Exception exception) {
        this.file = null;
      }
    } else {
      this.file = null;
    }
    if (this.file != null) {
      this.fenster.setVisible(true);
      this.undoItem.setEnabled(false);
      this.redoItem.setEnabled(false);
      this.kontrolleur.FensterTitel\u00c4ndernWeitergeben(this.selbst);
    } else {
      this.kontrolleur.SchließenAusführen(this.selbst);
      this.fenster.dispose();
    }
  }

  void DateiLesen(String string) {
    this.file = new File(string);
    try {
      FileReader fileReader = new FileReader(this.file);
      this.editor.read(fileReader, null);
      fileReader.close();
      this.sicherungsstand = this.editor.getText();
      this.fenster.setTitle(this.file.getPath());
    } catch (Exception exception) {
      this.file = null;
    }
    this.fenster.setVisible(true);
    this.undoItem.setEnabled(false);
    this.redoItem.setEnabled(false);
    this.kontrolleur.FensterTitel\u00c4ndernWeitergeben(this.selbst);
  }

  void FehlerAnzeigen(String string, int n) {
    this.status.setText(string);
    this.editor.select(n - 2, n - 1);
  }

  private void DruckenAusführen() {
    int n;
    String[] arrstring = this.editor.getText().split("\n");
    for (n = 0; n < arrstring.length; ++n) {
      int n2;
      while ((n2 = arrstring[n].indexOf(9)) >= 0) {
        arrstring[n] = arrstring[n].substring(0, n2) + "        ".substring(0, 8 - n2 % 8)
            + arrstring[n].substring(n2 + 1);
      }
    }
    PrintJob printJob = this.fenster.getToolkit().getPrintJob(this.fenster, this.fenster.getTitle(), null);
    Dimension dimension = printJob.getPageDimension();
    int n3 = printJob.getPageResolution();
    int n4 = 15000 * n3 / 25410;
    int n5 = 10000 * n3 / 25410;
    dimension.width -= n4 * 2;
    dimension.height -= n4 * 2;
    Font font = new Font("Monospaced", 0, 10);
    Font font2 = new Font("Monospaced", 0, 14);
    Graphics graphics = printJob.getGraphics();
    int n6 = graphics.getFontMetrics(font).getHeight();
    int n7 = (dimension.height - n5 * 2) / n6;
    int n8 = (arrstring.length + n7 - 1) / n7;
    this.RahmenDrucken(graphics, dimension, n4, n5, 1, n8, font, font2);
    for (n = 0; n < arrstring.length; ++n) {
      graphics.drawString(arrstring[n], n4 + n5 * 5 / 10, n4 + 2 * n5 + n % n7 * n6);
      if ((n + 1) % n7 != 0)
        continue;
      graphics.dispose();
      graphics = printJob.getGraphics();
      this.RahmenDrucken(graphics, dimension, n4, n5, (n + 1) / n7, n8, font, font2);
    }
    graphics.dispose();
    printJob.end();
  }

  private void RahmenDrucken(Graphics graphics, Dimension dimension, int n, int n2, int n3, int n4, Font font,
      Font font2) {
    graphics.drawRoundRect(n, n, dimension.width, dimension.height, n2 * 2, n2 * 2);
    graphics.drawLine(n, n + n2, n + dimension.width, n + n2);
    graphics.drawLine(n, n + dimension.height - n2, n + dimension.width, n + dimension.height - n2);
    String string = this.fenster.getTitle();
    graphics.setFont(font2);
    graphics.drawString(string, n + dimension.width / 2 - graphics.getFontMetrics().stringWidth(string) / 2,
        n + n2 * 7 / 10);
    string = "\u2013 " + n3 + " von " + n4 + " \u2013";
    graphics.setFont(font);
    graphics.drawString(string, n + dimension.width / 2 - graphics.getFontMetrics().stringWidth(string) / 2,
        n + dimension.height - n2 * 4 / 10);
  }
}
