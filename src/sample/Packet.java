package sample;

import com.sun.tools.javac.util.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Packet {

    private byte destinationAddress;
    private byte sourceAddress;
    private byte length;
    private byte[] data;
    private byte flag = 0x7E;
    private byte trailer = 0x7D;

    public Packet() {
    }

    public void setDestinationAddress(Integer destAddr) {
        this.destinationAddress = destAddr.byteValue();
    }

    public void setSourceAddress(Integer sourceAddr) {
        this.sourceAddress = sourceAddr.byteValue();
    }

    public void setData(String inData) {
        data = inData.getBytes();
        length = (byte) data.length;
    }

    public Integer getDestinationAddress() {
        return (int) destinationAddress;
    }

    public Integer getSourceAddress() {
        return (int) sourceAddress;
    }

    public String getData() {
        return new String(data);
    }

    public byte[] getPacket() {
        byte[] packet = new byte[data.length+5];
        packet[0] = flag;
        packet[1] = destinationAddress;
        packet[2] = sourceAddress;
        packet[3] = length;
        System.arraycopy(data, 0, packet, 4, data.length);
        packet[packet.length-1] = trailer;
        return packet;
    }
}
