/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hyperspectral;
import java.awt.Color;
import java.awt.image.ImageProducer;
import java.awt.image.MemoryImageSource;
import java.util.Set;
import java.util.TreeSet;
/**
 *
 * @author admin
 */
public class FeatureMatrix 
{
    int[][][] data;
    int width;
    int height;
    int depth;
    byte[][] segment;

    public FeatureMatrix(int width, int height, int depth) 
    {
	super();
	this.data = new int[height][width][depth];
	this.width = width;
	this.height = height;
	this.depth = depth;
	this.segment = new byte[height][width];
	for (int i = 0; i < height; i++) 
        {
            for (int j = 0; j < width; j++) 
            {
		this.segment[i][j] = -1;
            }
	}
    }

    public int[][][] getData() 
    {
	return data;
    }

    public int getWidth() 
    {
	return width;
    }

    public int getHeight() 
    {
	return height;
    }

    public int getDepth() 
    {
	return depth;
    }

    public byte[][] getSegment() 
    {
	return segment;
    }

    public ImageProducer createSegmentedImage() 
    {
	int[] segmentedImage = new int[height * width];
	int segmentCount = segmentCount();

        for (int i = 0; i < height; i++) 
        {
            for (int j = 0; j < width; j++) 
            {
		if (this.segment[i][j] != -1) 
                {
                    segmentedImage[i * width + j] = segmentToColor(
                    this.segment[i][j], segmentCount).getRGB();
		}
            }
	}
	return new MemoryImageSource(this.width, this.height, segmentedImage,0, this.width);
    }

    public ImageMatrix getImageMatrix() 
    {
	ImageMatrix im =  new ImageMatrix(width, height);
	int[][] pixels = im.getPixels();
	int segmentCount = segmentCount();
	for (int i = 0; i < height; i++) 
        {
            for (int j = 0; j < width; j++) 
            {
		Color c = segmentToColor(segment[i][j], segmentCount);
                pixels[i][j] = c.getRGB();
            }
	}
		
	return im;
    }

    public int segmentCount() 
    {
	Set<Integer> set = new TreeSet<Integer>();
	for (int i = 0; i < height; i++) 
        {
            for (int j = 0; j < width; j++) 
            {
		set.add(new Integer(segment[i][j]));
            }
	}

	return set.size();
    }

    public Color segmentToColor(byte segment, int count) 
    {
	return Color.getHSBColor(segment / (float) count, 1.0f, 1.0f);
    }
}
