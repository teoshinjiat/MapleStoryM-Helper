package Commands;

import java.awt.Color;

import Service.ReactionService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

public class RoleManager {
//	@Override
//	public void onMessageReceived(MessageReceivedEvent event) {
//		
//	}

	private SlashCommandInteractionEvent event;
	public static long embedNodewarMessageId;
	public static long nodewarMessage;

	public RoleManager(SlashCommandInteractionEvent slashEvent) {
		event = slashEvent;
	}

	public void main(String[] args) {
	}

	public void buildEmbedMessage() {
		System.out.println("start buildEmbedMessage()");

		EmbedBuilder embedNodewar = new EmbedBuilder();
		TextChannel nodewarChannel = event.getGuild().getTextChannelsByName("nodewar", true).get(0);

		embedNodewar.setAuthor("Change Nodewar class");

		String roleId = "1015543762363105310";

		embedNodewar.setTitle("title here");

		embedNodewar.setColor(Color.RED);
//		embedDebug.setFooter("You can change your class in #change-class channel if you signed up with the wrong class");
		embedNodewar.setFooter("Initiated by " + event.getMember().getUser().getAsTag(),
				event.getMember().getUser().getAvatarUrl());

		embedNodewar.addField("⚔️ : Main Team", "", false);
		embedNodewar.addField("<:flex:1015502494576672778> : " + "Flex Team", "", false);
		embedNodewar.addField("🛡️ : Defense  Team", "", false);

		// new EmbedModel().setDebugMessage(embedDebug.build());
//		event.getChannel().sendMessage(embedDebug.build()).queue();
//
//		nodewarChannel.sendMessage(debugMessage).queue((message) -> {
//			embedDebugMessageId = message.getIdLong();
//		});

		MessageReactionAddEvent addEvent;

		// addEvent.getEmoji();
		event.deferReply().queue();
//		event.getHook().sendMessageEmbeds(embedDebug.build()).queue(message -> {
//			  message.addReaction(Emoji.fromCustom(":slavewhip:973497352348368927")).queue();
//			  message.addReaction(Emoji.fromFormatted(":slavewhip:973497352348368927")).queue();
//			  message.addReaction(Emoji.fromFormatted("slavewhip:973497352348368927")).queue();
//			});

//		long emoteId = Long.parseLong("973497352348368927");
//		event.getHook().sendMessageEmbeds(embedNodewar.build()).queue(message -> {
//			message.addReaction(Emoji.fromFormatted("⚔️")).queue();
//			message.addReaction(Emoji.fromFormatted("💪")).queue();
//			message.addReaction(Emoji.fromFormatted("🛡️")).queue();
//		});
		listenForReactionsThread.start(); // thread for periodically update the embed message in discord

		long emoteId = Long.parseLong("973497352348368927");
		event.getHook().sendMessageEmbeds(embedNodewar.build()).queue(message -> {
			embedNodewarMessageId = message.getIdLong();
			message.addReaction(Emoji.fromFormatted("⚔️")).queue();
			message.addReaction(Emoji.fromFormatted("<:flex:1015502494576672778>")).queue();
			message.addReaction(Emoji.fromFormatted("🛡️")).queue();
			nodewarMessage = message.getIdLong();
		});

//		event.getHook().editOriginal(null)

		System.out.println("end buildEmbedMessage()");
	}

	// run periodically
	Thread listenForReactionsThread = new Thread() {
		public void run() {
			System.out.println("listenForReactionsThread()");
			while (true) {
				System.out.println("listenForReactionsThread()");

				ifNumberOfReactionsChanged();
//				if(ifNumberOfReactionsChanged()) { // if role changed.
//					// update embed
//				}
				try {
					Thread.sleep(2000); // 2 seconds interval to check for changes
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};

	private void ifNumberOfReactionsChanged() {
		System.out.println("ifNumberOfReactionsChanged()");
//		new ReactionService().checkForReactions(event);
	}
}
