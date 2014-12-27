package Adaptation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import Case_Structure.Case;
import Case_Structure.Case_Library;
import Main.TextFileReader;
import Retrieval.Similarity;

public class Adaptation {
	private Case_Library clib;
	// Diff knowledge
	private HashMap<String, String> diffKnowledge = new HashMap<String, String>();// Contains the info of the diff_knowledge file
	private HashMap<String, HashMap<String, String>> diffStringsInfo = new HashMap<String, HashMap<String, String>>(); // Contains a hashmap for every String attribute which has the info in its att_diff file
																													   // The format is "A-B", C
	// Join knowledge
	private HashMap<String, String> joinKnowledge = new HashMap<String, String>();// Contains the info of the diff_knowledge file
	private HashMap<String, HashMap<String, String>> joinStringsInfo = new HashMap<String, HashMap<String, String>>(); // Contains a hashmap for every String attribute which has the info in its att_diff file
																													   // The format is "A-B", C
	
	/** Create an Adaptation instance based on the path in which the knowledge is and the case base.
	 * 
	 * @param rulesPath path to the "AdaptationKnowledge" folder of the dataset
	 * @param lib Case library
	 */
	public Adaptation(String rulesPath, Case_Library lib) {
		clib = lib;
		parseAdaptationKnowledgeFile(rulesPath + "/diff_knowledge.txt", diffKnowledge, diffStringsInfo);
		parseAdaptationKnowledgeFile(rulesPath + "/join_knowledge.txt", joinKnowledge, joinStringsInfo);
	}
	
	/**
	 * 
	 * @param path Path to the knowledge info file (join or diff)
	 * @param knowledgeMap [out] Will be filled with the information from the file
	 * @param stringKnowledgeMap [out] Will be filled with the information from the strings files which are in the path file
	 */
	private void parseAdaptationKnowledgeFile(String path, HashMap<String, String> knowledgeMap,
											  HashMap<String, HashMap<String, String>> stringKnowledgeMap) {
		ArrayList<String> diff_lines = new TextFileReader().readTextFile(path);
		ArrayList<String[]> data = new ArrayList<String[]>();
		for (String line : diff_lines) {
			if (line.startsWith("#")) continue; // It is a comment
			data.add(line.split(","));
		}
		assert(data.size() == 2 && data.get(0).length == data.get(1).length); // We can only have two lines
		for (int i = 0 ; i < data.get(0).length; ++i) {
			knowledgeMap.put(data.get(0)[i], data.get(1)[i]);
			if (data.get(1)[i].length() > 1) { 
				HashMap<String, String> info = parseStringFile(path.substring(0, path.lastIndexOf("/")) + "/" + data.get(1)[i] + ".txt");
				stringKnowledgeMap.put(data.get(1)[i], info);
			}
		}
	}
	
	/** Parses a String attribute file
	 * 
	 * @param path Path to the file
	 * @return A HashMap containing "A-B" as key and C as parameter, being A,B two possible values and C the result of the operation
	 */
	private HashMap<String, String> parseStringFile(String path) {
		ArrayList<String> lines = new TextFileReader().readTextFile(path);
		ArrayList<String[]> data = new ArrayList<String[]>();
		HashMap<String, String> info = new HashMap<String, String>();
		for (String line : lines) {
			if (line.startsWith("#")) continue; // It is a comment
			data.add(line.split(","));
		}
		for (String[] d : data) {
			assert(d.length == 3);
			info.put(d[0]+"-"+d[1], d[2]);
		}
		return info;
	}
	
	/** 
	 * Adapts a case via the Null Adaptation method, by copying the solution of the case.
	 * 
	 * @param newCase Case which needs to be adapted (i.e. has no solution). At the end of the call this case has 
	 * the solution filled with the result of the adaptation.
	 * @param similarCase A case which is similar to the newCase
	 */
	public void NullAdaptation(Case newCase, Case similarCase) {
		newCase.copySolution(similarCase);
	}
	
	/*
	public void SubstitutionAdaptation(Case newCase, Case similarCase, Similarity sim) throws Exception {
		ArrayList<Case> caseList = new ArrayList<Case>();
		caseList.add(similarCase);
		SubstitutionAdaptation(newCase, caseList, sim);
	}*/
	
