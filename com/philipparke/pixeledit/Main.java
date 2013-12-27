package com.philipparke.pixeledit;

import java.util.*;
import java.net.*;
import java.io.*;

import javax.swing.Timer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Main extends JComponent
{
	private static JFrame window;
	private static Display display;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		// Create a new JFrame
		window = new JFrame("Tiling");
		
		// Create a new tiler
		display = new Display();

		// Add the display output (frame) to the window
		window.add(display);
		// Shrink to fit
		window.pack();
		// Set the close operation to kill the process
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.setLocationRelativeTo(null);
		// Make the window visible
		window.setVisible(true);
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
	        public void run() {
	            System.out.println("In shutdown hook");
	        }
	    }, "Shutdown-thread"));

		// Refresh the display
		Timer t = new Timer(50, display);
		t.start();
		
		// Add event listeners
		//window.addKeyListener(tiler);
		//window.addMouseMotionListener(display);
		//window.addMouseListener(display);
		
		System.out.println(System.getProperty("user.dir"));

	}
	
	
	
}

