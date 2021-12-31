package application.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class DashboardController {

    @FXML
    private Label lblIniziale;

    @FXML
    private AnchorPane anchorPaneDash;
    
    @FXML
    private ImageView imageLogo;
    
    @FXML
    private Label lblNome;
    
    @FXML
    void initialize() {
    	lblIniziale.setText("CIAO BROOO");
    	anchorPaneDash.setStyle("-fx-background-color: white");
    }
}

