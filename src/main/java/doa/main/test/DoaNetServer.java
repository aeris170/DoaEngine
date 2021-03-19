package doa.main.test;

import doa.engine.log.DoaLogger;
import doa.engine.log.LogLevel;
import doa.engine.net.DoaNetPacket;
import doa.engine.net.DoaServer;

class DoaNetServer {

	public static void main(final String... args) {
		DoaLogger.getInstance().setLevel(LogLevel.FINEST);
		createServer();
	}

	private static void createServer() {
		var ds = new DoaServer();
		ds.open(27015);
		ds.broadcast(-1, new DoaNetPacket<>("Is it me you're looking for?"));
		ds.close();
	}
}
