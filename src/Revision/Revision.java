package Revision;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import Case_Structure.Case;

/**
 * 
 * @author Albert Busqué
 *
 */
public class Revision {
	
	/* Integer that defines the type of the revision:
		0 : no revision (default)
		1 : user revision
		2 : expert revision
	*/
	private int policy;
	
	private Expert expert;
	
	public void setType(int type) {
		this.policy = type;
	}

	public Revision()
	{
		policy = 0;
	}
	
	public Revision(int type)
	{
		this.policy = type;
	}
	
	public Revision(int type, Expert e)
	{
		this.policy = type;
		this.expert = e;
	}

	/**
	 * Revises the case passed as argument. 
	 * @param c
	 * @return True if the solution is good, false otherwise according to the policy. (see attribute policy) 
	 */
	public boolean revise(Case c)
	{
		// TODO Know how to extract solution from a Case object 
		ArrayList<ArrayList<Integer>> list = c.existsSolution("sol1");
		
		switch (this.policy)
		{
			case 1:
				return this.userRevision(c);
			case 2:
				assert this.expert != null;
				return this.expertRevision(c);
			default:
				return true;
		}
	}
	
	/**
	 * Get feedback from the user of the proposed solution for the given case.
	 * The user manually inputs if agrees with the proposed solution.
	 * @param c Case object containing the case to revise.
	 * @return True if the solution is good; False otherwise.
	 */
	private boolean userRevision(Case c)
	{
		Scanner scan = new Scanner(System.in);
		String opt = "";
		
		System.out.println("Case to revise: " + c.toString());
		System.out.println("Do you agree with the proposed solution? (type yes or not):");
		
		/* Get the feedback from the user via keyboard input */
		boolean ok = false;
		while(!ok)
		{
			try
			{
				opt = scan.next("(yes|not)");
				ok = true;
				scan.close();
			}
			catch (NoSuchElementException e)
			{
				System.out.println("Please, type yes or not:");
			}
		}
		
		if (opt.equals("yes"))
			return true;
		else
			return false;
	}
	
	/**
	 * Get feedback from a virtual expert of the proposed solution for the given case.
	 * The case is passed to the Expert object in order to ask for feedback.
	 * @param c Case object containing the case to revise.
	 * @return True if the solution is good; False otherwise.
	 */
	private boolean expertRevision(Case c)
	{
		return this.expert.ask(c);
	}
}
