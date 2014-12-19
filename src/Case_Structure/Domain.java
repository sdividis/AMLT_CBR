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
	// List of existent attribute/solution names/identifiers for the current Domain.
	private ArrayList<String> attribute_name_list;
	private ArrayList<String> solution_name_list;
	
	public Domain(String n){
		name = n;
		attribute_name_list = new ArrayList<String>();
		solution_name_list = new ArrayList<String>();
	}
	
	/**
	 * Checks if the name passed by parameter matches the domain name.
	 * @param name String.
	 * @return true if it matches or false if it doesn't.
	 */
	public boolean isDomain(String name){
		return this.name.equals(name);
	}
	
	public String getName(){
		return name;
	}
	
	public ArrayList<String> getAttributeNameList(){
		return attribute_name_list;
	}
	
	public ArrayList<String> getSolutionNameList(){
		return solution_name_list;
	}
	
	/**
	 * Adds an attribute name previously checking if it exists. 
	 * @param t String identifying the attribute name.
	 * @return false if already exists or true if has been successfully added.
	 */
	public boolean addAttributeName(String t){
		boolean exists = existsAttribute(t);
		if(!exists){
			attribute_name_list.add(t);
		}
		return !exists;
	}
	
	/**
	 * Adds a solution name previously checking if it exists. 
	 * @param t String identifying the solution name.
	 * @return false if already exists or true if has been successfully added.
	 */
	public boolean addSolutionType(String t){
		boolean exists = existsSolution(t);
		if(!exists){
			solution_name_list.add(t);
		}
		return !exists;
	}
	
	/**
	 * Check if a given attribute name exists.
	 * @param t String identifying the attribute name.
	 * @return true if it exists or false if doesn't exist.
	 */
	public boolean existsAttribute(String t){
		boolean found = false;
		int i = 0; int size = attribute_name_list.size();
		while(!found && i < size){
			found = this.attribute_name_list.get(i).equals(t);
			i++;
		}
		return found;
	}
	
	/**
	 * Check if a given solution name exists.
	 * @param t String identifying the solution name.
	 * @return true if it exists or false if doesn't exist.
	 */
	public boolean existsSolution(String t){
		boolean found = false;
		int i = 0; int size = solution_name_list.size();
		while(!found && i < size){
			found = this.solution_name_list.get(i).equals(t);
			i++;
		}
		return found;
	}
	
	/**
	 * String representation of a Domain instance.
	 */
	public String toString(){
		String str = "Domain name: " + this.name +"\n";
		// Print attributes
		str += "Attributes: ";
		int size = attribute_name_list.size();
		if(size > 0){
			for(int i = 0; i < size-1; i++){
				str += attribute_name_list.get(i) +",";
			}
			str += attribute_name_list.get(size-1) +"\n";
		}
		// Print solutions
		str += "Solutions: ";
		size = solution_name_list.size();
		if(size > 0){
			for(int i = 0; i < size-1; i++){
				str += solution_name_list.get(i) +",";
			}
			str += solution_name_list.get(size-1) +"\n";
		}
		return str;
	}
	
}
