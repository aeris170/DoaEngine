package doa.engine.utils.hardwareinfo;

import java.util.Map;

public final class DoaBIOSInfo {
	
	public String getDate() { return info.getDate(); }
	public String getManufacturer() { return info.getManufacturer(); }
	public String getVersion() { return info.getVersion(); }
	public Map<String, String> getFullInfo() { return info.getFullInfo(); }

	private org.jutils.jhardware.model.BiosInfo info = org.jutils.jhardware.HardwareInfo.getBiosInfo();
	DoaBIOSInfo() {}
}
