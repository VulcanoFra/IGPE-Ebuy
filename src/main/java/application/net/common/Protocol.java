package application.net.common;

public class Protocol {
	public final static String LOGIN = "Login";
	public final static String REGISTRATION = "Registration";
	
	public final static String BAD_REQUEST= "Richiesta non corretta";
	
	public final static String AUTHENTICATION_ERROR = "Errore durante l'autenticazione";
	public final static String USER_ALREADY_LOGGED = "Utente già collegato";
	public final static String LENGTH_PASSWORD_ERROR = "La password deve essere lunga almeno 8 caratteri";
	
	public final static String USERNAME_ERROR = "Username must be a number, lecter and _ or -";
	public final static String ERROR = "Errore durante la conessione";
	public final static String ERROR_DB = "Errore durante la conessione al DB";
	public final static String OK = "Ok";
	public final static String USER_EXISTS_ERROR = "L'utente inserito esiste già";
	public final static String USER_NOT_EXISTS = "L'utente inserito non esiste";
	
	public final static String OK_ADMIN= "L'utente è correttamente loggato come admin";

	public final static String ADD_PRODUCT_IN_CATALOG = "Richiesta di aggiunta prodotto";
	public final static String CANNOT_ADD_PRODUCT = "Impossibile aggiungere prodotto";
	public final static String GET_PRODUCT = "Richiesta dei prodotti all'interno del DB";
	public final static String GET_PRODUCT_FAILED = "I prodotti ricercati non sono presenti nel catalogo";

	public final static String ADD_PRODUCT_IN_CART = "Richiesta di inserimento del prodotto nel carrello del cliente";
	public final static String PRODUCT_IS_ALREADY_IN_CART = "Controlla il carrello, il prodotto scelto è già nel carrello";
	public final static String PRODUCT_CORRECTLY_ADDED_IN_CART = "Il prodotto è stato correttamente aggiunto al carrello";
	public final static String PRODUCT_AVAILABLE = "Il prodotto è disponibile";
	public final static String PRODUCT_IS_NOT_AVAILABLE = "Il prodotto non è disponibile, prova a controllare più tardi";
	public final static String IMPOSSIBLE_ADD_PRODUCT_IN_CART = "Impossibile aggiungere il prodotto nel carrello";
	public final static String GET_PRODUCT_IN_CART = "Richiesta dei prodotti nel carrello dell'utente";
	
	public final static String PROCEED_TO_ORDER = "Procedi all'ordine";

}
