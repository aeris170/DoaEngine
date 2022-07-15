package doa.engine.utils.hardwareinfo;

import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class DoaEngineInfo {

	@Getter private static final DoaRuntimeInfo runtimeInfo = new DoaRuntimeInfo();
	@Getter private static final DoaBIOSInfo biosInfo = new DoaBIOSInfo();
	@Getter private static final DoaDisplayInfo displayInfo = new DoaDisplayInfo();
	@Getter private static final DoaGPUInfo gpuInfo = new DoaGPUInfo();
	@Getter private static final DoaMemoryInfo memoryInfo = new DoaMemoryInfo();
	@Getter private static final DoaMOBOInfo motherboardInfo = new DoaMOBOInfo();
	@Getter private static final DoaNetworkInfo networkInfo = new DoaNetworkInfo();
	@Getter private static final DoaOSInfo osInfo = new DoaOSInfo();
	@Getter private static final DoaCPUInfo cpuInfo = new DoaCPUInfo();
}
