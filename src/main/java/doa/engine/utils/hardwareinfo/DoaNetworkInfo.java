package doa.engine.utils.hardwareinfo;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import lombok.Getter;

public final class DoaNetworkInfo {

	@Getter
	private ImmutableList<DoaNetworkInterface> networkInterfaces;
	
	DoaNetworkInfo() {
		List<org.jutils.jhardware.model.NetworkInterfaceInfo> networks = org.jutils.jhardware.HardwareInfo.getNetworkInfo().getNetworkInterfaces();
		List<DoaNetworkInterface> doaNetworks = new ArrayList<>();
		for(org.jutils.jhardware.model.NetworkInterfaceInfo n : networks) {
			doaNetworks.add(new DoaNetworkInterface(n));
		}
		networkInterfaces = ImmutableList.copyOf(doaNetworks);
	}
}
