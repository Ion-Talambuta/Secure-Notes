package sample.appConfigurations;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class EncryptionAES {

    //Parameters for encryption algorithm with AES/GCM/NiPadding
    private static final String ENCRYPT_ALGORITHM = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT_ENCRYPTION = 128;
    private static final int IV_LENGTH_BYTE_ENCRYPTION = 12;
    private static final int SALT_LENGTH_BYTE_ENCRYPTION = 16;
    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    /**
     * @param TextToEncrypt text to encrypt with Password
     * @param Password to encrypt the text in database
     * @return Base64 Encoded Text
     * @throws Exception for encrypted Text
     */
    // return a base64 encoded AES encrypted text
    public static String encryptText(byte[] TextToEncrypt, String Password) throws Exception {
        byte[] salt = CryptoUtilities.getRandomNonce(SALT_LENGTH_BYTE_ENCRYPTION);
        byte[] iv = CryptoUtilities.getRandomNonce(IV_LENGTH_BYTE_ENCRYPTION);

        SecretKey aesKeyGeneratedFromPassword = CryptoUtilities.getAESKeyFromPassword(Password.toCharArray(), salt);
        var cipherTextForEncryption = Cipher.getInstance(ENCRYPT_ALGORITHM);

        // ASE-GCM needs the GCMParameterSpec
        cipherTextForEncryption.init(Cipher.ENCRYPT_MODE, aesKeyGeneratedFromPassword,
                new GCMParameterSpec(TAG_LENGTH_BIT_ENCRYPTION, iv));
        byte[] cipherText = cipherTextForEncryption.doFinal(TextToEncrypt);

        // prefix @IV & @Salt to cipher text
        byte[] cipherTextWithIvSalt = ByteBuffer.allocate(iv.length + salt.length + cipherText.length)
                .put(iv).put(salt).put(cipherText).array();

        // string representation
        // base64 --> send this string to other for decryption.
        return Base64.getEncoder().encodeToString(cipherTextWithIvSalt);
    }

    // Utilise the same @password, @salt and @iv to decrypt Encrypted Text
    public static String decryptText(String cText, String password) throws NoSuchAlgorithmException,
                    InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException,
                    InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        byte[] decodeEncryptedTex = Base64.getDecoder().decode(cText.getBytes(UTF_8));

        // @iv & @salt from the Cipher text
        var byteBufferVariable = ByteBuffer.wrap(decodeEncryptedTex);
        var ivVariable = new byte[IV_LENGTH_BYTE_ENCRYPTION];
        byteBufferVariable.get(ivVariable);

        var saltToDecryption = new byte[SALT_LENGTH_BYTE_ENCRYPTION];
        byteBufferVariable.get(saltToDecryption);

        var cipherText = new byte[byteBufferVariable.remaining()];
        byteBufferVariable.get(cipherText);

        //Generate the AES key from the same @password and @salt
        SecretKey aesKeyFromPassword = CryptoUtilities.getAESKeyFromPassword(password.toCharArray(), saltToDecryption);
        var cipherEA = Cipher.getInstance(ENCRYPT_ALGORITHM);
        cipherEA.init(Cipher.DECRYPT_MODE, aesKeyFromPassword,
                new GCMParameterSpec(TAG_LENGTH_BIT_ENCRYPTION, ivVariable));

        byte[] decryptedText = cipherEA.doFinal(cipherText);

        return new String(decryptedText, UTF_8);
    }
}
