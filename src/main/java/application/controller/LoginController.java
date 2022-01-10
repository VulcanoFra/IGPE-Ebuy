package application.controller;

import application.net.client.Client;
import application.net.client.Rules;
import application.net.common.Protocol;
import application.view.SceneHandler;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class LoginController {

	@FXML
    private AnchorPane loginPrincPane;
	
    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Label labelPassword;

    @FXML
    private Label labelUsername;

    @FXML
    private Button btnLogin;

    @FXML
    private Label labelNonSeiRegistrato;

    @FXML
    private ImageView logo;
    
    @FXML
    private Button btnRegister;
    
    @FXML
    private Label benvenutoLabel;
    
    @FXML
    private FontAwesomeIcon eyeIcon;
    
    @FXML
    void initialize() {
    	logo.setImage(new Image(getClass().getResourceAsStream("/application/image/logoBasso.png"), 200, 200, true, true));
    	labelPassword.setVisible(false);
    	labelUsername.setVisible(false);
    	
    	loginPrincPane.getStylesheets().add(getClass().getResource("/application/css/loginAndRegister.css").toExternalForm());
    	
    	handlerEvent();
    }
    
    private void handlerEvent() {
    	eyeIcon.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				
				passwordField.setPromptText(passwordField.getText());
				passwordField.setText(""); 
				eyeIcon.requestFocus();
				//passwordField.setDisable(true);
	 
				eyeIcon.setGlyphName("EYE");
				
				/*double x = SceneHandler.getInstance().getStageX();
				double y = SceneHandler.getInstance().getStageY();
				
				toolTipPassword.show(passwordField ,x + 500, y +300);
				toolTipPassword.setText(passwordField.getText());*/
			}
		});
    	
    	eyeIcon.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				eyeIcon.setGlyphName("EYE_SLASH");
				
				passwordField.setText(passwordField.getPromptText());
				if(passwordField.getPromptText().equals(""))
					passwordField.setPromptText("Password");
				else
					passwordField.setPromptText("");
				passwordField.requestFocus();
				passwordField.positionCaret(passwordField.getLength());
				//passwordField.setDisable(false);
			}
		});
    }
    
    private void clearField() {
    	usernameField.setText("");
    	passwordField.setText("");
    	labelUsername.setVisible(false);
    	labelPassword.setVisible(false);
    }
    
    private boolean risposte(String resUsername, String resPassword) {
    	boolean risposta = true;
    	
    	if(!resUsername.equals(Rules.USERNAME_OK)) {
    		risposta = false;
    		if(!labelUsername.isVisible())
    			labelUsername.setVisible(true);
    		labelUsername.setText(resUsername);
    	} else {
    		labelUsername.setVisible(false);    	
    	}
    	
    	if(!resPassword.equals(Rules.PASSWORD_OK)) {
    		risposta = false;
    		if(!labelPassword.isVisible())
    			labelPassword.setVisible(true);
    		labelPassword.setText(resPassword);
    	} else { 
    		labelPassword.setVisible(false);
    	}
    	
    	return risposta;
    }
    
    @FXML
    void clickAccedo(ActionEvent event)  {
    	String resUsername = Rules.getInstance().checkRulesUsername(usernameField.getText());
    	String resPassword = Rules.getInstance().checkRulesPassword(passwordField.getText());
    	
    	boolean regoleRispettate = risposte(resUsername, resPassword);
    	if(!regoleRispettate)
    		return;
    	
    	String resLogin = Client.getInstance().login(usernameField.getText(), passwordField.getText());

    	if(resLogin.equals(Protocol.OK)) {
        	try {
        		clearField();
				SceneHandler.getInstance().setHomeScene();
			} catch (Exception e) {
				SceneHandler.getInstance().showError("Cannot load the page");
			}
        	return;
    	}
    	else if(resLogin.equals(Protocol.USER_ALREADY_LOGGED)) {
    		SceneHandler.getInstance().showWarning("Utente già connesso su un altro dispositivo");
    	}
    	else if(resLogin.equals(Protocol.OK_ADMIN)) {
    		clearField();
    		SceneHandler.getInstance().setAdminPage();
    		return;
        }
		else if(resLogin.equals(Protocol.AUTHENTICATION_ERROR)) {
    		SceneHandler.getInstance().showError("Hai sbagliato la combinazione di username/password");
    	}
		else if(resLogin.equals(Protocol.USER_NOT_EXISTS)){
    		SceneHandler.getInstance().showError("Attenzione non esiste nessuno account con questo username. Prova a registrarti");
		}
    	
		Client.getInstance().resetClient();
    }

    @FXML
    void clickRegistrati(ActionEvent event) throws Exception {
    	SceneHandler.getInstance().setRegisterScene();
    }
    
}
