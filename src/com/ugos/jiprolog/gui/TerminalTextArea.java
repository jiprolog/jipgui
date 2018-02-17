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

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class TerminalTextArea extends TextArea implements KeyListener
{
    private static int MAX_TEXT_SIZE = 8192;
    private static int TRESHOULD = 1024;

    transient ActionListener actionListener;

    private boolean m_bEnableAction = true;

    private static final int HISTORY_LEN = 30;

    private String m_strPrompt;

    // Caret Position for prompt
    private int             m_nPromptPos;
    private int             m_nTextPos;

    // Prompt Hystory
    private int             m_nHistoryIndex;
    private Vector          m_historyVect;
    private boolean         m_bRecordHistory;

    public TerminalTextArea(String strText, int nRow, int nCol, int nScrollBars)
    {
        super(strText, nRow, nCol, nScrollBars);

        m_historyVect = new Vector();

        // update history
        m_nHistoryIndex = -1;

        addKeyListener(this);

        m_bRecordHistory = true;
    }

    public void recordHistory(boolean bRecord)
    {
        m_bRecordHistory = bRecord;
    }

    public void keyPressed(KeyEvent e)
    {
        if(!m_bEnableAction)
            return;

        if((e.getModifiers() & e.CTRL_MASK) == e.CTRL_MASK || 
        		(e.getModifiers() & e.CTRL_DOWN_MASK) == e.CTRL_DOWN_MASK ||
        		(e.getModifiers() & e.META_MASK) == e.META_MASK || 
        		(e.getModifiers() & e.META_DOWN_MASK) == e.META_DOWN_MASK)
        {
            return;
        }

        if(getSelectionStart() < m_nPromptPos)
        {
            setSelectionStart(m_nPromptPos);
        }

        if(e.getKeyCode() == e.VK_ENTER)
        {
            e.consume();

            String strQuery = getText().substring(m_nTextPos, getText().length());

            // update history
            if(m_bRecordHistory && !strQuery.equals(""))
            {
                m_historyVect.addElement(strQuery);
                m_nHistoryIndex = m_historyVect.size();

                if(m_historyVect.size() > HISTORY_LEN)
                    m_historyVect.removeElementAt(0);
            }

            // Genera l'ActionEvent
            ActionEvent ae = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, strQuery);
            processEvent(ae);
        }

        else if(e.getKeyCode() == KeyEvent.VK_DOWN)
        {
            if(m_historyVect.size() > 0)
            {
                m_nHistoryIndex++;

                if(m_nHistoryIndex < m_historyVect.size())
                {
                    setText(getText().substring(0, m_nTextPos) + m_historyVect.elementAt(m_nHistoryIndex).toString());
                }
                else
                {
                	setText(getText().substring(0, m_nTextPos) + "");
                	m_nHistoryIndex = m_historyVect.size();
                }

                setCaretPosition(getText().length());
            }

            e.consume();
        }
        else if(e.getKeyCode() == KeyEvent.VK_UP)
        {
            if(m_historyVect.size() > 0)
            {
                m_nHistoryIndex--;

                if(m_nHistoryIndex >= 0)
                {
                    setText(getText().substring(0, m_nTextPos) + m_historyVect.elementAt(m_nHistoryIndex).toString());
                }
                else
                {
                    m_nHistoryIndex = 0;
                }

                setCaretPosition(getText().length());
            }

            e.consume();
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            if(getSelectionStart() <= m_nPromptPos)
                e.consume();
        }
        else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
        {
            if(getSelectionStart() <= m_nPromptPos)
                e.consume();
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
        }
    }

    public void keyTyped(KeyEvent e) {}

    public void keyReleased(KeyEvent e) {}

    public synchronized void addActionListener(ActionListener l)
    {
        actionListener = AWTEventMulticaster.add(actionListener, l);
    }

    public synchronized void removeActionListener(ActionListener l)
    {
        actionListener = AWTEventMulticaster.remove(actionListener, l);
    }

    protected void processEvent(AWTEvent e)
    {
        if (e instanceof ActionEvent)
        {
            processActionEvent((ActionEvent)e);
            return;
        }
        super.processEvent(e);
    }

    protected void processActionEvent(ActionEvent e)
    {
        if (actionListener != null)
        {
            actionListener.actionPerformed(e);
        }
    }

    public void setPrompt(String strPrompt)
    {
        m_strPrompt = strPrompt;
    }

    public void prompt()
    {
        append("\n" + m_strPrompt);
        updatePrompt();
    }

    public void updatePrompt()
    {
        if(getText().length() > MAX_TEXT_SIZE)
        {
            setText(getText().substring(TRESHOULD));
        }

        m_nTextPos = getText().length();

        if(m_nTextPos  > getSelectionStart())
            setCaretPosition(m_nTextPos);

        m_nPromptPos = getSelectionStart();
    }

    public void setEnableAction(boolean bEnable)
    {
        m_bEnableAction = bEnable;
    }
}
