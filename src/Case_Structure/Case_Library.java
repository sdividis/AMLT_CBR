package Case_Structure;

import java.util.ArrayList;

/**
 * Case Library structure. In charge of storing and managing all the CBR cases and any
 * of their related information.
 * 
 * @author Marc Bola–os
 *
 */
public class Case_Library {

	private ArrayList<Case> case_list;
	private ArrayList<Domain> domain_list;
	
	public Case_Library(){
		case_list = new ArrayList<Case>();
		domain_list = new ArrayList<Domain>();
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
	 * @param dom
	 * @param dom1
	 */
	public void resetDomain(Domain domain, int pos) {
		domain_list.remove(pos);
		domain_list.add(pos, domain);
	}
	
}
