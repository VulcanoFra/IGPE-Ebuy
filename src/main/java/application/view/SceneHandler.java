package application.view;

import java.io.IOException;
import java.util.Optional;

import application.controller.AdminHomePageController;
import application.controller.AllProductController;
import application.controller.AndamentoProdottoController;
import application.controller.CartController;
import application.controller.GestioneProdottiAdminController;
import application.controller.HomePageController;
import application.controller.ProductDetailsController;
import application.net.client.Client;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SceneHandler {
	private Scene scena;
	private Stage stage;
	
	private BorderPane homePage;
	private HomePageController controllerHomePage;
	private AnchorPane registerPage;
	private AnchorPane loginPage;
	private AnchorPane productPane;
	private BorderPane cartPane;
	private BorderPane adminHomePage;
	private AdminHomePageController adminController;
	private AllProductController prdContrl;
	private CartController cartController;
	private VBox gestioneProdottiAdminPane;
	private GestioneProdottiAdminController gestioneProdottiAdminController;
	private AnchorPane changePasswordPage;
	
	private static SceneHandler instance = null;
	
	public static SceneHandler getInstance() {
		if(instance == null)
			instance = new SceneHandler();
		return instance;
	} 
	
	public void init(Stage primaryStage) throws Exception {
		stage = primaryStage;
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/application/image/LogoBasso.png"), 20, 20, true, true));

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/clienti/homePage.fxml"));
		homePage = (BorderPane) loader.load();
		controllerHomePage = loader.getController();
		loader = new FXMLLoader(getClass().getResource("/application/fxml/clienti/registerPage.fxml"));
		registerPage =(AnchorPane) loader.load();
		loader = new FXMLLoader(getClass().getResource("/application/fxml/clienti/loginPage.fxml"));
		loginPage = (AnchorPane) loader.load();
		loader = new FXMLLoader(getClass().getResource("/application/fxml/clienti/productPane.fxml"));
		productPane = (AnchorPane) loader.load();
		prdContrl = loader.getController();
		loader = new FXMLLoader(getClass().getResource("/application/fxml/clienti/cart.fxml"));
		cartPane = (BorderPane) loader.load();
		cartController = loader.getController();
		loader = new FXMLLoader(getClass().getResource("/application/fxml/admin/adminHomePage.fxml"));
		adminHomePage = (BorderPane) loader.load();
		adminController = loader.getController();
		loader = new FXMLLoader(getClass().getResource("/application/fxml/admin/gestioneProdottiAdmin.fxml"));
		gestioneProdottiAdminPane = (VBox) loader.load();
		gestioneProdottiAdminController = loader.getController();
		loader = new FXMLLoader(getClass().getResource("/application/fxml/common/changePassword.fxml"));
		changePasswordPage = (AnchorPane) loader.load();
		
		initScene();
		stage.setScene(scena);
		stage.setMinWidth(700);
    	stage.setMinHeight(575);
		stage.setTitle("E-Commerce");
		stage.show();	
	}
	
	public Stage getStage() {
		return stage;
	}
	
	public Scene getScena() {
		return scena;
	}
	
	private void initScene() throws Exception {
		
		scena = new Scene(loginPage,700,575);
	}
	
	public void setLoginScene() throws Exception {
		if(scena.getRoot() != registerPage)
			stage.hide();
		
		scena.setRoot(loginPage);
    	//stage.setResizable(false);
    	stage.setWidth(700);
    	stage.setHeight(575);
    	stage.setMinWidth(700);
    	stage.setMinHeight(575);
    	stage.show();
	}
	
	public void setRegisterScene() throws Exception {
		
		//stage.hide();
		scena.setRoot(registerPage);
    	//stage.setResizable(false);
    	stage.setWidth(700);
    	stage.setHeight(680);
    	stage.setMinWidth(700);
    	stage.setMinHeight(680);
    	stage.show();
	}
	
	public void setHomeScene() throws Exception {
		Thread t = new Thread(Client.getInstance());
    	t.setDaemon(true);
    	t.start();
    	
		controllerHomePage.riempiCombo();
		
		if(scena.getRoot() == loginPage)
			stage.hide();
		stage.hide();
		scena.setRoot(homePage);
		
		setProductInHome("");
    	stage.setWidth(900);
    	stage.setHeight(750);
    	stage.setMinWidth(900);
    	stage.setMinHeight(750);
    	stage.show();
	}
	
	public void setAdminPage() {
		Thread t = new Thread(Client.getInstance());
    	t.setDaemon(true);
    	t.start();
    	
		if(scena.getRoot() == loginPage)
			stage.hide();
		
		stage.hide();
		scena.setRoot(adminHomePage);
		adminController.setPageAggiungiProdotti();
    	stage.setWidth(970);
    	stage.setHeight(750);
    	stage.setMinWidth(900);
    	stage.setMinHeight(750);
    	stage.show();
	}

	public void setProductInHome(String parametro) {
		
		if(StackPaneHome.getInstance().getChildren().contains(productPane)) {
			StackPaneHome.getInstance().getChildren().remove(productPane);
		}
		StackPaneHome.getInstance().getChildren().add(productPane);

		try {
			prdContrl.setProdottiPane(parametro);	
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("NON POSSO CARICARE PRODOTTI");
		}
	
	}
	
	public void setProductInHomeByCategory(String category) {
		if(StackPaneHome.getInstance().getChildren().contains(productPane)) {
			StackPaneHome.getInstance().getChildren().remove(productPane);
		}
		StackPaneHome.getInstance().getChildren().add(productPane);
		try {
			prdContrl.setProdottiPaneByCategory(category);	
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("NON POSSO CARICARE PRODOTTI");
		}
	}
	
	
	public void setCartInHome(StackPane stackPaneHome) {

		boolean trovati = cartController.setProdottiInCart();
		if(!trovati) {
			showInfo("Non è presente alcun articolo nel carrello");
			setProductInHome("");
			return;
		}
		if(stackPaneHome.getChildren().contains(cartPane))
			stackPaneHome.getChildren().remove(cartPane);
		stackPaneHome.getChildren().add(cartPane);
		
	}
	
	public void setGestioneProdottiAdmin(StackPane stackPaneHome) {
		if(stackPaneHome.getChildren().contains(gestioneProdottiAdminPane)) {
			stackPaneHome.getChildren().remove(gestioneProdottiAdminPane);
			gestioneProdottiAdminController.pulisciCombo();
		}	
		gestioneProdottiAdminPane.prefWidthProperty().bind(stackPaneHome.widthProperty().multiply(1));
		gestioneProdottiAdminPane.prefHeightProperty().bind(stackPaneHome.heightProperty().multiply(1));
		stackPaneHome.getChildren().add(gestioneProdottiAdminPane);
		gestioneProdottiAdminController.riempiCombo();
	}
	
	public void setChangePassword(StackPane stackPaneHome){
		if(stackPaneHome.getChildren().contains(changePasswordPage)) {
			stackPaneHome.getChildren().remove(changePasswordPage);
		}	
		stackPaneHome.getChildren().add(changePasswordPage);
	}
	
	public void getPaneAndamentoProdotto(String nome) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/clienti/andamentoProdotto.fxml"));
			BorderPane pane = (BorderPane) loader.load();
			
			AndamentoProdottoController controller = loader.getController();
			controller.setNomeProdotto(nome);
			controller.setEvent();
			Stage stageAndamento = new Stage();
			Scene s = new Scene(pane);
			stageAndamento.setScene(s);

			stageAndamento.setResizable(false);
			stageAndamento.showAndWait();
			
		} catch (IOException e) {
			showError("Non è possibile visualizzare l'andamento del prodotto! Riprova più tardi");
		}
		
	}
	
	public void getPaneDetailsProduct(String nome, String descrizione, String prezzo, Image img) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/clienti/productDetails.fxml"));
			AnchorPane pane = (AnchorPane) loader.load();
			
			ProductDetailsController controller = loader.getController();
			controller.setData(nome, descrizione, prezzo, img);
			
			Stage stageDeatils = new Stage();
			Scene s = new Scene(pane);
			stageDeatils.setScene(s);
			
			stageDeatils.setResizable(false);
			stageDeatils.showAndWait();
			
		} catch (IOException e) {
			showError("Non è possibile visualizzare i dettagli del prodotto! Riprova più tardi");
		}
	}
	
	public void resetPage(StackPane stackPaneHome) {
		stackPaneHome.getChildren().clear();
	}
	
	public void showError(String message) {
    	Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("");
		alert.setContentText(message);
		alert.show();
    }
    
    public void showInfo(String message) {
    	Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Info");
		alert.setHeaderText("");
		alert.setContentText(message);
		alert.show();
    }

    public void showWarning(String message) {
    	Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Info");
		alert.setHeaderText("");
		alert.setContentText(message);
		alert.show();
    }

	public boolean showConfirm() {
		Alert dialog = new Alert(AlertType.CONFIRMATION);
		dialog.setHeaderText("Sei sicuro di voler processare l'ordine?");
		
		Optional<ButtonType> opt = dialog.showAndWait();
		
		if(opt.get() == ButtonType.OK)
			return true;
		
		return false;
	}
}
