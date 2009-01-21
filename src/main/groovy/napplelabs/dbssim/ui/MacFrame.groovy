/**
 * 
 */
package napplelabs.dbssim.ui

import javax.swing.JFrameimport java.awt.Dimensionimport javax.swing.SwingUtilitiesimport java.lang.Runnableimport com.explodingpixels.macwidgets.MacWidgetFactoryimport com.explodingpixels.macwidgets.TriAreaComponentimport java.awt.BorderLayoutimport com.explodingpixels.macwidgets.BottomBarSizeimport javax.swing.JLabelimport javax.swing.JPanelimport javax.swing.JButtonimport ddf.minim.Minimimport napplelabs.dbssim.NeuronSignalimport ddf.minim.signals.PinkNoiseimport napplelabs.dbssim.TracePanelimport ddf.minim.AudioOutputimport javax.swing.AbstractButtonimport javax.swing.JComponentimport java.util.Listimport javax.swing.ButtonGroupimport com.explodingpixels.macwidgets.LabeledComponentGroup/**
 * @author zkim
 *
 */
public class MacFrame{
	
	JFrame frame
	TriAreaComponent toolbar
	TriAreaComponent bottombar
	JPanel content;
	List<JButton> toolbarButtons = []
	
	boolean visible = false
	
	public MacFrame(int width, int height) {
		
		
		frame = new JFrame("DBS Simulator")
		frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
		frame.size = new Dimension(width, height)
		
		toolbar = MacWidgetFactory.createUnifiedToolBar()
		frame.contentPane.add(toolbar.component, BorderLayout.NORTH)
		
		bottombar = MacWidgetFactory.createBottomBar(BottomBarSize.LARGE);
		frame.contentPane.add(bottombar.component, BorderLayout.SOUTH)
		content = new JPanel()
		content.layout = new BorderLayout()
		frame.contentPane.add(content, BorderLayout.CENTER)
		
	}
	
	public void setVisisble(boolean vis) {
		frame.visible = vis
	}
	
	public boolean getVisible() {
		return frame.visible
	}
	
	def revalidate() {
		frame.contentPane.revalidate()
	}
	
	def addToolbarComponentRight(JComponent comp) {
		toolbar.addComponentToRight(comp)
	}
	
	def addToolbarComponentCenter(JComponent comp) {
		toolbar.addComponentToCenter(comp)
	}
	
	def addToolbarButton(List<JButton> buttons) {
		
		ButtonGroup group = new ButtonGroup();
		
		buttons.eachWithIndex {button, i ->
			button.putClientProperty("JButton.buttonType", "segmentedTextured");
			if(i == 0) {
				button.putClientProperty("JButton.segmentPosition", "first");
			} else if(i == buttons.size()-1) {
				button.putClientProperty("JButton.segmentPosition", "last");
			} else {
				button.putClientProperty("JButton.segmentPosition", "middle");
			}
			
			group.add(button)
			button.setFocusable(false);
		};
		        
        LabeledComponentGroup viewButtons = new LabeledComponentGroup(null, buttons);
        //addToolbarComponentCenter(viewButtons.component)
        revalidate()
	}
	
	public static void main(String[] args) {
		MacFrame mf = new MacFrame();
		
		
		
	}
}
