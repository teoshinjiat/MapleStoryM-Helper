package Commands;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.jetbrains.annotations.NotNull;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.ImageHelper;
import nu.pattern.OpenCV;

public class CommandManager extends ListenerAdapter {
	public static SlashCommandInteractionEvent mainEvent;
	public static MessageEmbed debugMessage;

	public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
		String command = event.getName();
		if (!event.getGuild().getId().equals("1103529354107031622")) {
			event.getChannel().sendMessage("Invalid server, please join Stormwind guild to use the command.");
		} else {
			switch (command) {
			case "grind_calc":
				grindCalc(event);
			case "ab_calc":
				ab_calc(event);
			}
		}
	}

	private void ab_calc(@NotNull SlashCommandInteractionEvent event) {
		OptionMapping messageOption = event.getOption("time");
		String endTime = messageOption.getAsString();

		if (!isValidTime(endTime) || endTime.length() != 5) {
			event.reply("Invalid Input! **" + endTime + "** is not a valid 24 hour format.").setEphemeral(false)
					.queue(); // setEphemeral(true) only the user who called the function can see the error
			
		} else {
			event.reply("You need **" + calcTime(endTime) + "** minutes to auto battle until **" + endTime + "**")
					.setEphemeral(false).queue();
		}
	}

	private String calcTime(String endTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String current24HourTime = sdf.format(new Date());
		System.out.println("current24HourTime : " + current24HourTime);

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime startLocalTime = LocalTime.parse(current24HourTime, dtf);
		LocalTime endLocalTime = LocalTime.parse(endTime, dtf);

		long diff = ChronoUnit.MINUTES.between(startLocalTime, endLocalTime); // get diff in minutes

		if (endLocalTime.isBefore(startLocalTime)) {
			diff += TimeUnit.DAYS.toMinutes(1); // add a day to account for day diff
		}

		return String.valueOf(diff);
	}

	private boolean isValidTime(String userInput) {
		// https://www.geeksforgeeks.org/how-to-validate-time-in-24-hour-format-using-regular-expression/
		String regex = "([01]?[0-9]|2[0-3]):[0-5][0-9]";

		Pattern p = Pattern.compile(regex);
		System.out.println("p.matcher(userInput).matches() : " + p.matcher(userInput).matches());
		return p.matcher(userInput).matches();
	}

	public void grindCalc(@NotNull SlashCommandInteractionEvent event) {
		TextInput duration = TextInput.create("durationModal", "Duration in MM:SS format", TextInputStyle.SHORT)
				.setMinLength(1).setRequired(true).setPlaceholder("360:00").build();

		TextInput numberOfMobsKilled = TextInput.create("numberModal", "Number of Mobs Killed", TextInputStyle.SHORT)
				.setMinLength(1).setRequired(false).setPlaceholder("1258, can be empty").build();

		TextInput goldMesos = TextInput.create("goldMesosModal", "Gold Mesos", TextInputStyle.SHORT).setMinLength(1)
				.setRequired(true).setPlaceholder("32154632 or 32,154,632").build();

		TextInput redMesos = TextInput.create("redMesosModal", "Red Mesos", TextInputStyle.SHORT).setMinLength(1)
				.setRequired(true).setPlaceholder("32154632 or 32,154,632").build();

		TextInput exp = TextInput.create("expModal", "EXP", TextInputStyle.SHORT).setMinLength(1).setRequired(false)
				.setPlaceholder("32154632 or 32,154,632").build();

		Modal modal = Modal.create("grind-modal", "Grind Calculator").addActionRows(ActionRow.of(duration),
				ActionRow.of(numberOfMobsKilled), ActionRow.of(goldMesos), ActionRow.of(redMesos), ActionRow.of(exp))
				.build();

		event.replyModal(modal).queue();
	}

	public void onMessageReceived(MessageReceivedEvent event) {
		if (!event.getAuthor().isBot()) {
			if (!event.getMessage().getAttachments().isEmpty()) {

				String contentType = event.getMessage().getAttachments().get(0).getContentType();
				System.out.println("contentType : " + contentType);

				List<Attachment> attachments = event.getMessage().getAttachments();
				System.out.println("attachments.get(0).getUrl() : " + attachments.get(0).getUrl());

				try (InputStream in = new URL(attachments.get(0).getUrl()).openStream()) {
					Files.copy(in, Paths.get("D:/MaplestoryM/AB/AB.png"), StandardCopyOption.REPLACE_EXISTING);
					imageHandling();
					ITesseract image = new Tesseract();
					String textFromImage = image.doOCR(new File("D:\\MaplestoryM\\AB\\AB.png"));
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

	public static void imageHandling() throws Exception {
		File f = new File("D:\\MaplestoryM\\AB\\AB.png");

		BufferedImage ipimage = ImageIO.read(f);

		// getting RGB content of the whole image file
		double d = ipimage.getRGB(ipimage.getTileWidth() / 2, ipimage.getTileHeight() / 2);

		// comparing the values
		// and setting new scaling values
		// that are later on used by RescaleOP
		if (d >= -1.4211511E7 && d < -7254228) {
			processImg(ipimage, 3f, -10f);
		} else if (d >= -7254228 && d < -2171170) {
			processImg(ipimage, 1.455f, -47f);
		} else if (d >= -2171170 && d < -1907998) {
			processImg(ipimage, 1.35f, -10f);
		} else if (d >= -1907998 && d < -257) {
			processImg(ipimage, 1.19f, 0.5f);
		} else if (d >= -257 && d < -1) {
			processImg(ipimage, 1f, 0.5f);
		} else if (d >= -1 && d < 2) {
			processImg(ipimage, 1f, 0.35f);
		}
	}

	public static void processImg(BufferedImage ipimage, float scaleFactor, float offset)
			throws IOException, TesseractException {

		// rescale OP object
		// for gray scaling images
		Tesseract it = new Tesseract();
		it.setTessVariable("tessedit_char_whitelist", "0123456789:,"); // detect number only

		String convertImageToBinary = it.doOCR(ImageHelper.convertImageToBinary(ipimage)); // greyscale to fix red
																							// coloured text not
		String splits[] = convertImageToBinary.split("\\n");

		ArrayList<String> importantInfo = new ArrayList<>();
		for (String split : splits) {
			System.out.println("split : " + split);
			String splitsWithoutComma[] = convertImageToBinary.split(",");
		}

		it.setDatapath("D:\\MaplestoryM\\AB");

		nu.pattern.OpenCV.loadLocally(); // add this

		OpenCV cv = new OpenCV();

		// https://www.tutorialspoint.com/reading-a-colored-image-as-grey-scale-using-java-opencv-library
		Mat gray = Imgcodecs.imread("D:\\MaplestoryM\\AB\\AB.png", Imgcodecs.IMREAD_GRAYSCALE);
		Mat resizedMat = new Mat();
		double width = gray.cols();
		double height = gray.rows();
		double aspect = width / height;
		Size sz = new Size(width * aspect * 2, height * aspect * 2);
		Imgproc.resize(gray, resizedMat, sz);

		Imgcodecs.imwrite("D:\\MaplestoryM\\AB\\AB_gray.png", gray);

		BufferedImage topimage = Mat2BufferedImage(gray);
		String topimageString = it.doOCR(topimage); // greyscale to fix red coloured text not

		ImageIO.write(thresholdImage(ipimage, 128), "jpg", new File("D:\\MaplestoryM\\AB\\AB_thresholdImage.png"));
	}

	// https://www.tutorialspoint.com/how-to-convert-opencv-mat-object-to-bufferedimage-object-using-java
	public static BufferedImage Mat2BufferedImage(Mat mat) throws IOException {
		// Encoding the image
		MatOfByte matOfByte = new MatOfByte();
		Imgcodecs.imencode(".jpg", mat, matOfByte);
		// Storing the encoded Mat in a byte array
		byte[] byteArray = matOfByte.toArray();
		// Preparing the Buffered Image
		InputStream in = new ByteArrayInputStream(byteArray);
		BufferedImage bufImage = ImageIO.read(in);
		return bufImage;
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

	// https://stackoverflow.com/a/17542851
	public static BufferedImage thresholdImage(BufferedImage image, int threshold) {
		BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		result.getGraphics().drawImage(image, 0, 0, null);
		WritableRaster raster = result.getRaster();
		int[] pixels = new int[image.getWidth()];
		for (int y = 0; y < image.getHeight(); y++) {
			raster.getPixels(0, y, image.getWidth(), 1, pixels);
			for (int i = 0; i < pixels.length; i++) {
				if (pixels[i] < threshold)
					pixels[i] = 0;
				else
					pixels[i] = 255;
			}
			raster.setPixels(0, y, image.getWidth(), 1, pixels);
		}
		return result;
	}

}
