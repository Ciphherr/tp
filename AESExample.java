import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class AESExample {

    private static final String AES = "AES";
    private static final String AES_CBC = "AES/CBC/PKCS5Padding";

    // Method to encrypt a plaintext string
    public static String encrypt(String plaintext, SecretKey secretKey, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_CBC);
        IvParameterSpec ivParams = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParams);
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Method to decrypt a ciphertext string





    
    public static String decrypt(String ciphertext, SecretKey secretKey, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_CBC);
        IvParameterSpec ivParams = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParams);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
        return new String(decryptedBytes);
    }

    public static void main(String[] args) {
        try {
            // Generate a new AES key
            KeyGenerator keyGen = KeyGenerator.getInstance(AES);
            keyGen.init(128); // You can use 192 or 256 bits as well
            SecretKey secretKey = keyGen.generateKey();

            // Use a static IV for this example (NOTE: In practice, use a random IV for each encryption)
            byte[] iv = new byte[16]; // AES block size is 16 bytes
            System.arraycopy("RandomInitVector".getBytes(), 0, iv, 0, iv.length);

            String plaintext = "AES Example";
            System.out.println("Plaintext: " + plaintext);

            // Encrypt the plaintext
            String ciphertext = encrypt(plaintext, secretKey, iv);
            System.out.println("Ciphertext: " + ciphertext);

            // Decrypt the ciphertext
            String decryptedText = decrypt(ciphertext, secretKey, iv);
            System.out.println("Decrypted Text: " + decryptedText);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
