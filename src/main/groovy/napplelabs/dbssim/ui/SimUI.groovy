package napplelabs.dbssim.ui

import ddf.minim.Minim
import ddf.minim.AudioOutput
import napplelabs.dbssim.NeuronSignal
import napplelabs.dbssim.TracePanel
import ddf.minim.signals.PinkNoise
import java.awt.BorderLayout
import com.explodingpixels.macwidgets.MacButtonFactory
import javax.swing.AbstractButton
import javax.swing.JButton
import javax.swing.ImageIcon
import javax.swing.JToggleButton
import javax.swing.ButtonGroup
import com.explodingpixels.macwidgets.LabeledComponentGroup
import java.awt.event.ActionListener
import javax.swing.SwingUtilities
import java.lang.Runnable
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JLabel
import com.explodingpixels.macwidgets.HudWindow
import java.awt.Dimension
import ddf.minim.AudioPlayer
import processing.core.PApplet
import napplelabs.dbssim.SignalContainer
import java.awt.Component

class SimUI {
	MacFrame mf
	Component currentComponent = new JPanel()
	Component tracePanel = new JPanel()
	
	ControlHud controlHud
	
	public SimUI() {
		mf = new MacFrame(1200, 800)
		
		Minim minim = new Minim(new PApplet());
		
		double one = (float) (1.1*Math.PI / 100);
		int sin_size = 100;
		float[] sin = new float[sin_size];
		for(int i=0; i < sin_size; i++) {
			sin[i] = (float) Math.sin(one * i);
		}
		
		SignalContainer container = new SignalContainer()
		
		container.addSignal("/Users/zkim/napplelabs/dbssim/src/main/resources/15-PD-SNr.wav")
		container.addSignal("/Users/zkim/napplelabs/dbssim/src/main/resources/10-PD-STN.wav")
		container.addSignal("/Users/zkim/napplelabs/dbssim/src/main/resources/11-PD-STN-2unit.wav")
		container.addSignal("/Users/zkim/napplelabs/dbssim/src/main/resources/12-PD-STN.wav")
		container.addSignal("/Users/zkim/napplelabs/dbssim/src/main/resources/13-PD-STN.wav")
		container.addSignal("/Users/zkim/napplelabs/dbssim/src/main/resources/14-PD-STN.wav")
		container.addSignal("/Users/zkim/napplelabs/dbssim/src/main/resources/15-PD-SNr.wav")
		container.addSignal("/Users/zkim/napplelabs/dbssim/src/main/resources/2-dystonia-gpe.wav")
		container.addSignal("/Users/zkim/napplelabs/dbssim/src/main/resources/2-PD-GPe-burster.wav")
		container.addSignal("/Users/zkim/napplelabs/dbssim/src/main/resources/3-dystonia-gpi.wav")
		container.addSignal("/Users/zkim/napplelabs/dbssim/src/main/resources/4-dystonia-gpi.wav")
		container.addSignal("/Users/zkim/napplelabs/dbssim/src/main/resources/4-PD-border.wav")
		container.addSignal("/Users/zkim/napplelabs/dbssim/src/main/resources/5-dystonia-gpi.wav")
		container.addSignal("/Users/zkim/napplelabs/dbssim/src/main/resources/5-PD-gpi.wav")
		container.addSignal("/Users/zkim/napplelabs/dbssim/src/main/resources/6-PD-gpi.wav")
		container.addSignal("/Users/zkim/napplelabs/dbssim/src/main/resources/7-PD-gpi.wav")
		container.addSignal("/Users/zkim/napplelabs/dbssim/src/main/resources/8-PD-gpi-tremor.wav")
		container.addSignal("/Users/zkim/napplelabs/dbssim/src/main/resources/9-PD-optictract-multipleaxons.wav")
		
		
		
		
		
		
		//final TracePanel panel = new TracePanel(out, player);
		//panel.start()
		
		//mf.content.add(panel, BorderLayout.CENTER)
		
		AbstractButton playButton =
                MacButtonFactory.makeUnifiedToolBarButton(
                        new JButton("Control", new ImageIcon(SimUI.class.getResource(
                        "/napplelabs/resources/dotmac.png"))));
		
		playButton.addActionListener({
			SwingUtilities.invokeLater({
				controlHud.visible = true
			} as Runnable)
		} as ActionListener)
		
		mf.addToolbarComponentRight(playButton)
		
		final JPanel canvasPanel = new JPanel()

		tracePanel = new TracePApplet(container)
		tracePanel.init()

		TabManager tabManager = new TabManager(mf.content)
        tabManager.add("Canvas", canvasPanel)
        tabManager.add("Trace", tracePanel)

        tabManager.load("Canvas");
		


        mf.addToolbarComponentCenter(tabManager.build().component)
		
		mf.frame.visible = true
		
		container.play()
		
		controlHud = new ControlHud(container)
		//controlHud.visible = true
		//panel.start()
		
	}
	
	public JComponent getCurrentComponent() {
		return currentComponent
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater({
			new SimUI()
		} as Runnable)
		
	}
}