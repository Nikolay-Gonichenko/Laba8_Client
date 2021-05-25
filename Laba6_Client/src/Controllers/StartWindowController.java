package Controllers;

import Client.ClientMain;
import Client.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.ResourceBundle;


public class StartWindowController {
    private static Stage stage;
    private static Locale locale = new Locale("en", "US");


    public static void setStage(Stage stage) {
        StartWindowController.stage = stage;
    }

    ObservableList<String> languages = FXCollections.observableArrayList("Russian", "Portuguese", "French", "English");
    @FXML
    private PasswordField passwordTextField;

    @FXML
    private TextField nicknameTextField;

    @FXML
    private ChoiceBox<String> ChangeLanguageChoiceBox;

    @FXML
    private Button entrance;

    @FXML
    private Label nickLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private Label languagesLabel;

    @FXML
    private Label headerLabel;

    @FXML
    void entrance(ActionEvent event) throws IOException {

        String username = nicknameTextField.getText();
        String password = passwordTextField.getText();
        User user = new User(username, password);
        if (!username.equals("") && !password.equals("")) {
            String answer = ClientMain.init(user);
            if (answer.equals("User entered a wrong password")) {
                showWindow("You entered a wrong password. Try again");
            } else {

                setLocale();

                Parent content = FXMLLoader.load(getClass().getResource("../console.fxml"));
                Scene scene = new Scene(content);
                Stage consoleStage = new Stage();
                consoleStage.setMinHeight(50);
                consoleStage.setMinWidth(50);
                consoleStage.setScene(scene);
                stage.close();
                consoleStage.setTitle("Life of vehicles");
                ConsoleWindowController.setStage(consoleStage);
                consoleStage.show();
                showWindow(answer);
            }
        } else {
            Label lbl = new Label("You didn't enter the nickname and/or password");
            lbl.setPrefWidth(300);
            Button btn = new Button("Ok");
            btn.setPrefWidth(80);

            VBox root = new VBox(lbl, btn);
            root.setAlignment(Pos.TOP_CENTER);
            root.setSpacing(50);
            Scene scene = new Scene(root);
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.setTitle("Life of vehicles");
            newStage.setWidth(400);
            newStage.setHeight(400);
            btn.setOnAction(event1 -> newStage.close());
            newStage.show();

        }

    }

    private void showWindow(String label) {
        Button btn = new Button("Ok");
        Label lbl = new Label(label);
        VBox root = new VBox(lbl, btn);
        root.setSpacing(50);
        root.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(root, 400, 400);
        Stage stage1 = new Stage();
        stage1.setScene(scene);
        btn.setOnAction(event -> stage1.close());
        stage1.show();
    }

    private void setLocale() {
        ConsoleWindowController.setLocale(locale);
        TableWindowController.setLocale(locale);
        VisualizationWindowController.setLocale(locale);
        AddWindowController.setLocale(locale);
        UpdateWindowController.setLocale(locale);
        RemoveFuelTypeWindowController.setLocale(locale);
        RemoveIdWindowController.setLocale(locale);
    }

    @FXML
    void initialize() {
        ChangeLanguageChoiceBox.setValue("English");
        ChangeLanguageChoiceBox.setItems(languages);

        ChangeLanguageChoiceBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String language = ChangeLanguageChoiceBox.getValue();
                if (language.equals("Russian")) locale = new Locale("ru", "RU");
                if (language.equals("Portuguese")) locale = new Locale("pt", "PT");
                if (language.equals("French")) locale = new Locale("fr", "FR");
                if (language.equals("English")) locale = new Locale("en", "US");

                ResourceBundle rb = ResourceBundle.getBundle("text", locale);

                String buttonText = rb.getString("entrance.button");
                buttonText = new String(buttonText.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

                String nick = rb.getString("nickname.label");
                nick = new String(nick.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

                String password = rb.getString("password.label");
                password = new String(password.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

                String langs = rb.getString("languages.label");
                langs = new String(langs.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

                String header = rb.getString("login.label");
                header = new String(header.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

                entrance.setText(buttonText);
                nickLabel.setText(nick);
                passwordLabel.setText(password);
                languagesLabel.setText(langs);
                headerLabel.setText(header);
            }
        });

    }

}
