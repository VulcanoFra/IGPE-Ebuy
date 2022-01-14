package application.model;

public class ProductInCart extends Product{

	private static final long serialVersionUID = 7228337179503386602L;
	
	private int quantitaNelCarrello;
	
	public ProductInCart(String nome, double prezzoGenerico, double prezzoAttuale, byte[] img, String descrizione, int quantita) {
		super(nome, prezzoGenerico, prezzoAttuale, img, descrizione);
		this.quantitaNelCarrello = quantita;
	}
	
	public int getQuantitaNelCarrello() {
		return quantitaNelCarrello;
	}
	
	public void setQuantitaNelCarrello(int quantità) {
		this.quantitaNelCarrello = quantità;
	}

}
