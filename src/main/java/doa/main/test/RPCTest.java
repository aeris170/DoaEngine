package doa.main.test;

import java.time.Instant;

import doa.engine.utils.discordapi.DoaDiscordActivity;
import doa.engine.utils.discordapi.DoaDiscordService;

public class RPCTest {

	public static void main(String[] args) {
		DoaDiscordService.init(750261216500383765L);

		DoaDiscordActivity act = new DoaDiscordActivity();
		act.name = "Pufin xD";
		act.description = "Testing DRPC";

		act.start = Instant.now();

		act.partySize = 1;
		act.partyMaxSize = 100;

		act.largeImageName = "puffin";
		act.largeImageText = "you are hovering over a puffin";

		act.smallImageName = "gull";
		act.smallImageText = "what did dat gull ever do to you?";

		DoaDiscordService.switchActivity(act);
		while (true);
	}
}
