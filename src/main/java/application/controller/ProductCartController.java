package application.controller;

import java.io.ByteArrayInputStream;

import application.model.ProductInCart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    
    @FXML
    void diminuisciQuantit�(ActionEvent event) {
    	int number = Integer.parseInt(quantit�.getText());
    	number--;
    	quantit�.setText(number + "");
    }

    @FXML
    void aumentaQuanti�(ActionEvent event) {
    	int number = Integer.parseInt(quantit�.getText());
    	number++;
    	quantit�.setText(number + "");
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
    	quantit�.setText(p.getQuantit�()+"");
    	descrizione.setText(p.getDescrizione());
    }
}

