package com.ugos.awt;

import java.awt.*;
import java.awt.event.*;


public class XDialog extends Dialog implements WindowListener
{
    public static final int CHAR_WEIGHT = 7;
    
    public XDialog(Frame parent)
    {
        this(parent, "", false);
    }

    public XDialog(Frame parent, String strCaption)
    {
        this(parent, strCaption, false);
    }
    
    public XDialog(Frame parent, boolean bModal)
    {
        this(parent, "", bModal);
    }
    
    public XDialog(Frame parent, String strCaption, boolean bModal)
    {
        super(parent, strCaption, bModal);
        addWindowListener(this);
    }
    
    public void windowClosing(WindowEvent e)
    {
        onDestroy();
    }

    public void windowActivated(WindowEvent e){};
    public void windowClosed(WindowEvent e){}
    public void windowDeactivated(WindowEvent e){}
    public void windowDeiconified(WindowEvent e){}
    public void windowIconified(WindowEvent e){}
    public void windowOpened(WindowEvent e){}
    
    protected void onDestroy()
    {
        setVisible(false);
        dispose();
    }
}
