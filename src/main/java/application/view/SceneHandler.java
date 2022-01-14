package application.view;

import java.io.IOException;

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
import javafx.scene.control.TextInputDialog;
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
	private AnchorPane cartPane;
	private BorderPane adminHomePage;
	private AllProductController prdContrl;
	private CartController cartController;
	private VBox gestioneProdottiAdminPane;
	private GestioneProdottiAdminController gestioneProdottiAdminController;
	private AnchorPane dashboard;
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
		cartPane = (AnchorPane) loader.load();
		cartController = loader.getController();
		loader = new FXMLLoader(getClass().getResource("/application/fxml/admin/adminHomePage.fxml"));
		adminHomePage = (BorderPane) loader.load();
		loader = new FXMLLoader(getClass().getResource("/application/fxml/admin/gestioneProdottiAdmin.fxml"));
		gestioneProdottiAdminPane = (VBox) loader.load();
		gestioneProdottiAdminController = loader.getController();
		loader = new FXMLLoader(getClass().getResource("/application/fxml/clienti/dashboard.fxml"));
		dashboard = (AnchorPane) loader.load();
		loader = new FXMLLoader(getClass().getResource("/application/fxml/common/changePassword.fxml"));
		changePasswordPage = (AnchorPane) loader.load();
		
		initScene();
		stage.setScene(scena);
		stage.setMinWidth(700);
    	stage.setMinHeight(575);
		stage.setTitle("E-Commerce");
		//stage.setResizable(false);
		stage.show();	
	}
	
	public Stage getStage() {
		return stage;
	}
	private void initScene() throws Exception {
		
		scena = new Scene(loginPage,700,575);
	}
	
	public void setLoginScene() throws Exception {
		//if(scena.getRoot() == homePage || scena.getRoot() == adminHomePage) {
			stage.hide();
		//}
		scena.setRoot(loginPage);
    	//stage.setResizable(false);
    	stage.setWidth(700);
    	stage.setHeight(575);
    	stage.setMinWidth(700);
    	stage.setMinHeight(575);
    	stage.show();
	}
	
	public void setRegisterScene() throws Exception {
		stage.hide();
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
		
		if(scena.getRoot() == loginPage)
			stage.hide();
		stage.hide();
		scena.setRoot(homePage);
		setDashboardInHome();
		controllerHomePage.riempiCombo();
		//StackPaneHome.getInstance().getChildren().add(dashboard);
    	//stage.setResizable(false);
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
    	//stage.setResizable(false);
    	stage.setWidth(970);
    	stage.setHeight(750);
    	stage.setMinWidth(900);
    	stage.setMinHeight(750);
    	stage.show();
	}

	public void setDashboardInHome() {
		if(StackPaneHome.getInstance().getChildren().contains(dashboard)) {
			StackPaneHome.getInstance().getChildren().remove(dashboard);
		}
		StackPaneHome.getInstance().getChildren().add(dashboard);
	}

	public void setProductInHome(String parametro) {
		
		if(StackPaneHome.getInstance().getChildren().contains(productPane)) {
			StackPaneHome.getInstance().getChildren().remove(productPane);
		}
		StackPaneHome.getInstance().getChildren().add(productPane);

		try {
			prdContrl.setProdottiPane(parametro);	
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("NON POSSO CARICARE PRODOTTI");
		}
	
	}
	
	public void setProductInHomeByCategory(String category) {
		if(StackPaneHome.getInstance().getChildren().contains(productPane)) {
			StackPaneHome.getInstance().getChildren().remove(productPane);
			//allProductPane.getChildren().get(2).;
		}
		StackPaneHome.getInstance().getChildren().add(productPane);
		//System.out.println(allProductPane.getChildren().get(1));
		try {
			prdContrl.setProdottiPaneByCategory(category);	
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("NON POSSO CARICARE PRODOTTI");
		}
	}
	
	
	public void setCartInHome(StackPane stackPaneHome) {

		System.out.println("CartHome");
		boolean trovati = cartController.setProdottiInCart();
		System.out.println("DOPO");
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
		stackPaneHome.prefWidthProperty().bind(homePage.widthProperty().multiply(0.8));
		//stackPane.prefHeightProperty().bind(homePage.heightProperty());
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
			Stage stage = new Stage();
			Scene s = new Scene(pane);
			stage.setScene(s);
			
			stage.showAndWait();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void getPaneDetailsProduct(String nome, String descrizione, String prezzo, Image img) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/clienti/productDetails.fxml"));
			AnchorPane pane = (AnchorPane) loader.load();
			
			ProductDetailsController controller = loader.getController();
			controller.setData(nome, descrizione, prezzo, img);
			
			Stage stage = new Stage();
			Scene s = new Scene(pane);
			stage.setScene(s);
			
			stage.showAndWait();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	public String showInput() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setHeaderText("Inserisci il numero della carta di credito");
		
		dialog.showAndWait();
		
		return dialog.getEditor().getText();
	}
}
