package application.net.common;

public class Protocol {
	public final static String LOGIN = "Login";
	public final static String REGISTRATION = "Registration";
	
	public final static String BAD_REQUEST= "Richiesta non corretta";
	
	public final static String AUTHENTICATION_ERROR = "Errore durante l'autenticazione";
	public final static String USER_ALREADY_LOGGED = "Utente gi� collegato";
	public final static String LENGTH_PASSWORD_ERROR = "La password deve essere lunga almeno 8 caratteri";
	
	public final static String USERNAME_ERROR = "Username must be a number, lecter and _ or -";
	public final static String ERROR = "Errore durante la conessione";
	public final static String ERROR_DB = "Errore durante la conessione al DB";
	public final static String OK = "Ok";
	public final static String USER_EXISTS_ERROR = "L'utente inserito esiste gi�";
	public final static String USER_NOT_EXISTS = "L'utente inserito non esiste";
	
	public final static String OK_ADMIN= "L'utente � correttamente loggato come admin";
	
	public final static String ADD_QUANTITY = "Aumenta la quantit� del prodotto";
	
	public final static String ADD_PRODUCT_IN_CATALOG = "Richiesta di aggiunta prodotto";
	public final static String CANNOT_ADD_PRODUCT = "Impossibile aggiungere prodotto";
	public final static String GET_PRODUCT = "Richiesta dei prodotti all'interno del DB";
	public final static String GET_PRODUCT_FAILED = "I prodotti ricercati non sono presenti nel catalogo";
	public final static String GET_PRODUCTS_BY_CATEGORY = "Richiesta dei prodotti per categoria all'interno del DB";
	
	public final static String ADD_PRODUCT_IN_CART = "Richiesta di inserimento del prodotto nel carrello del cliente";
	public final static String PRODUCT_IS_ALREADY_IN_CART = "Controlla il carrello, il prodotto scelto � gi� nel carrello";
	public final static String PRODUCT_CORRECTLY_ADDED_IN_CART = "Il prodotto � stato correttamente aggiunto al carrello";
	public final static String PRODUCT_AVAILABLE = "Il prodotto � disponibile";
	public final static String PRODUCT_IS_NOT_AVAILABLE = "Il prodotto non � disponibile, prova a controllare pi� tardi";
	public final static String IMPOSSIBLE_ADD_PRODUCT_IN_CART = "Impossibile aggiungere il prodotto nel carrello";
	public final static String GET_PRODUCT_IN_CART = "Richiesta dei prodotti nel carrello dell'utente";
	public final static String REMOVE_PRODUCT_FROM_CART = "Rimuovi il prodotto dal carrello";
	public final static String PRODUCT_CORRECTLY_REMOVED_FROM_CART = "Prodotto rimosso correttamente";
	public final static String IMPOSSIBLE_REMOVE_PRODUCT_FROM_CART = "ERRORE! Non � stato possibile rimuovere il prodotto dal carrello";
	public final static String CHECK_QUANTITY_AVAILABILITY = "Controllo sulla disponibilit� del prodotto";
	public final static String QUANTITY_AVAILABLE = "Quantit� disponibile";
	public final static String QUANTITY_UNAVAILABLE = "Quantit� non disponibile";
	public final static String ORDER_SUCCESS = "Ordine processato con successo";
	public final static String IMPOSSIBLE_PROCEED_ORDER = "Impossibile procedere all'ordine";
	public final static String SOME_PRODUCT_ARE_UNAVAILABLE = "Attenzione! Qualche prodotto non � disponibile nella quantit� richiesta";
	
	public final static String GET_TREND_PRODUCT = "Dammi l'andamento delle vendite del prodotto";
	public final static String PROCEED_TO_ORDER = "Procedi all'ordine";

	public final static String GET_ALL_CATEGORIES = "Restituisci tutte le categorie";
	
	public final static String UPDATE_PASSWORD = "Cambia password";
	public final static String OLD_PASSWORD_ERROR = "Attenzione! La password inserita non corrisponde a quella dell'utente";
	
	public final static String EXIT = "Fai il LogOut";

}
