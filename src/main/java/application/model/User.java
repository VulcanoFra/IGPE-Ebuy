package application.model;

import java.io.Serializable;

public class User implements Serializable{
	
	private static final long serialVersionUID = 5257249663796286997L;
	
	private String username;
	private String password;
	private String nome;
	private String cognome;

	public User(String username) {
		this.username = username;
	}
	
	public User(String username, String pass) {
		this.username = username;
		this.password = pass;
	}
	
	public User(String username, String pass, String nome, String cognome) {
		this.username = username;
		this.password = pass;
		this.nome = nome;
		this.cognome = cognome;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getNome() {
		return nome;
	}

	public String getCognome() {
		return cognome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
}

