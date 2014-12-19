All datasets introduced in the CBR system must have the same generic format, 
which is described in this document (see example in folder testDataset).


- FIVE different kinds of files must be compulsorily included in order to correctly 
describe and read a dataset: 
	domain.txt ---> file with a single line that will give a name to the dataset's domain.
	names.txt ---> stores the abstract variable names for each attribute/solution (separated by commas).
	values.txt ---> stores the values of each attribute/solution (separated by commas).
	types.txt ---> stores the Java variable type for each attribute/solution (separated by commas).
	att_sol.txt ---> stores 'a' if attribute or 's' if solution (separated by commas).

- These four different files (all except domain.txt) will always contain the same number 
of lines, and each line will describe the corresponding information for a single case, having also
exactly the same number of comma-separted elements.
	
- In names.txt, values.txt and types.txt, the information of the sub-solutions will be stored as follows:
		...,general_solution<sub_solution1,sub_solution2<sub_sub_solution>>,...



Many datasets for testing are availabel at: https://archive.ics.uci.edu/ml/datasets.html