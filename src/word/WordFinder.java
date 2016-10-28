package word;

import grid.Grid;

public class WordFinder
{
	WordList wordList;
	public WordMap wordMap;
	public PatternMap patternMap;
	
	@SuppressWarnings("unused")
	private WordFinder()
	{		
	}	
			
	public WordFinder(String [] wordList)
	{
		this.wordList = new WordList(wordList, true);
		this.wordMap = new WordMap(this.wordList);
		this.patternMap = new PatternMap();		
	}			
		
	public String [] getWordsPattern(Grid grid, String pattern)
	{		
		if (pattern.indexOf(grid.getBlankSymbol()) == -1)
		{
			if (wordMap.hasWord(pattern))
			{
				String [] word = {pattern};
				return word;
			}
			else
			{
				return new String[0];
			}
		}
		
		String [] patternWords = patternMap.getWordsPattern(grid, wordList,pattern);		
		
		return patternWords;
	}	
}