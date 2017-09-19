package sample;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class SerialPackageConnect extends SerialConnect {

    public void sendPacket() {
        
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
}
