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

import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import com.ugos.awt.Dialogs;
import com.ugos.jiprolog.engine.JIPEngine;

public class JIPConsoleViewApp extends JIPConsoleView implements ItemListener
{
    private Menu             m_mnuFile;
    private Menu             m_mnuTools;
   
    //private Menu             m_mnuLibs;
    private Menu             m_mnuColors;
    private Menu             m_mnuFontSize;
//  private Menu             m_mnuFontName;
    private Menu             m_mnuWinDim;
    private Menu             m_mnuReg;

    MenuItem				 m_mniTrace;
    
//    private CheckboxMenuItem m_mnuiTrace;
    private CheckboxMenuItem m_curmnuiFontSize;
    private CheckboxMenuItem m_curmnuiDimensions = null;

    // Constructor
    public JIPConsoleViewApp(Frame mainFrame)
    {
        super(mainFrame);
    }

    // Initializzation
    public void init()
    {
        // Set Menu
        MenuBar menuBar = new MenuBar();
        m_mnuFile = new Menu("File", true);

        MenuItem mi;
        //MenuShortcut mnsh;

        //mnsh = new MenuShortcut('n');
        mi = new MenuItem("New");
        mi.addActionListener(this);
        m_mnuFile.add(mi);

        //mnsh = new MenuShortcut('o');
        mi = new MenuItem("Open");
        mi.addActionListener(this);
        m_mnuFile.add(mi);

        m_mnuFile.addSeparator();

        //mnsh = new MenuShortcut('x');
        mi = new MenuItem("Exit");
        mi.addActionListener(this); // e' necessario per ogni item nel menu
        m_mnuFile.add(mi);

        /////////////////

        menuBar.add(m_mnuFile);

        /////////////////

        /////////////////

        CheckboxMenuItem mic;

        m_mnuTools = new Menu("Tools");
        
        m_mniTrace = mi = new MenuItem("Trace to file");
        mi.setActionCommand("TraceToFile");
        mi.addActionListener(this);
        m_mnuTools.add(mi);
        
        //mnsh = new MenuShortcut('d');
        mi = new MenuItem("Reset database");
        mi.setActionCommand("Reset");
        mi.addActionListener(this);
        m_mnuTools.add(mi);

        //mnsh = new MenuShortcut('s');
        mi = new MenuItem("Stop execution");
        mi.setActionCommand("Stop");
        mi.addActionListener(this);
        mi.setEnabled(false);
        m_mnuTools.add(mi);

//        m_mnuiTrace = new CheckboxMenuItem("Trace", false);
//        m_mnuiTrace.addItemListener(this);
//        m_mnuTools.add(m_mnuiTrace);

        m_mnuTools.addSeparator();
        menuBar.add(m_mnuTools);

        // Font
        m_mnuFontSize = new Menu("Font Size");
        for (int i = 6; i < 21; i++)
        {
            mic = new CheckboxMenuItem("size: " + i, i == s_nDefaultFontSize);
            if(i == s_nDefaultFontSize)
                m_curmnuiFontSize = mic;

            mic.addItemListener(this);
            m_mnuFontSize.add(mic);
        }

        m_mnuTools.add(m_mnuFontSize);

        // Font
        m_mnuWinDim = new Menu("Window Size");

        // windows
        Dimension ds = Toolkit.getDefaultToolkit().getScreenSize();
        ds.width  *= (9.0/10.0);
        ds.height *= (9.0/10.0);
        String strBestDim = ds.width + "x" + ds.height;
        mic = new CheckboxMenuItem("1: " + strBestDim + " (best)", false);
        mic.addItemListener(this);
        m_mnuWinDim.add(mic);

        ds = s_winDim;
        String strInitDim = ds.width + "x" + ds.height;
        mic = new CheckboxMenuItem("2: " + strInitDim + " (initial)", false);
        mic.addItemListener(this);
        m_mnuWinDim.add(mic);

        double dRatio = (double)s_winDim.height / (double)s_winDim.width;

        int nWidth  = 200;
        int nHeight = (int)(nWidth * dRatio);

        for(int i = 3; i < 10; i++)
        {
            if(!strBestDim.equals(nWidth + "x" + nHeight) && !strInitDim.equals(nWidth + "x" + nHeight))
            {
                mic = new CheckboxMenuItem(i + ": " + nWidth + "x" + nHeight, false);
                mic.addItemListener(this);
                m_mnuWinDim.add(mic);
            }

            nWidth  += 100;
            nHeight = (int)(nWidth * dRatio);
        }

        m_mnuTools.add(m_mnuWinDim);

        // Colors
        m_mnuColors = new Menu("Background Color");
        mic = new CheckboxMenuItem("White", s_defaultBckgColor == Color.white);
        mic.addItemListener(this);
        m_mnuColors.add(mic);

        mic = new CheckboxMenuItem("Black", s_defaultBckgColor == Color.black);
        mic.addItemListener(this);
        m_mnuColors.add(mic);
        m_mnuTools.add(m_mnuColors);

        /////////////////

        m_mnuReg = new Menu("Help");
        //mnsh = new MenuShortcut('r');
        mi = new MenuItem("View License");
        mi.setActionCommand("ViewReg");
        mi.addActionListener(this);
        m_mnuReg.add(mi);

        //mnsh = new MenuShortcut('b');
        mi = new MenuItem("About");
        mi.setActionCommand("About");
        mi.addActionListener(this);
        m_mnuReg.add(mi);

        menuBar.add(m_mnuReg);

        m_mainFrame.setMenuBar(menuBar);

        /////////////////

        super.init();
    }



