package word;
import java.util.Random;
import java.util.HashMap;
import java.util.ArrayList;

public class WordList
{
	private String [] wordList;
	private HashMap<Integer, String []> wordsByLength;
	
	@SuppressWarnings("unused")
	private WordList()
	{				
	}	
			
	public WordList(String [] wordList, boolean shuffle)
	{		
		this.wordList = wordList;
		this.wordsByLength = new HashMap<Integer, String[]>();
		
		if (shuffle == true)
		{
			shuffleWords();
		}			
	}
	
	public void shuffleWords()
	{		
		Random rand = new Random();
		int index = 0;
		
		String temp = "";
		
		for (int i = getWordListLength() - 1; i > 0; i --)
		{
			index = rand.nextInt(i);
			temp = getWord(index);
			setWord(getWord(i),index);
			setWord(temp, i);			
		}
	}
	
	public String [] getWords(int length)
	{		
		if (wordsByLength.containsKey(length))
		{
			return wordsByLength.get(length);
		}
		
		ArrayList<String> words = new ArrayList<String>();
		
		for (int x = 0; x < wordList.length; x ++)
		{
			if (wordList[x].length() == length)
			{
				words.add(wordList[x]);
			}
		}
		
		String [] properLengthWords = words.toArray(new String[words.size()]);
		
		wordsByLength.put(length, properLengthWords);
		
		return properLengthWords;
		
	}
	
	public String getWord(int index)
	{
		return this.wordList[index];
	}
		
	public void setWord(String word, int index)
	{
		this.wordList[index] = word;		
	}
	
	public String [] getList()
	{
		return this.wordList;
	}
	
	public void setList(String [] wordList)
	{
		this.wordList = wordList;
	}
	
	public int getWordListLength()
	{
		return this.wordList.length;
	}	
}