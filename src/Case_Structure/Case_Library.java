package Case_Structure;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Main.TextFileReader;

/**
 * Case Library structure. In charge of storing and managing all the CBR cases and any
 * of their related information.
 * 
 * @author Marc Bola�os
 *
 */
public class Case_Library {

	private ArrayList<Case> case_list;
	private ArrayList<Domain> domain_list;
	// ArrayList ready for storing a set of cases for testing the CBR implementation.
	private ArrayList<Case> test_case_list;
	
	public Case_Library(){
		case_list = new ArrayList<Case>();
		domain_list = new ArrayList<Domain>();
		test_case_list = new ArrayList<Case>();
	}
	
	/**
	 * Inserts the given case to the end of the list of cases.
	 */
	public void addCase(Case new_case){
		case_list.add(new_case);
	}
	
	/**
	 * Inserts the given case to the position given.
	 */
	public void addCase(Case new_case, int pos){
		case_list.add(pos, new_case);
	}
	
	/**
	 * Removes the case stored in the position passed by parameter.
	 */
	public void removeCase(int pos){
		case_list.remove(pos);
	}
	
	
	/**
	 * Returns the position where the given domain is stored, or -1 if it does not exist.
	 * @param name String domain name.
	 */
	public int existsDomain(String name){
		boolean found = false;
		int i = 0; int size = domain_list.size();
		while(!found && i < size){
			if(domain_list.get(i).isDomain(name)){
				found = true;
			} else {
				i++;
			}
		}
		if(!found){
			i = -1;
		}
		return i;
	}
	
	/**
	 * Adds a new domain to the list.
	 * @param name
	 */
	public void addDomain(String name){
		domain_list.add(new Domain(name));
	}
	
	
	
	public Domain getDomain(String name) throws Exception{
		int pos = this.existsDomain(name);
		Domain dom = new Domain("");
		if(pos == -1){
			throw new Exception("The given domain does not exist.");
		} else {
			dom = domain_list.get(pos);
		}
		return dom;
	}
	
	public Domain getDomain(int pos){
		return domain_list.get(pos);
	}
	
	public int getNumCases(){
		return case_list.size();
	}
	
	/**
	 * Returns the Case in the given position.
	 * 
	 * @param pos int.
	 * @return Case instance.
	 */
	public Case getCase(int pos){
		return case_list.get(pos);
	}
	
	public void setCase(int pos, Case c){
		case_list.set(pos, c);
	}
	
	/**
	 * Returns the k most similar cases to the new case passed by parameter.
	 * 
	 * @param new_case Case.
	 * @param k int identifying the number of cases that will be returned.
	 */
	public ArrayList<Case> getMostSimilarCases(Case new_case, int k){
		ArrayList<Case> list_cases = new ArrayList<Case>();
		// TODO: by David
		return list_cases;
	}

	/**
	 * Resets the domain in the given position for the one passed by parameter.
	 * 
	 * @param domain Domain object.
	 * @param pos int domain position for which it will be substituted.
	 */
	public void resetDomain(Domain domain, int pos) {
		domain_list.remove(pos);
		domain_list.add(pos, domain);
	}
	
	
	/**
	 * Reads all the information contained in a new DATASET
	 * 
	 * @param path String with the path to the stored dataset (see file datasets/README.txt)
	 */
	public void readDataset(String path){
		
		TextFileReader reader = new TextFileReader();
		int id_domain;
		
		// Read domain name
		ArrayList<String> lines_domain = reader.readTextFile(path + "/domain.txt");
		if(lines_domain.size()==1){
			id_domain = this.existsDomain(lines_domain.get(0));
			// Create domain if it didn't exist
			if(id_domain == -1){
				this.addDomain(lines_domain.get(0));
				id_domain = this.existsDomain(lines_domain.get(0));
			}
			
			// Read names name
			ArrayList<String> lines_names = reader.readTextFile(path + "/names.txt");
			// Read values name
			ArrayList<String> lines_values = reader.readTextFile(path + "/values.txt");
			// Read types name
			ArrayList<String> lines_types = reader.readTextFile(path + "/types.txt");
			// Read att-sol name
			ArrayList<String> lines_att_sol = reader.readTextFile(path + "/att_sol.txt");
			
			// Correct number of lines
			int nLines = lines_names.size();
			if(nLines == lines_values.size() && nLines == lines_types.size() && nLines == lines_att_sol.size()){
				// Create and insert each new case
				for(int i = 0; i < nLines; i++){
					this.createCase(id_domain, lines_values.get(i), lines_names.get(i), lines_types.get(i), lines_att_sol.get(i), reader, i);
				}
			} else {
				System.err.println("Error: number of lines in the names, values, types and att-sol files do not match.");
			}
			
		} else {
			System.err.println("Error: incorrect domain.txt file.");
		}
	}

