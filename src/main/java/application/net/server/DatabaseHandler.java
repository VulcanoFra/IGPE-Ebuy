package application.net.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

import org.springframework.security.crypto.bcrypt.BCrypt;

import application.model.DatiAndamentoProdotto;
import application.model.Product;
import application.model.ProductInCart;
import application.model.User;
import application.net.common.Protocol;

public class DatabaseHandler {
	
	private static DatabaseHandler instance = null;
	private Connection con = null;
	
	public static DatabaseHandler getInstance() {
		if(instance == null)
			instance = new DatabaseHandler();
		return instance;
	}
	
	private DatabaseHandler() {
		try {
			con = DriverManager.getConnection("jdbc:sqlite:ebuyDatabase.db");
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	public synchronized boolean insertUser(User user, boolean admin) throws SQLException {
		if(con == null || user == null || con.isClosed()) 
			return false;
		
		if(existUser(user))
			return false;
		
		String query = "INSERT INTO utente VALUES(?, ?, ?, ?, ?);";
		PreparedStatement p = con.prepareStatement(query);
		p.setString(1,user.getUsername());
		p.setString(2,user.getNome());
		p.setString(3,user.getCognome());
		p.setString(4, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12)));
		if(admin)
			p.setInt(5, 1);
		else
			p.setInt(5, 0);
		
		p.executeUpdate();
		p.close();
		return true;
	}
	
