package Commands;

import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
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
import java.util.ArrayList;
import java.util.List;

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
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.ImageHelper;
import nu.pattern.OpenCV;

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
//			new RoleManager(event).buildEmbedMessage();
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
					Files.copy(in, Paths.get("D:/MaplestoryM/AB/AB.png"), StandardCopyOption.REPLACE_EXISTING);
					ttt();
					ITesseract image = new Tesseract();
					String textFromImage = image.doOCR(new File("D:\\MaplestoryM\\AB\\AB.png"));
					// System.out.println("textFromImage : " + textFromImage);
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

	public static void ttt() throws Exception {
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

		// Making an empty image buffer
		// to store image later
		// ipimage is an image buffer
		// of input image
//		BufferedImage opimage = new BufferedImage(1050, 1024, ipimage.getType());

		// creating a 2D platform
		// on the buffer image
		// for drawing the new image
//		Graphics2D graphic = opimage.createGraphics();

		// drawing new image starting from 0 0
		// of size 1050 x 1024 (zoomed images)
		// null is the ImageObserver class object
//		graphic.drawImage(ipimage, 0, 0, 1050, 1024, null);
//		graphic.dispose();

		// rescale OP object
		// for gray scaling images
		RescaleOp rescale = new RescaleOp(scaleFactor, offset, null);

		// performing scaling
		// and writing on a .png file
//		BufferedImage fopimage = rescale.filter(opimage, null);
//		ImageIO.write(fopimage, "jpg", new File("D:\\MaplestoryM\\AB\\AB_improved.png"));

		// Instantiating the Tesseract class
		// which is used to perform OCR
		Tesseract it = new Tesseract();

//		String str = it.doOCR(ipimage); // greyscale to fix red coloured text not
//		System.out.println("ipimageipimage:" + str);
		it.setTessVariable("tessedit_char_whitelist", "0123456789:,"); // detect number only

		String convertImageToBinary = it.doOCR(ImageHelper.convertImageToBinary(ipimage)); // greyscale to fix red
																							// coloured text not
		System.out.println("convertImageToGrayscaletext:" + convertImageToBinary);

		String splits[] = convertImageToBinary.split("\\n");

		ArrayList<String> importantInfo = new ArrayList<>();
		for (String split : splits) {
			System.out.println("split : " + split);
			String splitsWithoutComma[] = convertImageToBinary.split(",");
		}

		it.setDatapath("D:\\MaplestoryM\\AB");
//		System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // https://www.tutorialspoint.com/reading-a-colored-image-as-grey-scale-using-java-opencv-library

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

//		Mat result;
//		adaptiveThreshold(gray, result, 255, ADAPTIVE_THRESH_MEAN_C, THRESH_BINARY, 15, 40);

		// doing OCR on the image
		// and storing result in string str

		BufferedImage topimage = Mat2BufferedImage(gray);
		String topimageString = it.doOCR(topimage); // greyscale to fix red coloured text not

		ImageIO.write(thresholdImage(ipimage, 128), "jpg", new File("D:\\MaplestoryM\\AB\\AB_thresholdImage.png"));

//		String thresholdImage = it.doOCR(thresholdImage(ipimage, 128)); // greyscale to fix red coloured text not

//		System.out.println("\n\n\nthresholdImage:" + thresholdImage);

//		BufferedImage fopimage = rescale.filter(opimage, null);
//		ImageIO.write(fopimage, "jpg", new File("D:\\MaplestoryM\\AB\\AB_improved.png"));

		// reading
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
