/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package satellite;

import java.util.Vector;
import java.util.ArrayList;
/**
 *
 * @author admin
 */
public class ConvNet 
{
    private boolean debugCNN = true;
    private int countClasses;

    private Convolution conv1;
    private Pooling maxPool1;
    private Convolution conv2;
    private Pooling maxPool2;
    private Convolution conv3;
    private Pooling maxPool3;
    private FlatLayer flat;
    private OutputLayer out;
    private int bestTune;


   public ConvNet( Vector<Vector<Double>> inputFeatureVectors , int hyperparameters, boolean debugSwitch)
   {

       debugCNN     = debugSwitch;

       conv1        = new Convolution(inputFeatureVectors, hyperparameters, debugCNN);    
       maxPool1     = new Pooling(conv1, debugCNN);                                       
       conv2        = new Convolution(maxPool1, hyperparameters,  debugCNN);              
       maxPool2     = new Pooling(conv2, debugCNN);                                              
       flat         = new FlatLayer(maxPool2, debugCNN);                                  
       out          = new OutputLayer(flat, hyperparameters, debugCNN);                   

   }


   public int trainCNN( Vector<Vector<Double>> trainFeatureVectors) 
   {       
       out.resetCountCorrect();
       int errorCount = 0;
	   
       
       
       for (int trainingIpNum = 0; trainingIpNum < trainFeatureVectors.size(); trainingIpNum++) 
       {

           Vector<Double> trainFeatureVector = trainFeatureVectors.get(trainingIpNum);
       
           conv1.train(trainFeatureVector);
           
           ArrayList<FeatureMap> mp=conv1.get_fMaps();          
           
           maxPool1.train(conv1);
           conv2.train(maxPool1);
           maxPool2.train(conv2);
           
           flat.trainwithDropOut(maxPool2);
           out.train(flat);
       }
       System.out.println(out.getCountCorrect());
       return errorCount;
   }

    
}
