import java.math.BigInteger;

public class Packet {

    private final int[] binary;
    private final BigInteger key;
    private final BigInteger size;

    public Packet(int[] binary, BigInteger key, BigInteger size) {
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

    public BigInteger getKeySize() {
        return size;
    }
}
