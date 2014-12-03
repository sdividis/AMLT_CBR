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
	// Name or identifier of the abstract solution type (e.g. ingredient, action, color, etc.)
	private String name;
	// Sub-solutions stored in this solution component.
	private ArrayList<Solution> component_list;
	
	public Solution(Object value, String name){
		this.value = value;
		this.name = name;
	}
	
	public ArrayList<Solution> getComponentList(){
		return this.component_list;
	}
	
	/**
	 * Adds a new solution component at the position given.
	 * @param value Object generic representation of the solution value.
	 * @param name String with the abstract value identifier/name.
	 * @param position ArrayList<Integer> with the hierarchical position desired 
	 * 					(each element is a position in the hierarchy). 
	 * 					If is empty then will be inserted in the top level.
	 */
	public void addComponent(Object value, String name, ArrayList<Integer> position){
		if(position.size()==0){
			this.component_list.add(new Solution(value, name));
		} else {
			Solution comp = component_list.get(position.get(0));
			ArrayList<Integer> new_pos = new ArrayList<Integer>(position);
			new_pos.remove(0);
			comp.addComponent(value, name, new_pos);
			// Maybe not needed because addComponent() already adds the value?
			//component_list.set(position.get(0), comp);
		}	
	}
	
	/**
	 * Returns all the solution values and names in an ArrayList keeping its 
	 * hierarchical structure using also encapsulated ArrayList<Object>.
	 * 
	 * @return ArrayList<Object> with the list of solutions with the following format: 
	 * 				[value, name, component0, component1, ..., componentN]
	 */
	public ArrayList<Object> getValuesAndNames(){
		ArrayList<Object> sol = new ArrayList<Object>();
		sol.add(value);
		sol.add(name);
		int size = this.component_list.size();
		if(size > 0){
			for(int i = 0; i < size; i++){
				sol.add(this.component_list.get(i).getValuesAndNames());
			}
		}
		return sol;
	}
	
	/**
	 * Returns all the solution values and names in an ArrayList keeping its 
	 * hierarchical structure using also encapsulated ArrayList<Object> only from
	 * a given hierarchical point.
	 * 
	 * @param hierarchy ArrayList<Integer> list of integers describing the hierarchy of
	 * 					the first wanted solution point.
	 * 			Example1: [3, 2, 0]: level0: solution3, level1: component2, level2: component0
	 * 			Example2: [0, 1]: level0: solution0, level1: component1
	 * @return ArrayList<Object> with the list of solutions with the following format: 
	 * 				[value, name, component0, component1, ..., componentN]
	 */
	public ArrayList<Object> getValuesAndNames(ArrayList<Integer> hierarchy){
		int size = hierarchy.size();
		Solution sol = this;
		for(int i = 1; i < size; i++){
			sol = sol.getComponentList().get(hierarchy.get(i));
		}
		return sol.getValuesAndNames();
	}
	
}
