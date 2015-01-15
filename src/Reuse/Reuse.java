package Reuse;

import java.util.ArrayList;

import Case_Structure.Case;
import Case_Structure.Case_Library;
import Retrieval.Similarity;

public class Reuse {
	
	private double upper_threshold;
	private double lower_threshold;
	Similarity similarity;
	private Case_Library lib;
	
	/* Integer that defines the type of the revision:
		0 : TOTAL_REUSE (default)
		1 : MAXIMAL_DIVERSITY
		2 : MINIMUM_DIVERSITY
		3 : BALANCED
	 */
	private int policy;
	
	public Reuse(int p, Similarity sim, Case_Library l)
	{
		this.policy = p;
		this.similarity = sim;
		this.lib = l;
		
		set_thresholds();
	}
	
	public Reuse(Similarity sim, Case_Library l)
	{
		this.policy = 0;
		this.similarity = sim;
		this.lib = l;
		
		set_thresholds();
	}
	
	private void set_thresholds()
	{
		switch (this.policy)
		{
			case 1: 
				upper_threshold = 0.9;
				lower_threshold = 0.7;
				break;
			case 2:
				upper_threshold = 0.3;
				lower_threshold = 0.1;
				break;
			case 3:
				upper_threshold = 0.9;
				lower_threshold = 0.1;
				break;
			default:
				upper_threshold = 1.0;
				lower_threshold = 0.0;
				break;
		}
	}
	
	/**
	 * Method that implements reuse step. 
	 * First, all distances are computed between c and all cases in library. 
	 * Then, the mean distance is computed and later normalized between [0,1].
	 * Finally, according to the policy, it will be reused or not.
	 * @param c
	 * @return
	 */
	public boolean reuse(Case c)
	{
		// Compute euclidean distances with respect to all cases from library 
		ArrayList<Double> distances = similarity.compute_distances_all_cases(c);
		
		// Compute the mean
		Double mean = mean(distances);
		
		// Normalize the distance
		Double norm_mean = normalize(distances, mean);
		
		if (inside_threshold(norm_mean))
		{
			System.out.println("-> Adding new case to Library!");
			lib.addCase(c);
			return true;
		}
		else 
			return false;
	}
	
	/**
	 * Check if a value x is inside an interval defined by lower_threshold and upper_threshold class attributes.
	 * @param x
	 * @return
	 */
	private boolean inside_threshold (Double x)
	{
		return (x >= this.lower_threshold) && (x <= this.upper_threshold);
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
		Double max = Double.MAX_VALUE;
	    Double min = Double.MIN_VALUE;
	    
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
		
		/* Do feature scaling */
		if (max_val == min_val)
			return 1.0;		// To avoid division by zero
		return (x-min_val) / (max_val-min_val);
	}
}
