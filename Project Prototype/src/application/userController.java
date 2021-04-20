package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class userController {
	
	public static HashMap<String, String> recipeList=new HashMap<String,String>();
	public static Properties properties = new Properties();
	
	@FXML
	private TextArea instructions;
	@FXML
	private TextField garnishes;
	@FXML
	private TextArea ingredientList;	
	@FXML
	private TextField cocktailName;
	@FXML
	private TextArea description;
	@FXML
	private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private ListView<String> cocktailRecipeList;
    @FXML
    private TextField searchName;
    @FXML
    private Button searchButton;
    @FXML
    private Button viewButton;
    @FXML
    private Label viewLabel;
    @FXML
    private TextArea recipeArea;
    
    public void searchRecipe(ActionEvent event) throws IOException {
    	
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("RecipeList.fxml"));
		Parent recipeListParent = loader.load();
		userController userController = loader.getController();
		
		HashMap<String, String> h=new HashMap<String,String>();
		File file=new File("data.properties");
        FileInputStream reader = new FileInputStream(file);
		Properties properties=new Properties();
		properties.load(reader);
		
		Pattern searchPattern = Pattern.compile(searchName.getText(),Pattern.CASE_INSENSITIVE);
		
		for(String key: properties.stringPropertyNames()){
        	Matcher searchMatcher = searchPattern.matcher(key);
        	h.put(key, properties.get(key).toString());
        	if(searchMatcher.find()) {
        		
        		userController.cocktailRecipeList.getItems().add(key+ "\t\t\t" + h.get(key));
        	}
        }
		userController.cocktailRecipeList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		
		Scene listScene = new Scene(recipeListParent);
		listScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(listScene);
		window.show();
		
		
    }
    
	public void addRecipe(ActionEvent event) throws IOException {
		HashMap<String, String> h=new HashMap<String,String>();
		File file=new File("data.properties");
        FileInputStream reader = new FileInputStream(file);
		Properties properties=new Properties();
		properties.load(reader);
		String inputGarnishes;
		
		for(String key: properties.stringPropertyNames()){
        	h.put(key, properties.get(key).toString());
        }
		if (h.containsKey(cocktailName.getText().toString()) == true) {
			Alert a = new Alert(AlertType.NONE);
    		a.setAlertType(AlertType.ERROR);
    		a.setContentText("There is already recipe called " + cocktailName.getText().toString() + "\nPlease enter a different name or delete the recipe named " + cocktailName.getText().toString());
    		a.show();	
    		return;
		}
		if (cocktailName.getText().isEmpty()) {
			Alert a2 = new Alert(AlertType.NONE);
    		a2.setAlertType(AlertType.ERROR);
    		a2.setContentText("Please enter a cocktail name!");
    		a2.show();
    		return;
		}
		if (ingredientList.getText().isEmpty()) {
			Alert a2 = new Alert(AlertType.NONE);
    		a2.setAlertType(AlertType.ERROR);
    		a2.setContentText("Can't have a cocktail with no ingredients!");
    		a2.show();
    		return;
		}
		if (instructions.getText().isEmpty()) {
			Alert a2 = new Alert(AlertType.NONE);
    		a2.setAlertType(AlertType.ERROR);
    		a2.setContentText("Can't have a cocktail with no instructions!");
    		a2.show();
    		return;
		}
		else {	
			Alert a = new Alert(AlertType.NONE);
    		a.setAlertType(AlertType.CONFIRMATION);
    		a.setContentText("Create the recipe for a(n) " + cocktailName.getText().toString() + " ?");
    		Optional<ButtonType> result = a.showAndWait();
    		if (result.isPresent() && result.get() == ButtonType.OK) {
    			String inputDescription; 
    			if (description.getText().trim().length() == 0) {
    				inputDescription = "(No Description)";
    			}
    			else {
    				inputDescription = description.getText().toString();
    			}
    			
    			if (garnishes.getText().trim().length() == 0) {
    				inputGarnishes = "(No Garnishes)";
    			}
    			else {
    				inputGarnishes = garnishes.getText().toString();
    			}
    			
    			h.put(cocktailName.getText().toString(),inputDescription);
    			properties.putAll(h);
    			FileOutputStream writer=new FileOutputStream("data.properties");
    			properties.store(writer,null);
    			writer.close();
    				
    			File file2 = new File(cocktailName.getText().toString() + ".ct");
    			file2.createNewFile();
    			try (BufferedWriter writer2 = new BufferedWriter(new FileWriter(file2))) {
    				writer2.write(ingredientList.getText().toString().trim() + "\n\n");
    				writer2.write(instructions.getText().toString() + "\n\n");
    				writer2.write(inputGarnishes + "\n\n");
    			}	
    			description.clear();
    			cocktailName.clear();
    			ingredientList.clear();
    			instructions.clear();
    			garnishes.clear();
    		}
    		
			
				
			
		}
	}	  
	
	public void deleteRecipe(ActionEvent event) throws IOException {
		if (cocktailRecipeList.getSelectionModel().isEmpty()) {
			Alert a2 = new Alert(AlertType.NONE);
    		a2.setAlertType(AlertType.ERROR);
    		a2.setContentText("Please select a cocktail from the list view or create a new recipe with the create button on the main menu!");
    		a2.show();
    		return;
		}
		HashMap<String, String> h=new HashMap<String,String>();
		File file=new File("data.properties");
        FileInputStream reader = new FileInputStream(file);
		Properties properties=new Properties();
		properties.load(reader);
		
		for(String key: properties.stringPropertyNames()){
        	h.put(key, properties.get(key).toString());
        }
		
		String delims = "[\t]+";
		String [] tokens = cocktailRecipeList.getSelectionModel().getSelectedItem().split(delims);
		String name = tokens[0];	
		
		Alert a = new Alert(AlertType.NONE);
   		a.setAlertType(AlertType.CONFIRMATION);
    	a.setContentText("Do you want to delete the recipe for a(n) " + name + " ?");
    	Optional<ButtonType> result = a.showAndWait();
    		
    	if (result.isPresent() && result.get() == ButtonType.OK) {
    		File file2 = new File(name + ".ct"); 
   			file2.delete();
   			h.remove(name);
   			properties.remove(name);
   			FileOutputStream writer=new FileOutputStream("data.properties");
   			properties.store(writer,null);
    				
   			FXMLLoader loader = new FXMLLoader(getClass().getResource("RecipeList.fxml"));
   			Parent recipeListParent = loader.load();
   			userController userController = loader.getController();
   			userController.initializeRecipeList();
   			Scene listScene = new Scene(recipeListParent);
   			listScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
   			Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
   			window.setScene(listScene);
   			window.show();
   		}
		
		
	}
	
	public void viewRecipe(ActionEvent event) throws IOException {
			//View Recipe in the view scene
		if (cocktailRecipeList.getSelectionModel().isEmpty()) {
			Alert a2 = new Alert(AlertType.NONE);
    		a2.setAlertType(AlertType.ERROR);
    		a2.setContentText("Please select a cocktail from the list view or create a new recipe with the create button on the main menu!");
    		a2.show();
    		return;
		}
			String delims = "[\t]+";
			String [] tokens = cocktailRecipeList.getSelectionModel().getSelectedItem().split(delims);
			String name = tokens[0];
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewRecipe.fxml"));
			Parent viewRecipeParent = loader.load();
			userController userController = loader.getController();
			userController.initializeViewRecipe(name);
			Scene viewRecipeScene = new Scene(viewRecipeParent);
			viewRecipeScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
			window.setScene(viewRecipeScene);
			window.show();
	}
	
	@FXML
	public void returnToList(ActionEvent event) throws IOException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("RecipeList.fxml"));
		Parent recipeListParent = loader.load();
		userController userController = loader.getController();
		userController.initializeRecipeList();
		Scene listScene = new Scene(recipeListParent);
		listScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(listScene);
		window.show();
	}
	
	public void initializeViewRecipe(String inputName) throws IOException {
		File file = new File(inputName + ".ct");
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = br.readLine();
			String completeRecipe = "";
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
			recipeArea.setText(completeRecipe);
		}
		viewLabel.setText("[Viewing] " + inputName);
		
	}
	
	public void initializeRecipeList() throws IOException {
		HashMap<String, String> h=new HashMap<String,String>();
    	File file=new File("data.properties");
        FileInputStream reader = new FileInputStream(file);
		Properties properties=new Properties();
		properties.load(reader);
		for(String key: properties.stringPropertyNames()){
        	h.put(key, properties.get(key).toString());
        	cocktailRecipeList.getItems().add(key+ "\t\t\t" + h.get(key));
		}
		cocktailRecipeList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	}
	
	@FXML    
	public void changeToMenu(ActionEvent event) throws IOException{
		Parent menuParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
		Scene menuScene = new Scene(menuParent);
		menuScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(menuScene);
		window.show();
	}	

}
