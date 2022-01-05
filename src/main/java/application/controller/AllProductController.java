package application.controller;

import java.io.IOException;
import java.util.Vector;

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
    	
		Vector<Product> prodotti = new Vector<Product>();
		prodotti = Client.getInstance().getProduct(parametro);
		
		System.out.println("sto qui");
		
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
