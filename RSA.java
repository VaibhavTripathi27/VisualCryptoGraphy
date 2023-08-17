import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSA{
  static final int WIDTH = 1000, HEIGHT = 1000;
  static final String[] COLORS = {"Red", "Green", "Blue"};

  static final PrintStream stdout = System.out;

  static {
    try {
      KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
      keyPairGenerator.initialize(512);

      KeyPair keyPair = keyPairGenerator.generateKeyPair();
      Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

      for(String i: COLORS) {
        crypto(i, keyPair, cipher, "target", new String[]{"RSA_cipher", "RSA_target"});
      }
    } catch(NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | IOException e){
      e.printStackTrace();
    }
  }
  
  private static void crypto(String clr, KeyPair keyPair, Cipher cipher, String src, String[] dest)
  throws NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, IOException {
    Scanner scanner = new Scanner(new File(src + "text_" + clr + ".txt"));

    byte[][] buffer = new byte[2][WIDTH * HEIGHT];

    for(int i = 0; i < 31250; i++){
      byte[] tmp = new byte[32];
      for(int j = 0; j < tmp.length; j++) {
        tmp[j] = (byte) scanner.nextInt();
      }

      cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
      byte[] buff = cipher.doFinal(tmp);

      for(int j = 0; j < tmp.length; j++) {
        buffer[0][tmp.length * i + j] = buff[j];
      }

      cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
      buff = cipher.doFinal(buff);

      for(int j = 0; j < tmp.length; j++) {
        buffer[1][tmp.length * i + j] = buff[j];
      }
    }

    for(int i = 0; i < buffer.length; i++) {
      toFile(dest[i], clr, buffer[i]);
    }

    System.setOut(stdout);
  }

  private static void toFile(String dest, String clr, byte[] buffer) throws IOException {
    System.setOut(new PrintStream(new File(dest + "text_" + clr + ".txt")));
    for(byte i: buffer) {
      System.out.print((i & 255) + " ");
    }
  }
}
