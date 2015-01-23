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

public class JIPConsole extends ApplicationFrame //implements Runnable
{
//    private Thread m_workerThread;

    private JIPConsoleController m_consoleCtrl;

    // To run JIP as an application
    public static void main(String args[])
    {
    	if(args.length == 1)
    	{
    		if(args[0].equals("-debug"))
    		{
    			JIPDebugger.debug = true;
    		}
    		else if(args[0].startsWith("-h"))
    		{
    			printHelp();
    			return;
    		}
    	}

        JIPConsole console = new JIPConsole();
        console.processArgs(args);
    }

    public static void printHelp()
    {
    	System.out.println("*************************************************");
        System.out.println("** JIProlog GUI v" + JIPConsoleController.VERSION);
        System.out.println("** Based on JIProlog v" + JIPEngine.getVersion());
        System.out.println("** " + JIPEngine.getCopyrightInfo());
        System.out.println("*************************************************");
        System.out.println("\nUsage: java -classpath jiprolog.jar;jipgui.jar com.ugos.jiprolog.gui.JIPConsole [-s<searchpath>] [-c<file to compile>]* [-o<file to edit>]*");
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
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/com/ugos/jiprolog/gui/resources/beer.png")));
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
        	if(args[i].startsWith("-o"))
        	{
        		m_consoleCtrl.openFile(args[i].substring(2));
        	}
        	else if(args[i].startsWith("-c"))
        	{
        		m_consoleCtrl.compileFile(args[i].substring(2));
        	}
        	else if(args[i].startsWith("-s"))
				try
        		{
					m_consoleCtrl.getJIPEngine().setSearchPath(args[i].substring(2));
				}
        		catch (IOException e)
        		{
					e.printStackTrace();
				}
        }
    }

    public void onDestroy()
    {
        m_consoleCtrl.onDestroy();
        super.onDestroy();
    }
}
