public class Packet {

    private final int[] binary;
    private final int[] key;

    public Packet(int[] binary, int[]
            key) {
        this.binary = binary;
        this.key = key;
    }

    public int[] interceptBinary() {
        return binary;
    }

    public int[] interceptKey() {
        return key;
    }
}
