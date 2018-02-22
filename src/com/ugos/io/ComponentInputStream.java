package com.ugos.io;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class ComponentInputStream extends InputStream implements KeyListener
{
    private Component m_component;
    private int m_char;

    public ComponentInputStream(final Component component)
    {
        m_component = component;
        m_component.addKeyListener(this);
    }

    public synchronized void close() throws IOException
    {        
        m_char = -1;
        notify();
        m_component.removeKeyListener(this);
        super.close();
    }

    public synchronized int read() throws IOException
    {
        try
        {
            wait();
        }
        catch(InterruptedException ex)
        {
            return -1;
        }

        if(m_char == 0)
            throw new IOException("CTRL+C typed by the user");

        //System.out.println("char: " + (int)m_char);
        //System.out.println(m_char);
        return m_char;
    }

    public synchronized void keyTyped(final KeyEvent e)
    {
        m_char = e.getKeyChar();

//        if(((e.getModifiers() & e.CTRL_MASK) == e.CTRL_MASK))
//      {
//          System.out.println("e.CTRL_MASK " + m_char);
//          if(m_char == 'c' || m_char == 'C')
//          {
//              m_char = 0;
//              notify();
//          }
        //        }
        if(m_char == 3) // CTRL+C
        {
            m_char = 0;
            notify();
        }
        else if(m_char >= 32) //|| m_char == 13)// || m_char == 10)//(m_char >= 8 && m_char <= 13))
        {
            notify();
        }
        else if(m_char == 13 || m_char == 10)
        {
            m_char = -1;
            notify();
        }

        //Character.isLetterOrDigit(m_char) ||
    }

    public void keyReleased(final KeyEvent e)
    {

    }

    public void keyPressed(final KeyEvent e)
    {

    }
}

