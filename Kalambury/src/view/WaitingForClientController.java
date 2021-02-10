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
import net.Server;
/**
 * Kontroler JavaFX okienka oczekiwania na połączenie klienta
 *
 * @author Piotr Dymala / p.dymala@gmail.com
 * @version 1.0
 * @since 2019-01-20
 */
public class WaitingForClientController {

	@FXML
	private TextField TF1;
	@FXML
	private Label Label1;
	private Server server;
	private Settings hostSettings;
	Player hostPlayer;
	Player clientPlayer;

	public Settings getHostSettings() {
		return hostSettings;
	}

	public void setHostSettings(Settings hostSettings) {
		this.hostSettings = hostSettings;
	}

	private String hostName;
	private Stage waitingWindowStage;

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public void setStage(Stage waitingWindowStage) {
		this.waitingWindowStage = waitingWindowStage;
	}

	public void serverStart() {

		Runnable serverRun = () -> {

			serverRun();
		};

		Runnable serverListener = () -> {
			serverListener();
		};

		Thread thread = new Thread(serverRun);
		thread.start();
		Thread thread2 = new Thread(serverListener);
		thread2.start();

	}

	public synchronized void serverRun() {

		server = new Server(hostSettings.getPort(), hostName);
		notifyAll();
	}

	public synchronized void serverListener() {
		if (server == null) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		server.addObserver((obj, arg) -> {
		
				if (arg instanceof String) {
					clientPlayer = new Player((String) arg);
					
					TF1.setText(clientPlayer.getNick() + " się połączył!");

					// wysyla czesc do klienta

					// czeka 1,5s
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
										Main.class.getResource("/view/HostGameWindowView.fxml"));
								AnchorPane gamePane = loader.load();

								Stage gameStage = new Stage();
								gameStage.setMinWidth(800.0);
								gameStage.setMinHeight(600.0);
								gameStage.setTitle("Kalambury - Gra");
								gameStage.initModality(Modality.WINDOW_MODAL);
								gameStage.initOwner(waitingWindowStage);

								Scene scene = new Scene(gamePane);
								gameStage.setScene(scene);

								HostGameWindowController gameWindowController = loader.getController();
								gameWindowController.setStage(gameStage);
								gameWindowController.sethostPlayer(new Player(hostName));
								gameWindowController.setClientPlayer(clientPlayer);
								gameWindowController.setServer(server);
								gameWindowController.setSettings(hostSettings);
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

}
