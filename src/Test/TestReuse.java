package Test;

import java.util.ArrayList;

import Case_Structure.Case;
import Retrieval.Similarity;
import Reuse.Reuse;

public class TestReuse {

	public TestReuse()
	{
		TestCBLibrary t = new TestCBLibrary();
		Similarity similarity = new Similarity(t.lib);
		Reuse reuse = new Reuse(similarity, t.lib);
		//Case c1 = t.lib.getCase(0);
		
		/* New case*/
		String dom1 = "Cooking_Recipes";
		Case c1 = new Case(dom1);
		
		/**
		 * Add some ingredients as attributes
		 */
		c1.addAttribute((Object) "salt", "ingredient", "String");
		c1.addAttribute((Object) "pepper", "ingredient", "String");
		c1.addAttribute((Object) "fish", "ingredient", "String");
		
		/**
		 * One of the solutions for our Recipe will be to mix salt and 200g of meat, so
		 * in a simplified way it will be: mix(salt,meat(200g))
		 */
		// add a general solution object
		int pos_action = c1.addSolution("mix", "action", "String");
		ArrayList<Integer> hierarchy_pos = new ArrayList<Integer>();
		
		// insert a sub-solution for the solution "mix"
		c1.addSolutionComponent(pos_action, (Object)"salt", "ingredient", "String", hierarchy_pos);
		
		// insert another sub-solution for "mix"
		c1.addSolutionComponent(pos_action, (Object)"fish", "ingredient", "String", hierarchy_pos);
		
		// insert a sub-sub-solution (200g)
		hierarchy_pos.add(1); // add to the second sub-solution (meat)
		c1.addSolutionComponent(pos_action, (Object) 200, "quantity (grams)", "int", hierarchy_pos);

		reuse.reuse(c1);
	}
	
	public static void main(String[] args) {
		
		new TestReuse();
	}
}
