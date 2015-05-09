package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class ServerGUI extends Application {
	StackPane root;
	TableView tblView;
	TableColumn tcDispatcher, tcFuel, tcAutoWash, tcManualWash, tcLeftStation;

	@Override
	public void start(Stage primaryStage) throws Exception {
		root = new StackPane();
		
		tcManualWash = new TableColumn("Manual wash");
		tcManualWash.prefWidthProperty().set(150);
		tcAutoWash = new TableColumn("Auto wash");
		tcAutoWash.prefWidthProperty().set(150);
		tcDispatcher = new TableColumn("Dispatcher");
		tcDispatcher.prefWidthProperty().set(150);
		tcFuel = new TableColumn("Fuel");
		tcFuel.prefWidthProperty().set(150);
		tcLeftStation = new TableColumn("Left Station");
		tcLeftStation.prefWidthProperty().set(150);
		
		
		tblView = new TableView();
		tblView.prefHeight(50);
		tblView.getColumns().addAll(tcDispatcher, tcManualWash, tcAutoWash, tcFuel, tcLeftStation);
		
		root.getChildren().add(tblView);
	    primaryStage.setScene(new Scene(root, 750, 800));
	    primaryStage.setTitle("Gas Station Management System");
	    primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
