package com.ugos.awt;

import java.awt.*;

public class ApplicationFrame extends XFrame
{
    public ApplicationFrame()
    {
        super();
    }

    public ApplicationFrame(String strCaption)
    {
        super(strCaption);
    }
  
    protected void onDestroy()
    {
    	super.onDestroy();
    	    	
        System.exit(0);
    }

}
