package application.controller;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import application.model.ProductInCart;
import application.net.client.Client;
import application.net.common.Protocol;
import application.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class CartController {

	@FXML
    private Label lblRiepilogo;
	
	@FXML
	private ScrollPane scrollPane;
	
	@FXML
    private BorderPane borderPaneCart;
	
	@FXML
    private Button btnProcediAllOrdine;
	
	@FXML
    private GridPane gridPaneCart;
    
    @FXML
    void initialize() {
    	borderPaneCart.setPrefSize(730, 630);
    	borderPaneCart.getStylesheets().add(getClass().getResource("/application/css/cart.css").toExternalForm());
    	scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
    	scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
    }
    
    public boolean setProdottiInCart(){
    	clearCart();
    	
    	int column = 0;
		int row = 1;	

		ArrayList<ProductInCart> prodotti = Client.getInstance().getProductInCart();

		double totale = 0.0;
		
		if(prodotti == null || prodotti.size() == 0)
			return false;
		
		try {
			for(int i = 0 ; i < prodotti.size(); ++i) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/clienti/productInCart.fxml"));
				VBox prodotto = (VBox) loader.load();
				prodotto.prefWidthProperty().bind( scrollPane.widthProperty());
				ProductCartController controller = loader.getController();	
				controller.setData(prodotti.get(i));
				
				if(prodotti.get(i).getPrezzoGenerico() == prodotti.get(i).getPrezzoAttuale())
					totale += prodotti.get(i).getPrezzoGenerico() * prodotti.get(i).getQuantitaNelCarrello();
				else
					totale += prodotti.get(i).getPrezzoAttuale() * prodotti.get(i).getQuantitaNelCarrello();
				
				gridPaneCart.add(prodotto,column,row++);
			}

			if(totale > 50)
				lblRiepilogo.setText("Riepilogo Ordine: \n"
						   + "SubTotale = " + new DecimalFormat("##.##").format(totale) + "$ \n"
						   + "--------------------------- \n"
						   + "Totale = " + new DecimalFormat("##.##").format(totale) + "$");
			else
				lblRiepilogo.setText("Riepilogo Ordine: \n"
						   + "SubTotale:" + new DecimalFormat("##.##").format(totale) + "$ \n"
						   + "Spedizione: " + 5.0 + "$ \n"
						   + "--------------------------- \n"
						   + "Totale = " + new DecimalFormat("##.##").format(totale + 5.0) + "$");
			return true;
		} catch (IOException e) {
			return false;
		}
	}
    
    public void clearCart() {
    	gridPaneCart.getChildren().clear();
    }
    
    @FXML
    void clickProcediBtn(ActionEvent event) {
    	boolean procedi = SceneHandler.getInstance().showConfirm();
    	
    	if(procedi) {
    		String risposta = Client.getInstance().procediAllOrdine();
        	
        	if(risposta.equals(Protocol.ORDER_SUCCESS)) {
        		SceneHandler.getInstance().showInfo(risposta);
        		SceneHandler.getInstance().setProductInHome("");
        	} else if(risposta.equals(Protocol.SOME_PRODUCT_ARE_UNAVAILABLE)){
        		SceneHandler.getInstance().showWarning(risposta);
        	} else if(risposta.equals(Protocol.ERROR_DB)){
        		SceneHandler.getInstance().showError(risposta);
        	}
    	} 
    	
    }
}

