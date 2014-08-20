package com.ugos.awt;

import java.awt.*;
import java.awt.event.*;


public class XFrame extends Frame implements WindowListener
{
	public XFrame()
	{
		super();
        addWindowListener(this);
	}
	
	public XFrame(String strCaption)
	{
		super(strCaption);
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

/*
    public boolean handleEvent(Event evt)
    {
   		switch(evt.id)
        {
        case Event.WINDOW_DESTROY:
            return onDestroy(evt);
        case Event.WINDOW_EXPOSE:
            return onExpose(evt);
        case Event.WINDOW_ICONIFY:
            return onIconify(evt);
        case Event.WINDOW_DEICONIFY:
            return onDeiconify(evt);
        case Event.WINDOW_MOVED:
            return onMoved(evt);
        }

        return super.handleEvent(evt);
    }


    protected boolean onExpose(Event evt)
    {
        return super.handleEvent(evt);
    }

    protected boolean onIconify(Event evt)
    {
        return super.handleEvent(evt);
    }

    protected boolean onDeiconify(Event evt)
    {
        return super.handleEvent(evt);
    }

    protected boolean onMoved(Event evt)
    {
        return super.handleEvent(evt);
    }
*/
}
