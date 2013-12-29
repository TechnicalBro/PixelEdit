package com.philipparke.pixeledit;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

import javax.swing.JComponent;

public class Display extends JComponent implements ActionListener
{

	private Color 			backgroundColor;
	private int 			screenWidth, 
							screenHeight,
							centerX,
							centerY,
                            zoom,
                            sourceWidth,
                            sourceHeight,
                            zoomWidth,
                            zoomHeight,
							lineHeight = 12,
							padding = 2;
	
	private Point 			overheadOrigin,
                            overheadCenter,
                            overheadImageOrigin,
							loupeOrigin,
                            loupeCenter,
                            loupeImageOrigin,
                            paletteOrigin;
	
	private Dimension 		overheadSize,
							loupeSize,
                            paletteSquareSize,
                            paletteSize;


    private ImageHandler    imageHandler;

    private UserInterface   ui;

    int[]   rgbColor;

    Color[] paletteColors;

    private static final int DEFAULT_ZOOM = 5;

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
        this.overheadCenter = new Point(overheadOrigin.x+overheadSize.width/2, overheadOrigin.y+overheadSize.height/2);
		this.loupeOrigin = new Point(0, centerY);
        this.loupeCenter = new Point(loupeOrigin.x+loupeSize.width/2, loupeOrigin.y+loupeSize.height/2);
        this.zoom = DEFAULT_ZOOM;

	}

    public void setImageHandler(ImageHandler im)
    {
        imageHandler = im;
        setOverheadImageOrigin(imageHandler.getImage());
        sourceWidth = imageHandler.getWidth();
        sourceHeight = imageHandler.getHeight();
        setZoom(DEFAULT_ZOOM);
    }

    public void setPalette(Point origin, Dimension squareSize, Color[] colors)
    {
        paletteOrigin = origin;
        paletteSquareSize = squareSize;
        paletteColors = colors;
        paletteSize = new Dimension(paletteSquareSize.width, paletteSquareSize.height*colors.length);
    }

    public void setUI(UserInterface ui)
    {
        this.ui = ui;
    }

    public void setOverheadImageOrigin(BufferedImage image)
    {
        overheadImageOrigin = new Point(overheadCenter.x - (image.getWidth()/2), overheadCenter.y - (image.getHeight()/2));
    }

    public void setLoupeImageOrigin(int width, int height)
    {
        loupeImageOrigin = new Point(loupeCenter.x - (width/2), loupeCenter.y - (height/2));
    }

    public void setZoom(int factor)
    {
        zoom = factor;
        zoomHeight = sourceHeight*zoom;
        zoomWidth = sourceWidth*zoom;
        setLoupeImageOrigin(zoomWidth, zoomHeight);
    }

    public Point getLoupeOrigin(){ return loupeOrigin; }

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

    public void drawOverhead(Graphics2D g2, BufferedImage image)
    {
        g2.drawImage(image, overheadImageOrigin.x, overheadImageOrigin.y, this);
    }

    public void drawPalette(Graphics2D g2, Color[] palette)
    {
        for (int i = 0; i < palette.length; i++)
        {
            g2.setColor(palette[i]);
            g2.fillRect(paletteOrigin.x, paletteOrigin.y+(i*paletteSquareSize.height), paletteSquareSize.width, paletteSquareSize.height);
        }
    }

    public void drawLoupe(Graphics2D g2, ImageHandler image)
    {
        for (int x = 0; x < sourceWidth; x++)
        {
            for (int y = 0; y < sourceHeight; y++)
            {
                rgbColor = image.getPixelRGB(x, y);
                g2.setColor(new Color(rgbColor[0], rgbColor[1], rgbColor[2]));
                g2.fillRect(loupeImageOrigin.x + (x * zoom), loupeImageOrigin.y + (y * zoom), zoom, zoom);
            }
        }
    }

    public void drawCenterLine(Graphics2D g2)
    {
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawLine(0, centerY, screenWidth, centerY);
    }

	public void showImage(Graphics2D g2, Point origin, Dimension size, BufferedImage source)
	{
		showImage(g2, origin, size, 1.0, source);
	}
	
	public void showImage(Graphics2D g2, Point center, Dimension size, double zoom, BufferedImage source)
	{
		int x = center.x-(source.getWidth()/2);
		int y = center.y-(source.getHeight()/2);
		
		// Apply zoom
		g2.scale(zoom, zoom);
		
		// Draw the image to the panel
		g2.drawImage( source, x, y, this);

	}

    public Point translatePoint(String view, Point p)
    {
        if (view.toLowerCase().equals("overhead"))
        {
            return new Point(p.x - overheadImageOrigin.x, p.y - overheadImageOrigin.y);
        }
        else
        {
            return new Point((p.x - loupeImageOrigin.x)/zoom, (p.y - loupeImageOrigin.y)/zoom);
        }
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

		// Draw the loupe background
		g2.setColor(backgroundColor);
		g2.fillRect(0, centerY, screenWidth, screenHeight/2);

        // Draw the loupe image
        if (imageHandler != null)
            drawLoupe(g2, imageHandler);

        // Draw the overhead background
        g2.setColor(backgroundColor);
        g2.fillRect(0, 0, screenWidth, screenHeight/2);

        drawCenterLine(g2);

		// Draw the overhead image
		//showImage(g2, overheadCenter, overheadSize, Main.getUI().getImage());
        drawOverhead(g2, imageHandler.getImage());

        drawPalette(g2, ui.getPalette());
		
		// Release graphics resources
		g2.dispose();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();

	}	

}
