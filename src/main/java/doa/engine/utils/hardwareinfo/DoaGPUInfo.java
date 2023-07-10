package doa.engine.utils.hardwareinfo;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import lombok.Getter;

public final class DoaGPUInfo {

	@Getter
	private ImmutableList<DoaGPU> graphicsCards;

	DoaGPUInfo() {
		List<org.jutils.jhardware.model.GraphicsCard> gpus = org.jutils.jhardware.HardwareInfo.getGraphicsCardInfo().getGraphicsCards();
		List<DoaGPU> doaGpus = new ArrayList<>();
		for(org.jutils.jhardware.model.GraphicsCard gpu : gpus) {
			doaGpus.add(new DoaGPU(gpu));
		}
		graphicsCards = ImmutableList.copyOf(doaGpus);
	}
}
