package com.ugos.awt;

import java.awt.*;

public class MessageDialog extends XDialog
{
    private Button    m_btnOk;
    private Label     m_lbMsg;
    
    private static final int HEIGHT = 120;
    
    public MessageDialog(Frame parent, String strTitle, String strMessage)
    {
        super(parent, strTitle, true);
    
       
        
//      int nChar = (strTitle.length() > strMessage.length()) ? strTitle.length() : strMessage.length();
//      int nWidth = nChar * XDialog.CHAR_WEIGHT;
        
        // Crea Label da inserire nella finestra
        m_lbMsg = new Label(strMessage, Label.CENTER);
        
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        setLayout(gbl);
        
        m_btnOk     = new Button("  Ok  ");
                
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx   = 2;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.insets    = new Insets(5, 5, 5, 5);
        gbl.setConstraints(m_lbMsg, gbc);
        add(m_lbMsg);
                
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx   = 1;
        gbc.anchor    = GridBagConstraints.CENTER;
        gbc.fill      = GridBagConstraints.NONE;
        gbc.ipadx     = 20;
        gbl.setConstraints(m_btnOk, gbc);
        add(m_btnOk);
                        
    
    /*
          //Calcola la dimensioni della label
        Dimension dimLabel = getPreferredSize();
        int nHeight = dimLabel.height + 10;
        int nWidth = dimLabel.width + 10;
        
        System.out.println(nHeight);
        System.out.println(nWidth);
     */
//        setSize(nWidth, 100);
//
//         Dimension d = getSize();
//        // Centra la dialog
//        Dimension ds = Toolkit.getDefaultToolkit().getScreenSize();
//
//        int nTop  = (ds.height - d.height) / 2;
//        int nLeft = (ds.width  - d.width)  / 2;
//
//        reshape(nLeft, nTop, d.width, d.height);
        setResizable(false);
        
        setTextFont(new Font("Arial", Font.PLAIN, 11));
    }

    public void setTextFont(Font f)
    {
        m_btnOk.setFont(f);
        m_lbMsg.setFont(f);
        
        int nChar = (getTitle().length() > m_lbMsg.getText().length()) ? getTitle().length() : m_lbMsg.getText().length();
        //int nWidth = nChar * XDialog.CHAR_WEIGHT;
        int nWidth = nChar * (Toolkit.getDefaultToolkit().getFontMetrics(f).charWidth('A'));
        
        // Centra la dialog
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        
        int nTop  = (d.height - HEIGHT) / 2;
        int nLeft = (d.width  - nWidth)  / 2;
        //int nLeft = (d.width  - WIDTH)  / 2;
        
        setBounds(nLeft, nTop, nWidth, HEIGHT);
    }

/*
    public void paint(Graphics g)
    {
        super.paint(g);
        
        // Ridimensiona la finestra
        Dimension dlb = m_lbMsg.getSize();
        System.out.println(dlb);

        Dimension dbtn = m_btnOk.getSize();
        System.out.println(dbtn);
        
        Dimension total = new Dimension(dlb.width, dlb.height + dbtn.height + 60);
        System.out.println(total);
        
        setSize(total);
    }
*/
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
    
    public void setMessage(String strMessage)
    {
        m_lbMsg.setText(strMessage);
        // Ridimensiona la finestra
        Dimension dlb = m_lbMsg.getSize();
        Dimension dbtn = m_btnOk.getSize();
        setSize(new Dimension(dlb.width, dlb.height + dbtn.height + 20));
    }
        
}
