package Retrieval;

import java.util.ArrayList;
import java.util.HashMap;
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
	
	public int booleanDistance(Boolean targetValue, Boolean caseValue){
		int x;
		boolean result = (targetValue == caseValue);
		if(result){
			x = 1;
		}else{
			x = 0;
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
	 */
	private double computeKNN(Case targetCase, Case c){
		double total;
		float sum=0, x;
		int numAttributes = targetCase.getNumAttributes();
		for(int i=0; i<numAttributes; i++){
			//Get the attributes
			ArrayList<Object> targetAttribute = targetCase.getAttribute(i);
			ArrayList<Object> caseAttribute = c.getAttribute(i);
			
			//Get the type of the attribute
			Object type = targetAttribute.get(POS_TYPE);
			
			String targetName = (String)targetAttribute.get(POS_NAME);
			String caseName = (String)targetAttribute.get(POS_NAME);

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
					x = booleanDistance((Boolean) targetValue, (Boolean) caseValue);
					sum += x;
				}else if(typeLower.equals("float")){
					float targetFloat = Float.valueOf((String)targetValue);
					float caseFloat = Float.valueOf((String)caseValue);

					//Normalize the target value	
//					System.out.println(targetFloat);
					targetFloat = (targetFloat - minMap.get(targetName)) / (maxMap.get(targetName) - minMap.get(targetName));
					sum += (targetFloat - caseFloat)*(targetFloat - caseFloat); 
				}
			}
		}
		total = Math.sqrt(sum);
		return total;
	}
	
	/**
	 * Computes euclidean distance between target case and all cases from library.
	 * @param targetCase
	 * @return distances ArrayList of doubles containing the similarities.
	 * @author Albert Busqu�
	 */
	public ArrayList<Double> compute_distances_all_cases(Case targetCase)
	{
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
					System.out.println("Normalized:"+normalizedValue+" Name:"+name+" Data:"+Float.valueOf((String)value)+ "Min:"+minMap.get(name)+" Max:"+maxMap.get(name));
					c.setValue(j, String.valueOf(normalizedValue));
				}
			}	
		}
		System.out.println("Data was normalized!");

	}
}
