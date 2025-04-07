/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hyperspectral;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.Insets;

import java.util.HashMap;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 *
 * @author admin
 */
public class ImgOptionsPanel extends JPanel 
{

    public static enum SegmentationMethod 
    {
	K_MEANS("K-means");

        public String name;

	SegmentationMethod(String s) 
        {
            name = s;
	}

	public String toString() 
        {
            return name;
	}
    };

	
    public JComboBox combo;

    public JPanel panel;

    public SegmentationMethod selection;

    public HashMap<String, String> parameters;

    public ImgOptionsPanel() 
    {
	parameters = new HashMap<String, String>();
	setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	c.anchor = GridBagConstraints.NORTH;
	c.weighty = 0;
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridwidth = GridBagConstraints.REMAINDER;
	c.gridx = 0;
	c.gridy = 0;
	add(new JLabel("Method:"), c);
	c.gridy = 1;
	add(getComboBox(), c);
	c.gridy = 2;
	panel = new JPanel();
	panel.setPreferredSize(new Dimension(250, 150));
	panel.setLayout(new GridBagLayout());

	add(panel, c);
	showOptions();
    }

    public JComboBox getComboBox() 
    {
	if (combo == null) 
        {
            SegmentationMethod[] ms  = SegmentationMethod.values();
            String[] names = new String[ms.length];
            for (int i = 0; i < ms.length; i++) 
            {
		names[i] = ms[i].toString();
            }
            combo = new JComboBox(names);
            selection = ms[0];
            combo.addActionListener(new ActionListener() 
            {
                public void actionPerformed(ActionEvent e) 
                {
                    JComboBox combo = (JComboBox) e.getSource();
                    int i = combo.getSelectedIndex();
                    SegmentationMethod newSelection = SegmentationMethod.values()[i];
                    if (selection != newSelection) 
                    {
                        selection = newSelection;
                        showOptions();
                    }
                }

            });
	}
	return combo;
    }

    public clsAlgorithm getSelectedSegmentationMethod() 
    {
	clsAlgorithm sa = null;
	switch (selection) 
        {
            case K_MEANS:
                sa = new KMeans();
		break;
		
	}
	return sa;
    }
	
	public HashMap<String, String> getParameters() 
        {
            return parameters;
	}

	public void showOptions() 
        {
            if (selection == null) 
            {
		return;
            }
	
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(16, 4, 2, 4);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.gridx = 0;
		
            panel.setVisible(false);		
            panel.removeAll();
            parameters.clear();
		
            switch (selection) 
            {
	
		case K_MEANS:
		c.gridy = 0;
		parameters.put("clustersCount","10");
                c.insets = new Insets(2, 4, 2, 4);
		c.gridy = 1;
		break;					
            }	
			
	}	    
}
