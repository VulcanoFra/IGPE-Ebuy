package application.view;

import application.controller.AllProductController;
import application.controller.CartController;
import application.controller.GestioneProdottiAdminController;
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
	private AnchorPane registerPage;
	private AnchorPane loginPage;
	private VBox allProductPane;
	private VBox cartPane;
	private BorderPane adminHomePage;
	private AllProductController prdContrl;
	private CartController cartController;
	private VBox gestioneProdottiAdminPane;
	private GestioneProdottiAdminController gestioneProdottiAdminController;
	private AnchorPane dashboard;
	
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
		loader = new FXMLLoader(getClass().getResource("/application/fxml/clienti/registerPage.fxml"));
		registerPage =(AnchorPane) loader.load();
		loader = new FXMLLoader(getClass().getResource("/application/fxml/clienti/loginPage.fxml"));
		loginPage = (AnchorPane) loader.load();
		loader = new FXMLLoader(getClass().getResource("/application/fxml/clienti/allProductPane.fxml"));
		allProductPane = (VBox) loader.load();
		prdContrl = loader.getController();
		loader = new FXMLLoader(getClass().getResource("/application/fxml/clienti/cart.fxml"));
		cartPane = (VBox) loader.load();
		cartController = loader.getController();
		loader = new FXMLLoader(getClass().getResource("/application/fxml/admin/adminHomePage.fxml"));
		adminHomePage = (BorderPane) loader.load();
		loader = new FXMLLoader(getClass().getResource("/application/fxml/admin/gestioneProdottiAdmin.fxml"));
		gestioneProdottiAdminPane = (VBox) loader.load();
		gestioneProdottiAdminController = loader.getController();
		loader = new FXMLLoader(getClass().getResource("/application/fxml/clienti/dashboard.fxml"));
		dashboard = (AnchorPane) loader.load();
		
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
		if(scena.getRoot() == homePage) {
			stage.hide();
		}
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
		
		/*if(scena.getRoot() == loginPage)
			stage.hide();*/
		stage.hide();
		scena.setRoot(homePage);
		setDashboardInHome();
		//StackPaneHome.getInstance().getChildren().add(dashboard);
    	//stage.setResizable(false);
    	stage.setWidth(900);
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

	public void setAllProductInHome(String parametro) {
		
		/*if(!(stackPaneHome.getChildren().size() == 0) || stackPaneHome.getChildren().get(0).equals(allProductPane))
			return;*/
		
		StackPaneHome.getInstance().prefWidthProperty().bind(homePage.widthProperty().multiply(0.8));
		if(StackPaneHome.getInstance().getChildren().contains(allProductPane)) {
			StackPaneHome.getInstance().getChildren().remove(allProductPane);
			//allProductPane.getChildren().get(2).;
		}
		StackPaneHome.getInstance().getChildren().add(allProductPane);
		//System.out.println(allProductPane.getChildren().get(1));
		try {
			prdContrl.setProdottiPane(parametro);		//NON MI PIACE FAR COSì
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
			setAllProductInHome("");
			return;
		}
			
		//stackPaneHome.prefWidthProperty().bind(homePage.widthProperty().multiply(0.8));
		//stackPaneHome.prefHeightProperty().bind(homePage.heightProperty());
		if(stackPaneHome.getChildren().contains(cartPane))
			stackPaneHome.getChildren().remove(cartPane);
		//cartController.setProdottiInCart();
		stackPaneHome.getChildren().add(cartPane);
		
		
	}
	
	public void setAdminPage() {
		if(scena.getRoot() == loginPage)
			stage.hide();
		
		stage.hide();
		scena.setRoot(adminHomePage);
    	//stage.setResizable(false);
    	stage.setWidth(970);
    	stage.setHeight(550);
    	stage.setMinWidth(900);
    	stage.setMinHeight(750);
    	stage.show();
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
