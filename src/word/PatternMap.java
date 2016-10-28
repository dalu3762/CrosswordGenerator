package word;
import java.util.ArrayList;
import java.util.HashMap;

import grid.Grid;

public class PatternMap
{
	public HashMap <String,String[]> patternMap;
	
	public PatternMap()
	{
		this.patternMap = new HashMap<String,String[]>(10000);
	}
	
	public String [] getWordsPattern(Grid grid, WordList wordList, String pattern)
	{						
		if (hasPattern(pattern) == false)
		{
			updatePatternMap(grid, wordList, pattern);
		}		
				
		return patternMap.get(pattern);
	}
	
	public int getNumWordsPattern(Grid grid, WordList wordList, String pattern)
	{		
		return getWordsPattern(grid, wordList,pattern).length;
	}	
		
	private void updatePatternMap (Grid grid, WordList wordList, String pattern)
	{		
		String word = "";
				
		ArrayList<String> patternWords = new ArrayList<String>();
		
		String [] wordsOfCorrectLength = wordList.getWords(pattern.length());
		
		for (int x = 0; x < wordsOfCorrectLength.length; x ++)
		{
			word = wordsOfCorrectLength[x];
					
			if (fitsPattern(grid, word,pattern))
			{
				patternWords.add(word);
			}
		}
		
		patternMap.put(pattern,patternWords.toArray(new String[patternWords.size()])); 		
	}
		
	private boolean fitsPattern(Grid grid, String word, String pattern)
	{
		if (word.length() != pattern.length())
		{
			return false;
		}
		
		char char1;
		char char2;
		
		for (int x = 0; x < word.length(); x ++)
		{
			char1 = word.charAt(x);
			char2 = pattern.charAt(x);
			if (char1 != char2 && char1 != grid.getBlankSymbol() && char2 != grid.getBlankSymbol())
			{
				return false;
			}
		}
		
		return true;
	}
	
	private boolean hasPattern(String pattern)
	{
		return patternMap.containsKey(pattern) ? true : false;
	}	
}