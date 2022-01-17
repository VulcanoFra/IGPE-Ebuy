package application.net.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import application.model.DatiAndamentoProdotto;
import application.model.Ordine;
import application.model.Product;
import application.model.ProductInCart;
import application.model.User;
import application.net.common.Protocol;
import application.view.SceneHandler;


public class Client implements Runnable{
	
	private Socket socket = null;
	private static Client instance = null;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private boolean isLogged = false;
	
	private Client() {
		try {
			socket = new Socket("localhost", 8000);
			out = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			out = null;
			SceneHandler.getInstance().showError("Connessione persa");
		}
	}
	
	public static Client getInstance() {
		if(instance == null)
			instance = new Client();
		return instance;
	}

	public void resetClient() {
		instance = null;
		out = null;
		in = null;
		socket = null;
		isLogged = false;
	}
	
	private boolean sendMessageString(String message) {
		return sendObject(message);
	}
	
	private boolean sendObject(Object message) {
		if(out == null)
			return false;
		try {
			out.writeObject(message);
			out.flush();
			return true;
		} catch (IOException e) {
			SceneHandler.getInstance().showError(Protocol.ERROR);
			out = null;			
			return false;
		}
	}
	
	@Override
	public void run(){
		try {
			while(out != null && in != null && isLogged) {
				
				checkQuantityAndSetDiscount();
				
				Thread.sleep(10000);	
			}
		} catch(InterruptedException e) {
			System.out.println("errore thread client");
		}	
	}
	
	public void checkQuantityAndSetDiscount() {
		sendMessageString(Protocol.CHECK_AND_DISCOUNT);
	}
	
	public String login(String username, String password) {		
		sendMessageString(Protocol.LOGIN);
		sendObject(new User(username, password));
		try {
			if(in == null)
				in = new ObjectInputStream(socket.getInputStream());
			String res = (String) in.readObject();
			if(res.equals(Protocol.OK) || res.equals(Protocol.OK_ADMIN))
				isLogged = true;
			return res;			
		} catch (Exception e) {
			out = null;
			return Protocol.ERROR;
		}
	}
	
	public String registration(User user) {	
		sendMessageString(Protocol.REGISTRATION);
		sendObject(user);
		try {
			if(in == null)
				in = new ObjectInputStream(socket.getInputStream());
			String res = (String) in.readObject();
			return res;			
		} catch (Exception e) {
			out = null;
			return Protocol.ERROR;
		}
	}
	
