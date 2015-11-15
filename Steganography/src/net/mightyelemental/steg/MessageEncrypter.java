package net.mightyelemental.steg;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class MessageEncrypter {

	JFileChooser	jfc	= new JFileChooser();
	int[]			imgBin;

	public MessageEncrypter() {
		try {
			jfc.showOpenDialog(null);
			File imgFile = jfc.getSelectedFile();
			BufferedImage img = ImageIO.read(imgFile);
			byte[] imgRawBin = new byte[img.getWidth() * img.getHeight()];
			imgBin = new int[img.getWidth() * img.getHeight()];

			imgRawBin = extractBytes(img);
			for (int x = 0; x < imgRawBin.length; x++) {
				// System.out.println(hexToBin((imgRawBin[x] & 0xFFFFFF) + ""));
				// System.out.println(imgRawBin[x] & 0xFFFFFF);
				imgBin[x] = Integer.parseInt(hexToBin(imgRawBin[x] + ""));
			}
			String message = JOptionPane.showInputDialog("Type message here");
			String messageBin = stringToBin(message);
			System.out.println(message + " = " + messageBin);
			if (messageBin.length() - message.length() > imgBin.length) {
				System.err.println("Image not large enough, or message is too long!!");
			}
			System.out.println(binToHex(encryptMessageIntoImage(imgBin, messageBin)[1] + ""));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int[] encryptMessageIntoImage(int[] imgBin, String messageBin) {
		String temp = messageBin.replaceAll(" ", "");
		String[] temp2 = temp.split("");
		for (int i = 0; i < imgBin.length; i++) {
			System.out.println(imgBin[i]);
			if (i >= temp2.length) {
				break;
			}
			StringBuilder b = new StringBuilder(imgBin[i] + "");
			b.deleteCharAt((imgBin[i] + "").length());
			b.append(temp2[i]);
			imgBin[i] = Integer.parseInt(b.toString());
		}
		return imgBin;
	}

	public static void main(String[] args) {
		new MessageEncrypter();
	}

	static String hexToBin(String s) {
		return new BigInteger(s, 16).toString(2);
	}

	public byte[] extractBytes(BufferedImage bufferedImage) throws IOException {

		// get DataBufferBytes from Raster
		WritableRaster raster = bufferedImage.getRaster();
		DataBufferByte data = (DataBufferByte) raster.getDataBuffer();

		return (data.getData());
	}

	public String binToHex(String binary) {
		String answer = "";
		int digitNumber = 1;
		int sum = 0;
		for (int i = 0; i < binary.length(); i++) {
			if (digitNumber == 1) sum += Integer.parseInt(binary.charAt(i) + "") * 8;
			else if (digitNumber == 2) sum += Integer.parseInt(binary.charAt(i) + "") * 4;
			else if (digitNumber == 3) sum += Integer.parseInt(binary.charAt(i) + "") * 2;
			else if (digitNumber == 4 || i < binary.length() + 1) {
				sum += Integer.parseInt(binary.charAt(i) + "") * 1;
				digitNumber = 0;
				if (sum < 10) answer += sum;
				else if (sum == 10) answer += "A";
				else if (sum == 11) answer += "B";
				else if (sum == 12) answer += "C";
				else if (sum == 13) answer += "D";
				else if (sum == 14) answer += "E";
				else if (sum == 15) answer += "F";
				sum = 0;
			}
			digitNumber++;
		}
		return answer;
	}

	public String stringToBin(String s) {
		byte[] bytes = s.getBytes();
		StringBuilder binary = new StringBuilder();
		for (byte b : bytes) {
			int val = b;
			for (int i = 0; i < 8; i++) {
				binary.append((val & 128) == 0 ? 0 : 1);
				val <<= 1;
			}
			binary.append(' ');
		}
		return binary.toString();
	}

}
