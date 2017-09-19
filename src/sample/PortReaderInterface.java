package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public interface PortReaderInterface extends SerialPortEventListener {

    public static SerialPort serialPort = null;
    public final StringProperty textIn = new SimpleStringProperty("");

    public StringProperty getTextIn();

    public void serialEvent(SerialPortEvent event);

}