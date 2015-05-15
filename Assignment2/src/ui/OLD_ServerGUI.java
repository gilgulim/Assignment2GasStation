package ui;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.sun.deploy.uitoolkit.impl.fx.ui.FXAboutDialog;

import bl.BlProxy;
import bl.Car;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class OLD_ServerGUI extends Application {
	BlProxy bl;
	StackPane root;
	TableView tblView;
	TableColumn tcDispatcher, tcFuel, tcAutoWash, tcManualWash, tcLeftStation;
	ObservableList obsList;
	//TODO:delete car
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		bl = BlProxy.getBlProxy();

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
	    displayGasStationCarStatus();
	}

	private void displayGasStationCarStatus() {
		tblView.setItems(obsList);
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	public static void carState(){
		String a,b,c,d,e;
	}
}
