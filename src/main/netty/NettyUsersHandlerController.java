package main.netty;

import java.util.HashMap;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

public class NettyUsersHandlerController extends HashMap implements Runnable {
   private static final long serialVersionUID = 4922899768061891423L;
   private Thread _thread = new Thread(this);

   public NettyUsersHandlerController() {
      this._thread.setName("NettyUsersHandlerController THREAD");
      this._thread.start();
   }

   public void onClientConnected(ChannelHandlerContext ctx) {
      this.put(ctx, new ProtocolTransfer(ctx.getChannel(), ctx));
   }

   public void onClientDisconnect(ChannelHandlerContext ctx) {
      if(this.get(ctx) != null) {
         ((ProtocolTransfer)this.get(ctx)).onDisconnect();
         this.remove(ctx);
      }
   }

   public void onMessageRecived(ChannelHandlerContext ctx, MessageEvent msg) {
      try {
         ((ProtocolTransfer) this.get(ctx)).decryptProtocol((String) msg.getMessage());
      }catch(Exception e){}
   }

   public void run() {
   }
}
