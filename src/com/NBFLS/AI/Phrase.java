package com.NBFLS.AI;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;


public class Phrase implements Writable
{
	public static final int PHRASE_LENGTH = 4;
	
	private LinkedList<Word> words;
	private HashSet<String> nextWritables;
	private int useCount;
	
	public Phrase(LinkedList<Word> words)
	{
		this.words = words;
		useCount = 1;
		nextWritables = new HashSet<String>();
	}
	
	public LinkedList<Word> getWords() { return words; }
	
	public int getUseCount() { return useCount; }
	public void incrementUseCount(int inc) { useCount+=inc; }

	@Override
	public String getText() {
		String output = "";
		for(Word w:words)
		{
			output+=w.getText()+" ";
		}
		return output.trim();
	}

	@Override
	public boolean isCapitalizable() {
		return words.getFirst().isCapitalizable();
	}

	@Override
	public Writable getHeuristicNext(Map<String,Writable> writables) {
		return words.getLast().getHeuristicNext(writables);
	}

	@Override
	public char getHeuristicEndPunctuation() {
		return words.getLast().getHeuristicEndPunctuation();
	}

	@Override
	public void addNextWritable(Writable next) {
		if(!next.getText().equals(getText()))
			nextWritables.add(next.getText());
	}

	@Override
	public Set<String> getNextWritables() {
		return nextWritables;
	}
	
	public String toString()
	{
		return getText();
	}
	
}
