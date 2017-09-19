package sample;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortException;

public class PortReaderPackage extends PortReader {

    private final StringProperty textIn = new SimpleStringProperty("");
    private byte[] packetBytes;
    private Packet packet;
    private Integer address;

    public void setAddress(Integer addr) {
        this.address = addr;
    }

    @Override
    public final StringProperty getTextIn() {
        return textIn;
    }

    public Packet getPacket() {
        return packet;
    }

    public void serialEvent(SerialPortEvent event) {
        if (event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    packetBytes = serialPort.readString(event.getEventValue()).getBytes();
                    packet = decodePacket(packetBytes);
                    if(address == packet.getDestinationAddress()) {
                        textIn.set(textIn.get()+packet.getData());
                    }
                    else {
                        serialPort.writeBytes(packetBytes);
                    }
                } catch (SerialPortException ex) {
                    System.out.println(ex);
                }
        }
    }

    private Packet decodePacket(byte[] inPacket) {

        Packet packet = new Packet();
        packet.setDestinationAddress((int) inPacket[1]);
        packet.setSourceAddress((int) inPacket[2]);
        byte[] inData = new byte[inPacket[3]];
        System.arraycopy(inPacket, 4,inData,0,inPacket[3]);
        packet.setData(new String(inData));
        return packet;
    }
}
