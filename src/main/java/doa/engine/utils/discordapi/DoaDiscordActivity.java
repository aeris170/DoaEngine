package doa.engine.utils.discordapi;

import java.time.Instant;

/**
 * Please see Discord API documentation for more info.
 * 
 * @author Doga Oruc
 * @since DoaEngine 3.0
 * @version 3.0
 */
public class DoaDiscordActivity {

	public Instant start = null;
	public Instant end = null;

	public String name = null;
	public String description = null;

	public String partyID = null;
	public int partySize = -1;
	public int partyMaxSize = -1;

	public String largeImageName = null;
	public String largeImageText = null;
	public String smallImageName = null;
	public String smallImageText = null;

	public String matchSecret = null;
	public String joinSecret = null;
	public String spectateSecret = null;
}
