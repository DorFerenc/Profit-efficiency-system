package application;
	
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

import MVC.Company;
import MVC.CompanyController;
import View.CompanyView;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application implements Serializable{
	@Override
	public void start(Stage primaryStage) throws FileNotFoundException, ClassNotFoundException, IOException, Exception {
		Company programModel = new Company();
		CompanyView dynamicView = new CompanyView(primaryStage);
		CompanyController controller = new CompanyController(programModel, dynamicView);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
