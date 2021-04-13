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
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

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
    private TextField drinkType;
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
    private Button viewButton;
    @FXML
    private Label viewLabel;
    @FXML
    private TextArea recipeArea;

	public void addRecipe(ActionEvent event) throws IOException {
		HashMap<String, String> h=new HashMap<String,String>();
		File file=new File("data.properties");
        FileInputStream reader = new FileInputStream(file);
		Properties properties=new Properties();
		properties.load(reader);
		
		for(String key: properties.stringPropertyNames()){
        	h.put(key, properties.get(key).toString());
        }
		if (h.containsKey(cocktailName.getText().toString()) == true) {
			Alert a = new Alert(AlertType.NONE);
    		a.setAlertType(AlertType.ERROR);
    		a.setContentText("There is already recipe called " + cocktailName.getText().toString() + "\nPlease enter a different name or delete the recipe named " + cocktailName.getText().toString());
    		a.show();	
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
    			h.put(cocktailName.getText().toString(),inputDescription);
    			properties.putAll(h);
    			FileOutputStream writer=new FileOutputStream("data.properties");
    			properties.store(writer,null);
    			writer.close();
    				
    			File file2 = new File(cocktailName.getText().toString() + ".ct");
    			file2.createNewFile();
    			try (BufferedWriter writer2 = new BufferedWriter(new FileWriter(file2))) {
    				writer2.write(cocktailName.getText().toString() + "\n");
    				writer2.write(inputDescription + "\n");
    				writer2.write(ingredientList.getText().toString() + "\n");
    				writer2.write(instructions.getText().toString() + "\n");
    				writer2.write(garnishes.getText().toString() + "\n");
    			}	
    		}
    		
			
				
			
		}
	}	
	
	public void deleteRecipe(ActionEvent event) throws IOException {
		HashMap<String, String> h=new HashMap<String,String>();
		File file=new File("data.properties");
        FileInputStream reader = new FileInputStream(file);
		Properties properties=new Properties();
		properties.load(reader);
		
		for(String key: properties.stringPropertyNames()){
        	h.put(key, properties.get(key).toString());
        }
		if (h.containsKey(searchName.getText().toString()) == false) {
			Alert a = new Alert(AlertType.NONE);
    		a.setAlertType(AlertType.ERROR);
    		a.setContentText("Cannot delete recipe!\n" + "There is no recipe called \"" + searchName.getText().toString() + "\"\nRefer to the list view below for the names of cocktails that currently exist.");
    		a.show();	
		}
		else {
			
			Alert a = new Alert(AlertType.NONE);
    		a.setAlertType(AlertType.CONFIRMATION);
    		a.setContentText("Do you want to delete the recipe for a(n) " + searchName.getText().toString() + " ?");
    		Optional<ButtonType> result = a.showAndWait();
    		
    		if (result.isPresent() && result.get() == ButtonType.OK) {
    			File file2 = new File(searchName.getText().toString() + ".ct"); 
    			file2.delete();
    			h.remove(searchName.getText().toString());
    			properties.remove(searchName.getText().toString());
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
		
	}
	
	public void viewRecipe(ActionEvent event) throws IOException {
		HashMap<String, String> h=new HashMap<String,String>();
		File file=new File("data.properties");
        FileInputStream reader = new FileInputStream(file);
		Properties properties=new Properties();
		properties.load(reader);
		
		for(String key: properties.stringPropertyNames()){
        	h.put(key, properties.get(key).toString());
        }
		if (h.containsKey(searchName.getText().toString()) == false) {
			Alert a = new Alert(AlertType.NONE);
    		a.setAlertType(AlertType.ERROR);
    		a.setContentText("Cannot view recipe!\n" + "There is no recipe called \"" + searchName.getText().toString() + "\"\nRefer to the list view below for the names of cocktails that currently exist.");
    		a.show();	
		}
		else {
			//View Recipe in the view scene
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewRecipe.fxml"));
			Parent viewRecipeParent = loader.load();
			userController userController = loader.getController();
			userController.initializeViewRecipe(searchName.getText().toString());
			Scene viewRecipeScene = new Scene(viewRecipeParent);
			viewRecipeScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
			window.setScene(viewRecipeScene);
			window.show();
		}
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
			while(line != null) {
				
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
