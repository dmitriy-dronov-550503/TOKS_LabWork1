package controllers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import sample.SerialConnect;
import sample.SerialPackageConnect;

import javax.xml.soap.Text;
import java.util.*;

public class Controller {

    private static SerialPackageConnect serialConnect = new SerialPackageConnect();
    private static TextField textOut = new TextField();
    private TextArea textIn = new TextArea();
    private TextField addressField = new TextField();
    private TextField destAddressField = new TextField();

    private String baudrates[] = {"110", "300", "600", "1200", "4800",
            "9600", "19200", "38400", "57600", "115200", "128000", "256000"};
    private ChoiceBox baudrateChoice;
    private String databits[] = {"8", "7", "6", "5"};
    private ChoiceBox databitsChoice;
    private Map<String, Integer> stopbits = new LinkedHashMap<String, Integer>();
    private ChoiceBox stopbitsChoice;
    private Map<String, Integer> parity = new LinkedHashMap<String, Integer>();
    private ChoiceBox parityChoice;
    private String ports[] = {"/dev/ttys001", "/dev/ttys002"}; // /dev/tty.SLAB_USBtoUART
    private ChoiceBox portChoice;
    private LinkedList<String> sentMessageList = new LinkedList<>();
    private ChoiceBox sentMessageChoice;
    private Hyperlink clearTextInLink = new Hyperlink("✖");
    private Hyperlink refreshPortChoiceLink = new Hyperlink("↺");
    private Button connectButton;
    private Button sendButton;
    private AnchorPane workingArea = new AnchorPane();
    private GridPane controlArea = new GridPane();

    public TextField getTextOut() {
        return textOut;
    }

    public TextArea getTextIn() {
        textIn.textProperty().bindBidirectional(serialConnect.getTextIn());
        return textIn;
    }

    public TextField getAddressField() {
        return addressField;
    }

    public TextField getDestAddressField() {
        return destAddressField;
    }

    public Hyperlink getClearTextInLink() {
        clearTextInLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                textIn.clear();
            }
        });
        clearTextInLink.setStyle("-fx-underline: false;");
        return clearTextInLink;
    }

    public Hyperlink getRefreshPortChoiceLink() {
        refreshPortChoiceLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addPortsToPortChoice();
                portChoice.getSelectionModel().select(0);
            }
        });
        refreshPortChoiceLink.setStyle("-fx-underline: false;");
        return refreshPortChoiceLink;
    }

    public ChoiceBox getBaudrateChoice() {
        baudrateChoice = new ChoiceBox(FXCollections.observableArrayList(baudrates));
        return baudrateChoice;
    }

    public ChoiceBox getDatabitsChoice() {
        databitsChoice = new ChoiceBox(FXCollections.observableArrayList(databits));
        return databitsChoice;
    }

    public ChoiceBox getStopbitsChoice() {
        stopbits.put("1", 1);
        stopbits.put("2", 2);
        stopbits.put("1.5", 3);
        stopbitsChoice = new ChoiceBox(FXCollections.observableArrayList(stopbits.keySet()));
        return stopbitsChoice;
    }

    public ChoiceBox getParityChoice() {
        parity.put("NONE", 0);
        parity.put("ODD", 1);
        parity.put("EVEN", 2);
        parity.put("MARK", 3);
        parity.put("SPACE", 4);
        parityChoice = new ChoiceBox(FXCollections.observableArrayList(parity.keySet()));
        return parityChoice;
    }

    public ChoiceBox getPortChoice() {
        portChoice = new ChoiceBox();
        addPortsToPortChoice();
        return portChoice;
    }

    private void addPortsToPortChoice() {
        ObservableList portList = FXCollections.observableArrayList(ports);
        portList.addAll(serialConnect.getPortNames());
        portChoice.setItems(portList);
    }

    public ChoiceBox getSentMessageChoice() {
        sentMessageChoice = new ChoiceBox(FXCollections.observableArrayList());
        sentMessageChoice.setOnAction(t -> chooseSentMessage());
        return sentMessageChoice;
    }

    private void chooseSentMessage() {
        if (sentMessageChoice.isShowing()) {
            textOut.setText(sentMessageChoice.getValue().toString());
        }
    }

    public Button getConnectButton() {
        connectButton = new Button("Connect");
        connectButton.setOnAction(t -> connectButtonAction());
        return connectButton;
    }

    private void connectButtonAction() {
        if (serialConnect.isPortConnected()) {
            disconnectSerialPort();
        } else {
            try {
                connectSerialPort();
                serialConnect.setAddress(Integer.parseInt(addressField.getText()));
            } catch (Exception ex) {
                alert(ex.getMessage());
            }
        }
    }

    private void connectSerialPort() {
        serialConnect.open(portChoice.getValue().toString(),
                Integer.parseInt(baudrateChoice.getValue().toString()),
                Integer.parseInt(databitsChoice.getValue().toString()),
                stopbits.get(stopbitsChoice.getValue()),
                parity.get(parityChoice.getValue()));
        sendButton.setDefaultButton(true);
        connectButton.setDefaultButton(false);
        connectButton.setText("Disconnect");
    }

    public void disconnectSerialPort() {
        if (serialConnect.isPortConnected()) {
            serialConnect.close();
            sendButton.setDefaultButton(false);
            connectButton.setDefaultButton(true);
            connectButton.setText("Connect");
        }
    }

    public Button getSendButton() {
        sendButton = new Button("Send");
        sendButton.setOnAction(t -> sendMessage());
        return sendButton;
    }

    private void sendMessage() {
        if (textOut.getText().isEmpty()) return;
        try {
            serialConnect.writeString(textOut.getText(), Integer.parseInt(destAddressField.getText()));
            sentMessageList.addFirst(textOut.getText());
            sentMessageChoice.getItems().clear();
            sentMessageChoice.getItems().addAll(sentMessageList);
            textOut.clear();
        } catch (Exception ex) {
            ex.printStackTrace();
            alert("Port not connected");
        }
    }

    public AnchorPane getWorkingArea() {
        return workingArea;
    }

    public GridPane getControlArea() {
        return controlArea;
    }

    public static void alert(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
}
