package Main;

import javax.security.auth.login.LoginException;

import Commands.CommandManager;
import Listener.ABListener;
import Listener.ModalListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Main {
	// https://www.youtube.com/watch?v=jGrD8AZfTig
	public static JDA jda;
	public static String prefix = "-";

	// Main method
	public static void main(String[] args) throws LoginException, InterruptedException {
		System.out.println("Started MaplestoryM-Helper");

		JDA builder = JDABuilder
				.createDefault("MTA5OTk1NzgxMTQ0MzY4MzQwMA.GtDONf.q62otLQ2j-UjW-worQ1IaJgd4BEl4wScNovtZE")
				.enableIntents(GatewayIntent.MESSAGE_CONTENT).build().awaitReady();
		builder.getPresence().setStatus(OnlineStatus.IDLE);
		builder.getPresence().setActivity(Activity.competing("MaplestoryM"));
		builder.addEventListener(new CommandManager());
		builder.addEventListener(new ModalListener());
		builder.addEventListener(new ABListener());


		Guild Maple = builder.getGuildById("557904022523346945");
		if (Maple != null) {
			Maple.upsertCommand("calc", "calculate grind result").queue();
			Maple.upsertCommand("calcAB", "calculate minutes until expected end time").queue();
		}
	}
}
