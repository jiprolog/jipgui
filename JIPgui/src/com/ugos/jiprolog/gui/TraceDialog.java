/*
 * 09/19/2002
 *
 * Copyright (C) 1999-2004 Ugo Chirico
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

import com.ugos.awt.XDialog;
import com.ugos.io.*;
import com.ugos.jiprolog.engine.*;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.awt.event.*;
    
public class TraceDialog extends XDialog implements ActionListener, JIPTraceListener
{
    private static int MAX_TEXT_SIZE = 32768 * 2;
    private static int TRESHOULD = 1024 * 2;
    
    private Button              m_btnSkip;
    private Button              m_btnStep;
    private Button              m_btnAbort;
    private Button              m_btnAll;
    private Button              m_btnSave;
    private Button              m_btnHide;
    private Button              m_btnRetry;
        
    private TextArea            m_traceArea;
    
    private boolean         m_bContinue = false;
    private JIPTraceEvent   m_traceEvent;
    
    private PrintStream     m_outs             = null;
    private JIPEngine       m_prolog = null;
    
    public TraceDialog(Frame parent, JIPEngine prolog)
    {
        super(parent, "JIProlog - Trace Window", false);
        
        m_prolog = prolog;
        
        setResizable(true);
    
        m_btnStep       = new Button("  Step   ");
        m_btnAbort      = new Button("  Abort  ");
        m_btnSkip       = new Button("  Skip   ");
        m_btnRetry      = new Button("  Retry  ");
        m_btnAll        = new Button("Trace All");
        m_btnHide       = new Button("  Hide   ");
        m_btnSave       = new Button("  Save   ");
        
        m_traceArea   = new TextArea();
        
        Font fnt = new Font(JIPConsoleView.s_strDefaultFontName, Font.PLAIN, JIPConsoleView.s_nDefaultFontSize);
        Font fntBtn = new Font(JIPConsoleView.s_strDefaultFontName, Font.PLAIN, JIPConsoleView.s_nDefaultFontSize - 1);
        //Font fnt = new Font("Arial", Font.PLAIN, 10);
        m_traceArea.setFont(fnt);
        m_btnSave.setFont(fntBtn);
        m_btnHide.setFont(fntBtn);
        m_btnAll.setFont(fntBtn);
        m_btnStep.setFont(fntBtn);
        m_btnAbort.setFont(fntBtn);
        m_btnSkip.setFont(fntBtn);
        m_btnRetry.setFont(fntBtn);
      
        m_btnStep.addActionListener(this);
        m_btnAbort.addActionListener(this);
        m_btnSkip.addActionListener(this);
        m_btnAll.addActionListener(this);
        m_btnHide.addActionListener(this);
        m_btnSave.addActionListener(this);
        m_btnRetry.addActionListener(this);
        
        m_btnStep.setEnabled(false);
        m_btnSkip.setEnabled(false);
        m_btnAbort.setEnabled(false);
        m_btnHide.setEnabled(false);
        m_btnSave.setEnabled(false);
        m_btnRetry.setEnabled(false);
        m_btnAll.setEnabled(false);
                  
        setLayout(new BorderLayout());
        //setLayout(new FlowLayout());
       
        Panel panBtn = new Panel();
                
        panBtn.setLayout(new FlowLayout(FlowLayout.LEFT));
        panBtn.add(m_btnStep);
        panBtn.add(m_btnSkip);
        panBtn.add(m_btnRetry);
        panBtn.add(m_btnAll);
                
        Panel panDownBtn = new Panel();
        panDownBtn.setLayout(new FlowLayout(FlowLayout.LEFT));
        panDownBtn.add(m_btnAbort);
        panDownBtn.add(m_btnSave);
        panDownBtn.add(m_btnHide);
        
//      m_btnStep.setBounds(10,10, JIPConsoleView.s_nDefaultFontSize * 10, JIPConsoleView.s_nDefaultFontSize * 2);
//        m_btnSkip.setSize(JIPConsoleView.s_nDefaultFontSize * 10, JIPConsoleView.s_nDefaultFontSize * 2);
//        m_btnAbort.setSize(JIPConsoleView.s_nDefaultFontSize * 10, JIPConsoleView.s_nDefaultFontSize * 2);
//        m_btnHide.setSize(JIPConsoleView.s_nDefaultFontSize * 10, JIPConsoleView.s_nDefaultFontSize * 2);
//        m_btnSave.setSize(JIPConsoleView.s_nDefaultFontSize * 10, JIPConsoleView.s_nDefaultFontSize * 2);
//        m_btnRetry.setSize(JIPConsoleView.s_nDefaultFontSize * 10, JIPConsoleView.s_nDefaultFontSize * 2);
//        m_btnAll.setSize(JIPConsoleView.s_nDefaultFontSize * 10, JIPConsoleView.s_nDefaultFontSize * 2);
              
        add("North", panBtn);
        add("Center", m_traceArea);
        add("South", panDownBtn);
        m_outs = new PrintStream(new TextAreaOutputStream(m_traceArea), false);
     }
    
     public void actionPerformed(ActionEvent evt)
     {
        if(evt.getSource() == m_btnStep)
        {
            waitCursor(true);
            m_btnStep.setEnabled(false);
            m_traceEvent.nextStep();
        }
        else if(evt.getSource() == m_btnSkip)
        {
            waitCursor(true);
            m_btnSkip.setEnabled(false);
            m_traceEvent.skip();
        }
        else if(evt.getSource() == m_btnRetry)
        {
            waitCursor(true);
            m_btnRetry.setEnabled(false);
            m_traceEvent.retry();
        }
        else if (evt.getSource() == m_btnAbort)
        {
            waitCursor(true);
            m_traceEvent.abort();
            setVisible(false);
        }
        else if (evt.getSource() == m_btnAll)
         {
            waitCursor(true);
            m_bContinue = true;
            m_btnStep.setEnabled(false);
            m_btnSkip.setEnabled(false);
            m_btnRetry.setEnabled(false);
            m_btnAbort.setEnabled(true);
            m_btnAll.setEnabled(false);
            m_traceEvent.nextStep();
        }
        else if (evt.getSource() == m_btnHide)
        {
            setVisible(false);
        }
        else if (evt.getSource() == m_btnSave)
        {
            saveContent();
        }
     }
    
    private void saveContent()
    {
        try
        {
            FileDialog fDlg = new FileDialog((Frame)getParent(), "Save as", FileDialog.SAVE);
            fDlg.setModal(true);
            fDlg.setFile("");
            fDlg.show();
            if(fDlg.getFile() == null)
                return;
                
            String strFileName = fDlg.getDirectory() + fDlg.getFile();
            StringReader sbins = new StringReader(m_traceArea.getText());
            BufferedReader ins = new BufferedReader(sbins);
            PrintWriter     outs = new PrintWriter(new FileWriter(strFileName));
                                    
            String strLine;
            while ((strLine = ins.readLine()) != null)
                outs.println(strLine);
                
            ins.close();
            outs.close();
        }
        catch(SecurityException ex)
        {
            m_traceArea.append("\n:<e> You cannot save the file maybe you are running JIP from a web page. \nDue to security limitation imposed by the browser you cannot open or save file on local machine\n");
        }
        catch(IOException ex)
        {
            m_traceArea.append("\n:<e> " + ex.getMessage()+"\n");
        }
    }
    
    private void waitCursor(boolean bWait)
    {
        if(bWait)
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
        else
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    public void callNotified(JIPTraceEvent e)
    {
        setVisible(true);
        
        m_traceEvent = e;

//        StringBuffer strSpace = new StringBuffer(e.getLevel());
//        for (int i = 0; i < e.getLevel() - 1; i++)
//        {
//            strSpace.append(".");
//        }
        
        m_outs.print("[" + e.getLevel() + "]");
//        m_outs.print("> ");
//        m_outs.print(strSpace);
        m_outs.print("CALL:  ");
        m_outs.println(e.getTerm().toString(m_prolog));
                
        Thread.currentThread().yield();
                
        m_btnStep.setEnabled(true);
        m_btnSkip.setEnabled(true);
        m_btnRetry.setEnabled(false);
        m_btnAbort.setEnabled(true);
        m_btnHide.setEnabled(false);
        m_btnSave.setEnabled(true);
        m_btnAll.setEnabled(true);
        
        if(m_bContinue)
        {
            // Don't wait for user input
            e.nextStep();
            return;
        }
        
        waitCursor(false);
        
        // Wait fo user input
    }

    public void foundNotified(JIPTraceEvent e)
    {
        setVisible(true);
        
        m_traceEvent = e;
        
//        StringBuffer strSpace = new StringBuffer((int)e.getLevel());
//        for (int i = 0; i < e.getLevel() - 1; i++)
//        {
//            strSpace.append(".");
//        }
        m_outs.print("[" + e.getLevel() + "]");
//      m_outs.print(e.getLevel());
//        m_outs.print("> ");
//        m_outs.print(strSpace);
        m_outs.print("FOUND: ");
                
        m_outs.println(e.getTerm().toString(m_prolog));
        
        Thread.currentThread().yield();
        
        m_btnStep.setEnabled(true);
        m_btnSkip.setEnabled(true);
        m_btnRetry.setEnabled(true);
        m_btnAbort.setEnabled(true);
        m_btnHide.setEnabled(false);
        m_btnSave.setEnabled(true);
        m_btnAll.setEnabled(true);
        
        if(m_bContinue)
        {
            // Don't wait for user input
            e.nextStep();
            return;
        }
        
        waitCursor(false);
        
        //wait for user input
        // maybe retry

        // Don't wait for user input
        //e.nextStep();
    }
    
    public void bindNotified(JIPTraceEvent e)
    {
        setVisible(true);
        
        m_traceEvent = e;
        
//        StringBuffer strSpace = new StringBuffer(e.getLevel());
//        for (int i = 0; i < e.getLevel() - 1; i++)
//        {
//            strSpace.append(".");
//        }
        //
        m_outs.print("[" + e.getLevel() + "]");
//        m_outs.print(e.getLevel());
//        m_outs.print("> ");
//        m_outs.print(strSpace);
        m_outs.print("BIND:  ");
        m_outs.println(e.getTerm().toString(m_prolog));
                
        Thread.currentThread().yield();
        
//        if(m_bContinue)
//        {
//            // Don't wait for user input
//            e.nextStep();
//            return;
//        }

        m_btnStep.setEnabled(true);
        m_btnRetry.setEnabled(false);
        m_btnSkip.setEnabled(false);
        m_btnAbort.setEnabled(true);
        m_btnHide.setEnabled(false);
        m_btnSave.setEnabled(true);
        m_btnAll.setEnabled(true);
        // wait for user input
        
        //dont waiy
        e.nextStep();
    }

    public void failNotified(JIPTraceEvent e)
    {
        setVisible(true);
        
        m_traceEvent = e;

//        StringBuffer strSpace = new StringBuffer(e.getLevel());
//        for (int i = 0; i < e.getLevel() - 1; i++)
//        {
//            strSpace.append(".");
//        }
        //
        m_outs.print("[" + e.getLevel() + "]");
//        m_outs.print(e.getLevel());
//        m_outs.print("> ");
//        m_outs.print(strSpace);
        m_outs.print("FAIL:  ");
        m_outs.println(e.getTerm().toString(m_prolog));
                        
        m_btnSkip.setEnabled(false);
        
        Thread.currentThread().yield();
        
        // Don't wait for user input
        e.nextStep();
    }

    public void redoNotified(JIPTraceEvent e)
    {
        setVisible(true);
        
        m_traceEvent = e;

//        StringBuffer strSpace = new StringBuffer(e.getLevel());
//        for (int i = 0; i < e.getLevel() - 1; i++)
//        {
//            strSpace.append(".");
//        }
        //
        m_outs.print("[" + e.getLevel() + "]");
//        m_outs.print(e.getLevel());
//        m_outs.print("> ");
//        m_outs.print(strSpace);
        m_outs.print("REDO:  ");
        
        //m_outs.println(e);
        //m_outs.println(e.getTerm());
        
        
        m_outs.println(e.getTerm().toString(m_prolog));
                
        //waitCursor(false);
               
        Thread.currentThread().yield();
        
        // Don't wait for user input
        e.nextStep();
    }
    
    public void exitNotified(JIPTraceEvent e)
    {
        setVisible(true);
        
        m_traceEvent = e;

//        StringBuffer strSpace = new StringBuffer(e.getLevel());
//        for (int i = 0; i < e.getLevel() - 1; i++)
//        {
//            strSpace.append(".");
//        }
        //
        m_outs.print("[" + e.getLevel() + "]");
//        m_outs.print(e.getLevel());
//        m_outs.print("> ");
//        m_outs.print(strSpace);
        m_outs.print("EXIT:  ");
        m_outs.println(e.getTerm().toString(m_prolog));
                
        Thread.currentThread().yield();
        
        m_btnStep.setEnabled(false);
        m_btnRetry.setEnabled(false);
        m_btnSkip.setEnabled(false);
        m_btnAbort.setEnabled(false);
        m_btnHide.setEnabled(true);
        m_btnSave.setEnabled(true);
        m_btnAll.setEnabled(false);
        
        // Don't wait for user input
        e.nextStep();
    }
    
    // A Start event occurred
    public void startNotified(JIPTraceEvent e)
    {
        setVisible(true);
        
        Dimension ds = JIPConsoleView.s_winDim;
        
        int nWidth = (int) (ds.width * (8.0/10.0));
        int nHeigth = (int) (ds.height * (5.0/10.0));
                        
        Rectangle d = getParent().getBounds();
            
        int nTop  = d.y + (d.height - nHeigth) / 2;
        int nLeft = d.x + (d.width  - nWidth)  / 2;
        setBounds(nLeft, nTop, nWidth, nHeigth);
        
//        m_traceArea.setText("");
//
//        m_outs.println();
//        m_outs.println("-----START TRACING-----");
//        m_outs.println();
        
        m_bContinue = false;
        m_btnAll.setEnabled(true);
        m_btnHide.setEnabled(true);
        m_btnSave.setEnabled(true);
        
//        if(m_traceArea.getText().length() > MAX_TEXT_SIZE)
//        {
//            m_traceArea.setText(m_traceArea.getText().substring(TRESHOULD));
//        }
    }
    
    // A Stop event occurred
    public void stopNotified(JIPTraceEvent e)
    {
        m_btnAbort.setEnabled(false);
        m_btnSkip.setEnabled(false);
        m_btnStep.setEnabled(false);
        m_btnRetry.setEnabled(false);
        m_btnHide.setEnabled(true);
        m_btnSave.setEnabled(true);
        m_btnAll.setEnabled(false);
        
        waitCursor(false);
    }
    
    public void windowClosing(WindowEvent e)
    {
        // do nothing
        setVisible(false);
    }
}
