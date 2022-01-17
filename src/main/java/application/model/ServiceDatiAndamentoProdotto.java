package application.model;

import java.util.ArrayList;

import application.net.client.Client;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ServiceDatiAndamentoProdotto extends Service<ArrayList<DatiAndamentoProdotto>>{

	private String nomeProdotto;
	
	public void setNomeProdotto(String nomeProdotto) {
		this.nomeProdotto = nomeProdotto;
	}
	
	@Override
	protected Task<ArrayList<DatiAndamentoProdotto>> createTask() {
		return new Task<ArrayList<DatiAndamentoProdotto>>() {

			@Override
			protected ArrayList<DatiAndamentoProdotto> call() throws Exception {
				ArrayList<DatiAndamentoProdotto> array =  Client.getInstance().getTrendProduct(nomeProdotto);
				if(array == null)
					return null;
				
				for(DatiAndamentoProdotto d : array)
					System.out.println(d);
				return array;
			}
		};
	}

}
