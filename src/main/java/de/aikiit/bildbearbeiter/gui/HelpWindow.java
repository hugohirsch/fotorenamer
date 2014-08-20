/*
 * Created on 20.10.2003
 */
package de.aikiit.bildbearbeiter.gui;

import de.aikiit.bildbearbeiter.util.ComponentGaugeUtil;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * This class represents a help window component. It loads a HTML-page to show
 * as application help.
 *
 * @author hirsch, 20.10.2003
 * @version 2004-01-08
 */
public class HelpWindow extends JFrame implements ActionListener {
    /** Logger for this class. */
    private static final Logger LOG = LogManager.getLogger(HelpWindow.class);
    /**
    End button needs to be visible inside the class to perform programme
    exit.
    */
    private JButton endButton = null;

    /**
     * Relative location of the UI's image icon.
     */
    private static String HTML_HELP_LOCATION = File.separator + "html"
            + File.separator + "hilfe.html";

    /**
     * Creates a HelpWindow, initializes its components but does
     * <strong>not</strong> show the window.
     */
    public HelpWindow() {
        init();
    } // end of Konstruktor

    /**
     * Initialize UI-compontens and make them visible.
     */
    private void init() {
        // REVIEW add i18n
        JPanel oben = null;
        JPanel unten = null;
        JScrollPane rollpanel = null;
        JEditorPane textfeld = null;

        this.setTitle("Programmhilfe");
        this.setResizable(false);
        oben = new JPanel(new FlowLayout());
        unten = new JPanel(new FlowLayout());

        // Inhalt des Textfeldes von einer URL laden
        textfeld = new JEditorPane();
        textfeld.setContentType("text/html");
        textfeld.setEditable(false);
        try {
            // REVIEW extract this to a constant to make refactorings easier
            textfeld.setPage(HelpWindow.class.getResource(HTML_HELP_LOCATION));
            oben.add(textfeld);
        } catch (Exception e) {
            oben.setLayout(new GridLayout(3, 1));
            oben.add(new JLabel("Fehler in der Hilfe -"));
            oben.add(new JLabel("" + e.getMessage()));
            oben.add(new JLabel("" + e.getClass()));
        } // end of catch

        // Eigenschaften des Scrollpanels anpassen
        rollpanel = new JScrollPane(oben);
        rollpanel.setPreferredSize(new Dimension(350, 300));
        rollpanel.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Ende-Knopf
        this.endButton = new JButton("Schließen");
        this.endButton.addActionListener(this);
        this.endButton.setMnemonic('S');
        unten.add(this.endButton);

        // Zusammenbasteln ...
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(rollpanel, BorderLayout.NORTH);
        this.getContentPane().add(unten, BorderLayout.CENTER);

        // Zentrieren und Anzeigen
        this.pack();
        ComponentGaugeUtil.makeCentered(this);
        LOG.debug("HelpWindow init done.");
    } // end of init

    /**
     * Make this component react to close button.
     *
     * @param event Event to react on in this UI-component.
     */
    public final void actionPerformed(final ActionEvent event) {
        if (event.getSource() == this.endButton) {
            LOG.debug("Disabling visibility of helpWindow.");
            setVisible(false);
        }
    } // end of actionPerformed
} // end of class
