package Listener;

import java.awt.Color;

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
			String numberOfMobsKilled = event.getValue("numberModal").getAsString();
			String goldMesos = event.getValue("goldMesosModal").getAsString();
			String redMesos = event.getValue("redMesosModal").getAsString();
			String exp = event.getValue("expModal").getAsString();

//			event.reply("Sup, " + duration).queue();
			calculate(duration, numberOfMobsKilled, goldMesos, redMesos, exp);
			createEmbed(event);
		}
	}

	private void calculate(String duration, String numberOfMobsKilled, String goldMesos, String redMesos, String exp) {
		calculateTime(duration);
		calculateNumberOfMobsKilled(numberOfMobsKilled);

	}
	
	private void calculateNumberOfMobsKilled(String numberOfMobsKilled) {
		resultModel.setMobsKilledPerSec(numberOfMobsKilled/resultModel.getDurationInSeconds());
		
	}

	private void calculateTime(String duration) {
		String minuteSeconds[] = duration.split(":");
		int seconds;
		if(minuteSeconds.length > 1) {
			seconds = (Integer.parseInt(minuteSeconds[0]) * 60) + Integer.parseInt(minuteSeconds[1]);
		} else {
			seconds = (Integer.parseInt(minuteSeconds[0]) * 60);
		}
		resultModel.setExpPerSec(seconds);
	}

	private void createEmbed(ModalInteractionEvent event) {

		EmbedBuilder embedResult = new EmbedBuilder();

		MessageEmbed debugMessage;

		TextChannel farmLogChannel = event.getGuild().getTextChannelsByName("farm-log", true).get(0);

		embedResult.setAuthor("Result");

		embedResult.setColor(Color.GREEN);
		
		embedResult.addField("Duration : " + String.valueOf(resultModel.getExpPerSec()),"", true);
		
		event.deferReply().queue();
		
		event.getHook().sendMessageEmbeds(embedResult.build()).queue(message -> {
			embedMessageId = message.getIdLong();
//			message.addReaction(Emoji.fromFormatted(Emotes.MAIN)).queue();
//			message.addReaction(Emoji.fromFormatted(Emotes.FLEX)).queue();
//			message.addReaction(Emoji.fromFormatted(Emotes.DEFEND)).queue();
		});

		

	}

}
