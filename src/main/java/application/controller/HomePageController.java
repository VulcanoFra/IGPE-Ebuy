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
    private Button btnInfo;

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
    private Button btnDashboard;

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
    	/*btnInfo.getStyleClass().add("buttonLeft");
    	btnContatti.getStyleClass().add("buttonLeft");
    	btnProfilo.getStyleClass().add("buttonLeft");
    	btnDashboard.getStyleClass().add("buttonLeft");
    	
    	btnProdotti.getStyleClass().add("buttonTop");*/
    	
    	
    }
    
    public String getRicerca() {
    	return ricercaField.getText();
    }
    
    /*private void handler() {
    	lblMenu.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				TranslateTransition slide = new TranslateTransition();
				slide.setDuration(Duration.seconds(UtilitiesView.DURATA_SLIDE_MENUHOMEPAGE));
				slide.setNode(slider);
				
				slide.setToX(0);
				slide.play();
				
				slider.setTranslateX(UtilitiesView.DIM_X_SLIDER_HOMEPAGE * -1);				
				StackPaneHome.getInstance().setTranslateX(0);
				
				slide.setOnFinished(new EventHandler<ActionEvent>(){
					@Override
					public void handle(ActionEvent event) {
						lblMenu.setVisible(false);
						lblMenuClose.setVisible(true);
					}
					
				});
			}
		});
    	
    	lblMenuClose.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				TranslateTransition slide = new TranslateTransition();
				slide.setDuration(Duration.seconds(UtilitiesView.DURATA_SLIDE_MENUHOMEPAGE));
				slide.setNode(slider);
				
				slide.setToX(UtilitiesView.DIM_X_SLIDER_HOMEPAGE * -1);
				slide.play();
				
				slider.setTranslateX(0);
				
				slide.setOnFinished(new EventHandler<ActionEvent>(){
					@Override
					public void handle(ActionEvent event) {
						StackPaneHome.getInstance().setTranslateX(UtilitiesView.DIM_X_SLIDER_HOMEPAGE * -1);
						lblMenu.setVisible(true);
						lblMenuClose.setVisible(false);
					}
					
				});
				
			}
		});
    }
    */
    
    @FXML
    void clickDashboardbtn(ActionEvent event) {
    	SceneHandler.getInstance().setDashboardInHome();
    }
    
    @FXML
    void clickCatalogoBtn(ActionEvent event) throws Exception {
    	//if(AllProductController.sizeLista() > 0)
    		SceneHandler.getInstance().setProductInHome("");
    	//else {
    		/*..............*/
    	//}
    }
    
    @FXML
    void clickExit(ActionEvent event) {
    	pulisciCombo();
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
    	try {
			PdfGenerator.getInstance().pdfs();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			SceneHandler.getInstance().showError("Non è stato possibile scaricale il catalogo, riprova più tardi");
		}
    }
    
    @FXML
    void clickCategorie(ActionEvent event) {
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
    
	public void riempiCombo() {
		ArrayList<String> categorie = Client.getInstance().getCategories();
    	for(String i : categorie) {
    		comboBoxCategoria.getItems().add(i);
    	}
	}

	public void pulisciCombo() {
		comboBoxCategoria.getItems().clear();
	}
}
