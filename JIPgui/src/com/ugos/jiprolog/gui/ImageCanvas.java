/**
 * ImageCanvas.java
 *
 * @author Created by Omnicore CodeGuide
 */

package com.ugos.jiprolog.gui;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.InputStream;
import java.net.URL;

public class ImageCanvas extends Canvas
{
    Image m_img;
    
    public ImageCanvas(Image img)
    {
        m_img = img;
    }
    
    public ImageCanvas(URL urlImage)
    {
        super();
        if(urlImage != null)
            m_img = loadImage(urlImage);
        else
            m_img = null;
            
        if(m_img == null)
            System.out.println("img = null");

    }

//    public ImageCanvas(InputStream ins)
//    {
//        super();
//        
//        m_img = 
//    }

    private static final Image loadImage(URL urlImage)
    {
        return Toolkit.getDefaultToolkit().getImage(urlImage);
    }
    
    public void paint(Graphics g)
    {
        super.paint(g);
        drawImage(g);
    }

    protected void drawImage(Graphics g)
    {
        Dimension dim = getSize();
        //Rectangle rect = new Rectangle(getSize());
                 
        int nOY = dim.height / 2;
        int nOX = dim.width / 2;
        int nImgWidth = m_img.getWidth(this);
        int nImgHeight = m_img.getHeight(this);
        
        int nX = (nOX) - (nImgWidth / 2);
        int nY = (nOY) - (nImgHeight / 2);
        
        int nX1 = (nOX) - ((nImgWidth + 12) / 2);
        int nY1 = (nOY) - ((nImgHeight + 12) / 2);
        
        int nX2 = (nOX) - ((nImgWidth + 14) / 2);
        int nY2 = (nOY) - ((nImgHeight + 14) / 2);
        
        g.setColor(Color.white);
        g.fillOval(nX1, nY1, nImgWidth + 12, nImgHeight + 12);
        g.setColor(Color.black);
        g.drawOval(nX2, nY2, nImgWidth + 14, nImgHeight + 14);
        g.drawImage(m_img, nX, nY, this);
    }
    
    public void setImage(Image img)
    {
        m_img = img;
        repaint();
    }

	/* (non-Javadoc)
	 * @see java.awt.Component#getPreferredSize()
	 */
	@Override
	public Dimension getPreferredSize() 
	{
		ImageObserver obs = new ImageObserver() {
			
			public boolean imageUpdate(Image img, int infoflags, int x, int y,
					int width, int height) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		
		Dimension d = new Dimension(m_img.getHeight(obs), m_img.getWidth(obs));		
		return d;
	}
}

