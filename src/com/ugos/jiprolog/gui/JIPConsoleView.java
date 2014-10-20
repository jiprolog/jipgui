/*
 * 09/19/2002
 *
 * Copyright (C) 1999-2005 Ugo Chirico
 *
 * This is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */


package com.ugos.jiprolog.gui;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import com.ugos.JIProlog.engine.*;
import com.ugos.awt.*;
import com.ugos.io.*;
import com.ugos.jiprolog.igui.*;

public abstract class JIPConsoleView extends Panel implements IJIPConsoleView, ActionListener
{
    // Panel's Field
    protected TerminalTextArea m_mainArea;

    protected IJIPConsoleController m_consoleCtrl;

    // Stream
    protected PrintStream m_outs = null;
    protected InputStream m_ins = null;
    protected Frame       m_mainFrame;

    // font
    public static int    s_nDefaultFontSize;
    public static String s_strDefaultFontName;
    public static Color  s_defaultBckgColor;
    public static Color  s_defaultFgColor;

    // windows dimension
    public static Dimension s_winDim;

    public static String PROPERTIES_FILE;

    public static final Color BKGCOLOR = Color.darkGray.brighter();

    // load properties file
    static
    {
        //System.out.println(s_strHomePath);
        Properties props = new Properties();
        try
        {
            PROPERTIES_FILE = System.getProperty("user.home") + "\\JIProlog.properties";

            props.load(new FileInputStream(PROPERTIES_FILE));

            // set font
            s_nDefaultFontSize = Integer.parseInt((String)props.get("Font.Size"));
            s_strDefaultFontName = (String)props.get("Font.Face");
            // set Color
            s_defaultBckgColor = ((String)props.get("Color")).equals("white") ? Color.white : Color.black;
            // set dimension
            s_winDim = new Dimension(Integer.parseInt((String)props.get("Windows.Width")), Integer.parseInt((String)props.get("Windows.Height")));

        }
        catch(Throwable ex)
        {
            // windows dimensions
            Dimension ds = Toolkit.getDefaultToolkit().getScreenSize();
            ds.width  *= (9.0/10.0);
            ds.height *= (9.0/10.0);

            s_nDefaultFontSize = 12;
            s_strDefaultFontName = "Courier";
            s_defaultBckgColor   = Color.white;

            props.put("Font.Size", Integer.toString(s_nDefaultFontSize));
            props.put("Color", "white");

            // windows
            s_winDim = ds;
            props.put("Windows.Width", Integer.toString(ds.width));
            props.put("Windows.Height", Integer.toString(ds.height));
        }
    }

    // Constructor
    public JIPConsoleView(final Frame mainFrame)
    {
        m_mainFrame = mainFrame;
        init();
    }

    public void setController(final IJIPConsoleController controller)
    {
        m_consoleCtrl = controller;
    }

//  public void setVisible(boolean bVisible)
//  {
//      super.setVisible(bVisible);
//  }

    // Initializzation
    public void init()
    {
        m_mainArea    = new TerminalTextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);//SCROLLBARS_NONE);
        // Set OutputStream

        m_outs = new PrintStream(new TextAreaOutputStream(m_mainArea), true);

        m_ins = new ComponentInputStream(m_mainArea);

        setBackground(BKGCOLOR);
        setLayout(new BorderLayout());

        initNorth();
        initCenter();
        initSouth();

        m_mainArea.setPrompt(JIPConsoleController.PROMPT);
    }

    public void initNorth()
    {
        // Top Panel
        Panel topPanel = new Panel();
        topPanel.setLayout(new GridLayout(2,1));
        topPanel.setBackground(BKGCOLOR);

        Label lb = new Label("JIProlog - Java Internet Prolog v" + JIPEngine.getVersion(), Label.CENTER);
        lb.setFont(new Font("TimesRoman", Font.BOLD, s_nDefaultFontSize + 1));
        lb.setForeground(Color.white);
        topPanel.add(lb);

        lb = new Label("by Ugo Chirico - http://www.jiprolog.com", Label.CENTER);
        lb.setFont(new Font("TimesRoman", Font.BOLD, s_nDefaultFontSize));
        lb.setForeground(Color.white);
        topPanel.add(lb);

        add("North", topPanel);
    }

    public void initCenter()
    {
        // Main Panel
        Panel mainPanel = new Panel();
        mainPanel.setLayout(new GridLayout(1,1));

        m_mainArea.setFont(new Font(s_strDefaultFontName, Font.PLAIN, s_nDefaultFontSize));
        m_mainArea.addActionListener(this);
        m_mainArea.setForeground(s_defaultBckgColor == Color.white ? Color.black : Color.white);
        m_mainArea.setBackground(s_defaultBckgColor);

        mainPanel.add(m_mainArea);
        add("Center", mainPanel);
    }

    public void initSouth()
    {
        // Bottom panel
        Label copyr   = new Label("Copyright (c) 1999-2012 by Ugo Chirico. All rights reserved", Label.RIGHT);

        copyr.setFont(new Font("Arial", Font.BOLD, s_nDefaultFontSize - 2));
        copyr.setForeground(Color.white);

        Panel panButtons = new Panel();

        panButtons.add(copyr);

        panButtons.setBackground(BKGCOLOR);
        add("South", panButtons);
    }

    public final void editable(boolean bEditable)
    {
        m_mainArea.setEditable(bEditable);
    }

    public final void prompt()
    {
        m_mainArea.prompt();
    }

    public final void updatePrompt()
    {
        m_mainArea.updatePrompt();
    }

