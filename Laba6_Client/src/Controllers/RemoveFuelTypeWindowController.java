package Controllers;

import Client.ClientMain;
import data.FuelType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.ResourceBundle;

public class RemoveFuelTypeWindowController {
    private static Stage stage;
    ObservableList<String> fuelTypes = FXCollections.observableArrayList("KEROSENE","NUCLEAR","PLASMA");
    public static void setStage(Stage stage) {
        RemoveFuelTypeWindowController.stage = stage;
    }

    private static Locale locale;
    private ResourceBundle rb;
    public static void setLocale(Locale locale) {
        RemoveFuelTypeWindowController.locale = locale;
    }

    @FXML
    private Label enterFuelLabel;

    @FXML
    private Button removeButton;

    @FXML
    private ChoiceBox<String> FuelTypeChoiceBox;

    @FXML
    void remove(ActionEvent event) {
        String fueltype = FuelTypeChoiceBox.getValue();
        FuelType fuelType = FuelType.valueOf(fueltype);

        String answer = ClientMain.removeFuelType(fuelType);
        showWindow(answer);

    }

    @FXML
    void initialize(){
        FuelTypeChoiceBox.setItems(fuelTypes);
        FuelTypeChoiceBox.setValue("PLASMA");

        initLocale();
    }
    private void showWindow(String label){

        Button btn = new Button("Ok");
        Label lbl = new Label(label);
        VBox root = new VBox(lbl,btn);
        root.setSpacing(50);
        root.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(root, 400, 400);
        Stage stage1 = new Stage();
        stage1.setScene(scene);
        btn.setOnAction(kevent -> stage1.close());
        stage.close();
        stage1.show();
    }
    private void initLocale() {
        rb = ResourceBundle.getBundle("text",locale);

        enterFuelLabel.setText(setString("enterFuel.label"));
        removeButton.setText(setString("remove.button"));

    }
    private String setString(String text){
        String stringToSet = rb.getString(text);
        stringToSet = new String(stringToSet.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        return stringToSet;
    }
}
