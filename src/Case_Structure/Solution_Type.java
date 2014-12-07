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
public class Solution_Type {

	// String representation of a real Java data type.
	private String data_type;
	// Sub-solutions stored in this solution component.
	private ArrayList<Solution_Type> component_list;
	
	public Solution_Type(String type){
		this.data_type = type;
	}
	
	public ArrayList<Solution_Type> getComponentList(){
		return this.component_list;
	}
	
	/**
	 * Adds a new data_type component at the position given by "position".
	 * @param type String data type.
	 * @param position ArrayList<Integer> with the hierarchical position desired 
	 * 					(each element is a position in the hierarchy). 
	 * 					If is empty then will be inserted in the top level.
	 */
	public void addComponent(String type, ArrayList<Integer> position){
		if(position.size()==0){
			this.component_list.add(new Solution_Type(type));
		} else {
			Solution_Type comp = component_list.get(position.get(0));
			ArrayList<Integer> new_pos = new ArrayList<Integer>(position);
			new_pos.remove(0);
			comp.addComponent(type, new_pos);
			// Maybe not needed because addComponent() already adds the value?
			//component_list.set(position.get(0), comp);
		}	
	}
	
	/**
	 * Recursively retrieves a String representation of all the data types for all the components.
	 * 
	 * @return ArrayList<Object> data types hierarchical representation.
	 */
	public ArrayList<Object> getDataTypes(){
		
		ArrayList<Object> str = new ArrayList<Object>();
		str.add(this.data_type);
		int size = this.component_list.size();
		if(size > 0){
			for(int i = 0; i < size; i++){
				str.add(this.component_list.get(i).getDataTypes());
			}
		}
		return str;
	}
	
	/**
	 * Returns all the solution data types in an ArrayList keeping its 
	 * hierarchical structure using also encapsulated ArrayList<String> only from
	 * a given hierarchical point.
	 * 
	 * @param hierarchy ArrayList<Integer> list of integers describing the hierarchy of
	 * 					the first wanted solution point.
	 * 			Example1: [3, 2, 0]: level0: solution3, level1: component2, level2: component0
	 * 			Example2: [0, 1]: level0: solution0, level1: component1
	 * @return ArrayList<Object> with the list of solutions with the following format: 
	 * 				[value, name, component0, component1, ..., componentN]
	 */
	public ArrayList<Object> getDataTypes(ArrayList<Integer> hierarchy){
		int size = hierarchy.size();
		Solution_Type sol = this;
		for(int i = 1; i < size; i++){
			sol = sol.getComponentList().get(hierarchy.get(i));
		}
		return sol.getDataTypes();
	}
	
	/*
	public ArrayList<Solution_Type> getComponents(){
		return component_list;
	}
	*/
	
	/**
	 * Removes inner solution_type component stored in position given by "hierarchy" (see Case.existsSolution()).
	 */
	public void removeComponent(ArrayList<Integer> hierarchy){
		if(hierarchy.size() == 1){
			component_list.remove(hierarchy.get(0));
		// any inner level
		} else {
			int pos = (int)hierarchy.get(0);
			Solution_Type sol_type = component_list.get(hierarchy.get(0));
			component_list.remove(pos);
			hierarchy.remove(0);
			sol_type.removeComponent(hierarchy);
			component_list.add(pos, sol_type);
		}
	}
	
	/**
	 * String representation of a Solution_Type.
	 */
	public String toString(){
		String str = " < T_" + data_type;
		int size = component_list.size();
		for(int i = 0; i < size; i++){
			str += component_list.get(i).toString();
		}
		str += " > ";
		return str;
	}
	
}
