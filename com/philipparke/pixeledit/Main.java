package com.philipparke.pixeledit;

import javax.swing.*;

public class Main extends JComponent {
	private static JFrame window;
	private static Display display;
	private static UserInterface ui;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Create a new JFrame
		window = new JFrame("Pixel Edit");

		// Create the display
		display = new Display();

		// Create a UserInterface object
		ui = new UserInterface();

		// Add the display output (frame) to the window
		window.add(display);
		// Shrink to fit
		window.pack();

		// Set the close operation to kill the process
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.setLocationRelativeTo(null);
		// Make the window visible
		window.setVisible(true);

		// Initialize ui fields
		ui.setBorderFields(window.getSize(), display.getSize());

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
		window.addMouseListener(ui);
		window.addKeyListener(ui);

		System.out.println(display.getSize());
		System.out.println(window.getSize());

		System.out.println(System.getProperty("user.dir"));

	}

	public static UserInterface getUI() {
		return ui;
	}

	public static Display getDisplay() {
		return display;
	}

	public static JFrame getWindow() {
		return window;
	}

}

