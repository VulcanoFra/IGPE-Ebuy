package application.net.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;

import application.model.Product;
import application.model.ProductInCart;
import application.model.User;
import application.net.common.Protocol;
import application.view.SceneHandlerVecchio;

public class Client implements Runnable{
	
	private Socket socket = null;
	private static Client instance = null;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	private Client() {
		try {
			socket = new Socket("localhost", 8000);
			out = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			out = null;
			SceneHandlerVecchio.getInstance().showError("Client cannot connect");
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
			e.printStackTrace();
			out = null;			
			return false;
		}
	}
	
	@Override
	public void run(){
		while(out != null && in != null) {
			/*try {
				Object message = (Object) in.readObject();
				if(message.equals(Protocol.ERROR)) {
					out = null;
					return;
				}
			} catch (ClassNotFoundException | IOException e) {
				out = null;
				//SceneHandlerVecchio.getInstance().showError("Cannot connect");
			}*/
		}
		
	}
	
	
	public String login(String username, String password) {		
		sendMessageString(Protocol.LOGIN);
		sendObject(new User(username, password));
		try {
			if(in == null)
				in = new ObjectInputStream(socket.getInputStream());
			String res = (String) in.readObject();
			/*if(res.equals(Protocol.OK) || res.equals(Protocol.OK_ADMIN))
				actualUser = new User(username ,password);*/
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
	public Vector<Product> getProduct(String parametro)  {
		sendMessageString(Protocol.GET_PRODUCT);
		sendMessageString(parametro);

		try {
			
			if(in == null) 
				in = new ObjectInputStream(socket.getInputStream());			
			
			Vector<Product> prodotti = new Vector<Product>();
			prodotti = (Vector<Product>) in.readObject();
			
			return prodotti;
		}
		catch(Exception e) {
			e.printStackTrace();
			out = null;
			return null;
		}	
	}

	@SuppressWarnings("unchecked")
	public Vector<ProductInCart> getProductInCart()  {
		sendMessageString(Protocol.GET_PRODUCT_IN_CART);

		try {
			if(in == null) 
				in = new ObjectInputStream(socket.getInputStream());			
			
			Vector<ProductInCart> prodotti = new Vector<ProductInCart>();
			prodotti = (Vector<ProductInCart>) in.readObject();
			
			return prodotti;
		}
		catch(Exception e) {
			e.printStackTrace();
			out = null;
			return null;
		}	
	}
	
	public void addAProductInCart(String nomeProdotto) {
		sendMessageString(Protocol.ADD_PRODUCT_IN_CART);
		sendMessageString(nomeProdotto);
		
		try {
			String risposta = (String) in.readObject();
			
			if(risposta.equals(Protocol.PRODUCT_CORRECTLY_ADDED_IN_CART)) {
				SceneHandlerVecchio.getInstance().showInfo("Prodotto aggiunto correttamente al carrello");
				return;
			}
			else if(risposta.equals(Protocol.ERROR_DB)) {
				SceneHandlerVecchio.getInstance().showError("Errore interno al database, invitiamoa riprovare più tardi. A breve una manutenzione");
			}
			else {
				SceneHandlerVecchio.getInstance().showWarning(risposta);
				return;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void procediAllOrdine() {
		sendMessageString(Protocol.PROCEED_TO_ORDER);
	}
}
	
	
