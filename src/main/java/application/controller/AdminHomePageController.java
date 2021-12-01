package application.controller;

import application.net.client.Client;
import application.net.common.Protocol;
import application.view.SceneHandlerVecchio;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class AdminHomePageController {

    @FXML
    private Button btnDownloadCatalogo;

    @FXML
    private MenuItem btnLogOut;

    @FXML
    private StackPane stackPaneAdminHome;
    
    @FXML
    private Button bntGestisciProdotti;

    @FXML
    private MenuItem btnChangePassword;

    @FXML
    private BorderPane borderPaneHomeAdmin;

    @FXML
    private Button bntGestisciAdmin;

    @FXML
    private MenuButton btnProfilo;

    @FXML
    void initialize() {
    	borderPaneHomeAdmin.getStylesheets().add(getClass().getResource("/application/css/homePage.css").toExternalForm());
    	btnProfilo.getStyleClass().add("Profilo");
    }
    
    @FXML
    void clickGestisciProdotti(ActionEvent event) {
    	SceneHandlerVecchio.getInstance().setGestioneProdottiAdmin(stackPaneAdminHome);
    }
    
    @FXML
    void clickExit(ActionEvent event) {
    	Client.getInstance().resetClient();
    	try {
			SceneHandlerVecchio.getInstance().resetPage(stackPaneAdminHome);
			SceneHandlerVecchio.getInstance().setLoginScene();
		} catch (Exception e) {
			SceneHandlerVecchio.getInstance().showError(Protocol.ERROR);
			/*OPPURE IL PROBLEMA DEVE ESSERE NEL FILE DI LOG*/
		}
    }
}

