package network;

import main.netty.ProtocolTransfer;
import org.jboss.netty.channel.ChannelHandlerContext;

public class Session {
   private ProtocolTransfer protocolTransfer;
   private ChannelHandlerContext context;

   public Session(ProtocolTransfer protocolTransfer, ChannelHandlerContext context) {
      this.protocolTransfer = protocolTransfer;
      this.context = context;
   }

   public ProtocolTransfer getTransfer() {
      return this.protocolTransfer;
   }

   public boolean sessionOpened() {
      return this.context.getChannel().isOpen();
   }

   public boolean connected() {
      return this.context.getChannel().isConnected();
   }

   public String getIp() {
      return this.context.getChannel().getRemoteAddress().toString();
   }
}
