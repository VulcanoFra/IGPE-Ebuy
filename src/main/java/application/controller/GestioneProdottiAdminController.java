package application.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import application.model.Product;
import application.net.client.Client;
import application.net.common.Protocol;
import application.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class GestioneProdottiAdminController {

	@FXML
    private ImageView removeImage;
	
    @FXML
	private TextArea descrizioneTextArea;
	
    @FXML
    private TextField prezzoField;

    @FXML
    private Button btnAddProduct;

    @FXML
    private TextField categoriaField;

    @FXML
    private Button btnScegliImage;

    @FXML
    private TextField nomeField;

    @FXML
    private ImageView imageSelected;
    
    @FXML
    private AnchorPane adminAnchorPane;
    
    @FXML
    private TextField quantitaField;

    private byte[] imgCurrentProduct = null;
    
    @FXML
    void initialize() {
    	adminAnchorPane.getStylesheets().add(getClass().getResource("/application/css/loginAndRegister.css").toExternalForm());
    	imageSelected.setImage(new Image(getClass().getResourceAsStream("/application/image/noImageProduct.png"), 200, 200, true, true));
    	removeImage.setVisible(false);
    	addListener();
    }
    
    private void addListener() {
    	removeImage.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				Alert alert = new Alert(AlertType.CONFIRMATION, "Sei sicuro di voler rimuovere l'immagine?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
		    	alert.showAndWait();

		    	if(alert.getResult() == ButtonType.YES) {
		    		imageSelected.setImage(new Image(getClass().getResourceAsStream("/application/image/noImageProduct.png"), 200, 200, true, true));
		        	removeImage.setVisible(false);
		    	}		    	
			}
		});
    }
    
    @FXML
    void clickScegliImmagine(ActionEvent event) {
    	try {
    		FileChooser fileChooser = new FileChooser();
    		FileChooser.ExtensionFilter extFilterJPG =  new FileChooser.ExtensionFilter("JPG files (*.JPG)", "*.JPG");
            FileChooser.ExtensionFilter extFilterjpg = new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.PNG)", "*.PNG");
            FileChooser.ExtensionFilter extFilterpng = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
            fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterjpg, extFilterPNG, extFilterpng);
        	fileChooser.setTitle("Open Resource File");
        	File file = fileChooser.showOpenDialog(new Stage());

        	try {
        		byte[] imageBytes = Files.readAllBytes(file.toPath());
        	    imgCurrentProduct = imageBytes;
        	    imageSelected.setImage(new Image(file.toURI().toString(), 200, 200, true, true));
        	    removeImage.setVisible(true);
        	} catch (IOException e) {
        		System.out.println("File couldn't be read to byte[].");
        	}
    	}
    	catch(RuntimeException e) {
    		if(imgCurrentProduct == null)
    			SceneHandler.getInstance().showWarning("Attenzione! Il prodotto non avr� nessuna immagine");
    		return;
    	}
    }

    private void clearField() {
    	nomeField.setText("");
    	categoriaField.setText("");
    	quantitaField.setText("");
    	prezzoField.setText("");
    	imgCurrentProduct = null;
    	imageSelected.setImage(null);
    }
    
    @FXML
    void clickAddProduct(ActionEvent event) {
    	if(nomeField.getText().equals("") || prezzoField.getText().equals("") || quantitaField.getText().equals("") || categoriaField.getText().equals("")) {
    		SceneHandler.getInstance().showError("Hai lasciato dei campi vuoti");
    		return;
    	}
    	
    	Product p = new Product( nomeField.getText(), Double.parseDouble(prezzoField.getText()), Integer.parseInt(quantitaField.getText()), 
				imgCurrentProduct, categoriaField.getText(), descrizioneTextArea.getText());
		
		String res = Client.getInstance().addProduct(p);
		
		if(res.equals(Protocol.OK)) 
			SceneHandler.getInstance().showInfo("Prodotto aggiunto correttamente");
		else if(res.equals(Protocol.CANNOT_ADD_PRODUCT)) 
			SceneHandler.getInstance().showInfo(res);
		
		clearField();
    }
    
}
