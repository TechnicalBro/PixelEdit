package com.philipparke.pixeledit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;


/**
 * Created by philip on 29/12/13.
 */
public class UserInterface implements MouseListener
{
    private Point               clickLocation;

    private ImageHandler        image;



    private int                 borderWidth,
                                titleBarHeight;

    private Color               selectedColor;

    private Color[]             colorPalette,
                                standardColors = {  Color.black,
                                                    Color.white,
                                                    Color.red,
                                                    Color.orange,
                                                    Color.yellow,
                                                    Color.green,
                                                    Color.blue,
                                                    Color.cyan,
                                                    Color.magenta,
                                                    Color.pink,
                                                    Color.darkGray,
                                                    Color.gray,
                                                    Color.lightGray,
                                                 };
    private Rectangle           paletteRect;

    private final int           DEFAULT_WIDTH           = 64,
                                DEFAULT_HEIGHT          = 64,
                                DEFAULT_PALETTE_SIZE    = 7;

    private final Point         PALETTE_ORIGIN          = new Point(2, 2);

    private final Dimension     PALETTE_SQUARE_SIZE     = new Dimension(20, 20);


    public UserInterface()
    {
        image = new ImageHandler();
        image.newImage(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        fillImage();

        Main.getDisplay().setImageHandler(image);
        Main.getDisplay().setUI(this);

        // Set up the color palette
        colorPalette = new Color[DEFAULT_PALETTE_SIZE];

        for (int i = 0; i < DEFAULT_PALETTE_SIZE; i++)
            setPaletteColor(i, standardColors[i]);

        selectedColor = colorPalette[0];

        Main.getDisplay().setPalette(PALETTE_ORIGIN, PALETTE_SQUARE_SIZE, colorPalette);
        paletteRect = new Rectangle(PALETTE_ORIGIN.x, PALETTE_ORIGIN.y, PALETTE_SQUARE_SIZE.width, PALETTE_SQUARE_SIZE.height*DEFAULT_PALETTE_SIZE);
    }

    public void setBorderFields(Dimension window, Dimension display)
    {
        borderWidth = (window.width - display.width)/2;
        titleBarHeight = window.height - display.height - borderWidth;

        System.out.println("borderWidth = " + borderWidth + " titleBarHeight = " + titleBarHeight);
    }

    public void setPaletteColor(int index, Color color)
    {
        colorPalette[index] = color;
    }

    public Color[] getPalette()
    {
        return colorPalette;
    }

    public void setClickLocation(Point click){ this.clickLocation = click; }

    public ImageHandler getImageHandler()
    {
        return image;
    }

    public BufferedImage getImage()
    {
        return image.getImage();
    }

    public void fillImage()
    {
        // Just adds some pretty colors
        for ( int i = 0; i < image.getWidth(); i++ )
        {
            for ( int j = 0; j < image.getHeight(); j++ )
            {
                image.setPixelRGB(i, j, i % 255, j % 255, 128);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        if (e.getButton() == MouseEvent.BUTTON1)
        {
            //System.out.println("original = " + e.getPoint());
            Point click = new Point (e.getPoint().x - borderWidth, e.getPoint().y - titleBarHeight);

            if (paletteRect.contains(click))
            {
                int y = click.y - PALETTE_ORIGIN.y;
                y /= PALETTE_SQUARE_SIZE.height;
                selectedColor = colorPalette[y];
            }

            if (click.y < Main.getDisplay().getLoupeOrigin().y)
            {
                //System.out.println("Overhead = " + Main.getDisplay().translatePoint("overhead", click));
                setClickLocation(Main.getDisplay().translatePoint("overhead", click));
            }
            else
            {
                //System.out.println("Loupe = " + Main.getDisplay().translatePoint("loupe", click));
                setClickLocation(Main.getDisplay().translatePoint("loupe", click));
            }

            //System.out.println(String.format("Clicked %d %d", clickLocation.x, clickLocation.y));
            image.setPixelColor(clickLocation.x, clickLocation.y, selectedColor);
            //System.out.println(image.getPixel(clickLocation.x, clickLocation.y));


        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
