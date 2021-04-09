package com.pnc.run;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class PrimeNumberChecker {

	public static void main(String[] args) {
		PrimeNumberChecker pnc = new PrimeNumberChecker();
		File file = pnc.validateInput(args);
		List<Integer> values = pnc.readValidColumnDataFromXLSX(file, 1 , true);
		//System.out.println("All valid values from column: " + values);
		pnc.printAllPrimeNumbers(values);						
	}
	
	/**
	 * Validate given parameter (file name) and check if file exist
	 * @param args - name of file
	 * @return File - *.xlsx file with data
	 */
	public File validateInput(String [] args) {		
		String fileName = null;		
		File file = null;
		if (args.length > 0) {
			fileName = args[0];
			if(!fileName.endsWith(".xlsx")) {
				System.out.println("File type not supported, please use *.xlsx excel file, exiting program...");
				System.exit(0);
			}			
			File path = new File("resources");
			if(!path.exists()) 				
				path.mkdirs();			
			file = new File(path + File.separator + fileName);			
			if(!file.exists()) {
				System.out.println("File "+ fileName +" doesnt exist in 'resources' folder, exiting program...");
				System.exit(0);
			}		
		}
		else {
			System.out.println("File name parameter is missing, exiting program...");
			System.exit(0);
		}		
		return file;		
	}
	/**
	 * Read specific column from given (*.xlsx) file and return valid (Integer) List of numbers.
	 * @param file - *.xlsx file 
	 * @param columnNumber - number of column from we want to read data
	 * @param duplicates - allowed/disabled
	 * @return List<Integer>
	 */
	public List<Integer> readValidColumnDataFromXLSX(File file, Integer columnNumber, boolean duplicates) {
		
		List <Integer> list = new ArrayList<Integer>();
		DataFormatter dataFormatter = new DataFormatter();
		try {			
			FileInputStream fis = new FileInputStream(file);		
			XSSFWorkbook wb = new XSSFWorkbook(fis);   
			XSSFSheet sheet = wb.getSheetAt(0);     									
			Iterator<Row> rowIterator = sheet.rowIterator();  
			while (rowIterator.hasNext()) {  
				Row row = rowIterator.next();  
				Cell cell = row.getCell(columnNumber);				
				if(cell != null) {
					String cellValue = dataFormatter.formatCellValue(cell);
					if(isStringPositiveInteger(cellValue))	
						list.add(Integer.parseInt(cellValue));						
				}				
			}
			wb.close();
			fis.close();
		}		
		catch (IOException ioe) {
			ioe.printStackTrace();			
		}
		// removing duplicates
		List<Integer> listWithoutDuplicates = new ArrayList<>(new HashSet<>(list));
		if (duplicates)
			return list;
		return listWithoutDuplicates;
	}	
	/**
	 * Print all prime numbers from given List
	 * @param values - List of Integer numbers  
	 */
	public void printAllPrimeNumbers(List<Integer> values) {
		boolean found = false;
		for(Integer i:values) {
			if(isIntPrimeNumber(i)) {
				System.out.println("Number " + i + " is prime number.");
				found = true;
			}
		}
		if(!found)
			System.out.println("No prime numbers found.");
		
	}	
	/**
	 * Check if given String is positive Integer number
	 * @param s - String
	 * @return true if String is positive Integer otherwise false
	 */
	private static boolean isStringPositiveInteger(String s) {
		Integer i;
		try { 
	        i = Integer.parseInt(s);	        
	    } 
		catch(NumberFormatException | NullPointerException e) {			
	        return false; 
	    }		
	    if(i > 0) 
	    	return true;    
	    return false;
	}
	
	/**
	 * Check if given Integer number is prime number.
	 * @param Integer number
	 * @return true if Integer number is prime number otherwise false
	 */
	private static boolean isIntPrimeNumber(Integer number) {
		boolean isPrime = true;
	    for (int i = 2; i <= number / 2; ++i) {	      
	      if (number % i == 0) {
	    	  isPrime = false;
	        break;
	      }
	    }	    
		return isPrime;
	}
}