package application.controller;

import java.util.ArrayList;

import application.model.PdfGenerator;
import application.net.client.Client;
import application.net.common.Protocol;
import application.view.SceneHandler;
import application.view.StackPaneHome;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class HomePageController {

	@FXML
    private VBox slider;

    @FXML
    private ComboBox<String> comboBoxCategoria;

    @FXML
    private BorderPane borderPaneHome;

    @FXML
    private Button btnCatalogo;

    @FXML
    private ImageView imgLogo;

    @FXML
    private HBox hBox;

    @FXML
    private TextField ricercaField;

    @FXML
    private MenuItem itemChangePassword;

    @FXML
    private Button btnContatti;

    @FXML
    private Button downloadProductBtn;

    @FXML
    private MenuItem itemExit;

    @FXML
    private Button btnCart;

    @FXML
    private MenuButton btnProfilo;
    
    @FXML
    void initialize() {
    	borderPaneHome.getTop().getStyleClass().add(getRicerca());
    	borderPaneHome.getStylesheets().add(getClass().getResource("/application/css/homePage.css").toExternalForm());
    	btnProfilo.getStyleClass().add("Profilo");
    	imgLogo.setImage(new Image(getClass().getResourceAsStream("/application/image/logoBasso.png"),180 ,60 ,true,true));
    	borderPaneHome.setCenter(StackPaneHome.getInstance());	
    }
    
    public String getRicerca() {
    	return ricercaField.getText();
    }
    
    @FXML
    void clickCatalogoBtn(ActionEvent event) throws Exception {
    	SceneHandler.getInstance().setProductInHome("");
    }
    
    @FXML
    void clickExit(ActionEvent event) {
    	pulisciCombo();
    	ricercaField.setText("");
    	SceneHandler.getInstance();
    	Client.getInstance().exit();
    	try {		
			SceneHandler.getInstance().resetPage(StackPaneHome.getInstance());
			SceneHandler.getInstance().setLoginScene();	    	
		} catch (Exception e) {
			SceneHandler.getInstance().showError(Protocol.ERROR);
		}
    }
    
    @FXML
    void clickCart(ActionEvent event) {
    	SceneHandler.getInstance().setCartInHome(StackPaneHome.getInstance());	
    }
    
    @FXML
    void clickDownloadProductPDF(ActionEvent event) {
    	String res = PdfGenerator.getInstance().pdfOrderUser();
			
		if(!res.equals(Protocol.OK)) {
			SceneHandler.getInstance().showWarning(res);
		}
    }
    
    @FXML
    void clickCategorie(ActionEvent event) {
    	if(SceneHandler.getInstance().getScena().getRoot() == borderPaneHome)
    		SceneHandler.getInstance().setProductInHomeByCategory(comboBoxCategoria.getValue());
    }
    
    @FXML
    void insertCharacterInRicercaField(KeyEvent event) {
    	SceneHandler.getInstance().setProductInHome(ricercaField.getText());
    }
    
    @FXML
    void clickChangePassword(ActionEvent event) {
    	SceneHandler.getInstance().setChangePassword(StackPaneHome.getInstance());
    }
    
    @FXML
    void clickLogo(ActionEvent event) {
    	SceneHandler.getInstance().setProductInHome("");
    }
    
    @FXML
    void clickContatti(ActionEvent event) {
    	SceneHandler.getInstance().showInfo("Contattaci al numero 3297222868 oppure all'email: francescovulcano7@gmail.com");
    }
    
	public void riempiCombo() {
		ArrayList<String> categorie = Client.getInstance().getCategories();
    	for(String i : categorie) {
    		comboBoxCategoria.getItems().add(i);
    	}
    	comboBoxCategoria.setPromptText("Categoria");
	}

	public void pulisciCombo() {
		comboBoxCategoria.getSelectionModel().clearSelection();
		comboBoxCategoria.getItems().clear();
		comboBoxCategoria.setValue("Categorie");
	}

}
