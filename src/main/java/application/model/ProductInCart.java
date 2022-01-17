package application.model;


public class ProductInCart extends Product{

	private static final long serialVersionUID = 7228337179503386602L;
	
	private int quantitaNelCarrello;
	private double prezzoAcquisto;
	
	public ProductInCart(String nome, double prezzoGenerico, double prezzoAttuale, byte[] img, String descrizione, int quantita) {
		super(nome, prezzoGenerico, prezzoAttuale, img, descrizione);
		this.quantitaNelCarrello = quantita;
	}
	
	public ProductInCart(String nomeP, byte[] img, int quantita, double prezzoAcquisto2) {
		super(nomeP, img);
		this.prezzoAcquisto = prezzoAcquisto2;
		this.quantitaNelCarrello = quantita;
	}

	public int getQuantitaNelCarrello() {
		return quantitaNelCarrello;
	}
	
	public void setQuantitaNelCarrello(int quantità) {
		this.quantitaNelCarrello = quantità;
	}

	public void setPrezzoAcquisto(double prezzoAcquisto) {
		this.prezzoAcquisto = prezzoAcquisto;
	}
	
	public double getPrezzoAcquisto() {
		return prezzoAcquisto;
	}
}
