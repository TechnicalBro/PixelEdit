package com.philipparke.pixeledit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;


/**
 * Created by philip on 29/12/13.
 */
public class UserInterface implements MouseListener, KeyListener
{
    private Point               clickLocation;

    private ImageHandler        image;

    private Display             display;

    private int                 borderWidth,
                                titleBarHeight;

    private Color               primaryColor,
                                secondaryColor;

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
    private Rectangle           paletteRect,
                                zoomRect,
                                overheadRect,
                                saveRect;

    private final int           DEFAULT_WIDTH           = 64,
                                DEFAULT_HEIGHT          = 64,
                                DEFAULT_PALETTE_SIZE    = 7;

    private final Point         PALETTE_ORIGIN          = new Point(2, 2),
                                ZOOM_ORIGIN             = new Point(24, 2),
                                SELECTED_COLORS_ORIGIN  = new Point(380, 2),
                                SAVE_ORIGIN             = new Point(46, 2);

    private final Dimension     CONTROL_SIZE     = new Dimension(20, 20),
                                SCREEN_SIZE      = new Dimension(800, 600);


    public UserInterface()
    {
        image = new ImageHandler();
        display = Main.getDisplay();
        image.newImage(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        fillImage();

        display.setDisplaySize(SCREEN_SIZE.width, SCREEN_SIZE.height);
        display.setImageHandler(image);
        display.setUI(this);

        overheadRect = new Rectangle(SCREEN_SIZE.width-image.getWidth(), 0, image.getWidth(), image.getHeight());

        // Set up the color palette
        colorPalette = new Color[DEFAULT_PALETTE_SIZE];

        for (int i = 0; i < DEFAULT_PALETTE_SIZE; i++)
            setPaletteColor(i, standardColors[i]);

        primaryColor = colorPalette[0];
        secondaryColor = colorPalette[1];
        display.setSelectedColors(0, primaryColor);
        display.setSelectedColors(1, secondaryColor);
        display.setSelectedColorsDisplay(SELECTED_COLORS_ORIGIN, CONTROL_SIZE);
        display.setPalette(PALETTE_ORIGIN, CONTROL_SIZE, colorPalette);
        paletteRect = new Rectangle(PALETTE_ORIGIN.x, PALETTE_ORIGIN.y, CONTROL_SIZE.width, CONTROL_SIZE.height*DEFAULT_PALETTE_SIZE);

        // Setup zoom buttons
        display.setZoomControls(ZOOM_ORIGIN, CONTROL_SIZE);
        zoomRect = new Rectangle(ZOOM_ORIGIN.x, ZOOM_ORIGIN.y, CONTROL_SIZE.width, CONTROL_SIZE.height*2);

        display.setSaveControls(SAVE_ORIGIN, CONTROL_SIZE);
        saveRect = new Rectangle(SAVE_ORIGIN.x, SAVE_ORIGIN.y, CONTROL_SIZE.width, CONTROL_SIZE.height);

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

            //System.out.println("original = " + e.getPoint());
            Point click = new Point (e.getPoint().x - borderWidth, e.getPoint().y - titleBarHeight);

            if (paletteRect.contains(click))
            {
                int y = click.y - PALETTE_ORIGIN.y;
                y /= CONTROL_SIZE.height;
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    primaryColor = colorPalette[y];
                    display.setSelectedColors(0, primaryColor);
                }
                else
                {
                    secondaryColor = colorPalette[y];
                    display.setSelectedColors(1, secondaryColor);
                }
            }

            else if (zoomRect.contains(click))
            {
                int y = click.y - ZOOM_ORIGIN.y;
                y /= CONTROL_SIZE.height;
                if (y == 0)
                    display.changeZoom(1);
                else
                    display.changeZoom(-1);
            }

            else if (saveRect.contains(click))
            {
                image.saveImage("MyImage", "PNG");
                System.out.println(System.getProperty("user.dir"));
            }

            else if (overheadRect.contains(click))
            {
                //System.out.println("Overhead = " + Main.getDisplay().translatePoint("overhead", click));
                setClickLocation(display.translatePoint("overhead", click));
                if (e.getButton() == MouseEvent.BUTTON1)
                    image.setPixelColor(clickLocation.x, clickLocation.y, primaryColor);
                else
                    image.setPixelColor(clickLocation.x, clickLocation.y, secondaryColor);
            }
            else
            {
                //System.out.println("Loupe = " + Main.getDisplay().translatePoint("loupe", click));
                setClickLocation(display.translatePoint("loupe", click));
                if (clickLocation.x < 0)
                {
                    display.moveLeft();
                }
                else if (clickLocation.x > image.getWidth())
                {
                   display.moveRight();
                }
                if (clickLocation.y < 0)
                {
                    display.moveUp();
                }
                else if (clickLocation.y > image.getHeight())
                {
                    display.moveDown();
                }
                if (e.getButton() == MouseEvent.BUTTON1)
                    image.setPixelColor(clickLocation.x, clickLocation.y, primaryColor);
                else
                    image.setPixelColor(clickLocation.x, clickLocation.y, secondaryColor);
            }

            //System.out.println(String.format("Clicked %d %d", clickLocation.x, clickLocation.y));

            //System.out.println(image.getPixel(clickLocation.x, clickLocation.y));


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

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        switch (code)
        {
            case KeyEvent.VK_UP:
                display.moveDown();
                break;
            case KeyEvent.VK_DOWN:
                display.moveUp();
                break;
            case KeyEvent.VK_LEFT:
                display.moveRight();
                break;
            case KeyEvent.VK_RIGHT:
                display.moveLeft();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
