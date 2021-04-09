package application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class userController {
	 	
	@FXML
	private TextField instructions;

	@FXML
	private TextField garnishes;

	@FXML
	private TextField ingredientList;

	@FXML
	private TextField cocktailName;

	@FXML
	private TextField description;

	@FXML
	private Button addButton;
	

	public void addRecipe(ActionEvent event) throws IOException {
		File file = new File(cocktailName.getText().toString() + ".ct");
		file.createNewFile();
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			writer.write(cocktailName.getText().toString() + "\n");
			writer.write(description.getText().toString() + "\n");
			writer.write(ingredientList.getText().toString() + "\n");
			writer.write(garnishes.getText().toString() + "\n");
			writer.write(instructions.getText().toString() + "\n");
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
