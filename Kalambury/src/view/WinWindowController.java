package view;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Player;
import model.Results;
import model.Settings;
import model.Word;
import net.Server;

/**
 * Kontroler JavaFX okienka z wynikami gry
 *
 * @author Katarzyna Samkowska / katarzyna.samkowska@gmail.com
 * @version 1.0
 * @since 2019-01-20
 */
public class WinWindowController {
	private Stage winWindowStage;
	private ObservableList<Results> resultsList = FXCollections.observableArrayList();
	private Server server;
	private Settings settings;

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public Stage getHostGameWindowController() {
		return hostGameWindowController;
	}

	public void setHostGameWindowController(Stage hostGameWindowController) {
		this.hostGameWindowController = hostGameWindowController;
	}

	public Stage getClientGameWindowController() {
		return clientGameWindowController;
	}

	public void setClientGameWindowController(Stage clientGameWindowController) {
		this.clientGameWindowController = clientGameWindowController;
	}

	private Stage hostGameWindowController = null;
	private Stage clientGameWindowController = null;

	public Stage getWinWindowStage() {
		return winWindowStage;
	}

	public void setStage(Stage winWindowStage) {
		this.winWindowStage = winWindowStage;
	}

	@FXML
	private Button closeWin;
	@FXML
	private Button backToChat;
	@FXML
	private Label winLabel;
	@FXML
	private Label lossLabel;
	@FXML
	private Label winNickLabel;
	@FXML
	private Label lossNickLabel;
	@FXML
	private Label winPointsLabel;
	@FXML
	private Label lossPointsLabel;

	public void closeWin() {
		winWindowStage.close();
	}

	public void backToMenu() {

		if (clientGameWindowController != null) {
			clientGameWindowController.close();
		}

		if (hostGameWindowController != null) {
			hostGameWindowController.close();
		}
		server.stop();
		winWindowStage.close();

	}

	public void saveResults() {

		BufferedWriter bw = null;

		try {

			File file = new File(settings.getResultListUrl());

			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file);
			bw = new BufferedWriter(fw);

			for (int x = 0; x < resultsList.size(); x++) {

				bw.write(resultsList.get(x).getId() + "\t" + resultsList.get(x).getNick1() + "\t"
						+ resultsList.get(x).getResult1() + "\t" + resultsList.get(x).getNick2() + "\t"
						+ resultsList.get(x).getResult2() + "\t" + resultsList.get(x).getDate()

				);
				bw.newLine();

			}

			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void loadData() {

		
		try {
			// InputStream in = getClass().getResourceAsStream(settings.getResultListUrl());
			InputStream in = new FileInputStream(settings.getResultListUrl());
			BufferedReader input = new BufferedReader(new InputStreamReader(in));

			String line = "";
			String cvsSplitBy = "\t";

			while ((line = input.readLine()) != null) {
				// adding words into memory
				// id, word, category, hint1, hint2
				Object[] word = line.split(cvsSplitBy);

				resultsList.add(new Results(Integer.valueOf((String) word[0]), (String) word[1],
						Integer.valueOf((String) word[2]), (String) word[3], Integer.valueOf((String) word[4]),
						(String) word[5]));
			}

			input.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (NoSuchElementException e1) {
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		resultsList
				.add(new Results(resultsList.size(), winNickLabel.getText(), Integer.parseInt(winPointsLabel.getText()),
						lossNickLabel.getText(), Integer.parseInt(lossPointsLabel.getText()), date()));

	}

	public String date() {

		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd HH.mm");
		return ft.format(dNow);

	}

	public void setWinner(Player player) {

		winNickLabel.setText(player.getNick());
		winPointsLabel.setText(Integer.toString(player.getPoints()));

	}

	public void setLosser(Player player) {

		lossNickLabel.setText(player.getNick());
		lossPointsLabel.setText(Integer.toString(player.getPoints()));

	}

}
