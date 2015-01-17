package Retrieval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import Case_Structure.Case;
import Case_Structure.Case_Library;


/**
 * Similarity class in which all the similarities methods are included
 * @author davidsanchezpinsach
 *
 */
public class Similarity {

	/**
	 * Library of cases
	 */
	private Case_Library lib;
	private static int POS_VALUE = 0;
	private static int POS_NAME = 1;
	private static int POS_TYPE = 2;
	HashMap<String, Float> minMap = new HashMap<String, Float>();
	HashMap<String, Float> maxMap = new HashMap<String, Float>();
	
	/**
	 * Similary class
	 * @param lib Case_Library that contains all the cases
	 */
	public Similarity(Case_Library lib){
		this.lib = lib;	
		normalizedLibrary();
	}
	
	/**
	 * Method to compute the distance between two string.
	 * @param first First string to compare
	 * @param second Second string to compare
	 * @return Return 1 when the strings are the same, otherwise 0.
	 */
	public int stringDistance(String first, String second){
		int x = 0;
		if(first.equals(second)){
			x = 0;
		}else{
			x = 1;
		}
		return x;
	}
	
	/**
	 * Method to compue the distance between two boolean
	 * @param targetValue First value
	 * @param caseValue Second value
	 * @return Return 1 when the boolean are the same, otherwise0
	 */
	public int booleanDistance(Boolean targetValue, Boolean caseValue){
		int x;
		boolean result = (targetValue == caseValue);
		if(result){
			x = 0;
		}else{
			x = 1;
		}
		return x;
	}
	
	/**
	 * Method to compute and obtain the most similar cases of the targetCase
	 * @param targetCase
	 * @param k
	 * @return
	 */
	public ArrayList<Case> getSimilarCases(Case targetCase, int k){
		ArrayList<Case> mostSimilarCases = new ArrayList<Case>();
		Case c = null;
		double value;
		ArrayList<Float> similarities = new ArrayList<Float>();
	    Map<Double, Integer> map = new TreeMap<Double, Integer>();
	    targetCase = normalize_case(targetCase);
	    
		int num_cases = lib.getNumCases();
		//Iterate all the cases and compute the euclidean distance for each.
		for(int i=0; i<num_cases; i++){
			c = lib.getCase(i);
			value = computeKNN(targetCase, c);	
			similarities.add((float) value);
	        map.put(value,i);
		}
		
	    ArrayList<Integer> indices = new ArrayList<Integer>(map.values());

	    System.out.println("Similarities: "+similarities);
		System.out.println("Index \n"+indices);

		//Using the sort indices the system will built the most similar case arraylist
		for(int i= 0; i<k; i++){
			c = lib.getCase(indices.get(i));
			mostSimilarCases.add(c);
		}
		
		return mostSimilarCases;
	}	
	
	/**
	 * Method that compute the KNN algorithm giving the most similar cases
	 * @param targetCase The new case
	 * @param c Case of the our Case Library
	 * @return Return the similarity value
	 * @author Albert Busqué
	 */
	private double computeKNN(Case targetCase, Case c){
		int j;
		double total;
		boolean found;
		float sum=0, x;
		int numAttributes = targetCase.getNumAttributes();
		int numAttributesCase = 0;
		for(int i=0; i<numAttributes; i++){
			found = false;
			j = 0;
			ArrayList<Object> targetAttribute = targetCase.getAttribute(i);
			String targetName = (String)targetAttribute.get(POS_NAME);
			Object type = targetAttribute.get(POS_TYPE);

			numAttributesCase = c.getNumAttributes();
			
			//Iterate for all the samples
			while(!found && j<numAttributesCase){
				//Get the attributes
				ArrayList<Object> caseAttribute = c.getAttribute(j);
				
				//Get the type of the attribute
				
				String caseName = (String)caseAttribute.get(POS_NAME);
				
				//Check if the attributes are the same
				if(targetName.equals(caseName)){
					//Get the value of the attributes
					Object targetValue = targetAttribute.get(POS_VALUE);
					Object caseValue = caseAttribute.get(POS_VALUE);
					
					String typeLower = (String) type;
					typeLower = typeLower.toLowerCase();
					
					//Distinguish between different type of values and using different method to compute the similarity
					if(typeLower.equals("string")){
						x = stringDistance((String) targetValue,(String) caseValue);
						sum += x;
					}else if(typeLower.equals("boolean")){
						x = booleanDistance(Boolean.valueOf((String) targetValue), Boolean.valueOf((String) caseValue));
						sum += x;
					}else if(typeLower.equals("float")){
						float targetFloat = Float.valueOf((String)targetValue);
						float caseFloat = Float.valueOf((String)caseValue);

						//Normalize the target value	
//						System.out.println(targetFloat);
						targetFloat = (targetFloat - minMap.get(targetName)) / (maxMap.get(targetName) - minMap.get(targetName));
						sum += (targetFloat - caseFloat)*(targetFloat - caseFloat); 
					}
					found = true;
				}else{
					j++;
				}
			}
			
			if(!found){
				sum += 1;
			}
			
		}
		total = Math.sqrt(sum);
		return total;
	}
	
