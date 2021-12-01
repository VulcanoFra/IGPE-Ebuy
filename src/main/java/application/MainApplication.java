package application;

import application.view.SceneHandlerVecchio;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApplication extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		SceneHandlerVecchio.getInstance().init(primaryStage);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
