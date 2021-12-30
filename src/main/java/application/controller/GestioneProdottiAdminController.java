package application.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import application.model.Product;
import application.net.client.Client;
import application.net.common.Protocol;
import application.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class GestioneProdottiAdminController {

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

        	System.out.println(file.toPath());
        	try {
        		byte[] imageBytes = Files.readAllBytes(file.toPath());
        	    imgCurrentProduct = imageBytes;
        	    imageSelected.setImage(new Image(file.toURI().toString()));
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
