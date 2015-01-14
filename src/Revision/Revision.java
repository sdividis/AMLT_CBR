package Revision;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

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
	
	/**
	 * Sets the policy to use for Revision operation.
	 * @param p Integer defining the policy to perform. It can be:
	 * 		0 : no revision (default, it always returns true)
			1 : user revision
			2 : expert revision
	 */
	public void setPolicy(int p) {
		this.policy = p;
	}

	public Revision()
	{
		policy = 0;
	}
	
	public Revision(int type)
	{
		this.policy = type;
	}
	
	public Revision(Expert e)
	{
		this.policy = 2;
		this.expert = e;
	}

	/**
	 * Revises the case passed as argument. 
	 * @param c
	 * @return True if the solution is good, false otherwise according to the policy. (see attribute policy) 
	 */
	public boolean revise(Case c)
	{
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
		
		System.out.println("CASE TO REVISE: \n"+c);
		System.out.println("Do you agree with the proposed solution? (type yes or not):");
		
		/* Get the feedback from the user via keyboard input */
		boolean ok = false;
		Pattern p = Pattern.compile("(yes|not)");
		while(!ok)
		{
			opt = scan.nextLine();
			if (p.matcher(opt).matches())
			{
				ok = true;
				scan.close();
			}
			else
				System.out.println("Please, type yes or not:");
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
