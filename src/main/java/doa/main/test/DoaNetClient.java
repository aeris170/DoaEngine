package doa.main.test;

import java.io.Serializable;
import java.net.UnknownHostException;

import doa.engine.log.DoaLogger;
import doa.engine.log.LogLevel;
import doa.engine.net.DoaClient;
import doa.engine.net.DoaNetPacket;

class DoaNetClient implements Serializable {

	private int a;

	public DoaNetClient() { a = 6; }

	public int getA() { return a; }

	public static void main(final String... args) throws UnknownHostException {
		DoaLogger.getInstance().setLevel(LogLevel.FINEST);
		createClient();
	}

	private static void createClient() {
		var ds = new DoaClient() {

			@Override
			protected void onReceive(DoaNetPacket<Serializable> packet) {
				super.onReceive(packet);
				System.out.println(packet);
			}
		};
		try {
			ds.connect("localhost", 27015);
		} catch (UnknownHostException ex) {}
		ds.send(new DoaNetPacket<>("HELLO"));
		ds.send(new DoaNetPacket<>(new DoaNetClient()));
		ds.disconnect();
	}
}
