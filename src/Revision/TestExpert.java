package Revision;

import java.util.ArrayList;
import Case_Structure.Case;

public class TestExpert extends Expert {

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
