/**
 * IJIPConsoleView.java
 *
 * @author Created by Omnicore CodeGuide
 */

package com.ugos.jiprolog.igui;
import java.io.*;
import java.awt.*;

public interface IJIPConsoleView
{
    public void enableNew(boolean bEnable);
    
    public void enableOpen(boolean bEnable);
    
    public void enableReset(boolean bEnable);
    
    public void enableStop(boolean bEnable);
    
    public void editable(boolean bEditable);
    
    public void prompt();
    
    public void recordHistory(boolean bRecord);
    
    public void updatePrompt();
    
    public void requestFocus();
    
    public void startUserInput();
    
    public void stopUserInput();
    
    public void waitCursor(boolean bWait);
    
    public PrintStream getPrintStream(String encoding) throws UnsupportedEncodingException;
    
    public InputStream getInputStream();
    
    public void printHeader();
    
    public Frame getMainFrame();
    
    public void setController(IJIPConsoleController controller);
    
    public void setVisible(boolean bVisible);
    
    public void onDestroy();
}

