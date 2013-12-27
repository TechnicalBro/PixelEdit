package com.philipparke.pixeledit;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.*;

import javax.swing.JComponent;

public class Display extends JComponent implements ActionListener
{

	private Color 			backgroundColor;
	private int 			screenWidth, 
							screenHeight,
							centerX,
							centerY,
							lineHeight = 12,
							padding = 2;
	
	private Point 			overheadOrigin,
							loupeOrigin;
	
	private Dimension 		overheadSize,
							loupeSize;
	
	private ImageHandler	imagehandler;
	

	private Font consoleFont = new Font("Monospaced", Font.PLAIN, lineHeight);

	public Display()
	{
		// Default to 800x600
		this(800, 600);
	}

	public Display(int screenWidth, int screenHeight)
	{
		this.backgroundColor = Color.black;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.centerX = screenWidth/2;
		this.centerY = screenHeight/2;
		this.overheadSize = new Dimension(screenWidth, screenHeight/2);
		this.loupeSize = new Dimension(screenWidth, screenHeight/2);
		this.overheadOrigin = new Point(0, 0);
		this.loupeOrigin = new Point(0, centerY);
		this.imagehandler = new ImageHandler();
		
		// Set up a new image by default
		imagehandler.newImage(overheadSize.width, overheadSize.height);
		
		// Add the event listener
		addMouseListener(imagehandler);
		
		// Just adds some pretty colors
		for ( int i = 0; i < overheadSize.width; i++ )
		{
			for ( int j = 0; j < overheadSize.height; j++ )
			{
				imagehandler.setPixelRGB( i, j, i%255, j%255, 128 );
			}
		}
	}


	public void drawConsoleWindow(Graphics2D g2, Point origin, Dimension size, List<String> source)
	{
		int maxRows = (size.height-padding) / lineHeight;
		int row = 1;
		int x = origin.x + padding;
		int y = 0 - padding;

		for (int i = source.size()-1; i >= 0 && row <= maxRows; i--)
		{
			// Start one row up from the bottom, allowing room for the command line
			y = (origin.y+size.height)-(row*lineHeight);
			g2.setColor(Color.cyan);
			g2.setFont(consoleFont);
			g2.drawString(source.get(i), x, y);
			//System.out.println(stdout.get(i));
			row++;
		}

		// Draw a frame
		g2.setColor(Color.cyan);
		g2.setStroke(new BasicStroke(2));
		g2.drawRect(origin.x, origin.y, size.width, size.height);
	}

	public void drawCommandLine(Graphics2D g2, Point origin, Dimension size, String source)
	{
		int x = origin.x + padding;
		int y = (origin.y+size.height) - padding;
		g2.setColor(Color.cyan);
		g2.setFont(consoleFont);
		g2.drawString(source, x, y);
	}
	
	public void showImage(Graphics2D g2, Point origin, Dimension size, BufferedImage source)
	{
		showImage(g2, origin, size, 1.0, source);
	}
	
	public void showImage(Graphics2D g2, Point origin, Dimension size, double zoom, BufferedImage source)
	{
		int x = origin.x;
		int y = origin.y;
		
		// Apply zoom
		g2.scale(zoom, zoom);
		
		// Draw the image to the panel
		g2.drawImage( source, x, y, this);

	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(screenWidth, screenHeight);
	}

	@Override
	public void paintComponent(Graphics g)
	{
		// Convert g to G2D
		Graphics2D g2 = (Graphics2D)g;

		// Draw the background
		g2.setColor(backgroundColor);
		g2.fillRect(0, 0, screenWidth, screenHeight);

		// Draw the overhead image
		showImage(g2, overheadOrigin, overheadSize, imagehandler.getImage());

		
		// Draw the loupe image
		showImage(g2, loupeOrigin, loupeSize, 2.0, imagehandler.getImage());
		
		// Release graphics resources
		g2.dispose();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();

	}	

}