	public String addProduct(Product p) {
		sendMessageString(Protocol.ADD_PRODUCT_IN_CATALOG);		
		
		if(!sendObject(p)) {
			System.out.println("[CLIENT] Non riesco ad inviare il prodotto");
		}
					
		try {
			if(in == null)
				in = new ObjectInputStream(socket.getInputStream());
			String res = (String) in.readObject();
			return res;
		} catch (Exception e) {
			out = null;
			return Protocol.ERROR;		
		}		
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Product> getProduct(String parametro)  {
		sendMessageString(Protocol.GET_PRODUCT);
		sendMessageString(parametro);

		try {
			
			if(in == null) 
				in = new ObjectInputStream(socket.getInputStream());			
			
			ArrayList<Product> prodotti = new ArrayList<Product>();
			prodotti = (ArrayList<Product>) in.readObject();
			
			return prodotti;
		}
		catch(Exception e) {
			e.printStackTrace();
			out = null;
			return null;
		}	
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Product> getProductByCategory(String category) {
		sendMessageString(Protocol.GET_PRODUCTS_BY_CATEGORY);
		sendMessageString(category);

		try {
			
			if(in == null) 
				in = new ObjectInputStream(socket.getInputStream());			
			
			ArrayList<Product> prodotti = new ArrayList<Product>();
			prodotti = (ArrayList<Product>) in.readObject();
			
			return prodotti;
		}
		catch(Exception e) {
			e.printStackTrace();
			out = null;
			return null;
		}	
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<ProductInCart> getProductInCart()  {
		sendMessageString(Protocol.GET_PRODUCT_IN_CART);

		try {
			if(in == null) 
				in = new ObjectInputStream(socket.getInputStream());			
			
			ArrayList<ProductInCart> prodotti = new ArrayList<ProductInCart>();
			prodotti = (ArrayList<ProductInCart>) in.readObject();
			
			return prodotti;
		}
		catch(Exception e) {
			out = null;
			return null;
		}	
	}
	
	public void addAProductInCart(String nomeProdotto) {
		try {
			sendMessageString(Protocol.ADD_PRODUCT_IN_CART);
			sendMessageString(nomeProdotto);
			
			String risposta = (String) in.readObject();
			
			if(risposta.equals(Protocol.PRODUCT_CORRECTLY_ADDED_IN_CART)) {
				SceneHandler.getInstance().showInfo("Prodotto aggiunto correttamente al carrello");
				return;
			}
			else if(risposta.equals(Protocol.ERROR_DB)) {
				SceneHandler.getInstance().showError("Errore interno al database, invitiamoa riprovare più tardi. A breve una manutenzione");
			}
			else {
				SceneHandler.getInstance().showWarning(risposta);
				return;
			}
		} catch (Exception e) {
			out = null;
		}
	}
	
	public void exit() {
		sendMessageString(Protocol.EXIT);
		isLogged = false;
		resetClient();
	}
	
	public String procediAllOrdine() {
		sendMessageString(Protocol.PROCEED_TO_ORDER);

		try {
			String rispostaServer = (String) in.readObject();
			return rispostaServer;
			
		} catch (ClassNotFoundException | IOException e) {
			out = null;
			return Protocol.ERROR;
		}
		
	}
	
	public boolean removeProductFromCart(String nomeProdotto) {
		sendMessageString(Protocol.REMOVE_PRODUCT_FROM_CART);
		sendMessageString(nomeProdotto);
		
		try {
			String res = (String) in.readObject();
			if(res.equals(Protocol.PRODUCT_CORRECTLY_REMOVED_FROM_CART)) 
				return true;
			
		} catch (ClassNotFoundException | IOException e) {
			//e.printStackTrace();
			out = null;
		}
		return false;
	}

	public Integer isAvailbleProduct(String nomeProdotto, Integer number) {
		sendMessageString(Protocol.CHECK_QUANTITY_AVAILABILITY);
		sendMessageString(nomeProdotto);
		sendMessageString(number.toString());
		
		try {
			String risposta = (String) in.readObject();
			
			if(risposta.equals(Protocol.QUANTITY_AVAILABLE)) {
				return 0;
			}
			String quantitaDisponibile = (String) in.readObject();
			
			return Integer.parseInt(quantitaDisponibile);
		} catch (ClassNotFoundException | IOException e) {
			out = null;
			return null;
		} 
	}

	@SuppressWarnings("unchecked")
	public ArrayList<String> getCategories() {
		sendMessageString(Protocol.GET_ALL_CATEGORIES);
		try {
			ArrayList<String> categorie = (ArrayList<String>) in.readObject();
			return categorie;
		} catch (ClassNotFoundException | IOException e) {
			out = null;
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public ArrayList<DatiAndamentoProdotto> getTrendProduct(String nomeProdotto){
		sendMessageString(Protocol.GET_TREND_PRODUCT);
		sendMessageString(nomeProdotto);
		
		try {
			ArrayList<DatiAndamentoProdotto> dati = (ArrayList<DatiAndamentoProdotto>) in.readObject();
			return dati;
		} catch (ClassNotFoundException | IOException e) {
			out = null;
			return null;
		}
	}

	public boolean addQuantityOfProduct(String nomeProdotto, Integer quantita) {
		sendMessageString(Protocol.ADD_QUANTITY);
		sendMessageString(nomeProdotto);
		sendObject(quantita);
		
		try {
			String risultato = (String) in.readObject();
			
			if(risultato.equals(Protocol.OK))
				return true;
		} catch (ClassNotFoundException | IOException e) {
			out = null;
		}
		return false;
	}

	public String updatePassword(String oldPassword, String newPassword) {
		sendMessageString(Protocol.UPDATE_PASSWORD);
		sendMessageString(oldPassword);
		sendMessageString(newPassword);
		
		String risposta = null;
		try {
			risposta = (String) in.readObject();
		} catch (ClassNotFoundException | IOException e) {
			risposta = Protocol.ERROR;
			out = null;
		}
		return risposta;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Ordine> getOrdersUser() {
		sendMessageString(Protocol.GET_ORDERS_OF_USER);
		
		try {
			ArrayList<Ordine> ordini = (ArrayList<Ordine>) in.readObject();
			
			if(ordini != null)
				return ordini;
		} catch (ClassNotFoundException | IOException e) {
		}
		
		return null;
	}
}
	
	
