package sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Packet {

    private Byte flag;
    private Byte destinationAdress;
    private Byte sourceAdress;
    private Byte length;

    private Byte[] data;

    private Byte trailer;

    public Packet(Byte flag, Byte destinationAdress, Byte sourceAdress, Byte[] data, Byte trailer) {
        this.flag = flag;
        this.destinationAdress = destinationAdress;
        this.sourceAdress = sourceAdress;
        this.data = data;
        this.trailer = trailer;
        this.length = new Integer(data.length).byteValue();
    }

    public Byte[] getPacket() {
        List<Byte> packet = new ArrayList<>();
        packet.add(flag);
        packet.add(destinationAdress);
        packet.add(sourceAdress);
        packet.add(length);
        packet.addAll(Arrays.asList(data));
        packet.add(trailer);

        return (Byte[])packet.toArray();
    }
}
