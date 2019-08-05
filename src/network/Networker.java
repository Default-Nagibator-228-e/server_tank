package network;

import utils.StringUtils;
import commands.Command;
import commands.Type;
import logger.Logger;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/** @deprecated */
@Deprecated
public class Networker implements INetworker {
   public Socket client;
   public SocketChannel channel;
   public final String splitterCommands = "~";
   public int bytes;

   public Networker(Socket client) {
      this.client = client;
      this.channel = client.getChannel();

      try {
         this.channel.configureBlocking(true);
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }

   public String socketToString() {
      return this.client.getInetAddress().toString().substring(1) + ":" + this.client.getPort() + "(local port:" + this.client.getLocalPort() + ")";
   }

   public String onCommand() throws IOException {
      ByteBuffer buffer = ByteBuffer.allocate(1024);
      buffer.clear();
      this.bytes = this.channel.read(buffer);
      if(this.bytes > 0) {
         buffer.flip();
         return new String(buffer.array());
      } else {
         return "";
      }
   }

   public boolean send(String msg) throws IOException {
      this.channel.write(ByteBuffer.wrap(msg.getBytes()));
      return true;
   }

   public boolean send(Type type, String... args) throws IOException {
      StringBuilder request = new StringBuilder();
      request.append(type.toString());
      request.append(";");

      for(int requestFinal = 0; requestFinal < args.length - 1; ++requestFinal) {
         request.append(StringUtils.concatStrings(new String[]{args[requestFinal], ";"}));
      }

      request.append(StringUtils.concatStrings(new String[]{args[args.length - 1], "~"}));
      String var5 = request.toString();
      this.channel.write(ByteBuffer.wrap(var5.getBytes()));
      request = null;
      var5 = null;
      return true;
   }

   public boolean send(Command command) throws IOException {
      this.send(command.type, command.args);
      return true;
   }

   public void closeConnection() {
      try {
         this.channel.close();
      } catch (IOException var2) {
         Logger.log(logger.Type.ERROR, var2.getMessage());
      }

   }
}
