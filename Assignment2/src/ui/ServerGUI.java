package ui;

import javax.swing.text.TableView.TableCell;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
 
public class ServerGUI extends Application {
 
    private final TableView<CarState> table = new TableView<CarState>();
    private final ObservableList<CarState> data =
            FXCollections.observableArrayList(
            new CarState("Jacob", "Smith", "jacob.smith@example.com"),
            new CarState("Isabella", "Johnson", "isabella.johnson@example.com"),
            new CarState("Ethan", "Williams", "ethan.williams@example.com"),
            new CarState("Emma", "Jones", "emma.jones@example.com"),
            new CarState("Michael", "Brown", "michael.brown@example.com"));
    final HBox hb = new HBox();
 
    public static void main(String[] args) {
        launch(args);
    }
 
    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setWidth(800);
        stage.setHeight(600);
 
		TableColumn tcDispatcher = new TableColumn("Dispatcher");
        tcDispatcher.setPrefWidth(150);
     // SETTING THE CELL FACTORY FOR THE ALBUM ART                 
 
        
        
        
        
        
        
        TableColumn tcManualWash = new TableColumn("Manual Wash");
        tcManualWash.setPrefWidth(150);
        tcManualWash.setCellValueFactory(
                new PropertyValueFactory<>("firstName"));
 
        TableColumn tcAutoWash = new TableColumn("Auto Wash");
        tcAutoWash.setPrefWidth(150);
        tcAutoWash.setCellValueFactory(
                new PropertyValueFactory<>("lastName"));
 
        TableColumn tcFuel = new TableColumn("Fuel");
        tcFuel.setPrefWidth(150);
        tcFuel.setCellValueFactory(
                new PropertyValueFactory<>("email"));
 
        TableColumn tcLeftStation = new TableColumn("LeftStation");
        tcLeftStation.setPrefWidth(150);
        table.setItems(data);
        table.getColumns().addAll(tcDispatcher,tcManualWash, tcAutoWash, tcFuel, tcLeftStation);
 
        final TextField addFirstName = new TextField();
        addFirstName.setPromptText("First Name");
        addFirstName.setMaxWidth(tcManualWash.getPrefWidth());
        final TextField addLastName = new TextField();
        addLastName.setMaxWidth(tcAutoWash.getPrefWidth());
        addLastName.setPromptText("Last Name");
        final TextField addEmail = new TextField();
        addEmail.setMaxWidth(tcFuel.getPrefWidth());
        addEmail.setPromptText("Email");
 
        final Button addButton = new Button("Add");
        addButton.setOnAction((ActionEvent e) -> {
            data.add(new CarState(
                    addFirstName.getText(),
                    addLastName.getText(),
                    addEmail.getText()));
            addFirstName.clear();
            addLastName.clear();
            addEmail.clear();
        });
 
        hb.getChildren().addAll(addFirstName, addLastName, addEmail, addButton);
        hb.setSpacing(3);
 
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll( table, hb);
 
        ((Group) scene.getRoot()).getChildren().addAll(vbox);
 
        stage.setScene(scene);
        stage.show();
    }
 
    public static class CarState {
 
        private final SimpleStringProperty firstName;
        private final SimpleStringProperty lastName;
        private final SimpleStringProperty email;
 
        private CarState(String fName, String lName, String email) {
            this.firstName = new SimpleStringProperty(fName);
            this.lastName = new SimpleStringProperty(lName);
            this.email = new SimpleStringProperty(email);
        }
 
        public String getFirstName() {
            return firstName.get();
        }
 
        public void setFirstName(String fName) {
            firstName.set(fName);
        }
 
        public String getLastName() {
            return lastName.get();
        }
 
        public void setLastName(String fName) {
            lastName.set(fName);
        }
 
        public String getEmail() {
            return email.get();
        }
 
        public void setEmail(String fName) {
            email.set(fName);
        }
    }
} 

