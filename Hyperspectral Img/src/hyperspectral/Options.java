/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hyperspectral;
import java.awt.GridLayout;
import java.util.HashMap;
import javax.swing.JPanel;

/**
 *
 * @author admin
 */
public class Options extends JPanel {
	
    public FeatureOptionsPanel panel1;

    public ImgOptionsPanel panel2;

    public Options() 
    {
	GridLayout layout = new GridLayout(1, 2);
	layout.setHgap(8);
	setLayout(layout);
	panel1 = new FeatureOptionsPanel();
	add(panel1);
	panel2 = new ImgOptionsPanel();
	add(panel2);
    }
	
    public HashMap<String, String> getSegmentationParameters() 
    {
	return panel2.getParameters();
    }

    public ImageConverter getSelectedFeature(ImageMatrix matrix) 
    {
	return panel1.getSelectedFeature(matrix);
    }
    public clsAlgorithm getSelectedSegmentationMethod() 
    {
        return panel2.getSelectedSegmentationMethod();
    }   
}
