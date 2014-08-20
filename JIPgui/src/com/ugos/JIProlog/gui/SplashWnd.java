/**
 * SplashWnd.java
 *
 * @author Created by Omnicore CodeGuide
 */

package com.ugos.JIProlog.gui;

import com.ugos.JIProlog.engine.*;
import java.awt.*;
import java.awt.event.*;

public class SplashWnd extends Window
{
    static Dimension s_screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    /**
     * Initializes the frame.
     */
    public SplashWnd(Frame mainFrame)
    {
        super(mainFrame);
                
        Image jipIco = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/com/ugos/JIProlog/gui/resources/icoJip.gif"));
        
//        if(s_screenSize.width >= 640)
//        {
            jipIco = jipIco.getScaledInstance(45, 40, Image.SCALE_SMOOTH);
//        }
//        else
//        {
//            //PDA
//            jipIco = jipIco.getScaledInstance(s_screenSize.height / 3, s_screenSize.height / 3, Image.SCALE_SMOOTH);
//        }
        
        
        ImageCanvas img =
            new ImageCanvas(jipIco);
        
        setLayout(new GridLayout(2,1));
        add(img);
        Panel pan = new Panel();
        pan.setLayout(new GridLayout(4,1));
        Label lab = new Label("JIProlog v" + JIPEngine.getVersion(), Label.CENTER);
        lab.setFont(new Font("Arial", Font.PLAIN, 11));
        lab.setForeground(Color.white);
        pan.add(lab);
        lab = new Label("Copyright (c) 1999-2007 by Ugo Chirico", Label.CENTER);
        lab.setFont(new Font("Arial", Font.PLAIN, 9));
        lab.setForeground(Color.white);
        pan.add(lab);
        lab = new Label("All rights reserved", Label.CENTER);
        lab.setFont(new Font("Arial", Font.PLAIN, 9));
        lab.setForeground(Color.white);
        pan.add(lab);
        lab = new Label("http://www.ugochirico.com", Label.CENTER);
        lab.setFont(new Font("Arial", Font.PLAIN, 11));
        lab.setForeground(Color.white);
        pan.add(lab);
        
        add(pan);
        setBackground(JIPConsoleView.BKGCOLOR);
    }
    
    /**
     * Shows the frame.
     */
    public void show()
    {
        Dimension size;
        
        if(s_screenSize.width >= 640)
        {
            size = new Dimension(210,130);
            setBounds(
                (s_screenSize.width - size.width) / 2,
                (s_screenSize.height - size.height) / 2,
                size.width,
                size.height);
        }
        else
        {
            //PDA or similar
            size = new Dimension(s_screenSize.width, s_screenSize.width);
            setBounds(0,0, size.width, size.height);
        }

        //Dimension size = getPreferredSize();
        //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        
        //setVisible(true);
        super.show();
    }
    
//  public void paint(Graphics g)
//  {
//      //super.paint(g);
//
//      Dimension dim = getSize();
//
//    System.out.println(dim.height);
//    System.out.println(dim.width);
//
//      g.setColor(Color.white);
//
//      g.drawRect(0, 0, dim.width, dim.height);
//
//      g.drawRect(4, 4, dim.width - 4, dim.height - 4);
//  }
}

