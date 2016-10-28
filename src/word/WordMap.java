package word;
import java.util.HashMap;

public class WordMap
{
	public HashMap <String,String> wordMap;
	
	@SuppressWarnings("unused")
	private WordMap()
	{		
	}	
			
	public WordMap(WordList wordList)
	{		
		int listLength = wordList.getWordListLength();
		
		this.wordMap = new HashMap<String,String>(listLength);
		
		for (int x = 0; x < listLength; x ++)
		{
			wordMap.put(wordList.getWord(x),"Y");
		}			
	}
	
	public boolean hasWord(String word)
	{		
		return wordMap.containsKey(word) ? true : false;
	}	
}