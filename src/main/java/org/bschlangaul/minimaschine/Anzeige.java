package org.bschlangaul.minimaschine;

import com.apple.eawt.AboutHandler;
import com.apple.eawt.AppEvent;
import com.apple.eawt.Application;
import com.apple.eawt.OpenFilesHandler;
import com.apple.eawt.QuitHandler;
import com.apple.eawt.QuitResponse;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

abstract class Anzeige {
  protected JFrame fenster;
  protected JMenuBar menüZeile;
  protected JMenu dateiMenü;
  protected JMenu bearbeitenMenü;
  protected JMenu werkzeugMenü;
  protected JMenu fensterMenü;
  protected JMenuItem schließenItem;
  protected JMenuItem sichernItem;
  protected JMenuItem sichernUnterItem;
  protected JMenuItem druckenItem;
  protected JCheckBoxMenuItem größeItem;
  protected KontrolleurInterface kontrolleur;
  protected Anzeige selbst;
  private static boolean erster = true;
  protected static boolean isMac;
  protected static int kommando;

  private void MacOSVorbereiten() {
    Application application = Application.getApplication();
    application.setAboutHandler(new AboutHandler() {

      @Override
      public void handleAbout(AppEvent.AboutEvent aboutEvent) {
        Über.Zeigen();
      }
    });
    application.setQuitHandler(new QuitHandler() {

      @Override
      public void handleQuitRequestWith(AppEvent.QuitEvent quitEvent, QuitResponse quitResponse) {
        Anzeige.this.kontrolleur.BeendenAusführen();
        quitResponse.cancelQuit();
      }
    });
    application.setOpenFileHandler(new OpenFilesHandler() {

      @Override
      public void openFiles(AppEvent.OpenFilesEvent openFilesEvent) {
        // https://stackoverflow.com/a/2848268/10193818
        List<File> list = castList(File.class, openFilesEvent.getFiles());
        for (File file : list) {
          Anzeige.this.kontrolleur.ÖffnenAusführen(file.getPath());
        }
      }
    });
  }

  public static <T> List<T> castList(Class<? extends T> clazz, Collection<?> c) {
    // https://stackoverflow.com/a/2848268/10193818
    List<T> r = new ArrayList<T>(c.size());
    for (Object o : c)
      r.add(clazz.cast(o));
    return r;
  }

