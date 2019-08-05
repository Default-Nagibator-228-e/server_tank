package main.netty;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

public class NettyPipelineFactory implements ChannelPipelineFactory {
   public ChannelPipeline getPipeline() {
      ChannelPipeline pipeline = Channels.pipeline();
      pipeline.addLast("decoder", new StringDecoder());
      pipeline.addLast("encoder", new StringEncoder());
      pipeline.addLast("handler", new NettyUsersHandler());
      return pipeline;
   }
}
