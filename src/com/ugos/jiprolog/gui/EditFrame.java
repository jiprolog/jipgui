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

import com.ugos.awt.*;
import com.ugos.io.*;
import com.ugos.jiprolog.engine.*;

import java.awt.*;
import java.util.*;
import java.io.*;
import java.net.*;
import java.awt.event.*;

public class EditFrame extends XFrame implements JIPEventListener, ActionListener, KeyListener, MouseListener//, ItemListener
{
    private TextArea  m_editArea;
    private TextArea  m_consultArea;
    private JIPEngine m_engine;
    private String    m_strFileName;
    private boolean   m_bChanged = false;
    private Label     m_lbLineNumber;

    private static int       s_nDocument = 0;
    private static Vector    s_docsVector = new Vector(5);

    public static void newFile(JIPEngine engine)
    {
        new EditFrame(engine).show();
    }

    public static void openFile(JIPEngine engine)
    {
        EditFrame edt = new EditFrame(engine);
        String strFile = edt.openFile();
        if(strFile != null)
        {
            edt.loadFile(strFile);
            edt.update(strFile);
        }

        edt.show();
    }

    public static void openFile(JIPEngine engine, String strFileName)
    {
        new EditFrame(engine, strFileName).show();
    }

    public static void setMainFrame(Frame mainFrame)
    {
        s_docsVector.insertElementAt(mainFrame, 0);
    }

    private EditFrame(JIPEngine engine)
    {
        super();

        s_nDocument++;

        s_docsVector.addElement(this);

        // icons
        URL url = getClass().getResource("/com/ugos/jiprolog/gui/resources/logo.png");
        if(url != null)
            setIconImage(Toolkit.getDefaultToolkit().getImage(url));

        Dimension ds = JIPConsoleView.s_winDim;
        //Dimension ds = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) (ds.width * (9.0/10.0)), (int)(ds.height * (9.0/10.0)));

        m_engine = engine;

        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("File");
        MenuItem mi;

        //MenuShortcut mnsh = new MenuShortcut('n');
        //mi = new MenuItem("New", mnsh);
        mi = new MenuItem("New");
        mi.addActionListener(this);
        menu.add(mi);

        //mnsh = new MenuShortcut('o');
        //mi = new MenuItem("Open", mnsh);
        mi = new MenuItem("Open");
        mi.addActionListener(this);
        menu.add(mi);

        ////////////////

        //mnsh = new MenuShortcut('s');
        //mi = new MenuItem("Save", mnsh);
        mi = new MenuItem("Save");
        mi.addActionListener(this); // e' necessario per ogni item nel menu
        menu.add(mi);

        ////////////////

        //mnsh = new MenuShortcut('a');
        mi = new MenuItem("Save as");
        mi.addActionListener(this); // e' necessario per ogni item nel menu
        menu.add(mi);

        /////////////////

        menu.addSeparator();

        /////////////////

        //mnsh = new MenuShortcut('t');
        mi = new MenuItem("Consult");
        mi.addActionListener(this); // e' necessario per ogni item nel menu
        menu.add(mi);

        /////////////////

        menu.addSeparator();

        /////////////////

        //mnsh = new MenuShortcut('e');
        mi = new MenuItem("Close");
        mi.addActionListener(this); // e' necessario per ogni item nel menu
        menu.add(mi);

        /////////////////

        menuBar.add(menu);

        /////////////////

        // add win men�
        menu = new Menu("Windows");

        //mnsh = new MenuShortcut('j');
        mi = new MenuItem("JIProlog Console");
        mi.addActionListener(this);
        menu.add(mi);
        menuBar.add(menu);
//
//        mi = new Menu("Windows");
//        mi.addActionListener(this);
//      menu.add(mi);

        /////////////////

        /////////////////
        menu = new Menu("Help");
        //mnsh = new MenuShortcut('h');
        mi = new MenuItem("JIProlog Editor Quick Help");
        mi.addActionListener(this);
        menu.add(mi);

        /////////////////

        menuBar.add(menu);
        setMenuBar(menuBar);

        /////////////////

        setLayout(new BorderLayout());

        m_editArea    = new TextArea();
        m_consultArea = new TextArea("", 4, 20, TextArea.SCROLLBARS_VERTICAL_ONLY);

        m_editArea.addKeyListener(this);
        m_editArea.addMouseListener(this);
        Panel pan = new Panel();
        pan.setLayout(new GridLayout(1,1));

        pan.add(m_editArea);

        add("Center", pan);

        pan = new Panel();
        //pan.setLayout(new GridLayout(1,1));
        pan.setLayout(new BorderLayout());
        pan.add("Center", m_consultArea);
        //Panel panStatusBar = new Panel();
        //panStatusBar.setLayout(new FlowLayout());
        m_lbLineNumber = new Label("Line 0/0", Label.LEFT);
        pan.add("South", m_lbLineNumber);

        add("South", pan);

        Font ft = new Font(JIPConsoleView.s_strDefaultFontName, Font.PLAIN, JIPConsoleView.s_nDefaultFontSize);
        m_editArea.setFont(ft);
        m_consultArea.setFont(ft);

        m_strFileName = null;

        setTitle("New program " + Integer.toString(s_nDocument));

        m_editArea.requestFocus();

        updateLineCounter();
    }

