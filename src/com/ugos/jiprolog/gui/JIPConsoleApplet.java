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

import java.applet.Applet;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import com.ugos.awt.*;
import com.ugos.io.*;
import com.ugos.jiprolog.engine.*;
import com.ugos.jiprolog.igui.*;

public class JIPConsoleApplet extends Applet
{
    // Panel's Field
    private Button           m_btnNewProg;
    private Button           m_btnOpenProg;
    private Button           m_btnReset;
    private Button           m_btnClose;
    
    private JIPConsoleController m_consoleCtrl;
    
//    // Initialization
//    public void init()
//    {
//
//    }
        

    // Start
    public void start()
    {
        // Search for main frame
        // (used for predicates concerning windows such as wmsgbox, etc.)
        java.awt.Container ct = getParent();
        while(!(ct instanceof Frame) && ct != null)
        {
            ct = ct.getParent();
        }
        
        //System.out.println(ct);
        if(ct == null)
            ct = new Frame();
        
        final SplashWnd splash = new SplashWnd(new Frame());
        splash.show();
                
        try
        {
            Thread.currentThread().sleep(1500);
        }
        catch(InterruptedException ex)
        {}
        
        setLayout(new GridLayout(1,1));
        JIPConsoleViewApplet view = new JIPConsoleViewApplet((Frame)ct);
        add(view);
                
        m_consoleCtrl = new JIPConsoleController(view, getCodeBase().toString());
                                                                            
        show();
                
        splash.setVisible(false);
        splash.dispose();
        
//        try
//        {
//            m_consoleCtrl.setSearchPath(getCodeBase().toString());
//        }
//        catch(Throwable th)
//        {
//            System.out.println(th);
//        }
            
        m_consoleCtrl.start();
        
//        // Search for main frame
//        // (used for predicates concerning windows such as wmsgbox, etc.)
//        java.awt.Container ct = getParent();
//        while(!(ct instanceof Frame) && ct != null)
//        {
//            ct = ct.getParent();
//        }
//
//        //System.out.println(ct);
//        if(ct == null)
//            ct = new Frame();
        
        //m_prolog.setMainFrame((Frame)ct);
        //EditFrame.setMainFrame((Frame)ct);
    }
                 
    public void stop()
    {
        m_consoleCtrl.onStop();
    }
    
    // Consult a prolog file
    public void consult(String strFilePath)
    {
        m_consoleCtrl.openFile(strFilePath);
    }
            
    // Applet Info
    public String getAppletInfo()
    {
        return "JIProlog - Java Internet Prolog\n Applet v" + JIPEngine.getVersion()
            + " by Ugo Chirico."
            + "\nCopyright (c) 1999-2007 by Ugo Chirico. All rights reserved"
            + "\nContacts: jiprolog@ugosweb.com or visit http://www.ugochirico.com";
    }
}
