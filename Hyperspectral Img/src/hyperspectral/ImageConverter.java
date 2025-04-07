/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hyperspectral;
import java.util.HashMap;
/**
 *
 * @author admin
 */
public abstract class ImageConverter
{
    protected ImageMatrix image;
    protected HashMap<String, String> params;
	
    public ImageConverter(ImageMatrix image, HashMap<String, String> params) 
    {
	this.image = image;
	this.params = params;
    }
    public FeatureMatrix createFeatureMatrix() 
    {
	FeatureMatrix imageMatrix = new FeatureMatrix(image.getWidth(), image.getHeight(), this.getDepth());
		
	for (int i=0; i<image.getHeight(); i++) 
        {
            for (int j=0; j<image.getWidth(); j++) 
            {
		createFeature(i, j, imageMatrix.getData()[i][j]);
            }
	}
		
	return imageMatrix;
    }
	
    protected abstract int getDepth();
    
    protected abstract void createFeature(int i, int j, int[] feature);
}
