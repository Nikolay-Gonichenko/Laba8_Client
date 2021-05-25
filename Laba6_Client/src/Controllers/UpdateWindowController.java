package Controllers;

import Client.ClientMain;
import Exceptions.NotThisVehicleInPossessionException;
import data.Coordinates;
import data.FuelType;
import data.Vehicle;
import data.VehicleType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.ResourceBundle;

public class UpdateWindowController {

    private static Stage stage;

    public static void setStage(Stage stage) {
        UpdateWindowController.stage = stage;
    }

    ObservableList<String> vehicleTypes = FXCollections.observableArrayList("HELICOPTER","SUBMARINE","CHOPPER",
            "HOVERBOARD","SPACESHIP");
    ObservableList<String> fuelTypes = FXCollections.observableArrayList("KEROSENE","NUCLEAR","PLASMA");
    private static Locale locale;
    private ResourceBundle rb;
    public static void setLocale(Locale locale) {
        UpdateWindowController.locale = locale;
    }


    private static String idInit = null;
    private static String nameInit = null;
    private static String xInit = null;
    private static String yInit = null;
    private static String capacityInit = null;
    private static String enginePowerInit = null;
    private static String fuelTypeInit = null;
    private static String vehicleTypeInit = null;
    public static void setAllInit(String id,String name,String x, String y,String capacity,String enginePower,String fuelType,String vehicleType){
        idInit = id;
        nameInit = name;
        xInit = x;
        yInit = y;
        capacityInit = capacity;
        enginePowerInit = enginePower;
        fuelTypeInit = fuelType;
        vehicleTypeInit = vehicleType;
    }
    @FXML
    private Label enterIdLabel;

    @FXML
    private TextField idTextField;

    @FXML
    private TextField yCoordinateTextField;

    @FXML
    private TextField capacityTextField;

    @FXML
    private TextField xCoordinateTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField enginePowerTextField;

    @FXML
    private ChoiceBox<String> FuelTypeChoiceBox;

    @FXML
    private ChoiceBox<String> VehicleTypeChoiceBox;

    @FXML
    private Button updateButton;


    @FXML
    void update(ActionEvent event) throws IOException {
        Parent content = null;
        int check = 0;
        try{
            String id_check = idTextField.getText();
            String name = nameTextField.getText();
            String x_check = xCoordinateTextField.getText();
            String y_check = yCoordinateTextField.getText();
            String engine_Power_check = enginePowerTextField.getText();
            String capacity_check = capacityTextField.getText();
            String fuelType_check = FuelTypeChoiceBox.getValue();
            String vehicleType_check = VehicleTypeChoiceBox.getValue();

            int id = Integer.parseInt(id_check);
            if (id<=0) throw new NumberFormatException();
            double x = Double.parseDouble(x_check);
            if (x<-898) throw new NumberFormatException();
            double y = Double.parseDouble(y_check);
            float enginePower = Float.parseFloat(engine_Power_check);
            if (enginePower<=0) throw new NumberFormatException();
            int capacity = Integer.parseInt(capacity_check);
            if (capacity<=0) throw new NumberFormatException();
            FuelType fuelType = FuelType.valueOf(fuelType_check);
            VehicleType vehicleType = VehicleType.valueOf(vehicleType_check);

            Vehicle vehicle = new Vehicle(id,name,new Coordinates((float) x,y),enginePower,capacity,vehicleType,fuelType);
            String answer = ClientMain.update(vehicle);
            if (answer.equals("You don't have such element in your possession")) throw new NotThisVehicleInPossessionException();
            content = FXMLLoader.load(getClass().getResource("../commandIsDone.fxml"));
        }catch (NumberFormatException e){
            content = FXMLLoader.load(getClass().getResource("../commandIsNotDone.fxml"));
            check++;
        } catch (NotThisVehicleInPossessionException e) {
            Button btn = new Button("Ok");
            Label lbl = new Label("You don't have such element in your possession");
            VBox root = new VBox(lbl,btn);
            root.setSpacing(50);
            root.setAlignment(Pos.TOP_CENTER);

            Scene scene = new Scene(root, 400, 400);
            Stage stage1 = new Stage();
            stage1.setScene(scene);
            btn.setOnAction(kevent -> stage1.close());

            setAllInitNull();

            stage1.show();
            check = 2;
        } finally {
            if (check!=2){
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
    }

    private void setAllInitNull() {
        idInit = null;
        xInit = null;
        yInit = null;
        capacityInit = null;
        fuelTypeInit = null;
        vehicleTypeInit = null;
        enginePowerInit = null;
        nameInit = null;
    }

    @FXML
    void initialize(){
        VehicleTypeChoiceBox.setItems(vehicleTypes);
        FuelTypeChoiceBox.setItems(fuelTypes);

        if (nameInit==null){
            VehicleTypeChoiceBox.setValue("CHOPPER");
            FuelTypeChoiceBox.setValue("PLASMA");
            idTextField.setEditable(true);
        }else{
            VehicleTypeChoiceBox.setValue(vehicleTypeInit);
            FuelTypeChoiceBox.setValue(fuelTypeInit);
            idTextField.setText(idInit);
            nameTextField.setText(nameInit);
            capacityTextField.setText(capacityInit);
            xCoordinateTextField.setText(xInit);
            yCoordinateTextField.setText(yInit);
            enginePowerTextField.setText(enginePowerInit);
            idTextField.setEditable(false);
        }

        initLocale();
    }
    private void initLocale() {
        rb = ResourceBundle.getBundle("text",locale);

        enterIdLabel.setText(setString("enterId.label"));
        updateButton.setText(setString("update.button"));

    }
    private String setString(String text){
        String stringToSet = rb.getString(text);
        stringToSet = new String(stringToSet.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        return stringToSet;
    }
}
