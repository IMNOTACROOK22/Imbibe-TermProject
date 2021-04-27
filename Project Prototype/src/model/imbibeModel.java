package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class imbibeModel {
	
	//Deletes a .ct file and removes the cocktail from the hash-map.
	
	public static void delete(String name) throws IOException {
		HashMap<String, String> h=new HashMap<String,String>();
		File file=new File("data.properties");
        FileInputStream reader = new FileInputStream(file);
		Properties properties=new Properties();
		properties.load(reader);
		
		for(String key: properties.stringPropertyNames()){
        	h.put(key, properties.get(key).toString());
        }
		
		File file2 = new File(name + ".ct"); 
		file2.delete();
		h.remove(name);
		properties.remove(name);
		FileOutputStream writer=new FileOutputStream("data.properties");
		properties.store(writer,null);
		
		return;
	}
	
	//Creates a .ct file and inputs the cocktail into the hash-map.
	
	public static void createCocktailFile(String name, String ingredients, String instructions, String garnishes, String inputDescription) throws IOException {
		//Read in hash-map
		HashMap<String, String> h=new HashMap<String,String>();
		File file=new File("data.properties");
        FileInputStream reader = new FileInputStream(file);
		Properties properties=new Properties();
		properties.load(reader);
		
		for(String key: properties.stringPropertyNames()){
        	h.put(key, properties.get(key).toString());
        }
		
		//Add cocktail to the hash-map
		h.put(name,inputDescription);
		properties.putAll(h);
		FileOutputStream writer=new FileOutputStream("data.properties");
		properties.store(writer,null);
		writer.close();
		
		//Create a .ct file containing other information
		File file2 = new File(name + ".ct");
		file2.createNewFile();
		try (BufferedWriter writer2 = new BufferedWriter(new FileWriter(file2))) {
			writer2.write(ingredients + "\n\n");
			writer2.write(instructions + "\n\n");
			writer2.write(garnishes + "\n\n");
		}	
	}
	
	//Parses the .ct file and formats the recipe with unit conversions such that it is easy to read
	
	public static String parseAndConvertRecipe(String name) throws IOException {
	
		File file = new File(name + ".ct");
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = br.readLine();
			String completeRecipe = "Ingredients:\n-----------------------------------------------------------------\n";
			int numofNl = 0;
			int numofInst = 0;
			Pattern ouncesPattern = Pattern.compile("ounces|oz|ounce", Pattern.CASE_INSENSITIVE);
			Pattern mlPattern = Pattern.compile("mls\\.|milliliters|ml\\.",Pattern. CASE_INSENSITIVE);
			while(line != null) {
				Matcher ouncesMatcher = ouncesPattern.matcher(line);
				Matcher mlMatcher = mlPattern.matcher(line);
				
				boolean mlMatch = mlMatcher.find(); 
				boolean ounceMatch = ouncesMatcher.find();
				
				if (mlMatch) {
					//change mls to ounces and insert into the string
					String delims = "[ ]+";
					String[] tokens = line.split(delims);
					line = "";
					int numMls = Integer.parseInt(tokens[0]); 
					double numOunces = numMls * 0.033814;
					DecimalFormat df = new DecimalFormat("##.##");
					String stringToInsert = "( "+ df.format(numOunces) + " ounce(s) )";
					for (int i = 0; i < tokens.length; i++) {
						line = line + tokens[i] + " ";
						if (i == 1) {
							line = line + stringToInsert + " ";
						}
					}
				}
				if (ounceMatch) {
					//change ounces to milliliters
					Pattern numPattern = Pattern.compile("[0-9]");
					Pattern fracPattern = Pattern.compile("/");
					double totalMls = 0;
					String delims = "[ ]+";
					String[] tokens = line.split(delims);
					line = "";
					int numOfNumTokens = 0;
					Matcher tokenMatch = ouncesPattern.matcher(tokens[0]);
					for (int i = 0; !tokenMatch.find() ;i++) {
						tokenMatch = ouncesPattern.matcher(tokens[i]);
						Matcher numMatch = numPattern.matcher(tokens[i]);
						if(numMatch.find()) {
							Matcher fracMatch = fracPattern.matcher(tokens[i]);
							if (fracMatch.find()) {
								double fraction = 0;
								switch(tokens[i]) {
									case "1/8":
										fraction = 0.125;
										break;
									case "1/4":
										fraction = 0.25;
										break;
									case "1/2":
										fraction = 0.5;
										break;
									case "3/4":
										fraction = 0.75;
										break;
								}
								totalMls = totalMls + fraction * 29.5735;
								numOfNumTokens++;
							}
							else {
								totalMls = totalMls + Integer.parseInt(tokens[i]) * 29.5735;
								numOfNumTokens++;
							}
						}
					}
					String stringToInsert = "( "+ Math.round(totalMls) + " ml. )";
					for (int i = 0; i < tokens.length; i++) {
						line = line + tokens[i] + " ";
						if (i == numOfNumTokens) {
							line = line + stringToInsert + " ";
						}
					}
					
				}
				if (line.trim().isEmpty()) {
					line = "-----------------------------------------------------------------";
					numofNl++;
				}
				if (numofNl == 0) {
					line = "- " + line;
				}
				if(numofNl == 2) {
					numofInst++;
					line = numofInst+ ". "+ line; 
				}
				if(numofNl == 1) {
					line = "-----------------------------------------------------------------\nInstructions: \n"+ line; 
					numofNl++;
				}
				
				completeRecipe = completeRecipe + line + "\n";
				line = br.readLine();
			}
		
		return completeRecipe; 
		}
	}

}