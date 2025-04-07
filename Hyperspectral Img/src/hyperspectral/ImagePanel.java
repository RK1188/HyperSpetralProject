/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hyperspectral;
import java.util.List;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

import java.io.File;
import javax.imageio.ImageIO;
        
/**
 *
 * @author admin
 */
public class ImagePanel extends javax.swing.JPanel 
{

    public int currentImage;
    public List<int[]> images;
    public BufferedImage onScreenImage;

    public int width;
    public int height;

    public ImagePanel() 
    {
	super();
	initGUI();
    }

    public BufferedImage getCurrentImage() 
    {
	return onScreenImage;
    }

    public void initGUI() 
    {
	try 
        {
            setPreferredSize(new Dimension(400, 300));
	} 
        catch (Exception e) 
        {
            e.printStackTrace();
	}
    }

    public void paintComponent(Graphics g) 
    {
	super.paintComponent(g);
	g.drawImage(onScreenImage, 0, 0, this);
	if (onScreenImage != null)
            g.drawImage(onScreenImage, 0, 0, this);
    }

    public void setSingleImage(BufferedImage image) 
    {
	this.onScreenImage = image;
	if (image == null) 
        {
            this.images = null;
            this.width = 0;
            this.height = 0;
            this.currentImage = 0;
	}
        else 
        {
            this.width = onScreenImage.getWidth(this);
            this.height = onScreenImage.getHeight(this);
            setPreferredSize(new Dimension(this.width, this.height));
        }
    }

    public void setImages(List<int[]> images, int width, int height) 
    {
        this.images = images;
	if (this.onScreenImage != null)
            this.onScreenImage.flush();

        this.width = width;
	this.height = height;

	if (images != null) 
        {
            this.onScreenImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            showImage(0);
	}

	setPreferredSize(new Dimension(width, height));
	
    }

    public void showImage(int i) 
    {
	try
	{
            if (images != null) 
            {
		currentImage = Math.min(Math.max(0, i), images.size() - 1);

                int[] pixels = images.get(currentImage);

                DataBuffer buffer = new DataBufferInt(pixels, pixels.length);
                int[] bands = { 0xFF0000, 0xFF00, 0xFF, 0xFF000000 };
                WritableRaster raster = Raster.createPackedRaster(buffer, width, height, width, bands, null);
                onScreenImage.getRaster().setDataElements(0, 0, raster);
                ImageIO.write(onScreenImage,"jpg",new File("band\\"+i+".jpg"));
                repaint();
            }
	}
	catch(Exception e)
	{
            e.printStackTrace();
	}
    }
}
