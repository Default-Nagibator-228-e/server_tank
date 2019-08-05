package main.netty;

import logger.Logger;
import logger.remote.RemoteDatabaseLogger;
import main.netty.blackip.model.BlackIPsModel;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class NettyUsersHandler extends SimpleChannelUpstreamHandler {
   private NettyUsersHandlerController controller = new NettyUsersHandlerController();
   private static BlackIPsModel blackList = new BlackIPsModel();

   public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
      super.handleUpstream(ctx, e);
   }

   public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
      if(blackList.contains(ctx.getChannel().getRemoteAddress().toString().split(":")[0])) {
         ctx.getChannel().close();
      } else {
         this.controller.onClientConnected(ctx);
         this.log("Client connected from " + ctx.getChannel().getRemoteAddress() + " (" + ctx.getChannel().getId() + ")");
      }
   }

   public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
      this.controller.onClientDisconnect(ctx);
      //this.log("Connection closed from " + ctx.getChannel().getRemoteAddress() + " (" + ctx.getChannel().getId() + ")");
   }

   public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
      this.controller.onMessageRecived(ctx, e);
   }

   public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
      try {
         StringWriter ex = new StringWriter();
         e.getCause().printStackTrace(new PrintWriter(ex));
         String exceptionAsString = ex.toString();
         RemoteDatabaseLogger.error(exceptionAsString.toString());
         if(ctx.getChannel().isConnected()) {
            ctx.getChannel().close();
         }
      } catch (Exception var5) {
         var5.printStackTrace();
         RemoteDatabaseLogger.error(var5);
      }

   }

   public static void block(String ip) {
      System.out.println(ip);
      ip = ip.split(":")[0];
      blackList.block(ip);
   }

   public static void unblock(String ip) {
      ip = ip.split(":")[0];
      blackList.unblock(ip);
   }

   private void log(String txt) {
      Logger.log("[Netty]: " + txt);
   }
}
