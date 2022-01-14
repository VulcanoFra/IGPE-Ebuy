package application.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
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
    private ComboBox<String> comboBoxCategoria;
    
    @FXML
    private Button btnScegliImage;

    @FXML
    private TextField nomeField;

    @FXML
    private ImageView imageSelected;
    
    @FXML
    private VBox vBoxAddProduct;
    
    @FXML
    private TextField quantitaField;

    private byte[] imgCurrentProduct = null;
    
    @FXML
    void initialize() {
    	vBoxAddProduct.getStylesheets().add(getClass().getResource("/application/css/pageAdminProduct.css").toExternalForm());
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
    			SceneHandler.getInstance().showWarning("Attenzione! Il prodotto non avrà nessuna immagine");
    		return;
    	}
    }

    public void clearField() {
    	nomeField.setText("");
    	comboBoxCategoria.setPromptText("Categoria");
    	quantitaField.setText("");
    	prezzoField.setText("");
    	descrizioneTextArea.setText("");
    	imgCurrentProduct = null;
    	removeImage.setVisible(false);
		imageSelected.setImage(new Image(getClass().getResourceAsStream("/application/image/noImageProduct.png"), 200, 200, true, true));
    }
    
    @FXML
    void clickAddProduct(ActionEvent event) {
    	if(nomeField.getText().equals("") || prezzoField.getText().equals("") || quantitaField.getText().equals("") || comboBoxCategoria.getValue() == null) {
    		SceneHandler.getInstance().showError("Hai lasciato dei campi vuoti");
    		return;
    	}
    	
    	try {
    		int quantita = Integer.parseInt(quantitaField.getText());
    		double prezzoGenerico =  Double.parseDouble(prezzoField.getText());
    		double prezzoAttuale;
    		
    		if(quantita > 100)
    			prezzoAttuale = prezzoGenerico * 0.90;
    		else
    			prezzoAttuale = prezzoGenerico;
    		
    		Product p = new Product( nomeField.getText(), prezzoGenerico, prezzoAttuale, quantita, 
    				imgCurrentProduct, comboBoxCategoria.getValue(), descrizioneTextArea.getText());
    		
    		String res = Client.getInstance().addProduct(p);
    		
    		if(res.equals(Protocol.OK)) 
    			SceneHandler.getInstance().showInfo("Prodotto aggiunto correttamente");
    		else if(res.equals(Protocol.CANNOT_ADD_PRODUCT)) 
    			SceneHandler.getInstance().showInfo(res);
    		
    		clearField();
    	}catch(NumberFormatException e) {
    		SceneHandler.getInstance().showError("Attenzione! è stata inserita una stringa nel campo del prezzo o della quantità");
    	}
    	
    }

	public void riempiCombo() {
		ArrayList<String> categorie = Client.getInstance().getCategories();
    	for(String i : categorie) {
    		comboBoxCategoria.getItems().add(i);
    	}
	}

	public void pulisciCombo() {
		comboBoxCategoria.getItems().clear();
	}
    
}
