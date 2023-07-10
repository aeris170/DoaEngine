package doa.engine.net;

import static doa.engine.log.DoaLogger.LOGGER;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayDeque;
import java.util.Queue;

import javax.validation.constraints.NotNull;

import com.google.errorprone.annotations.ForOverride;

/**
 * A barebones wrapper for {@link Socket} and it's desired functionality. <br>
 * <br>
 * In order to create and connect a client, and communicate with it refer to the
 * code segment below.
 *
 * <pre>
 * <code>
 * DoaClient cl = new DoaClient(); // create a client instance
 * try {
 *    // try to connect to 159.10.210.199:27015
 *    cl.connect("159.10.210.199", port); // a server at 159.10.210.199 must be listening at the port 27015
 *    // an invoke to connect will block until a connection is made
 *    // or an error occurs, like a connection timeout
 * } catch (UnknownHostException ex) {
 *    // if connection fails, handle it
 * }
 * cl.send(new DoaNetPacket&lt;&gt;("HELLO")); // send a packet containing the string "HELLO"
 * cl.disconnect(); // disconnect form the server
 * </code>
 * </pre>
 *
 * @author Doga Oruc
 * @since DoaEngine 3.0
 * @version 3.0
 * @see Socket
 */
public class DoaClient {

	/**
	 * The instance of socket connected to server. This socket exists when the
	 * object is instantiated. The socket is connected to a server when a call to
	 * {@link #connect(String, int)} returns successfully.
	 */
	protected Socket socket;

	/**
	 * Input stream used to receive data from the server.
	 */
	protected ObjectInputStream input = null;

	/**
	 * Output stream used to send data to the server.
	 */
	protected ObjectOutputStream output = null;

	/**
	 * The unique ID of this socket, appointed by the server upon a successful
	 * connection.
	 */
	protected int id = -1;

	protected Queue<DoaNetPacket<Serializable>> q = new ArrayDeque<>();

	/**
	 * Constructor
	 */
	public DoaClient() { socket = new Socket(); }

	/**
	 * Connects to a server. This method blocks until a connection has been made or
	 * an error occurs.
	 *
	 * @param ipAddress IP Address of the server
	 * @param port port in which the server is listening on
	 * @throws UnknownHostException thrown when an error occurs while trying to
	 * connect, most common case is a connection timeout
	 */
	public synchronized void connect(final String ipAddress, final int port) throws UnknownHostException {
		try {
			socket.connect(new InetSocketAddress(ipAddress, port));
			LOGGER.info(new StringBuilder(64).append("Succesfully connected to Server at ")
				.append(ipAddress)
				.append(":")
				.append(port)
				.append("."));
		} catch (IOException ex) {
			LOGGER.warning(new StringBuilder(64).append("Could not resolve host at ")
				.append(ipAddress)
				.append(":")
				.append(port)
				.append("!"));
			throw new UnknownHostException(ex.getMessage());
		}
		try {
			input = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
			new Thread(() -> {
				while (!socket.isClosed()) {
					try {
						DoaNetPacket<Serializable> packet = (DoaNetPacket<Serializable>) input.readObject();
						if (packet.type == PacketType.DATA) {
							LOGGER.finer(new StringBuilder(256).append("A packet containing ")
								.append(packet.getData())
								.append(" has been received!"));
							onReceive(packet);
						} else if (packet.type == PacketType.DISCONNECT) {
							if (!socket.isClosed()) {
								LOGGER
									.finer("A packet containing DISCONNECT signal has been received! Disconnecting from the Server.");
								disconnect();
							}
						} else if (packet.type == PacketType.ID) {
							this.id = (int) packet.getData();
							Thread.currentThread()
								.setName(new StringBuilder(64).append("Client #")
									.append(id)
									.append(" Data Retrieval Thread")
									.toString());
							LOGGER
								.finer("A packet containing ID information of client has been received from the server.");
						}
					} catch (ClassNotFoundException ex) {
						ex.printStackTrace();
					} catch (SocketException ex) {
						LOGGER
							.finer("Connected Server is shut down using some extreme measures. Disconnecting from the Server.");
						disconnect();
					} catch (IOException ex) {
						// swallow
					}
				}
				LOGGER.info("Connection with the Server has been successfully ended.");
			}).start();
			onConnect();
		} catch (IOException ex) {
			// swallow
		}
	}

	/**
	 * Sends a packet of data to the connected server. A connection must exist prior
	 * to a call to this method, otherwise this method will throw an
	 * {@link IllegalStateException}.
	 *
	 * @param packet data to send
	 * @throws IllegalStateException if {@link #isConnected()} returns false
	 */
	public synchronized void send(@NotNull final DoaNetPacket<Serializable> packet) {
		if (isConnected()) {
			if (DoaNetUtils.send(output, packet)) {
				LOGGER.finer(new StringBuilder(256).append("Successfully sent a packet containing ")
					.append(packet.getData())
					.append("."));
			}
		} else {
			throw new IllegalStateException(
					"Cannot send, since no connection has been made to a Server or a connection existed but has already been ended with the Server!");
		}
	}

	/**
	 * Disconnects from the server. Prior to a call to this method, a connection
	 * must exist, otherwise an {@link IllegalStateException} is thrown.
	 *
	 * @throws IllegalStateException if {@link #isConnected()} returns false
	 */
	public synchronized void disconnect() {
		try {
			send(new DoaNetPacket<>(null, PacketType.DISCONNECT));
			socket.close();
			onDisconnect();
		} catch (IOException ex) {
			// swallow
		}
	}

	/**
	 * Returns the connection state of the client.
	 *
	 * @return true if and only if client is already connected to a server and the
	 * connection has not been ended yet, otherwise false
	 */
	public boolean isConnected() { return socket.isConnected() && !socket.isClosed(); }

	/**
	 * @return the unique ID of this client, appointed by the Server it is connected
	 * to; or -1, if no connection has been made so far
	 */
	public int getID() { return id; }

	/**
	 * Invoked upon a successful connection. Upon entering this method, the client
	 * is ready to send and receive messages from the Server.
	 */
	@ForOverride
	protected void onConnect() {}

	/**
	 * Invoked upon the retrieval of data from the Server.
	 *
	 * @param packet data coming from the server
	 */
	@ForOverride
	protected void onReceive(final DoaNetPacket<Serializable> packet) {}

	/**
	 * Invoked upon a successful disconnection. Upon entering this method, the
	 * client has already disconnected from the Server.
	 */
	@ForOverride
	protected void onDisconnect() {}
}
