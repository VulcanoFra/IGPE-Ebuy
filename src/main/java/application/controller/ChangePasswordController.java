package application.controller;

import application.net.client.Client;
import application.net.client.Rules;
import application.net.common.Protocol;
import application.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;

public class ChangePasswordController {

	@FXML
    private AnchorPane anchorPane;
	
    @FXML
    private PasswordField lblNewPassword;
    
    @FXML
    private PasswordField lblOldPassword;

    @FXML
    private Label lblErrorOldPassword;

    @FXML
    private Button bntConferma;

    @FXML
    private Label lblErrorNewPassword;
    
    @FXML
    void initialize() {
    	anchorPane.getStylesheets().add(getClass().getResource("/application/css/changePassword.css").toExternalForm());
    	lblErrorNewPassword.setVisible(false);
    	lblErrorOldPassword.setVisible(false);
    }
    
    private boolean check(String resOld, String resNew) {
    	boolean ok = true;
    	
    	if(!resOld.equals(Rules.PASSWORD_OK)) {
    		ok = false;
    		if(!lblErrorOldPassword.isVisible())
    			lblErrorOldPassword.setVisible(true);
    		lblErrorOldPassword.setText(resOld);
    	} else {
    		lblErrorOldPassword.setVisible(false);
    	}
    	
    	if(!resNew.equals(Rules.PASSWORD_OK)) {
    		ok = false;
    		if(!lblErrorNewPassword.isVisible())
    			lblErrorNewPassword.setVisible(true);
    		lblErrorNewPassword.setText(resNew);
    	} else {
    		lblErrorNewPassword.setVisible(false);
    	}
    	
    	return ok;
    }
    
    private void clearField() {
    	lblErrorNewPassword.setVisible(false);
    	lblErrorOldPassword.setVisible(false);
    	lblNewPassword.setText("");
    	lblOldPassword.setText("");
    }
    
    @FXML
    void clickBtnConferma(ActionEvent event) {
    	String resOldPassword = Rules.getInstance().checkRulesPassword(lblOldPassword.getText());
    	String resNewPassword = Rules.getInstance().checkRulesPassword(lblNewPassword.getText());
    	
    	if(!check(resOldPassword, resNewPassword))
    		return;
    	
    	String risposta = Client.getInstance().updatePassword(lblOldPassword.getText(), lblNewPassword.getText());
    	
    	if(risposta.equals(Protocol.OK))
    		SceneHandler.getInstance().showInfo("Password cambiata con successo");
    	else {
    		SceneHandler.getInstance().showError(risposta);
    	}
    	
    	clearField();
    }
    
    
}
