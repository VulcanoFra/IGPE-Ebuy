package application.model;

import java.util.ArrayList;
import java.util.List;

public class Ordine {
	private List<ProductInCart> prodottiOrdine;
	
	public Ordine() {
		prodottiOrdine = new ArrayList<ProductInCart>();
	}
	
	public double getTotale() {
		double totale = 0.0;
		for(ProductInCart p : prodottiOrdine) {
			totale += p.getPrezzoGenerico();
		}
		
		return totale;
	}
}