	public void SubstitutionAdaptation(Case newCase,Case similarCase, Similarity sim) throws Exception {
		//Create Case difference with similarCase
		Case caseDiff = getCaseDifference(newCase, similarCase); // FIXME change to a more global solution
		System.out.println("---------------------------\nCaseDifff:\n" + caseDiff.toString());
		// Retrieve a case which is similar to the case difference
		ArrayList<Case> similarDiffCases = sim.getSimilarCases(caseDiff, 1); // Cases which are similar to the difference case
		
		if (similarDiffCases.size() == 0) {
			NullAdaptation(newCase, similarCase);
			return;
		}
		
		// Apply case retrieval rules
		applySolution(newCase, similarCase, caseDiff);
	}

	/** Returns the case which is the difference between two cases, applying the rules.
	 * 
	 * @param a A case to take the difference from
	 * @param b A case which makes the difference to a
	 * @return The difference case
	 * @throws Exception When a method is not known
	 */
	public Case getCaseDifference(Case a, Case b) throws Exception {
		assert(/*a.getNumAttributes() == b.getNumAttributes() && */a.getDomain().equals(b.getDomain()));
		Case retCase = new Case(a.getDomain());
		HashSet<String> usedAtts = new HashSet<String>();
		for (int i = 0 ; i < a.getNumAttributes(); ++i) {
			ArrayList<Object> attA = a.getAttribute(i);
			usedAtts.add((String)attA.get(1));
			int j = b.existsAttribute((String)attA.get(1));
			ArrayList<Object> attDif = (ArrayList<Object>) attA.clone();
			if (j != -1) {  // They have the same attribute, let's compute the difference
				attDif = getAttDiference(attA, b.getAttribute(j));
			} // else A has an attribute that B does not have, so this attribute makes a difference
			retCase.addAttribute(attDif.get(0), (String)attDif.get(1), (String)attDif.get(2));
		}
		for (int j = 0; j < b.getNumAttributes(); ++j) { // The same for the possible attributes that b has but a does not.
			ArrayList<Object> attB = b.getAttribute(j);
			if (usedAtts.contains(attB.get(1))) continue; // If we have used this attribute then A had it and we don't care
			// Then it's the same case as above but with j == -1
			retCase.addAttribute(attB.get(0), (String)attB.get(1), (String)attB.get(2));
		}
		return retCase;
	}

	/** 
	 * 
	 * @param attA Attribute A
	 * @param attB Attribute B
	 * @return Attribute difference
	 * @throws Exception When an unexpected event happens (method with wrong type or unknown method).
	 */
	private ArrayList<Object> getAttDiference(ArrayList<Object> attA, ArrayList<Object> attB) throws Exception {
		assert(((String)attA.get(1)).equals((String)attB.get(1)));
		assert(((String)attA.get(2)).equals((String)attB.get(2)));
		String method = diffKnowledge.get(attA.get(1));
		ArrayList<Object> diffAtt = new ArrayList<Object>();
		diffAtt.add(attA.get(0)); diffAtt.add(attA.get(1)); diffAtt.add(attA.get(2)); // Copy type and name, value will be changed
		if (method.length() > 1) {
			String key = (String)attA.get(0) + "-" + (String)attB.get(0);
			Object val = diffStringsInfo.get(method).get(key);
			if (val == null) {
				key = (String)attB.get(0) + "-" + (String)attA.get(0);
				val = diffStringsInfo.get(method).get(key);
			}
			diffAtt.set(0, val);
		}
		else if (method.equals("+")) {
			if (((String)attA.get(2)).toLowerCase().equals("float")) {
				diffAtt.set(0, Double.parseDouble((String)attA.get(0)) + Double.parseDouble((String)attB.get(0)));
			}
			else if (((String)attA.get(2)).toLowerCase().equals("int")) {
				diffAtt.set(0, Integer.parseInt((String)attA.get(0)) + Integer.parseInt((String)attB.get(0)));
			}
			else {
				throw new Exception("Unknown type " + (String)attA.get(2) + " for method " + method);
			}
		}
		else if (method.equals("-")) {
			if (((String)attA.get(2)).toLowerCase().equals("float")) {
				diffAtt.set(0, Double.parseDouble((String)attA.get(0)) - Double.parseDouble((String)attB.get(0)));
			}
			else if (((String)attA.get(2)).toLowerCase().equals("int")) {
				diffAtt.set(0, Integer.parseInt((String)attA.get(0)) - Integer.parseInt((String)attB.get(0)));
			}
			else {
				throw new Exception("Unknown type " + (String)attA.get(2) + " for method " + method);
			}
		}
		else if (method.equals("&")) {
			if (((String)attA.get(2)).toLowerCase().equals("boolean")) {
				diffAtt.set(0, Boolean.parseBoolean((String)attA.get(0)) && Boolean.parseBoolean((String)attB.get(0)));
			}
			else {
				throw new Exception("Unknown type " + (String)attA.get(2) + " for method " + method);
			}
		}
		else if (method.equals("|")) {
			if (((String)attA.get(2)).toLowerCase().equals("boolean")) {
				diffAtt.set(0, Boolean.parseBoolean((String)attA.get(0)) || Boolean.parseBoolean((String)attB.get(0)));
			}
			else {
				throw new Exception("Unknown type " + (String)attA.get(2) + " for method " + method);
			}
		}
		else {
			throw new Exception("Unknown difference method " + method);
		}
		return diffAtt;
	}
	
