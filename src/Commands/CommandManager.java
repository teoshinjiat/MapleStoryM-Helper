package Commands;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.imageio.ImageIO;

import org.jetbrains.annotations.NotNull;

import EmbedManager.Nodewar;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class CommandManager extends ListenerAdapter {
	public static SlashCommandInteractionEvent mainEvent;
	public static MessageEmbed debugMessage;

	public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
		this.setMainEvent(event);
		String command = event.getName();

		if (command.equals("nodewar_signup")) { // command / welcome
			String userTag = event.getUser().getAsTag();
			// event.reply("Creating nodewar template, **" + userTag + "**!").queue();
			System.out.println("nodewar_signup");
//			new Nodewar(event).buildEmbedMessage();
		} else if (command.equals("role_request")) { // command / welcome
			String userTag = event.getUser().getAsTag();
			System.out.println("role_request");
			new RoleManager(event).buildEmbedMessage();
		}
	}

	public void onMessageReceived(MessageReceivedEvent event) {
		System.out.println("MessageReceivedEvent()");

//		if(Long.valueOf(event.getMessageId()).equals(Nodewar.embedNodewarMessageId) && !user.isBot()) {
//		updateEmbedMessage();
//		System.out.println("User, " + user.getAsTag() + " removed emoji : " + emoji + " on message id : " + event.getMessageId());
//		Nodewar.attendanceModel.nameAndRole.remove(user.getAsMention());
//		System.out.println("hash size : " + Nodewar.attendanceModel.nameAndRole.size());
//	}

		if (!event.getAuthor().isBot()) {
//			event.getChannel().sendTyping();
//			event.getChannel().sendMessage("gg").queue();
			System.out.println("message : " + event.getMessage().getContentRaw());

			System.out.println("event.getMessage().getAttachments().size() : " + event.getMessage().getAttachments());
			System.out.println("event.getMessage().getEmbeds().size() : " + event.getMessage().getEmbeds().size());

			if (!event.getMessage().getAttachments().isEmpty()) {

				String contentType = event.getMessage().getAttachments().get(0).getContentType();
				System.out.println("contentType : " + contentType);

				List<Attachment> attachments = event.getMessage().getAttachments();
				System.out.println("attachments.get(0).getUrl() : " + attachments.get(0).getUrl());

				try (InputStream in = new URL(attachments.get(0).getUrl()).openStream()) {
					Files.copy(in, Paths.get("D:/Gonzo/Nodewar/scoreboard.png"), StandardCopyOption.REPLACE_EXISTING);
					ttt();
					ITesseract image = new Tesseract();
					String textFromImage = image.doOCR(new File("D:\\Gonzo\\Nodewar\\scoreboard.png"));
					//System.out.println("textFromImage : " + textFromImage);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TesseractException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				System.out.println("The message does not contain any image.");
			}

		}
	}
	
	 public static void ttt() throws Exception
	    {
	        File f
	            = new File(
	                "D:\\Gonzo\\Nodewar\\scoreboard.png");
	  
	        BufferedImage ipimage = ImageIO.read(f);
	  
	        // getting RGB content of the whole image file
	        double d
	            = ipimage
	                  .getRGB(ipimage.getTileWidth() / 2,
	                          ipimage.getTileHeight() / 2);
	  
	        // comparing the values
	        // and setting new scaling values
	        // that are later on used by RescaleOP
	        if (d >= -1.4211511E7 && d < -7254228) {
	            processImg(ipimage, 3f, -10f);
	        }
	        else if (d >= -7254228 && d < -2171170) {
	            processImg(ipimage, 1.455f, -47f);
	        }
	        else if (d >= -2171170 && d < -1907998) {
	            processImg(ipimage, 1.35f, -10f);
	        }
	        else if (d >= -1907998 && d < -257) {
	            processImg(ipimage, 1.19f, 0.5f);
	        }
	        else if (d >= -257 && d < -1) {
	            processImg(ipimage, 1f, 0.5f);
	        }
	        else if (d >= -1 && d < 2) {
	            processImg(ipimage, 1f, 0.35f);
	        }
	    }

	public static void processImg(BufferedImage ipimage, float scaleFactor, float offset)
			throws IOException, TesseractException {
		// Making an empty image buffer
		// to store image later
		// ipimage is an image buffer
		// of input image
		BufferedImage opimage = new BufferedImage(1050, 1024, ipimage.getType());

		// creating a 2D platform
		// on the buffer image
		// for drawing the new image
		Graphics2D graphic = opimage.createGraphics();

		// drawing new image starting from 0 0
		// of size 1050 x 1024 (zoomed images)
		// null is the ImageObserver class object
		graphic.drawImage(ipimage, 0, 0, 1050, 1024, null);
		graphic.dispose();

		// rescale OP object
		// for gray scaling images
		RescaleOp rescale = new RescaleOp(scaleFactor, offset, null);

		// performing scaling
		// and writing on a .png file
		BufferedImage fopimage = rescale.filter(opimage, null);
		ImageIO.write(fopimage, "jpg", new File("D:\\Gonzo\\Nodewar\\improved.png"));

		// Instantiating the Tesseract class
		// which is used to perform OCR
		Tesseract it = new Tesseract();

		it.setDatapath("D:\\Gonzo\\Nodewar");

		// doing OCR on the image
		// and storing result in string str
		String str = it.doOCR(fopimage);
		System.out.println(str);
	}

//	// Guild command -- instantly updated (max 100)
//	@Override
//	public void onGuildReady(@NotNull GuildReadyEvent event) {
//		List<CommandData> commandData = new ArrayList<>();
//		commandData.add(Commands.slash("nodewar_signup",
//				"Create Nodewar Sign-up template, follow by a message if you have a title for it").addOption(OptionType.STRING, "The title for the event", "leave empty if none", true));
//		event.getGuild().updateCommands().addCommands(commandData).queue();
//	}
//
//	public void onGuildJoin(@NotNull GuildReadyEvent event) {
//		List<CommandData> commandData = new ArrayList<>();
//		commandData.add(Commands.slash("nodewar_signup",
//				"Create Nodewar Sign-up template, follow by a message if you have a title for it").addOption(OptionType.STRING, "The title for the event", "leave empty if none", true));
//		event.getGuild().updateCommands().addCommands(commandData).queue();
//	}

	// Global command -- up to an hour to update
	// i dont need these

	public SlashCommandInteractionEvent getMainEvent() {
		return mainEvent;
	}

	public void setMainEvent(SlashCommandInteractionEvent mainEvent) {
		this.mainEvent = mainEvent;
	}

}
