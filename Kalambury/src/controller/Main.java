package controller;
	
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import view.MainWindowController;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

/**
 * Klasa Main gry sieciowej Kalambury. Służy do włączania gry
 *
 * @author Piotr Dymala / Katarzyna Samkowska
 * @version 1.0
 * @since 2019-01-20
 */
public class Main extends Application {
	private Stage primaryStage;
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage=primaryStage;
		mainWindow();
	}
	
	

	
	public void mainWindow() {
		try {
		  FXMLLoader loader=
		    new FXMLLoader(
			  Main.class.getResource("/view/MainWindowView.fxml"));
		    AnchorPane pane=loader.load();
		    primaryStage.setMinWidth(450.0);
		    primaryStage.setMinHeight(500.0);
		    primaryStage.setTitle("Kalambury");
		    Scene scene=new Scene(pane);
		    MainWindowController mainWindowController=
		    		loader.getController();
		    mainWindowController.setMain(primaryStage);
		    primaryStage.setScene(scene);
		    primaryStage.show();
		} catch (IOException e) {
		  e.printStackTrace();	
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}