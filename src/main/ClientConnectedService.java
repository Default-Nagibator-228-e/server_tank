package main;

import logger.Logger;
import network.requests.RequestsTransfer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;

/** @deprecated */
@Deprecated
public class ClientConnectedService extends Thread {
   private ServerSocketChannel serverChannel;
   private static final int PORT = 12345;

   public ClientConnectedService() {
      this.setName("ClientConnectedService Thread");

      try {
         this.serverChannel = ServerSocketChannel.open();
         this.serverChannel.configureBlocking(true);
         this.serverChannel.socket().bind(new InetSocketAddress(12345));
         Logger.log("Server run on 12345 . Wait clients...\n");
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }

   public static void clientConnected(Socket s) {
      Logger.log("User " + s.getInetAddress() + " connected... Recived to auth thread!");
      Thread thread = new Thread(new RequestsTransfer(s));
      thread.setName("RequestThread " + s.getInetAddress());
      thread.start();
   }

   public void run() {
      try {
         Socket ex = null;

         while(true) {
            ex = this.serverChannel.socket().accept();
            ex.setKeepAlive(true);
            clientConnected(ex);
         }
      } catch (Exception var2) {
         var2.printStackTrace();
      }
   }
}
