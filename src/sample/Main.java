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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Main extends Application {

    Controller controller = new Controller();
    AnchorPane workingArea = controller.getWorkingArea();
    GridPane controlArea = controller.getControlArea();
    Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception{

        BorderPane root = new BorderPane();
        BorderPane.setMargin(controlArea, new Insets(10, 10, 10, 10));
        //root.setPadding(new Insets(25, 25, 25, 25));

        initControlArea();
        GridPane.setVgrow(controlArea, Priority.ALWAYS);
        root.setLeft(controlArea);

        initWorkingArea();
        root.setCenter(workingArea);

        scene = new Scene(root, 500, 300);
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
        TextArea textIn = controller.getTextIn();
        textIn.setWrapText(true);
        AnchorPane.setTopAnchor(textIn, 5.0);
        AnchorPane.setBottomAnchor(textIn, 130.0);
        AnchorPane.setRightAnchor(textIn, 10.0);
        AnchorPane.setLeftAnchor(textIn, 0.0);
        Hyperlink clearTextIn = controller.getClearTextInLink();
        AnchorPane.setRightAnchor(clearTextIn, 12.0);
        AnchorPane.setTopAnchor(clearTextIn, 7.0);

        Label daLabel = new Label("To address:");
        AnchorPane.setBottomAnchor(daLabel, 106.0);
        AnchorPane.setRightAnchor(daLabel, 10.0);
        AnchorPane.setLeftAnchor(daLabel, 0.0);
        TextField daField = controller.getDestAddressField();
        AnchorPane.setBottomAnchor(daField, 100.0);
        AnchorPane.setRightAnchor(daField, 10.0);
        AnchorPane.setLeftAnchor(daField, 80.0);

        TextField textOut = controller.getTextOut();
        AnchorPane.setBottomAnchor(textOut, 70.0);
        AnchorPane.setRightAnchor(textOut, 10.0);
        AnchorPane.setLeftAnchor(textOut, 0.0);
        ChoiceBox sentMessages = controller.getSentMessageChoice();
        AnchorPane.setBottomAnchor(sentMessages, 40.0);
        AnchorPane.setLeftAnchor(sentMessages, 0.0);
        AnchorPane.setRightAnchor(sentMessages, 10.0);
        HBox buttonsBox = new HBox();
        Button sendButton = controller.getSendButton();
        buttonsBox.getChildren().addAll(sendButton);
        AnchorPane.setBottomAnchor(buttonsBox, 10.0);
        workingArea.getChildren().addAll(textIn, clearTextIn, daLabel, daField, textOut, sentMessages, buttonsBox);
    }

    public void initControlArea(){
        Insets labelInsets = new Insets(0,10,5,0);
        Insets choicesInsets = new Insets(0,0,5,0);

        Label addressLabel = new Label("Address:");
        TextField addressField = controller.getAddressField();
        controlArea.add(addressLabel, 0,0);
        controlArea.setMargin(addressLabel, labelInsets);
        controlArea.add(addressField, 1, 0);
        controlArea.setMargin(addressField, choicesInsets);

        Label baudrateLabel = new Label("Baudrate:");
        ChoiceBox baudrateChoice = controller.getBaudrateChoice();
        baudrateChoice.getSelectionModel().select(5);
        controlArea.add(baudrateLabel, 0,1);
        controlArea.setMargin(baudrateLabel, labelInsets);
        controlArea.add(baudrateChoice, 1, 1);
        controlArea.setMargin(baudrateChoice, choicesInsets);

        Label databitsLabel = new Label("Databits:");
        ChoiceBox databitsChoice = controller.getDatabitsChoice();
        databitsChoice.getSelectionModel().select(0);
        controlArea.add(databitsLabel, 0, 2);
        controlArea.setMargin(databitsLabel, labelInsets);
        controlArea.add(databitsChoice, 1, 2);
        controlArea.setMargin(databitsChoice, choicesInsets);

        Label stopbitsLabel = new Label("Stopbits:");
        ChoiceBox stopbitsChoice = controller.getStopbitsChoice();
        stopbitsChoice.getSelectionModel().select(0);
        controlArea.add(stopbitsLabel, 0, 3);
        controlArea.setMargin(stopbitsLabel, labelInsets);
        controlArea.add(stopbitsChoice, 1, 3);
        controlArea.setMargin(stopbitsChoice, choicesInsets);

        Label parityLabel = new Label("Parity:");
        ChoiceBox parityChoice = controller.getParityChoice();
        parityChoice.getSelectionModel().select(0);
        controlArea.add(parityLabel, 0, 4);
        controlArea.setMargin(parityLabel, labelInsets);
        controlArea.add(parityChoice, 1, 4);
        controlArea.setMargin(parityChoice, choicesInsets);

        Label portLabel = new Label("Port:");
        ChoiceBox portChoice = controller.getPortChoice();
        portChoice.getSelectionModel().select(0);
        controlArea.add(portLabel, 0, 5);
        controlArea.setMargin(portLabel, labelInsets);
        controlArea.add(portChoice, 1, 5);
        Hyperlink refresh = controller.getRefreshPortChoiceLink();
        controlArea.add(refresh, 2, 5);
        controlArea.setMargin(refresh, choicesInsets);
        controlArea.setMargin(portChoice, choicesInsets);

        Button connectButton = controller.getConnectButton();
        connectButton.setDefaultButton(true);
        controlArea.add(connectButton, 1,6);
    }
}
