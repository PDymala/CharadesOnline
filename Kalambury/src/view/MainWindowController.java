package view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import controller.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Settings;

/**
 * Kontroler JavaFX okienka głównego
 *
 * @author Katarzyna Samkowska / katarzyna.samkowska@gmail.com
 * @version 1.0
 * @since 2019-01-20
 */

public class MainWindowController {
	private Stage primaryStage;
	private Settings settingsHost = new Settings(8080, 4, Settings.Type.HOST);
	private Settings settingsClient = new Settings(8080, "localhost", Settings.Type.CLIENT);

	@FXML
	private TextField userNick;
	@FXML
	private Button newGame;
	@FXML
	private Button rules;
	@FXML
	private Button results;
	@FXML
	private Button closeApp;
	@FXML
	private Button addUser;
	@FXML
	private Button settings;

	public void setMain(Stage primaryStage) {
		this.primaryStage = primaryStage;

		primaryStage.setTitle("Kalambury KS PD");
		newGame.setVisible(true);
	}

	public void initialize(URL location, ResourceBundle resources) {

	}

	@FXML
	public void closeMainWindow() {

		primaryStage.close();
		System.exit(0);
	}

	@FXML
	public void openQuestionWindow() {

		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/QuestionWindowView.fxml"));
			AnchorPane questionPane = loader.load();

			Stage questionStage = new Stage();
			questionStage.setMinWidth(800.0);
			questionStage.setMinHeight(400.0);
			questionStage.setTitle("Kalambury - Gra");
			questionStage.initModality(Modality.WINDOW_MODAL);
			questionStage.initOwner(primaryStage);

			Scene scene = new Scene(questionPane);
			questionStage.setScene(scene);

			QuestionWindowController questionWindowController = loader.getController();
			questionWindowController.setStage(questionStage);
			questionWindowController.setSettingsClient(settingsClient);
			questionWindowController.setSettingsHost(settingsHost);

			questionStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void openResultsWindow() {
		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/ResultsWindowView.fxml"));
			AnchorPane resultsPane = loader.load();

			Stage resultsStage = new Stage();
			resultsStage.setMinWidth(800.0);
			resultsStage.setMinHeight(600.0);
			resultsStage.setTitle("Kalambury - Wyniki");
			resultsStage.initModality(Modality.WINDOW_MODAL);
			resultsStage.initOwner(primaryStage);

			Scene scene = new Scene(resultsPane);
			resultsStage.setScene(scene);

			ResultsWindowController resultsWindowController = loader.getController();
			resultsWindowController.setSettings(settingsHost);

			resultsWindowController.setStage(resultsStage);
			
			resultsStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void openRulesWindow() {

		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/RulesWindowView.fxml"));
			AnchorPane rulesPane = loader.load();

			Stage rulesStage = new Stage();
			rulesStage.setMinWidth(800.0);
			rulesStage.setMinHeight(600.0);
			rulesStage.setTitle("Zasady gry w Kalambury");
			rulesStage.initModality(Modality.WINDOW_MODAL);
			rulesStage.initOwner(primaryStage);

			Scene scene = new Scene(rulesPane);
			rulesStage.setScene(scene);

			RulesWindowController rulesWindowController = loader.getController();
			rulesWindowController.setStage(rulesStage);

			rulesStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void openSettingsWindow() {

		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/SettingsWindowView.fxml"));
			AnchorPane settingsPane = loader.load();
//login controller
			Stage settingsStage = new Stage();
			settingsStage.setMinWidth(800.0);
			settingsStage.setMinHeight(600.0);
			settingsStage.setTitle("Ustawienia gry");
			settingsStage.initModality(Modality.WINDOW_MODAL);
			settingsStage.initOwner(primaryStage);

			Scene scene = new Scene(settingsPane);
			settingsStage.setScene(scene);

			SettingsWindowController settingsWindowController;
			settingsWindowController = loader.getController();
			settingsWindowController.setStage(settingsStage);
			settingsWindowController.setOldSettingsClient(settingsClient);
			settingsWindowController.setOldSettingsHost(settingsHost);
			settingsWindowController.addObserver((obj, arg) -> {

				settingsHost = ((Settings[]) arg)[0];
				settingsClient = ((Settings[]) arg)[1];

			});

			settingsStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}