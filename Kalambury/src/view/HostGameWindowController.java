package view;

/**
 * Kontroler JavaFX okienka gry- host'a
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
import net.Server;

public class HostGameWindowController {

	private Stage gameWindowStage;

	private Settings settings;
	private Server server;
	private Player hostPlayer;
	private Player clientPlayer;
	private gameOn gameOn;
	private Thread thread;
	private Game game;
	private int interval;
	private Timer timer2;
	Thread t3;
	Runnable serverObserver = null;

	private StringProperty category = new SimpleStringProperty();
	private StringProperty hint1 = new SimpleStringProperty();
	private StringProperty hint2 = new SimpleStringProperty();

	@FXML
	private Button closeGame;
	@FXML
	private Button playGame;
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

	/**
	 * Klasa główna gry. Prowadzi przez pytania na które nie padły odpowiedzi.
	 * Wyswietla podpowiedzi. Dzieje się w odrębnym wątku.
	 */

	private class gameOn implements Runnable {

		int round;

		public gameOn(int round) {
			this.round = round;
		}

		@SuppressWarnings("deprecation")
		@Override
		public void run() {

			serverSpeaks("Runda rozpoczęta!");
			setTimer();

			Platform.runLater(() -> category.setValue(game.getWord(((int) round)).getCategory()));
			server.sendMsg(11);
			server.sendMsg(game.getWord(((int) round)).getCategory());

			try {
				Thread.sleep(game.getHintTime() * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (((int) round) != game.getCurrentRound()) {
				category.setValue(" ");
				hint1.setValue(" ");
				hint2.setValue(" ");
				server.sendMsg(14);

			}
			Platform.runLater(() -> hint1.setValue(game.getWord(((int) round)).getHint1()));
			server.sendMsg(12);
			server.sendMsg(game.getWord(((int) round)).getHint1());

			timer2.cancel();
			setTimer();
			// Platform.runLater(() -> hint1Label.setText(game.getWord(((int)
			// arg)).getHint1()));
			try {
				Thread.sleep(game.getHintTime() * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (((int) round) != game.getCurrentRound()) {
				category.setValue(" ");
				hint1.setValue(" ");
				hint2.setValue(" ");
				server.sendMsg(14);

			}

			Platform.runLater(() -> hint2.setValue(game.getWord(((int) round)).getHint2()));
			server.sendMsg(13);
			server.sendMsg(game.getWord(((int) round)).getHint2());

			timer2.cancel();
			setTimer();

			try {
				Thread.sleep(game.getHintTime() * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Platform.runLater(() -> category.setValue(" "));
			Platform.runLater(() -> hint1.setValue(" "));
			Platform.runLater(() -> hint2.setValue(" "));
			server.sendMsg(14);

			if (game.getCurrentRound() == game.getNumberOfRounds() - 1) {
				serverSpeaks("Nikt nie zgadł. Prawidłowa odpowiedz to: "+game.getWord(game.getCurrentRound()).getWord());
				
				serverSpeaks("Koniec gry");
				Platform.runLater(() -> {
					server.sendMsg(80);

					server.sendMsg(hostPlayer.getPoints());

					server.sendMsg(clientPlayer.getPoints());
				});

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Platform.runLater(() -> {
					openGameWindow();
				});

				playGame.setDisable(false);
 
				game.setOn(false);
				thread.stop();
				t3.interrupt();
				server.deleteObservers();
			}

			else {

				
				
				serverSpeaks("Nikt nie zgadł. Prawidłowa odpowiedz to: "+game.getWord(game.getCurrentRound()).getWord());
				game.setNextRound();
				serverSpeaks("Następna runda");

				gameOn = new gameOn(game.getCurrentRound());
				thread = new Thread(gameOn);
				thread.start();

			}

		}

	}

	/**
	 * Metoda wysyłająca tekst do klienta oraz wyświetla go na ekranie host'a
	 *
	 * @param text tekst do wysłania
	 */
	public void serverSpeaks(String text) {

		String temp = "---SERVER---" + " : " + text;

		Platform.runLater(() -> {
			if (chatArea.getText() != null) {
				chatArea.setText(chatArea.getText().trim() + "\n" + temp);
				chatArea.selectPositionCaret(chatArea.getLength());

			}
		});

		server.sendMsg(9);
		server.sendMsg(temp);

	}

	@SuppressWarnings("deprecation")
	public void gameStartButtonAction() {

		Platform.runLater(
				() -> chatArea.setText(chatArea.getText().trim() + "\n" + (("---SERVER--- : Gra rozpoczęta!"))));
		setTimer();

		if (game.isOn() == false) {
			hostPlayer.setPoints(0);
			clientPlayer.setPoints(0);

			if (firstGame == false) {
				thread.stop();
				t3.interrupt();
				server.deleteObservers();
				gameStart();
			}

			playGame.setDisable(true);
			server.sendMsg(20);
			game.setOn(true);
			thread.start();

		} else {

			playGame.setDisable(true);
			server.sendMsg(20);
			thread.start();

		}
		firstGame = false;

	}

	/**
	 * Metoda która rozpoczyna grę oraz nasłuchuje odpowiedzi od klienta.
	 */
	boolean firstGame = true;

	@SuppressWarnings("deprecation")
	@FXML
	public void gameStart() {

		if (firstGame == true) {
			chatArea.setText("---SERVER--- : Witaj w grze Kalambury! Rozpocznij grę.");

		}

		game = new Game(10, settings.getRounds(), 5);

		categoryLabel.textProperty().bind(category);
		hint1Label.textProperty().bind(hint1);
		hint2Label.textProperty().bind(hint2);

		gameOn = new gameOn(0);
		thread = new Thread(gameOn);

		serverObserver = new Runnable() {
			@Override
			public void run() {

				if (Thread.interrupted()) {

					return;
				}
				server.addObserver((obj, arg) -> {

					if (arg instanceof Answer) {
					
						chatArea.setText(chatArea.getText().trim() + "\n"
								+ ((clientPlayer.getNick() + " : " + (((Answer) arg).getAnswer()))));
						chatArea.selectPositionCaret(chatArea.getLength());

						if (game.checkAnswer(((Answer) arg).getAnswer(), game.getCurrentRound()) == true
								&& game.isOn() == true) {

							clientPlayer.setPoints(clientPlayer.getPoints() + 1);
							Platform.runLater(() -> category.setValue(" "));
							Platform.runLater(() -> hint1.setValue(" "));
							Platform.runLater(() -> hint2.setValue(" "));
							server.sendMsg(14);
							serverSpeaks("Gracz " + clientPlayer.getNick() + " udzielił poprawnej odpowiedzi! : "
									+ game.getWord(game.getCurrentRound()).getWord());
							// server.sendMsg(23);
							thread.stop();
							//

							if (game.getCurrentRound() == game.getNumberOfRounds() - 1 && game.isOn() == true) {
								Platform.runLater(() -> {
									server.sendMsg(80);

									server.sendMsg(hostPlayer.getPoints());

									server.sendMsg(clientPlayer.getPoints());
								});

								serverSpeaks("Koniec gry");

								Platform.runLater(() -> {
									openGameWindow();
								});
								playGame.setDisable(false);
								// server.sendMsg(21);
								game.setOn(false);
								if (t3.isAlive()) {
									t3.interrupt();
									server.deleteObservers();
								}
								;

							}

							else {
								game.setNextRound();

								gameOn = new gameOn(game.getCurrentRound());
								thread = new Thread(gameOn);
								thread.start();
								setTimer();

							}

						}

					} else if (arg instanceof Integer) {
						if (((int) arg) == 88) {

							chatArea.setText(
									chatArea.getText().trim() + "\n" + ((clientPlayer.getNick() + " rozłączył się!")));
							chatArea.selectPositionCaret(chatArea.getLength());

						}

					}
					
				});
			}
		};

		t3 = new Thread(serverObserver);
		t3.start();

	}

	public void cancelTimer() {
		timer2.cancel();
	}

	public void setTimer() {
		interval = game.getHintTime();
		timer2 = new Timer();
		timer2.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				if (interval > 0) {
					Platform.runLater(() -> timerLabel.setText(Integer.toString(interval)));
					interval--;
				} else
					timer2.cancel();
			}
		}, 1000, 2000);
	}

	/**
	 * Metoda która wyswietla tekst wpisany przez hosta oraz sprawdza jego
	 * poprawność z pytaniem.
	 * 
	 * @param e KeyEent, tu ENTER
	 */
	@SuppressWarnings("deprecation")
	@FXML
	public void textAction(KeyEvent e) {

		if (e.getCode().equals(KeyCode.ENTER)) {

			server.sendMsg(10);
			server.sendMsg(writeField.getText());

			Answer answer = new Answer(hostPlayer, writeField.getText());

			chatArea.setText(
					chatArea.getText().trim() + "\n" + ((hostPlayer.getNick() + " : " + (answer.getAnswer()))));
			chatArea.selectPositionCaret(chatArea.getLength());

			chatArea.deselect();
			writeField.clear();

			if (game.checkAnswer(answer.getAnswer(), game.getCurrentRound()) == true && game.isOn() == true) {

				hostPlayer.setPoints(hostPlayer.getPoints() + 1);
				Platform.runLater(() -> category.setValue(" "));
				Platform.runLater(() -> hint1.setValue(" "));
				Platform.runLater(() -> hint2.setValue(" "));
				server.sendMsg(14);
				serverSpeaks("Gracz " + hostPlayer.getNick() + " udzielił poprawnej odpowiedzi! : "
						+ game.getWord(game.getCurrentRound()).getWord());
				// server.sendMsg(22);

				thread.stop();

				if (game.getCurrentRound() == game.getNumberOfRounds() - 1 && game.isOn() == true) {

					Platform.runLater(() -> {
						server.sendMsg(80);

						server.sendMsg(hostPlayer.getPoints());

						server.sendMsg(clientPlayer.getPoints());
					});

					serverSpeaks("Koniec gry");

					try {
						Thread.sleep(1000);
					} catch (InterruptedException et) {
						// TODO Auto-generated catch block
						et.printStackTrace();
					}

					Platform.runLater(() -> {
						openGameWindow();
					});
					playGame.setDisable(false);
					game.setOn(false);
					if (t3.isAlive()) {
						t3.interrupt();
						server.deleteObservers();
					}
					;
	/*				
					serverSpeaks("Koniec gry");

					Platform.runLater(() -> {
						openGameWindow();
					});
					playGame.setDisable(false);
					// server.sendMsg(21);
					game.setOn(false);
					if (t3.isAlive()) {
						t3.interrupt();
						server.deleteObservers();
					}
					;
*/
					
					
					
				}

				else {
					game.setNextRound();

					gameOn = new gameOn(game.getCurrentRound());
					thread = new Thread(gameOn);
					thread.start();
					setTimer();

				}

			}

		}

	}

	/**
	 * Otwiera okno z wynikami danej gry.
	 */
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
			winWindowController.setHostGameWindowController(gameWindowStage);
			winWindowController.setSettings(settings);
			if (hostPlayer.getPoints() >= clientPlayer.getPoints()) {
				winWindowController.setWinner(hostPlayer);
				winWindowController.setLosser(clientPlayer);

			} else {
				winWindowController.setWinner(clientPlayer);
				winWindowController.setLosser(hostPlayer);

			}
			winWindowController.setServer(server);
		
			
			if (!settings.getResultListUrl().isEmpty()) {
				winWindowController.loadData();
				winWindowController.saveResults();
	
			}
			
			game.setOn(false);

			winStage.show(); 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void closeGameWindow() {
		server.sendMsg(88);
		server.stop();
		gameWindowStage.close();

	}

	public Player getClientPlayer() {
		return clientPlayer;
	}

	public void setClientPlayer(Player clientPlayer) {
		this.clientPlayer = clientPlayer;
	}

	public Player gethostPlayer() {
		return hostPlayer;
	}

	public void sethostPlayer(Player player) {
		this.hostPlayer = player;
		Platform.runLater(() -> nickLabel.setText(player.getNick()));
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public void setStage(Stage gameWindowStage) {
		this.gameWindowStage = gameWindowStage;
	}

}