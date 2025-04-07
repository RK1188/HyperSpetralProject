/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hyperspectral;

import java.awt.Image;
import java.awt.image.PixelGrabber;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author admin
 */
public class ImageSeg 
{
    boolean converged; 
    int width;
    int height;
    int[] image; 
    int k;
    byte[] labels; 
    int[] means; 
	 
    public int steps; 
    ArrayList<int[]> snapshots; 
	
    public ImageSeg(Image im, int k) 
    {
	this.width = im.getWidth(null);
	this.height = im.getHeight(null);
	this.k = k;
	this.labels = new byte[width * height];
	this.means = new int[k];
		
	this.steps = 0;
	this.converged = false;
		
	this.image = new int[width * height];
	this.snapshots = new ArrayList<int[]>(100);
		
	PixelGrabber pg = new PixelGrabber(im, 0, 0, width, height, image, 0, width);
		
	try 
	{
	    pg.grabPixels();
	}
	catch (InterruptedException e)
	{
	    System.err.println("Unable to grab image pixels");
	    return;
	}
    }
	
    public void assignment()
    {
	converged = true; 
	
	for (int pixel=0; pixel<image.length; pixel++)
	{
            byte bestClusterID = 0;
            double bestInterclusterDistance = Double.MAX_VALUE;
            for (byte clusterID=0; clusterID<means.length; clusterID++)
            {
		int rdiff = ((image[pixel] >> 16) & 0xff) - ((means[clusterID] >> 16) & 0xff);
		int gdiff = ((image[pixel] >>  8) & 0xff) - ((means[clusterID] >>  8) & 0xff);
		int bdiff = ((image[pixel]      ) & 0xff) - ((means[clusterID]      ) & 0xff);
			
		double cartesianDistance = Math.sqrt(rdiff*rdiff + gdiff*gdiff + bdiff*bdiff);
		if (cartesianDistance < bestInterclusterDistance)
		{
                    bestInterclusterDistance = cartesianDistance;
                    bestClusterID = clusterID;
		}
            }
			
            if (labels[pixel] != bestClusterID)
		converged = false;
			
            labels[pixel] = bestClusterID;
	}
		
	doSnapshot();
    }

    public void doSnapshot()
    {	
        int[] clusteredImage = new int[width * height];
		
	for (int i=0; i<labels.length; i++)
            clusteredImage[i] = means[labels[i]];
		
	snapshots.add(clusteredImage);
    }
    
    public void execute()
    {	
	for (int i=0; i<k; i++)
            this.means[i] = image[new Random().nextInt(image.length)];

	while (!converged)
	{
            
            assignment();
            update();
	
            steps++;
	}
		
	
    }
	
    public int[] getMeans() 
    {
	return means;
    }
	
    public ArrayList<int[]> getSnapshots() 
    {
	return snapshots;
    }
	
	
    public void update()
    {
	int[][] centerComponents = new int[k][4];
	for (int pixel=0; pixel<image.length; pixel++)
	{
            centerComponents[labels[pixel]][0] += (image[pixel] >> 16) & 0xff; 
            centerComponents[labels[pixel]][1] += (image[pixel] >>  8) & 0xff; 
            centerComponents[labels[pixel]][2] += (image[pixel]      ) & 0xff; 
            centerComponents[labels[pixel]][3]++; 
	}
		
	for (int clusterID=0; clusterID<means.length; clusterID++)
	{
            int ravg = (int)((double)centerComponents[clusterID][0] / centerComponents[clusterID][3]);
            int gavg = (int)((double)centerComponents[clusterID][1] / centerComponents[clusterID][3]);
            int bavg = (int)((double)centerComponents[clusterID][2] / centerComponents[clusterID][3]);
		
            means[clusterID] = 255 << 24 | ravg << 16 | gavg << 8 | bavg; 
	}
    }
}
