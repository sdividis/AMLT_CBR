package Test;

import java.util.ArrayList;

import Case_Structure.*;

/**
 * Class used for testing all the classes in the Case Base Library structure.
 * 
 * @author Marc Bola–os
 *
 */
public class TestCBLibrary {

	public Case_Library lib;
	
	public TestCBLibrary(){
		lib = new Case_Library();
		
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
		dom.addAttributeName("ingredient");
		/////// Add solution names
		dom.addSolutionType("action");
		dom.addSolutionType("ingredient");
		dom.addSolutionType("quantity (grams)");
		/////// Insert again the domain
		lib.resetDomain(dom, dom_pos);
		
		// PRINT DOMAIN INFO
		System.out.println(dom);
		
		////// Now we create some examples Cases for this Domain
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
		
		// PRINT CASE
		System.out.println(c1);
		
		/**
		 * Insert case into case library
		 */
		lib.addCase(c1);
		
		
		////// Find all solutions in c1
		int numSol = c1.getNumSolutions(); // get number of high level solutions
		ArrayList<ArrayList<Object>> solutions_info = new ArrayList<ArrayList<Object>>();
		// Store all the information [[value, name, subcomponents], [type, subcomponents]] 
		// from each solution in a position of the array.
		ArrayList<Integer> hierarchy = new ArrayList<Integer>();
		for(int i = 0; i < numSol; i++){
			hierarchy.add(i);
			solutions_info.add(c1.getSolution(hierarchy));
			hierarchy = new ArrayList<Integer>();
		}
		
		
		/////// Find all ingredients in CASE1 solutions
		ArrayList<ArrayList<Integer>> sol_position = c1.existsSolution("ingredient");
		// PLOTS as an example, the hierarchical positions for all solutions named "ingredient"
		System.out.println(sol_position);
		
	}
	
}
