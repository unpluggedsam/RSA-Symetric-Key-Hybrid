import java.util.ArrayList;
import java.util.List;
import java.math.BigInteger;

public class Computer {
    private final List<String> recentMessages = new ArrayList<>();
    private final BigInteger publicKey;
    private final BigInteger privateKey;
    private final BigInteger mod;

    /**
     * Constructor to create a new Computer object.
     * Generates a new public and private key for RSA encryption and a modulus.
     */
    public Computer() {
        Key key = new Key();
        this.publicKey = key.getPublicKey();
        this.privateKey = key.getPrivateKey();
        this.mod = key.getModulus();
    }

    /**
     * Sends a message to another computer.
     *
     * @param computer the computer to send the message to
     * @param message the message to send
     * @param size the size of the key to use for symmetric key encryption
     * @return the Packet object containing the encrypted message
     */
    public Packet sendMessage(Computer computer, String message, OneTimePad.KEY_SIZE size) {
        // generate key and encrypt it using the other computer's public key and modulus
        OneTimePad pad = new OneTimePad();
        int[] key = pad.generateKey(size);
        BigInteger encryptedDecimalKey = convertKeyToDecimalAndEncrypt(key, computer);
        BigInteger encryptedSize = cipher(BigInteger.valueOf(size.getSize()), computer.getPublicKey(), computer.getMod());

        // encrypt the message using the symmetric key and package it in a Packet object
        Packet packet = new Packet(OneTimePad.encryptString(message, key), encryptedDecimalKey, encryptedSize);

        // call the receiveMessage method on the other computer and return the Packet
        computer.receiveMessage(packet);
        return packet;
    }

    /**
     * This method receives a packet and decrypts the message stored in it.
     * It retrieves the binary message from the packet, the key used to encrypt it, and the key size.
     * It then uses the private key and modulus of the current computer to decrypt the key size and key.
     * The key is then used to decrypt the binary message using the OneTimePad.decryptBinary method.
     * The decrypted message is then added to the recentMessages list.
     *
     * @param packet - The packet containing the encrypted message, key, and key size.
     */
    public void receiveMessage(Packet packet) {
        // Decrypt the key size using the private key and modulus.
        int keySize = cipher(packet.getKeySize(), this.privateKey, this.mod).intValue();
        // Decrypt the key using the private key and modulus and convert it to binary.
        int[] key = convertKeyToBinaryAndDecrypt(packet.getKey(), OneTimePad.KEY_SIZE.valueOf(keySize).get());
        // Decrypt the binary message using the key.
        String decryptedMessage = OneTimePad.decryptBinary(packet.getBinary(), key);
        // Add the decrypted message to the recent messages list.
        recentMessages.add(decryptedMessage);
    }

    /**
     * Returns the recent messages received by the computer
     * @return list of recent messages
     */
    public List<String> getRecentMessages() {
        return recentMessages;
    }
    /**
     * Returns the public key of the computer
     * @return public key
     */
    public BigInteger getPublicKey() {
        return publicKey;
    }
    /**
     * Returns the modulus of the computer
     * @return modulus
     */
    public BigInteger getMod() {
        return mod;
    }
    /**
     * Converts a key in binary form to decimal and encrypts it
     * @param key: the key in binary form
     * @param computer: the computer to encrypt the key for
     * @return the encrypted key in decimal form
     */
    private BigInteger convertKeyToDecimalAndEncrypt(int[] key, Computer computer) {
        return cipher(convertBinaryToDecimal(key), computer.getPublicKey(), computer.getMod());
    }
    /**
     * Converts an encrypted key in decimal form to binary and decrypts it
     * @param key: the encrypted key in decimal form
     * @param size: the size of the key
     * @return the decrypted key in binary form
     */
    private int[] convertKeyToBinaryAndDecrypt(BigInteger key, OneTimePad.KEY_SIZE size) {
        return convertDecimalToBinary(cipher(key, this.privateKey, this.mod), size.getSize());
    }
    /**
     * Performs a modular exponentiation operation on the given binary key in decimal form
     * @param binaryKeyAsDecimal: the binary key in decimal form
     * @param key: the exponent
     * @param modulus: the modulus
     * @return the result of the operation
     */
    private BigInteger cipher(BigInteger binaryKeyAsDecimal, BigInteger key, BigInteger modulus) {
        return binaryKeyAsDecimal.modPow(key, modulus);
    }
    /**
     * Converts a binary key to decimal form
     * @param binary: the binary key
     * @return the key in decimal form
     */
    private BigInteger convertBinaryToDecimal(int[] binary) {
        BigInteger decimal = BigInteger.ZERO;
        BigInteger two = BigInteger.TWO;
        for (int i = 0; i < binary.length; i++) {
            decimal = decimal.add(BigInteger.valueOf(binary[i]).multiply(two.pow(binary.length - i - 1)));
        }
        return decimal;
    }

    /**
     * This method converts a decimal number to binary and pads it to the keySize. The reason for the padding
     * is that the {@link BigInteger} loses the prepended 0's when converting. Padding it by the key size
     * fixes this problem.
     * @param decimal the decimal number to be converted
     * @param keySize the desired size of the binary key
     * @return int array of binary digits
     */
    private int[] convertDecimalToBinary(BigInteger decimal, int keySize) {
        ArrayList<Integer> binaryList = new ArrayList<>();
        // divide the decimal by 2 and add the remainder to the binaryList
        while (!decimal.equals(BigInteger.ZERO)) {
            BigInteger[] divideAndRemainder = decimal.divideAndRemainder(BigInteger.TWO);
            binaryList.add(divideAndRemainder[1].intValue());
            decimal = divideAndRemainder[0];
        }

        int[] binary = new int[binaryList.size()];
        for (int i = 0; i < binary.length; i++) {
            binary[i] = binaryList.get(binary.length - 1 - i);
        }

        // if the binary key is smaller than keySize, pad it with 0s
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
