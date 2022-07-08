package doa.engine.utils.discordapi;

import java.io.File;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.activity.Activity;
import doa.engine.utils.DoaUtils;

/**
 * Please see Discord API documentation for more info.
 * 
 * @author Doga Oruc
 * @since DoaEngine 3.0
 * @version 3.0
 */
public final class DoaDiscordService {

	private static Core Core;

	private static DoaDiscordActivity Activity;

	private DoaDiscordService() {}

	public static void init(long ID) {
		de.jcm.discordgamesdk.Core.init(new File("src/main/resources/discord_game_sdk.dll"));
		CreateParams Params = new CreateParams();
		Params.setClientID(ID);
		Params.setFlags(CreateParams.getDefaultFlags());
		Core = new Core(Params);

		new Thread(() -> {
			while (true) {
				Core.runCallbacks();
				DoaUtils.sleepFor(50);
			}
		}, "DoaEngine Discord Rich Presence Service").start();
	}

	public static void switchActivity(DoaDiscordActivity activity) {
		Activity = activity;

		Activity nativeActivity = new Activity();
		if (activity.name != null)
			nativeActivity.setDetails(activity.name);
		if (activity.description != null)
			nativeActivity.setState(activity.description);

		if (activity.start != null)
			nativeActivity.timestamps().setStart(activity.start);
		if (activity.end != null)
			nativeActivity.timestamps().setEnd(activity.end);

		if (activity.partyID != null)
			nativeActivity.party().setID(activity.partyID);
		if (activity.partySize != -1)
			nativeActivity.party().size().setCurrentSize(activity.partySize);
		if (activity.partyMaxSize != -1)
			nativeActivity.party().size().setMaxSize(activity.partyMaxSize);

		if (activity.largeImageText != null)
			nativeActivity.assets().setLargeText(activity.largeImageText);
		if (activity.largeImageName != null)
			nativeActivity.assets().setLargeImage(activity.largeImageName);
		if (activity.smallImageText != null)
			nativeActivity.assets().setSmallText(activity.smallImageText);
		if (activity.smallImageName != null)
			nativeActivity.assets().setSmallImage(activity.smallImageName);

		if (activity.matchSecret != null)
			nativeActivity.secrets().setMatchSecret(activity.matchSecret);
		if (activity.joinSecret != null)
			nativeActivity.secrets().setJoinSecret(activity.joinSecret);
		if (activity.spectateSecret != null)
			nativeActivity.secrets().setSpectateSecret(activity.spectateSecret);

		Core.activityManager().updateActivity(nativeActivity);
	}

	public static DoaDiscordActivity getCurrentActivity() { return Activity; }
}
