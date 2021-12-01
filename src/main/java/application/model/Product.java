package application.model;

import java.io.Serializable;

public class Product implements Serializable {
	
	private static final long serialVersionUID = -2017388049932622216L;
	
	private String nomeProdotto;
	private double prezzoGenerico;
	private byte[] imgProdotto;
	private String descrizione;
	private int quantita;
	private String categoria;
	
	public Product(String nome, double prezzo, byte[] img, String descrizione) {
		this.nomeProdotto = nome;
		this.prezzoGenerico = prezzo;
		this.imgProdotto = img;
		this.descrizione = descrizione;
	}
	
	public Product(String nome, double prezzo, int quantita, byte[] img, String categoria, String descrizione){
		this.nomeProdotto = nome;
		this.prezzoGenerico = prezzo;
		this.quantita = quantita;
		this.imgProdotto = img;
		this.categoria = categoria;
		this.descrizione = descrizione;
	}
	
	public byte[] getImgProdotto() {
		return imgProdotto;
	}
	
	public String getNomeProdotto() {
		return nomeProdotto;
	}
	
	public double getPrezzoGenerico() {
		return prezzoGenerico;
	}
	
	public int getQuantita() {
		return quantita;
	}
	
	public String getCategoria() {
		return categoria;
	}
	
	public String getDescrizione() {
		return descrizione;
	}
	
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	
	public void setImgProdotto(byte[] imgProdotto) {
		this.imgProdotto = imgProdotto;
	}
	
	public void setNomeProdotto(String nomeProdotto) {
		this.nomeProdotto = nomeProdotto;
	} 
	
	public void setPrezzoAttuale(double prezzoAttuale) {
		this.prezzoGenerico = prezzoAttuale;
	}
	
	public void setQuantita(int quantita) {
		this.quantita = quantita;
	}
	
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	
	@Override
	public String toString() {
		return "Prodotto "+nomeProdotto + ", prezzo: " + prezzoGenerico + ", categoria: " + categoria + ",quantità: "+ quantita + ", Image:" + imgProdotto;
	}
}
