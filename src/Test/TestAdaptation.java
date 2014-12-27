package Test;

import Adaptation.Adaptation;
import Case_Structure.Case;
import Case_Structure.Case_Library;
import Retrieval.Similarity;

public class TestAdaptation {

	public void testAdaptation() throws Exception {
		String path = "datasets/abalone/";
		Case_Library lib = new Case_Library();
		lib.readDataset(path);
		
		Adaptation adapt = new Adaptation(path + "/AdaptationKnowledge", lib);
		Case newCase = lib.getCase(2);
		Case similarCase = lib.getCase(15);
		System.out.println("New Case: " + newCase.toString() + "\nSimilar Case: " + similarCase.toString());
		//adapt.NullAdaptation(newCase, similarCase);
		adapt.SubstitutionAdaptation(newCase, similarCase, new Similarity(lib));
		System.out.println("------------------------ After adaptation ------------------------");
		System.out.println("New Case: " + newCase.toString() + "\nSimilar Case: " + similarCase.toString());
	}
}
