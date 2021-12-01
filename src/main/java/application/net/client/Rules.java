package application.net.client;

import java.util.regex.Pattern;

public class Rules {
	public final static String PASSWORD_CORTA = "La password deve essere di almeno 8 caratteri";
	public final static String PASSWORD_LUNGA = "La password deve essere al massimo di 20 caratteri";
	public final static String PASSWORD_ERROR = "La password deve contenere almeno un carattere maiuscolo, uno minuscolo, un numero e (@#$%^&+=$)";
	public final static String PASSWORD_OK = "La password è valida";
	
	public final static String USERNAME_CORTO = "L'username deve essere di almeno 4 caratteri";
	public final static String USERNAME_LUNGO = "L'username deve essere al massimo di 20 caratteri";
	public final static String USERNAME_ERROR = "Ci sono caratteri non ammessi nell'username";
	public final static String USERNAME_OK = "L'username è valido";

	private static Rules instance = null;
	
	public static Rules getInstance(){
		if(instance == null) 
			instance = new Rules();
		return instance;
	}
	
	public String checkRulesUsername(String username) {
		if(username == null || username.length() < 4)
			return USERNAME_CORTO;
		if(username.length() > 20)
			return USERNAME_LUNGO;
		String regex = "[a-zA-Z0-9_\\.]+";

		if(!Pattern.matches(regex, username)) 
			return USERNAME_ERROR;
		
		return USERNAME_OK;
	}
	
	public String checkRulesPassword(String password) {
		if(password == null || password.length() < 8)
			return PASSWORD_CORTA;
		if(password.length() > 20)
			return PASSWORD_LUNGA;
		
		String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$";
		if(!Pattern.matches(regex, password))
			return PASSWORD_ERROR;
		return PASSWORD_OK;
	}
}
