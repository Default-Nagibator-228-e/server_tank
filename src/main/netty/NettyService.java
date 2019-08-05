package main.netty;

import logger.Logger;
import system.destroy.Destroyable;
import test.osgi.OSGi;
import test.server.configuration.entitys.NettyConfiguratorEntity;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;

public class NettyService implements Destroyable {
   private static final NettyService instance = new NettyService();
   public int port;
   private ServerBootstrap bootstrap;

   private NettyService() {
      this.initParams();
      OrderedMemoryAwareThreadPoolExecutor bossExec = new OrderedMemoryAwareThreadPoolExecutor(100, 4000000000L, 2000000000L, 60L, TimeUnit.MILLISECONDS);
      OrderedMemoryAwareThreadPoolExecutor ioExec = new OrderedMemoryAwareThreadPoolExecutor(100, 4000000000L, 2000000000L, 60L, TimeUnit.MILLISECONDS);
      NioServerSocketChannelFactory factory = new NioServerSocketChannelFactory(bossExec, ioExec, 100);
      this.bootstrap = new ServerBootstrap(factory);
      this.bootstrap.setPipelineFactory(new NettyPipelineFactory());
      this.bootstrap.setOption("child.tcpNoDelay", Boolean.valueOf(true));
      this.bootstrap.setOption("child.keepAlive", Boolean.valueOf(true));
   }

   public void init() {
      this.bootstrap.bind(new InetSocketAddress(4894));
      Logger.log("[Netty] Server run on port: " + this.port);
   }

   public void destroy() {
      this.bootstrap.releaseExternalResources();
      //this.bootstrap.shutdown();
   }

   public static NettyService inject() {
      return instance;
   }

   private void initParams() {
      this.port = ((NettyConfiguratorEntity)OSGi.getModelByInterface(NettyConfiguratorEntity.class)).getPort();
   }
}
