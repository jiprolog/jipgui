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

import java.awt.Toolkit;
import java.io.IOException;
import java.util.Vector;

import com.ugos.awt.ApplicationFrame;
import com.ugos.jiprolog.JIProlog;
import com.ugos.jiprolog.engine.JIPDebugger;
import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPSyntaxErrorException;

public class JIPConsole extends ApplicationFrame //implements Runnable
{
//    private Thread m_workerThread;

    private JIPConsoleController m_consoleCtrl;

    private static Vector<String> fileToOpen = new Vector<String>();
    private static Vector<String> fileToConsult = new Vector<String>();
    private static String initializationGoal;
    private static String searchPath;
    private static boolean noui;


    // To run JIP as an application
    public static void main(String args[])
    {
        try {
			processArgs(args);
		} catch (JIPSyntaxErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        if(noui)
        {
        	JIProlog.main(args);
        }
        else
        {
        	JIPConsole console = new JIPConsole();

        	if(searchPath != null)
        		console.m_consoleCtrl.setSearchPath(searchPath);

        	for(String file : fileToOpen)
        	{
        		console.m_consoleCtrl.openFile(file);
        	}

        	for(String file : fileToConsult)
        	{
        		console.m_consoleCtrl.consultFile(file);
        	}

        	if(initializationGoal != null)
        	{
        		console.m_consoleCtrl.onQuery(initializationGoal);
        	}
        }
    }

    public static void printHelp()
    {
    	System.out.println("*************************************************");
        System.out.println("** JIProlog GUI v" + JIPConsoleController.VERSION);
        System.out.println("** Based on JIProlog v" + JIPEngine.getVersion());
        System.out.println("** " + JIPEngine.getCopyrightInfo());
        System.out.println("*************************************************");
        System.out.println("\nOptions:");
        System.out.println("\n -debug to run JIProlog in debug mode");
        System.out.println("\n -o <file> to open a prolog file in edit mode");
        System.out.println("\n -c <file> to compile a prolog file");
        System.out.println("\n -g <goal> initialization goal");
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

    private static void processArgs(String args[]) throws JIPSyntaxErrorException, IOException
    {
    	if(args.length == 1)
    	{
    		if(args[0].startsWith("-h"))
    		{
    			printHelp();
    			return;
    		}
    		else if(args[0].startsWith("-version"))
        	{
    	        System.out.println("JIProlog GUI v" + JIPConsoleController.VERSION);
    	        System.out.println("Based on JIProlog v" + JIPEngine.getVersion());
        	}
    		else if(args[0].startsWith("-debug"))
        	{
        		JIPDebugger.debug = true;
        	}
    	}
    	else
    	{
	        for(int i = 0; i < args.length; i++)
	        {
	        	if(args[i].startsWith("-debug"))
	        	{
	        		JIPDebugger.debug = true;
	        	}
	        	else if(args[i].startsWith("-n"))
	        	{
	        		noui = true;
	        	}
	        	else if(args[i].startsWith("-o"))
	        	{
	        		if(i + 1 < args.length)
	        		{
	        			fileToOpen.add(args[i + 1]);
	        			i++;
	        		}
	        	}
	        	else if(args[i].startsWith("-c"))
	        	{
	        		if(i + 1 < args.length)
	        		{
	        			fileToConsult.add(args[i + 1]);
	        			i++;
	        		}
	        	}
	        	else if(args[i].startsWith("-g"))
	        	{
	        		if(i + 1 < args.length)
	        		{
	        			initializationGoal = args[i + 1];
	        			i++;
	        		}
	        	}
	        	else if(args[i].startsWith("-d"))
	        	{
	        		if(i + 1 < args.length)
	        		{
	        			searchPath = args[i + 1];
	        			i++;
	        		}
	        	}
	        }
    	}

//    	if(initializationGoal != null)
//    	{
//    		 m_consoleCtrl.runGoal(initializationGoal);
//    	}
    }

    public void onDestroy()
    {
        m_consoleCtrl.onDestroy();
        super.onDestroy();
    }
}
