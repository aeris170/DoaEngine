package doa.engine.net;

import java.io.Serializable;

/**
 * Use objects of this class to encapsulate data that will be transmitted
 * through the objects of DoaClient and DoaServer. The data to be sent must
 * implement {@link Serializable} interface. See how Java's {@link Serializable}
 * interface works for more info.
 * 
 * @author Doga Oruc
 * @since DoaEngine 3.0
 * @version 3.0
 * @param <T> a type implementing {@link Serializable}
 * @see Serializable
 */
public class DoaNetPacket<T extends Serializable> implements Serializable {

	private static final long serialVersionUID = -8346748842335921739L;

	PacketType type;
	private T data;

	/**
	 * Constructor
	 * 
	 * @param data object to send
	 */
	public DoaNetPacket(final T data) { this(data, PacketType.DATA); }

	/**
	 * Constructor
	 * 
	 * @param data object to send
	 */
	DoaNetPacket(final T data, final PacketType type) {
		this.data = data;
		this.type = type;
	}

	/**
	 * @return the data inside this packet
	 */
	public T getData() { return data; }
}

enum PacketType {
	DISCONNECT,
	DATA,
	ID
}
