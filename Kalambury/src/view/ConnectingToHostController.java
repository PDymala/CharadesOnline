package view;

import java.io.IOException;

import controller.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Player;
import model.Settings;
import net.Client;

/**
 * Kontroler JavaFX okienka oczekiwania na połączenie z host'em
 *
 * @author Piotr Dymala / p.dymala@gmail.com
 * @version 1.0
 * @since 2019-01-20
 */
public class ConnectingToHostController {

	private Client client;
	private String clientName;
	private Player clientPlayer;
	private Player hostPlayer;
	private Stage waitingWindowStage;
	private Settings clientSettings;

	@FXML
	private TextField TF1;
	@FXML
	private Label Label1;

	/**
	 * Metoda tworząca dwa zsynchronizowane wątki. Najpierw połączenie z serwerem,
	 * kolejno drugi- nasłuchiwanie odpowiedzi oraz utworzenie okna gry
	 */
	public synchronized void serverStart() {

		Runnable serverRun = () -> {

			clientRun();
		};

		Runnable serverListener = () -> {
			serverListener();
		};

		Thread thread = new Thread(serverRun);
		thread.start();
		Thread thread2 = new Thread(serverListener);
		thread2.start();

	}

	/**
	 * Tworzy nowe połączenie jako klient. Po jego uzyskaniu daje znać innym metodą,
	 * że połączono.
	 */
	public synchronized void clientRun() {
		client = new Client(clientSettings.getIp(), clientSettings.getPort(), clientName);
		notifyAll();

	}

	/**
	 * Czeka aż będzie uzyskane połącznie jako klient. Jeżeli uzyskanasłuchuję
	 * sygnału od serwera. Pierwsze, to dostaje nick host'a. Pokazuję go w oknie i
	 * włącza kolejne- gry.
	 * 
	 */
	public synchronized void serverListener() {
		if (client == null) {
			try {

				wait();
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}

		client.addObserver((obj, arg) -> {

			if (arg instanceof Player) {
				// pokazuje kto sie polaczyl
				hostPlayer = (Player) arg;
				TF1.setText((hostPlayer.getNick() + " się połączył!"));
				
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				Platform.runLater(new Runnable() {
					@Override
					public void run() {

						try {
							FXMLLoader loader = new FXMLLoader(
									Main.class.getResource("/view/ClientGameWindowView.fxml"));
							AnchorPane gamePane = loader.load();

							Stage gameStage = new Stage();
							gameStage.setMinWidth(800.0);
							gameStage.setMinHeight(600.0);
							gameStage.setTitle("Kalambury - Gra");
							gameStage.initModality(Modality.WINDOW_MODAL);

							Scene scene = new Scene(gamePane);
							gameStage.setScene(scene);

							ClientGameWindowController gameWindowController = loader.getController();
							gameWindowController.setStage(gameStage);
							gameWindowController.setClientPlayer(clientPlayer);
							gameWindowController.setHostPlayer(hostPlayer);
							gameWindowController.setClient(client);
							gameWindowController.setSettings(clientSettings);
							gameWindowController.gameStart();

							waitingWindowStage.hide();
							gameStage.show();

						} catch (IOException e) {
							e.printStackTrace();
						}

					}
				});
			}

		});

	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Settings getClientSettings() {
		return clientSettings;
	}

	public void setClientSettings(Settings clientSettings) {
		this.clientSettings = clientSettings;
	}

	public String getclientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
		clientPlayer = new Player(clientName);
	}

	public void setStage(Stage waitingWindowStage) {
		this.waitingWindowStage = waitingWindowStage;
	}

}
