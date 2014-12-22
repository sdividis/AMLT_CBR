package Test;

import Case_Structure.Case_Library;

public class TestCreateDataset {

	public Case_Library lib;
	
	public TestCreateDataset(){
		lib = new Case_Library();
	}
	
	public void insertDataset(String path){
		lib.readDataset(path);
		System.out.println("Done inserting dataset in " + path);
	}
	
}