    public void enableNew(boolean bEnable)
    {
        // New
        m_mnuFile.getItem(0).setEnabled(bEnable);
    }

    public void enableOpen(boolean bEnable)
    {
        // Open
        m_mnuFile.getItem(1).setEnabled(bEnable);
    }

    public void enableReset(boolean bEnable)
    {
        // Reset
        m_mnuTools.getItem(0).setEnabled(bEnable);
    }

    public void enableStop(boolean bEnable)
    {
        // Stop
        m_mnuTools.getItem(1).setEnabled(bEnable);
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
            else if(command.equals("TraceToFile"))
            {
            	m_mniTrace.setActionCommand("TraceToWindows");
            	m_mniTrace.setLabel("Trace to Trace dialog");
                m_consoleCtrl.onRedirectTrace();
            }
            else if(command.equals("TraceToWindows"))
            {
            	m_mniTrace.setActionCommand("TraceToFile");
            	m_mniTrace.setLabel("Trace to file");
                m_consoleCtrl.onRedirectTrace();
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
                Dialogs.showTextMessageDlg(m_mainFrame, "Registration", JIPEngine.getLicenseInfo());
            }
            else if(command.equals("About"))
            {
                Dialogs.showTextMessageDlg(m_mainFrame, "About",
                                           "\nJIProlog Console v" + JIPConsoleController.VERSION +
                                           "\nBased on JIProlog v" + JIPEngine.getVersion() +
                                           "\nBy Ugo Chirico, http://www.jiprolog.com" +
                                           "\nCopyright (c) 1999-2012 By Ugo Chirico." +
                                           "\nAll rights reserved");
            }
        }
    }

    public void itemStateChanged(ItemEvent e)
    {
        if(e.getSource() instanceof CheckboxMenuItem)
        {
            String command = (String)e.getItem();
            if(command.equals("White"))
            {
                m_mainArea.setBackground(Color.white);
                m_mainArea.setForeground(Color.black);
                ((CheckboxMenuItem)m_mnuColors.getItem(0)).setState(true);
                ((CheckboxMenuItem)m_mnuColors.getItem(1)).setState(false);
                s_defaultBckgColor = Color.white;
            }
            else if(command.equals("Black"))
            {
                m_mainArea.setBackground(Color.black);
                m_mainArea.setForeground(Color.white);
                ((CheckboxMenuItem)m_mnuColors.getItem(0)).setState(false);
                ((CheckboxMenuItem)m_mnuColors.getItem(1)).setState(true);
                s_defaultBckgColor = Color.black;
            }
            else if(command.startsWith("size: "))
            {
                //System.out.println("size: " + command.substring(6));
                s_nDefaultFontSize = Integer.parseInt(command.substring(6));
                m_mainArea.setFont(new Font(s_strDefaultFontName, Font.PLAIN, s_nDefaultFontSize));
                m_mainArea.invalidate();
                m_curmnuiFontSize.setState(false);
                m_curmnuiFontSize = (CheckboxMenuItem)e.getSource();
            }
            else // window dimension  1: 200x300
            {
                //System.out.println("dim: " + command.substring(2));
                s_winDim = new Dimension(Integer.parseInt(command.substring(3,6)), Integer.parseInt(command.substring(7,10)));
                m_mainFrame.setSize(s_winDim);
                m_mainFrame.validate();
                if(m_curmnuiDimensions != null)
                    m_curmnuiDimensions.setState(false);
                m_curmnuiDimensions = (CheckboxMenuItem)e.getSource();
            }
//          else if(command.startsWith("name: "))
//          {
//              System.out.println("name " + command.substring(6));
//              s_strDefaultFontName = command.substring(6);
//              m_mainArea.setFont(new Font(s_strDefaultFontName, Font.PLAIN, s_nDefaultFontSize));
//              m_mainArea.invalidate();
//              m_curmnuiFontName.setState(false);
//              m_curmnuiFontName = (CheckboxMenuItem)e.getSource();
//            }
        }

        m_mainArea.requestFocus();
    }
}
