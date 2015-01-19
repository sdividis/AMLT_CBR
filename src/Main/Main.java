package Main;

import java.util.ArrayList;

import Adaptation.Adaptation;
import Case_Structure.Case;
import Case_Structure.Case_Library;
import Case_Structure.Solution;
import Case_Structure.Solution_Type;
import Retain.Retain;
import Retrieval.Similarity;
import Revision.Revision;
import Revision.TestExpert;
import Test.TestAdaptation;
import Test.TestCBLibrary;
import Test.TestCreateDataset;
import Test.TestSimilarity;

/**
 * Main class
 * @author davidsanchezpinsach
 *
 */
public class Main {

	private static Case_Library lib;
	private static Case_Library trainDataset;
	private static Case_Library testDataset;
	private static Similarity similarity;
	private static Retain retain;
	
	private static boolean debug = false;
	
	//Adaptation
	private final static int SUBSTITUTION_ADAPTATION = 1;
	private final static int NULL_ADAPTATION = 2;
	
	//Revision
	private final static int NO_REVISION = 1;
	private final static int EXPERT_REVISION = 2;
	private final static int USER_REVISION = 3;
	
	
	public static void main(String[] args) throws Exception {
		
		int id_test = 1;
		float prop_test = 1/8f;
		int type_eval = 2; // (1 = general, 2 = MSE)
		
		// We apply a CBR accuracy test on the dataset identified 
		// by id_test splitting prop_test of the cases for test
		finalTests(id_test, prop_test, type_eval);
	}
	
	
	/**
	 * Mehtod to compute the final tests
	 * @param id_test int identification of the Dataset used for the tests
	 * @param prop_test float indication the proportion of cases used for testing
	 * @param type_eval int selecting the evaluation type (1 = general, 2 = MSE)
	 * @return
	 * @throws Exception 
	 */
	public static boolean finalTests(int id_test, float prop_test, int type_eval) throws Exception{
		//Create and read the dataset structure
		loadAndReadDataset(id_test);
		
		//Split the data into train and test
		splitDataset(prop_test);
		
		int numTestCases = testDataset.getNumCases();
		similarity = new Similarity(trainDataset);

		Double totalError = 0.0;
		Double error = 0.0;
		
		for(int i=0; i<numTestCases; i++){
			Case c = testDataset.getCase(i);
			
			// Keep a ground truth copy of the original case (for accuracy evaluation)
			Case c_GT = new Case(c.getDomain());
			c_GT.copyAttributes(c);
			c_GT.copySolution(c);
			
			//Remove the solution of the test case
			//ArrayList<Solution> solutions = c.getSolutions();
			//ArrayList<Solution_Type> solutionsTypes = c.getSolutionsTypes();
			c.removeAllSolutions();
			
			//RETRIEVE - Looking for the similar cases
			Case similarCase = similarCase(c,1);	
			
			//REUSE - Adapt the similar case to the new solution
			Case adaptedCase = adaptationCase(similarCase, 1,1); 
			
			//REVISE - Revise the case
			Case revisedCase = revisionCase(adaptedCase,1);
			
			//RETAIN - Save or not save the test case into the trainCases
			retainCase(revisedCase);
			
			//Compare the results for measure the accuracy
			//ArrayList<Solution> solutionsRevised = revisedCase.getSolutions();
			//ArrayList<Solution_Type> solutionsTypesRevised = revisedCase.getSolutionsTypes();
			
			// Compute the accuracy of the solution using the real solution of the case
			if(type_eval == 1){
				error = 1 - similarity.evaluateAccuracy(revisedCase, c_GT);
			}else if(type_eval == 2){
				error = similarity.evaluateMSE(revisedCase, c_GT);
			}
			//error = checkSolutions(revisedCase, c_GT);
			totalError += error;
		}
		
		System.out.println("Total error = "+totalError+"/"+numTestCases);
		System.out.println("Total error = "+(totalError/numTestCases));
		return true;
	}
	
	
	/**
	 * Method to create and load the dataset
	 * @param dataset Id of the dataset
	 */
	private static void loadAndReadDataset(int dataset){
		System.out.println("Starting the CBR system...");
		String path = "";
		
		lib = new Case_Library();
		switch(dataset){
		case 1:
			path = "datasets/abalone";
			break;
		case 2:
			//other dataset
			path = "datasets/";
			break;
		}
		lib.readDataset(path);
		if(debug) System.out.println("Num total cases:"+lib.getNumCases());
	}
	
	
	/**
	 * Method to split the dataset into train and test dataset
	 */
	private static void splitDataset(float prop_test){
		int testCases = Math.round((lib.getNumCases()) * prop_test);
		trainDataset = new Case_Library();
		testDataset = new Case_Library();
		
		//Test cases
		for(int i=0; i<testCases; i++){
			Case c = lib.getCase(0);
			lib.removeCase(0);
			testDataset.addCase(c);
		}
		
		int trainCases = lib.getNumCases();
		//Train cases
		for(int i=0; i<trainCases; i++){
			Case c = lib.getCase(0);
			lib.removeCase(0);
			trainDataset.addCase(c);
		}
		
		if(debug) System.out.println("Num train cases:"+trainDataset.getNumCases());
		if(debug) System.out.println("Num test cases:"+testDataset.getNumCases());
	}
	
	
	/**
	 * Method to compute the most similar case.
	 * @param k Number of similar cases
	 * @return Return the most similar case
	 */
	private static Case similarCase(Case c,int k){
		//Get the most similars cases
		ArrayList<Case> similarCases = similarity.getSimilarCases(c,k);
		Case similarCase = similarCases.get(0); //Get the most similar
		return similarCase;
	}
	
	
	/**
	 * Method to adapt the most similar case
	 * @throws Exception 
	 */
	private static Case adaptationCase(Case similarCase, int k, int dataset) throws Exception{
		String path = "";
		switch(dataset){
		case 1:
			path = "datasets/abalone/";
			break;
		case 2:
			path = "datasets/";
			break;
		}
		
		Adaptation adapt = new Adaptation(path+"AdaptationKnowledge", trainDataset);
		Case newCase = trainDataset.getCase(0);
		newCase.removeAllSolutions();
		if(debug) System.out.println("The case was adapted!");
		
		switch(k){
			case SUBSTITUTION_ADAPTATION:
				adapt.SubstitutionAdaptation(newCase, similarCase, similarity);
				break;
			case NULL_ADAPTATION:
				adapt.NullAdaptation(newCase, similarCase);
				break;
		}
		System.out.println(newCase.getSolutions());
	
		return newCase;
	}
	
