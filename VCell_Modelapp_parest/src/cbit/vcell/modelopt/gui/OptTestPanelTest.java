/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package cbit.vcell.modelopt.gui;
/**
 * Insert the type's description here.
 * Creation date: (8/23/2005 5:20:49 PM)
 * @author: Jim Schaff
 */
public class OptTestPanelTest {
/**
 * OptTestPanelTest constructor comment.
 */
public static void main(java.lang.String[] args) {
	try {
		javax.swing.JFrame frame = new javax.swing.JFrame();
		OptTestPanel aOptTestPanel;
		aOptTestPanel = new OptTestPanel();
		frame.setContentPane(aOptTestPanel);
		frame.setSize(aOptTestPanel.getSize());
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		frame.show();
		java.awt.Insets insets = frame.getInsets();
		frame.setSize(frame.getWidth() + insets.left + insets.right, frame.getHeight() + insets.top + insets.bottom);
		frame.setVisible(true);

		aOptTestPanel.setParameterEstimationTask(cbit.vcell.modelopt.ParameterEstimationTaskTest.getExample());
		aOptTestPanel.setOptimizationService(new cbit.vcell.opt.solvers.LocalOptimizationService());
		
	} catch (Throwable exception) {
		System.err.println("Exception occurred in main() of javax.swing.JPanel");
		exception.printStackTrace(System.out);
	}
}
}