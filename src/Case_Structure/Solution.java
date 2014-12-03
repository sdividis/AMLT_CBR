package Case_Structure;

import java.util.ArrayList;

/**
 * Object storing the information of a solution type (which must 
 * match to at least one of the domains' solution types). This means 
 * that the existence of this Solution_Type must be checked in the 
 * corresponding Domain before creating an object of this type. 
 * 
 * @author Marc Bola–os
 *
 */
public class Solution {

	// Generic object representation of any kind of solution value 
	// (its type is referenced in its equivalent Solution_Type)
	private Object value;
	// Sub-solutions stored in this solution component.
	private ArrayList<Solution> component_list;
	
	public Solution(Object value){
		this.value = value;
	}
	
	/**
	 * Adds a new solution component at the position given.
	 * @param value Object generic representation of the solution value.
	 * @param position ArrayList<Integer> with the hierarchical position desired 
	 * 					(each element is a position in the hierarchy). 
	 * 					If is empty then will be inserted in the top level.
	 */
	public void addComponent(Object value, ArrayList<Integer> position){
		if(position.size()==0){
			this.component_list.add(new Solution(value));
		} else {
			Solution comp = component_list.get(position.get(0));
			ArrayList<Integer> new_pos = new ArrayList<Integer>(position);
			new_pos.remove(0);
			comp.addComponent(value, new_pos);
			// Maybe not needed because addComponent() already adds the value?
			//component_list.set(position.get(0), comp);
		}	
	}
	
	
	public ArrayList<Solution> getSolution(){
		return component_list;
	}
	
	
}
