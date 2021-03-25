package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

public class userController {
	public void changeToMenu(ActionEvent event) throws IOException{
		Parent menuParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
		Scene menuScene = new Scene(menuParent);
		menuScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(menuScene);
		window.show();
	}
}
