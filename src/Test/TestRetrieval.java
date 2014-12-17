package Test;

import java.util.ArrayList;
import java.util.List;

import Case_Structure.*;
import Retrieval.Similarity;

/**
 * Class used for testing all the classes in the Case Base Library structure.
 * 
 * @author Marc Bola�os
 *
 */
public class TestRetrieval {

	public Case_Library lib;
	private String dom1;
	
	public TestRetrieval(){
		lib = new Case_Library();
		
		// DOMAINS
		this.dom1 = "Cooking_Recipes";
		
		
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
		
		Case c1 = createCase1();
		Case c2 = createCase2();
		
		System.out.println("\n\nTarget case");
		Case targetCase = createTargetCase();
		
		/**
		 * Insert case into case library
		 */
		lib.addCase(c1);
		lib.addCase(c2);
		
		//Retrieval part
		Similarity sim = new Similarity(lib);
		ArrayList<Case> similarCases = sim.getSimilarCases(targetCase, lib.getNumCases());
		
	}
	
	
	public Case createCase1(){
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
		c1.addSolution("mix", "action", "String");
		
		// find the position of the solution named "action"
		ArrayList<ArrayList<Integer>> sol_position = c1.existsSolution("action");
		int pos_action = (int)sol_position.get(0).get(0);
		sol_position.get(0).remove(0);
		ArrayList<Integer> hierarchy_pos = sol_position.get(0);
		
		// insert a sub-solution for the solution "mix"
		c1.addSolutionComponent(pos_action, (Object)"salt", "ingredient", "String", hierarchy_pos);
		
		// insert another sub-solution for "mix"
		c1.addSolutionComponent(pos_action, (Object)"meat", "ingredient", "String", hierarchy_pos);
		
		// insert a sub-sub-solution (200g)
		hierarchy_pos.add(1); // add to the second sub-solution (meat)
		c1.addSolutionComponent(pos_action, (Object) 200, "quantity (grams)", "int", hierarchy_pos);
		
//		// PRINT CASE
		System.out.println(c1);
//		
//		/////// Find all ingredients in CASE1 solutions
//		sol_position = c1.existsSolution("ingredient");
//		// PLOTS as an example, the hierarchical positions for all solutions named "ingredient"
//		System.out.println(sol_position);
		
		return c1;
	}
	
	public Case createCase2(){
		Case c1 = new Case(dom1);
		
		
		/**
		 * Add some ingredients as attributes
		 */
		c1.addAttribute((Object) "milk", "ingredient", "String");
		c1.addAttribute((Object) "pepper", "ingredient", "String");
		c1.addAttribute((Object) "patatoes", "ingredient", "String");
		
		
		/**
		 * One of the solutions for our Recipe will be to mix salt and 200g of meat, so
		 * in a simplified way it will be: mix(salt,meat(200g))
		 */
		// add a general solution object
		c1.addSolution("mix", "action", "String");
		
		// find the position of the solution named "action"
		ArrayList<ArrayList<Integer>> sol_position = c1.existsSolution("action");
		int pos_action = (int)sol_position.get(0).get(0);
		sol_position.get(0).remove(0);
		ArrayList<Integer> hierarchy_pos = sol_position.get(0);
		
		// insert a sub-solution for the solution "mix"
		c1.addSolutionComponent(pos_action, (Object)"milk", "ingredient", "String", hierarchy_pos);
		
		// insert another sub-solution for "mix"
		c1.addSolutionComponent(pos_action, (Object)"meat", "ingredient", "String", hierarchy_pos);
		
		// insert a sub-sub-solution (200g)
		hierarchy_pos.add(1); // add to the second sub-solution (meat)
		c1.addSolutionComponent(pos_action, (Object) 200, "quantity (grams)", "int", hierarchy_pos);
		
		// PRINT CASE
		System.out.println(c1);
		
//		/////// Find all ingredients in CASE1 solutions
//		sol_position = c1.existsSolution("ingredient");
//		// PLOTS as an example, the hierarchical positions for all solutions named "ingredient"
//		System.out.println(sol_position);
		
		return c1;
	}
	
	
	
	
	public Case createTargetCase(){
		Case c1 = new Case(dom1);
		
		/**
		 * Add some ingredients as attributes
		 */
		c1.addAttribute((Object) "salt", "ingredient", "String");
		c1.addAttribute((Object) "tomatoes", "ingredient", "String");
		c1.addAttribute((Object) "meat", "ingredient", "String");

		// PRINT CASE
		System.out.println(c1);
		
		return c1;
	}
}