  Anzeige(KontrolleurInterface kontrolleurInterface) {
    this.kontrolleur = kontrolleurInterface;
    this.selbst = this;
    if (erster) {
      erster = false;
      isMac = System.getProperty("os.name", "").startsWith("Mac");
      if (isMac) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        this.MacOSVorbereiten();
        kommando = 256;
      } else {
        kommando = 128;
      }
      try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (Exception exception) {
        // empty catch block
      }
    }
    this.MenüsErzeugen();
    this.OberflächeAufbauen();
    if (isMac) {
      // empty if block
    }
  }

  protected abstract void OberflächeAufbauen();

  protected void MenüsErzeugen() {
    this.menüZeile = new JMenuBar();
    this.dateiMenü = new JMenu("Ablage");
    this.menüZeile.add(this.dateiMenü);
    JMenuItem jMenuItem = new JMenuItem("Neu", 78);
    jMenuItem.setAccelerator(KeyStroke.getKeyStroke(78, kommando));
    jMenuItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        Anzeige.this.kontrolleur.NeuAusführen();
      }
    });
    this.dateiMenü.add(jMenuItem);

    jMenuItem = new JMenuItem("Öffnen \u2026", 79);
    jMenuItem.setAccelerator(KeyStroke.getKeyStroke(79, kommando));
    jMenuItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        Anzeige.this.kontrolleur.ÖffnenAusführen();
      }
    });
    this.dateiMenü.add(jMenuItem);
    this.dateiMenü.addSeparator();

    this.schließenItem = new JMenuItem("Schließen", 87);
    this.schließenItem.setAccelerator(KeyStroke.getKeyStroke(87, kommando));
    this.dateiMenü.add(this.schließenItem);

    this.sichernItem = new JMenuItem("Sichern", 83);
    this.sichernItem.setAccelerator(KeyStroke.getKeyStroke(83, kommando));
    this.dateiMenü.add(this.sichernItem);

    this.sichernUnterItem = new JMenuItem("Sichern unter \u2026");
    this.sichernUnterItem.setAccelerator(KeyStroke.getKeyStroke(83, kommando + 64));
    this.dateiMenü.add(this.sichernUnterItem);
    this.dateiMenü.addSeparator();

    this.druckenItem = new JMenuItem("Drucken \u2026");
    this.druckenItem.setAccelerator(KeyStroke.getKeyStroke(80, kommando));
    this.dateiMenü.add(this.druckenItem);
    if (!isMac) {
      this.dateiMenü.addSeparator();
      jMenuItem = new JMenuItem("Beenden", 81);
      jMenuItem.setAccelerator(KeyStroke.getKeyStroke(81, kommando));
      jMenuItem.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
          Anzeige.this.kontrolleur.BeendenAusführen();
        }
      });
      this.dateiMenü.add(jMenuItem);
    }
    this.bearbeitenMenü = new JMenu("Bearbeiten");
    this.menüZeile.add(this.bearbeitenMenü);
    this.werkzeugMenü = new JMenu("Werkzeuge");
    this.menüZeile.add(this.werkzeugMenü);

    this.größeItem = new JCheckBoxMenuItem("Große Darstellung");
    this.größeItem.setEnabled(true);
    this.größeItem.setSelected(false);
    this.größeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, kommando + ActionEvent.ALT_MASK));
    this.größeItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        Anzeige.this.DarstellungsgrößeSetzen(Anzeige.this.größeItem.isSelected());
      }
    });
    this.werkzeugMenü.add(this.größeItem);
    this.fensterMenü = new JMenu("Fenster");
    this.menüZeile.add(this.fensterMenü);
    if (!isMac) {
      jMenuItem = new JMenuItem("Über Minimaschine");
      jMenuItem.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
          Über.Zeigen();
        }
      });
      this.fensterMenü.add(jMenuItem);
      this.fensterMenü.addSeparator();
    }
    jMenuItem = new JMenuItem("CPU-Fenster");
    jMenuItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        Anzeige.this.kontrolleur.CpuFensterAuswählen();
      }
    });
    this.fensterMenü.add(jMenuItem);
    jMenuItem = new JMenuItem("Speicher-Fenster");
    jMenuItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        Anzeige.this.kontrolleur.SpeicherFensterAuswählen();
      }
    });
    this.fensterMenü.add(jMenuItem);
    this.fensterMenü.addSeparator();
  }

  protected abstract void DarstellungsgrößeSetzen(boolean var1);

  private String TitelGeben() {
    return this.fenster.getTitle();
  }

  void FenstereintragHinzufügen(int n, Anzeige anzeige) {
    JMenuItem jMenuItem = new JMenuItem(anzeige.TitelGeben());
    jMenuItem.addActionListener(new FensterAktion(anzeige));
    this.fensterMenü.insert(jMenuItem, n + 3);
  }

  void FenstereintragEntfernen(int n) {
    this.fensterMenü.remove(n + 3);
  }

  void FenstereintragÄndern(int n, Anzeige anzeige) {
    this.fensterMenü.getItem(n + 3).setText(anzeige.TitelGeben());
  }

  void Aktivieren() {
    if (!this.fenster.isVisible()) {
      this.fenster.setVisible(true);
    }
    this.fenster.toFront();
  }

  void Ausblenden() {
    this.fenster.setVisible(false);
  }

  void BeendenMitteilen() {
  }

  static boolean IstMacOS() {
    return isMac;
  }

  class FensterAktion implements ActionListener {
    private Anzeige anzeige;

    FensterAktion(Anzeige anzeige2) {
      this.anzeige = anzeige2;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
      this.anzeige.Aktivieren();
    }
  }
}
