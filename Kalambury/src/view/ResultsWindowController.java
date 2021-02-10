package view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Results;
import model.Settings;

/**
 * Kontroler JavaFX okienka z wynikami gier
 *
 * @author Katarzyna Samkowska / katarzyna.samkowska@gmail.com
 * @version 1.0
 * @since 2019-01-20
 */
public class ResultsWindowController {
	private Stage resultsWindowStage;
	private ObservableList<Results> resultsList = FXCollections.observableArrayList();
	private Settings settings;

	Results results;
//nr	gracz1 wynik gracz2 wynik data
	@FXML
	private Button closeResults;
	@FXML
	private TableView<Results> tableResults;
	@FXML
	private TableColumn<Results, Integer> tableColumnNo;
	@FXML
	private TableColumn<Results, String> tableColumnNick1;
	@FXML
	private TableColumn<Results, Integer> tableColumnResult1;
	@FXML
	private TableColumn<Results, String> tableColumnNick2;
	@FXML
	private TableColumn<Results, Integer> tableColumnResut2;
	@FXML
	private TableColumn<Results, String> tableColumnDate;

	public void setStage(Stage resultsWindowStage) {
		this.resultsWindowStage = resultsWindowStage;
		loadData();

	}

	@FXML
	public void closeResultsWindow() {
		resultsWindowStage.close();
	}

	@FXML
	public void initialize() {
		tableResults.setPlaceholder(new Label("Nie ustawiono adresu pliku z wynikami"));
		/*
		 * tableColumnNo.prefWidthProperty().bind(tableResults.widthProperty().multiply(
		 * 0.05));
		 * tableColumnNick.prefWidthProperty().bind(tableResults.widthProperty().
		 * multiply(0.3));
		 * tableColumnResult.prefWidthProperty().bind(tableResults.widthProperty().
		 * multiply(0.15));
		 * tableColumnTestCount.prefWidthProperty().bind(tableResults.widthProperty().
		 * multiply(0.1));
		 * tableColumnTimer.prefWidthProperty().bind(tableResults.widthProperty().
		 * multiply(0.1));
		 * tableColumnFaultCount.prefWidthProperty().bind(tableResults.widthProperty().
		 * multiply(0.1));
		 * tableColumnDate.prefWidthProperty().bind(tableResults.widthProperty().
		 * multiply(0.2));
		 */
		tableColumnNo.setCellValueFactory(new PropertyValueFactory<Results, Integer>("id"));
		tableColumnNick1.setCellValueFactory(new PropertyValueFactory<Results, String>("nick1"));
		tableColumnResult1.setCellValueFactory(new PropertyValueFactory<Results, Integer>("result1"));
		tableColumnNick2.setCellValueFactory(new PropertyValueFactory<Results, String>("nick2"));
		tableColumnResut2.setCellValueFactory(new PropertyValueFactory<Results, Integer>("result2"));
		tableColumnDate.setCellValueFactory(new PropertyValueFactory<Results, String>("date"));

		tableResults.setItems(resultsList);

	}

	public void loadData() {

		int id;
		String nick1;
		int result1;
		String nick2;
		int result2;
		String date;

//		InputStream in = getClass().getResourceAsStream("Wyniki.tsv");

		String line = "";
		String cvsSplitBy = "\t";

		
		
		
		
		try {
			InputStream in;
			BufferedReader input;

			if (settings.getResultListUrl().isEmpty()) {
			

			} else {
				in = this.getClass().getResourceAsStream(settings.getResultListUrl());				
				input = new BufferedReader(new InputStreamReader(in));
			
				while ((line = input.readLine()) != null) {
					// adding words into memory
					// id, word, category, hint1, hint2
					Object[] word = line.split(cvsSplitBy);

					resultsList.add(new Results(Integer.valueOf((String) word[0]), (String) word[1],
							Integer.valueOf((String) word[2]), (String) word[3], Integer.valueOf((String) word[4]),
							(String) word[5]));
				}

				input.close();

			}

			

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (NoSuchElementException e1) {
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

}