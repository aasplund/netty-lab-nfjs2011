package se.asplund.lab;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

public class MyClient extends SimpleChannelHandler {
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
//		readAndSendDataToSever(e.getChannel());
		Person person = new Person("Anders", "Asplund", 37);
		ChannelBuffer buffer = PersonEncoder.encode(person);
		e.getChannel().write(buffer);
	}
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		Person person = (Person) e.getMessage();
	}
	
	private void readAndSendDataToSever(Channel channel) {
		String input = System.console().readLine();
		
		if(input != null) {
			ChannelBuffer buffer = ChannelBuffers.buffer(input.length());
			buffer.writeBytes(input.getBytes());
			channel.write(buffer);
		} else {
			channel.close();
		}
	}

	public static void main(String[] args) {
		ChannelFactory factory = new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());

		ClientBootstrap bootStrap = new ClientBootstrap(factory);

		bootStrap.setPipelineFactory(new ChannelPipelineFactory() {
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(new MyServer());
			}
		});

		bootStrap.setOption("child.keepAlive", true);

		ChannelFuture channelFuture = bootStrap.connect(new InetSocketAddress("localhost", 8080));

		System.out.println("Client started...");

		channelFuture.getChannel().getCloseFuture().awaitUninterruptibly();
		System.out.println("ending client");

		factory.releaseExternalResources();
	}
}
