package application.controller;

import java.util.List;
import application.model.DatiAndamentoProdotto;
import application.model.ServiceDatiAndamentoProdotto;
import application.view.SceneHandler;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;

public class AndamentoProdottoController {

    @FXML
    private BorderPane borderPane;
     
    private String nomeProdotto;
    private ServiceDatiAndamentoProdotto service = new ServiceDatiAndamentoProdotto();
    
    public void setNomeProdotto(String nomeProdotto) {
		this.nomeProdotto = nomeProdotto;
		this.service.setNomeProdotto(nomeProdotto);
    }
    
    @FXML
    void initialize() {
    	service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				
				CategoryAxis xAxis = new CategoryAxis();
				NumberAxis yAxis = new NumberAxis();
				LineChart<String, Number> lineChart = new LineChart<String, Number>(xAxis, yAxis);
				lineChart.setTitle("Andamento Prodotto: " + nomeProdotto);
				xAxis.setLabel("Data");
				yAxis.setLabel("Quantit‡");
				
				@SuppressWarnings("unchecked")
				List<DatiAndamentoProdotto> result = (List<DatiAndamentoProdotto>) event.getSource().getValue();				
				
				XYChart.Series<String, Number> andamento = new XYChart.Series<String, Number>();
				
				for(DatiAndamentoProdotto d : result) {					
					andamento.getData().add(new XYChart.Data<String, Number>(d.getData(), d.getQuantit‡Venduta()));	
				}
				lineChart.getData().add(andamento);
				borderPane.setCenter(lineChart);
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
    	service.restart();
    }

}
