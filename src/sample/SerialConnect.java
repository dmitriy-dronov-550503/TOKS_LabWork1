package sample;


import controllers.Controller;
import javafx.beans.property.StringProperty;
import jssc.*;

import java.util.concurrent.TimeUnit;

public class SerialConnect {

    protected static SerialPort serialPort;
    protected static PortReader portReader = new PortReader();
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
            portReader.setSerialPort(serialPort);
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

    protected void finalize() {
        close();
    }

}
