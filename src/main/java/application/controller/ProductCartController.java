package application.controller;

import java.io.ByteArrayInputStream;
import java.util.Optional;

import application.model.ProductInCart;
import application.net.client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ProductCartController {

    @FXML
    private ImageView imgProduct;

    @FXML
    private Label descrizione;
    
    @FXML
    private Button btnDiminuisciQuantità;

    @FXML
    private Label lblNomeProdotto;

    @FXML
    private Label quantità;

    @FXML
    private Button btnAumentaQuantità;

    @FXML
    private Label lblPrezzo;

    @FXML
    void initialize() {
    	btnAumentaQuantità.getStyleClass().add("Quantita");
    	btnDiminuisciQuantità.getStyleClass().add("Quantita");
    }
    
    @FXML
    void diminuisciQuantità(ActionEvent event) {
    	int number = Integer.parseInt(quantità.getText());
    	if(number == 1) {
    		Alert alert = new Alert(AlertType.CONFIRMATION);
    		alert.setTitle("Confirmation Dialog");

    		alert.setHeaderText("Vuoi rimuovere il prodotto dal carrello?");
    		alert.setContentText("Premi OK per rimuovere");

    		Optional<ButtonType> result = alert.showAndWait();
    		
    		if (result.get() == ButtonType.OK){
    			number--;
    			quantità.setText(number + "");
    			Client.getInstance().removeProductFromCart(this.lblNomeProdotto.getText());
    		} else {
    			System.out.println("NO");
    		}
    		
    	} else {
    		number--;
			quantità.setText(number + "");
    	}
    	  	
    	
    }

    @FXML
    void aumentaQuantià(ActionEvent event) {
    	int number = Integer.parseInt(quantità.getText());
    	number++;
    	quantità.setText(number + "");
    }
    
    public void setData(ProductInCart p) {
    	lblNomeProdotto.setText(p.getNomeProdotto());
    	lblPrezzo.setText(p.getPrezzoGenerico() + "$");
    	if(p.getImgProdotto() != null) {
    		imgProduct.setImage(new Image(new ByteArrayInputStream(p.getImgProdotto())));
    	}
    	else {
    		imgProduct.setImage(new Image(getClass().getResourceAsStream("/application/image/noImageProduct.png")));
    	}
    	quantità.setText(p.getQuantità()+"");
    	descrizione.setText(p.getDescrizione());
    }
}

