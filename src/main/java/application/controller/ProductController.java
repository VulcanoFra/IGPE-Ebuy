package application.controller;

import java.io.ByteArrayInputStream;
import application.model.Product;
import application.net.client.Client;
import application.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class ProductController {

    @FXML
    private ImageView imgProduct;

    @FXML
    private Label labelPrezzo;

    @FXML
    private Label labelNomeProdotto;
    
    @FXML
    private VBox vBoxProdotto;

    @FXML
    private Button addCartBtn;
    
    private String descrizione;
    
    public void setData(Product p) {  	
    	if(p.getPrezzoAttuale() == p.getPrezzoGenerico()) {
        	labelPrezzo.setText(p.getPrezzoGenerico()+" $");
        	labelPrezzo.setStyle("-fx-text-fill: white;");
    	} else {
        	labelPrezzo.setText(p.getPrezzoAttuale()+" $");
        	labelPrezzo.setStyle("-fx-text-fill: linear-gradient(to bottom, #ec9f05, #ff4e00);");
    	}
    	
    	labelNomeProdotto.setText(p.getNomeProdotto());
    	if(p.getImgProdotto() != null)
    		imgProduct.setImage(new Image(new ByteArrayInputStream(p.getImgProdotto()), 160, 160, false, false)); 
    	else 
    		imgProduct.setImage(new Image(getClass().getResourceAsStream("/application/image/noImageProduct.png")));
    	
    	descrizione = p.getDescrizione();
    }
    
    @FXML
    void initialize() {
    	vBoxProdotto.getStylesheets().add(getClass().getResource("/application/css/product.css").toExternalForm());
    }
    
    @FXML
    void clickOnProdotto(MouseEvent event) {
    	SceneHandler.getInstance().getPaneDetailsProduct(labelNomeProdotto.getText(), descrizione, labelPrezzo.getText(), imgProduct.getImage());
    }

    @FXML
    void clickAddCart(ActionEvent event) {
    	Client.getInstance().addAProductInCart(labelNomeProdotto.getText());
    }
}