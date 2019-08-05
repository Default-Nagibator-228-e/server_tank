package bugs.screenshots;

import java.io.*;

public class BufferScreenshotTransfer {

   public BufferScreenshotTransfer() {

   }

   public void encryptPacket(String packet,int f) {
      try {
         FileWriter writer = new FileWriter("skr/" + f + ".jpg", false);
         writer.write(packet);
         writer.flush();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
