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


package com.ugos.JIProlog.gui;

import com.ugos.JIProlog.igui.*;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import com.ugos.JIProlog.engine.*;
import com.ugos.awt.*;
import com.ugos.io.*;

public class JIPConsole extends ApplicationFrame //implements Runnable
{
//    private Thread m_workerThread;

    private JIPConsoleController m_consoleCtrl;

    // To run JIP as an application
    public static void main(String args[])
    {
            JIPConsole console = new JIPConsole();
            console.processArgs(args);
    }

    // Constructor
    public JIPConsole()
    {
        super(JIPConsoleController.TITLE);

        // set dimension
        setSize(JIPConsoleView.s_winDim.width, JIPConsoleView.s_winDim.height);

        final SplashWnd splash = new SplashWnd(this);
        splash.show();

        try
        {
            Thread.currentThread().yield();
            Thread.currentThread().sleep(1500);

            // set icon
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/com/ugos/JIProlog/gui/resources/icoJip.gif")));
        }
        catch(Throwable ex)
        {}

        JIPConsoleView view = new JIPConsoleViewApp(this);
        add(view);

        m_consoleCtrl = new JIPConsoleController(view, null);

        splash.setVisible(false);
        splash.dispose();

        show();

        m_consoleCtrl.start();
    }

    private void processArgs(String args[])
    {
        for(int i = 0; i < args.length; i++)
        {
            m_consoleCtrl.openFile(args[i]);
        }
    }

    public void onDestroy()
    {
        m_consoleCtrl.onDestroy();
        super.onDestroy();
    }
}
