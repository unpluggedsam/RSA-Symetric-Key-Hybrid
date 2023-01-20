public class Packet {

    private final int[] binary;
    private final int[] key;
    private final OneTimePad.KEY_SIZE size;

    public Packet(int[] binary, int[]
            key, OneTimePad.KEY_SIZE size) {
        this.binary = binary;
        this.key = key;
        this.size = size;
    }

    public int[] interceptBinary() {
        return binary;
    }

    public int[] interceptKey() {
        return key;
    }

    public OneTimePad.KEY_SIZE interceptKeySize() {
        return size;
    }
}
