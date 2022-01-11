package application.controller;

import java.util.Optional;

import application.net.client.Client;
import application.net.common.Protocol;
import application.view.SceneHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Pair;

public class AdminHomePageController {

    @FXML
    private Button btnDownloadCatalogo;

    @FXML
    private MenuItem btnLogOut;

    @FXML
    private StackPane stackPaneAdminHome;
    
    @FXML
    private Button bntGestisciProdotti;

    @FXML
    private MenuItem btnChangePassword;

    @FXML
    private BorderPane borderPaneHomeAdmin;

    @FXML
    private Button btnModificaQuantita;

    @FXML
    private MenuButton btnProfilo;

    @FXML
    void initialize() {
    	borderPaneHomeAdmin.getStylesheets().add(getClass().getResource("/application/css/homePage.css").toExternalForm());
    	btnProfilo.getStyleClass().add("Profilo");
    }
    
    @FXML
    void clickGestisciProdotti(ActionEvent event) {
    	SceneHandler.getInstance().setGestioneProdottiAdmin(stackPaneAdminHome);
    }
    
    @FXML
    void clickExit(ActionEvent event) {
    	Client.getInstance().exit();
    	try {
			SceneHandler.getInstance().resetPage(stackPaneAdminHome);
			SceneHandler.getInstance().setLoginScene();
		} catch (Exception e) {
			SceneHandler.getInstance().showError(Protocol.ERROR);
		}
    }
    
    @FXML
    void clickAggiungiQuantita(ActionEvent event) {
    	Dialog<Pair<String, String>> dialog = new Dialog<>();
    	dialog.setTitle("Aggiunta quantità prodotto");
    	dialog.setHeaderText("Inserisci il nome del prodtto e la quantità di prodotto disponibile che vuoi aggiungere");

    	ButtonType okButtonType = new ButtonType("Ok", ButtonData.OK_DONE);
    	dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

    	GridPane grid = new GridPane();
    	grid.setHgap(10);
    	grid.setVgap(10);
    	grid.setPadding(new Insets(20, 150, 10, 10));

    	TextField nomeProdottoField = new TextField();
    	nomeProdottoField.setPromptText("Nome Prodotto");
    	TextField quantitaProdottoField = new TextField();
    	quantitaProdottoField.setPromptText("Quantità");

    	grid.add(new Label("Nome prodotto:"), 0, 0);
    	grid.add(nomeProdottoField, 1, 0);
    	grid.add(new Label("Quantità:"), 0, 1);
    	grid.add(quantitaProdottoField, 1, 1);

    	Node okButton = dialog.getDialogPane().lookupButton(okButtonType);
    	okButton.setDisable(true);

    	nomeProdottoField.textProperty().addListener((observable, oldValue, newValue) -> {
    		okButton.setDisable(newValue.trim().isEmpty());
    	});

    	quantitaProdottoField.textProperty().addListener((observable, oldValue, newValue) -> {
    		okButton.setDisable(!newValue.trim().matches("[0-9]*"));
    	});
    	dialog.getDialogPane().setContent(grid);

    	Platform.runLater(() -> nomeProdottoField.requestFocus());

    	dialog.setResultConverter(dialogButton -> {
    	    if (dialogButton == okButtonType) {
    	        return new Pair<>(nomeProdottoField.getText(), quantitaProdottoField.getText());
    	    } 
    	    return null;
    	});

    	Optional<Pair<String, String>> result = dialog.showAndWait();

    	if(!result.isEmpty()) {
    		Pair<String,String> risultati = result.get();
    	
	    	try {
	    		String nomeProdotto = risultati.getKey();
	        	Integer quantita = Integer.parseInt(risultati.getValue());
	        	
	        	if(Client.getInstance().addQuantityOfProduct(nomeProdotto, quantita))
	        		SceneHandler.getInstance().showInfo("Aggiornamento riuscito!");
	        	else 
	        		SceneHandler.getInstance().showWarning("Errore interno. Riprova più tardi");
	        	
	    	} catch(NumberFormatException e) {
	    		SceneHandler.getInstance().showError("Attenzione! Inserire un numero nel campo quantità");
	    	}
    	}
    }
    
    @FXML
    void clickChangePassword(ActionEvent event) {
    	SceneHandler.getInstance().setChangePassword(stackPaneAdminHome);

    }
}

