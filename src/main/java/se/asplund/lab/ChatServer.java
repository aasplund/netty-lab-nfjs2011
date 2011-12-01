package se.asplund.lab;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
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

public class ChatServer extends SimpleChannelHandler {
	private static final ChannelGroup allMyChannels = new DefaultChannelGroup(
			"my channels");

	@Override
	public void connectRequested(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		System.out.println("Client connected");
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
		ChannelBuffer buff = (ChannelBuffer) e.getMessage();
		if (buff.getByte(0) == 4) { // 4 == Ctrl+D
			e.getChannel().close();
		} else {
			allMyChannels.write(buff);
		}	
	}

	public static void main(String[] args) {
		ChannelFactory factory = new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());

		ServerBootstrap bootStrap = new ServerBootstrap(factory);

		bootStrap.setPipelineFactory(new ChannelPipelineFactory() {
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(new ChatServer());
			}
		});

		bootStrap.setOption("child.keepAlive", true);

		Channel channel = bootStrap.bind(new InetSocketAddress(8080));
		allMyChannels.add(channel);

		System.out.println("Server started...");
		System.out.println("Hit any key to quit server");

		System.console().readLine();

		System.out.println("Terminating server");

		ChannelGroupFuture closeFuture = allMyChannels.close();
		closeFuture.awaitUninterruptibly();
		channel.getCloseFuture().awaitUninterruptibly();
	}
}
