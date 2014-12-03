package Case_Structure;

import java.util.ArrayList;

/**
 * Basic structure for a single general Case-Based Reasoning case.
 * 
 * @author Marc Bola–os
 *
 */
public class Case {
	
	// String referring to the unique identification of a Domain in the Case Library Structure.
	private String domain;
	// List of attribute values (non-fixed size but equal to attribute_type_list and attribute_name_list)
	private ArrayList<Object> attribute_value_list;
	// List of attribute unique domain names/identifiers (non-fixed size but equal to attribute_name_list)
	private ArrayList<String> attribute_name_list;
	// String representation of real Java data types
	private ArrayList<String> attribute_type_list;
	// List of ordered or non-ordered (depending on the domain) solutions 
	// (non-fixed size but equal to solution_type_list)
	private ArrayList<Solution> solution_list;
	private ArrayList<Solution_Type> solution_type_list;
	
	
	public Case(String domain){
		this.domain = domain;
	}
	
	public String getDomain(){
		return domain;
	}
	
	/**
	 * Checks if exists any attribute with the given name/identifier.
	 * 
	 * @param name String attribute unique name/identifier.
	 * @return int with the element position or -1 if it doesn't exist.
	 */
	public int existsAttribute(String name){
		boolean found = false;
		int i = 0; int size = attribute_name_list.size();
		while(!found && i < size){
			found = attribute_name_list.get(i).equals(name);
			i++;
		}
		
		if(found){
			i--;
		} else {
			i = -1;
		}
		
		return i;
	}
	
	/**
	 * Checks if exists any solution with the given name/identifier (see Solution.getValuesAndNames())
	 * 
	 * @param name String solution unique name/identifier.
	 * @return ArrayList<Integer> with the hierarchical element position or empty if it doesn't exist.
	 * 			Example1: [3, 2, 0]: level0: solution3, level1: component2, level2: component0
	 * 			Example2: [0, 1]: level0: solution0, level1: component1
	 */
	public ArrayList<Integer> existsSolution(String name){
		ArrayList<Integer> position = new ArrayList<Integer>();
		boolean found = false;
		int i = 0; int size = solution_list.size();
		while(!found && i < size){
			ArrayList<Object> val_names  = solution_list.get(i).getValuesAndNames();
			position = existsSolutionRecursive(name, val_names, new ArrayList<Integer>());
			if(!position.isEmpty()){
				found = true;
				position.remove(0);
				position.add(0,i);
			} else {
				i++;
			}
		}
		return position;
	}
	
	private ArrayList<Integer> existsSolutionRecursive(String name, ArrayList<Object> val_names, ArrayList<Integer> position){
		String this_name = (String)val_names.get(2);
		int len_pos = position.size();
		if(!this_name.equals(name)){
			int size = val_names.size();
			int i = 2;
			boolean found = false;
			while(!found && i < size){
				ArrayList<Object> sub_val_names = (ArrayList<Object>)val_names.get(i);
				position = existsSolutionRecursive(name, sub_val_names, position);
				if(position.size() > len_pos){
					found = true;
					position.remove(len_pos);
					position.add(len_pos, i-2);
					position.add(len_pos, -1);
				} else {
					i++;
				}
			}
		} else {
			position.add(-1);
		}
		return position;
	}
	
	
	/**
	 * Returns a single attribute element.
	 * 
	 * @param i int of the attribute position returned.
	 * @return ArrayList<Object> attribute element with the format: [value, name, type]
	 */
	public ArrayList<Object> getElementAttribute(int i){
		ArrayList<Object> element = new ArrayList<Object>();
		element.add(attribute_value_list.get(i));
		element.add(attribute_name_list.get(i));
		element.add(attribute_type_list.get(i));
		return element;
	}
	
	/*
	public ArrayList<Object> getAttributeValues(){
		return attribute_value_list;
	}
	
	public ArrayList<String> getAttributeNames(){
		return attribute_name_list;
	}
	
	public ArrayList<String> getAttributeTypes(){
		return attribute_type_list;
	}
	*/
	
	/**
	 * Adds a new attribute to the case. We must previously check that the attribute
	 * name exists in the current domain list of attribute_names.
	 * 
	 * @param value Object value of this attribute.
	 * @param name String unique name/identification of this kind of attribute in the current domain.
	 * @param type String representation of the Java data type.
	 */
	public void addAttribute(Object value, String name, String type){
		attribute_value_list.add(value);
		attribute_name_list.add(name);
		attribute_type_list.add(type);
	}
	
	/**
	 * Adds a new solution to the case. We must previously check that the solution
	 * name exists in the current domain list of solution_names.
	 * 
	 * @param value Object value of this solution.
	 * @param name String unique name/identification of this kind of solution in the current domain.
	 * @param type String representation of the Java data type.
	 */
	public void addSolution(Object value, String name, String type){
		solution_list.add(new Solution(value, name));
		solution_type_list.add(new Solution_Type(type));
	}
	
	/**
	 * Adds a new solution component to the case. We must previously check that the solution
	 * name exists in the current domain list of solution_names.
	 * 
	 * @param position int identifying the solution where we will add the component.
	 * @param value Object value of this solution.
	 * @param name String unique name/identification of this kind of solution in the current domain.
	 * @param type String representation of the Java data type.
	 * @param hierarchy ArrayList<Integer> with the inner component position (see Solution.addComponent(...))
	 */
	public void addSolutionComponent(int position, Object value, String name, String type, ArrayList<Integer> hierarchy){
		Solution sol = solution_list.get(position);
		Solution_Type sol_type = solution_type_list.get(position);
		sol.addComponent(value, name, hierarchy);
		sol_type.addComponent(type, hierarchy);
		solution_list.remove(position);
		solution_list.add(position, sol);
		solution_type_list.remove(position);
		solution_type_list.add(position, sol_type);
	}
	

}
