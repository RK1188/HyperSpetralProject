/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package satellite;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author admin
 */
public class ImageInstance 
{
    
    private BufferedImage image;
    private String label;
    private int width, height;
    private String fname;
    private int[][] red_channel, green_channel, blue_channel, gray_image;

    
    public ImageInstance(BufferedImage image, String label,String fname) 
    {
        this.image = image;
        this.label = label;
        this.fname=fname;
        width  = image.getWidth();
        height = image.getHeight();

        gray_image = null;
        
        red_channel   = new int[height][width];
        green_channel = new int[height][width];
        blue_channel  = new int[height][width];

        for(int row = 0; row < height; ++row) 
        {
            for(int col = 0; col < width; ++col) 
            {
                Color c = new Color(image.getRGB(col, row));
                red_channel[  row][col] = c.getRed();
                green_channel[row][col] = c.getGreen();
                blue_channel[ row][col] = c.getBlue();
            }
        }
    }

    
    public int[][] getRedChannel() 
    {
        return red_channel;
    }

    
    public int[][] getGreenChannel() 
    {
        return green_channel;
    }

    
    public int[][] getBlueChannel() 
    {
        return blue_channel;
    }

    
    public int[][] getGrayImage() 
    {    
        if(gray_image != null) 
        {
            return gray_image;
        }

        gray_image = new int[height][width];
     
        for(int row = 0; row < height; ++row)         
        {
            for(int col = 0; col < width; ++col) 
            {
                int rgb = image.getRGB(col, row) & 0xFF;
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >>  8) & 0xFF;
                int b = (rgb        & 0xFF);
                gray_image[row][col] = (r + g + b) / 3;
            }
        }
        return gray_image;
    }



    public int getWidth() 
    {
        return width;
    }

    public int getHeight() 
    {
        return height;
    }

    public String getLabel() 
    {
        return label;
    }

    public String getName()
    {
        return fname;
    }
    
    public void display2D(int[][] img) 
    {
        BufferedImage bufferedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for(int row = 0; row < height; ++row) 
        {
            for(int col = 0; col < width; ++col) 
            {
                int c = img[row][col] << 16 | img[row][col] << 8 | img[row][col];
                bufferedImg.setRGB(col, row, c);
            }
        }        
    }

    
   
}
