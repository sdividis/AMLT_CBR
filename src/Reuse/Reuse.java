package Reuse;

import java.util.ArrayList;

import Case_Structure.Case;
import Case_Structure.Case_Library;
import Retrieval.Similarity;

public class Reuse {
	
	private double threshold;
	Similarity similarity;
	private Case_Library lib;
	
	public Reuse(double t, Similarity sim, Case_Library l)
	{
		this.threshold = t;
		this.similarity = sim;
		this.lib = l;
	}
	
	public boolean reuse(Case c)
	{
		// Compute euclidean distances with respect to all cases from library 
		ArrayList<Double> distances = similarity.compute_distances_all_cases(c);
		
		// Compute the mean
		Double mean = mean(distances);
		
		// Normalize the distance
		Double norm_mean = normalize(distances, mean);
		
		if (norm_mean > this.threshold)
		{
			lib.addCase(c);
			return true;
		}
		else 
			return false;
	}
	
	/**
	 * Computes the mean of given vector.
	 * @param x Vector of numbers
	 * @return The mean of x
	 */
	private Double mean(ArrayList<Double> x)
	{
		Double sum = 0.0;
		
		for (Double value : x)
		{
			sum += value;
		}
		return sum/x.size();
	}
	
	/**
	 * Normalize x between 0 and 1.
	 * @param vector
	 * @param x
	 * @return
	 */
	private Double normalize(ArrayList<Double> vector, Double x)
	{
		Double min = Double.MAX_VALUE;
	    Double max = Double.MIN_VALUE;
	    
	    // Get the min value
	    Double min_val = max;
		for (Double value : vector)
		{
			if (value < min_val)
				min_val = value;
		}
		
		// Get the max value
	    Double max_val = min;
		for (Double value : vector)
		{
			if (value > max_val)
				max_val = value;
		}
		
		// Do feature scaling
		return (x-min_val) / (max_val-min_val);
	}
}
