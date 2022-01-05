package application.controller;

import java.io.ByteArrayInputStream;
import java.util.Optional;

import application.model.ProductInCart;
import application.net.client.Client;
import application.net.common.Protocol;
import application.view.SceneHandler;
import application.view.StackPaneHome;
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
    
    public void setData(ProductInCart p) {
    	lblNomeProdotto.setText(p.getNomeProdotto());
    	lblPrezzo.setText(p.getPrezzoGenerico() + "$");
    	if(p.getImgProdotto() != null) {
    		imgProduct.setImage(new Image(new ByteArrayInputStream(p.getImgProdotto())));
    	}
    	else {
    		imgProduct.setImage(new Image(getClass().getResourceAsStream("/application/image/noImageProduct.png")));
    	}
    	quantità.setText(p.getQuantitaNelCarrello()+"");
    	descrizione.setText(p.getDescrizione());
    }
    
    @FXML
    void diminuisciQuantità(ActionEvent event) {
    	Integer number = Integer.parseInt(quantità.getText()) - 1;
    	if(number == 0) {
    		Alert alert = new Alert(AlertType.CONFIRMATION);
    		alert.setTitle("Confirmation Dialog");

    		alert.setHeaderText("Vuoi rimuovere il prodotto dal carrello?");
    		alert.setContentText("Premi OK per rimuovere");

    		Optional<ButtonType> result = alert.showAndWait();
    		
    		if (result.get() == ButtonType.OK){
    			quantità.setText(number + "");
    			if(Client.getInstance().removeProductFromCart(lblNomeProdotto.getText())) {
        			SceneHandler.getInstance().setCartInHome(StackPaneHome.getInstance());
    			}
    		} else {
    			System.out.println("NO");
    		}
    		
    	} else {
    		checkAndSetNewQuantity(number);
    	}
    }

    @FXML
    void aumentaQuantià(ActionEvent event) {
    	Integer number = Integer.parseInt(quantità.getText()) + 1 ;
    	
    	checkAndSetNewQuantity(number);
    	
    }
    
    private void checkAndSetNewQuantity(Integer number) {
    	Integer risposta = Client.getInstance().isAvailbleProduct(lblNomeProdotto.getText(), number);
    	
    	if(risposta == null) {
    		SceneHandler.getInstance().showError(Protocol.ERROR);
    		return;
    	}
    	//Se la risposta è zero allora la quantità chesta è disponibile, altrimenti viene restituito la disponibilità 
    	//effettiva
    	if(risposta == 0) {
    		quantità.setText(number + "");
    	} else {
    		SceneHandler.getInstance().showWarning("Attenzione! La quantità di prodotto pari a "
    				+ number +" richiesta, non è disponibile");
    		quantità.setText(risposta + "");
    	}
    }
}

