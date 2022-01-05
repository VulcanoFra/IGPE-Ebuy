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
    private Button btnDiminuisciQuantit�;

    @FXML
    private Label lblNomeProdotto;

    @FXML
    private Label quantit�;

    @FXML
    private Button btnAumentaQuantit�;

    @FXML
    private Label lblPrezzo;

    @FXML
    void initialize() {
    	btnAumentaQuantit�.getStyleClass().add("Quantita");
    	btnDiminuisciQuantit�.getStyleClass().add("Quantita");
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
    	quantit�.setText(p.getQuantitaNelCarrello()+"");
    	descrizione.setText(p.getDescrizione());
    }
    
    @FXML
    void diminuisciQuantit�(ActionEvent event) {
    	Integer number = Integer.parseInt(quantit�.getText()) - 1;
    	if(number == 0) {
    		Alert alert = new Alert(AlertType.CONFIRMATION);
    		alert.setTitle("Confirmation Dialog");

    		alert.setHeaderText("Vuoi rimuovere il prodotto dal carrello?");
    		alert.setContentText("Premi OK per rimuovere");

    		Optional<ButtonType> result = alert.showAndWait();
    		
    		if (result.get() == ButtonType.OK){
    			quantit�.setText(number + "");
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
    void aumentaQuanti�(ActionEvent event) {
    	Integer number = Integer.parseInt(quantit�.getText()) + 1 ;
    	
    	checkAndSetNewQuantity(number);
    	
    }
    
    private void checkAndSetNewQuantity(Integer number) {
    	Integer risposta = Client.getInstance().isAvailbleProduct(lblNomeProdotto.getText(), number);
    	
    	if(risposta == null) {
    		SceneHandler.getInstance().showError(Protocol.ERROR);
    		return;
    	}
    	//Se la risposta � zero allora la quantit� chesta � disponibile, altrimenti viene restituito la disponibilit� 
    	//effettiva
    	if(risposta == 0) {
    		quantit�.setText(number + "");
    	} else {
    		SceneHandler.getInstance().showWarning("Attenzione! La quantit� di prodotto pari a "
    				+ number +" richiesta, non � disponibile");
    		quantit�.setText(risposta + "");
    	}
    }
}

