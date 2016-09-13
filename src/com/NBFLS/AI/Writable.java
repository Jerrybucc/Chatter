package com.NBFLS.AI;

import java.util.Map;
import java.util.Set;

public interface Writable 
{
	public String getText();
	public int getUseCount();
	public boolean isCapitalizable();
	public void addNextWritable(Writable next);
	public Set<String> getNextWritables();
	//returns next Writable object for a sentence. Null if sentence stops or
	//there is no next word.
	public Writable getHeuristicNext(Map<String, Writable> writables);
	public char getHeuristicEndPunctuation();
}
