package Case_Structure;

import java.util.ArrayList;

/**
 * Basic structure for a single general Case-Based Reasoning case.
 * 
 * @author Marc Bola�os
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
	
	// Number of attributes
	private int numAtributes;
	
	// List for storing all the solution names matches in an existsSolution() search
	private ArrayList<ArrayList<Integer>> list_found_solutions;
	
	public Case(String domain){
		this.domain = domain;
		attribute_value_list = new ArrayList<Object>();
		attribute_name_list = new ArrayList<String>();
		attribute_type_list = new ArrayList<String>();
		solution_list = new ArrayList<Solution>();
		solution_type_list = new ArrayList<Solution_Type>();
	}
	
	public String getDomain(){
		return domain;
	}
	
	/**
	 * Checks if the case is from the domain passed by parameter.
	 * @param name String representing the domain.
	 * @return true or false.
	 */
	public boolean hasDomain(String name){
		return this.domain.equals(name);
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
	 * @return ArrayList<ArrayList<Integer>> with the hierarchical element positions or empty 
	 * 			if doesn't exist any. Here are some examples of each of the ArrayList<Integer> 
	 * 			instances.
	 * 			Example1: [3, 2, 0]: level0: solution3, level1: component2, level2: component0
	 * 			Example2: [0, 1]: level0: solution0, level1: component1
	 */
	public ArrayList<ArrayList<Integer>> existsSolution(String name){
		list_found_solutions = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> position = new ArrayList<Integer>();
		int size = solution_list.size();
		for(int i = 0; i < size; i++){
			ArrayList<Object> val_names  = solution_list.get(i).getValuesAndNames();
			position = new ArrayList<Integer>();
			position.add(i);
			existsSolutionRecursive(name, val_names, position);
		}
		return list_found_solutions;
	}
	
	/**
	 * Recursive function for extracting all the occurrences of a given solution name.
	 */
	private void existsSolutionRecursive(String name, ArrayList<Object> val_names, ArrayList<Integer> position){
		boolean equal = false;
		try{
			String this_name = (String)val_names.get(1);
			equal = this_name.equals(name);
		}catch(Exception e){}
		if(!equal){
			int size = val_names.size();
			for(int i = 2; i < size; i++){
				ArrayList<Object> sub_val_names = (ArrayList<Object>)val_names.get(i);
				position.add(i-2);
				existsSolutionRecursive(name, sub_val_names, position);
				position.remove(position.size()-1);
			}
		} else {
			// Element found! Add this solution to the list.
			ArrayList<Integer> pos = (ArrayList<Integer>)position.clone();
			list_found_solutions.add(pos);
		}
	}
	
	
	/**
	 * Returns a single attribute element.
	 * 
	 * @param i int of the attribute position returned.
	 * @return ArrayList<Object> attribute element with the format: [value, name, type]
	 */
	public ArrayList<Object> getAttribute(int i){
		ArrayList<Object> element = new ArrayList<Object>();
		element.add(attribute_value_list.get(i));
		element.add(attribute_name_list.get(i));
		element.add(attribute_type_list.get(i));
		return element;
	}
	
	public void setValue(int i, String value){
		attribute_value_list.set(i, value);
	}
	
	/**
	 * Returns all the information from a solution and its sub-components given
	 * a hierarchy of indices (see existsSolution());
	 * 
	 * @param hierarchy see existsSolution()
	 * @return ArrayList<Object> composed by two ArrayLists, the first one with 
	 * 			the value-name pairs and their subcomponents, and the second
	 * 			with the data types and their subcomponents.
	 */
	public ArrayList<Object> getSolution(ArrayList<Integer> hierarchy){
		ArrayList<Object> element = new ArrayList<Object>();
		element.add(solution_list.get(hierarchy.get(0)).getValuesAndNames(hierarchy));
		element.add(solution_type_list.get(hierarchy.get(0)).getDataTypes(hierarchy));
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
	 * @return int position in which the solution has been inserted.
	 */
	public int addSolution(Object value, String name, String type){
		solution_list.add(new Solution(value, name));
		solution_type_list.add(new Solution_Type(type));
		return solution_list.size()-1;
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
	 * @return inserted_pos with the integer position where the new solution has been inserted.
	 */
	public int addSolutionComponent(int position, Object value, String name, String type, ArrayList<Integer> hierarchy){
		Solution sol = solution_list.get(position);
		Solution_Type sol_type = solution_type_list.get(position);
		int inserted_pos = sol.addComponent(value, name, hierarchy);
		sol_type.addComponent(type, hierarchy);
		solution_list.remove(position);
		solution_list.add(position, sol);
		solution_type_list.remove(position);
		solution_type_list.add(position, sol_type);
		return inserted_pos;
	}
	
	/**
	 * Removes the attribute stored in the position passed by parameter and all its related information.
	 * 
	 * @param position int with the position provided by existsAttribute()
	 */
	public void removeAttribute(int position){
		if(position < attribute_value_list.size() && position >= 0){
			attribute_value_list.remove(position);
			attribute_name_list.remove(position);
			attribute_type_list.remove(position);
		} else {
			throw new IndexOutOfBoundsException("Position " + position + " does not exist in attributes list.");
		}
	}
	
	/**
	 * Removes the solution stored in the position passed by parameter as a hierarchical
	 * index (see existsSolution()) and all its related information.
	 * 
	 * @param hierarchy ArrayList<Integer> with the hierarchical position provided by existsSolution()
	 */
	public void removeSolution(ArrayList<Integer> hierarchy){
		if(!hierarchy.isEmpty()){
			// level 0
			if(hierarchy.size() == 1){
				solution_type_list.remove(hierarchy.get(0));
				solution_list.remove(hierarchy.get(0));
			// any inner level
			} else {
				int pos = (int) hierarchy.get(0);
				// Retrieves selected solution
				Solution_Type sol_type = solution_type_list.get(pos);
				Solution sol = solution_list.get(pos);
				// Deletes it from the list
				solution_type_list.remove(pos);
				solution_list.remove(pos);
				// Modifies its inner solution components
				hierarchy.remove(0);
				sol_type.removeComponent(hierarchy);
				sol.removeComponent(hierarchy);
				// Adds again the elements after the modifications
				solution_type_list.add(pos, sol_type);
				solution_list.add(pos, sol);
			}
		}
	}
	
	/**
	 * Returns the number of attributes in this case.
	 * @return number of attributes belonging to the case.
	 */
	public int getNumAttributes(){
		return this.attribute_value_list.size();
	}
	
	/**
	 * Returns the number of solutions in this case (without counting sub-solutions).
	 * @return number of solutions belonging to the case.
	 */
	public int getNumSolutions(){
		return this.solution_list.size();
	}
	
	/**
	 * String representation of a Case.
	 */
	public String toString(){
		String str = "[ --- CASE --- ]\n";
		str += "[ DOMAIN ]\n";
		str += domain.toString();
		str += "\n[ END DOMAIN ]\n";
		str += "[ ATTRIBUTES ]\n";
		int size = attribute_value_list.size();
		for(int i = 0; i < size; i++){
			str += "  <" + "N_" + attribute_name_list.get(i) + " V_" + attribute_value_list.get(i).toString() + " T_" + attribute_type_list.get(i) + ">\n";
		}
		str += "[ END ATTRIBUTES ]\n";
		str += "[ SOLUTIONS ]\n";
		size = solution_list.size();
		for(int i = 0; i < size; i++){
			str += solution_list.get(i).toString() + "\n";
			str += solution_type_list.get(i).toString() + "\n";
		}
		str += "[ END SOLUTIONS ]\n";
		str += "[ --- END CASE --- ]\n";
		return str;
	}
	
	/**
	 * Copies the solution from case c into the this case.
	 * @param c Case to get the solution from
	 */
	public void copySolution(Case c) {
		this.solution_type_list = c.solution_type_list;
		this.solution_list = c.solution_list;
	}

}
