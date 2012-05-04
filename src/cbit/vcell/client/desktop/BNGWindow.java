/*
 * Copyright (C) 1999-2011 University of Connecticut Health Center
 *
 * Licensed under the MIT License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *  http://www.opensource.org/licenses/mit-license.php
 */

package cbit.vcell.client.desktop;

import cbit.vcell.client.ChildWindowManager;

/**
 * Insert the type's description here.
 * Creation date: (7/18/2006 11:06:11 AM)
 * @author: Anuradha Lakshminarayana
 */
public class BNGWindow extends javax.swing.JFrame implements TopLevelWindow {
	private javax.swing.JPanel ivjJFrameContentPane = null;
	private cbit.vcell.client.BNGWindowManager fieldBngWindowManager = null;
	private final ChildWindowManager childWindowManager;

/**
 * BNGWindow constructor comment.
 */
public BNGWindow() {
	super();
	initialize();
	childWindowManager = new ChildWindowManager(this);
}

/**
 * Gets the bngWindowManager property (cbit.vcell.client.BNGWindowManager) value.
 * @return The bngWindowManager property value.
 * @see #setBngWindowManager
 */
public cbit.vcell.client.BNGWindowManager getBngWindowManager() {
	return fieldBngWindowManager;
}


/**
 * Return the JFrameContentPane property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getJFrameContentPane() {
	if (ivjJFrameContentPane == null) {
		try {
			ivjJFrameContentPane = new javax.swing.JPanel();
			ivjJFrameContentPane.setName("JFrameContentPane");
			ivjJFrameContentPane.setLayout(new java.awt.BorderLayout());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJFrameContentPane;
}


/**
 * Insert the method's description here.
 * Creation date: (7/18/2006 1:52:57 PM)
 * @return cbit.vcell.client.desktop.TopLevelWindowManager
 */
public cbit.vcell.client.TopLevelWindowManager getTopLevelWindowManager() {
	return getBngWindowManager();
}


/**
 * Called whenever the part throws an exception.
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {

	/* Uncomment the following lines to print uncaught exceptions to stdout */
	// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
	// exception.printStackTrace(System.out);
}


/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("BNGWindow");
		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		setSize(612, 612);
		add(getJFrameContentPane());
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	// user code end
}


/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		BNGWindow aBNGWindow;
		aBNGWindow = new BNGWindow();
		aBNGWindow.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		aBNGWindow.show();
		java.awt.Insets insets = aBNGWindow.getInsets();
		aBNGWindow.setSize(aBNGWindow.getWidth() + insets.left + insets.right, aBNGWindow.getHeight() + insets.top + insets.bottom);
		aBNGWindow.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("Exception occurred in main() of javax.swing.JFrame");
		exception.printStackTrace(System.out);
	}
}


/**
 * Sets the bngWindowManager property (cbit.vcell.client.BNGWindowManager) value.
 * @param bngWindowManager The new value for the property.
 * @see #getBngWindowManager
 */
public void setBngWindowManager(cbit.vcell.client.BNGWindowManager bngWindowManager) {
	cbit.vcell.client.BNGWindowManager oldValue = fieldBngWindowManager;
	fieldBngWindowManager = bngWindowManager;
	firePropertyChange("bngWindowManager", oldValue, bngWindowManager);
}


/**
 * Insert the method's description here.
 * Creation date: (5/24/2004 11:39:07 AM)
 * @param c java.awt.Component
 */
public void setWorkArea(java.awt.Component c) {
	getJFrameContentPane().add(c, java.awt.BorderLayout.CENTER);
}


/**
 * Insert the method's description here.
 * Creation date: (7/18/2006 1:52:57 PM)
 * @param connectionStatus cbit.vcell.client.server.ConnectionStatus
 */
public void updateConnectionStatus(cbit.vcell.client.server.ConnectionStatus connectionStatus) {}


/**
 * Insert the method's description here.
 * Creation date: (7/18/2006 1:52:57 PM)
 * @param freeBytes long
 * @param totalBytes long
 */
public void updateMemoryStatus(long freeBytes, long totalBytes) {}


/**
 * Insert the method's description here.
 * Creation date: (7/18/2006 1:52:57 PM)
 * @param i int
 */
public void updateWhileInitializing(int i) {}

public ChildWindowManager getChildWindowManager() {
	return childWindowManager;
}
}
