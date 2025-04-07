/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hyperspectral;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
/**
 *
 * @author admin
 */
public class FeatureOptionsPanel extends JPanel 
{

    public static enum Feature {COLOR};
    
    public JComboBox combo;

    public JPanel panel;

    public JPanel optionalPanel;

    public Feature selection;

    public HashMap<String, String> parameters;

    public FeatureOptionsPanel() 
    {
	parameters = new HashMap<String, String>();
	setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridwidth = GridBagConstraints.REMAINDER;
	c.gridx = 0;
	c.gridy = 0;
	add(new JLabel("Feature:"), c);
	c.gridy = 1;
	add(getComboBox(), c);
	c.gridy = 2;
	panel = createOptionsPanel();
	add(panel, c);
	selection = Feature.COLOR;
	
    }

    public JComboBox getComboBox() 
    {
	if (combo == null) 
        {
            String[] options = new String[] { "Por color"};
            combo = new JComboBox(options);
            selection = Feature.COLOR;
            combo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) 
            {
		JComboBox combo = (JComboBox) e.getSource();
		int i = combo.getSelectedIndex();
		selection = Feature.values()[i];
            }
            });
	}
	return combo;
    }

    public JPanel createOptionsPanel() 
    {
	JPanel panel = new JPanel();
	panel.setPreferredSize(new Dimension(250, 150));
	panel.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	c.insets = new Insets(8, 0, 2, 0);
	c.anchor = GridBagConstraints.NORTH;
	c.weighty = 0;
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridwidth = GridBagConstraints.REMAINDER;
	c.gridx = 0;
	c.gridy = 0;
	panel.add(new JLabel("Perfil:"), c);
	c.insets = new Insets(2, 0, 2, 0);
	c.weighty = 1;
	c.gridy = 1;
	panel.add(createRadioButtonsPanel(), c);
	c.weighty = 0;
	c.insets = new Insets(8, 0, 2, 0);
	c.gridy = 2;
	
	return panel;
    }

    public JPanel createRadioButtonsPanel() 
    {
	ActionListener al = new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
		parameters.put("colorSpace", e.getActionCommand());
            }
	};

	JRadioButton rgb = new JRadioButton("RGB", true);
	rgb.setMnemonic(KeyEvent.VK_R);
	rgb.setActionCommand("rgb");
	rgb.addActionListener(al);
	parameters.put("colorSpace", "rgb");

	ButtonGroup group = new ButtonGroup();
	group.add(rgb);
	
        JPanel p = new JPanel();
	p.add(rgb);
	return p;
    }

	
    public ImageConverter getSelectedFeature(ImageMatrix matrix) 
    {
	ImageConverter ic = null;
    	switch (selection) 
        {
            case COLOR:	ic = new ColorImageConverter(matrix, parameters);
            break;
	
	}
	return ic;
    }

    public void addSpinner(String name, String key, SpinnerModel sm,GridBagConstraints c) 
    {
	JLabel label = new JLabel(name);
	c.gridx = 0;
	c.gridwidth = 2;
	optionalPanel.add(label, c);
	JSpinner spinner = new JSpinner(sm);
	final String k = key;
	final JSpinner s = spinner;
	spinner.addChangeListener(new ChangeListener() 
        {
            public void stateChanged(ChangeEvent e) 
            {
		parameters.put(k, s.getValue().toString());
            }
	});
	c.gridx = 2;
	c.gridwidth = GridBagConstraints.REMAINDER;
	optionalPanel.add(spinner, c);
	parameters.put(key, sm.getValue().toString());
    }
    
}
