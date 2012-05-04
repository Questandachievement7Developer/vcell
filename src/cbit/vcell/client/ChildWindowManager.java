package cbit.vcell.client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.vcell.util.BeanUtils;

import cbit.vcell.client.desktop.TopLevelWindow;
import cbit.vcell.resource.ResourceUtil;
import cbit.vcell.util.DebugUtils;
//import cbit.vcell.client.desktop.biomodel.ChildWindowListener;



public class ChildWindowManager {
	 
	// matches the modality constants that are new to JDialog in Java 1.6 
	public static enum ModalityType {APPLICATION_MODAL,DOCUMENT_MODAL,MODELESS,TOOLKIT_MODAL}
	
	private class JDialogFactory{
		public JDialog createJDialog(Frame owner, String title, boolean modal) {
			return new JDialog(owner, title, modal);
		}
				
		public  JDialog createJDialog(Window owner, String title, ModalityType requestedModalityType) throws Exception {
			// may throw any of: SecurityException, NoSuchMethodException, NoSuchFieldException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
			
			if ((ResourceUtil.getJavaVersion()==ResourceUtil.JavaVersion.SIX) || (ResourceUtil.getJavaVersion()==ResourceUtil.JavaVersion.SEVEN)){	
				Class modalityTypeClass = Class.forName("java.awt.Dialog$ModalityType");
				Constructor<JDialog> jDialogConstructor = JDialog.class.getConstructor(new Class[] { Window.class, modalityTypeClass });
				Object modality = modalityTypeClass.getEnumConstants()[requestedModalityType.ordinal()];
				JDialog jDialog = jDialogConstructor.newInstance(new Object[] {owner, title, modality});
				return jDialog;
			} else {
				// If we're not running Java 1.6 or 1.7, assume we're running 1.5.  Do what's safe in any case.	
				return new JDialog(owner, title);
			}
		}		
		
	}
	
	public class ChildWindow {
		
