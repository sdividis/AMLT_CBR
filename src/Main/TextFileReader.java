package Main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Object used for reading and processing all the files that are used to describe
 * the cases in a dataset.
 * 
 * @author Marc Bola–os
 *
 */
public class TextFileReader {
	
	final static String ENCODING = "UTF-8";
	
	public TextFileReader(){}
	
	/**
	 * Reads all the lines in the file referenced by the path.
	 * 
	 * @param filepath String path to the file.
	 * @return ArrayList<String> with all the lines found in filepath.
	 */
	public ArrayList<String> readTextFile(String filepath) {
		ArrayList<String> list = new ArrayList<String>();
		try{
			Scanner s = new Scanner(new File(filepath));
			while (s.hasNextLine()){
			    list.add(s.nextLine());
			}
			s.close();
		} catch (IOException e) {
			System.err.println("Error: reading file " + filepath);
		}
		return list;
	}
	
	/**
	 * Splits the elements separated by ',' in a single dataset line.
	 * 
	 * @param line String with comma-separated elements.
	 * @return ArrayList of elements or empty if there was some format error.
	 */
	public ArrayList<String> splitCommas(String line){
		ArrayList<String> list = new ArrayList<String>();
		
		String acum_string = "";
		int len = line.length();
		int open = 0;
		char c;
		for(int i = 0; i < len; i++){
			c = line.charAt(i);
			if(c == '<'){
				open++;
			} else if(c == '>'){
				open--;
			}
			// Wrong "<...>" format
			if(open < 0){
				list = new ArrayList<String>();
				break;
			}
			
			if(c == ',' && open == 0){
				list.add(acum_string);
				acum_string = "";
			} else {
				acum_string += c;
			}
		}
		
		if(open == 0){
			list.add(acum_string);
		} else if(open > 0){ // Wrong "<...>" format
			list = new ArrayList<String>();
		}
		
		return list;
	}
	
	/**
	 * Splits all the inner elements in a single solution. Returns the main element as the first
	 * position in the ArrayList and the sub-elements in the following ones.
	 * 
	 * @param line String with a single solution.
	 * @return ArrayList<String> with the main element in the first position and the sub-elements
	 * 		in the following ones. If the array is empty means that there was a format error.
	 */
	public ArrayList<String> getInnerSolutions(String line){
		ArrayList<String> list = new ArrayList<String>();
		
		boolean first = false;
		
		String acum_string = "";
		int nChars = line.length();
		int open = 0;
		char c;
		for(int i = 0; i < nChars; i++){
			c = line.charAt(i);
			if (c == '<'){
				// Found main solution, will continue for sub-solutions
				if(open == 0 && !first){
					first = true;
					if(acum_string.length() == 0){
						break;
					}
					list.add(acum_string);
					acum_string = "";
				}
				open++;
			} else if (c == '>'){
				open--;
			}
			acum_string += c;
			
			// Wrong "<...>" format
			if(first && open == 0 && i < (nChars-1)){
				list = new ArrayList<String>();
				break;
			}
		}
		
		// Extract sub_solutions
		if(first && open == 0){
			if(acum_string.length() > 0){
				acum_string = acum_string.substring(1, acum_string.length()-1);
				ArrayList<String> sub_list = this.splitCommas(acum_string);
				if(sub_list.size() == 0){
					list = new ArrayList<String>();
				} else {
					for(int i = 0; i < sub_list.size(); i++){
						list.add(sub_list.get(i));
					}
				}
			}
		}
		
		// Single solution without sub-solutions
		if(!first && nChars > 0){
			list.add(acum_string);
		}
		
		return list;
	}
	
}
