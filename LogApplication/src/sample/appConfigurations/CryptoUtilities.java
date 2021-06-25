package sample.appConfigurations;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class CryptoUtilities {

    public static byte[] getRandomNonce(int numBytes) {

        var stringNonce = new byte[numBytes];
        new SecureRandom().nextBytes(stringNonce);

        return stringNonce;
    }

    // Password generated with AES 256 bits secret key
    public static SecretKey getAESKeyFromPassword(char[] password, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        var secretFactoryKey = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec keySpecGenerated = new PBEKeySpec(password, salt, 65536, 256);

        return new SecretKeySpec(secretFactoryKey.generateSecret(keySpecGenerated).getEncoded(), "AES");

    }
}
