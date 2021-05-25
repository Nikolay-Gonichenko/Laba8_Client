package Controllers;

import Client.ClientMain;
import data.Coordinates;
import data.FuelType;
import data.Vehicle;
import data.VehicleType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class TableWindowController {
    private ResourceBundle rb;
    private static Stage stage;

    public static void setStage(Stage stage) {
        TableWindowController.stage = stage;
    }

    private static Locale locale;

    public static void setLocale(Locale locale) {
        TableWindowController.locale = locale;
    }

    ObservableList<String> fields = FXCollections.observableArrayList("Id", "Name", "X", "Y", "Engine power",
            "Capacity", "Creation Date", "Fuel Type", "Vehicle Type");
    List<Vehicle> vehiclesArrayList = new ArrayList<>();
    @FXML
    private Button goToVisualizationButton;

    @FXML
    private Button goToConsoleButton;
    @FXML
    private Label tableLabel;

    @FXML
    private TableView<Vehicle> table;
    @FXML
    private TableColumn<Vehicle, Integer> capacityColumn;

    @FXML
    private TableColumn<Vehicle, Float> xColumn;

    @FXML
    private TableColumn<Vehicle, String> nameColumn;

    @FXML
    private TableColumn<Vehicle, String> vehicleTypeColumn;

    @FXML
    private TableColumn<Vehicle, String> fuelTypeColumn;

    @FXML
    private TableColumn<Vehicle, Double> yColumn;
    @FXML
    private TableColumn<Vehicle, String> dateColumn;

    @FXML
    private TableColumn<Vehicle, Float> enginePowerColumn;
    @FXML
    private TableColumn<Vehicle, Integer> idColumn;
    @FXML
    private TextField valuesTextField;
    @FXML
    private Button filterButton;
    @FXML
    private Button sortButton;
    @FXML
    private Button updateButton;
    @FXML
    private ChoiceBox<String> fieldsChoiceBox;

    @FXML
    void goToVisualization(ActionEvent event) throws IOException {
        Parent content = FXMLLoader.load(getClass().getResource("../visualization.fxml"));
        Stage stage = changeWindow(content);
        VisualizationWindowController.setStage(stage);
    }

    @FXML
    void goToConsole(ActionEvent event) throws IOException {
        Parent content = FXMLLoader.load(getClass().getResource("../console.fxml"));
        Stage stage = changeWindow(content);
        ConsoleWindowController.setStage(stage);
    }

    private Stage changeWindow(Parent content) {
        Scene scene = new Scene(content);
        Stage newStage = new Stage();
        newStage.setMinHeight(50);
        newStage.setMinWidth(50);
        newStage.setScene(scene);
        stage.close();
        newStage.setTitle("Life of vehicles");
        newStage.show();
        return newStage;
    }

    @FXML
    void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<Vehicle, Integer>("id"));
        xColumn.setCellValueFactory(new PropertyValueFactory<Vehicle, Float>("x"));
        yColumn.setCellValueFactory(new PropertyValueFactory<Vehicle, Double>("y"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("creationDate"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("name"));
        enginePowerColumn.setCellValueFactory(new PropertyValueFactory<Vehicle, Float>("enginePower"));
        fuelTypeColumn.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("fuelType"));
        vehicleTypeColumn.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("vehicleType"));
        capacityColumn.setCellValueFactory(new PropertyValueFactory<Vehicle, Integer>("capacity"));


        initVehicles();

        initLocale();

        fieldsChoiceBox.setValue("Id");
        fieldsChoiceBox.setItems(fields);

    }

    private void initLocale() {
        rb = ResourceBundle.getBundle("text", locale);

        tableLabel.setText(setString("table.label"));
        goToConsoleButton.setText(setString("goToConsole.button"));
        goToVisualizationButton.setText(setString("goTiVis.button"));
        filterButton.setText(setString("filter.button"));
        sortButton.setText(setString("sort.button"));
        updateButton.setText(setString("update.button"));
    }

    private void initVehicles() {
        ObservableList<Vehicle> vehicles = FXCollections.observableArrayList();
        String collection = ClientMain.getCollection();
        //System.out.println(collection);
        String[] collectionArray = collection.split(",");
        for (int i = 0; i < collectionArray.length; i += 10) {
            int id = Integer.parseInt(collectionArray[i]);
            String name = collectionArray[i + 1];
            float x = Float.parseFloat(collectionArray[i + 2]);
            double y = Double.parseDouble(collectionArray[i + 3]);
            LocalDate date = LocalDate.parse(collectionArray[i + 4]);
            int capacity = Integer.parseInt(collectionArray[i + 5]);
            float enginePower = Float.parseFloat(collectionArray[i + 6]);
            FuelType fuelType;
            try {
                fuelType = FuelType.valueOf(collectionArray[i + 7]);
            } catch (IllegalArgumentException e) {
                fuelType = null;
            }
            VehicleType vehicleType;
            try {
                vehicleType = VehicleType.valueOf(collectionArray[i + 8]);
            } catch (IllegalArgumentException e) {
                vehicleType = null;
            }
            Vehicle vehicle = new Vehicle(id, name, new Coordinates(x, y), date, enginePower, capacity, vehicleType, fuelType);
            vehicles.add(vehicle);
            vehiclesArrayList.add(vehicle);
        }
        table.setItems(vehicles);
    }

    private String setString(String text) {
        String stringToSet = rb.getString(text);
        stringToSet = new String(stringToSet.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        return stringToSet;
    }


    @FXML
    public void filter(ActionEvent actionEvent) {
        table.getItems().removeAll(vehiclesArrayList);
        String valueName = fieldsChoiceBox.getValue();
        String filterValue = valuesTextField.getText();
        ObservableList<Vehicle> filterVehicles = FXCollections.observableArrayList();
        if (valueName.equals("Id")) {
            filterVehicles.addAll(vehiclesArrayList.stream().filter(v -> String.valueOf(v.getId()).contains(filterValue)).collect(Collectors.toList()));
        }
        if (valueName.equals("Name")) {
            filterVehicles.addAll(vehiclesArrayList.stream().filter(v -> String.valueOf(v.getName()).contains(filterValue)).collect(Collectors.toList()));
        }
        if (valueName.equals("X")) {
            filterVehicles.addAll(vehiclesArrayList.stream().filter(v -> String.valueOf(v.getX()).contains(filterValue)).collect(Collectors.toList()));
        }
        if (valueName.equals("Y")) {
            filterVehicles.addAll(vehiclesArrayList.stream().filter(v -> String.valueOf(v.getY()).contains(filterValue)).collect(Collectors.toList()));
        }
        if (valueName.equals("Engine power")) {
            filterVehicles.addAll(vehiclesArrayList.stream().filter(v -> String.valueOf(v.getEnginePower()).contains(filterValue)).collect(Collectors.toList()));
        }
        if (valueName.equals("Capacity")) {
            filterVehicles.addAll(vehiclesArrayList.stream().filter(v -> String.valueOf(v.getCapacity()).contains(filterValue)).collect(Collectors.toList()));
        }
        if (valueName.equals("Creation Date")) {
            filterVehicles.addAll(vehiclesArrayList.stream().filter(v -> String.valueOf(v.getCreationDate()).contains(filterValue)).collect(Collectors.toList()));
        }
        if (valueName.equals("Vehicle Type")) {
            filterVehicles.addAll(vehiclesArrayList.stream().filter(v -> String.valueOf(v.getVehicleType()).contains(filterValue)).collect(Collectors.toList()));
        }
        if (valueName.equals("Fuel Type")) {
            filterVehicles.addAll(vehiclesArrayList.stream().filter(v -> String.valueOf(v.getFuelType()).contains(filterValue)).collect(Collectors.toList()));
        }
        table.setItems(filterVehicles);
    }
    @FXML
    public void sort(ActionEvent actionEvent) {
        table.getItems().removeAll(vehiclesArrayList);
        String valueName = fieldsChoiceBox.getValue();

        ObservableList<Vehicle> sortVehicles = FXCollections.observableArrayList();
        if (valueName.equals("Id")) {
            sortVehicles.addAll(vehiclesArrayList.stream().sorted(Comparator.comparing(Vehicle::getId)).collect(Collectors.toList()));
        }
        if (valueName.equals("Name")) {
            sortVehicles.addAll(vehiclesArrayList.stream().sorted(Comparator.comparing(Vehicle::getName)).collect(Collectors.toList()));
        }
        if (valueName.equals("X")) {
            sortVehicles.addAll(vehiclesArrayList.stream().sorted(Comparator.comparing(Vehicle::getX)).collect(Collectors.toList()));
        }
        if (valueName.equals("Y")) {
            sortVehicles.addAll(vehiclesArrayList.stream().sorted(Comparator.comparing(Vehicle::getY)).collect(Collectors.toList()));
        }
        if (valueName.equals("Engine power")) {
            sortVehicles.addAll(vehiclesArrayList.stream().sorted(Comparator.comparing(Vehicle::getEnginePower)).collect(Collectors.toList()));
        }
        if (valueName.equals("Capacity")) {
            sortVehicles.addAll(vehiclesArrayList.stream().sorted(Comparator.comparing(Vehicle::getCapacity)).collect(Collectors.toList()));
        }
        if (valueName.equals("Creation Date")) {
            sortVehicles.addAll(vehiclesArrayList.stream().sorted(Comparator.comparing(Vehicle::getCreationDate)).collect(Collectors.toList()));
        }
        if (valueName.equals("Vehicle Type")) {
            sortVehicles.addAll(vehiclesArrayList.stream().sorted(Comparator.comparing(Vehicle::getVehicleType)).collect(Collectors.toList()));
        }
        if (valueName.equals("Fuel Type")) {
            sortVehicles.addAll(vehiclesArrayList.stream().sorted(Comparator.comparing(Vehicle::getFuelType)).collect(Collectors.toList()));
        }
        table.setItems(sortVehicles);
    }
    @FXML
    public void update(ActionEvent actionEvent) {
        Vehicle vehicle = table.getSelectionModel().getSelectedItem();
        UpdateWindowController.setAllInit(String.valueOf(vehicle.getId()), vehicle.getName(),
                String.valueOf(vehicle.getX()),String.valueOf(vehicle.getY()),
                String.valueOf(vehicle.getCapacity()),String.valueOf(vehicle.getCapacity()),
                vehicle.getFuelType().toString(),vehicle.getVehicleType().toString());
        Parent content = null;
        try {
            content = FXMLLoader.load(getClass().getResource("../updateWindow.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage newStage = madePopUp(content);
        UpdateWindowController.setStage(newStage);
    }

    private Stage madePopUp(Parent content) {
        Scene scene = new Scene(content);
        Stage popUp = new Stage();
        popUp.setMinHeight(50);
        popUp.setMinWidth(50);
        popUp.setScene(scene);
        popUp.setTitle("Life of vehicles");
        popUp.show();
        return popUp;
    }

}
