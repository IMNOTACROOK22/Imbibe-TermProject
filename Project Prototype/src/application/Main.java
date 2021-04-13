package application;
	
import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Imbibe +");
			primaryStage.show();
			try {
				   File file = new File("data.properties");
				   file.createNewFile();
				} catch(Exception e) {
				   e.printStackTrace();
				}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	@FXML
	public Button toCreate;
	
	@FXML
	public Button toRecipeList;
	
	@FXML
	public Button exit;
	
	@FXML
	public void changeToCreate(ActionEvent event) throws IOException{
		Parent createParent = FXMLLoader.load(getClass().getResource("Create.fxml"));
		Scene createScene = new Scene(createParent);
		createScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(createScene);
		window.show();
	}
	
	@FXML
	public void changeToList(ActionEvent event) throws IOException{
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
	
	
	//Logic for exiting the program entirely with a button press
	public void handleCloseButtonAction(ActionEvent event) {
	    Stage stage = (Stage) exit.getScene().getWindow();
	    stage.close();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
