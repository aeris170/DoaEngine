package doa.engine.utils.hardwareinfo;

import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class DoaEngineInfo {

	@Getter private static boolean isInitialized = false;
	public static final void initialize() {
		if (isInitialized) { return; }
		
		runtimeInfo = new DoaRuntimeInfo();
		biosInfo = new DoaBIOSInfo();
		displayInfo = new DoaDisplayInfo();
		gpuInfo = new DoaGPUInfo();
		memoryInfo = new DoaMemoryInfo();
		motherboardInfo = new DoaMOBOInfo();
		networkInfo = new DoaNetworkInfo();
		osInfo = new DoaOSInfo();
		cpuInfo = new DoaCPUInfo();
		
		isInitialized = true;
	}
	
	@Getter private static DoaRuntimeInfo runtimeInfo;
	@Getter private static DoaBIOSInfo biosInfo;
	@Getter private static DoaDisplayInfo displayInfo;
	@Getter private static DoaGPUInfo gpuInfo;
	@Getter private static DoaMemoryInfo memoryInfo;
	@Getter private static DoaMOBOInfo motherboardInfo;
	@Getter private static DoaNetworkInfo networkInfo;
	@Getter private static DoaOSInfo osInfo;
	@Getter private static DoaCPUInfo cpuInfo;
}
