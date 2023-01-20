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

    public int[] getBinary() {
        return binary;
    }

    public int[] getKey() {
        return key;
    }

    public OneTimePad.KEY_SIZE getKeySize() {
        return size;
    }
}
