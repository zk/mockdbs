package napplelabs.dbssim.ui

import javax.swing.JComponent
import java.awt.Component
import com.explodingpixels.macwidgets.TriAreaComponent
import com.explodingpixels.macwidgets.MacWidgetFactory
import com.explodingpixels.macwidgets.LabeledComponentGroup
import javax.swing.ButtonGroup
import javax.swing.JToggleButton
import java.awt.event.ActionListener
import javax.swing.JPanel
import javax.swing.SwingUtilities
import java.awt.BorderLayout

/**
 * Created by IntelliJ IDEA.
 * User: zkim
 * Date: Jan 31, 2009
 * Time: 6:37:14 PM
 * To change this template use File | Settings | File Templates.
 */

public class TabManager {

    List<String> tabNames = []
    Map<String, Component> tabComponents = [:]
    Component currentComponent = new JPanel()
    JPanel parent

    public TabManager(JPanel parent) {
        this.parent = parent
    }


    public TabManager add(String name, Component comp) {
        tabNames += name
        tabComponents.put(name, comp)
    }

    public LabeledComponentGroup build() {
        ButtonGroup group = new ButtonGroup();

        def controls = []

        tabNames.eachWithIndex {String name, int i ->
            JToggleButton control = new JToggleButton(name);
            control.putClientProperty("JButton.buttonType", "segmentedTextured");
            if(i == 0) {
                control.putClientProperty("JButton.segmentPosition", "first");
            } else if(i == tabNames.size()-1) {
                control.putClientProperty("JButton.segmentPosition", "last");
            } else {
                control.putClientProperty("JButton.segmentPosition", "middle");
            }

            control.setFocusable(false);
            control.addActionListener({
                currentComponent = tabComponents[name]
                SwingUtilities.invokeLater({
                    parent.repaint()
                    //parent.revalidate()
                } as Runnable)
            } as ActionListener);

            group.add(control)
            controls += control
        }



		LabeledComponentGroup viewButtons = new LabeledComponentGroup(null, controls);
    }

    public void load(String name) {
        def component = tabComponents[name]
        if(!component) return
        
        parent.remove(currentComponent)
		currentComponent = component
		parent.add currentComponent, BorderLayout.CENTER
    }
}