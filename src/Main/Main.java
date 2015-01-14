package Main;

import Test.TestAdaptation;
import Test.TestCBLibrary;
import Test.TestCreateDataset;
import Test.TestSimilarity;

public class Main {

	public static void main(String[] args) throws Exception {
		
		// Retrieve information from a predefined dataset
		TestCreateDataset testdataset = new TestCreateDataset();
		//testdataset.insertDataset("datasets/testDataset");
		testdataset.insertDataset("datasets/abalone");
		
		// Create Case Base Library test class
		TestCBLibrary testcbl = new TestCBLibrary();
		
		//TestRetrieval testRetrieval = new TestRetrieval();
		TestSimilarity.getSimilarsCases();
		
		// Test Adaptation
//		TestAdaptation ta = new TestAdaptation();
//		ta.testAdaptation();

	}

}
