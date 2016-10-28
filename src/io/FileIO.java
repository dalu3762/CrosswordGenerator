package io;
import java.util.ArrayList;

import grid.Grid;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileIO
{	
	private FileIO()
	{				
	}	
	
	//Returns lines of file as String array
	public static String [] readFileByLine(String fileName) throws IOException
    {    	
    	ArrayList<String> lines = new ArrayList<String>();    	
    	
    	BufferedReader in = new BufferedReader(new FileReader(fileName));
    	
    	String line = "";    	
    	line = in.readLine();
    	
    	while (line != null)
    	{
    		lines.add(line);
    		line = in.readLine();
    	}
    	
    	in.close();    	
    	
    	return lines.toArray(new String[lines.size()]);
    }   
       
    public static Grid getGridFromFile(String gridFile, char blankSymbol, char wallSymbol) throws IOException
    {
    	String [] grid = readFileByLine(gridFile);
    	return new Grid(grid, blankSymbol, wallSymbol);
    }
}