import java.math.BigInteger;

public class Packet {

    private final int[] binary;
    private final BigInteger key;
    private final OneTimePad.KEY_SIZE size;

    public Packet(int[] binary, BigInteger key, OneTimePad.KEY_SIZE size) {
        this.binary = binary;
        this.key = key;
        this.size = size;
    }

    public int[] getBinary() {
        return binary;
    }

    public BigInteger getKey() {
        return key;
    }

    public OneTimePad.KEY_SIZE getKeySize() {
        return size;
    }
}
