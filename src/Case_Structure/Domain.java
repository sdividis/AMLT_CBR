package Case_Structure;
import java.util.ArrayList;

/**
 * This class allows to create and maintain all the information relative
 * to a data domain.
 * 
 * @author Marc Bola–os
 *
 */
public class Domain {

	// Used as an unique id for classifying all the cases from the same domain.
	private String name;
	// List of existent attribute/solution types for the current Domain.
	private ArrayList<String> attribute_type_list;
	private ArrayList<String> solution_type_list;
	
	public Domain(String n){
		name = n;
	}
	
	/**
	 * Checks if the name passed by parameter matches the domain name.
	 * @param name String.
	 * @return true if it matches or false if it doesn't.
	 */
	public boolean isDomain(String name){
		return this.name.equals(name);
	}
	
	public ArrayList<String> getAttributeTypeList(){
		return attribute_type_list;
	}
	
	public ArrayList<String> getSolutionTypeList(){
		return solution_type_list;
	}
	
	/**
	 * Adds an attribute type previously checking if it exists. 
	 * @param t String identifying the attribute type.
	 * @return false if already exists or true if has been successfully added.
	 */
	public boolean addAttributeType(String t){
		boolean exists = checkAttributeExists(t);
		if(!exists){
			attribute_type_list.add(t);
		}
		return !exists;
	}
	
	/**
	 * Adds a solution type previously checking if it exists. 
	 * @param t String identifying the solution type.
	 * @return false if already exists or true if has been successfully added.
	 */
	public boolean addSolutionType(String t){
		boolean exists = checkSolutionExists(t);
		if(!exists){
			solution_type_list.add(t);
		}
		return !exists;
	}
	
	/**
	 * Check if a given attribute type exists.
	 * @param t String identifying the attribute type.
	 * @return true if it exists or false if doesn't exist.
	 */
	public boolean checkAttributeExists(String t){
		boolean found = false;
		int i = 0; int size = attribute_type_list.size();
		while(!found && i < size){
			found = this.attribute_type_list.get(i).equals(t);
			i++;
		}
		return found;
	}
	
	/**
	 * Check if a given solution type exists.
	 * @param t String identifying the solution type.
	 * @return true if it exists or false if doesn't exist.
	 */
	public boolean checkSolutionExists(String t){
		boolean found = false;
		int i = 0; int size = solution_type_list.size();
		while(!found && i < size){
			found = this.solution_type_list.get(i).equals(t);
			i++;
		}
		return found;
	}
	
}
