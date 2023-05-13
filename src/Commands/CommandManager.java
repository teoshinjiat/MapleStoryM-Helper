package Commands;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.jetbrains.annotations.NotNull;

import Model.ResultModel;
import net.dv8tion.jda.api.EmbedBuilder;
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

public class CommandManager extends ListenerAdapter {
	public static SlashCommandInteractionEvent mainEvent;
	public static MessageEmbed debugMessage;
	private static ResultModel resultModel = new ResultModel();

	public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
		String command = event.getName();
		if (!event.getGuild().getId().equals("1103529354107031622")) {
			event.getChannel().sendMessage("Invalid server, please join Stormwind guild to use the command.");
		} else {
			switch (command) {
			case "grind_calc":
				grindCalc(event);
			case "ab_calc":
				abCalc(event);
			case "package_calc":
				packageCalc(event);
			}
		}
	}

	private void packageCalc(@NotNull SlashCommandInteractionEvent event) {
		TextInput crystal = TextInput.create("durationModal", "Duration in MM:SS format", TextInputStyle.PARAGRAPH)
				.setMinLength(1).setMaxLength(400).setRequired(true).setPlaceholder("5800|120\n4800|80").build();

		Modal modal = Modal.create("grind-modal", "Package Calculator").addActionRows(ActionRow.of(crystal)).build();
		event.replyModal(modal).queue();

	}

	private void abCalc(@NotNull SlashCommandInteractionEvent event) {
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

				Long test = Long.valueOf("2675418");
				System.out.println("test:" + test);
				String contentType = event.getMessage().getAttachments().get(0).getContentType();
				System.out.println("contentType : " + contentType);

				List<Attachment> attachments = event.getMessage().getAttachments();
				System.out.println("attachments.get(0).getUrl() : " + attachments.get(0).getUrl());

				try (InputStream in = new URL(attachments.get(0).getUrl()).openStream()) {

					System.out.println("in : " + in);

					Files.copy(in, Paths.get("D:/MaplestoryM/AB/AB.png"), StandardCopyOption.REPLACE_EXISTING);

					BufferedImage img = ImageIO.read(new File("D:/MaplestoryM/AB/AB.png"));
					ImageIO.write(img, "png", new File("image.jpg"));

					// imageHandling();
					ITesseract image = new Tesseract();
//					image.setPageSegMode(7);
					image.setLanguage("eng");
					image.setOcrEngineMode(0);

					String textFromImage = image.doOCR(new File("D:\\MaplestoryM\\AB\\AB.png"));
					System.out.println("new \n : " + textFromImage);

					File imageFile = new File("D:\\MaplestoryM\\AB\\AB.png");
					BufferedImage bufferedImage = null;
					try {
						bufferedImage = ImageIO.read(imageFile);
						BufferedImage image2 = bufferedImage.getSubimage(1210, 425, 150, 50);
						File pathFile = new File("D:\\MaplestoryM\\AB\\time.png");
						ImageIO.write(image2, "png", pathFile);
						String time = image.doOCR(new File("D:\\MaplestoryM\\AB\\time.png"));
						System.out.println("time: " + time);
						resultModel.calculateTime(time.trim());

						image2 = bufferedImage.getSubimage(1465, 420, 180, 50);
						pathFile = new File("D:\\MaplestoryM\\AB\\mobs_killed.png");
						ImageIO.write(image2, "png", pathFile);
						String mobs_killed = image.doOCR(new File("D:\\MaplestoryM\\AB\\mobs_killed.png"));
						System.out.println("mobs_killed: " + mobs_killed);
						resultModel.calculateNumberOfMobsKilled(mobs_killed.trim());

						image2 = bufferedImage.getSubimage(1210, 500, 500, 50);
						pathFile = new File("D:\\MaplestoryM\\AB\\gold_mesos.png");
						ImageIO.write(image2, "png", pathFile);
						String gold_mesos = image.doOCR(new File("D:\\MaplestoryM\\AB\\gold_mesos.png"));
						System.out.println("gold_mesos: " + gold_mesos);
						resultModel.calculateGoldMesos(gold_mesos.trim());

						image2 = bufferedImage.getSubimage(1210, 570, 500, 50);
						pathFile = new File("D:\\MaplestoryM\\AB\\red_mesos.png");
						ImageIO.write(image2, "png", pathFile);
						String red_mesos = image.doOCR(new File("D:\\MaplestoryM\\AB\\red_mesos.png"));
						System.out.println("red_mesos: " + red_mesos);
						resultModel.calculateRedMesos(red_mesos.trim());

						image2 = bufferedImage.getSubimage(1250, 650, 400, 60);
						pathFile = new File("D:\\MaplestoryM\\AB\\exp.png");
						ImageIO.write(image2, "png", pathFile);
						String exp = image.doOCR(new File("D:\\MaplestoryM\\AB\\exp.png"));
						System.out.println("exp: " + exp);
						resultModel.calculateExp(exp.trim());

						sendEmbed(event);
					} catch (IOException e) {
						System.out.println(e);
					}

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

	private void sendEmbed(MessageReceivedEvent event) {
		EmbedBuilder embedResult = new EmbedBuilder();
		NumberFormat nf = NumberFormat.getInstance(Locale.US);

		embedResult.setAuthor("Result");

		embedResult.setColor(Color.GREEN);

		// mobs
		embedResult.addField("<:mobs_killed:1107015902882443325>Mobs Killed Per/s : " + String.valueOf(nf.format(resultModel.getMobsKilledPerSec())), "",
				true);
		embedResult.addField("", "", true);
		embedResult.addField(
				"<:mobs_killed:1107015902882443325>Mobs Killed Per/h : " + String.valueOf(nf.format((int) (resultModel.getMobsKilledPerSec() * 60 * 60))),
				"", true);

		// exp
		embedResult.addField("<:exp:1107016139617337384>EXP/s : " + String.valueOf(nf.format(resultModel.getExpPerSec())), "", true);
		embedResult.addField("", "", true);
		embedResult.addField("<:exp:1107016139617337384>EXP/h : " + String.valueOf(nf.format(resultModel.getExpPerSec() * 60 * 60)), "", true);

		// gold mesos
		embedResult.addField("<:gold_mesos:1107015907257110629>Gold Mesos/s : " + String.valueOf(nf.format(resultModel.getGoldMesosPerSec())), "", true);
		embedResult.addField("", "", true);
		embedResult.addField("<:gold_mesos:1107015907257110629>Gold Mesos/h : " + String.valueOf(nf.format(resultModel.getGoldMesosPerSec() * 60 * 60)),
				"", true);

		// red mesos
		embedResult.addField("<:red_mesos:1107015904568549386>Red Mesos/s : "
				+ String.valueOf(nf.format(resultModel.getRedMesosPerSec())), "", true);
		embedResult.addField("", "", true);
		embedResult.addField("<:red_mesos:1107015904568549386>Red Mesos/h : "
				+ String.valueOf(nf.format(resultModel.getRedMesosPerSec() * 60 * 60)), "", true);

		// total mesos
		embedResult.addField(
				"<:total_mesos:1107016711946903603>Total Mesos/s : "
						+ String.valueOf(nf.format(resultModel.getGoldMesosPerSec() + resultModel.getRedMesosPerSec())),
				"", true);
		embedResult.addField("", "", true);
		embedResult.addField(
				" <:total_mesos:1107016711946903603>Total Mesos/h : " + String.valueOf(
						nf.format((resultModel.getGoldMesosPerSec() + resultModel.getRedMesosPerSec()) * 60 * 60)),
				"", true);

//		event.deferReply().queue();

		event.getChannel().sendMessageEmbeds(embedResult.build()).queue();
	}

//	public static void imageHandling() throws Exception {
//		File f = new File("D:\\MaplestoryM\\AB\\AB.png");
//
//		BufferedImage ipimage = ImageIO.read(f);
//
////		ipimage = ImageHelper.convertImageToBinary(ipimage);
////		
////		ITesseract image = new Tesseract();
//////		image.setPageSegMode(7);
////		image.setLanguage("eng");
////		image.setOcrEngineMode(0);
////
////		String textFromImage = image.doOCR(ipimage);
////		System.out.println("imageHandling() : " + textFromImage);
//
//		// getting RGB content of the whole image file
//		double d = ipimage.getRGB(ipimage.getTileWidth() / 2, ipimage.getTileHeight() / 2);
//
//		// comparing the values
//		// and setting new scaling values
//		// that are later on used by RescaleOP
//		if (d >= -1.4211511E7 && d < -7254228) {
//			processImg(ipimage, 3f, -10f);
//		} else if (d >= -7254228 && d < -2171170) {
//			processImg(ipimage, 1.455f, -47f);
//		} else if (d >= -2171170 && d < -1907998) {
//			processImg(ipimage, 1.35f, -10f);
//		} else if (d >= -1907998 && d < -257) {
//			processImg(ipimage, 1.19f, 0.5f);
//		} else if (d >= -257 && d < -1) {
//			processImg(ipimage, 1f, 0.5f);
//		} else if (d >= -1 && d < 2) {
//			processImg(ipimage, 1f, 0.35f);
//		}
//	}

//	public static void processImg(BufferedImage ipimage, float scaleFactor, float offset)
//			throws IOException, TesseractException {
//
//		// rescale OP object
//		// for gray scaling images
//		Tesseract it = new Tesseract();
//		it.setTessVariable("tessedit_char_whitelist", "0123456789:,"); // detect number only
//
//		String convertImageToBinary = it.doOCR(ImageHelper.convertImageToBinary(ipimage)); // greyscale to fix red
//																							// coloured text not
//		String splits[] = convertImageToBinary.split("\\n");
//
//		ArrayList<String> importantInfo = new ArrayList<>();
//		for (String split : splits) {
//			// System.out.println("split : " + split);
//			String splitsWithoutComma[] = convertImageToBinary.split(",");
//		}
//
//		it.setDatapath("D:\\MaplestoryM\\AB");
//
//		nu.pattern.OpenCV.loadLocally(); // add this
//
//		OpenCV cv = new OpenCV();
//
//		// https://www.tutorialspoint.com/reading-a-colored-image-as-grey-scale-using-java-opencv-library
//		Mat gray = Imgcodecs.imread("D:\\MaplestoryM\\AB\\AB.png", Imgcodecs.IMREAD_GRAYSCALE);
//		Mat resizedMat = new Mat();
//		double width = gray.cols();
//		double height = gray.rows();
//		double aspect = width / height;
//		Size sz = new Size(width * aspect * 2, height * aspect * 2);
//		Imgproc.resize(gray, resizedMat, sz);
//
//		Imgcodecs.imwrite("D:\\MaplestoryM\\AB\\AB_gray.png", gray);
//
//		BufferedImage topimage = Mat2BufferedImage(gray);
//		String topimageString = it.doOCR(topimage); // greyscale to fix red coloured text not
//
//		System.out.println("imageHandling()");
//		System.out.println("topimageString : " + topimageString);
//		ImageIO.write(thresholdImage(ipimage, 128), "jpg", new File("D:\\MaplestoryM\\AB\\AB_thresholdImage.png"));
//	}

//	// https://www.tutorialspoint.com/how-to-convert-opencv-mat-object-to-bufferedimage-object-using-java
//	public static BufferedImage Mat2BufferedImage(Mat mat) throws IOException {
//		// Encoding the image
//		MatOfByte matOfByte = new MatOfByte();
//		Imgcodecs.imencode(".jpg", mat, matOfByte);
//		// Storing the encoded Mat in a byte array
//		byte[] byteArray = matOfByte.toArray();
//		// Preparing the Buffered Image
//		InputStream in = new ByteArrayInputStream(byteArray);
//		BufferedImage bufImage = ImageIO.read(in);
//		return bufImage;
//	}

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

//	// Global command -- up to an hour to update
//	// i dont need these
//
//	public SlashCommandInteractionEvent getMainEvent() {
//		return mainEvent;
//	}
//
//	public void setMainEvent(SlashCommandInteractionEvent mainEvent) {
//		this.mainEvent = mainEvent;
//	}

//	// https://stackoverflow.com/a/17542851
//	public static BufferedImage thresholdImage(BufferedImage image, int threshold) {
//		BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
//		result.getGraphics().drawImage(image, 0, 0, null);
//		WritableRaster raster = result.getRaster();
//		int[] pixels = new int[image.getWidth()];
//		for (int y = 0; y < image.getHeight(); y++) {
//			raster.getPixels(0, y, image.getWidth(), 1, pixels);
//			for (int i = 0; i < pixels.length; i++) {
//				if (pixels[i] < threshold)
//					pixels[i] = 0;
//				else
//					pixels[i] = 255;
//			}
//			raster.setPixels(0, y, image.getWidth(), 1, pixels);
//		}
//		return result;
//	}

}
