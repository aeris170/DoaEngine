package doa.engine.utils.hardwareinfo;

import doa.engine.core.DoaEngine;
import doa.engine.core.DoaGame;

public final class DoaRuntimeInfo {
	
	public int getFPS() { return DoaGame.getInstance().getFPS(); }
	public int getTPS() { return DoaGame.getInstance().getTPS(); }
	public String getVersion() { return DoaEngine.VERSION; }
}