    private EditFrame(JIPEngine engine, String strFileName)
    {
        this(engine);

        update(strFileName);

        loadFile(strFileName);
    }

    public void onDestroy()
    {
        checkChanged();
        s_docsVector.removeElement(this);

        super.onDestroy();

    }

     public void actionPerformed(ActionEvent e)
     {
        String command = e.getActionCommand();

         //System.out.println(command);

        if (command.equals("New"))
         {
            checkChanged();
            EditFrame edtFrame = new EditFrame(m_engine);
            edtFrame.show();
        }
        else if (command.equals("Open"))
         {
            checkChanged();

            m_editArea.setText("");

            String strFile = openFile();
             if(strFile != null)
             {
                 loadFile(strFile);
                 update(strFile);
             }
        }
        else if (command.equals("Save"))
         {
            saveFileEx();
        }
        else if (command.equals("Save as"))
        {
            saveFileAs();
        }
        else if (command.equals("Consult"))
        {
            if(m_editArea.getText() == "")
                return;

            waitCursor(true);

            consultFile();

            waitCursor(false);
        }
//       else if(command.startsWith(":"))
//       {
//           MenuItem mi = (MenuItem)e.getSource();
//           int nWin = Integer.parseInt(mi.getName());
//           ((Frame)s_docsVector.elementAt(nWin)).requestFocus();
//       }

//        else if(command.equals("Windows"))
//        {
//            Menu menu = (Menu)e.getSource();
//
//            CheckboxMenuItem mic;
//            for (int i = 0; i < s_docsVector.size(); i++)
//            {
//                mic = new CheckboxMenuItem(":" + ((int)(i + 1)) + " " + ((Frame)s_docsVector.elementAt(i)).getTitle(), s_docsVector.elementAt(i) == this);
//                mic.addItemListener(this);
//                menu.add(mic);
//            }
//      }
         else if (command.equals("JIProlog Console"))
         {
             ((Frame)s_docsVector.elementAt(0)).show();
         }
        else if (command.equals("JIProlog Editor Quick Help"))
        {
            m_consultArea.append("\n:Type your prolog code here and consult it by F7 or File/Consult.");
            m_consultArea.append("\nThen type your query in the console and press <enter>");
        }
        else if (command.equals("Close"))
        {
            checkChanged();
            onDestroy();
        }
     }

//    public void itemStateChanged(ItemEvent e)
//    {
//        if(e.getSource() instanceof CheckboxMenuItem)
//        {
//            String command = (String)e.getItem();
//            if(command.startsWith(":"))
//            {
//                int n = Integer.parseInt(command.substring(1, 2));
//                Frame fr = (Frame)s_docsVector.elementAt(n);
//                fr.requestFocus();
//            }
//        }
//    }

