package application.controller;

import java.io.ByteArrayInputStream;

import application.model.Product;
import application.net.client.Client;
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
    
    public void setData(Product p) {  	
    	labelPrezzo.setText(p.getPrezzoGenerico()+" $");
    	labelNomeProdotto.setText(p.getNomeProdotto());
    	if(p.getImgProdotto() != null)
    		imgProduct.setImage(new Image(new ByteArrayInputStream(p.getImgProdotto()), 160, 160, false, false)); 
    	else 
    		imgProduct.setImage(new Image(getClass().getResourceAsStream("/application/image/noImageProduct.png")));
    }
    
    @FXML
    void initialize() {
    	vBoxProdotto.getStylesheets().add(getClass().getResource("/application/css/product.css").toExternalForm());
    }
    
    @FXML
    void clickOnProdotto(MouseEvent event) {
    	System.out.println(labelNomeProdotto.getText());
    }

    @FXML
    void clickAddCart(ActionEvent event) {
    	Client.getInstance().addAProductInCart(labelNomeProdotto.getText());
    }
}