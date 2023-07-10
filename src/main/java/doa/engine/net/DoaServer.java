package doa.engine.net;

import static doa.engine.log.DoaLogger.LOGGER;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.validation.constraints.NotNull;

import com.simtechdata.waifupnp.UPnP;

/**
 * Standard issue socket server. An object of this class represents a physical
 * server which can send and receive messages to/from connected clients.<br>
 * <br>
 * In order to create and use a server, refer to the code segment below. *
 *
 * <pre>
 * <code>
 * DoaServer sv = new DoaServer(); // create a server instance
 * sv.open(27015); // open the server and start listening at port 27015
 * //...
 * sv.broadcast(-1, new DoaNetPacket&lt;&gt;("Is it me you're looking for?")); // broadcast a packet containing a string, -1 denoted server is the source of the message
 * sv.close(); // disconnect from all the clients and close the server
 * </code>
 * </pre>
 *
 * @author Doga Oruc
 * @since DoaEngine 3.0
 * @version 3.0
 */
public class DoaServer {

	private ServerSocket server = null;
	private List<DummyClient> clients = new CopyOnWriteArrayList<>();
	private int totalConnections = 0;

	/**
	 * Constructor
	 */
	public DoaServer() {
		try {
			server = new ServerSocket();
		} catch (IOException ex) {
			// swallow
		}
	}

	/**
	 * Opens the server on the specified port and binds it. A valid port value is
	 * between 0 and 65535. A port number of zero will let the system pick up an
	 * ephemeral port in a bind operation. Port forwarding is required to receive
	 * connection from outside the LAN.
	 *
	 * @param port the port number this server will get bound to
	 * @return the port number bounded to server or -1 upon failure to bind
	 */
	public int open(final int port) {
		try {
			if (UPnP.openPortTCP(port)) {
				server.bind(new InetSocketAddress((InetAddress) null, port));
				new Thread(() -> {
					while (!server.isClosed()) {
						try {
							Socket s = server.accept();
							ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
							oos.flush();
							ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
							DummyClient cl = new DummyClient(s, ois, oos, totalConnections++);
							clients.add(cl);
							new Thread(cl).start();
							LOGGER.info(new StringBuilder(128).append("Succesfully accepted a connection, Client #")
								.append(cl.id)
								.append(" from: ")
								.append(s.getInetAddress()));
						} catch (IOException ex) {
							// swallow
						}
					}
					LOGGER.info("Server successfully closed.");
				}, "Server Connection Accept Thread").start();
				LOGGER.info(new StringBuilder(128).append("Server succesfully bound to the port at ")
					.append(server.getLocalPort())
					.append(". Server is now listening for connections."));
				return server.getLocalPort();
			}
			LOGGER.warning(new StringBuilder(128).append("Could not bind to the port at ")
				.append(port)
				.append("! Port is either closed or is bound to some other socket."));
		} catch (IllegalArgumentException ex) {
			LOGGER.warning(new StringBuilder(128).append("Could not bind to the port at ")
				.append(port)
				.append("! Port number must be between 0 and 65535!"));
			ex.printStackTrace();
		} catch (IOException ex) {
			LOGGER.severe("Could not bind the socket! This is generally due to socket being already bound.");
			ex.printStackTrace();
		}
		return -1;
	}

	/**
	 * Closes all connections to the clients and shuts the server down. After this
	 * method returns, the server cannot be opened again. Applications that require
	 * to re-open a closed server, must create a new server.
	 */
	public void close() {
		clients.forEach(DummyClient::disconnect);
		try {
			server.close();
		} catch (IOException ex) {
			// swallow
		}
	}

	/**
	 * Sends a message to all connected clients, except the sender itself. A
	 * senderID of -1 indicates the server itself is the sender of the broadcast,
	 * any other valid ID indicates a client with that ID is the sender.
	 *
	 * @param senderID ID of the sender, -1 for server sourced broadcast
	 * @param packet message to send
	 */
	public void broadcast(final int senderID, @NotNull final DoaNetPacket<Serializable> packet) {
		clients.stream()
			.filter(client -> client.id != senderID)
			.forEach(client -> DoaNetUtils.send(client.output, packet));
		LOGGER.finer(new StringBuilder(256).append("Successfully broadcasted a packet containing ")
			.append(packet.getData())
			.append("."));
	}

	private class DummyClient implements Runnable {

		private Socket socket;
		private ObjectInputStream input;
		private ObjectOutputStream output;
		private int id;

		private DummyClient(final Socket socket, final ObjectInputStream input, final ObjectOutputStream output, final int id) {
			this.socket = socket;
			this.input = input;
			this.output = output;
			this.id = id;
			DoaNetUtils.send(output, new DoaNetPacket<>(id, PacketType.ID));
		}

		synchronized void disconnect() {
			try {
				DoaNetUtils.send(output, new DoaNetPacket<>(null, PacketType.DISCONNECT));
				socket.close();
				clients.remove(this);
			} catch (IOException ex) {
				// swallow
			}
		}

		@Override
		public void run() {
			new Thread(() -> {
				while (!socket.isClosed()) {
					try {
						DoaNetPacket<Serializable> packet = (DoaNetPacket<Serializable>) input.readObject();
						if (packet.type == PacketType.DATA) {
							broadcast(id, packet);
						} else if (packet.type == PacketType.DISCONNECT) {
							LOGGER.finer(new StringBuilder(128)
								.append("A DISCONNECT signal has been received from Client #")
								.append(id)
								.append("! Disconnecting from the client."));
							disconnect();
						}
					} catch (ClassNotFoundException ex) {
						ex.printStackTrace();
					} catch (SocketException ex) {
						LOGGER.finer(new StringBuilder(128).append("Client #")
							.append(id)
							.append(" has been shut down using extreme measures. Disconnecting from the client."));
						disconnect();
					} catch (IOException ex) {
						// swallow
					}
				}
				LOGGER.info(new StringBuilder(64).append("Connection with the Client #")
					.append(id)
					.append(" has been successfully ended."));
			},
					"DummyClient #"
						+ id).start();
		}
	}
}
