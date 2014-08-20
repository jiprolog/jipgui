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
//        if(args.length > 0)
//        {
//            if(!searchForJIPConsole(args[0]));
//            {
//                JIPConsole console = new JIPConsole();
//                console.processArgs(args);
//            }
//        }
//        else
//        {
            JIPConsole console = new JIPConsole();
            console.processArgs(args);
//        }
    }
    
//    public static boolean searchForJIPConsole(String strFileName)
//    {
//        try
//        {
//            Socket socket = new Socket("localhost", 9999);
//
//            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
//
//            // send command
//            writer.println("edit:" + strFileName);
//
//            socket.setSoTimeout(200);
//            //
//            reader.readLine();
//
//            return true;
//            // exit
//            //System.exit(0);
//        }
//        catch(Exception ex)
//        {
//            return false;
//        }
//    }
//
//    public void run()
//    {
//        try
//        {
//            ServerSocket servSocket = new ServerSocket(9999);
//
//            while(m_workerThread != null)
//            {
////              System.out.println("b accept");
//                Socket socket = servSocket.accept();
////              System.out.println("a accept");
//
//                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
//                String strLine = reader.readLine();
////              System.out.println(strLine);
//                String strFileName;
//                if(strLine.startsWith("edit:"))
//                {
//                    strFileName = strLine.substring(5);
//                    m_consoleCtrl.openFile(strFileName);
//                    writer.println("ok");
//                }
//                else
//                    writer.println("unknown command");
//
//                reader.close();
//                writer.close();
//            }
//        }
//        catch(IOException ex)
//        {
//            throw new RuntimeException(ex.getMessage());
//        }
//    }

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
                                                    
//        m_workerThread = new Thread(this);
//        m_workerThread.setDaemon(true);
//        m_workerThread.setName("JIProlog Console thread");
//        m_workerThread.start();
        
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
        
//        m_workerThread.stop();
//        m_workerThread = null;
                    
        super.onDestroy();
    }
}