	/**
	 * Creates a new case using the text file String information passed 
	 * by parameter adds it into the Case_Library. 
	 * 
	 * @param domain_pos int with the position of the domain in the domain_list.
	 * @param values separated by commas.
	 * @param names abstract names separated by commas.
	 * @param types Java types string representation separated by commas.
	 * @param att_sol 'a' or 's' indicating if each element is an attribute or a solution.
	 * @param reader TextReader for managing the formated data.
	 * @param line_pos int with the line position
	 */
	private void createCase(int domain_pos, String values, String names, String types, String att_sol, TextFileReader reader, int line_pos) {
		
		Domain domain = this.getDomain(domain_pos);
		Case new_case = new Case(domain.getName());
		String name;
		boolean error = false;
		
		ArrayList<String> values_list = reader.splitCommas(values);
		if(values_list.size() == 0){
			System.err.println("Error: in values, line " + line_pos+1 + ".");
			error = true;
		}
		ArrayList<String> names_list = reader.splitCommas(names);
		if(names_list.size() == 0){
			System.err.println("Error: in names, line " + line_pos+1 + ".");
			error = true;
		}
		ArrayList<String> types_list = reader.splitCommas(types);
		if(types_list.size() == 0){
			System.err.println("Error: in types, line " + line_pos+1 + ".");
			error = true;
		}
		ArrayList<String> att_sol_list = reader.splitCommas(att_sol);
		if(att_sol_list.size() == 0){
			System.err.println("Error: in att_sol, line " + line_pos+1 + ".");
			error = true;
		}
		
		int nElems = values_list.size();
		if(nElems == names_list.size() && nElems == types_list.size() && nElems == att_sol_list.size()){
		
			// Go over each element in the lists
			for(int i = 0; i < nElems; i++){
				if(att_sol_list.get(i).equals("a")){
					/** ATTRIBUTE */
					name = names_list.get(i);
					// Create attribute in domain if it doesn't exist
					if(!domain.existsAttribute(name)){
						domain.addAttributeName(name);
					}
					// Insert attribute into Case
					new_case.addAttribute(values_list.get(i), name, types_list.get(i));
				} else if(att_sol_list.get(i).equals("s")){
					/** SOLUTION */
					ArrayList<String> inner_values = reader.getInnerSolutions(values_list.get(i));
					if(inner_values.size() == 0){
						System.err.println("Error: in inner_values, line " + line_pos+1 + ", elem " + i+1 + ".");
						error = true;
					}
					ArrayList<String> inner_names = reader.getInnerSolutions(names_list.get(i));
					if(inner_names.size() == 0){
						System.err.println("Error: in inner_names, line " + line_pos+1 + ", elem " + i+1 + ".");
						error = true;
					}
					ArrayList<String> inner_types = reader.getInnerSolutions(types_list.get(i));
					if(inner_types.size() == 0){
						System.err.println("Error: in inner_types, line " + line_pos+1 + ", elem " + i+1 + ".");
						error = true;
					}
					// Create solution in domain if it doesn't exist
					if(!domain.existsSolution(inner_names.get(0))){
						domain.addSolutionType(inner_names.get(0));
					}
					// Insert solution in case
					int sol_pos = new_case.addSolution(inner_values.get(0), inner_names.get(0), inner_types.get(0));
					// Insert sub-solutions
					for(int j = 1; j < inner_values.size(); j++){
						ArrayList<Object> result = this.createCaseSubsolutions(inner_values.get(j), inner_names.get(j), inner_types.get(j), new ArrayList<Integer>(), domain, new_case, sol_pos, reader);
						domain = (Domain)result.get(0);
						new_case = (Case)result.get(1);
					}
				} else {
					System.err.println("Error: unknown attribute-solution identifier in line " + line_pos+1 + ", elem " + i + ".");
					error = true;
				}
			}
			
		} else {
			System.err.println("Error: number of elements not matching in line " + line_pos+1 + ".");
			error = true;
		}
		
		// If everything went OK, then we add the new case and we update the Domain
		if(!error){
			this.resetDomain(domain, domain_pos);
			this.addCase(new_case);
		}
	}
	
	
	/**
	 * This recursive method adds all the sub-solutions in the corresponding case.
	 * 
	 * @param value solution value String representation to add.
	 * @param name solution name String representation to add.
	 * @param type solution type String representation to add.
	 * @param hierarchy ArrayList<Integer> with the position of the current sub-solution in the hierarchy.
	 * @param domain Domain that the case belongs to.
	 * @param new_case Case being created.
	 * @param sol_pos int position of the main solution where the sub-solutions will be added.
	 * @param reader TextFileReader for reading the String representations of value, name and type.
	 * @return ArrayList<Object> with the following format: [DOMAIN, CASE]
	 */
	private ArrayList<Object> createCaseSubsolutions(String value, String name, String type, ArrayList<Integer> hierarchy, Domain domain, Case new_case, int sol_pos, TextFileReader reader){
		
		/** SUB-SOLUTION */
		ArrayList<String> inner_values = reader.getInnerSolutions(value);
		ArrayList<String> inner_names = reader.getInnerSolutions(name);
		ArrayList<String> inner_types = reader.getInnerSolutions(type);
		
		// Create solution in domain if it doesn't exist
		if(!domain.existsSolution(inner_names.get(0))){
			domain.addSolutionType(inner_names.get(0));
		}
		// Insert solution in case
		int inserted_pos = new_case.addSolutionComponent(sol_pos, inner_values.get(0), inner_names.get(0), inner_types.get(0), hierarchy);
		hierarchy.add(inserted_pos);
		// Insert sub-solutions
		for(int j = 1; j < inner_values.size(); j++){
			ArrayList<Object> result = this.createCaseSubsolutions(inner_values.get(j), inner_names.get(j), inner_types.get(j), hierarchy, domain, new_case, sol_pos, reader);
			domain = (Domain)result.get(0);
			new_case = (Case)result.get(1);
		}
		
		// Prepare the return data
		ArrayList<Object> result = new ArrayList<Object>();
		result.add(domain);
		result.add(new_case);
		return result;
	}
	
}
