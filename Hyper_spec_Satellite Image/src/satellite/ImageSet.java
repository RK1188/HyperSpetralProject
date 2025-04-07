/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package satellite;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author admin
 */
public class ImageSet 
{
    private ArrayList<ImageInstance> instances;

    public ImageSet() 
    {
        this.instances = new ArrayList<ImageInstance>();
    }

    
    public int getSize() 
    {
        return instances.size();
    }

    
    public void add(ImageInstance inst) 
    {
        instances.add(inst);
    }

    
    public List<ImageInstance> getImages() 
    {
        return instances;
    }
}
