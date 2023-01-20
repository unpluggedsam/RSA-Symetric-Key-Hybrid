import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        String text = "I LOVE eating some stuff occasionally and having friends.";

        Computer computer1 = new Computer();
        Computer computer2 = new Computer();

        Packet packet = computer1.sendMessage(computer2, text, OneTimePad.KEY_SIZE.ONE_HUNDRED_TWENTY_EIGHT);

        System.out.println(packet.getKeySize());
        System.out.println(OneTimePad.decryptBinary(packet.getBinary(), convertDecimalToBinary(packet.getKey(), OneTimePad.KEY_SIZE.TWO_HUNDRED_FIFTY_SIX.getSize())));
        System.out.println(computer2.getRecentMessages().get(0));

    }

    private static int[] convertDecimalToBinary(BigInteger decimal, int keySize) {
        ArrayList<Integer> binaryList = new ArrayList<>();
        while (!decimal.equals(BigInteger.ZERO)) {
            BigInteger[] divideAndRemainder = decimal.divideAndRemainder(BigInteger.TWO);
            binaryList.add(divideAndRemainder[1].intValue());
            decimal = divideAndRemainder[0];
        }

        int[] binary = new int[binaryList.size()];
        for (int i = 0; i < binary.length; i++) {
            binary[i] = binaryList.get(binary.length - 1 - i);
        }

        if (binary.length < keySize) {
            int[] newBinary = new int[keySize];
            int difference = keySize - binary.length;
            for (int i = 0; i < difference; i++) {
                newBinary[i] = 0;
            }
            if (keySize - difference >= 0)
                System.arraycopy(binary, 0, newBinary, difference, keySize - difference);
            binary = newBinary;
        }
        return binary;
    }
}