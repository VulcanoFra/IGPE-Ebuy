package application.controller;


import application.model.User;
import application.net.client.Client;
import application.net.client.Rules;
import application.net.common.Protocol;
import application.view.SceneHandler;
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
    private Label labelPassword;

    @FXML
    private Label labelUsername;
    
    @FXML
    private ImageView logo;

    @FXML
    private VBox vBoxColorata;
    
    @FXML
    private Label labelNomeCognome;
    
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
    	logo.setImage(new Image(getClass().getResourceAsStream("/application/image/logoBasso.png"), 200, 200, true, true));
    	registerPrincPane.getStylesheets().add(getClass().getResource("/application/css/loginAndRegister.css").toExternalForm());
    	labelPassword.setVisible(false);
    	labelUsername.setVisible(false);
    	labelNomeCognome.setVisible(false);
    }
    
    private void clearField() {
    	usernameField.setText("");
    	passwordField.setText("");
    	nomeField.setText("");
    	cognomeField.setText("");
    	labelPassword.setVisible(false);
    	labelUsername.setVisible(false);
    }
    
    public boolean risposte(String resUsername, String resPassword, String resNome, String resCognome) {
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
    	
    	if(!resNome.equals(Rules.WORD_OK)) {
    		risposta = false;
    		if(!labelNomeCognome.isVisible())
    			labelNomeCognome.setVisible(true);
    		labelNomeCognome.setText(resNome);
    	}
    	else {
    		labelNomeCognome.setVisible(false);
    	}
    	
    	if(!resCognome.equals(Rules.WORD_OK)) {
    		risposta = false;
    		if(!labelNomeCognome.isVisible())
    			labelNomeCognome.setVisible(true);
    		labelNomeCognome.setText(resCognome);
    	}
    	else {
    		labelNomeCognome.setVisible(false);
    	}
    	
    	return risposta;
    }
    
    @FXML
    void clickRegistrati(ActionEvent event) throws Exception {
    	String resUsername = Rules.getInstance().checkRulesUsername(usernameField.getText());
    	String resPassword = Rules.getInstance().checkRulesPassword(passwordField.getText());
    	String resNome = Rules.getInstance().checkRulesWords(nomeField.getText());
    	String resCognome = Rules.getInstance().checkRulesWords(cognomeField.getText());

    	if(!risposte(resUsername, resPassword, resNome, resCognome))
    		return;
    	
    	String res = Client.getInstance().registration(new User(usernameField.getText(), passwordField.getText(), nomeField.getText(), cognomeField.getText()));
    	
    	if(res.equals(Protocol.OK)) {
        	try {
        		clearField();
        		Client.getInstance().resetClient();
        		SceneHandler.getInstance().showInfo("Utente registrato correttamente");
        		SceneHandler.getInstance().setLoginScene();
			} catch (Exception e) {
				SceneHandler.getInstance().showError("Cannot load the page");
			}
    	}else if(res.equals(Rules.USERNAME_CORTO)){
    		//SceneHandlerVecchio.getInstance().showError(res);
    		if(!labelUsername.isVisible())
    			labelUsername.setVisible(true);
    		labelUsername.setText("Username deve essere di almeno 4 caratteri");
    		Client.getInstance().resetClient();
    	} else if(res.equals(Rules.USERNAME_ERROR)) {
    		if(!labelUsername.isVisible())
    			labelUsername.setVisible(true);
    		labelUsername.setText("Ci sono caratteri non ammessi nell'username");
    	}
    }

    @FXML
    void clickAccedi(ActionEvent event) throws Exception {
    	SceneHandler.getInstance().setLoginScene();
    }
    
}
