//+======================================================================
// $Source: /segfs/tango/cvsroot/jclient/jblvac/src/jblvac/tools/PopupHtml.java,v $
//
// Project:   Tango
//
// Description:	java source code for display JTree
//
// $Author: verdier $
//
// Copyright (C) :      2004,2005,2006,2007,2008,2009,2009
//						European Synchrotron Radiation Facility
//                      BP 220, Grenoble 38043
//                      FRANCE
//
// This file is part of Tango.
//
// Tango is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
// 
// Tango is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with Tango.  If not, see <http://www.gnu.org/licenses/>.
//
// $Revision: 1.1.1.1 $
//
// $Log: PopupHtml.java,v $
// Revision 1.1.1.1  2014-04-02 12:52:20  verdier
// Initial Revision
//
//
//-======================================================================


package org.tango.hdb_configurator.common;


import fr.esrf.TangoDs.TangoConst;
import fr.esrf.tangoatk.widget.util.ErrorPane;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * Class Description:
 * This class display a dialog with a html message
 *
 * @author verdier
 */

public class PopupHtml extends JDialog implements TangoConst {

    protected Component parent;
    protected JEditorPane pane;
    private String urlFile;

    private static final String header =
            "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2//EN\">\n" +
                    "<HTML>\n" +
                    "<HEAD>\n" +
                    "<Title> title </Title>\n" +
                    "</HEAD>\n" +
                    "<BODY TEXT=\"#000000\" BGCOLOR=\"#FFFFFF\" LINK=\"#0000FF\" VLINK=\"#7F00FF\" ALINK=\"#FF0000\">\n" +
                    "<P><!-------TITLE------></P>\n";
    private static final String footer =
            "</Body>\n" +
                    "</Html>\n";

    //===============================================================
    /**
     * Initializes the Form
     *
     * @param parent the parent form instance
     */
    //===============================================================
    public PopupHtml(JFrame parent) {
        this(parent, true);
    }
    //===============================================================
    /**
     * Initializes the Form
     *
     * @param parent the parent form instance
     */
    //===============================================================
    public PopupHtml(JFrame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        this.parent = parent;
        pack();
    }
    //===============================================================
    /**
     * Initializes the Form
     *
     * @param parent the parent form instance
     */
    //===============================================================
    public PopupHtml(JDialog parent, boolean modal) {
        super(parent, modal);
        initComponents();

        this.parent = parent;
        pack();
    }
    //===============================================================
    //===============================================================
    private void setScreenPosition () {
        if (parent!=null && parent.isVisible() && parent.getWidth()>0) {
            Point p = parent.getLocationOnScreen();

            p.x += 50;
            p.y += parent.getHeight()/2-getHeight()/2;
            if (p.y<10) p.y = 10;
            setLocation(p);
        }
    }
    //===============================================================
    //===============================================================
    private void initComponents() {
        javax.swing.JPanel jPanel1;
        javax.swing.JLabel jLabel1;
        javax.swing.JButton cancelBtn;
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cancelBtn = new javax.swing.JButton();
        setBackground(new java.awt.Color(198, 178, 168));
        setTitle("");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel1.setLayout(new java.awt.FlowLayout(FlowLayout.RIGHT, 5, 5));

        jLabel1.setText("                     ");
        jPanel1.add(jLabel1);

        cancelBtn.setText("Dismiss");
        cancelBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cancelBtn.addActionListener(evt->dismissBtnActionPerformed());

        jPanel1.add(cancelBtn);
        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

    }

    //======================================================
    //======================================================
    protected synchronized void setPage(URL url) {
        try {
            //	And Try to display page
            pane.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            pane.setPage(url);
        } catch (IOException e) {
            ErrorPane.showErrorMessage(parent, null, e);
        }
        pane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    //======================================================
    //======================================================
    private String buildTmpFile(String code) {
        String urlstr = null;
        try {
            int random_value = new java.util.Random().nextInt(30000);
            String tmpdir = System.getProperty("java.io.tmpdir");
            urlFile = tmpdir + "/html." + random_value;
            FileOutputStream fidout = new FileOutputStream(urlFile);
            fidout.write((header + code + footer).getBytes());
            fidout.close();

            urlstr = "file:" + urlFile;
        } catch (Exception e) {
            ErrorPane.showErrorMessage(parent, null, e);
            e.printStackTrace();
        }
        return urlstr;
    }
    //======================================================
    @SuppressWarnings({"UnusedDeclaration"})
    private void closeDialog(java.awt.event.WindowEvent evt) {
        doClose();
    }
    //======================================================
    private void dismissBtnActionPerformed() {
        doClose();
    }
    //======================================================
    private void doClose() {
        try {
            if (urlFile!=null)
                if (!new File(urlFile).delete())
                    System.err.println("Cannot delete " + urlFile);
        } catch (Exception e) {
            ErrorPane.showErrorMessage(parent, null, e);
        }
        setVisible(false);
        dispose();
    }
    //======================================================
    //======================================================
    private String htmlTitle(String title) {
        return "<b><u>" + title + "</u></b>\n<ul>";
    }
    //======================================================
    /**
     * Show the dialog window.
     *
     * @param code an URL address or HTML page source code.
     */
    //======================================================
    public void show(String code) {
        show(code, null, 700, 750);
    }
    //======================================================
    /**
     * Show the dialog window.
     *
     * @param code an URL address or HTML page source code.
     */
    //======================================================
    public void show(String code, String title) {
        show(code, title, 700, 750);
    }
    //======================================================
    /**
     * Show the dialog window.
     *
     * @param code   an URL address or HTML page source code.
     * @param width  dialog size
     * @param height dialog size
     */
    //======================================================
    public void show(String code, String title, int width, int height) {
        try {
            if (title!=null)
                code =  htmlTitle(title)+ code ;

            pane = new JEditorPane();
            pane.setEditable(false);
            //	add an hyperlink listener
            pane.addHyperlinkListener(evt -> { });
            getContentPane().add(new JScrollPane(pane), java.awt.BorderLayout.CENTER);

            URL url = new URL(buildTmpFile(code));
            setPage(url);
            setSize(width, height);
        } catch (IOException e) {
            e.printStackTrace(System.err);
            return;
        }
        setScreenPosition();
        setVisible(true);
    }
    //======================================================
    /**
     * Show the dialog window.
     *
     * @param url    specified URL to display.
     * @param width  dialog size
     * @param height dialog size
     */
    //======================================================
    public void show(URL url, int width, int height) {
        pane = new JEditorPane();
        pane.setEditable(false);
        //	add an hyperlink listener
        pane.addHyperlinkListener(evt -> { });
        getContentPane().add(new JScrollPane(pane), java.awt.BorderLayout.CENTER);
        setSize(width, height);
        setPage(url);
        setScreenPosition();
        setVisible(true);
    }

    //===============================================================
    //===============================================================
    public static final String Space = "&nbsp;";
    public static String toHtml(String code) {
        int idx = 0;
        while ((idx=code.indexOf('\n', idx))>0) {
            code = code.substring(0, idx) + "<br>" + code.substring(idx);
            idx += 5; //    "<br>\n"
        }
        idx = 0;
        while ((idx=code.indexOf('(', idx))>0) {
            code = code.substring(0, idx) + Space+Space + code.substring(idx);
            idx += (Space+Space).length()+1; //    "  ("
        }
        return code;
    }
    //===============================================================
    //===============================================================
}
