package doa.engine.utils.hardwareinfo;

public final class DoaNetworkInterface {

	public String getName() { return network.getName(); }
	public String getType() { return network.getType(); }
	public String getIpv4() { return network.getIpv4(); }
	public String getIpv6() { return network.getIpv6(); }
	public String getReceivedPackets() { return network.getReceivedPackets(); }
	public String getTransmittedPackets() { return network.getTransmittedPackets(); }
	public String getReceivedBytes() { return network.getReceivedBytes(); }
	public String getTransmittedBytes() { return network.getTransmittedBytes(); }

	private org.jutils.jhardware.model.NetworkInterfaceInfo network;
	DoaNetworkInterface(org.jutils.jhardware.model.NetworkInterfaceInfo network) { this.network = network; }
}
