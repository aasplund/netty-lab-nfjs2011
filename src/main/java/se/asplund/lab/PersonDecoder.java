package se.asplund.lab;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

public class PersonDecoder extends FrameDecoder {

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer) throws Exception {
		Object person = null;
		if (buffer.readByte() > 0) {
			byte[] data = new byte[buffer.readableBytes()];
			buffer.readBytes(data);
			person = new ObjectInputStream(new ByteArrayInputStream(data)).readObject();

		}
		return person;
	}

}
