package application.net.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Vector;

import application.model.Product;
import application.model.ProductInCart;
import application.model.User;
import application.net.common.Protocol;

public class CommunicationHandler implements Runnable{
	
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String usernameLoggato;
	private boolean loggato = false;
	private Server server = null;
	
	public CommunicationHandler(Socket socket, Server server) throws IOException {
		this.socket = socket;
		this.server = server;
		out = new ObjectOutputStream(socket.getOutputStream());
	}

	private void closeStreams(){
		try {
			if (out != null)
				out.close();
			out = null;
			if (in != null)
				in.close();
			in = null;
			if (socket != null)
				socket.close();
			socket = null;
		}catch(Exception e) {
			System.out.println("[SERVER] Non riesco a chiudere gli stream");

		}		
	}
	
	private void disconnect() {
		server.removeActiveUser(usernameLoggato);
		usernameLoggato = null;
		closeStreams();
	}
	
	@Override
	public void run() {
		try {
			if(in == null)
				in = new ObjectInputStream(socket.getInputStream());
			
			while(!loggato) {
				String input = (String) in.readObject();
				
				if(input.equals(Protocol.LOGIN)){
					User user = (User) in.readObject();
					
					if(server.isLogged(user.getUsername()) != null) {
						sendMessage(Protocol.USER_ALREADY_LOGGED);
						closeStreams();
						return;
					}
					
					if(!DatabaseHandler.getInstance().existUser(user)) {
						sendMessage(Protocol.USER_NOT_EXISTS);
						closeStreams();
						return;
					}
					if(!DatabaseHandler.getInstance().checkUser(user)) {
						sendMessage(Protocol.AUTHENTICATION_ERROR);
						closeStreams();
						return;
					}
					
					if(DatabaseHandler.getInstance().isAdmin(user.getUsername())){
						sendMessage(Protocol.OK_ADMIN);
						loggato = true;
						usernameLoggato = user.getUsername();
					}else {
						sendMessage(Protocol.OK);
						loggato = true;
						usernameLoggato = user.getUsername();
					}
				}
				else if(input.equals(Protocol.REGISTRATION)){
					User user = (User) in.readObject();
					if(DatabaseHandler.getInstance().existUser(user)) {
						sendMessage(Protocol.USER_EXISTS_ERROR);
						closeStreams();
						return;
					}
					else if(!DatabaseHandler.getInstance().insertUser(user,false)) {
						sendMessage(Protocol.ERROR);
						closeStreams();
						return;
					}else {
						sendMessage(Protocol.OK);
					}
				}
			}
			
			server.addActiveUser(usernameLoggato, out);
			
			while(!Thread.currentThread().isInterrupted()) {
				String input = (String) in.readObject();

				if(input.equals(Protocol.ADD_PRODUCT_IN_CATALOG)) {
					Product p = (Product)in.readObject();

					if(DatabaseHandler.getInstance().existProduct(p.getNomeProdotto()) || !DatabaseHandler.getInstance().insertProduct(p)) {
						sendMessage(Protocol.CANNOT_ADD_PRODUCT);
						closeStreams();
						return;
					}else {
						sendMessage(Protocol.OK);
					}
				}
				else if(input.equals(Protocol.GET_PRODUCT)) {
					String parametro = (String) in.readObject();
					//String parametro = (String) in.readObject();
					Vector<Product> prodotti = new Vector<Product>();
					prodotti = DatabaseHandler.getInstance().listaProdotti(parametro);
					
					if(prodotti == null) {
						//sendMessage(Protocol.GET_PRODUCT_FAILED);
						closeStreams();
						return;
					}	
					
					sendObject(prodotti);
					System.out.println("[SERVER] Prodotti inviati");
				}
				else if(input.equals(Protocol.ADD_PRODUCT_IN_CART)) {
					String nomeProdotto = (String)in.readObject();
					
					String rispostaDB = DatabaseHandler.getInstance().productIsAvailable(nomeProdotto);				
					
					if(rispostaDB.equals(Protocol.PRODUCT_AVAILABLE)) {
						String risposta = DatabaseHandler.getInstance().addOrder(usernameLoggato, nomeProdotto);
						sendObject(risposta);
					}
					else {
						sendObject(rispostaDB);
					}
				}
				else if(input.equals(Protocol.GET_PRODUCT_IN_CART)) {

					Vector<ProductInCart> prodotti = new Vector<ProductInCart>();
					
					prodotti = DatabaseHandler.getInstance().getProductInCart(usernameLoggato);
					if(prodotti == null) {
						System.out.println("Non invio prodotti in cart");
						//sendMessage(Protocol.GET_PRODUCT_FAILED);
						closeStreams();
						return;
					}
					sendObject(prodotti);
				}
				else if(input.equals(Protocol.CHECK_QUANTITY_AVAILABILITY)) {
					String nomeProdotto = (String) in.readObject();
					String number = (String) in.readObject();
					
					int rispostaDB = DatabaseHandler.getInstance().quantityIsAvailable(nomeProdotto, usernameLoggato, 
							Integer.parseInt(number));
					if(rispostaDB == 0) {
						sendMessage(Protocol.QUANTITY_AVAILABLE);
						DatabaseHandler.getInstance().setQuantitaProdottoInOrdine(nomeProdotto, 
								usernameLoggato, Integer.parseInt(number));
					} else {
						sendMessage(Protocol.QUANTITY_UNAVAILABLE);
						sendMessage(rispostaDB + "");
						DatabaseHandler.getInstance().setQuantitaProdottoInOrdine(nomeProdotto, 
								usernameLoggato, rispostaDB);
					}
				}
				else if(input.equals(Protocol.PROCEED_TO_ORDER)) {
					String usernameUtenteDaProcessare = usernameLoggato;
					String numberCard = (String) in.readObject();
					
					String rispostaDB = DatabaseHandler.getInstance().proceedToOrder(usernameUtenteDaProcessare);

					if(rispostaDB.equals(Protocol.OK)) {
						sendMessage(Protocol.ORDER_SUCCESS);
					} else if(rispostaDB.equals(Protocol.SOME_PRODUCT_ARE_UNAVAILABLE)){
						sendMessage(Protocol.SOME_PRODUCT_ARE_UNAVAILABLE);
					} else {
						sendMessage(Protocol.IMPOSSIBLE_PROCEED_ORDER);
					}
				}
				else if(input.equals(Protocol.REMOVE_PRODUCT_FROM_CART)) {
					String prodotto = (String) in.readObject();
					if(DatabaseHandler.getInstance().removeProductFromCartOfUser(usernameLoggato, prodotto)) {
						sendMessage(Protocol.PRODUCT_CORRECTLY_REMOVED_FROM_CART);
					} else {
						sendMessage(Protocol.IMPOSSIBLE_REMOVE_PRODUCT_FROM_CART);
					}
				}
				else if(input.equals(Protocol.EXIT)) {
					System.out.println(usernameLoggato + " si è scollegato");
					disconnect();
					closeStreams();
					loggato = false;
					return;
				}
				else {
					//sendMessage(Protocol.ERROR);
					System.out.println("[SERVER] Errore e chiudo stream");
					disconnect();
					closeStreams();
					return;
				}
			}
		} catch (IOException e) {
			if(usernameLoggato != null) {
				System.out.println(usernameLoggato + " si è scollegato");
				disconnect();
				loggato = false;				
			}
			return;
		} catch (SQLException e) {
			sendMessage(Protocol.ERROR);
			e.printStackTrace();
			loggato = false;
			return;
		} catch (ClassNotFoundException e) {
			sendMessage(Protocol.BAD_REQUEST);
			e.printStackTrace();
			disconnect();
			loggato = false;
			return;
		} 
		
	}
	
	private void sendMessage(String message) {
		sendObject(message);
	}
	
	private void sendObject(Object object)  {
		if(out == null)
			return;
		try {
			out.writeObject(object);
			out.flush();
		} catch (IOException e) {
			
			closeStreams();
			e.printStackTrace();
			return;
		}
	}
}