	/** Applies the join rules to the similarCase solution with the caseDiff solution
	 * 
	 * @param newCase The case in which the solution has to be applied
	 * @param similarCase A case similar to the newCase to which the difference has to be applied
	 * @param caseDiff A case which is used to solve the differences between the similarCase and the newCase	
	 * @throws Exception 
	 */
	private void applySolution(Case newCase, Case similarCase, Case caseDiff) throws Exception {
		for (String s : joinKnowledge.keySet()) { // For each solution name
			ArrayList<ArrayList<Integer>> existSimilar = similarCase.existsSolution(s);
			ArrayList<ArrayList<Integer>> existDiff = caseDiff.existsSolution(s);

			for (int i = 0 ; i < existDiff.size() ; ++i) {
				if ((i >= existSimilar.size()) || (existDiff.get(i).size() > 0 && existSimilar.get(i).size() == 0)) { // If we have this solution in the differnce but not in the similar we add it as it might solve some difference
					ArrayList<Object> sol = caseDiff.getSolution(existDiff.get(i));
					newCase.addSolution(((ArrayList<Object>)sol.get(0)).get(0), (String)((ArrayList<Object>)sol.get(0)).get(1), (String)sol.get(2));
				}
				else if (existSimilar.get(i).size() == existDiff.get(i).size()) { // If both solutions at the same level
					ArrayList<Object> solDiff = caseDiff.getSolution(existDiff.get(i));
					ArrayList<Object> solSim = similarCase.getSolution(existSimilar.get(i));
					Object newValue = applyOperation(((ArrayList<Object>)solDiff.get(0)).get(0),
													 ((ArrayList<Object>)solSim.get(0)).get(0),
													 (String)solDiff.get(2),
													 joinKnowledge.get(s));
				}
			}
		}
		
	}

	/** Applies a method to two values given the type
	 * 
	 * @param diffValue First value
	 * @param simValue Second value
	 * @param type Type of the values
	 * @param method Operation (+,-,&,|...)
	 * @return Result of the operation
	 * @throws Exception If the method or the type are invalid
	 */
	private Object applyOperation(Object diffValue, Object simValue, String type, String method) throws Exception {
		Object ret;
		if (method.length() > 1) { // then it is a String type
			String key = (String)diffValue + "-" + (String)simValue;
			ret = joinStringsInfo.get(method).get(key);
			if (ret == null) {
				key = (String)simValue + "-" + (String)diffValue;
				ret = joinStringsInfo.get(method).get(key); 
			}
		}
		else if (method.equals("+")) {
			if (type.toLowerCase().equals("float")) {
				ret = Double.parseDouble((String)diffValue) + Double.parseDouble((String)simValue);
			}
			else if (type.toLowerCase().equals("int")) {
				ret = Integer.parseInt((String)diffValue) + Integer.parseInt((String)simValue);
			}
			else {
				throw new Exception("Unknown type " + type + " for method " + method);
			}
		}
		else if (method.equals("-")) {
			if (type.toLowerCase().equals("float")) {
				ret = Double.parseDouble((String)diffValue) - Double.parseDouble((String)simValue);
			}
			else if (type.toLowerCase().equals("int")) {
				ret = Integer.parseInt((String)diffValue) - Integer.parseInt((String)simValue);
			}
			else {
				throw new Exception("Unknown type " + type + " for method " + method);
			}
		}
		else if (method.equals("&")) {
			if (type.toLowerCase().equals("boolean")) {
				ret = Boolean.parseBoolean((String)diffValue) && Boolean.parseBoolean((String)simValue);
			}
			else {
				throw new Exception("Unknown type " + type + " for method " + method);
			}
		}
		else if (method.equals("|")) {
			if (type.toLowerCase().equals("boolean")) {
				ret = Boolean.parseBoolean((String)diffValue) || Boolean.parseBoolean((String)simValue);
			}
			else {
				throw new Exception("Unknown type " + type + " for method " + method);
			}
		}
		else {
			throw new Exception("Unknown difference method " + method);
		}
		return ret;
	}
		
}
