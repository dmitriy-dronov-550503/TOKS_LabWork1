package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortException;


public class PortReader implements PortReaderInterface {

    protected static SerialPort serialPort;

    public void setSerialPort(SerialPort sp) {
        serialPort = sp;
    }

    private final StringProperty textIn = new SimpleStringProperty("");

    public StringProperty getTextIn() {
        return textIn;
    }

    public void serialEvent(SerialPortEvent event) {
        if (event.isRXCHAR() && event.getEventValue() > 0) {
            try {
                textIn.set(textIn.get()+serialPort.readString(event.getEventValue())+"11");
            } catch (SerialPortException ex) {
                ex.printStackTrace();
            }
        }
    }
}
