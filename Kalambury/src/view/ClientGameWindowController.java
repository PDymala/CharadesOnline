package view;

/**
 * Kontroler JavaFX okienka gry- klienta
 *
 * @author Piotr Dymala / p.dymala@gmail.com
 * @version 1.0
 * @since 2019-01-20
 */

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import controller.Main;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Answer;
import model.Game;
import model.Player;
import model.Settings;
import net.Client;

public class ClientGameWindowController {

	private Stage gameWindowStage;
	private int id;
	private String nick;
	private int timer;

	private Settings settings;
	private Client client;

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	private Player clientPlayer;
	private Player hostPlayer;

	public Player getHostPlayer() {
		return hostPlayer;
	}

	public void setHostPlayer(Player hostPlayer) {
		this.hostPlayer = hostPlayer;
	}

	Game game;

	public Player getPlayer() {
		return clientPlayer;
	}

	public void setClientPlayer(Player player) {
		this.clientPlayer = player;
		nickLabel.setText(player.getNick());
	}

	private StringProperty category = new SimpleStringProperty();
	private StringProperty hint1 = new SimpleStringProperty();
	private StringProperty hint2 = new SimpleStringProperty();

	@FXML
	private Button closeGame;
	@FXML
	private Label nickLabel;
	@FXML
	private Label categoryLabel;
	@FXML
	private Label hint1Label;
	@FXML
	private Label hint2Label;
	@FXML
	private Label timerLabel;
	@FXML
	private Label label1;
	@FXML
	private Label label2;

	@FXML
	private TextArea chatArea;
	@FXML
	private TextField writeField;

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public void setStage(Stage gameWindowStage) {
		this.gameWindowStage = gameWindowStage;
	}

	public void setTimer(int timer) {
		timerLabel.setText(Integer.toString(timer));
		this.timer = timer;
	}

	@FXML
	public void gameStart() {
		category.setValue(" ");
		hint1.setValue(" ");
		hint2.setValue(" ");
		chatArea.setText("---SERVER--- : Witaj w grze Kalambury! Poczekaj aż host rozpocznie grę.");

		categoryLabel.textProperty().bind(category);
		hint1Label.textProperty().bind(hint1);
		hint2Label.textProperty().bind(hint2);

		client.addObserver((obj, arg) -> {

			if (arg instanceof Answer) {

				chatArea.setText(chatArea.getText().trim() + "\n"
						+ ((hostPlayer.getNick() + " : " + (((Answer) arg).getAnswer()))));
				chatArea.selectPositionCaret(chatArea.getLength());
				chatArea.deselect();
			} else if (arg instanceof String[]) {
				String[] temp = (String[]) arg;

				if (temp[0].equals(Integer.toString(11))) {
					Platform.runLater(() -> category.setValue(temp[1]));
					setTimer();				
				} else if (temp[0].equals(Integer.toString(12))) {
					Platform.runLater(() -> hint1.setValue(temp[1]));
					timer2.cancel();
					setTimer();
				} else if (temp[0].equals(Integer.toString(13))) {
					Platform.runLater(() -> hint2.setValue(temp[1]));
					timer2.cancel();
					setTimer();
				} else if (temp[0].equals(Integer.toString(14))) {
					Platform.runLater(() -> category.setValue(" "));
					Platform.runLater(() -> hint1.setValue(" "));
					Platform.runLater(() -> hint2.setValue(" "));
					timer2.cancel();
					
				} else if (temp[0].equals(Integer.toString(9))) {

					chatArea.setText(chatArea.getText().trim() + "\n" + temp[1]);
					chatArea.selectPositionCaret(chatArea.getLength());
					chatArea.deselect();

				}

				else if (temp[0].equals(Integer.toString(20))) {

					chatArea.setText(chatArea.getText().trim() + "\n" + "---SERVER--- : Gra rozpoczęta!");
					chatArea.selectPositionCaret(chatArea.getLength());
					chatArea.deselect();

				}

				else if (temp[0].equals(Integer.toString(80))) {
					Platform.runLater(() -> {
						hostPlayer.setPoints(Integer.parseInt(temp[1]));

						clientPlayer.setPoints(Integer.parseInt(temp[2]));

						openGameWindow();
					});

				} else if (temp[0].equals(Integer.toString(88))) {
					
					chatArea.setText(chatArea.getText().trim() + "\n" + "SERVER został odłączony");
					chatArea.selectPositionCaret(chatArea.getLength());
					chatArea.deselect();

					
				}

			}

		});

	}

	@FXML
	public void textAction(KeyEvent e) {

		if (e.getCode().equals(KeyCode.ENTER)) {

			client.sendMsg(10, client.getServerSocket());
			client.sendMsg(writeField.getText(), client.getServerSocket());

			chatArea.setText(
					chatArea.getText().trim() + "\n" + ((clientPlayer.getNick() + " : " + (((writeField.getText()))))));
			chatArea.selectPositionCaret(chatArea.getLength());

			chatArea.deselect();
			writeField.clear();

		}

	}

	
	public void cancelTimer() {
		timer2.cancel();
	}

	int interval = 10;
	Timer timer2;

	public void setTimer() {
		timer2 = new Timer();
		timer2.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				if (interval > 0) {
					Platform.runLater(() -> timerLabel.setText(Integer.toString(interval)));
					interval--;
				} else
					timer2.cancel();
			}
		}, 1000, 1000);
	}
	
	@FXML
	public void openGameWindow() {
		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/WinWindowView.fxml"));
			AnchorPane winPane = loader.load();

			Stage winStage = new Stage();
			winStage.setMinWidth(400.0);
			winStage.setMinHeight(100.0);
			winStage.setTitle("Wyniki gry");
			winStage.initModality(Modality.NONE);
			winStage.initOwner(gameWindowStage);

			Scene scene = new Scene(winPane);
			winStage.setScene(scene);

			WinWindowController winWindowController = loader.getController();
			winWindowController.setStage(winStage);
			winWindowController.setClientGameWindowController(gameWindowStage);
			winWindowController.setSettings(settings);
			if (hostPlayer.getPoints() >= clientPlayer.getPoints()) {
				winWindowController.setWinner(hostPlayer);
				winWindowController.setLosser(clientPlayer);

			} else {
				winWindowController.setWinner(clientPlayer);
				winWindowController.setLosser(hostPlayer);

			}

			
			if (!settings.getResultListUrl().isEmpty()) {
				winWindowController.loadData();
				winWindowController.saveResults();
	
			}

			winStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void closeGameWindow() {
		client.stop();
		gameWindowStage.close();
	}
}