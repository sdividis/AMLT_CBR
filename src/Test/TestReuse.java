package Test;

import Case_Structure.Case;
import Retrieval.Similarity;
import Reuse.Reuse;

public class TestReuse {

	public TestReuse()
	{
		TestCBLibrary t = new TestCBLibrary();
		Case c1 = t.lib.getCase(0);
		Similarity similarity = new Similarity(t.lib);
		
		Reuse reuse = new Reuse(0.5, similarity, t.lib);
		reuse.reuse(c1);
	}
	
	public static void main(String[] args) {
		
		new TestReuse();
	}
}
