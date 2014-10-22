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

import com.ugos.awt.*;
import com.ugos.io.*;
import com.ugos.jiprolog.engine.*;
import com.ugos.jiprolog.igui.*;

public class JIPConsoleViewApplet extends JIPConsoleView
{
    private Button           m_btnNewProg;
    private Button           m_btnOpenProg;
    private Button           m_btnReset;
    private Button           m_btnClose;
    
    // Constructor
    public JIPConsoleViewApplet(Frame mainFrame)
    {
        super(mainFrame);
    }
            
    // Initializzation
    public void initSouth()
    {
        m_btnNewProg  = new Button("New");
        m_btnOpenProg = new Button("Open");
        m_btnReset    = new Button("Reset");
        m_btnClose    = new Button("Stop");
        Label copyr   = new Label("Copyright (c) 1999-2007 by Ugo Chirico", Label.RIGHT);
        copyr.setFont(new Font("Arial", Font.BOLD, s_nDefaultFontSize - 2));
        copyr.setForeground(Color.white);
                          
        m_btnNewProg.addActionListener(this);
        m_btnReset.addActionListener(this);
        m_btnOpenProg.addActionListener(this);
        m_btnClose.addActionListener(this);
        
        m_btnNewProg.setEnabled(true);
        m_btnOpenProg.setEnabled(true);
        m_btnClose.setEnabled(false);
                                       
        Panel panButtons = new Panel();
    
        GridBagLayout layout   = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
    
        panButtons.setLayout(layout);
        
        Panel pan = new Panel();
        pan.setLayout(new GridLayout(1, 4, 3, 0));

        pan.add(m_btnNewProg);
        pan.add(m_btnOpenProg);
        pan.add(m_btnReset);
        pan.add(m_btnClose);
        
        // Repositioning components on main panel
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(3, 5, 3, 5);
        gbc.weightx = 1;
        
        layout.setConstraints(pan, gbc);
        panButtons.add(pan);
        
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(3, 10, 3, 0);
        gbc.fill = GridBagConstraints.NONE;
        layout.setConstraints(copyr, gbc);
        panButtons.add(copyr);
                                
        panButtons.setBackground(BKGCOLOR);
        add("South", panButtons);
    }
                                      
    public void enableNew(boolean bEnable)
    {
        // New
        m_btnNewProg.setEnabled(bEnable);
    }
    
    public void enableOpen(boolean bEnable)
    {
        // Open
        m_btnOpenProg.setEnabled(bEnable);
    }
    
    public void enableReset(boolean bEnable)
    {
        // Reset
        m_btnReset.setEnabled(bEnable);
    }
    
    public void enableStop(boolean bEnable)
    {
        // Stop
        m_btnClose.setEnabled(bEnable);
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
        else if(evt.getSource() instanceof Button)
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
                                           "\nBy Ugo Chirico, http://www.ugochirico.com" +
                                           "\nCopyright (c) 1999-2007 By Ugo Chirico." +
                                           "\nAll rights reserved");
            }
        }
    }
    
}
