package Revision;

import Case_Structure.Case;

public abstract class Expert {

	/**
	 * Returns feedback from an expert for the given Case-solution.
	 */
	abstract boolean ask(Case c);
}
