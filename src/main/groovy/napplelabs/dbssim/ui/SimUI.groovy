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
import java.awt.Font
import javax.swing.JCheckBoximport javax.swing.JSliderimport javax.swing.event.ChangeListenerimport javax.swing.UIManagerclass SimUI {
	MacFrame mf
	Component currentComponent = new JPanel()
	Component tracePanel = new JPanel()
	
	ControlHud controlHud
	
	PinkNoise pink
	
	public SimUI() {
		
		UIManager.setLookAndFeel(
	            UIManager.getSystemLookAndFeelClassName());
		
		mf = new MacFrame(1200, 800)
		
		Minim minim = new Minim(new PApplet());
		
		double one = (float) (1.1*Math.PI / 100);
		int sin_size = 100;
		float[] sin = new float[sin_size];
		for(int i=0; i < sin_size; i++) {
			sin[i] = (float) Math.sin(one * i);
		}
		
		SignalContainer container = new SignalContainer()
		
		//final TracePanel panel = new TracePanel(out, player);
		//panel.start()
		
		//mf.content.add(panel, BorderLayout.CENTER)


        //Set up control button
		AbstractButton playButton =
                MacButtonFactory.makeUnifiedToolBarButton(
                        new JButton("Control"));
		
		playButton.addActionListener({
			SwingUtilities.invokeLater({
				controlHud.visible = true
			} as Runnable)
		} as ActionListener)
		
		
		
		JLabel depthLabel = new JLabel("0.00 mm")
		depthLabel.font = new Font("Arial", Font.PLAIN, 30)
		//mf.addToolbarComponentLeft(depthLabel)
		

        //Set up tabs

		tracePanel = new TracePApplet(container)
		//tracePanel.init()

        def canvasPanel = new CanvasPanel(minim, depthLabel)

		TabManager tabManager = new TabManager(mf.content)
        tabManager.add("Canvas", canvasPanel)
        tabManager.add("Trace", tracePanel)


        
        mf.addBottombarComponentCenter(depthLabel)
        //mf.addToolbarComponentCenter(tabManager.build().component)
        //mf.addToolbarComponentRight(playButton)
        
        JPanel noisePanel = new JPanel();
		JSlider noiseSlider = new JSlider();
		noiseSlider.minimum = 0
		noiseSlider.maximum = 100
		noiseSlider.value = 0
		
		noisePanel.layout = new BorderLayout()
		noisePanel.add(noiseSlider, BorderLayout.CENTER)
		noisePanel.add(new JLabel("Noise"), BorderLayout.WEST)
		
		noiseSlider.addChangeListener({
			float val = noiseSlider.value as float
			val /= 100
			pink.amp = val
		} as ChangeListener)
		
		AudioOutput out = minim.getLineOut(Minim.MONO);
		pink = new PinkNoise(0.0f);
		out.addSignal(pink)
		
		
		mf.addToolbarComponentRight(noisePanel)
		
		mf.frame.visible = true
		
		//container.play()

        
		
		controlHud = new ControlHud(container)
		//controlHud.visible = true
		//panel.start()
		
		tabManager.setCurrentComponent(canvasPanel)
		
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