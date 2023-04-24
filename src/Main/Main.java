package Main;

import javax.security.auth.login.LoginException;

import Commands.CommandManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
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
		builder.getPresence().setActivity(Activity.competing("Nodewar"));
		builder.addEventListener(new CommandManager());

//		Guild Gonzo = builder.getGuildById("557904022523346945");
//		if (Gonzo != null) {
//			Gonzo.upsertCommand("nodewar_signup",
//					"Create Nodewar Sign-up template, hit \"Spacebar\" then follow by a message if you have a title for it.")
//					.addOption(OptionType.STRING, "title", "leave empty if none", false).queue();
//			Gonzo.upsertCommand("role_request", "Create get roles template").queue();
//		}
	}
}
