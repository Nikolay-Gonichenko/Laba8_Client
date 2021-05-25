package Controllers;

import Client.ClientMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ConsoleWindowController {

    private static Stage stage;
    private static Locale locale;
    private ResourceBundle rb;
    public static void setStage(Stage stage) {
        ConsoleWindowController.stage = stage;
    }

    public static void setLocale(Locale locale) {
        ConsoleWindowController.locale = locale;
    }

    @FXML
    private Button removeFirstCommand;

    @FXML
    private Button removeFuelTypeButton;

    @FXML
    private Button helpButton;

    @FXML
    private Button removeHeadButton;

    @FXML
    private Button addButton;

    @FXML
    private Button clearButton;

    @FXML
    private Button showButton;

    @FXML
    private Button goToVisualizationButton;

    @FXML
    private Button infoButton;

    @FXML
    private Button removeIdButton;

    @FXML
    private Button maxNameButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button addMaxButton;

    @FXML
    private Button groupCountButton;

    @FXML
    private Button goToTableButton;

    @FXML
    private Label header;

    @FXML
    void help(ActionEvent event) throws IOException {
        String message = ClientMain.help();
        showWindow(message);
    }

    @FXML
    void show(ActionEvent event) throws IOException {
        String message = ClientMain.show();
        showWindow(message);
    }

    @FXML
    void info(ActionEvent event) throws IOException {
        String message = ClientMain.info();
        showWindow(message);
    }

    @FXML
    void add(ActionEvent event) throws IOException {
        Parent content = FXMLLoader.load(getClass().getResource("../addWindow.fxml"));
        Stage newStage = madePopUp(content);
        AddWindowController.setStage(newStage,true);
    }

    @FXML
    void update(ActionEvent event) throws IOException {
        Parent content = FXMLLoader.load(getClass().getResource("../updateWindow.fxml"));
        Stage newStage = madePopUp(content);
        UpdateWindowController.setStage(newStage);
    }

    @FXML
    void clear(ActionEvent event) throws IOException {
        String label = ClientMain.clear();
        showWindow(label);
    }

    @FXML
    void removeId(ActionEvent event) throws IOException {
        Parent content = FXMLLoader.load(getClass().getResource("../removeIdWindow.fxml"));
        Stage newStage = madePopUp(content);
        RemoveIdWindowController.setStage(newStage);
    }

    @FXML
    void groupCount(ActionEvent event) throws IOException {
        String message = ClientMain.groupCount();
        showWindow(message);
    }

    @FXML
    void maxName(ActionEvent event) throws IOException {
        String message = ClientMain.maxName();
        showWindow(message);
    }

    @FXML
    void removeFirst(ActionEvent event) throws IOException {
        String message = ClientMain.removeFirst();
        showWindow(message);
    }

    @FXML
    void removeHead(ActionEvent event) throws IOException {
        removeFirst(event);
    }

    @FXML
    void removeFuelType(ActionEvent event) throws IOException {
        Parent content = FXMLLoader.load(getClass().getResource("../removeFuelType.fxml"));
        Stage newStage = madePopUp(content);
        RemoveFuelTypeWindowController.setStage(newStage);
    }

    @FXML
    void addMax(ActionEvent event) throws IOException {
        Parent content = FXMLLoader.load(getClass().getResource("../addWindow.fxml"));
        Stage newStage = madePopUp(content);
        AddWindowController.setStage(newStage,false);
    }


    @FXML
    void goToTable(ActionEvent event) throws IOException {
        Parent content = FXMLLoader.load(getClass().getResource("../table.fxml"));
        Stage stage = changeWindow(content);
        TableWindowController.setStage(stage);
    }

    @FXML
    void goToVisualization(ActionEvent event) throws IOException {
        Parent content = FXMLLoader.load(getClass().getResource("../visualization.fxml"));
        Stage newStage = changeWindow(content);
        VisualizationWindowController.setStage(newStage);
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
    private void showWindow(String label){
        Button btn = new Button("Ok");
        Label lbl = new Label(label);
        VBox root = new VBox(lbl,btn);
        root.setSpacing(50);
        root.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(root, 400, 400);
        Stage stage1 = new Stage();
        stage1.setScene(scene);
        btn.setOnAction(event -> stage1.close());
        stage1.show();
    }

    @FXML
    void initialize(){
        rb = ResourceBundle.getBundle("text",locale);

        goToTableButton.setText(setString("goToTable.button"));
        goToVisualizationButton.setText(setString("goTiVis.button"));
        header.setText(setString("commands.label"));
        addButton.setText(setString("add.button"));
        updateButton.setText(setString("update.button"));
        clearButton.setText(setString("clear.button"));
        showButton.setText(setString("show.button"));
        removeIdButton.setText(setString("removeId.button"));
        helpButton.setText(setString("help.button"));
        groupCountButton.setText(setString("groupCounting.button"));
        removeFirstCommand.setText(setString("removeFirst.button"));
        removeHeadButton.setText(setString("removeHead.button"));
        infoButton.setText(setString("info.button"));
        addMaxButton.setText(setString("addMax.button"));
        maxNameButton.setText(setString("maxName.button"));
        removeFuelTypeButton.setText(setString("removeFuelType.button"));

    }
    private String setString(String text){
        String stringToSet = rb.getString(text);
        stringToSet = new String(stringToSet.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        return stringToSet;
    }
}