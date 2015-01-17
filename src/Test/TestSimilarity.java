package Test;

import java.util.ArrayList;

import Adaptation.Adaptation;
import Case_Structure.Case;
import Case_Structure.Case_Library;
import Retrieval.Similarity;

public class TestSimilarity {

	public static void getSimilarsCases() throws Exception {
		int k = 10; //Number of similar cases to retrieve
		
		//Load the dataset
		String path = "datasets/abalone/";
		Case_Library lib = new Case_Library();
		lib.readDataset(path);
		
		//Get the target case
		Case newCase = lib.getCase(0);
		newCase.removeAttribute(0);
		lib.removeCase(0);
		
		//Get the similars cases
		Similarity similarity = new Similarity(lib);
		ArrayList<Case> similarCases = similarity.getSimilarCases(newCase,k);
		Case similarCase = similarCases.get(6);
		
		Adaptation adapt = new Adaptation(path + "/AdaptationKnowledge", lib);
		System.out.println("New Case: " + newCase.toString() + "\nSimilar Case: " + similarCase.toString());
		//adapt.NullAdaptation(newCase, similarCase);
		adapt.SubstitutionAdaptation(newCase, similarCase, similarity);
		System.out.println("------------------------ After adaptation ------------------------");
		System.out.println("New Case: " + newCase.toString() + "\nSimilar Case: " + similarCase.toString());
	}
}
