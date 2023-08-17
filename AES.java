import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class AES {
  static final int WIDTH = 1000, HEIGHT = 1000;
  static final String[] COLORS = {"Red", "Green", "Blue"};

  static final PrintStream stdout = System.out;

  static {
    try {
      SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey();
      Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");

      for(String i: COLORS) {
        crypto(i, secretKey, cipher, Cipher.ENCRYPT_MODE, "target", "AES_cipher");
        crypto(i, secretKey, cipher, Cipher.DECRYPT_MODE, "AES_cipher", "AES_target");
      }
    } catch(NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | IOException e) {
      e.printStackTrace();
    }
  }

  private static void crypto(String clr, SecretKey secretKey, Cipher cipher, int flag, String src, String dest)
  throws NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, IOException {
    Scanner scanner = new Scanner(new File(src + "text_" + clr + ".txt"));

    byte[] buffer = new byte[WIDTH * HEIGHT];
    for(int i = 0; i < buffer.length; i++) {
      buffer[i] = (byte) scanner.nextInt();
    }

    cipher.init(flag, secretKey);
    buffer = cipher.doFinal(buffer);

    System.setOut(new PrintStream(new File(dest + "text_" + clr + ".txt")));
    for(byte i: buffer) {
      System.out.print((i & 255) + " ");
    }

    System.setOut(stdout);
  }
}