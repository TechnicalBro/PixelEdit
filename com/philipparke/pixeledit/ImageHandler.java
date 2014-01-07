package com.philipparke.pixeledit;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageHandler {
	private BufferedImage image;

	private Dimension imageSize;

	// File for output
	private File outFile,inFile;

	private String fileName, format;

	public void setFileName(String name) {
		fileName = name;
	}

	public void setFormat(String f) {
		format = f;
	}

	public int getWidth() {
		return imageSize.width;
	}

	public int getHeight() {
		return imageSize.height;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void newImage(int width, int height) {
		imageSize = new Dimension(width, height);
		image = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_INT_RGB);
	}

	public void setImage(BufferedImage newImage) {
		image = newImage;
	}

	public void saveImage(String fileName, String format) {
		// Set filename and format
		setFileName(fileName);
		setFormat(format);

		saveImage();
	}

	public void saveImage() {
		if (fileName != null && format != null) {

			outFile = new File(fileName + "." + format);

			try {
				// Attempt to write the new buffered image in the specified format to the out file 
				ImageIO.write(image, format, outFile);
			} catch (IOException ex) {
				System.out.println(ex.toString());
				System.out.println("Could not find file " + outFile);
			}
		} else {
			System.out.println("Filename or format not set.");
		}
	}

	public void loadImage(String fileName, String format) {
		// Set filename and format
		setFileName(fileName);
		setFormat(format);

		loadImage();
	}

	public void loadImage() {
		if (fileName != null && format != null) {
			inFile = new File(fileName + "." + format);

			try {
				image = ImageIO.read(inFile);
			} catch (IOException ex) {
				System.out.println(ex.toString());
				System.out.println("Could not find file " + inFile);
			}
		} else {
			System.out.println("Filename or format not set.");
		}
	}

	// Convert separate int values to java color int
	public int convertRGB(int r, int g, int b) {
		return (r << 16) | (g << 8) | b; // shift r by 16 bits, g by 8 bits, b by none, combine using bitwise or to make one int
	}

	public int convertARGB(int a, int r, int g, int b) {
		return (a << 24) | (r << 16) | (g << 8) | b;
	}

	// Set individual pixels in the image using normal rgb int values
	public void setPixelRGB(int x, int y, int r, int g, int b) {
		if (x >= 0 && x <= image.getWidth() && y >= 0 && y <= image.getHeight()) {
			image.setRGB(x, y, convertRGB(r, g, b));
		}
	}

	// Set individual pixels in the image using normal argb int values
	public void setPixelRGB(int x, int y, int a, int r, int g, int b) {
		image.setRGB(x, y, convertARGB(a, r, g, b));
	}

	public void setPixelColor(int x, int y, Color c) {
		if (x >= 0 && x <= image.getWidth() && y >= 0 && y <= image.getHeight()) {
			image.setRGB(x, y, convertRGB(c.getRed(), c.getGreen(), c.getBlue()));
		}
	}

	public int getPixel(int x, int y) {
		if (x >= 0 && x <= image.getWidth() && y >= 0 && y <= image.getHeight()) {
			return image.getRGB(x, y);
		} else {
			return -1;
		}
	}

	public int[] getPixelRGB(int x, int y) {
		if (x >= 0 && x <= image.getWidth() && y >= 0 && y <= image.getHeight()) {
			int color = image.getRGB(x, y);
			return new int[]{(color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF};
		} else {
			return new int[]{-1};
		}
	}
}