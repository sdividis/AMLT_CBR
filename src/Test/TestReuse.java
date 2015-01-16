package Test;

import java.util.ArrayList;

import Case_Structure.Case;
import Case_Structure.Case_Library;
import Case_Structure.Domain;
import Retrieval.Similarity;
import Reuse.Reuse;

public class TestReuse {

	public TestReuse()
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
		
		////// Now we create some examples Cases for this Domain
		// CASE 1
		Case c1 = new Case(dom1);
		c1.addAttribute((Object) "meat", "ingredient1", "String");
		c1.addAttribute((Object) "shit", "ingredient2", "String");
		c1.addAttribute((Object) "true", "fried", "Boolean");
		c1.addAttribute((Object) "150.0", "time", "Float");
		lib.addCase(c1);
		
		////// Now we create some examples Cases for this Domain
		// CASE 2
		Case c2 = new Case(dom1);
		c2.addAttribute((Object) "fish", "ingredient1", "String");
		c2.addAttribute((Object) "shit", "ingredient2", "String");
		c2.addAttribute((Object) "true", "fried", "Boolean");
		c2.addAttribute((Object) "50.0", "time", "Float");
		lib.addCase(c2);
		
		////// Now we create some examples Cases for this Domain
		// CASE 3
		Case c3 = new Case(dom1);
		c3.addAttribute((Object) "fish", "ingredient1", "String");
		c3.addAttribute((Object) "shit", "ingredient2", "String");
		c3.addAttribute((Object) "false", "fried", "Boolean");
		c3.addAttribute((Object) "50.0", "time", "Float");
		
		Similarity sim = new Similarity(lib);
		Reuse r = new Reuse(sim, lib);
		r.reuse(c3);
		
	}
	
	public static void main(String[] args) {
		
		new TestReuse();
	}
}
