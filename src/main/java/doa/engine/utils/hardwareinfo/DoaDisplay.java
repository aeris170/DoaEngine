package doa.engine.utils.hardwareinfo;

public final class DoaDisplay {

	public String getName() { return d.getName(); }
	public String getCurrentResolution() { return d.getCurrentResolution(); }
	public String getRefreshRate() { return d.getRefreshRate(); }
	public String[] getSupportedResolutions() { return d.getSupportedResolutions(); }
	
	private org.jutils.jhardware.model.Display d;
	DoaDisplay(org.jutils.jhardware.model.Display d) { this.d = d; }
}
