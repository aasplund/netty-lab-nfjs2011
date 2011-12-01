package se.asplund.lab;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class MyServer extends SimpleChannelHandler {

	private static final ChannelGroup allMyChannels = new DefaultChannelGroup("my channels");
	
	@Override
	public void connectRequested(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		System.out.println("Connected");
		super.connectRequested(ctx, e);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		e.getCause().printStackTrace();
		super.exceptionCaught(ctx, e);
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		
		Person person = (Person) e.getMessage();
		System.out.println(person.firstName);
		
		if(person.age > 18) {
			person.setApproved(true);
		}
		e.getChannel().write(person);
	}

	public static void main(String[] args) {
		ChannelFactory factory = new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());

		ServerBootstrap bootStrap = new ServerBootstrap(factory);

		bootStrap.setPipelineFactory(new ChannelPipelineFactory() {
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(new PersonDecoder(), new MyServer());
			}
		});

		bootStrap.setOption("child.keepAlive", true);

		Channel channel = bootStrap.bind(new InetSocketAddress(8080));
		allMyChannels.add(channel);
		
		System.out.println("Server started...");
		
		System.out.println("enter something to quit");
		System.console().readLine();

		System.out.println("Terminating server");
		
		ChannelGroupFuture closeFuture = allMyChannels.close();
		closeFuture.awaitUninterruptibly();
		channel.getCloseFuture().awaitUninterruptibly();
		
		factory.releaseExternalResources();
	}
}