		private WindowListener windowListener = new WindowListener(){
			public void windowActivated(WindowEvent e) {}
			public void windowClosed(WindowEvent e) {
				for (ChildWindowListener listener : listeners){
					listener.closed(ChildWindow.this);
				}
				
			}
			public void windowClosing(WindowEvent e) {
				for (ChildWindowListener listener : listeners){
					listener.closing(ChildWindow.this);
				}
				if (ChildWindow.this != null) {
					closeChildWindow(ChildWindow.this);
				}
			}
			public void windowDeactivated(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowOpened(WindowEvent e) {}
		};

		
		private boolean bModal;
		private Container contentPane;
		private Object contextObject;
		private JDialog dialog;
		
		private String title = null;
		private Point location = null;
		private Dimension preferredSize = null;
		private Boolean resizable = null;
		private Boolean pack = null;
		private Dimension size = null;
		private Boolean isCenteredOnScreen = null;
		private Boolean isCenteredOnParent = null;
		
		
		private ArrayList<ChildWindowListener> listeners = new ArrayList<ChildWindowListener>();
	
		private ChildWindow(Container contentPane, Object aContextObject, String title) {
			this.contentPane = contentPane;
			this.contextObject = aContextObject;
		}
					
		private void initDialog(){
			dialog.addWindowListener(windowListener);
			//dialog = new JDialog(parent, title, false);
			dialog.getContentPane().setLayout(new BorderLayout());
			dialog.getContentPane().add(contentPane);
//				dialog.setContentPane(contentPane);
			dialog.setAlwaysOnTop(false);
			if (location!=null){
				dialog.setLocation(location);
			}
			if (preferredSize != null){
				dialog.setPreferredSize(preferredSize);
			}
			if (pack!=null && pack){
				dialog.pack();
			}
			
			if (resizable != null){
				dialog.setResizable(resizable);
			}
			if (size != null){
				dialog.setSize(size);
			}
			
			if (isCenteredOnScreen !=null) {
				dialog.setLocationRelativeTo(null);
			}
			
			if (isCenteredOnParent != null) {
				dialog.setLocationRelativeTo(dialog.getParent());
			}
			
			System.out.println("Making a child window.  My parent is a "+this.getParent().getName());
		}
		
		public void setIsCenteredOnParent(){
			isCenteredOnParent = true;
			isCenteredOnScreen = false;
			if (dialog!=null){
				dialog.setLocationRelativeTo(dialog.getParent());
				
			}
		}
		
		public void setIsCenteredOnScreen(){
			isCenteredOnParent = false;
			isCenteredOnScreen = true;
			if (dialog!=null){
				dialog.setLocationRelativeTo(null);  // as specified in java.awt.Window javadocs 
			}
		}
		
		public void addChildWindowListener(ChildWindowListener childWindowListener) {
			listeners.add(childWindowListener);
		}
		
		public void close(){
			closeChildWindow(this);
		}
		
		private void dispose(){			
			if (dialog != null){
				dialog.setVisible(false);
				//dialog.setContentPane(null);
				dialog.dispose();
				dialog = null;
			} else {
				//DebugUtils.stop("ChildWindowManager.ChildWindow.dispose(): I was just asked to displose of a null JDialog ");
			}
		}
		
		public Container getContentPane() {
			return this.contentPane;
		}
		
		public Object getContextObject() {
			return contextObject;
		}
				
		public Point getLocation(){
			if (dialog!=null){
				return dialog.getLocation();
			}else{
				return location;
			}
		}
		
		public Point getLocationOnScreen(){
			if (dialog!=null){
				return dialog.getLocationOnScreen();
			}else{
				return null;
			}
		}
		
		public boolean getModal(){
			return bModal;
		}
		
		public JFrame getParent() {
			return parent;
		}
		
		public String getTitle() {
			if (dialog!=null){
				return dialog.getTitle();
			}else{
				return title;
			}
		}
		public void hide(){
			if (dialog!=null && dialog.isVisible()){
				dialog.setVisible(false);
				for (ChildWindowListener listener : listeners){
					listener.closed(this);
				}
			}
		}
		public boolean isShowing() {
			if (dialog!=null){
				return dialog.isShowing();
			}else{
				return false;
			}
		}
		public void removehildWindowListener(ChildWindowListener childWindowListener) {
			listeners.remove(childWindowListener);
		}

		public void setContextObject(Object context) {
			this.contextObject = context;
		}
		
		public void setLocation(Point point){
			location = point;
			if (dialog!=null){
				dialog.setLocation(point);
			}
		}
		
		public void setPreferredSize(Dimension preferredSize){
			this.preferredSize = preferredSize;
			if (dialog!=null){
				dialog.setPreferredSize(preferredSize);
			}
		}
		
		public void setResizable(boolean resizable){
			this.resizable = resizable;
			if (dialog!=null){
				dialog.setResizable(resizable);
			}
		}

		public void pack(){
			this.pack = true;
			if (dialog!=null){
				dialog.pack();
			}
		}
		
		public void setPosition(int i, int j) {
			
		}
		
		public void setSize(int i, int j) {
			this.size = new Dimension(i,j);
			if (dialog!=null){
				dialog.setSize(i,j);
			}
		}
		
		public void setSize(Dimension dim){
			this.size = dim;
			if (dialog!=null){
				dialog.setSize(dim);
			}
		}

		public void setTitle(String title) {
			this.title = title;
			if (dialog!=null){
				dialog.setTitle(title);
			}
		}

		public void show(){
			if (dialog==null){
				dialog = (new JDialogFactory()).createJDialog(parent, title, false);
				initDialog();
				dialog.setVisible(true);
			}else if (!dialog.isVisible()){
				dialog.setVisible(true);
			}
		}

		public void showModal() {
			if (dialog==null){
				dialog = (new JDialogFactory()).createJDialog(parent, title, false);
				initDialog();
				dialog.setVisible(true);
				dialog.toFront();
			}else if (!dialog.isVisible()){
				dialog.setVisible(true);
				dialog.toFront();
			}
		}

		public void toFront() {
			dialog.toFront();
		}

	};   
	
	public static ChildWindowManager findChildWindowManager(Component component){
		ChildWindowManager childWindowManager = null;
		Frame topLevelFrame = JOptionPane.getFrameForComponent(component);
		if (topLevelFrame instanceof TopLevelWindow){
			childWindowManager = ((TopLevelWindow)topLevelFrame).getChildWindowManager();
		}
		
		if (childWindowManager==null){
			System.err.println("ChildWindowManager.findChildWindowManager(Component) could not find a ChildWindowManager for component: "+component.getName()+" which is a "+component.getClass().getCanonicalName());
			Thread.dumpStack();
			System.err.println();
		}
		
		return childWindowManager;
	}
	
	private final ArrayList<ChildWindow> childWindows = new ArrayList<ChildWindow>();
	
	private JFrame parent = null;
	
	public ChildWindowManager(JFrame parent){
		this.parent = parent;
	}
		
