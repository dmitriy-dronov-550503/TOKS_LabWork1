package sample;


import com.sun.tools.javac.util.ArrayUtils;
import controllers.Controller;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.transformation.TransformationList;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class SerialPackageConnect extends SerialConnect {

    private Integer address;
    private PortReaderPackage portReaderPackage = new PortReaderPackage();

    public SerialPackageConnect() {
        portReader = portReaderPackage;
    }

    public void setAddress(Integer addr) {
        address = addr;
        portReaderPackage.setAddress(address);
    }

    @Override
    public StringProperty getTextIn() {
        return portReader.getTextIn();
    }

    public void sendPacket(Packet packet) {
        try {
            //serialPort.setRTS(true);
            serialPort.writeBytes(packet.getPacket());
            TimeUnit.MILLISECONDS.sleep(100);
            //serialPort.setRTS(false);
        } catch (InterruptedException ex) {
            Controller.alert("TimeUnit exception\n" + ex.getMessage());
        } catch (SerialPortException ex) {
            Controller.alert("Port exception\n" + ex.getMessage());
        }
    }

    public void writeString(String message, Integer destAddr) {
        Packet packet = new Packet();
        packet.setSourceAddress(address);
        packet.setDestinationAddress(destAddr);
        packet.setData(message);
        sendPacket(packet);
    }

}
