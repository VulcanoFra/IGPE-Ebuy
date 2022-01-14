package application.controller;

import application.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ProductDetailsController {

	@FXML
    private Button btnTrendProduct;
	
	@FXML
    private Label descrizione;

    @FXML
    private Label prezzo;

    @FXML
    private Label nome;
    
    @FXML
    private ImageView imgProduct;
    
    public void setData(String n, String d, String p, Image img){
    	descrizione.setText(d);
    	prezzo.setText(p);
    	nome.setText(n);
    	imgProduct.setImage(img);
    }
    
    @FXML
    void clickTrendProduct(ActionEvent event) {
    	SceneHandler.getInstance().getPaneAndamentoProdotto(nome.getText());
    }

}
