package doa.main;

import java.time.Instant;

import doa.engine.utils.discordapi.DoaDiscordActivity;
import doa.engine.utils.discordapi.DoaDiscordService;

public class RPCTest {

	public static void main(String[] args) {
		DoaDiscordService.init(750261216500383765L);

		DoaDiscordActivity act = new DoaDiscordActivity();
		act.setName("Pufin xD");
		act.setDescription("Testing DRPC");

		act.setStart(Instant.now());

		act.setPartySize(1);
		act.setPartyMaxSize(100);

		act.setLargeImageName("puffin");
		act.setLargeImageText("you are hovering over a puffin");

		act.setSmallImageName("gull");
		act.setSmallImageText("what did dat gull ever do to you?");

		DoaDiscordService.switchActivity(act);
		while (true);
	}
}
