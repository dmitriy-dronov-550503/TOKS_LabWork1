package sample;


import javafx.scene.control.TextField;
import jssc.*;

import javafx.scene.control.TextArea;

import javax.sql.rowset.serial.SerialArray;

public class SerialConnect {

    private static SerialPort serialPort;
    private PortReader portReader = new PortReader();
    private boolean isOpened = false;

    public boolean isPortConnected() {
        return isOpened;
    }

    public SerialPort getSerialPort() {
        return serialPort;
    }

    public TextArea getTextIn() {
        return portReader.getTextIn();
    }

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

    private static class PortReader implements SerialPortEventListener {
        private static TextArea textIn = new TextArea();

        public TextArea getTextIn() {
            return textIn;
        }

        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    textIn.appendText(serialPort.readString(event.getEventValue()));
                } catch (SerialPortException ex) {
                    System.out.println(ex);
                }
            }
        }
    }

    public String[] getPortNames(){
        return SerialPortList.getPortNames();
    }

    protected void finalize() {
        close();
    }

}