	/**
	 * Method to revise the case
	 * @param adaptedCase Case that was adapted
	 * @return Return the revision of the adapted case
	 */
	private static Case revisionCase(Case adaptedCase, int k){
		Revision rev = null;
		switch(k){
			case NO_REVISION:
				rev = new Revision();
				break;
			case EXPERT_REVISION:
				TestExpert expert = new TestExpert();
				rev = new Revision(expert);
				break;
			case USER_REVISION:
				rev = new Revision(1);
				break;
			}
		boolean revised = rev.revise(adaptedCase);
		if(revised){
			if(debug) System.out.println("The case was revised correctly");
		}else{
			if(debug) System.out.println("The case was not revised correctly");
		}
		return adaptedCase;
	}
	
	/**
	 * Method to retain the case
	 * @param adaptedCase The case that was adapted
	 */
	private static void retainCase(Case adaptedCase){
		Retain r = new Retain(similarity, trainDataset);
		boolean retained = r.retain(adaptedCase);
		if(retained){
			if(debug) System.out.println("The case was retained!");
		}else{
			if(debug) System.out.println("The case was not retained!");
		}
	}
	
	/**
	 * Method to check if the two solutions are equal or not
	 * @param new_case Case with the solution proposed by the CBR system
	 * @param ground_truth Case with the original ground_truth solution
	 * @return error value (between 0 = perfect or 1 = completely wrong).
	 */
	private static Double checkSolutions(Case new_case, Case ground_truth){
		
		/*
		// Compare cases' solutions
		new_case = similarity.normalize_case(new_case);
		ground_truth = similarity.normalize_case(ground_truth);
		
		// Compute euclidean distances with GT and all cases from library
		ArrayList<Double> distances = similarity.compute_distances_all_cases(ground_truth);
		
		// Compute the distance between the mean case and normalized c
		Double dist = similarity.compute_distance(ground_truth, new_case);
		
		// Normalize the distance
		retain = new Retain(similarity, trainDataset);
		Double error = retain.normalize(distances, dist);
		*/
		
		Double error = 1 - similarity.evaluateAccuracy(new_case, ground_truth);
		return error;
	}
	
	/**
	 * Method to perfom unit tests
	 * @return
	 * @throws Exception
	 */
	public static boolean unitTests() throws Exception{
		// Retrieve information from a predefined dataset
		TestCreateDataset testdataset = new TestCreateDataset();
		//testdataset.insertDataset("datasets/testDataset");
		testdataset.insertDataset("datasets/abalone");
		
		// Create Case Base Library test class
		TestCBLibrary testcbl = new TestCBLibrary();
		
		TestSimilarity.getSimilarsCases();
		
		// Test Adaptation
		TestAdaptation ta = new TestAdaptation();
		ta.testAdaptation();

		return true;
	}

}
