package napplelabs.dbssim.ui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.explodingpixels.macwidgets.BottomBarSize;
import com.explodingpixels.macwidgets.LabeledComponentGroup;
import com.explodingpixels.macwidgets.MacWidgetFactory;
import com.explodingpixels.macwidgets.TriAreaComponent;

/**
 * 
 * MacFrame takes responsibility for creating the wrapping jframe,
 * a navigation bar, and a bottom bar.  It also provides a convience method, addToolbarButton, 
 * which allows you to create a center toolbar button group w/o having to concern yourself
 * with the setup & teardown.
 * 
 * |-----------------------------|
 * |			a				 |
 * |-----------------------------|
 * |						     |
 * |						     |
 * |						     |
 * |						     |
 * |						     |
 * |						     |
 * |						     |
 * |						     |
 * |						     |
 * |-----------------------------|
 * |			b				 |
 * |-----------------------------|
 * 
 * 
 * @author zkim
 *
 */

public class MacFrame {
	private JFrame frame;
	private TriAreaComponent toolbar;
	private TriAreaComponent bottombar;
	private JPanel content;
	private List<JButton> toolbarButtons = new ArrayList<JButton>();
	
	public MacFrame(int width, int height) {
		setFrame(new JFrame("MockDBS"));
		getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		getFrame().setSize(width, height);
		getFrame().setLocationRelativeTo(null);
		
		toolbar = MacWidgetFactory.createUnifiedToolBar();
		getFrame().getContentPane().add(toolbar.getComponent(), BorderLayout.NORTH);
		
		bottombar = MacWidgetFactory.createBottomBar(BottomBarSize.LARGE);
		getFrame().getContentPane().add(bottombar.getComponent(), BorderLayout.SOUTH);
		setContent(new JPanel());
		getContent().setLayout(new BorderLayout());
		getFrame().getContentPane().add(getContent(), BorderLayout.CENTER);
		
	}
	
	public void setVisisble(boolean vis) {
		getFrame().setVisible(vis);
	}
	
	public boolean getVisible() {
		return getFrame().isVisible();
	}
	
	public void addToolbarComponentRight(JComponent comp) {
		toolbar.addComponentToRight(comp);
	}
	
	public void addToolbarComponentCenter(JComponent comp) {
		toolbar.addComponentToCenter(comp);
	}
	
	public void addToolbarComponentLeft(JComponent comp) {
		toolbar.addComponentToLeft(comp);
	}
	
	public void addToolbarButton(List<JButton> buttons) {
		
		ButtonGroup group = new ButtonGroup();
		
		for(int i=0; i < buttons.size(); i++) {
			JButton button = buttons.get(i);
			button.putClientProperty("JButton.buttonType", "segmentedTextured");
			if(i == 0) {
				button.putClientProperty("JButton.segmentPosition", "first");
			} else if(i == buttons.size()-1) {
				button.putClientProperty("JButton.segmentPosition", "last");
			} else {
				button.putClientProperty("JButton.segmentPosition", "middle");
			}
			
			group.add(button);
			button.setFocusable(false);
		}
		
		//LabeledComponentGroup viewButtons = new LabeledComponentGroup(null, );
		//addToolbarComponentCenter(viewButtons.component)
	}
	
	public void addBottombarComponentCenter(JComponent comp) {
		bottombar.addComponentToCenter(comp);
	}
	
	public void addBottombarComponentRight(JComponent comp) {
		bottombar.addComponentToRight(comp);
	}

	public void setContent(JPanel content) {
		this.content = content;
	}

	public JPanel getContent() {
		return content;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public JFrame getFrame() {
		return frame;
	}
}
