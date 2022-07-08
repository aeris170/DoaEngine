package doa.engine.utils.discordapi;

import java.time.Instant;

import lombok.Data;
import lombok.ToString;

/**
 * Please see Discord API documentation for more info.
 * 
 * @author Doga Oruc
 * @since DoaEngine 3.0
 * @version 3.0
 */
@Data
@ToString(includeFieldNames=true)
public class DoaDiscordActivity {

	Instant start = null;
	Instant end = null;

	String name = null;
	String description = null;

	String partyID = null;
	int partySize = -1;
	int partyMaxSize = -1;

	String largeImageName = null;
	String largeImageText = null;
	String smallImageName = null;
	String smallImageText = null;

	String matchSecret = null;
	String joinSecret = null;
	String spectateSecret = null;
}
