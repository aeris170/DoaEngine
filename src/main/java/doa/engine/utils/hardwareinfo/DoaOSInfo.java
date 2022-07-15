package doa.engine.utils.hardwareinfo;

import java.util.Map;

public final class DoaOSInfo {

	public String getVersion() { return os.getVersion(); }
	public String getLastBootTime() { return os.getLastBootTime(); }
	public String getName() { return os.getName(); }
	public String getManufacturer() { return os.getManufacturer(); }
	public Map<String, String> getFullInfo() { return os.getFullInfo(); }

	private org.jutils.jhardware.model.OSInfo os = org.jutils.jhardware.HardwareInfo.getOSInfo();
	DoaOSInfo() {}

}
