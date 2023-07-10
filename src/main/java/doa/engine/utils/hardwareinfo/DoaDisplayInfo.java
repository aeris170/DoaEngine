package doa.engine.utils.hardwareinfo;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import lombok.Getter;

public final class DoaDisplayInfo {

	@Getter
	private ImmutableList<DoaDisplay> displayDevices;

	DoaDisplayInfo() {
		List<org.jutils.jhardware.model.Display> displays = org.jutils.jhardware.HardwareInfo.getDisplayInfo().getDisplayDevices();
		List<DoaDisplay> doaDisplays = new ArrayList<>();
		for(org.jutils.jhardware.model.Display display : displays) {
			doaDisplays.add(new DoaDisplay(display));
		}
		displayDevices = ImmutableList.copyOf(doaDisplays);
	}
}
