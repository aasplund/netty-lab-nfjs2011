package se.asplund.lab;

import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

public class ChatClient extends SimpleChannelHandler {

	public static void main(String[] args) {
		ChannelFactory factory = new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());
	
	ClientBootstrap bootstrap = new ClientBootstrap(factory);
	
	bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
		
		@Override
		public ChannelPipeline getPipeline() throws Exception {
			// TODO Auto-generated method stub
			return null;
		}
	});
	}
}
