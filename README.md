# RSA encryption simulation
This project is a simulation of sending encrypted packets across a network. It makes use of my <a href="https://github.com/unpluggedsam/symmetric-key-algorithim">Symmetric Key</a> 
and <a href="https://github.com/unpluggedsam/RSA-Cryptosystem">RSA Cryptosystem</a> algorithims. The simulation allows a user to create
`Computer` objects and send `Packet`'s between them. It encrypts the packet, sends it over, then decrypts it on the other side. The program also allows
a user to inspect a `Packet` object which encapsulates all the information about the message. 


## How it Works

The first step of the simulation is to create two `Computer` objects. 

```Java
Computer computer1 = new Computer();
Computer computer2 = new Computer();
```

Then, on one of them, call the `sendMessage()` method. Provide the other key, the text to send, and the key size you would like to use to encrypt the message. 
(The larger the key size the more secure the encryption will be.) Store this information in a `Packet` object.

```Java
Packet packet = computer1.sendMessage(computer2, text, OneTimePad.KEY_SIZE.ONE_HUNDRED_TWENTY_EIGHT);
```

### Sending the Message
The `Computer` classes `sendMessage()` method provides an algorithim to encrypt the text, key, and key size. It works in 7 steps:

1. Generate a key from the `OneTimePad` class. Store it as `key`. 
2. Convert the text to binary and encrypt it using `key`.
3. Encrypt `key` using the other computers public key and modulus.
4. Encrypt  `key_size` using the other computers public key and modulus.
5. Package the encrypted binary, encrypted key, and encrypted `key_size` in a `Packet` object and store it as `packet`.
6. Call `recieveMessage()` on the other computer and pass in `packet`.
7. Return `packet`.

### Recieving the Message
The `Computer`'s class `recieveMessage()` method is the exact opposite of it's `sendMessage()` method. It decrypts the key, decrypts the key size,
and uses those to decrypt the binary. 

1. Decrypt  `key_size` by using the `Computer`'s private key and modulus. 
2. Decrypt `key` through the same method, but also pass in the decrypted `key_size` to the `convertKeyToBinaryAndDecrypt()` method.
3. Use the `OneTimePad` classes `decryptBinary()` method and decrypt the binary.
4. Add the decrypted binary to the `recentMessages List`.

To access the recieved message simply call the `getRecentMessages()` method from the second `Computer` object. 
```Java
System.out.println(computer2.getRecentMessages().get(0));
```

## Testing the Code

Testing the code is a relatively simple process. A user can access all the information through a `Packet` Object.

First, as shown above, create the `Computer` objects.

```Java
 Computer computer1 = new Computer();
 Computer computer2 = new Computer();
 ```
 Then, choose some sample text to send. 

```Java
 String text = "I LOVE eating some stuff occasionally and having friends.";
 ```

Finally, send the message from one computer to the other and store the results in a `Packet` object. 

```Java
 Packet packet = computer1.sendMessage(computer2, text, OneTimePad.KEY_SIZE.ONE_HUNDRED_TWENTY_EIGHT);
```

For greater encryption choose a larger key size.
 
 
To ensure that the message has been succesfully encrypted a user can check the `Packet` objects stored information. 

Check it's binary with, `packet.getBinary()`, check it's key with, `packet.getKey()`, and check it's key size with, `packet.getKeySize()`.

## Sample Code

```Java
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

        System.out.println("Encrypted Key Size: " + packet.getKeySize());
        System.out.println("Encrypted Key: " + packet.getKey());
        System.out.println("Encrypted Binary: " + OneTimePad.decryptBinary(packet.getBinary(), convertDecimalToBinary(packet.getKey(), packet.getKeySize().intValue())));

        System.out.println("Decrypted Text: " + computer2.getRecentMessages().get(0));

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
```

Sample output: 
```
Encrypted Key Size: 562949953421312
Encrypted Key: 6655747628909150883605321113682484283950432275675827238929313750103518226623821161969841086812304745766931580047114874586148809581822087125906199241297766612126657313367502198359615779323116292574309772774650197477680886284305286631742040464958083373139190487751069901
Encrypted Binary: Anm+gx¡¶ª^I¦]*Q	"!{Ëº¼§X¼]) NNw&sõ·¥O »UgPK9#nÅ
Decrypted Text: I LOVE eating some stuff occasionally and having friends.
```

<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE.txt` for more information.


<!-- CONTACT -->
## Contact

unpluggedsam - [@SamJaco13689394](https://twitter.com/SamJaco13689394) - unpluggedsam990@gmail.com

Project Link: [https://github.com/unpluggedsam/RSA-symetric-key-hybrid](https://github.com/unpluggedsam/RSA-symetric-key-hybrid)



