package sample;


import controllers.Controller;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;
import jssc.*;

import javafx.scene.control.TextArea;

import javax.sql.rowset.serial.SerialArray;
import java.util.concurrent.TimeUnit;

public class SerialConnect {

    protected static SerialPort serialPort;
    protected PortReader portReader = new PortReader();
    private boolean isOpened = false;

    public boolean isPortConnected() {
        return isOpened;
    }

    public SerialPort getSerialPort() {
        return serialPort;
    }

    public StringProperty getTextIn() { return portReader.getTextIn(); }

    public String getTextInString() { return portReader.getTextIn().get(); }

    public void open(String portName, Integer baudrate, Integer databits, Integer stopbits, Integer parity) {
        try {
            serialPort = new SerialPort(portName);
            serialPort.openPort();
            serialPort.setParams(baudrate, databits, stopbits, parity);
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
                    SerialPort.FLOWCONTROL_RTSCTS_OUT);
            serialPort.addEventListener(portReader, SerialPort.MASK_RXCHAR);
            isOpened = true;
        } catch (SerialPortException ex) {
            System.out.println(ex);
            throw new RuntimeException("Can't open port");
        }
    }

    public void close() {
        if (isOpened) {
            try {
                serialPort.closePort();
                isOpened = false;
            } catch (Exception ex) {
                System.out.println(ex);
                throw new RuntimeException("Can't close port");
            }
        }
    }

    public void writeString(String message) {
        try {
            serialPort.setRTS(true);
            serialPort.writeBytes(message.getBytes());
            TimeUnit.MILLISECONDS.sleep(100);
            serialPort.setRTS(false);
            serialPort.sendBreak(1);
        } catch (InterruptedException ex) {
            Controller.alert("TimeUnit exception\n" + ex.getMessage());
        } catch (SerialPortException ex) {
            Controller.alert("Port exception\n" + ex.getMessage());
        }
    }

    public String[] getPortNames() {
        return SerialPortList.getPortNames();
    }

    private static class PortReader implements SerialPortEventListener {

        private final StringProperty textIn = new SimpleStringProperty("");

        public final StringProperty getTextIn() {
            return textIn;
        }

        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    textIn.set(textIn.get()+serialPort.readString(event.getEventValue()));
                } catch (SerialPortException ex) {
                    System.out.println(ex);
                }
            }
        }
    }

    protected void finalize() {
        close();
    }

}