	/**
	 * Computes euclidean distance between target case and all cases from library.
	 * @param targetCase The new case
	 * @return distances ArrayList of doubles containing the similarities.
	 * @author Albert Busqu�
	 */
	public ArrayList<Double> compute_distances_all_cases(Case targetCase){
		double value;
		int num_cases = lib.getNumCases();
		ArrayList<Double> distances = new ArrayList<Double>();
		Case c;
		
		//Iterate all the cases and compute the euclidean distance for each.
		for(int i=0; i<num_cases; i++){
			c = lib.getCase(i);
			value = computeKNN(targetCase, c);	
			distances.add(value);
		}
		return distances;
	}
	
	public Double compute_distance(Case c1, Case c2){
		return computeKNN(c1, c2);
	}
	
	/**
	 * Normalize the case
	 * @param c
	 * @return
	 * @author Albert Busqué
	 */
	public Case normalize_case(Case c)
	{
		for(int j=0; j<c.getNumAttributes(); j++){
			ArrayList<Object> caseAttribute = c.getAttribute(j);
			//Get the type of the attribute
			String type = (String)caseAttribute.get(POS_TYPE);
			String name = (String)caseAttribute.get(POS_NAME);
			Object value = caseAttribute.get(POS_VALUE);
			
			type = type.toLowerCase();
			if(type.equals("float")){
				Float normalizedValue =( (Float.valueOf((String)value) - minMap.get(name)) / (maxMap.get(name) - minMap.get(name)));
				System.out.println("Normalized:"+normalizedValue+" Name:"+name+" Data:"+Float.valueOf((String)value)+ "Min:"+minMap.get(name)+" Max:"+maxMap.get(name));
				c.setValue(j, String.valueOf(normalizedValue));
			}
		}
		return c;
	}
	
