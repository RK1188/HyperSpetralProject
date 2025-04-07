/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hyperspectral;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
/**
 *
 * @author admin
 */
public class KMeans extends clsAlgorithm 
{
    public Cluster[] clusters;
    public FeatureMatrix image;
    public ImgObserver observer;
    public boolean useAllClusters = false;
    public int clustersCount = 10;

	
    public void process(FeatureMatrix image, ImgObserver observer,HashMap<String, String> params) 
    {
	this.image = image;
	this.observer = observer;
	String s = params.get("useAllClusters");
	if (s != null) 
        {
            useAllClusters = (new Integer(s) == 0) ? false : true;
	}

	s = params.get("clustersCount");
	if (s != null) 
        {
            clustersCount = Integer.parseInt(s);
	}

	this.clusters = new Cluster[clustersCount];

    }
    public void generateClusters() 
    {
	boolean hasChanged = true;
	boolean[] clustersEmptyStatus = new boolean[clusters.length];		

	while (hasChanged && !isInterrupted()) 
        {
            for (int i = 0; i < clustersEmptyStatus.length; i++)
		clustersEmptyStatus[i] = true;
		
            hasChanged = false;
	
            for (int k = 0; k < clusters.length; k++) 
            {
		Cluster c = clusters[k];
		for (int i = 0; i < c.getObjects().size(); i++) 
                {
                    Point object = c.getObjects().get(i);
                    int bestCluster = -1;
                    double minDistance = Double.MAX_VALUE;

                    for (int m = 0; m < clusters.length; m++) 
                    {
			if (useAllClusters && (clusters[m].getCentroid() == null && clustersEmptyStatus[m])) 
                        {
                            bestCluster = m;
                            minDistance = 0;
                            clustersEmptyStatus[m] = false;
			} 
                        else 
                        {
                            if (clusters[m].getCentroid() != null && distanceBetween(KMeans.this.image.getData()[object.y][object.x],clusters[m].getCentroid()) < minDistance) 
                            {
				bestCluster = m;
				minDistance = distanceBetween(KMeans.this.image.getData()[object.y][object.x],clusters[m].getCentroid());
                            }
			}
                    }

                    if (bestCluster != k) 
                    {
			hasChanged = true;
			KMeans.this.image.getSegment()[object.y][object.x] = (byte) bestCluster;
                    }
		}

            }
            rebuildClusters();

            if (observer != null) 
            {
		observer.onChange();
            }
	}
	if (observer != null) 
        {
            observer.onComplete();
	}
    }

    public void rebuildClusters() 
    {
	this.clusters = new Cluster[this.clusters.length];

	for (int i = 0; i < clusters.length; i++) 
        {
            clusters[i] = new Cluster();
	}

	for (int i = 0; i < image.getHeight(); i++) 
        {
            for (int j = 0; j < image.getWidth(); j++) 
            {
		clusters[image.getSegment()[i][j]].add(new Point(j, i));
            }
	}

	for (int i = 0; i < clusters.length; i++) 
        {
            clusters[i].updateCentroid();
	}

    }

    public void randomInit() 
    {
	Random r = new Random();
	for (int i = 0; i < image.getHeight(); i++) 
        {
            for (int j = 0; j < image.getWidth(); j++) 
            {
                int clusterIndex = r.nextInt(clusters.length);
		image.getSegment()[i][j] = (byte) clusterIndex;
		
            }
	}

    }

    public double distanceBetween(int[] vec1, int[] vec2) 
    {
	double dist = 0;
	for (int i = 0; i < vec1.length; i++) 
        {
            dist += Math.pow(vec1[i] - vec2[i], 2);
	}

	return Math.sqrt(dist);
    }

    private boolean isInterrupted() {
    return Thread.currentThread().isInterrupted();
}

    class Cluster 
    {
	public List<Point> objects; 
	public int[] centroid; 

	public Cluster() 
        {
            objects = new ArrayList<Point>();
            centroid = null;
	}

	public Cluster(List<Point> objects) 
        {
            this.objects = objects;
            this.centroid = euclideanMeanDistance(objects);
	}

	public void add(Point o) 
        {
            objects.add(o);
	}

	public void updateCentroid() 
        {
            if (objects != null && objects.size() > 0) 
            {
		centroid = euclideanMeanDistance(objects);
            } 
            else 
            {
                centroid = null;
            }
	}

	public int[] getCentroid() 
        {
            return centroid;
	}

	public List<Point> getObjects() 
        {
            return objects;
	}

	public int[] euclideanMeanDistance(List<Point> points) 
        {
            int[] info = new int[KMeans.this.image.getDepth()];
            int[] output = new int[info.length];

            for (Point p : points) 
            {
		int[] data = KMeans.this.image.getData()[p.y][p.x];
		for (int i = 0; i < data.length; i++) 
                {
                    info[i] += data[i];
		}
            }

            for (int i = 0; i < info.length; i++) 
            {
		output[i] = (info[i] / points.size());
            }
            return output;
	}
    }

    public void run() 
    {
	randomInit();
	rebuildClusters();
	generateClusters();
    }
    
}
