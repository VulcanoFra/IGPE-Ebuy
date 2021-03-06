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
	private double prezzoAttuale;
	
	public Product(String nome, double prezzoGenerico, double prezzoAttuale, byte[] img, String descrizione) {
		this.nomeProdotto = nome;
		this.prezzoGenerico = prezzoGenerico;
		this.prezzoAttuale = prezzoAttuale;
		this.imgProdotto = img;
		this.descrizione = descrizione;
	}
	
	public Product(String nome, double prezzo, double prezzoA, int quantita, byte[] img, String categoria, String descrizione){
		this.nomeProdotto = nome;
		this.prezzoGenerico = prezzo;
		this.prezzoAttuale = prezzoA;
		this.quantita = quantita;
		this.imgProdotto = img;
		this.categoria = categoria;
		this.descrizione = descrizione;
	}
	
	public Product(String nomeP, byte[] img) {
		this.nomeProdotto = nomeP;
		this.imgProdotto = img;
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
	
	public double getPrezzoAttuale() {
		return prezzoAttuale;
	}
	
	public void setPrezzoGenerico(double prezzoGenerico) {
		this.prezzoGenerico = prezzoGenerico;
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
		return "Prodotto "+nomeProdotto + ", prezzo: " + prezzoGenerico + ", categoria: " + categoria + ",quantit?: "+ quantita + ", Image:" + imgProdotto;
	}
}
