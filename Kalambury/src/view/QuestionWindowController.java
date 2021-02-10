package view;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import controller.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Settings;

/**
 * Kontroler JavaFX okienka z wyborem gry (łączenie i zakładanie) oraz wpisanie
 * nick'u
 *
 * @author Katarzyna Samkowska / katarzyna.samkowska@gmail.com
 * @version 1.0
 * @since 2019-01-20
 */
public class QuestionWindowController {

	private Stage questionWindowStage;
	private Settings settingsHost;

	public Settings getSettingsHost() {
		return settingsHost;
	}

	public void setSettingsHost(Settings settingsHost) {
		this.settingsHost = settingsHost;
	}

	public Settings getSettingsClient() {
		return settingsClient;
	}

	public void setSettingsClient(Settings settingsClient) {
		this.settingsClient = settingsClient;
	}

	Settings settingsClient;

	@FXML
	private TextField userNick;
	@FXML
	private Button newGame;
	@FXML
	private Button joinGame;
	@FXML
	private Label Label1;

	public void setStage(Stage questionWindowStage) {
		this.questionWindowStage = questionWindowStage;
	}

	AudioClip powitanie = new AudioClip(this.getClass().getResource("/sound/0_main.mp3").toString());

	public void initialize(URL location, ResourceBundle resources) {
		powitanie.play();
	}

	@FXML
	public void closeQuestionWindow() {
		powitanie.stop();
		questionWindowStage.close();
	}

	@FXML
	public void hostGame() {

		if (userNick.getText().isEmpty()) {
			@SuppressWarnings("unused")
			Optional<ButtonType> result = AlertBox.showAndWait(AlertType.ERROR, "Brak nick'u", "Podaj nick.");
		} else {

			powitanie.stop();

			try {
				FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/WaitingForClientWindowView.fxml"));
				AnchorPane gamePane = loader.load();

				Stage waitingStage = new Stage();
				waitingStage.setMinWidth(320.0);
				waitingStage.setMinHeight(226.0);
				waitingStage.setTitle("Kalambury - Gra");
				waitingStage.initModality(Modality.WINDOW_MODAL);
				waitingStage.initOwner(questionWindowStage);

				Scene scene = new Scene(gamePane);
				waitingStage.setScene(scene);

				WaitingForClientController waitingForClientController = loader.getController();
				waitingForClientController.setHostSettings(settingsHost);
				waitingForClientController.setStage(waitingStage);
				waitingForClientController.setHostName(userNick.getText());
				waitingForClientController.serverStart();

				questionWindowStage.hide();
				waitingStage.show();

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	@FXML
	public void joinGame() {

		if (userNick.getText().isEmpty()) {
			@SuppressWarnings("unused")
			Optional<ButtonType> result = AlertBox.showAndWait(AlertType.ERROR, "Brak nick'u", "Podaj nick.");
		} else {

			powitanie.stop();

			try {
				FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/ConnectingToHostWindowView.fxml"));
				AnchorPane gamePane = loader.load();

				Stage connectingStage = new Stage();
				connectingStage.setMinWidth(320.0);
				connectingStage.setMinHeight(226.0);
				connectingStage.setTitle("Kalambury - Gra");
				connectingStage.initModality(Modality.WINDOW_MODAL);
				connectingStage.initOwner(questionWindowStage);

				Scene scene = new Scene(gamePane);
				connectingStage.setScene(scene);

				ConnectingToHostController connectingToHostController = loader.getController();
				connectingToHostController.setClientSettings(settingsClient);
				connectingToHostController.setStage(connectingStage);
				connectingToHostController.setClientName(userNick.getText());
				connectingToHostController.serverStart();

				questionWindowStage.hide();
				connectingStage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void emptyNickDialogBox() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.getModality();
		alert.setTitle("");
		alert.setHeaderText(null);
		alert.setContentText("Wpisz nick");
		alert.showAndWait();
	}

}