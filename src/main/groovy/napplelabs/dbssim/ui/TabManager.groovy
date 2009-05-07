/*
	MockDBS: Deep Brain Stimulation Simulator
    Copyright (C) 2009 Zachary Kim

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

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
                    parent.revalidate()
                } as Runnable)
            } as ActionListener);

            group.add(control)
            controls += control
        }



		LabeledComponentGroup viewButtons = new LabeledComponentGroup("Views", controls);
    }

    public void load(String name) {
        def component = tabComponents.get(name)
        if(!component) return

        currentComponent = component
    }

    public void setCurrentComponent(Component component) {
        parent.remove(currentComponent)
		currentComponent = component
		parent.add(currentComponent, BorderLayout.CENTER)
		parent.revalidate()
    }

    public Component getCurrentComponent() {
        return currentComponent
    }
}