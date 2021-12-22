package application.controller;

import java.io.IOException;
import java.util.Vector;

import application.model.ProductInCart;
import application.net.client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CartController {

	@FXML
    private VBox vBoxCart;
	
	@FXML
    private Button btnProcediAllOrdine;
	
	@FXML
    private GridPane gridPaneCart;
    
    @FXML
    void initialize() {
    	vBoxCart.setPrefSize(730, 630);
    	vBoxCart.getStylesheets().add(getClass().getResource("/application/css/cart.css").toExternalForm());
    	/*imgCarrelloVuoto.setImage(new Image(getClass().getResourceAsStream("/application/image/emptyCart.jpg"),250,200,true,true));*/
    	//setProdottiInCart();
    	/*vBoxCart.prefWidthProperty().bind(SceneHandlerVecchio.getInstance().getStage().widthProperty());/*.multiply(0.3));*/
    	/*vBoxCart.prefHeightProperty().bind(SceneHandlerVecchio.getInstance().getStage().heightProperty());/*.multiply(0.3));*/
    }
    
    public boolean setProdottiInCart(){
    	int column = 0;
		int row = 1;	

		Vector<ProductInCart> prodotti = Client.getInstance().getProductInCart();

		double totale = 0.0;
		
		if(prodotti == null || prodotti.size() == 0)
			return false;
		
		try {
			for(int i = 0 ; i < prodotti.size(); ++i) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/clienti/productInCart.fxml"));
				HBox prodotto = (HBox) loader.load();
				ProductCartController controller = loader.getController();	
				controller.setData(prodotti.get(i));
				
				totale += prodotti.get(i).getPrezzoGenerico() * prodotti.get(i).getQuantitaNelCarrello();
				
				gridPaneCart.add(prodotto,column,row++);
			}
			System.out.println(totale);
			return true;
		} catch (IOException e) {
			System.out.println("Errore nel cart controller" + e.getMessage());
			return false;
		}
	}
    
    @FXML
    void clickProcediBtn(ActionEvent event) {
    	Client.getInstance().procediAllOrdine();
    }
    
}

