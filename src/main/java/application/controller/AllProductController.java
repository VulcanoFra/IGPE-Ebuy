package application.controller;

import java.io.IOException;
import java.util.Vector;

import application.model.Product;
import application.net.client.Client;
import application.view.SceneHandlerVecchio;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class AllProductController {

    @FXML
    private GridPane gridAllProductPane;

    @FXML
    private Label allLabel;
        
    @FXML
    void initialize() {
    }
    
    public void setProdottiPane(String parametro) throws IOException {
		int column = 0;
		int row = 1;	

		Vector<Product> prodotti = new Vector<Product>();
		prodotti = Client.getInstance().getProduct(parametro);
		
		System.out.println("sto qui");
		
		if(prodotti == null || prodotti.size() == 0) {
			SceneHandlerVecchio.getInstance().showWarning("Non sono presenti prodotti ricercati");
			return;
		}
		
		
		for(int i = 0 ; i < prodotti.size() ; ++i) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/clienti/product.fxml"));
			VBox prodotto= (VBox) loader.load();
			
			ProductController pControl = loader.getController();
			pControl.setData(prodotti.get(i));
			
			if(column == 4) {
				column = 0;
				row++;
			}
			
			gridAllProductPane.add(prodotto, column++, row);
		}
		
	}

}
