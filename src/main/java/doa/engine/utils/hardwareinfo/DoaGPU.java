package doa.engine.utils.hardwareinfo;

public final class DoaGPU {

	public String getName() { return gpu.getName(); }
    public String getManufacturer() { return gpu.getManufacturer(); }
    public String getChipType() { return gpu.getChipType(); }
    public String getDacType() { return gpu.getDacType(); }
    public String getDeviceType() { return gpu.getDeviceType(); }
    public String getTemperature() { return gpu.getTemperature(); }
    public String getFanSpeed() { return gpu.getFanSpeed(); }

	private org.jutils.jhardware.model.GraphicsCard gpu;
	DoaGPU(org.jutils.jhardware.model.GraphicsCard gpu) { this.gpu = gpu; }
}
