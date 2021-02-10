package view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
/**
 * Kontroler JavaFX okienka z regułami gry
 *
 * @author Katarzyna Samkowska / katarzyna.samkowska@gmail.com
 * @version 1.0
 * @since 2019-01-20
 */
public class RulesWindowController {
	private Stage rulesWindowStage;
	@FXML private Button rules;

	 public void setStage(Stage rulesWindowStage){
	  	  this.rulesWindowStage=rulesWindowStage;
	    }
	
    @FXML private Button closeRules;
   
	@FXML public void closeRules(){
		rulesWindowStage.close();
	}
	
	
	
	}
