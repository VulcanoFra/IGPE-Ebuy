package application.model;

public class ProductInCart extends Product{

	private static final long serialVersionUID = 7228337179503386602L;
	
	private int quantitaNelCarrello;
	
	public ProductInCart(String nome, double prezzo, byte[] img, String descrizione, int quantita) {
		super(nome, prezzo, img, descrizione);
		this.quantitaNelCarrello = quantita;
	}
	
	public int getQuantit�() {
		return quantitaNelCarrello;
	}
	
	public void setQuantit�(int quantit�) {
		this.quantitaNelCarrello = quantit�;
	}

}
