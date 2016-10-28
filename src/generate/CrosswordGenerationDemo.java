package generate;
import java.io.IOException;

import grid.Grid;

public class CrosswordGenerationDemo 
{	
	public static void main(String [] args) throws IOException
	{
		System.out.print(System.getProperty("user.dir"));
		
		String gridFile = System.getProperty("user.dir") + "/" + "SampleGrid.txt"; //filename containing grid
		String wordListFile = System.getProperty("user.dir") + "/" + "SampleWordList.txt"; //filename containing words, one on each line		
		char blankSquareSymbol = '-'; //symbol in grid representing an unfilled square
		char wallSquareSymbol = '*'; //symbol in grid representing a wall
		int time = 150; //number of seconds to try filling the crossword before giving up		
				
		for (int x = 0; x < 100; x ++)
		{						
			Grid grid = CrosswordCreator.generateCrossword(gridFile,wordListFile,blankSquareSymbol, wallSquareSymbol, time);
				
			grid.printGrid();				
		}			
	}     
}