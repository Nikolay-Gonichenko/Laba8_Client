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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.ResourceBundle;

public class AddWindowController {

    private static Stage stage;
    private static boolean check2;
    private static Locale locale;
    private ResourceBundle rb;
    public static void setLocale(Locale locale) {
        AddWindowController.locale = locale;
    }

    public static void setStage(Stage stage,boolean check) {
        AddWindowController.stage = stage;
        AddWindowController.check2 = check;
    }

    ObservableList<String> vehicleTypes = FXCollections.observableArrayList("HELICOPTER","SUBMARINE","CHOPPER",
            "HOVERBOARD","SPACESHIP");
    ObservableList<String> fuelTypes = FXCollections.observableArrayList("KEROSENE","NUCLEAR","PLASMA");
    @FXML
    private Label enterLabel;
    @FXML
    private TextField x_coordinateTextField;

    @FXML
    private ChoiceBox<String> VehicleTypeChoiceBox;

    @FXML
    private TextField nameTextField;

    @FXML
    private ChoiceBox<String> FuelTypeChoiceBox;

    @FXML
    private TextField y_coordinateTextField;

    @FXML
    private Button done;

    @FXML
    private TextField engine_powerTextField;

    @FXML
    private TextField capacityTextField;

    @FXML
    void getVehicle(ActionEvent event) throws IOException {
        Parent content = null;
        int check = 0;
        try{
            String name = nameTextField.getText();
            String x_check = x_coordinateTextField.getText();
            String y_check = y_coordinateTextField.getText();
            String engine_Power_check = engine_powerTextField.getText();
            String capacity_check = capacityTextField.getText();
            String fuelType_check = FuelTypeChoiceBox.getValue();
            String vehicleType_check = VehicleTypeChoiceBox.getValue();


            double x = Double.parseDouble(x_check);
            if (x<-898) throw new NumberFormatException();
            double y = Double.parseDouble(y_check);
            float enginePower = Float.parseFloat(engine_Power_check);
            if (enginePower<=0) throw new NumberFormatException();
            int capacity = Integer.parseInt(capacity_check);
            if (capacity<=0) throw new NumberFormatException();
            FuelType fuelType = FuelType.valueOf(fuelType_check);
            VehicleType vehicleType = VehicleType.valueOf(vehicleType_check);

            Vehicle vehicle = new Vehicle(name,new Coordinates((float) x,y),enginePower,capacity,vehicleType,fuelType);
            if (check2) {
                ClientMain.add(vehicle);
            } else {
                ClientMain.addMax(vehicle);
            }


            content = FXMLLoader.load(getClass().getResource("../commandIsDone.fxml"));
        }catch (NumberFormatException e){
            content = FXMLLoader.load(getClass().getResource("../commandIsNotDone.fxml"));
            check++;
        }finally {
            Scene scene = new Scene(content);
            Stage newStage = new Stage();
            newStage.setMinHeight(50);
            newStage.setMinWidth(50);
            newStage.setScene(scene);
            stage.close();
            newStage.setTitle("Life of vehicles");
            if (check==0){
                CommandIsDoneWindowController.setStage(newStage);
            }else{
                CommandIsNotDoneWindowController.setStage(newStage);
            }
            newStage.show();
        }
    }
    @FXML
    void initialize(){
        VehicleTypeChoiceBox.setValue("CHOPPER");
        VehicleTypeChoiceBox.setItems(vehicleTypes);

        FuelTypeChoiceBox.setItems(fuelTypes);
        FuelTypeChoiceBox.setValue("PLASMA");

        initLocale();
    }

    private void initLocale() {
        rb = ResourceBundle.getBundle("text",locale);

        done.setText(setString("enter.button"));
        enterLabel.setText(setString("enter.label"));

    }
    private String setString(String text){
        String stringToSet = rb.getString(text);
        stringToSet = new String(stringToSet.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        return stringToSet;
    }
}
