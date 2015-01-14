package Test;

import java.util.ArrayList;

import Case_Structure.Case;
import Case_Structure.Case_Library;
import Revision.Expert;
import Revision.Revision;

class TestExpert extends Expert {

	@Override
	public boolean ask(Case c) {
		
		/* We will put some stupid rules */
		
		/* Extract the ingredients of the solution */
		ArrayList<String> ingredients = new ArrayList<String>();
		ArrayList<ArrayList<Integer>> sol_position = c.existsSolution("ingredient");
		for (ArrayList<Integer> pos : sol_position)
		{
			ingredients.add((String) ((ArrayList<Object>)c.getSolution(pos).get(0)).get(0));
		}
		
		/* If the solution contains salt, then it is not a good solution */
		String rule1 = "salt";
		if (ingredients.contains(rule1))
			return false;
		return true;
	}
	
}

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
