package Test;

import java.util.ArrayList;

import Case_Structure.Case;
import Case_Structure.Case_Library;
import Case_Structure.Domain;
import Retain.Retain;
import Retrieval.Similarity;

public class TestRetain {

	public TestRetain()
	{
		Case_Library lib = new Case_Library();
		
		// DOMAINS
		String dom1 = "Cooking_Recipes";
		
		/////// We must first create a Domain
		lib.addDomain(dom1);
		/////// Retrieve added Domain
		int dom_pos = lib.existsDomain(dom1);
		Domain dom = null;
		try {
			dom = lib.getDomain(dom1);
		} catch (Exception e) {
		}
		/////// Add attribute names
		dom.addAttributeName("ingredient1");
		dom.addAttributeName("ingredient2");
		dom.addAttributeName("ingredient3");
		/////// Add solution names
		dom.addSolutionType("action");
		dom.addSolutionType("ingredient");
		dom.addSolutionType("quantity (grams)");
		/////// Insert again the domain
		lib.resetDomain(dom, dom_pos);
		
		/*////// Now we create some examples Cases for this Domain
		// CASE 1
		Case c1 = new Case(dom1);
		c1.addAttribute((Object) "meat", "ingredient", "String");
		c1.addAttribute((Object) "shit", "ingredient", "String");
		c1.addAttribute((Object) "true", "fried", "Boolean");
		c1.addAttribute((Object) "150.0", "time", "Float");
		lib.addCase(c1);
		
		////// Now we create some examples Cases for this Domain
		// CASE 2
		Case c2 = new Case(dom1);
		c2.addAttribute((Object) "fish", "ingredient", "String");
		c2.addAttribute((Object) "shit", "ingredient", "String");
		c2.addAttribute((Object) "true", "fried", "Boolean");
		//c2.addAttribute((Object) "50.0", "time", "Float");
		lib.addCase(c2);
		
		////// Now we create some examples Cases for this Domain
		// CASE 3
		Case c3 = new Case(dom1);
		c3.addAttribute((Object) "fish", "ingredient", "String");
		c3.addAttribute((Object) "shit", "ingredient", "String");
		c3.addAttribute((Object) "false", "fried", "Boolean");
		c3.addAttribute((Object) "50.0", "time", "Float");*/
		
		// CASE 1
		Case c1 = new Case(dom1);
		
		
		/**
		 * Add some ingredients as attributes
		 */
		c1.addAttribute((Object) "salt", "ingredient", "String");
		c1.addAttribute((Object) "pepper", "ingredient", "String");
		c1.addAttribute((Object) "meat", "ingredient", "String");
		
		
		/**
		 * One of the solutions for our Recipe will be to mix salt and 200g of meat, so
		 * in a simplified way it will be: mix(salt,meat(200g))
		 */
		// add a general solution object
		int pos_action = c1.addSolution("mix", "action", "String");
		ArrayList<Integer> hierarchy_pos = new ArrayList<Integer>();
		
		/*
		// find the position of the solution named "action"
		ArrayList<ArrayList<Integer>> sol_position = c1.existsSolution("action");
		int pos_action = (int)sol_position.get(0).get(0);
		sol_position.get(0).remove(0);
		ArrayList<Integer> hierarchy_pos = sol_position.get(0);
		*/
		
		// insert a sub-solution for the solution "mix"
		c1.addSolutionComponent(pos_action, (Object)"salt", "ingredient", "String", hierarchy_pos);
		
		// insert another sub-solution for "mix"
		c1.addSolutionComponent(pos_action, (Object)"meat", "ingredient", "String", hierarchy_pos);
		
		// insert a sub-sub-solution (200g)
		hierarchy_pos.add(1); // add to the second sub-solution (meat)
		c1.addSolutionComponent(pos_action, (Object) 200, "quantity (grams)", "int", hierarchy_pos);
		
		// CASE 1
		Case c2 = new Case(dom1);
		
		
		/**
		 * Add some ingredients as attributes
		 */
		c2.addAttribute((Object) "salt", "ingredient", "String");
		c2.addAttribute((Object) "pepper", "ingredient", "String");
		c2.addAttribute((Object) "meat", "ingredient", "String");
		
		
		/**
		 * One of the solutions for our Recipe will be to mix salt and 200g of meat, so
		 * in a simplified way it will be: mix(salt,meat(200g))
		 */
		// add a general solution object
		pos_action = c2.addSolution("mix", "action", "String");
		hierarchy_pos = new ArrayList<Integer>();
		
		/*
		// find the position of the solution named "action"
		ArrayList<ArrayList<Integer>> sol_position = c1.existsSolution("action");
		int pos_action = (int)sol_position.get(0).get(0);
		sol_position.get(0).remove(0);
		ArrayList<Integer> hierarchy_pos = sol_position.get(0);
		*/
		
		// insert a sub-solution for the solution "mix"
		c2.addSolutionComponent(pos_action, (Object)"salt", "ingredient", "String", hierarchy_pos);
		
		// insert another sub-solution for "mix"
		c2.addSolutionComponent(pos_action, (Object)"meat", "ingredient", "String", hierarchy_pos);
		
		// insert a sub-sub-solution (200g)
		//hierarchy_pos.add(1); // add to the second sub-solution (meat)
		//c2.addSolutionComponent(pos_action, (Object) 200, "quantity (grams)", "int", hierarchy_pos);
		
		Similarity sim = new Similarity(lib);
		//Retain r = new Retain(sim, lib);
		//r.retain(c3);
		
		sim.evaluateAccuracy(c1, c2);
		
	}
	
	public static void main(String[] args) {
		
		new TestRetain();
	}
}
