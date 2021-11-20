package com.uol.compasso.api.assembler;

import java.util.List;

public interface TransformingObjects<T, S> {

	T assemblerFromObject(S source);
	
	List<T> assemblerFromListObject(List<S> sources);
}
