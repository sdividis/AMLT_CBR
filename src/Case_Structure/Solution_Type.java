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
	 * @return String data types hierarchical representation.
	 */
	public String getDataTypes(){
		String str = this.data_type;
		int size = this.component_list.size();
		if(size > 0){
			str = str + "<";
			for(int i = 0; i < size-1; i++){
				str = str + this.component_list.get(i).getDataTypes();
				str = str + ",";
			}
			str = str + this.component_list.get(size-1).getDataTypes();
			str = str + ">";
		}
		return str;
	}
	
	/*
	public ArrayList<Solution_Type> getComponents(){
		return component_list;
	}
	*/
	
}
