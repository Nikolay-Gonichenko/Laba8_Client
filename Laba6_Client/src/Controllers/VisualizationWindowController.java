package Controllers;

import Client.ClientMain;
import data.Coordinates;
import data.FuelType;
import data.Vehicle;
import data.VehicleType;
import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class VisualizationWindowController {
    private static Stage stage;
    private static Locale locale;
    private ResourceBundle rb;

    public static void setLocale(Locale locale) {
        VisualizationWindowController.locale = locale;
    }

    public static void setStage(Stage stage) {
        VisualizationWindowController.stage = stage;
    }

    @FXML
    private GridPane paneForVehicles;

    @FXML
    private Label visLabel;

    @FXML
    private Button goToConsoleButton;

    @FXML
    private Button goToTableButton;

    @FXML
    void goToConsole(ActionEvent event) throws IOException {
        Parent content = FXMLLoader.load(getClass().getResource("../console.fxml"));
        Stage stage = changeWindow(content);
        ConsoleWindowController.setStage(stage);
    }

    @FXML
    void goToTable(ActionEvent event) throws IOException {
        Parent content = FXMLLoader.load(getClass().getResource("../table.fxml"));
        Stage stage = changeWindow(content);
        TableWindowController.setStage(stage);
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
        initLocale();
        initVehicles();
    }

    private void initVehicles() {
        paneForVehicles.getChildren().clear();

        String collectionInString = ClientMain.getCollection();
        String[] collectionArray = collectionInString.split(",");
        List<Vehicle> vehicles = new ArrayList<>();
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
            String user = collectionArray[i + 9];
            Vehicle vehicle = new Vehicle(id, name, new Coordinates(x, y), date, enginePower, capacity, vehicleType, fuelType);
            vehicle.setUser(user);
            vehicles.add(vehicle);
        }
        List<ViewVehicle> viewVehicles = getPositions(vehicles);

        for (int i = 0; i < viewVehicles.size(); i++) {
            Rectangle rectangle = new Rectangle();
            rectangle.setX(20);
            rectangle.setY(10);
            rectangle.setWidth(100);
            rectangle.setHeight(50);
            int finalI = i;

            rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Vehicle vehicle = viewVehicles.get(finalI).getVehicle();
                    Parent content = null;
                    UpdateWindowController.setAllInit(Integer.toString(vehicle.getId()), vehicle.getName(), Float.toString(vehicle.getX()),
                            Double.toString(vehicle.getY()), Integer.toString(vehicle.getCapacity()), Float.toString(vehicle.getEnginePower()),
                            vehicle.getFuelType().toString(), vehicle.getVehicleType().toString());
                    try {
                        content = FXMLLoader.load(getClass().getResource("../updateWindow.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Stage newStage = madePopUp(content);
                    UpdateWindowController.setStage(newStage);
                }
            });

            String hex = convertStringToHex(viewVehicles.get(i).getVehicle().getUser());
            //System.out.println(viewVehicles.get(i).getVehicle().getUser());
            hex = (hex.length() > 6 ? hex.substring(0, 6) : hex);
            while (hex.length() < 6) {
                hex += "0";
            }

            Color color = Color.web("#" + hex);
            rectangle.setFill(color);
            Text text = new Text(viewVehicles.get(i).getVehicle().getName());
            text.setFill(Color.WHITE);

            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(1000));
            scaleTransition.setNode(rectangle);
            scaleTransition.setCycleCount(Animation.INDEFINITE);
            scaleTransition.setByX(0.25);
            scaleTransition.setByY(0.25);
            scaleTransition.setAutoReverse(true);
            scaleTransition.play();
            StackPane pane = new StackPane();
            pane.getChildren().addAll(rectangle, text);
            paneForVehicles.add(pane, viewVehicles.get(i).getX(), viewVehicles.get(i).getY());
        }

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

    private List<ViewVehicle> getPositions(List<Vehicle> vehicles) {
        List<Vehicle> sortX = vehicles.stream().sorted((v1, v2) -> Float.compare(v1.getX(), v2.getX())).collect(Collectors.toList());
        List<Vehicle> sortY = vehicles.stream().sorted((v1, v2) -> Double.compare(v1.getY(), v2.getY())).collect(Collectors.toList());
        List<ViewVehicle> view = new ArrayList<>();
        for (int i = 0; i < vehicles.size(); i++) {
            Vehicle vehicle = vehicles.get(i);
            view.add(new ViewVehicle(find(sortX, vehicle), find(sortY, vehicle), vehicle));
        }
        return view;
    }

    private int find(List<Vehicle> vehicles, Vehicle vehicle) {
        for (int i = 0; i < vehicles.size(); i++) {
            if (vehicles.get(i).equals(vehicle)) return i;
        }
        return 0;
    }

    public static String convertStringToHex(String str) {
        StringBuffer hex = new StringBuffer();
        for (char temp : str.toCharArray()) {
            int decimal = (int) temp;
            hex.append(Integer.toHexString(decimal));
        }

        return hex.toString();

    }

    private void initLocale() {
        rb = ResourceBundle.getBundle("text", locale);

        goToConsoleButton.setText(setString("goToConsole.button"));
        goToTableButton.setText(setString("goToTable.button"));
        visLabel.setText(setString("vis.label"));

    }

    private String setString(String text) {
        String stringToSet = rb.getString(text);
        stringToSet = new String(stringToSet.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        return stringToSet;
    }

    class ViewVehicle {
        int x;
        int y;
        Vehicle vehicle;

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public ViewVehicle(int x, int y, Vehicle vehicle) {
            this.x = x;
            this.y = y;
            this.vehicle = vehicle;
        }

        public Vehicle getVehicle() {
            return vehicle;
        }
    }
}

