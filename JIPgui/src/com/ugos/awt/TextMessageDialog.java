package com.ugos.awt;

import java.awt.event.*;
import java.awt.*;

public class TextMessageDialog extends XDialog
{
    private Button    m_btnOk;
    private TextArea  m_TextMsg;
    
    private int HEIGHT = 250;
    private int WIDTH = 350;
    
    
//  public static void main(String[] args)
//  {
//      Dialogs.showTextMessageDlg(new Frame(), "prova", "messaggio");
//  }

    public TextMessageDialog(Frame parent, String strTitle, String strMessage)
    {
        super(parent, strTitle);
        
        try
        {
            setModal(true);
        }
        catch(Throwable ex)
        {
        }
                
        // Crea TextArea da inserire nella finestra
        m_TextMsg = new TextArea(strMessage, 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
        m_btnOk   = new Button("  Ok  ");
        
        m_TextMsg.setEditable(false);
                
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        setLayout(gbl);
                
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx   = 2;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.insets    = new Insets(5, 5, 5, 5);
        gbl.setConstraints(m_TextMsg, gbc);
        add(m_TextMsg);
                
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx   = 1;
        gbc.anchor    = GridBagConstraints.CENTER;
        gbc.fill      = GridBagConstraints.NONE;
        gbc.ipadx     = 20;
        gbl.setConstraints(m_btnOk, gbc);
        add(m_btnOk);
                                
//        setSize(350, 250);
//
//        Dimension d = getSize();
//        // Centra la dialog
//        Dimension ds = Toolkit.getDefaultToolkit().getScreenSize();
//
//        int nTop  = (ds.height - d.height) / 2;
//        int nLeft = (ds.width  - d.width)  / 2;
//
        //        reshape(nLeft, nTop, d.width, d.height);
                
        setTextFont(new Font("Arial", Font.PLAIN, 11));
        setResizable(true);
    }
    
    public void setTextFont(Font f)
    {
        m_TextMsg.setFont(f);
        m_btnOk.setFont(f);
        
        int nChar = getTitle().length();
        //int nWidth = nChar * XDialog.CHAR_WEIGHT;
        int nWidth = nChar * (Toolkit.getDefaultToolkit().getFontMetrics(f).charWidth('A'));
        nWidth = nWidth  > WIDTH ? nWidth : WIDTH;
        
        // Centra la dialog
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        
        int nTop  = (d.height - HEIGHT) / 2;
        int nLeft = (d.width  - nWidth)  / 2;
        //int nLeft = (d.width  - WIDTH)  / 2;
        
        setBounds(nLeft, nTop, nWidth, HEIGHT);
    }

    public boolean action(Event evt, Object arg)
    {
        if(arg.equals(m_btnOk.getLabel()))
        {
            onOk();
            return true;
        }
        
        return super.action(evt, arg);
    }
    
    protected void onOk()
    {
        onDestroy();
    }
    
    public void windowActivated(WindowEvent e){m_btnOk.requestFocus();}
}