//    public final void append(final String string)
//    {
//        m_mainArea.append("\n");
//    }

    public final void recordHistory(boolean bRecord)
    {
        m_mainArea.recordHistory(bRecord);
    }

    public final PrintStream getPrintStream(String encoding) throws UnsupportedEncodingException
    {
        m_outs.close();
        m_outs = new PrintStream(new TextAreaOutputStream(m_mainArea), true, encoding);

        return m_outs;
    }

    public final InputStream getInputStream()
    {
        return m_ins;
    }

    public Frame getMainFrame()
    {
        return m_mainFrame;
    }

    public void actionPerformed(ActionEvent evt)
    {
        if(evt.getSource() == m_mainArea)
        {
            if(!m_mainArea.isEditable())
                return;

            String strQuery = evt.getActionCommand();

            m_consoleCtrl.onQuery(strQuery);
        }
        else if(evt.getSource() instanceof MenuItem)
        {
            String command = evt.getActionCommand();

            if(command.equals("New"))
            {
                m_consoleCtrl.onNewFile();
            }
            else if(command.equals("Open"))
            {
                m_consoleCtrl.onOpenFile();
            }
            else if(command.equals("Exit"))
            {
                m_consoleCtrl.onExit();
                m_consoleCtrl.onDestroy();
            }
            else if(command.equals("Reset"))
            {
                m_consoleCtrl.onReset();
            }
            else if(command.equals("Stop"))
            {
                m_consoleCtrl.onStop();
            }
            else if(command.equals("ViewReg"))
            {
                Dialogs.showTextMessageDlg(m_mainFrame, "License Info", JIPEngine.getLicenseInfo());
            }
            else if(command.equals("About"))
            {
                Dialogs.showTextMessageDlg(m_mainFrame, "About",
                                           "\nJIProlog Console v" + JIPConsoleController.VERSION +
                                           "\nBased on " + JIPEngine.getInfo());
            }
        }
    }

    public void requestFocus()
    {
        m_mainArea.requestFocus();
    }

    public void startUserInput()
    {
        m_mainArea.setEnableAction(false);
        m_mainArea.recordHistory(false);
        m_mainArea.append(":>");
        waitCursor(false);
        m_mainArea.removeActionListener(this);
        m_mainArea.setEditable(true);
        m_mainArea.requestFocus();
        m_mainArea.setCaretPosition(m_mainArea.getText().length());
    }

    public void stopUserInput()
    {
        //m_mainArea.append("\n");
        waitCursor(true);
        m_mainArea.recordHistory(true);
        m_mainArea.addActionListener(this);
        m_mainArea.setEditable(false);
        m_mainArea.setEnableAction(true);
    }

    public void waitCursor(boolean bWait)
    {
        if(bWait)
            m_mainArea.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        else
            m_mainArea.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    public void printHeader()
    {
        m_outs.println("***************************************");
        m_outs.println("* JIProlog - Java Internet Prolog");
        m_outs.println("* Version: " + JIPEngine.getVersion());
        m_outs.println("* " + JIPEngine.getCopyrightInfo());//Copyright (c) 1999-2014 By Ugo Chirico.");
        m_outs.println("* License: " + JIPEngine.getLicenseInfo());
        m_outs.println("***************************************");
        m_mainArea.prompt();
    }

    public void onDestroy()
    {
        // save properties
        Properties props = new Properties();

        // Font
        props.put("Font.Face", JIPConsoleView.s_strDefaultFontName);
        props.put("Font.Size", Integer.toString(JIPConsoleView.s_nDefaultFontSize));
        // Color
        props.put("Color", JIPConsoleView.s_defaultBckgColor == Color.white ? "white" : "black");

        // windows
        Dimension ds = m_mainFrame.getSize();
        props.put("Windows.Width", Integer.toString(ds.width));
        props.put("Windows.Height", Integer.toString(ds.height));

        try
        {
            props.save(new FileOutputStream(PROPERTIES_FILE), "JIProlog default properties");
        }
        catch(IOException ex)
        {
            //ex.printStackTrace();
            // do nothing
        }
    }
}
