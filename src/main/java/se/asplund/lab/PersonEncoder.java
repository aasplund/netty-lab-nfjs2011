package se.asplund.lab;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import org.jboss.netty.buffer.ChannelBuffer;

public class PersonEncoder {

	public static ChannelBuffer encode(Person person) {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		new ObjectOutputStream(data).write(person);
		//two more lines
	}
}
