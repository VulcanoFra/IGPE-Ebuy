package application.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class DashboardController {

    @FXML
    private Label lblIniziale;

    @FXML
    private AnchorPane anchorPaneDash;
    
    @FXML
    void initialize() {
    	lblIniziale.setText("CIAO BROOO");
    	anchorPaneDash.setStyle("-fx-background-color: white");
    }
}

