package application.view;


import javafx.scene.layout.StackPane;

public class StackPaneHome extends StackPane{

	private static StackPaneHome instance = null;
	
	private StackPaneHome() {}
	
	public static StackPaneHome getInstance() {
		if(instance == null )
			instance = new StackPaneHome();
		return instance;
	}
}
