package com.NBFLS.AI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;



public class ChatAI 
{
	Map<String,Writable> writables;
	String brainPath;

	XStream xstream = new XStream(new DomDriver());
	public static final String UTF8_BOM = "\uFEFF";
	private Scanner scan;
	
	@SuppressWarnings("unchecked")
	public ChatAI(String brainPath)
	{
		this.brainPath = brainPath;
		try
		{
		scan = new Scanner(new File(brainPath));
		String xml = "";
		while(scan.hasNextLine())
			xml += scan.nextLine();
		 if (xml.startsWith(UTF8_BOM))
	            xml = xml.substring(1);
		writables = (Map<String,Writable>) xstream.fromXML(xml);
		} catch (Exception e) {
			e.printStackTrace();
		writables = new HashMap<String,Writable>();
		}
	}
	public void addSentence(String sentence)
	{
		sentence = sentence.toLowerCase();
		
		Word last = null;
		String lastStr = null;
		
		int counter = 0;
		LinkedList<Word> phraseWords = new LinkedList<Word>();
		Phrase lastPhrase = null;
		for(String s:sentence.split(" "))
		{
			Word word = Word.createWord(s, (lastStr==null)?true:Word.hasEndPunctuation(lastStr));
			if(word != null)
			{			
				word = addWord(word);
				phraseWords.add(word);
				if(last!=null)
					last.addNextWritable(word);
				if(counter == 0 && lastPhrase != null)
					lastPhrase.addNextWritable(word);
				last = word;
				lastStr = s;
				counter++;
			}
			else
			{
				counter = 0;
			}
			if(counter>=Phrase.PHRASE_LENGTH)
			{
				Phrase phr = new Phrase(phraseWords);
				addPhrase(phr);
				if(lastPhrase!=null)
				{
					lastPhrase.addNextWritable(phr);
					lastPhrase.getWords().getLast().addNextWritable(phr);
				}
				phraseWords = new LinkedList<Word>();
				lastPhrase = phr;
				counter = 0;
			}
				
		}
		saveBrain();
	}
	
	private Word addWord(Word newWord)
	{
		//if the word already exists, combine them
		if(writables.containsKey(newWord.getWord()))
		{
			Word oldWord = (Word) writables.get(newWord.getWord());
			oldWord.incrementUseCount(newWord.getUseCount());
			//combine punctuation counts
			for(char p:newWord.getEndPunctuation().keySet())
			{
				if(oldWord.getEndPunctuation().containsKey(p))
					oldWord.getEndPunctuation().put(p, oldWord.getEndPunctuation().get(p)+newWord.getEndPunctuation().get(p));
				else
					oldWord.getEndPunctuation().put(p, newWord.getEndPunctuation().get(p));
			}
			
			oldWord.getNextWritables().addAll(newWord.getNextWritables());
			return oldWord;
		}
		else
		{
			writables.put(newWord.getWord(), newWord);
			return newWord;
		}
	}
	
	private void addPhrase(Phrase p)
	{
		//if phrase already exists, combine them
		if(writables.containsKey(p.getText()))
		{
			((Phrase)writables.get(p.getText())).incrementUseCount(p.getUseCount());
		}
		else
		{
			writables.put(p.getText(), p);
		}
	}
	
	public String generateSentence()
	{
		//TODO:figure out a better way of finding a capitalizable writable
		String sentence = "";
		Writable begin = null;
		while(begin==null || !begin.isCapitalizable())
		{
			begin = writables.get(getRandomSetObj(writables.keySet()));
		}
		Writable next = begin;
		while(next!=null)
		{
			sentence += next.getText();
			char punct = next.getHeuristicEndPunctuation();
			sentence += punct;
			if(punct == ' ')
				next = next.getHeuristicNext(writables);
			else
				next = null;
		}
		
		
		return sentence.substring(0,1).toUpperCase() + sentence.substring(1);
	}
	
	private String getRandomSetObj(Set<String> set)
	{
		int item = (int) (Math.random()*set.size()); 
		int i = 0;
		for(String s : set)
		{
		    if (i == item)
		        return s;
		    i = i + 1;
		}
		return null;
	}
	
	public void saveBrain()
	{
		try {
			xstream.toXML(writables,new FileOutputStream(brainPath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}
	
}
