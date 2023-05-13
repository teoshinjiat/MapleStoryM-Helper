package Listener;

import java.awt.Color;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.Locale;

import Model.ResultModel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ModalListener extends ListenerAdapter {
	public static long embedMessageId;
	private static ResultModel resultModel = new ResultModel();

	@Override
	public void onModalInteraction(ModalInteractionEvent event) {
		// TODO Auto-generated method stub
		if (event.getModalId().equals("grind-modal")) {
			String duration = event.getValue("durationModal").getAsString();
			double numberOfMobsKilled = Long.valueOf(event.getValue("numberModal").getAsString().replaceAll(",", ""));
			long goldMesos = Long.valueOf(event.getValue("goldMesosModal").getAsString().replaceAll(",", ""));
			long redMesos = Long.valueOf(event.getValue("redMesosModal").getAsString().replaceAll(",", ""));
			long exp = Long.valueOf(event.getValue("expModal").getAsString().replaceAll(",", ""));
			// display input for debug
			System.out.println("duration : " + duration);
			System.out.println("numberOfMobsKilled : " + numberOfMobsKilled);
			System.out.println("goldMesos : " + goldMesos);
			System.out.println("redMesos : " + redMesos);
			System.out.println("exp : " + exp);

//			event.reply("Sup, " + duration).queue();
			calculate(event, duration, numberOfMobsKilled, goldMesos, redMesos, exp);
		}
	}

	private void calculate(ModalInteractionEvent event, String duration, double numberOfMobsKilled, long goldMesos,
			long redMesos, long exp) {
		resultModel.setDurationInSeconds(calculateTime(duration));
		resultModel.setMobsKilledPerSec(calculateNumberOfMobsKilled(numberOfMobsKilled));
		resultModel.setGoldMesosPerSec(calculateGoldMesos(goldMesos));
		resultModel.setRedMesosPerSec(calculateRedMesos(redMesos));
		resultModel.setExpPerSec(calculateExp(exp));

		createEmbed(event, resultModel);
	}

	private long calculateExp(long exp) {
		// TODO Auto-generated method stub
		return (exp / resultModel.getDurationInSeconds());
	}

	private long calculateRedMesos(long redMesos) {
		// TODO Auto-generated method stub
		return (redMesos / resultModel.getDurationInSeconds());
	}

	private long calculateGoldMesos(long goldMesos) {
		// TODO Auto-generated method stub
		return (goldMesos / resultModel.getDurationInSeconds());
	}

	private double calculateNumberOfMobsKilled(double numberOfMobsKilled) {
		return (numberOfMobsKilled / resultModel.getDurationInSeconds());
	}

	private int calculateTime(String duration) {
		String minuteSeconds[] = duration.split(":");
		System.out.println("minuteSeconds[0] : " + minuteSeconds[0]);

		int seconds;
		if (minuteSeconds.length > 1) {
			seconds = (Integer.parseInt(minuteSeconds[0]) * 60) + Integer.parseInt(minuteSeconds[1]);
		} else {
			seconds = (Integer.parseInt(minuteSeconds[0]) * 60);
		}
		System.out.println("calc seconds : " + seconds);
		return seconds;
	}

	private void createEmbed(ModalInteractionEvent event, ResultModel resultModel2) {

		EmbedBuilder embedResult = new EmbedBuilder();

		MessageEmbed debugMessage;

		TextChannel farmLogChannel = event.getGuild().getTextChannelsByName("farm-log", true).get(0);

		NumberFormat nf = NumberFormat.getInstance(Locale.US);

		Path path = Paths.get("/resources/EXP.png");

		File file = path.toFile();

		System.out.println("file:" + file);

		embedResult.setAuthor("Result");

		embedResult.setColor(Color.GREEN);

		// mobs
		embedResult.addField("Mobs Killed Per/s : " + String.valueOf(nf.format(resultModel.getMobsKilledPerSec())), "",
				true);
		embedResult.addField("", "", true);
		embedResult.addField(
				"Mobs Killed Per/h : " + String.valueOf(nf.format((int) (resultModel.getMobsKilledPerSec() * 60 * 60))),
				"", true);

		// exp
		embedResult.addField("EXP/s : " + String.valueOf(nf.format(resultModel.getExpPerSec())), "", true);
		embedResult.addField("", "", true);
		embedResult.addField("EXP/h : " + String.valueOf(nf.format(resultModel.getExpPerSec() * 60 * 60)), "", true);

		// gold mesos
		embedResult.addField("Gold Mesos/s : " + String.valueOf(nf.format(resultModel.getGoldMesosPerSec())), "", true);
		embedResult.addField("", "", true);
		embedResult.addField("Gold Mesos/h : " + String.valueOf(nf.format(resultModel.getGoldMesosPerSec() * 60 * 60)),
				"", true);

		// red mesos
		embedResult.addField("Red Mesos/s : " + String.valueOf(nf.format(resultModel.getRedMesosPerSec())), "", true);
		embedResult.addField("", "", true);
		embedResult.addField("Red Mesos/h : " + String.valueOf(nf.format(resultModel.getRedMesosPerSec() * 60 * 60)),
				"", true);

		// total mesos
		embedResult.addField(
				"Total Mesos/s : "
						+ String.valueOf(nf.format(resultModel.getGoldMesosPerSec() + resultModel.getRedMesosPerSec())),
				"", true);
		embedResult.addField("", "", true);
		embedResult.addField(
				"Total Mesos/h : " + String.valueOf(
						nf.format((resultModel.getGoldMesosPerSec() + resultModel.getRedMesosPerSec()) * 60 * 60)),
				"", true);

		event.deferReply().queue();

		event.getHook().sendMessageEmbeds(embedResult.build()).queue(message -> {
			embedMessageId = message.getIdLong();
		});

	}

}
