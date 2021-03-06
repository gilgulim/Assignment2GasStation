package ui;

import pl.CarStatusPacket.CarStatusType;
import bl.BlProxy;
import bl.Car;
import bl.ServerController;
import bl.observers.CarChangeState_Observer;
import bl.observers.FillingMainFuelPool_Observer;
import bl.observers.MinTarget_Observer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ServerGUI extends Application implements CarChangeState_Observer, FillingMainFuelPool_Observer, MinTarget_Observer{

	private StackPane root;
	private GridPane jgpRoot, jgpAddCar, jgpAddFuel, jgpStationStatus,
			jgpCarsStatus, jgpStatisticsHeader, jgpStatisticsTable,
			jgpStatisticsContainer;
	private Label jlbAddCarTitle, jlbAddCarID, jlbAddCarWantsWash,
			jlbAddCarFuelAmount, jlbAddFuelTitle, jlbAddFuelAmount,
			jlbCloseStationTitle, jlbCarStatusTitle, jlbStatisticsTitle,
			jlbStatisticsService, jlbStatisticsPump, jlbAddFuelBusy;
	private TextField jtfAddCarId, jtfAddCarFuelAmount, jtfAddFuelAmount;
	private CheckBox jchWantsWash;
	private Button jbnAddCarAdd, jbnAddFuelAdd, jbnCloseStationStatus, jbnStatisticsRun;
	
	private TableColumn<CarStatusRecord, String> jtcCarsStatusCarId, jtcCarsStatusFuel, jtcCarsStatusWash, jtcCarsStatusLeft;
	private ObservableList<CarStatusRecord> carStatusData;
	private TableColumn<String, String> jtcStatisticDate, jtcStatisticCar, jtcStatisticAction, jtcStatisticServiceId, jtcStatisticProfit;
	private ObservableList<StatisticsRecord> statisticsRecordsData;
	private ComboBox<String> jcbServiceType, jcbPump;
	private TableView<CarStatusRecord> jtvCarsStatus;
	private TableView<StatisticsRecord> jtvStatistics;
	
	private ServerController serverController;

	private final static int TEXT_FIELD_MAX_WIDTH = 80;

	
	@Override
	public void start(Stage primaryStage) throws Exception {

		serverController = ServerController.getServerController();
		serverController.attachCarStateChanedObserver(this);
		serverController.attachMainFuelPoolObserver(this);
		
		root = new StackPane();
		jgpRoot = new GridPane();
		setGridPaneSpacing(jgpRoot);
		initComponents();

		root.getChildren().add(jgpRoot);
		primaryStage.setScene(new Scene(root, 800, 600));
		primaryStage.setTitle("Gas Station Management System");
		primaryStage.show();

	}

	private void initComponents() {
		initAddCarSection();
		initAddFuelAmountSection();
		initGasStationStatus();
		initCarsStatus();
		initStationStatistics();

		jgpRoot.add(jgpAddCar, 0, 0);
		jgpRoot.add(jgpAddFuel, 1, 0);
		jgpRoot.add(jgpStationStatus, 0, 2);
		jgpRoot.add(jgpCarsStatus, 0, 1);
		jgpRoot.add(jgpStatisticsContainer, 1, 1);
	}

	private void initStationStatistics() {
		jlbStatisticsTitle = new Label("Statistics");
		jlbStatisticsTitle.setUnderline(true);
		jlbStatisticsService = new Label("Select Service");
		jlbStatisticsPump = new Label("Select Pump");

		jbnStatisticsRun = new Button("Generate");
		jbnStatisticsRun.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				getStatistics();

			}
		});

		jcbServiceType = new ComboBox<String>();
		jcbServiceType.getItems().addAll("Wash", "Fuel");
		jcbPump = new ComboBox<String>();
		
		jcbServiceType.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				if (jcbServiceType.getValue() == "Wash"){
					jcbPump.setDisable(true);
				}else{
					jcbPump.setDisable(false);
				}
				
			}
		});;
		
		int pumpCount = serverController.getNumberOfPumps();
		for(int i=1; i<=pumpCount; i++){
			jcbPump.getItems().add(String.format("%d", i));
		}
		
		TableColumn<StatisticsRecord, String> jtcStatisticDate = new TableColumn<StatisticsRecord, String>("Datetime");
		jtcStatisticDate.setCellValueFactory(new PropertyValueFactory<StatisticsRecord, String>("dateTime"));
		
		TableColumn<StatisticsRecord, String> jtcStatisticCar = new TableColumn<StatisticsRecord, String>("Car");
		jtcStatisticCar.setCellValueFactory(new PropertyValueFactory<StatisticsRecord, String>("carId"));
		
		TableColumn<StatisticsRecord, String> jtcStatisticAction = new TableColumn<StatisticsRecord, String>("Action Type");
		jtcStatisticAction.setCellValueFactory(new PropertyValueFactory<StatisticsRecord, String>("actionType"));
		
		TableColumn<StatisticsRecord, String> jtcStatisticServiceId = new TableColumn<StatisticsRecord, String>("Service ID");
		jtcStatisticServiceId.setCellValueFactory(new PropertyValueFactory<StatisticsRecord, String>("serviceId"));
		
		TableColumn<StatisticsRecord, String> jtcStatisticProfit = new TableColumn<StatisticsRecord, String>("Profit");
		jtcStatisticProfit.setCellValueFactory(new PropertyValueFactory<StatisticsRecord, String>("profit"));
		
		statisticsRecordsData = FXCollections.observableArrayList();

		jtvStatistics = new TableView<StatisticsRecord>();
		jtvStatistics.setItems(statisticsRecordsData);
		
		jtvStatistics.getColumns().addAll(	jtcStatisticDate, 
											jtcStatisticCar, 
											jtcStatisticAction, 
											jtcStatisticServiceId, 
											jtcStatisticProfit);
		

		jgpStatisticsHeader = new GridPane();
		setGridPaneSpacing(jgpStatisticsHeader);

		jgpStatisticsHeader.add(jlbStatisticsTitle, 0, 0);
		jgpStatisticsHeader.add(jlbStatisticsService, 0, 1);
		jgpStatisticsHeader.add(jcbServiceType, 1, 1);
		jgpStatisticsHeader.add(jlbStatisticsPump, 2, 1);
		jgpStatisticsHeader.add(jcbPump, 3, 1);
		jgpStatisticsHeader.add(jbnStatisticsRun, 4, 1);

		jgpStatisticsTable = new GridPane();
		setGridPaneSpacing(jgpStatisticsTable);

		jgpStatisticsTable.add(jtvStatistics, 0, 0);

		jgpStatisticsContainer = new GridPane();

		jgpStatisticsContainer.add(jgpStatisticsHeader, 0, 0);
		jgpStatisticsContainer.add(jgpStatisticsTable, 0, 1);
	}

	private void initCarsStatus() {
		jlbCarStatusTitle = new Label("Cars Statuses");
		jlbCarStatusTitle.setUnderline(true);
		
		jtcCarsStatusCarId = new TableColumn<CarStatusRecord, String>("Car");
		jtcCarsStatusCarId.setCellValueFactory(new PropertyValueFactory<CarStatusRecord, String>("CarId"));
		
		jtcCarsStatusFuel = new TableColumn<CarStatusRecord, String>("Fueling");
		jtcCarsStatusFuel.setCellValueFactory(new PropertyValueFactory<CarStatusRecord, String>("carFuel"));
		
		jtcCarsStatusWash = new TableColumn<CarStatusRecord, String>("Washing");
		jtcCarsStatusWash.setCellValueFactory(new PropertyValueFactory<CarStatusRecord, String>("carWash"));
		
		jtcCarsStatusLeft = new TableColumn<CarStatusRecord, String>("Left");
		jtcCarsStatusLeft.setCellValueFactory(new PropertyValueFactory<CarStatusRecord, String>("carLeft"));
		
		carStatusData = FXCollections.observableArrayList();
		
		jtvCarsStatus = new TableView<CarStatusRecord>();
		jtvCarsStatus.setItems(carStatusData);
		
		jtvCarsStatus.getColumns().addAll(jtcCarsStatusCarId, jtcCarsStatusFuel, jtcCarsStatusWash, jtcCarsStatusLeft);

		
		jgpCarsStatus = new GridPane();
		setGridPaneSpacing(jgpCarsStatus);

		jgpCarsStatus.add(jlbCarStatusTitle, 0, 0);
		// blank grid-pane in order to align the content of the screen
		GridPane jgpBlank = new GridPane();
		jgpBlank.add(new Label(""), 0, 0);
		setGridPaneSpacing(jgpBlank);

		jgpCarsStatus.add(jgpBlank, 0, 1);
		jgpCarsStatus.add(jtvCarsStatus, 0, 2);
	}

	private void initGasStationStatus() {
		jlbCloseStationTitle = new Label("Update Station Status");
		jlbCloseStationTitle.setUnderline(true);

		jbnCloseStationStatus = new Button("Close");
		jbnCloseStationStatus.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				closeGasStation();
			}
		});
		
		jgpStationStatus = new GridPane();
		setGridPaneSpacing(jgpStationStatus);

		jgpStationStatus.add(jlbCloseStationTitle, 0, 0);
		jgpStationStatus.add(jbnCloseStationStatus, 0, 1);
	}

	private void initAddFuelAmountSection() {
		jlbAddFuelAmount = new Label("Fuel Amount");
		jlbAddFuelTitle = new Label("Add Fuel to Main Pool");
		jlbAddFuelTitle.setUnderline(true);
		jlbAddFuelBusy = new Label("");
		jlbAddFuelBusy.setVisible(false);
		
		jtfAddFuelAmount = new TextField("0");
		jtfAddFuelAmount.setMaxWidth(TEXT_FIELD_MAX_WIDTH);

		jbnAddFuelAdd = new Button("Add");
		jbnAddFuelAdd.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				addFuelToMainRepository();	
			}
		});
		
		jgpAddFuel = new GridPane();
		setGridPaneSpacing(jgpAddFuel);

		jgpAddFuel.add(jlbAddFuelTitle, 0, 0);
		jgpAddFuel.add(jlbAddFuelAmount, 0, 1);
		jgpAddFuel.add(jtfAddFuelAmount, 1, 1);
		jgpAddFuel.add(jbnAddFuelAdd, 0, 2);
		jgpAddFuel.add(jlbAddFuelBusy, 1, 2);
	}

	private void initAddCarSection() {

		jlbAddCarTitle = new Label("Add Car to Gas Station");
		jlbAddCarTitle.setUnderline(true);
		jlbAddCarID = new Label("Car ID");
		jlbAddCarWantsWash = new Label("Wants Wash");
		jlbAddCarFuelAmount = new Label("Fuel Amount");

		jtfAddCarId = new TextField();
		jtfAddCarId.setMaxWidth(TEXT_FIELD_MAX_WIDTH);
		jtfAddCarFuelAmount = new TextField("0");
		jtfAddCarFuelAmount.setMaxWidth(TEXT_FIELD_MAX_WIDTH);

		jchWantsWash = new CheckBox();
		jchWantsWash.setSelected(true);

		jbnAddCarAdd = new Button("Add");

		jbnAddCarAdd.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				addCarToStation();
			}
		});

		jgpAddCar = new GridPane();
		setGridPaneSpacing(jgpAddCar);

		jgpAddCar.add(jlbAddCarTitle, 0, 0);
		jgpAddCar.add(jlbAddCarID, 0, 1);
		jgpAddCar.add(jlbAddCarWantsWash, 0, 2);
		jgpAddCar.add(jchWantsWash, 1, 2);
		jgpAddCar.add(jlbAddCarFuelAmount, 0, 3);
		jgpAddCar.add(jtfAddCarId, 1, 1);
		jgpAddCar.add(jtfAddCarFuelAmount, 1, 3);
		jgpAddCar.add(jbnAddCarAdd, 0, 4);
	}

	protected void addCarToStation() {
		int carId;
		boolean requiredWash;
		boolean requiredFuel;
		int fuelAmount = 0;

		try {
			carId = Integer.parseInt(jtfAddCarId.getText());
			requiredWash = jchWantsWash.isSelected();
			requiredFuel = Integer.parseInt(jtfAddCarFuelAmount.getText()) == 0 ? false
					: true;
			fuelAmount = Integer.parseInt(jtfAddCarFuelAmount.getText());
			
			ServerController.getServerController().addCar(carId, requiredFuel, fuelAmount, requiredWash);
			

			// clear form
			jtfAddCarId.setText("");
			jchWantsWash.setSelected(true);
			jtfAddFuelAmount.setText("0");

		} catch (Exception e) {
			// TODO:invalid input;
			System.out.println(e);
		}		
		
		
	}

	private void setGridPaneSpacing(GridPane gp) {
		gp.setPadding(new Insets(5));
		gp.setHgap(10);
		gp.setVgap(10);
	}

	private void getStatistics() {
		try{
		String serviceType = jcbServiceType.getSelectionModel().getSelectedItem();
		String pumpId = jcbPump.getSelectionModel().getSelectedItem();
		
		//clear table for new data
		statisticsRecordsData.clear();
		
		//add new data to the table
		statisticsRecordsData.addAll(serverController.getStatistics(serviceType, pumpId));
		}catch (Exception e){
			
		}
	}
	
	private void addFuelToMainRepository() {

		try{
			
			Thread fuelMainFuelPool = new Thread(new Runnable() {
				
				@Override
				public void run() {
					serverController.addFuelToMainRepository(Integer.parseInt(jtfAddFuelAmount.getText()));
					
				}
			});
			
			fuelMainFuelPool.start();

		}catch (Exception e){
			
		}	
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void updateCarState(Car car, CarStatusType state) {
		switch (state){
		case Entered:
			carStatusData.add(new  CarStatusRecord(String.valueOf(car.getId()), "", "", ""));
			break;
		case AutoWashing:
			carStatusData.add(new CarStatusRecord(String.valueOf(car.getId()), "", "V", ""));
			break;
		case Fueling:
			carStatusData.add(new CarStatusRecord(String.valueOf(car.getId()), "V", "", ""));
			break;
		case Exited:
			carStatusData.add(new CarStatusRecord(String.valueOf(car.getId()), "", "", "V"));
			break;
		default:
			break;
		}
	}

	private void closeGasStation() {
		serverController.closeGasStation();
		jbnCloseStationStatus.setDisable(true);
	}
	
	@Override
	public void updateMainPumpStartedFueling() {
			
		Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	
            	jlbAddFuelBusy.setVisible(true);
        		jbnAddFuelAdd.setDisable(true);
        		jlbAddFuelBusy.setText("Please wait while fuelling...");      
            	
            }
          });  

	}

	@Override
	public void updateMainPumpFinishedFueling() {
		
		Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	
        		jlbAddFuelBusy.setVisible(false);
        		jbnAddFuelAdd.setDisable(false);
            	
            }
          }); 

	}

	@Override
	public void mainFuelPoolReachedMinimum() {
		
		
		Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	
            	jlbAddFuelBusy.setVisible(true);
        		jlbAddFuelBusy.setText("Warnning low fuel in fuel pool.");      
            	
            }
          });  
		
		
	}
}
