package com.ugos.io;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class TextAreaInputStream extends InputStream implements KeyListener
{
    private TextArea m_txtArea;
    private String m_strBuffer = "";
    private int m_nIndex;
    private int m_char;
    private boolean m_bEOF;

    public TextAreaInputStream(final TextArea txtArea)
    {
        m_txtArea = txtArea;
        m_nIndex = txtArea.getCaretPosition();
        m_bEOF = false;
        m_txtArea.addKeyListener(this);
    }

    public void close() throws IOException
    {
        super.close();
        m_txtArea.removeKeyListener(this);
    }

    public synchronized int read() throws IOException
    {
        if(m_bEOF)
        {
            m_bEOF = false;
            return -1;
        }

        if(!(m_nIndex < m_txtArea.getCaretPosition()))
        {
            try
            {
                wait();
            }
            catch(InterruptedException ex)
            {
                return -1;
            }
        }

        if(m_char == 0)
            throw new IOException("CTRL+C typed by the user");

//        .charAt(m_nIndex);
//        m_nIndex++;
//        m_bEOF == m_txtArea.getCaretPosition()
        //System.out.println("char: " + (int)m_char);
        //System.out.println(m_char);
        return m_char;
    }

    public synchronized void keyTyped(final KeyEvent e)
    {
        m_char = e.getKeyChar();

        if(m_char == 3) // CTRL+C
        {
            m_char = 0;
            notify();
        }
        else if(m_char == 13 || m_char == 10)
        {
            m_char = -1;
            m_strBuffer = m_txtArea.getText();
            notify();
        }
//        else if(m_char >= 32) //|| m_char == 13)// || m_char == 10)//(m_char >= 8 && m_char <= 13))
//        {
//            notify();
//        }

        //Character.isLetterOrDigit(m_char) ||
    }

    public void keyReleased(final KeyEvent e)
    {

    }

    public void keyPressed(final KeyEvent e)
    {

    }
}

