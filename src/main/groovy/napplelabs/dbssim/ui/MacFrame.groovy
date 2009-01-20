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
	List<JButton> toolbarButtons = []
	
	boolean visible = false
	
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