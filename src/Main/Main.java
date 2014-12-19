package Main;

import Test.TestCBLibrary;
import Test.TestCreateDataset;
import Test.TestRetrieval;

public class Main {

	public static void main(String[] args) {
		
		// Retrieve information from a predefined dataset
		TestCreateDataset testdataset = new TestCreateDataset();
		testdataset.insertDataset("datasets/testDataset");
		
		// Create Case Base Library test class
		//TestCBLibrary testcbl = new TestCBLibrary();
		
		//TestRetrieval testRetrieval = new TestRetrieval();

	}

}
