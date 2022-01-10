package application.model;

import java.io.Serializable;

public class DatiAndamentoProdotto implements Comparable<DatiAndamentoProdotto>, Serializable{

	private static final long serialVersionUID = -4721160539851844321L;
	
	private String data;
	private Number quantit‡Venduta;
	
	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	public Number getQuantit‡Venduta() {
		return quantit‡Venduta;
	}
	
	public void setQuantit‡Venduta(Number quantit‡Venduta) {
		this.quantit‡Venduta = quantit‡Venduta;
	}

	@Override
	public int compareTo(DatiAndamentoProdotto o) {
		return data.compareTo(o.data);
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return data + " : " + quantit‡Venduta;
	}
	
}
