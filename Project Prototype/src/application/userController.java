package application;

import java.io.BufferedReader;
import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
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
import model.imbibeModel;

public class userController {
	
	String oldCocktailName;
	boolean updateFlag = false;
	
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
    private Button editButton;
    @FXML
    private Label viewLabel;
    @FXML
    private Label viewNameLabel;
    @FXML
    private TextArea recipeArea;
    @FXML
    private Button toMainMenu;
    
    //Fills in the text fields/areas with the data from the hash-map and the .ct file
    
    public void initializeUpdate(String name) throws IOException {
    	cocktailName.setText(name);
    	int numBlankLines = 0;
    	String ingredients = "";
    	String instructionslist = "";
    	String garnishesList = "";
    	File file = new File(name + ".ct");
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = br.readLine();
			while(line != null) {
				
				if (line.trim().isEmpty()) {
					numBlankLines++;
				}
				else {
					if(numBlankLines == 0) {
					ingredients = ingredients + line + "\n";
					}
					if(numBlankLines == 1) {
						instructionslist = instructionslist + line + "\n";
					}
					if(numBlankLines == 2) {
						garnishesList = garnishesList + line + "\n";
					}
				}
				
				line = br.readLine();
			}
		}
		ingredientList.setText(ingredients);
		instructions.setText(instructionslist);
		garnishes.setText(garnishesList);
		
		HashMap<String, String> h=new HashMap<String,String>();
		File file2=new File("data.properties");
        FileInputStream reader = new FileInputStream(file2);
		Properties properties=new Properties();
		properties.load(reader);
		
		for(String key: properties.stringPropertyNames()){
        	h.put(key, properties.get(key).toString());
        }
		
		description.setText(h.get(name));
    }
    
    //Loads the create function in order to update a recipe 
    
    public void updateRecipe(ActionEvent event) throws IOException {
    		
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("Create.fxml"));
		Parent createParent = loader.load();
		userController userController = loader.getController();
		userController.updateFlag = true;
		userController.oldCocktailName = viewNameLabel.getText().toString();
		userController.initializeUpdate(viewNameLabel.getText().toString());
		Scene createScene = new Scene(createParent);
		createScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(createScene);
		window.show();
    }
    
    //Upon pressing the searchButton, it will look for recipes who's name contains the search term
    
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
    
    //Creates a new recipe file and enters the cocktail into the hash-map
    
	public void addRecipe(ActionEvent event) throws IOException {
		String inputGarnishes;
		
		HashMap<String, String> h=new HashMap<String,String>();
		File file=new File("data.properties");
        FileInputStream reader = new FileInputStream(file);
		Properties properties=new Properties();
		properties.load(reader);
		
		
		for(String key: properties.stringPropertyNames()){
        	h.put(key, properties.get(key).toString());
        }
		
		if ((h.containsKey(cocktailName.getText().toString()) == true) && updateFlag == false) {
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
    		if (updateFlag == true) {
    			a.setContentText("Update the recipe for a(n) " +  oldCocktailName + " ?");
    		}
    		else {
    			a.setContentText("Create the recipe for a(n) " + cocktailName.getText().toString() + " ?");
    		}
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
    			if(updateFlag == true) {
    				imbibeModel.delete(oldCocktailName);	
    			}
    			
    			imbibeModel.createCocktailFile(cocktailName.getText().toString(), ingredientList.getText().toString().trim(), instructions.getText().toString().trim(), inputGarnishes, inputDescription);	
    		
    			Alert a2 = new Alert(AlertType.NONE);
        		a2.setAlertType(AlertType.CONFIRMATION);
        		if (updateFlag == true) {
        			a2.setContentText("Updated the recipe for a(n) " + oldCocktailName + ".");
        		}
        		else {
        			a2.setContentText("Added " + cocktailName.getText().toString() + " to the recipe list! ");
        		}
        		a2.show();
        		
    			description.clear();
    			cocktailName.clear();
    			ingredientList.clear();
    			instructions.clear();
    			garnishes.clear();
    		}
    		
			
				
			
		}
	}	  
	
	//Deletes a selected recipe from the list view
	
	public void deleteRecipe(ActionEvent event) throws IOException {
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
		
		Alert a = new Alert(AlertType.NONE);
   		a.setAlertType(AlertType.CONFIRMATION);
    	a.setContentText("Do you want to delete the recipe for a(n) " + name + " ?");
    	Optional<ButtonType> result = a.showAndWait();
    		
    	if (result.isPresent() && result.get() == ButtonType.OK) {
    		imbibeModel.delete(name);		
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
	
	//Navigates to the view scene with some input validation
	
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
	
	//Return to the list view from the viewScene
	
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
	
	//Fills in the text area in the viewScene to show the complete recipe with unit conversions.
	
	public void initializeViewRecipe(String inputName) throws IOException {
		recipeArea.setText(imbibeModel.parseAndConvertRecipe(inputName));
		viewLabel.setText("[Viewing] ");
		viewNameLabel.setText(inputName);
		
	}
	
	//Initializes the recipe list-view 
	
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
	
	//Changes the scene to the main menu at upon pressing the toMainMenu button
	
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
