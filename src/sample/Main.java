package sample;

import controllers.Controller;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import jssc.*;
import sun.awt.PlatformFont;

import java.util.Optional;

public class Main extends Application {

    Controller controller = new Controller();
    VBox workingArea = controller.getWorkingArea();
    GridPane controlArea = controller.getControlArea();
    Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception{

        System.out.print(SerialPortList.getPortNames().length);

        BorderPane root = new BorderPane();
        BorderPane.setMargin(controlArea, new Insets(10, 10, 10, 10));
        //root.setPadding(new Insets(25, 25, 25, 25));

        initControlArea();
        GridPane.setVgrow(controlArea, Priority.ALWAYS);
        root.setLeft(controlArea);

        initWorkingArea();
        VBox.setVgrow(workingArea, Priority.ALWAYS);
        root.setCenter(workingArea);

        scene = new Scene(root, 640, 480);
        primaryStage.setTitle("Serial Port Connector");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    @Override
    public void stop(){
        controller.disconnectSerialPort();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void initWorkingArea(){
        workingArea.setMaxHeight(1000);
        TextField textOut = controller.getTextOut();
        TextArea textIn = controller.getTextIn();
        HBox buttonsBox = new HBox();
        Button sendButton = controller.getSendButton();
        buttonsBox.getChildren().addAll(sendButton);
        workingArea.getChildren().addAll(textIn, textOut, buttonsBox);
    }

    public void initControlArea(){
        Insets labelInsets = new Insets(0,10,5,0);
        Insets choicesInsets = new Insets(0,0,5,0);
        Label baudrateLabel = new Label("Baudrate:");
        ChoiceBox baudrateChoice = controller.getBaudrateChoice();
        baudrateChoice.getSelectionModel().select(5);
        controlArea.add(baudrateLabel, 0,0);
        controlArea.setMargin(baudrateLabel, labelInsets);
        controlArea.add(baudrateChoice, 1, 0);
        controlArea.setMargin(baudrateChoice, choicesInsets);

        Label databitsLabel = new Label("Databits:");
        ChoiceBox databitsChoice = controller.getDatabitsChoice();
        databitsChoice.getSelectionModel().select(0);
        controlArea.add(databitsLabel, 0, 1);
        controlArea.setMargin(databitsLabel, labelInsets);
        controlArea.add(databitsChoice, 1, 1);
        controlArea.setMargin(databitsChoice, choicesInsets);

        Label stopbitsLabel = new Label("Stopbits:");
        ChoiceBox stopbitsChoice = controller.getStopbitsChoice();
        stopbitsChoice.getSelectionModel().select(0);
        controlArea.add(stopbitsLabel, 0, 2);
        controlArea.setMargin(stopbitsLabel, labelInsets);
        controlArea.add(stopbitsChoice, 1, 2);
        controlArea.setMargin(stopbitsChoice, choicesInsets);

        Label parityLabel = new Label("Parity:");
        ChoiceBox parityChoice = controller.getParityChoice();
        parityChoice.getSelectionModel().select(0);
        controlArea.add(parityLabel, 0, 3);
        controlArea.setMargin(parityLabel, labelInsets);
        controlArea.add(parityChoice, 1, 3);
        controlArea.setMargin(parityChoice, choicesInsets);

        Label portLabel = new Label("Port:");
        ChoiceBox portChoice = controller.getPortChoice();
        portChoice.getSelectionModel().select(0);
        controlArea.add(portLabel, 0, 4);
        controlArea.setMargin(portLabel, labelInsets);
        controlArea.add(portChoice, 1, 4);
        controlArea.setMargin(portChoice, choicesInsets);

        Button connectButton = controller.getConnectButton();
        connectButton.setDefaultButton(true);
        controlArea.add(connectButton, 1,5);
    }
}
