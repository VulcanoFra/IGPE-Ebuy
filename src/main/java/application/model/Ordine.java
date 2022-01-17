package application.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Ordine implements Serializable {

	private static final long serialVersionUID = -6867809727243481934L;
	
	private int id;
	private ArrayList<ProductInCart> prodottiOrdine;
	
	public Ordine(int id) {
		this.id = id;
	}
	
	public ArrayList<ProductInCart> getProdottiOrdine() {
		return prodottiOrdine;
	}
	
	public void setProdottiOrdine(ArrayList<ProductInCart> prodottiOrdine) {
		this.prodottiOrdine = prodottiOrdine;
	}
	
	public void aggiungiProdtto(ProductInCart p) {
		prodottiOrdine.add(p);
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public double getTotale(){
		double tot = 0.0;
		
		for(ProductInCart p : prodottiOrdine) {
			tot += p.getPrezzoAcquisto() * p.getQuantitaNelCarrello();
		}
		
		if(tot > 50)
			return tot;
		return tot + 5.0;
	}
	
	@Override
	public String toString() {
		String risp = id + "\n";
		
		
		for(ProductInCart p : prodottiOrdine) {
			risp += p.getNomeProdotto() + "  " + p.getPrezzoAcquisto() + "\n";
		}
		
		risp += "\n\n\n";
		
		return risp;
		
	}
	
}
