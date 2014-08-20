package com.ugos.awt;

import java.awt.*;
import java.awt.event.*;


public class InputDialog extends XDialog implements ActionListener
{
    public static final int ID_OK = 1;
    public static final int ID_CANCEL = 2;
    
    //private static final int WIDTH  = 250;
    private static final int HEIGHT = 120;
    
    private int       m_nStatus;
    private TextField m_txtPrompt;
    private Label     m_lbMsg;
    private Button    m_btnOk;
    private Button    m_btnCancel;
    private String    m_strText;

    public InputDialog(Frame parent, String strTitle, String strMsg)
    {
        super(parent, strTitle, true);
        
        

//      int nChar = (strTitle.length() > strMsg.length()) ? strTitle.length() : strMsg.length();
//      int nWidth = nChar * XDialog.CHAR_WEIGHT;
//
//      // Centra la dialog
//      Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
//
//      int nTop  = (d.height - HEIGHT) / 2;
//      int nLeft = (d.width  - nWidth)  / 2;
//      //int nLeft = (d.width  - WIDTH)  / 2;
//
//      setBounds(nLeft, nTop, nWidth, HEIGHT);
        //setBounds(nLeft, nTop, WIDTH, HEIGHT);

        setResizable(false);
            
        m_nStatus = ID_CANCEL;
        
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        setLayout(gbl);
        
        m_txtPrompt = new TextField(20);
        m_btnOk     = new Button("  Ok  ");
        m_btnCancel = new Button("Cancel");
        m_lbMsg     = new Label(strMsg, Label.CENTER);
        
        m_txtPrompt.addActionListener(this);
        m_btnOk.addActionListener(this);
        m_btnCancel.addActionListener(this);
        
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx   = 2;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.insets    = new Insets(0, 5, 0, 5);
        gbl.setConstraints(m_lbMsg, gbc);
        add(m_lbMsg);

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx   = 2;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.insets    = new Insets(3, 5, 5, 5);
        gbl.setConstraints(m_txtPrompt, gbc);
        add(m_txtPrompt);
                
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.weightx   = 1;
        gbc.insets    = new Insets(5, 20, 5, 20);
        gbc.anchor    = GridBagConstraints.CENTER;
        gbc.fill      = GridBagConstraints.BOTH;
        gbl.setConstraints(m_btnOk, gbc);
        add(m_btnOk);
        
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbl.setConstraints(m_btnCancel, gbc);
        add(m_btnCancel);
        
        setTextFont(new Font("Arial", Font.PLAIN, 11));
    }
    
    public void setTextFont(Font f)
    {
        m_txtPrompt.setFont(f);
        m_btnCancel.setFont(f);
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

    public void actionPerformed(ActionEvent evt)
    {
        if(evt.getSource() == m_btnOk)
        {
            onOk();
        }
        else if(evt.getSource() == m_btnCancel)
        {
            onCancel();
        }
        else if (evt.getSource() == m_txtPrompt)
        {
            onEnter();
        }
    }
    
    private void onOk()
    {
        m_strText = m_txtPrompt.getText();
        m_nStatus = ID_OK;
        onDestroy();
    }
    
    private void onCancel()
    {
        m_nStatus = ID_CANCEL;
        onDestroy();
    }
    
    private void onEnter()
    {
        onOk();
    }
    
    public int getButtonPressed()
    {
        return m_nStatus;
    }
    
    public String getPrompt()
    {
        return m_strText;
    }
}
