package network.requests;

import commands.Command;
import commands.Commands;
import logger.Logger;
import utils.StringUtils;
import java.net.Socket;
import auth.Auth;
import lobby.LobbyManager;
import network.Networker;

@Deprecated
public class RequestsTransfer extends Networker implements Runnable
{
   private boolean work;
   private StringBuffer inputRequest;
   private StringBuffer badRequest;
   public LobbyManager lobby;
   private Auth auth;

   public RequestsTransfer(final Socket client) {
      super(client);
      this.work = true;
      this.badRequest = new StringBuffer();
   }

   public void run() {
      try {
         while (this.work && super.bytes != -1) {
            final StringBuffer inputRequest = new StringBuffer(this.onCommand().trim());
            this.inputRequest = inputRequest;
            if (inputRequest.length() > 0) {
               if (this.inputRequest.toString().endsWith("~")) {
                  this.inputRequest = new StringBuffer(StringUtils.concatStrings(new String[] { this.badRequest.toString(), this.inputRequest.toString() }));
                  this.parseInputRequest();
                  this.badRequest = new StringBuffer();
               }
               else {
                  this.badRequest = new StringBuffer(StringUtils.concatStrings(new String[] { this.badRequest.toString(), this.inputRequest.toString() }));
               }
            }
         }
         Logger.log("User " + super.socketToString() + " has been disconnected.");
         this.lobby.onDisconnect();
      }
      catch (Exception ex) {
         this.work = false;
         if (this.lobby != null) {
            this.lobby.onDisconnect();
         }
      }
   }

   private void parseInputRequest() {
      final String[] commands = this.inputRequest.toString().split("~");
      String[] array;
      for (int length = (array = commands).length, i = 0; i < length; ++i) {
         final String request = array[i];
         this.sendCommandToManagers(Commands.decrypt(request));
      }
   }

   private void sendCommandToManagers(final Command cmd) {
      if (this.auth == null) {
         this.auth = new Auth(null, null);
      }
      switch (cmd.type) {
         case AUTH: {
            this.auth.executeCommand(cmd);
            break;
         }
         case CHAT: {
            this.lobby.executeCommand(cmd);
            break;
         }
         case BATTLE: {
            this.lobby.executeCommand(cmd);
            break;
         }
         case GARAGE: {
            this.lobby.executeCommand(cmd);
            break;
         }
         case LOBBY: {
            this.lobby.executeCommand(cmd);
            break;
         }
         case LOBBY_CHAT: {
            this.lobby.executeCommand(cmd);
            break;
         }
         case PING: {
            this.lobby.executeCommand(cmd);
            break;
         }
         case REGISTRATON: {
            this.auth.executeCommand(cmd);
         }
         case UNKNOWN: {
            Logger.log("User " + this.socketToString() + " send unknowed request: " + cmd.toString());
            break;
         }
      }
   }
}