    private void waitCursor(boolean bWait)
    {
        if(bWait)
            m_editArea.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        else
            m_editArea.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    private final String openFile()
    {
        String strFileName = null;
        try
        {
            FileDialog fDlg = new FileDialog(this, "Open", FileDialog.LOAD);
            fDlg.setModal(true);
            fDlg.show();

            if(fDlg.getFile() == null)
                return null;

            strFileName = fDlg.getDirectory() + fDlg.getFile();
        }
        catch(SecurityException ex)
        {
            m_consultArea.append("\n:<e> You cannot open file maybe you are running JIP from a web page. \nDue to security limitation imposed by the browser you cannot open or save file on local machine");
        }

        return strFileName;
    }

    public boolean saveFileEx()
    {
        if(m_strFileName == null)
            return saveFileAs();
        else
            return saveFile();
    }

    private boolean saveFile()
    {
        if(!m_bChanged)
            return true;

        try
        {
            StringReader sbins = new StringReader(m_editArea.getText());
            BufferedReader ins = new BufferedReader(sbins);
            PrintWriter     outs = new PrintWriter(new FileWriter(m_strFileName));

            String strLine;
            while ((strLine = ins.readLine()) != null)
                outs.println(strLine);

            ins.close();
            outs.close();
        }
        catch(SecurityException ex)
        {
            m_consultArea.append("\n:<e> You cannot save the file maybe you are running JIP from a web page. \nDue to security limitation imposed by the browser you cannot open or save file on local machine");
            return false;
        }
        catch(IOException ex)
        {
            m_consultArea.append("\n:<e> " + ex.getMessage());
            return false;
        }

        m_consultArea.append("\n:Saved file: " + m_strFileName);

        m_bChanged = false;

        return true;
    }

    private boolean saveFileAs()
    {
        String strFileName = null;

        try
        {
            FileDialog fDlg = new FileDialog(this, "Save the trace as", FileDialog.SAVE);
            fDlg.setModal(true);
            fDlg.setFile(m_strFileName);
            fDlg.show();
            if(fDlg.getFile() == null)
                return false;

            strFileName = fDlg.getDirectory() + fDlg.getFile();
        }
        catch(SecurityException ex)
        {
            m_consultArea.append("\n:<e> You cannot save the file maybe you are running JIP from a web page. \nDue to security limitation imposed by the browser you cannot open or save file on local machine");
            return true;
        }

        update(strFileName);

        m_bChanged = true;

        return saveFile();
    }

    public void keyPressed(KeyEvent e)
    {
        if(e.getKeyCode() == KeyEvent.VK_TAB)
        {
            m_editArea.insert("   ", m_editArea.getCaretPosition());
            e.consume();
        }
        else if(e.getKeyCode() == KeyEvent.VK_F7)
        {
            consultFile();
            return;
        }

        m_bChanged = true;
    }

    public void keyReleased(KeyEvent e)
    {
        updateLineCounter();
    }

    public void keyTyped(KeyEvent e){}

    private void consultFile()
    {
        if(!saveFileEx())
            m_consultArea.append("\n:<e> " + "File not saved");

        try
        {
            StringReader ins = new StringReader(m_editArea.getText());

            m_engine.addEventListener(this);
            m_engine.consultStream(ins, m_strFileName);
            //m_engine.consultFile(m_strFileName);
            Thread.currentThread().yield();
            Thread.currentThread().sleep(200);
            m_consultArea.append("\n:File consulted");
            m_engine.removeEventListener(this);
        }
        catch(Exception ex)
        {
            m_consultArea.append("\n:<e> " + ex.getMessage());
        }
    }

    private void loadFile(Reader reader)
    {
        try
        {
            BufferedReader ins = new BufferedReader(reader);

            String strFile = "";

            String strLine;
            while ((strLine = ins.readLine()) != null)
                strFile += strLine + "\n";

            ins.close();

            m_editArea.setText(strFile);
            m_editArea.requestFocus();
        }
        catch(SecurityException ex)
        {
            m_consultArea.append("\n:<e> You cannot load a file maybe you are running JIP from a web page. \nDue to security limitation imposed by the browser you cannot open or save file on local machine");
            return;
        }
        catch(IOException ex)
        {
            m_consultArea.append("\n:<e> " + ex.getMessage());
            return;
        }

        updateLineCounter();

        m_bChanged = false;
    }

    private void loadFile(String strFilePath)
    {
        URL url = null;
        // try URL
        try
        {
            url = new URL(strFilePath);
            loadFile(new InputStreamReader(url.openStream()));
        }
        catch(MalformedURLException e)
        {
            try
            {
                loadFile(new FileReader(strFilePath));
            }
            catch(IOException ex)
            {
                m_consultArea.append("\n:<e> " + ex.getMessage());
                return;
            }
            catch(SecurityException ex)
            {
                m_consultArea.append("\n:<e> You cannot load a file maybe you are running JIP from a web page. \nDue to security limitation imposed by the browser you cannot open or save file on local machine");
                return;
            }
        }
        catch(IOException ex)
        {
            m_consultArea.append("\n:<e> " + ex.getMessage());
            return;
        }
        catch(SecurityException ex)
        {
            m_consultArea.append("\n:<e> You cannot load a file from url " + url + "\nDue to security limitation imposed by the browser");
            return;
        }
    }

    private void loadURL(URL url)
    {
        try
        {
            loadFile(new InputStreamReader(url.openStream()));
        }
        catch(IOException ex)
        {
            m_consultArea.append("\n:<e> " + ex.getMessage());
            return;
        }
        catch(SecurityException ex)
        {
            m_consultArea.append("\n:<e> You cannot load a file maybe you are running JIP from a web page. \nDue to security limitation imposed by the browser you cannot open or save file on local machine");
            return;
        }
    }

    private void update(String strTitle)
    {
        m_strFileName = strTitle;
        setTitle(strTitle);
    }

    private void checkChanged()
    {
        if(m_bChanged)
        {
            if(Dialogs.showYesNoDlg(this, "Warning", "The file has been changed. Save changes?"))
                saveFileEx();
        }
    }

    private int countLine(int nEnd)
    {
        String strContent = m_editArea.getText().substring(0, nEnd);
        int nLineCount = 0;
        for(int i = 0; i < strContent.length(); i++)
        {
            if(strContent.charAt(i) == '\r')
            {
                i++;
                nLineCount++;
            }
            else if(strContent.charAt(i) == '\n')
            {
                nLineCount++;
            }
        }
        //StringTokenizer tok = new StringTokenizer(strContent, "\r\n");
        //return tok.countTokens();
        return nLineCount;
    }

    private void updateLineCounter()
    {
        int nTotalLine = countLine(m_editArea.getText().length()) + 1;
        int nCurLine = countLine(m_editArea.getCaretPosition()) + 1;
        int nCurCol = m_editArea.getCaretPosition() - m_editArea.getText().substring(0, m_editArea.getCaretPosition()).lastIndexOf('\n');

        m_lbLineNumber.setText("[Line: " + nCurLine + "/" + nTotalLine + "] - [Col: " + nCurCol + "]");
    }

    public void mouseClicked(MouseEvent e)
    {

    }

    public void mousePressed(MouseEvent e)
    {

    }

    public void mouseReleased(MouseEvent e)
    {
        updateLineCounter();
    }

    public void mouseEntered(MouseEvent e)
    {

    }

    public void mouseExited(MouseEvent e)
    {

    }

    // A Start event occurred
    public void openNotified(JIPEvent e)
    {
    }

    public void moreNotified(JIPEvent e)
    {
    }

    // A solution event occurred
    public void solutionNotified(JIPEvent e)
    {

    }

    // A Term has been notified with notify/2
    public void termNotified(JIPEvent e)
    {
//      System.out.println("termNotified");
        if(0 == e.getQueryHandle())
        {
            if(e.getID() == JIPEvent.ID_SINGLETONVARS)
            {
                JIPCons cons = (JIPCons)e.getTerm();
                String strVars = cons.getTail().toString(m_engine);
                m_consultArea.append("\n- Warning, singleton variables found at line " + cons.getHead() + ": " + strVars);
                e.consume();
            }
        }
    }

    // The end has been reached because there wasn't more solutions
    public void endNotified(JIPEvent e)
    {

    }

    public void closeNotified(JIPEvent e)
    {

    }

    // An error (exception) has been raised up by prolog engine
    public void errorNotified(JIPErrorEvent e)
    {

    }
}