	public synchronized boolean existUser(User user) throws SQLException {
		if(con == null || user == null || con.isClosed()) 
			return false;
		
		try {
			String query = "SELECT * FROM utente WHERE username = ?;";
			PreparedStatement p = con.prepareStatement(query);
			p.setString(1, user.getUsername());
			ResultSet result = p.executeQuery();
			
			boolean res = result.next();
			
			p.close();
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public synchronized boolean checkUser(User user) throws SQLException {
		if(con == null || user == null || con.isClosed()) 
			return false;
		
		String query = "SELECT * FROM utente WHERE username = ?;";
		
		PreparedStatement p = con.prepareStatement(query);
		p.setString(1, user.getUsername());
		ResultSet result = p.executeQuery();
		boolean res = false;
		if(result.next()) {
			String pw = user.getPassword();
			String hashPw = result.getString("password");
			res = BCrypt.checkpw(pw, hashPw);
		}
		p.close();
		return res;
		
	}
	
	public synchronized boolean isAdmin(String username) throws SQLException {
		if(con == null || username.equals("") || con.isClosed()) 
			return false;

		String query = "SELECT * FROM utente WHERE username = ? AND admin = 1";

		PreparedStatement p = con.prepareStatement(query);
		p.setString(1, username);
		ResultSet res = p.executeQuery();
		return res.next();	
	}
	
	public synchronized boolean insertProduct(Product p) throws SQLException {
		if(con == null || p == null || con.isClosed()) 
			return false;
		
		String query = "INSERT INTO prodotto VALUES (?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement prep = con.prepareStatement(query);
		prep.setString(1, p.getNomeProdotto());
		prep.setDouble(2, p.getPrezzoGenerico());
		prep.setInt(3, p.getQuantita());
		if(p.getImgProdotto() == null)
			prep.setNull(4, Types.BLOB);
		else
			prep.setBytes(4, p.getImgProdotto());

		prep.setString(5, p.getCategoria());
		prep.setString(6, p.getDescrizione());
		prep.setDouble(7, p.getPrezzoAttuale());
		
		prep.executeUpdate();
		prep.close();
		return true;
	}

	public synchronized boolean existProduct(String nomeProdotto) throws SQLException {
		if(con == null || con.isClosed()) 
			return false;
			
		String query = "SELECT * FROM prodotto WHERE nome = ?;";
		PreparedStatement prep = con.prepareStatement(query);
		prep.setString(1, nomeProdotto);
		ResultSet res = prep.executeQuery();
		
		prep.close();
		return res.next();
		
	}
	
	public synchronized ArrayList<Product> listaProdotti(String parametro){
		try {
			if(con == null || con.isClosed()) 
				return null;
			
			String query = "SELECT * FROM prodotto WHERE nome LIKE ?;";
			PreparedStatement p = con.prepareStatement(query);
			p.setString(1,"%" + parametro + "%");

			ResultSet res = p.executeQuery();
			
			ArrayList<Product> prodotti = new ArrayList<Product>();
			
			while(res.next()) {
				try {
					Product prod = new Product(res.getString("nome"), res.getDouble("prezzo_generico"), res.getDouble("prezzo_attuale"),
							res.getInt("quantita_disponibile"), res.getBytes("immagine"), res.getString("categoria"), res.getString("descrizione"));
					prodotti.add(prod);
				} catch (SQLException e) {
					System.out.println("Cannot read one or more product from DB");
				}
			}
			
			p.close();
			return prodotti;
		} catch(SQLException e) {
			return null;
		}
		
		
	}
	
	public synchronized ArrayList<Product> getProductsByCategory(String categoria) {
		try {
			if(con == null || con.isClosed()) 
				return null;
			
			String query = "Select * from prodotto where categoria=?";
			PreparedStatement pr = con.prepareStatement(query);
			pr.setString(1, categoria);
			
			ArrayList<Product> prodotti = new ArrayList<Product>();
			
			ResultSet rs = pr.executeQuery();
			
			while(rs.next()) {
				try {
					Product prod = new Product(rs.getString("nome"), rs.getDouble("prezzo_generico"), rs.getDouble("prezzo_attuale"),
							rs.getInt("quantita_disponibile"), rs.getBytes("immagine"), rs.getString("categoria"), rs.getString("descrizione"));
					prodotti.add(prod);
				} catch (SQLException e) {
					System.out.println("Cannot read one or more product from DB");
				}
			}
			
			pr.close();
			return prodotti;
		} catch(SQLException e) {
			return null;
		}
	}
	
	public synchronized String productIsAvailable(String nomeProdotto) {
		try {
			if(con == null || nomeProdotto.equals("") || con.isClosed()) 
				return Protocol.ERROR_DB;
				
			
			String query = "SELECT * FROM prodotto WHERE nome = ?";
			PreparedStatement p = con.prepareStatement(query);
			p.setString(1, nomeProdotto);
			
			ResultSet rs = p.executeQuery();
			
			
			if(rs.next() && rs.getInt("quantita_disponibile") > 0) {
				p.close();
				return Protocol.PRODUCT_AVAILABLE;
			}
			
			p.close();
			return Protocol.PRODUCT_IS_NOT_AVAILABLE;
		}
		catch(SQLException e) {
			return Protocol.IMPOSSIBLE_ADD_PRODUCT_IN_CART;
		}
		
	}
	
	private synchronized int getIdCartUser(String username) {
		try {
			String query = "SELECT ID FROM ordini WHERE username_utente=? AND data IS NULL";
			PreparedStatement pr = con.prepareStatement(query);
			pr.setString(1, username);
			
			ResultSet rs = pr.executeQuery();
			
			if(rs.next()) {
				return rs.getInt("ID");
			} else {
				//Vuol dire che non ha un'istanza del suo carrello vuoto al momento
				String creaCart = "INSERT INTO ordini VALUES(null, ?, null);";
				PreparedStatement pr2 = con.prepareStatement(creaCart);
				pr2.setString(1, username);
				
				pr2.execute();
				
				return getIdCartUser(username);
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		//Se ritorna 0 vuol dire che c'Ë stato qualche problema
		return 0;
	}
	
	//OK
	private synchronized boolean isAlreadyInCart(String username, String nomeProdotto)  {
		try {
			
			int id = getIdCartUser(username);
			
			String query = "SELECT * FROM ordini,include WHERE ID=? AND ID=id_ordine AND nome_prodotto=?";
			PreparedStatement p = con.prepareStatement(query);
			
			p.setInt(1, id);
			p.setString(2, nomeProdotto);
			
			ResultSet rs = p.executeQuery();
			
			return rs.next();
		}catch(SQLException e) {
			return false;
		}
		
	}
	
	//OK
	public synchronized String addOrder(String username, String nomeProdotto) {
		try {	
			if(con == null || con.isClosed()) 
				return Protocol.ERROR_DB;
		
		
			if(isAlreadyInCart(username, nomeProdotto)) {
				return Protocol.PRODUCT_IS_ALREADY_IN_CART;
			}
			
			int id = getIdCartUser(username);
			
			if(id != 0) {
				String query = "INSERT INTO include VALUES(" + id + ", ?, 1)";
				PreparedStatement p = con.prepareStatement(query);
				p.setString(1, nomeProdotto);
				
				p.executeUpdate();
				p.close();
				
				return Protocol.PRODUCT_CORRECTLY_ADDED_IN_CART;
			}
			else {
				return Protocol.IMPOSSIBLE_ADD_PRODUCT_IN_CART;
			}
			
		} catch(SQLException e) {
			return Protocol.IMPOSSIBLE_ADD_PRODUCT_IN_CART;
		}
	}
	
	//OK
	public synchronized ArrayList<ProductInCart> getProductInCart(String username) {
		try {
			if(con == null || con.isClosed()) 
				return null;
			
			ArrayList<ProductInCart> prodotti = new ArrayList<ProductInCart>();
			
			int id = getIdCartUser(username);
			
			String query = "SELECT * FROM include, prodotto WHERE id_ordine=" + id + " AND nome_prodotto=nome;";

			PreparedStatement pr = con.prepareStatement(query);
			
			ResultSet rs = pr.executeQuery();
			
			
			while(rs.next()) {
				String nomeP = rs.getString("nome");
				double prezzoGenerico = rs.getDouble("prezzo_generico");
				double prezzoAttuale = rs.getDouble("prezzo_Attuale");
				byte img[] = rs.getBytes("immagine");
				String descrizione = rs.getString("descrizione");
				int quantita = rs.getInt("quantita");
				
				ProductInCart product = new ProductInCart(nomeP, prezzoGenerico, prezzoAttuale, img, descrizione, quantita);
				prodotti.add(product);
			}

			return prodotti;
		}
		catch(SQLException e) {
			return null;
		}	
	}
	
	//OK
	public synchronized boolean removeProductFromCartOfUser(String username, String nomeProdotto) {
		try {
			if(con == null || con.isClosed()) 
				return false;
			
			int id = getIdCartUser(username);
			
			String query = "DELETE FROM include WHERE id_ordine="+ id +" AND nome_prodotto=?";
			PreparedStatement pr = con.prepareStatement(query);
			pr.setString(1, nomeProdotto);
			
			pr.execute();
			
			return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}	
	}

	//Se ritorna 0 allora il prodotto Ë disponibile nella quantit‡ richiesta.
	//Altrimenti se non Ë disponibile nella quantit‡ richeista ritorna la quantit‡ disponibile
	public synchronized Integer quantityIsAvailable(String nomeProdotto, String username, Integer number) {
		try {
			if(con == null || con.isClosed()) 
				return null;
			
			String query = "SELECT * FROM prodotto WHERE nome=?";
			PreparedStatement pr = con.prepareStatement(query);
			pr.setString(1, nomeProdotto);
			
			ResultSet rs = pr.executeQuery();
			
			if(rs.getInt("quantita_disponibile") >= number) {
				return 0;
			}

			setQuantitaProdottoInOrdine(nomeProdotto, username, number);
			return rs.getInt("quantita_disponibile");
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	//OK
	public synchronized void setQuantitaProdottoInOrdine(String nomeProdotto, String username, Integer number) {
		try {
			if(con == null || con.isClosed()) 
				return;
			
			int id = getIdCartUser(username);
			
			String query = "UPDATE include SET quantita=? WHERE id_ordine=" + id + " AND nome_prodotto=?"; 
			PreparedStatement pr = con.prepareStatement(query);
			pr.setInt(1, number);
			pr.setString(2, nomeProdotto);
			pr.executeUpdate();
		} catch(SQLException e) {
			
		}
		
	}
	
	private synchronized boolean quantityOfEachProductInCartIsAvailable(Integer idOrdine) {
		try {
			if(con == null || con.isClosed()) 
				return false;
			
			String query = "Select * from include where id_ordine=" + idOrdine;
			PreparedStatement prst = con.prepareStatement(query);
			ResultSet rs = prst.executeQuery();
			
			Statement st = con.createStatement();
			
			while(rs.next()) {
				
				Integer quantita_richiesta = rs.getInt("quantita");
				
				String check = "select quantita_disponibile from prodotto where nome=\"" + rs.getString("nome_prodotto") + "\"";
				ResultSet result = st.executeQuery(check);
				
				
				Integer quantitaDisponibile = result.getInt("quantita_disponibile");
				
				if(quantitaDisponibile < quantita_richiesta) {
					System.out.println(quantitaDisponibile + " - " + quantita_richiesta);
					
					return false;
				}
					
			}
			
			return true;
			
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public synchronized String proceedToOrder(String usernameUtenteDaProcessare) {
		try {
			if(con == null || con.isClosed()) 
				return Protocol.ERROR_DB;
			
			int idOrdine = getIdCartUser(usernameUtenteDaProcessare);
						
			if(!quantityOfEachProductInCartIsAvailable(idOrdine)) {
				return Protocol.SOME_PRODUCT_ARE_UNAVAILABLE;
			}
			
			
			String query = "SELECT nome_prodotto, quantita FROM include WHERE id_ordine=" + idOrdine;
			PreparedStatement pr = con.prepareStatement(query);
			ResultSet rs = pr.executeQuery();
			
			//Per ogni prodotto nell'ordine diminuisci la quantit‡ disponibile
			while(rs.next()) {
				String update = "UPDATE prodotto SET quantita_disponibile=quantita_disponibile-" + rs.getInt("quantita") + " WHERE "
						+ "nome=\"" + rs.getString("nome_prodotto") + "\""; 
				
				Statement st = con.createStatement();
				st.executeUpdate(update);
				
			}
			
			String chiudiOrdine = "Update ordini set data=date('now') where ID=\"" + idOrdine + "\"";
			Statement statement = con.createStatement();
			
			statement.executeUpdate(chiudiOrdine);
			
			pr.close();
			statement.close();
			
			return Protocol.OK;
		} catch (SQLException e) {

			e.printStackTrace();
			return Protocol.ERROR_DB;
		}
	}
	
	public synchronized ArrayList<String> getAllCategories(){
		try {
			if(con == null || con.isClosed()) 
				return null;
			
			String query = "SELECT nome FROM categoria";
			Statement st = con.createStatement();
			
			ResultSet rs = st.executeQuery(query);
			
			ArrayList<String> categorie = new ArrayList<>();
			while(rs.next()) {
				categorie.add(rs.getString("nome"));
			}
			
			return categorie;
		} catch(SQLException e) {
			return null;
		}
		
	}
	
	public synchronized ArrayList<DatiAndamentoProdotto> getTrendProduct(String nomeProdotto){
		
		try {
			if(con == null || con.isClosed()) 
				return null;
			
			String query = "select data, nome_prodotto, sum(include.quantita) as quantita from ordini, include where include.nome_prodotto=? AND data IS NOT NULL AND include.id_ordine=ordini.ID group by data order by data";
			
			PreparedStatement pr = con.prepareStatement(query);
			pr.setString(1, nomeProdotto);

			ResultSet rs = pr.executeQuery();
			
			ArrayList<DatiAndamentoProdotto> dati = new ArrayList<DatiAndamentoProdotto>();
			
			while(rs.next()) {
				DatiAndamentoProdotto d = new DatiAndamentoProdotto();
				d.setData(rs.getString("data"));
				d.setQuantit‡Venduta(rs.getInt("quantita"));
				
				dati.add(d);
			}
			
			return dati;
		} catch(SQLException e) {
			return null;
		}
	}

	public synchronized String addQuantityProduct(String nomeProdotto, Integer quantita) {
		try {
			if(con == null || con.isClosed()) 
				return Protocol.ERROR_DB;
			
			String query = "UPDATE prodotto set quantita_disponibile=quantita_disponibile+? where nome=?";
			
			PreparedStatement pr = con.prepareStatement(query);
			pr.setInt(1, quantita);
			pr.setString(2, nomeProdotto);
			
			pr.executeUpdate();
			
			return Protocol.OK;
		} catch(SQLException e) {
			return Protocol.ERROR_DB;
		}
	}

	public synchronized String updatePassword(String oldPassword, String newPassword, String username) {
		try {
			if(con == null || con.isClosed()) 
				return Protocol.ERROR_DB;
			
			String query = "SELECT * FROM utente WHERE username=?";
			PreparedStatement pr = con.prepareStatement(query);
			pr.setString(1, username);
			
			ResultSet rs = pr.executeQuery();
			
			if(rs.next()) {
				String pw = rs.getString("password");
				if(!BCrypt.checkpw(oldPassword, pw)){
					return Protocol.OLD_PASSWORD_ERROR;
				} else{
					String hashpw = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));
					String update = "UPDATE utente SET password=? WHERE username=?";
					PreparedStatement pr2 = con.prepareStatement(update);
					pr2.setString(1, hashpw);
					pr2.setString(2, username);
					
					pr2.executeUpdate();
					
					return Protocol.OK;
				}
			} else {
				return Protocol.USER_NOT_EXISTS;
			}
		} catch(SQLException e) {
			return Protocol.ERROR_DB;
		}
	}

	public synchronized void checkQuantityAndSetDiscount() {
		try {
			if(con == null || con.isClosed()) 
				return;
			String query = "SELECT * FROM prodotto";
			Statement st = con.createStatement();
		
			ResultSet rs = st.executeQuery(query);
			
			while(rs.next()) {
				int quantita = rs.getInt("quantita_disponibile");
				double prezzoGenerico = rs.getDouble("prezzo_generico");
				String nome = rs.getString("nome");
				
				String up1 = "UPDATE prodotto SET prezzo_attuale=? WHERE nome=?;";
				PreparedStatement pr1 = con.prepareStatement(up1);
				pr1.setString(2, nome);
				
				Double prezzo = null;
				if(quantita <= 100) {
					prezzo = prezzoGenerico;
				} else if(quantita > 100 && quantita <= 199) {
					prezzo = prezzoGenerico * 0.90;
				} else if(quantita > 200 && quantita <= 499) {
					prezzo = prezzoGenerico * 0.75;
				} else {
					prezzo = prezzoGenerico * 0.50;
				}
				
			/*	DecimalFormat dfZero = new DecimalFormat("0.00");
				double p = Double.parseDouble(dfZero.format(prezzo));*/
				
				pr1.setDouble(1, prezzo);
				pr1.executeUpdate();
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
