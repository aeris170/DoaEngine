package doa.engine.utils.hardwareinfo;

import java.util.Map;

public final class DoaMemoryInfo {

	public String getTotalMemory() { return mem.getTotalMemory(); }
	public String getFreeMemory() { return mem.getFreeMemory(); }
	public String getAvailableMemory() { return mem.getAvailableMemory(); }
	public Map<String, String> getFullInfo() { return mem.getFullInfo(); }

	private org.jutils.jhardware.model.MemoryInfo mem = org.jutils.jhardware.HardwareInfo.getMemoryInfo();
	DoaMemoryInfo() {}
}
