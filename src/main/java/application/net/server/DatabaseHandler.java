package application.net.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import org.springframework.security.crypto.bcrypt.BCrypt;

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
			con = DriverManager.getConnection("jdbc:sqlite:database.db");
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
		
		String query = "INSERT INTO prodotto VALUES (?, ?, ?, ?, ?, ?)";
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
		
		System.out.println("[SERVER] Stampo il prod" + p);
		
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
	
	public synchronized Vector<Product> listaProdotti(String parametro) throws SQLException{
		if(con == null || con.isClosed()) 
			return null;
		
		String query = "SELECT * FROM prodotto WHERE nome LIKE ?;";
		PreparedStatement p = con.prepareStatement(query);
		p.setString(1,"%" + parametro + "%");

		ResultSet res = p.executeQuery();
		
		Vector<Product> prodotti = new Vector<Product>();
		
		while(res.next()) {
			try {
				Product prod = new Product(res.getString(1), res.getDouble(2), res.getInt(3), res.getBytes(4), res.getString(5), res.getString(6));
				prodotti.add(prod);
			} catch (SQLException e) {
				System.out.println("Cannot read one or more product from DB");
			}
		}
		p.close();
		return prodotti;
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
	
	private synchronized boolean isAlreadyInCart(String username, String nomeProdotto)  {
		try {
			String query = "SELECT * FROM ordini WHERE username_utente=? AND nome_prodotto=? AND evaso=0";
			PreparedStatement p = con.prepareStatement(query);
			
			p.setString(1, username);
			p.setString(2, nomeProdotto);
			
			ResultSet rs = p.executeQuery();
			
			return rs.next();
		}catch(SQLException e) {
			return false;
		}
		
	}
	
	public synchronized String addOrder(String username, String nomeProdotto) {
		try {	
			if(con == null || con.isClosed()) 
				return Protocol.ERROR_DB;
		
		
			if(isAlreadyInCart(username, nomeProdotto)) {
				return Protocol.PRODUCT_IS_ALREADY_IN_CART;
			}
			
			String query = "INSERT INTO ordini VALUES(null, ?, ?, 0, 1)";
			PreparedStatement p = con.prepareStatement(query);
			p.setString(1, username);
			p.setString(2, nomeProdotto);
			
			p.executeUpdate();
			p.close();
			
			return Protocol.PRODUCT_CORRECTLY_ADDED_IN_CART;
			
		} catch(SQLException e) {
			return Protocol.IMPOSSIBLE_ADD_PRODUCT_IN_CART;
		}
	}
	
	public synchronized Vector<ProductInCart> getProductInCart(String username) {
		try {
			if(con == null || con.isClosed()) 
				return null;
			
			Vector<ProductInCart> prodotti = new Vector<ProductInCart>();
			
			String query = "SELECT * FROM ordini,prodotto WHERE evaso=0 AND username_utente=? AND nome_prodotto=nome;";
			
			
			PreparedStatement pr = con.prepareStatement(query);
			pr.setString(1, username);
			
			ResultSet rs = pr.executeQuery();
			
			
			while(rs.next()) {
				String nomeP = rs.getString("nome");
				double prezzo = rs.getDouble("prezzo_generico");
				byte img[] = rs.getBytes("immagine");
				String descrizione = rs.getString("descrizione");
				
				ProductInCart product = new ProductInCart(nomeP, prezzo, img, descrizione);
				prodotti.add(product);
			}

			return prodotti;
		}
		catch(SQLException e) {
			return null;
		}	
	}
	
	public synchronized boolean removeProductFromCartOfUser(String usernameUser, String nomeProdotto) {
		try {
			if(con == null || con.isClosed()) 
				return false;
			
			String query = "DELETE FROM Ordini WHERE username_utente=? AND nome_prodotto=? AND evaso=0";
			PreparedStatement pr = con.prepareStatement(query);
			pr.setString(1, usernameUser);
			pr.setString(2, nomeProdotto);
			
			pr.execute();
			
			return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}	
	}
}
