package application.controller;

import java.io.IOException;
import java.util.ArrayList;

import application.model.Product;
import application.net.client.Client;
import application.view.SceneHandler;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class AllProductController {

    @FXML
    private FlowPane flowPaneProduct;

    @FXML
    private ScrollPane scrollPane;
        
    @FXML
    void initialize() {
    	flowPaneProduct.prefWidthProperty().bind(Bindings.add(-5, scrollPane.widthProperty()));
    	flowPaneProduct.prefHeightProperty().bind(Bindings.add(-5, scrollPane.heightProperty()));
    	scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
    	scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
    }
    
    public void setProdottiPane(String parametro) throws IOException {

    	flowPaneProduct.getChildren().clear();
    	
		ArrayList<Product> prodotti = new ArrayList<Product>();
		prodotti = Client.getInstance().getProduct(parametro);
		
		if(prodotti == null || prodotti.size() == 0) {
			SceneHandler.getInstance().showWarning("Non sono presenti prodotti ricercati");
			return;
		}
		
		
		for(int i = 0 ; i < prodotti.size() ; ++i) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/clienti/product.fxml"));
			VBox prodotto = (VBox) loader.load();
			
			ProductController pControl = loader.getController();
			pControl.setData(prodotti.get(i));
			
			flowPaneProduct.getChildren().add(prodotto);
		}
		
	}

	public void setProdottiPaneByCategory(String category) throws IOException {
		
	flowPaneProduct.getChildren().clear();
    	
		ArrayList<Product> prodotti = new ArrayList<Product>();
		prodotti = Client.getInstance().getProductByCategory(category);	
		
		if(prodotti == null || prodotti.size() == 0) {
			SceneHandler.getInstance().showWarning("Non sono presenti prodotti ricercati");
			return;
		}	
		
		for(int i = 0 ; i < prodotti.size() ; ++i) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/clienti/product.fxml"));
			VBox prodotto = (VBox) loader.load();
			
			ProductController pControl = loader.getController();
			pControl.setData(prodotti.get(i));
			
			flowPaneProduct.getChildren().add(prodotto);
		}
	}

}
