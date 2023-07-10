package doa.engine.utils.hardwareinfo;

import java.util.Map;

public final class DoaMOBOInfo {

	public String getName() { return mobo.getName(); }
	public String getManufacturer() { return mobo.getManufacturer(); }
	public String getVersion() { return mobo.getVersion(); }
	public Map<String, String> getFullInfo() { return mobo.getFullInfo(); }

	private org.jutils.jhardware.model.MotherboardInfo mobo = org.jutils.jhardware.HardwareInfo.getMotherboardInfo();
	DoaMOBOInfo() {}
}
