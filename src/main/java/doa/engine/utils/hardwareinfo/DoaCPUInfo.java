package doa.engine.utils.hardwareinfo;

import java.util.Map;

public final class DoaCPUInfo {

	public String getVendorId() { return cpu.getVendorId(); }
	public String getFamily() { return cpu.getFamily(); }
	public String getModel() { return cpu.getModel(); }
	public String getModelName() { return cpu.getModelName(); }
	public String getStepping() { return cpu.getStepping(); }
	public String getMhz() { return cpu.getMhz(); }
	public String getCacheSize() { return cpu.getCacheSize(); }
	public String getNumCores() { return cpu.getNumCores(); }
	public String getTemperature() { return cpu.getTemperature(); }
	public Map<String, String> getFullInfo() { return cpu.getFullInfo(); }

	private org.jutils.jhardware.model.ProcessorInfo cpu = org.jutils.jhardware.HardwareInfo.getProcessorInfo();
	DoaCPUInfo() {}

}