	/**
	 * Computes the mean case from the Case Library
	 * @author Albert Busqué
	 * @return
	 */
	public Case get_mean_case(ArrayList<Case> list)
	{
		boolean uselib = list == null || list.size() == 0;
		int num_cases = 0;
		
		if (uselib)
			num_cases = lib.getNumCases();
		else
			num_cases = list.size();
		assert num_cases > 0;
		
		HashMap<String, HashMap<String, Integer>> list_attributes = new HashMap<String, HashMap<String, Integer>>();
		HashMap<String, ArrayList<Float>> list_numerical_attributes = new HashMap<String, ArrayList<Float>>();
		HashMap<String, String> list_types = new HashMap<String, String>();
		String domain = "";
		
		/* Computing the mean case */
		for(int i=0; i<num_cases; i++)
		{
			Case c;
			if (uselib)
				c = lib.getCase(i);
			else
				c = list.get(i);
			if (i==0)
				domain = c.getDomain();
			for(int j=0; j<c.getNumAttributes(); j++)
			{
				ArrayList<Object> caseAttribute = c.getAttribute(j);
				
				//Get the type of the attribute
				String type = (String)caseAttribute.get(POS_TYPE);
				String name = (String)caseAttribute.get(POS_NAME);
				Object value = caseAttribute.get(POS_VALUE);
				String old_type = type;
				type = type.toLowerCase();
				
				if(type.equals("string") || type.equals("boolean"))
				{
					/* If first time, initialize with empty map */
					if (!list_attributes.containsKey(name))
					{
						HashMap<String, Integer> map = new HashMap<String, Integer>();
						list_attributes.put(name, map);
						list_types.put(name, old_type);
					}
					
					/* If first time, initialize with counts=0 */
					if(!list_attributes.get(name).containsKey((String)value))
						list_attributes.get(name).put((String)value, 0);
					
					/* Adding one count for the new string value */
					int counts = list_attributes.get(name).get((String)value);
					list_attributes.get(name).put((String)value, counts+1);
				}
				/* Accumulating the values of float for later on computing the mean*/
				else if (type.equals("float"))
				{
					/* If first time, initialize with empty map */
					if (!list_numerical_attributes.containsKey(name))
					{
						list_numerical_attributes.put(name, new ArrayList<Float>());
						list_types.put(name, old_type);
					}
					
					/* Adding the new value */
					list_numerical_attributes.get(name).add(Float.valueOf((String)value));
				}
				else
				{
					System.out.println("-> Type still not supported.");
				}
			}
		}
		
		/* Creating the new case as mean case */
		Case new_case = new Case(domain);
		
		/* Starting with String attributes. The mean will be the one with more occurences */
		Iterator it = list_attributes.entrySet().iterator();
		while (it.hasNext())
		{
			Map.Entry<String, HashMap<String, Integer>> pairs = (Map.Entry<String, HashMap<String, Integer>>)it.next();
			String name = pairs.getKey();
			/* Search for the max value */
			HashMap<String, Integer> map = pairs.getValue();
			int min = 0;
			String value_max = "";
			for (String value : map.keySet())
			{
				int count = map.get(value);
				if (count > min)
				{
					min = count;
					value_max = value;
				}
			}
			new_case.addAttribute((Object) value_max, name, list_types.get(name));
		}
		
		/* Numerical attributes */
		for (String name : list_numerical_attributes.keySet())
		{
			ArrayList<Float> vec = list_numerical_attributes.get(name);
			float mean = mean(vec);
			/* De-normalize, not used now */
			//mean = (this.maxMap.get(name) - this.minMap.get(name))*mean + this.minMap.get(name);
			new_case.addAttribute((Object) String.valueOf(mean), name, list_types.get(name));
		}
		
		return new_case;
	}
	
	/**
	 * Computes the mean of given vector.
	 * @param x Vector of numbers
	 * @return The mean of x
	 * @author Albert Busqué
	 */
	private Float mean(ArrayList<Float> x)
	{
		Double sum = 0.0;
		
		for (Float value : x)
		{
			sum += value;
		}
		return (float) (sum/x.size());
	}
	
	/**
	 * Function to normalized the lib
	 */
	public void normalizedLibrary(){
		System.out.println("\nNormalizing the data...");
		Case c = null;

		int num_cases = lib.getNumCases();
		
		// Extract the min and the max value
		for(int i=0; i<num_cases; i++){
			c = lib.getCase(i);
			for(int j=0; j<c.getNumAttributes(); j++){
				ArrayList<Object> caseAttribute = c.getAttribute(j);
				//Get the type of the attribute
				String type = (String)caseAttribute.get(POS_TYPE);
				String name = (String)caseAttribute.get(POS_NAME);
				Object value = caseAttribute.get(POS_VALUE);
				type = type.toLowerCase();
				
				if(type.equals("float")){
					Float valueF = Float.valueOf((String)value);
			
					if(minMap.containsKey(name) && maxMap.containsKey(name)){
						if(minMap.get(name) > valueF){
							minMap.put(name, valueF);
						}else if(maxMap.get(name) < valueF){
							maxMap.put(name, valueF);
						}
					}else{
						minMap.put(name, valueF);
						maxMap.put(name, valueF);
					}
				}
			}
		}
		
		// Normalized the data
		for(int i=0; i<num_cases; i++){
			c = lib.getCase(i);
			for(int j=0; j<c.getNumAttributes(); j++){
				ArrayList<Object> caseAttribute = c.getAttribute(j);
				//Get the type of the attribute
				String type = (String)caseAttribute.get(POS_TYPE);
				String name = (String)caseAttribute.get(POS_NAME);
				Object value = caseAttribute.get(POS_VALUE);
				
				type = type.toLowerCase();
				if(type.equals("float")){
					Float normalizedValue =( (Float.valueOf((String)value) - minMap.get(name)) / (maxMap.get(name) - minMap.get(name)));
//					System.out.println("Normalized:"+normalizedValue+" Name:"+name+" Data:"+Float.valueOf((String)value)+ "Min:"+minMap.get(name)+" Max:"+maxMap.get(name));
					c.setValue(j, String.valueOf(normalizedValue));
				}
			}	
		}
		System.out.println("Data was normalized!");

	}
}
