package com.ugos.awt;

import java.awt.*;

public class Dialogs
{
    private Button    m_btnOk;
    private Label     m_lbMsg;
    static Font       s_font = null;
    
    public static void setFont(Font f)
    {
        s_font = f;
    }
    
    public static void setFont(String strFontName, int nSize)
    {
        s_font = new Font(strFontName, nSize, Font.PLAIN);
    }
        
    public static void showMessageDlg(Frame parent, String strTitle, String strMessage)
    {
        MessageDialog msgDlg = new MessageDialog(parent, strTitle, strMessage);
        if(s_font != null)
            msgDlg.setTextFont(s_font);
        msgDlg.show();
    }
    
    public static boolean showYesNoDlg(Frame parent, String strTitle, String strMessage)
    {
        YesNoDialog dlg = new YesNoDialog(parent, strTitle, strMessage);
        if(s_font != null)
            dlg.setTextFont(s_font);
        dlg.show();
        return dlg.getButtonPressed() == YesNoDialog.ID_OK;
    }

    public static String showInputDlg(Frame parent, String strTitle, String strMessage)
    {
        InputDialog inputDlg = new InputDialog(parent, strTitle, strMessage);
        if(s_font != null)
            inputDlg.setTextFont(s_font);
        inputDlg.show();
        if(inputDlg.getButtonPressed() == InputDialog.ID_OK)
            return inputDlg.getPrompt();
        else
            return null;
    }
    
    public static String showPromptDlg(Frame parent, String strTitle)
    {
        PromptDialog promptDlg = new PromptDialog(parent, strTitle);
        if(s_font != null)
            promptDlg.setTextFont(s_font);
        promptDlg.show();
        if(promptDlg.getButtonPressed() == PromptDialog.ID_OK)
            return promptDlg.getPrompt();
        else
            return null;
    }
    
    public static void showTextMessageDlg(Frame parent, String strTitle, String strMessage)
    {
        TextMessageDialog textDlg = new TextMessageDialog(parent, strTitle, strMessage);
        if(s_font != null)
            textDlg.setTextFont(s_font);
        textDlg.show();
    }
}
