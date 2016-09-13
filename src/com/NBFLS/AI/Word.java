package com.NBFLS.AI;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeSet;


public class Word implements Writable
{
	private String word;
	private boolean capitalizable;
	private int useCount;
	private HashMap<Character,Integer> endPunctuation;
	private HashSet<String> nextWritables;

	private Word(String word) {
		
		this.word = word;
		useCount = 1;
		endPunctuation = new HashMap<Character,Integer>();
		nextWritables = new HashSet<String>();
	}

	/*
	 * Shrinks end punctuation, and checks for interior punctuation If the word
	 * passes the above it is created and its end punctuation is processed and
	 * added to its previous word if there is one, and checks the previous word
	 * to see if it can be capitalizable
	 * 
	 * Returns null if the word is not valid
	 */
	public static Word createWord(String word, boolean capitalizable) {
		// check if word is valid
		word = shrinkEndPunct(word);
		if (word == null)
			return null;
		if (!checkInteriorPunct(word))
			return null;

		// create word, process end punctuation

		Word result;

		char last = word.charAt(word.length() - 1);
		
		if (!Character.isAlphabetic(last) && !Character.isDigit(last)
				&& last != '\'') {
			result = new Word(word.substring(0, word.length() - 1));
			result.addEndPunctuation(last);
		}
		else
			result = new Word(word);
			result.addEndPunctuation(' ');

		// check for capitalizable
		result.capitalizable = capitalizable;
		
		return result;
	}
	/*
	 * 
	 */
	public static boolean hasEndPunctuation(String word)
	{
		word = shrinkEndPunct(word);
		char last = word.charAt(word.length() - 1);
		//TODO:Maybe add more punctuation that dont end sentences
		if (!Character.isAlphabetic(last) && !Character.isDigit(last)
				&& last != '\'' && last != ',') {
			return true;
		}
		return false;
	}
	/*
	 * TODO:make it shrink properly
	 * Shrinks the end punctuation by removing all of it after the last valid
	 * character, except the one right after that character
	 * 
	 * Returns null if there are no valid characters
	 */
	private static String shrinkEndPunct(String word) {
		// find last valid character
		
		word = word.replaceAll("\\.\\.+", ",");
		word = word.replaceAll("!!+","!");
		word = word.replaceAll("\\?\\?+","\\?");
		int lastCharPos = -1;
		for (int k = word.length() - 1; k >= 0; k--) {
			if (Character.isAlphabetic(word.charAt(k))
					|| Character.isDigit(word.charAt(k))
					|| word.charAt(k) == '\'') {
				lastCharPos = k;
				break;
			}
		}
		if (lastCharPos == -1)
			return null;
		if (lastCharPos == word.length() - 1)
			return word;

		return word.substring(0, lastCharPos + 2);
	}

	/*
	 * Checks all characters but the last to see if they are either a letter,
	 * number, or apostrophe
	 */
	private static boolean checkInteriorPunct(String word) {
		char[] chars = word.toCharArray();
		for (int k = 0; k < chars.length - 2; k++) // check all but last
													// character
		{
			if (!Character.isAlphabetic(chars[k])
					&& !Character.isDigit(chars[k]) && chars[k] != '\'')
				return false;
		}
		return true;
	}

	public String getWord() {
		return word;
	}

	public boolean isCapitalizable() {
		return capitalizable;
	}

	public int getUseCount() {
		return useCount;
	}

	public void incrementUseCount(int inc) {
		useCount+=inc;
	}

	public HashMap<Character,Integer> getEndPunctuation() {
		return endPunctuation;
	}

	public void addEndPunctuation(char punc) {
		if(endPunctuation.containsKey(punc))
			endPunctuation.put(punc, endPunctuation.get(punc)+1);
		else
			endPunctuation.put(punc, 1);
		
	}

	public HashSet<String> getNextWritables() {
		return nextWritables;
	}

	public void addNextWritable(Writable next) {
		if(!next.getText().equals(getText()))
			nextWritables.add(next.getText());
	}
	
	public String toString()
	{
		return this.getWord()+"-"+getUseCount();
	}

	public String getText() {
		return getWord();
	}

	@Override
	public Writable getHeuristicNext(Map<String,Writable> writables) {
		if(getNextWritables().isEmpty())
		{
			return null;
		}
		HashSet<Writable> temp = new HashSet<Writable>();
		for(String s:getNextWritables())
			temp.add(writables.get(s));
		TreeSet<Writable> ordered = new TreeSet<Writable>(new Comparator<Writable>(){

			public int compare(Writable one, Writable two) {
				return Integer.compare(one.getUseCount(),two.getUseCount());
			}});
		ordered.addAll(temp);
		ArrayList<Writable> heuristicPick = new ArrayList<Writable>();
		int count = 0;
		Word last = null;
		for(Writable w:ordered)
		{
			if(last==null || !(last.getUseCount()==w.getUseCount()))
				count++;
			for(int k=0;k<count;k++)
			{
				heuristicPick.add(w);
			}
			
		}
		
		return heuristicPick.get((int) (Math.random()*heuristicPick.size()));
		
	}

	public char getHeuristicEndPunctuation()
	{
		ArrayList<Character> heuristicPick = new ArrayList<Character>();
		for(char p:endPunctuation.keySet())
		{
			for(int k=0;k<endPunctuation.get(p);k++)
				heuristicPick.add(p);
		}
		return heuristicPick.get((int) (Math.random()*heuristicPick.size()));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Word other = (Word) obj;
		if (word == null) {
			if (other.word != null)
				return false;
		} else if (!word.equals(other.word))
			return false;
		return true;
	}
}
