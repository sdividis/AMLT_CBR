package Test;

import java.util.ArrayList;

import Case_Structure.Case;
import Case_Structure.Case_Library;
import Revision.Expert;
import Revision.Revision;
import Revision.TestExpert;



public class TestRevision {

	public TestRevision(){
		
		TestCBLibrary t = new TestCBLibrary();
		
		Case c1 = t.lib.getCase(0);
		
		/////// Find all ingredients in CASE1 solutions
		//ArrayList<ArrayList<Integer>> sol_position = c1.existsSolution("ingredient");
		// PLOTS as an example, the hierarchical positions for all solutions named "ingredient"
		//System.out.println(sol_position);

		/*ArrayList<Object> ingredient1 = c1.getSolution(sol_position.get(0));
		ArrayList<Object> caca = (ArrayList<Object>) ingredient1.get(0);
		System.out.println(caca.get(0));
		Object ingredient2 = c1.getSolution(sol_position.get(1));*/
		
		// Create an Expert
		TestExpert expert = new TestExpert();
		
		// Revise a case
		Revision rev = new Revision(expert);
		boolean answer = rev.revise(c1);
		System.out.println("Feedback from revision was: "+answer);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new TestRevision();
	}
}
