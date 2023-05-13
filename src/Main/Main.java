package Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.security.auth.login.LoginException;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import Commands.CommandManager;
import Listener.ABListener;
import Listener.ModalListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Main {
	// https://www.youtube.com/watch?v=jGrD8AZfTig
	public static JDA jda;
	public static String prefix = "-";

	// Main method
	public static void main(String[] args) throws LoginException, InterruptedException, IOException {
		System.out.println("Started MaplestoryM-Helper");

		JDA builder = JDABuilder.createDefault(getBotTokenFromConfig()).enableIntents(GatewayIntent.MESSAGE_CONTENT)
				.build().awaitReady();
		builder.getPresence().setStatus(OnlineStatus.IDLE);
		builder.getPresence().setActivity(Activity.playing("MaplestoryM"));
		builder.addEventListener(new CommandManager());
		builder.addEventListener(new ModalListener());
		builder.addEventListener(new ABListener());

		Guild Maple = builder.getGuildById("1103529354107031622");
		if (Maple != null) {
			Maple.upsertCommand("grind_calc", "calculate grind result").queue();
			Maple.upsertCommand("ab_calc", "calculate minutes until expected end time").addOption(OptionType.STRING,
					"time", "Time in 24H Format, 2PM = 14:00 | 12AM = 00:00 | 2AM = 02:00", true).queue();
			Maple.upsertCommand("package_calc", "calculate which growth package has the most value").queue();
		}
	}

	private static String getBotTokenFromConfig() throws IOException {
		String filePath = System.getProperty("user.dir") + "\\src\\config.json";
		File f = new File(filePath);
		String token = null;
		if (f.exists()) {
			InputStream is = new FileInputStream(filePath);
			String jsonTxt = IOUtils.toString(is, "UTF-8");
			JSONObject json = new JSONObject(jsonTxt);
			token = json.getString("token");
		}
		return token;
	}
}
