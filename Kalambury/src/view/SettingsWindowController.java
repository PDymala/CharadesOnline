package view;

import java.awt.FileDialog;
import java.awt.Frame;
import java.util.Observable;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Settings;
import net.ServerIP;

/**
 * Kontroler JavaFX okienka z ustawieniami
 *
 * @author Katarzyna Samkowska / katarzyna.samkowska@gmail.com
 * @version 1.0
 * @since 2019-01-20
 */
public class SettingsWindowController extends Observable {
	private Stage settingsWindowStage;
	@FXML
	TextField TFportHost;
	@FXML
	TextField TFroundHost;
	@FXML
	TextField TFadressIPHost;
	@FXML
	Label labelAdressIPHost;
	@FXML
	Label labelPortHost;
	@FXML
	Label labelRoundHost;

	@FXML
	Label labelResultFileUrl;
	
	@FXML
	TextField TFportClient;
	@FXML
	TextField TFadressIPClient;
	@FXML
	TextField TFResultFileUrl;
	
	@FXML
	Label labelAdressIPClient;
	@FXML
	Label labelPortClient;

	@FXML
	Label label1;
	@FXML
	Label label2;
	ServerIP serverIPClient = new ServerIP();

	@FXML
	public void initialize() {

		TFadressIPHost.setText(serverIPClient.getIpAdress());
	}

	public void setOldSettingsHost(Settings oldSettingsHost) {

		TFportHost.setText(Integer.toString(oldSettingsHost.getPort()));
		TFroundHost.setText(Integer.toString(oldSettingsHost.getRounds()));

		if  (!oldSettingsHost.getResultListUrl().isEmpty()) {
		
		TFResultFileUrl.setText(oldSettingsHost.getResultListUrl());
		}}

	public void setOldSettingsClient(Settings oldSettingsClient) {

		TFportClient.setText(Integer.toString(oldSettingsClient.getPort()));
		TFadressIPClient.setText(oldSettingsClient.getIp());
		
		if (!oldSettingsClient.getResultListUrl().isEmpty()) {
			TFResultFileUrl.setText(oldSettingsClient.getResultListUrl());			
		}
	}

	public void setStage(Stage settingsWindowStage) {
		this.settingsWindowStage = settingsWindowStage;
	}

	
	@FXML
	private Button selectFileButton;
	
	public void selectResultFile() {
		
		JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
               "tsv files", "tsv");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
        	TFResultFileUrl.setText( chooser.getSelectedFile().getAbsolutePath());

        }

		
		
		    
	}
	
	
	@FXML
	private Button closeSettings;

	@FXML
	public void closeSettings() {

	
		Settings newSettings[] = new Settings[2];

		newSettings[0] = new Settings(Integer.parseInt(TFportHost.getText()), Integer.parseInt(TFroundHost.getText()),
				Settings.Type.HOST);
		newSettings[0].setResultListUrl(TFResultFileUrl.getText());
		newSettings[1] = new Settings(Integer.parseInt(TFportClient.getText()), TFadressIPClient.getText(),
				Settings.Type.CLIENT);
		newSettings[1].setResultListUrl(TFResultFileUrl.getText());
		
		setChanged();
		notifyObservers(newSettings);

		settingsWindowStage.close();
	}

}
