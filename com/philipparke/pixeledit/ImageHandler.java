package com.philipparke.pixeledit;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

public class ImageHandler implements MouseInputListener
{
	private BufferedImage	workingImage;
	
	private Dimension		workingImageSize;
	
	private Point 			clickLocation;
	
	// File for output
	private File outFile;
	
	private String 			fileName,
							format;
	
	public BufferedImage getImage()
	{
		return workingImage;
	}
	
	public void newImage(int width, int height)
	{
		workingImageSize = new Dimension(width, height);
		workingImage = new BufferedImage(workingImageSize.width, workingImageSize.height, BufferedImage.TYPE_INT_RGB);
	}
	
	public void saveImage(String fileName, String format)
	{
		// Set filename and format
		this.fileName = fileName;
		this.format = format;
		
		saveImage();
	}
	
	public void saveImage()
	{
		if (fileName != null && format != null)
		{

			this.outFile = new File(fileName);

			try
			{
				// Attempt to write the new buffered image in the specified format to the out file 
				ImageIO.write( this.workingImage, this.format, this.outFile );
			}
			catch (IOException ex)
			{
				System.out.println(ex.toString());
				System.out.println("Could not find file " + outFile );
			}
		}
		else
			System.out.println("Filename or format not set.");
	}
	
	// Convert separate int values to java color int
	public int convertRGB( int r, int g, int b )
	{
		return ( r << 16 ) | ( g << 8 ) | b;		// shift r by 16 bits, g by 8 bits, b by none, combine using bitwise or to make one int
	}

	public int convertARGB( int a, int r, int g, int b )
	{
		return ( a << 24 ) | ( r << 16) | ( g << 8 ) | b;
	}

	// Set individual pixels in the image using normal rgb int values
	public void setPixelRGB( int x, int y, int r, int g, int b )
	{
		if (x >= 0 && x <= workingImage.getWidth() && y >= 0 && y <= workingImage.getHeight())
			workingImage.setRGB(x, y, convertRGB( r, g, b ));
	}
	
	// Set individual pixels in the image using normal argb int values
	public void setPixelRGB( int x, int y, int a, int r, int g, int b )
	{
		workingImage.setRGB(x, y, convertARGB( a, r, g, b ));
	}
	
	public int getPixel(int x, int y)
	{
		return workingImage.getRGB(x, y);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1)
		{
			clickLocation = e.getPoint();
			System.out.println(String.format("Clicked %d %d", clickLocation.x, clickLocation.y));
			setPixelRGB(clickLocation.x, clickLocation.y, 100, 100, 100);
			System.out.println(getPixel(clickLocation.x, clickLocation.y));
			
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
