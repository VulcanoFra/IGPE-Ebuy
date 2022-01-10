package application.controller;

import java.util.List;

import application.model.DatiAndamentoProdotto;
import application.model.ServiceDatiAndamentoProdotto;
import application.view.SceneHandler;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AndamentoProdottoController {

    @FXML
    private BorderPane borderPane;
     
    private String nomeProdotto;
    private ServiceDatiAndamentoProdotto service = new ServiceDatiAndamentoProdotto();
    
    public void setNomeProdotto(String nomeProdotto) {
		this.nomeProdotto = nomeProdotto;
	}
    
    @FXML
    void initialize() {
    	service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				
				CategoryAxis xAxis = new CategoryAxis();
				NumberAxis yAxis = new NumberAxis();
				BarChart<String, Number> barchart = new BarChart<String, Number>(xAxis, yAxis);
				barchart.setTitle("Somministrazioni vaccini - Regione: ");
				xAxis.setLabel("Data");
				yAxis.setLabel("Quantit‡");
				
				@SuppressWarnings("unchecked")
				List<DatiAndamentoProdotto> result = (List<DatiAndamentoProdotto>) event.getSource().getValue();				
				
				XYChart.Series<String, Number> andamento = new XYChart.Series<String, Number>();

				DatiAndamentoProdotto dati = new DatiAndamentoProdotto();
				dati.setData("2022-01-04"); dati.setQuantit‡Venduta(50);
				
				DatiAndamentoProdotto dati1 = new DatiAndamentoProdotto();
				dati.setData("2022-02-08"); dati.setQuantit‡Venduta(150);
				
				DatiAndamentoProdotto dati2 = new DatiAndamentoProdotto();
				dati.setData("2022-02-09"); dati.setQuantit‡Venduta(20);
				
				andamento.getData().add(new XYChart.Data<String, Number>(dati.getData(), dati.getQuantit‡Venduta()));
				andamento.getData().add(new XYChart.Data<String, Number>(dati1.getData(), dati1.getQuantit‡Venduta()));
				andamento.getData().add(new XYChart.Data<String, Number>(dati2.getData(), dati2.getQuantit‡Venduta()));

				
				/*for(DatiAndamentoProdotto d : result) {
					System.out.println(d);
					
					andamento.getData().add(new XYChart.Data<String, Number>(d.getData(), d.getQuantit‡Venduta()));
						
				}*/
				barchart.getData().add(andamento);
				borderPane.setCenter(barchart);
				/*Stage stage = new Stage();
				Scene scena = new Scene(borderPane);
				
				stage.setScene(scena);
				stage.showAndWait();*/
			}
		});
    	
    	service.setOnFailed(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				SceneHandler.getInstance().showError("ERROREE");
			}
		});
    }
    
    public void setEvent() {
    	service.setNomeProdotto(nomeProdotto);
    	service.restart();
    	
    }

}
