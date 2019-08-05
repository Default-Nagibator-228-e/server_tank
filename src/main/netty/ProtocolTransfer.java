package main.netty;

import java.io.IOException;
import commands.Type;
import commands.Command;
import commands.Commands;
import logger.Logger;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channel;
import auth.Auth;
import lobby.LobbyManager;

public class ProtocolTransfer
{
   private static final String SPLITTER_CMDS = "end~";
   private String inputRequest;
   private String badRequest;
   public LobbyManager lobby;
   public Auth auth;
   private Channel channel;
   private ChannelHandlerContext context;

   public ProtocolTransfer(final Channel channel, final ChannelHandlerContext context) {
      this.badRequest = "";
      this.channel = channel;
      this.context = context;
   }

   public void decryptProtocol(final String protocol) {
      this.inputRequest = protocol;
      if (inputRequest.length() > 0) {
         if (this.inputRequest.endsWith("end~")) {
            this.inputRequest = this.badRequest + this.inputRequest;//utils.StringUtils.concatStrings(new String[] { this.badRequest.toString(), this.inputRequest.toString() }));
            String[] cryptRequests;
            for (int length = (cryptRequests = this.parseCryptRequests()).length, i = 0; i < length; ++i) {
               final String request = cryptRequests[i];
               int key;
               try {
                  key = Integer.parseInt(String.valueOf(request.charAt(0)));
               }
               catch (Exception ex) {
                  //Logger.log("[EXCEPTION] Detected cheater(replace protocol): " + this.channel.toString());
                  //NettyUsersHandler.block(this.channel.getRemoteAddress().toString().split(":")[0]);
                  //this.closeConnection();
                  return;
               }
               /*if (key == this._lastKey) {
                  Logger.log("Detected cheater(replace protocol): " + this.channel.toString());
                  NettyUsersHandler.block(this.channel.getRemoteAddress().toString().split(":")[0]);
                  this.closeConnection();
                  return;
               }
               final int nextKey = (this._lastKey + 1) % this._keys.length;
               if (key != ((nextKey == 0) ? 1 : nextKey)) {
                  Logger.log("[NOT QUEQUE KEY " + nextKey + " " + this._lastKey + "] Detected cheater(replace protocol): " + this.channel.toString());
                  NettyUsersHandler.block(this.channel.getRemoteAddress().toString().split(":")[0]);
                  this.closeConnection();
                  return;
               }*/
               this.inputRequest = this.decrypt(request.substring(1), key);
               this.sendRequestToManagers(this.inputRequest);
            }
            this.badRequest = "";
         }
         else {
            this.badRequest = this.badRequest + this.inputRequest;//utils.StringUtils.concatStrings(new String[] { this.badRequest.toString(), this.inputRequest.toString() }));
         }
      }
   }

   private String[] parseCryptRequests() {
      return this.inputRequest.split("end~");
   }

   private String decrypt(final String request, final int key) {
      final char[] _chars = request.toCharArray();
      for (int i = 0; i < request.length(); ++i) {
         _chars[i] -= (char)key;
      }
      return new String(_chars);
   }

   private void sendRequestToManagers(final String request) {
      this.sendCommandToManagers(Commands.decrypt(request));
   }

   public void sendCommandToManagers(final Command cmd) {
      if (this.auth == null) {
         this.auth = new Auth(this, this.context);
      }
      if(cmd == null)
      {
         return;
      }
      switch (cmd.type) {
         case BATTLE :
         case CHAT:
         case GARAGE:
         case LOBBY:
         case LOBBY_CHAT:
            this.lobby.executeCommand(cmd);
            break;
         case AUTH:
         case REGISTRATON:
            this.auth.executeCommand(cmd);
            break;
         case PING:
            break;
         case SYSTEM:
            if (this.auth != null) {
               this.auth.executeCommand(cmd);
            }
            if (this.lobby != null) {
               this.lobby.executeCommand(cmd);
               break;
            }
            break;
         case UNKNOWN:
            Logger.log("User " + this.channel.toString() + " send unknowed request: " + cmd.toString());
      }
   }

   public boolean send(final Type type, final String... args) {
      StringBuilder request = new StringBuilder();
      request.append(type.toString());
      request.append(";");
      for (int i = 0; i < args.length - 1; ++i) {
         request.append(args[i] + ";");//utils.StringUtils.concatStrings(new String[] { args[i], ";" }));
      }
      request.append(args[args.length - 1] + "end~");//utils.StringUtils.concatStrings(new String[] { args[args.length - 1], "end~" }));
      if (this.channel.isWritable() && this.channel.isConnected() && this.channel.isOpen()) {
          if(request.length() < 60000) {
             this.channel.write(request.toString());
          }else{
             int a = 0;
             int b = 59999;
             while(b <= request.length())
             {
                this.channel.write(request.toString().substring(a,b));
                if(b == request.length())
                {
                   break;
                }
                if(request.length() - b >= 60000)
                {
                   a += 60000;
                   b += 60000;
                }else{
                   int f = request.length() - b;
                   a += f;
                   b += f;
                }
             }
          }
      }
      return true;
   }

   protected void onDisconnect() {
      if (this.lobby != null) {
         this.lobby.onDisconnect();
      }
      /*try {
         throw new Exception("");
      } catch (Exception e) {
         e.printStackTrace();
      }*/
   }

   public void closeConnection() {
      this.channel.close();
      /*try {
         throw new Exception("");
      } catch (Exception e) {
         e.printStackTrace();
      }*/
   }

   public String getIP() {
      return this.channel.getRemoteAddress().toString();
   }
}