	public ChildWindow addChildWindow(Container contentPane, Object contextObject, String title){
		if (getChildWindowFromContentPane(contentPane)!=null){
			throw new IllegalArgumentException("child window with content pane already exists");
		}
		if (getChildWindowFromContext(contextObject)!=null){
			throw new IllegalArgumentException("child window with content object "+contextObject+" already exists");
		}
		
		ChildWindow childWindow = new ChildWindow(contentPane, contextObject, title);
		childWindows.add(childWindow);
		return childWindow;
	}
	
	public ChildWindow addChildWindow(Container contentPane, Object contextObject, String title, boolean resizable){
		ChildWindow childWindow = addChildWindow(contentPane, contextObject, title);
		childWindow.setResizable(resizable);
		return childWindow;
	}
	
	public void closeAllChildWindows(){
		while (childWindows.size()>0){
			ChildWindow child = childWindows.get(0);
			child.hide();
			child.dispose();
			childWindows.remove(child);
		}
	}
	
	public void closeChildWindow(ChildWindow childWindow){
		childWindow.dispose();
		childWindows.remove(childWindow);
	}

	
	public ChildWindow[] getAllChildWindows(){
		return childWindows.toArray(new ChildWindow[childWindows.size()]);
	}

	public ChildWindow getChildWindowFromContentPane(Container contentPane){
		for (ChildWindow child : childWindows){
			if (child.getContentPane()==contentPane){
				return child;
			}
		}
		return null;
	}

	public ChildWindow getChildWindowFromContext(Object contextObject){
		for (ChildWindow child : childWindows){
			if (child.getContextObject().equals(contextObject)){
				return child;
			}
		}
		return null;
	}

	public void cascadeWindows() {
		Dimension sizeOfCurrentDisplay = new Dimension(1000,1000);
		int countShownWindows = 0;
		for (ChildWindow childWindow : childWindows){
			if (childWindow.isShowing()){
				countShownWindows++;
			}
		}
		if (countShownWindows==0){
			return;
		}
		
		double dx = sizeOfCurrentDisplay.getWidth() / countShownWindows / 4;
		double dy = sizeOfCurrentDisplay.getHeight() / countShownWindows / 4;
		int count = 0;
		for (ChildWindow childWindow : childWindows){
			if (childWindow.isShowing()){
				childWindow.setLocation(new Point((int)(dx*count),(int)(dy*count)));
				childWindow.show();
				count++;
			}
		}
	}

	public void tileWindows(boolean horizontal) {
//		JInternalFrame[] iframes = getOpenWindows();
//		Rectangle[] bounds = BeanUtils.getTiledBounds(iframes.length, getJDesktopPane().getWidth(), getJDesktopPane().getHeight(), horizontal);
//		for (int i=0;i<iframes.length;i++) {
//			iframes[i].setBounds(bounds[i]);
//			iframes[i].show();
//		}
	}	
	
	public static void main(String args[]){
		try {
			
			
			
			
			
			JFrame frame = new JFrame();
			frame.add(new JLabel("main frame"));
			frame.pack();
			frame.setLocation(400,400);
//			frame.addWindowListener(new WindowListener() {
//				public void windowActivated(WindowEvent e) {}
//				public void windowClosed(WindowEvent e) {}
//				public void windowClosing(WindowEvent e) {
//					System.exit(0);
//				}
//				public void windowDeactivated(WindowEvent e) {}
//				public void windowDeiconified(WindowEvent e) {}
//				public void windowIconified(WindowEvent e) {}
//				public void windowOpened(WindowEvent e) {}
//			});
			frame.setVisible(true);
			
//			JDialog aJDialog = (JDialog)ChildWindowManager.JDialogFactory.createJDialog(frame, ChildWindowManager.ModalityType.DOCUMENT_MODAL);
//			aJDialog.setVisible(true);
//			
//			ChildWindowManager windowManager = new ChildWindowManager(frame);
//			
//			String child1Obj = "child1";
//			JPanel child1 = new JPanel();
//			child1.add(new JLabel("child1"));
//			ChildWindow childWindow1 = windowManager.addChildWindow(child1, child1Obj, "child1 title");
//			childWindow1.show();
//			childWindow1.setLocation(new Point(200,200));
//			
//			String child2Obj = "child2";
//			JPanel child2 = new JPanel();
//			child2.add(new JLabel("child2"));
//			ChildWindow childWindow2 = windowManager.addChildWindow(child2, child2Obj, "child2 title");
//			childWindow2.show();
//			childWindow2.setLocation(new Point(300,300));
//			
//			
//			Thread.sleep(5000);
//			
//			windowManager.closeAllChildWindows();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
	
	