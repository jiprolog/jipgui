/**
 * IJIPConsoleView.java
 *
 * @author Created by Omnicore CodeGuide
 */

package com.ugos.jiprolog.igui;
import com.ugos.jiprolog.engine.*;

public interface IJIPConsoleController
{
    public void onQuery(String strQuery);
    
    public void onNewFile();
    
    public void onOpenFile();
    
    public void onExit();
    
    public void onReset();
    
    public void onRedirectTrace();
    
    public void onStop();
    
    public void onDestroy();
    
    public void openFile(String strFileName);
    
    public void start();
    
    public void setSearchPath(String strSearchPath);
    
    public JIPEngine getJIPEngine();
}

