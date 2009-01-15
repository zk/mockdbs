/**
 * 
 */
package napplelabs.dbssim.ui

import javax.swing.JFrame
 * @author zkim
 *
 */
public class MacFrame{
	
	JFrame frame
	TriAreaComponent toolbar
	TriAreaComponent bottombar
	JPanel content;
	
	public MacFrame() {
		
		
		frame = new JFrame("DBS Simulator")
		frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
		frame.size = new Dimension(500, 300)
		
		toolbar = MacWidgetFactory.createUnifiedToolBar()
		frame.contentPane.add(toolbar.component, BorderLayout.NORTH)
		
		bottombar = MacWidgetFactory.createBottomBar(BottomBarSize.LARGE);
		frame.contentPane.add(bottombar.component, BorderLayout.SOUTH)
		content = new JPanel()
		content.layout = new BorderLayout()
		frame.contentPane.add(content, BorderLayout.CENTER)
		
		frame.visible = true;
		
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
	
	public static void main(String[] args) {
		MacFrame mf = new MacFrame();
		
		
		
	}
}