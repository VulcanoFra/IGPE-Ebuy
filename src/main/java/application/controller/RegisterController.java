package application.controller;


import application.model.User;
import application.net.client.Client;
import application.net.client.Rules;
import application.net.common.Protocol;
import application.view.SceneHandlerVecchio;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private Label lblPasswordErrata;

    @FXML
    private Label lblUsernameEsistente;
    
    @FXML
    private ImageView logo;

    @FXML
    private VBox vBoxColorata;
    
    @FXML
    private Button btnLogin;

    @FXML
    private Label benvenutoLabel;

    @FXML
    private TextField nomeField;

    @FXML
    private TextField cognomeField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private AnchorPane registerPrincPane;
    
    @FXML
    private Button btnRegister;

    @FXML
    void initialize() {
    	logo.setImage(new Image(getClass().getResourceAsStream("/application/image/logo.png"), 200, 200, true, true));
    	registerPrincPane.getStylesheets().add(getClass().getResource("/application/css/loginAndRegister.css").toExternalForm());
    	lblPasswordErrata.setVisible(false);
    	lblUsernameEsistente.setVisible(false);
    }
    
    private void clearField() {
    	usernameField.setText("");
    	passwordField.setText("");
    	nomeField.setText("");
    	cognomeField.setText("");
    	lblPasswordErrata.setVisible(false);
    	lblUsernameEsistente.setVisible(false);
    }
    
    @FXML
    void clickRegistrati(ActionEvent event) throws Exception {
    	String res = Client.getInstance().registration(new User(usernameField.getText(), passwordField.getText(), nomeField.getText(), cognomeField.getText()));
    	
    	if(res.equals(Protocol.OK)) {
        	try {
        		clearField();
        		Client.getInstance().resetClient();
        		SceneHandlerVecchio.getInstance().showInfo("Utente registrato correttamente");
        		SceneHandlerVecchio.getInstance().setLoginScene();
			} catch (Exception e) {
				SceneHandlerVecchio.getInstance().showError("Cannot load the page");
			}
    	}else if(res.equals(Rules.USERNAME_CORTO)){
    		//SceneHandlerVecchio.getInstance().showError(res);
    		if(!lblUsernameEsistente.isVisible())
    			lblUsernameEsistente.setVisible(true);
    		lblUsernameEsistente.setText("Username deve essere di almeno 4 caratteri");
    		Client.getInstance().resetClient();
    	} else if(res.equals(Rules.USERNAME_ERROR)) {
    		if(!lblUsernameEsistente.isVisible())
    			lblUsernameEsistente.setVisible(true);
    		lblUsernameEsistente.setText("Ci sono caratteri non ammessi nell'username");
    	}
    }

    @FXML
    void clickAccedi(ActionEvent event) throws Exception {
    	SceneHandlerVecchio.getInstance().setLoginScene();
    }
    
}
