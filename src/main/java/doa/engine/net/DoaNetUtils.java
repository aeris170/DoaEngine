package doa.engine.net;

import static doa.engine.log.DoaLogger.LOGGER;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.SocketException;

class DoaNetUtils {

	private DoaNetUtils() {}

	static synchronized boolean send(final ObjectOutputStream stream, final DoaNetPacket<Serializable> packet) {
		try {
			stream.writeObject(packet);
			stream.flush();
			if (packet.type == PacketType.DATA) { return true; }
		} catch (SocketException ex) {
			if (packet.type == PacketType.DATA) {
				LOGGER.warning("Failed to send a packet containing "
					+ packet.getData()
					+ "! Connection lost!");
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return false;
	}
}
