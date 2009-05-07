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
import javax.swing.JCheckBoximport javax.swing.JSliderimport javax.swing.event.ChangeListenerimport javax.swing.UIManagerimport com.explodingpixels.painter.Painterimport com.explodingpixels.painter.GradientPainterimport com.explodingpixels.painter.FocusStatePainterimport com.explodingpixels.swingx.EPPanelimport com.explodingpixels.macwidgets.MacColorUtilsimport java.awt.event.KeyListenerimport java.awt.event.KeyAdapterimport java.awt.event.KeyEventimport java.awt.KeyboardFocusManagerimport java.awt.KeyEventDispatcher
import napplelabs.dbssim.canvasviews.Probe

/**
 * Responsible for initializing the program and creating the main UI frame.
 * 
 * The layout looks something like this:
 * 
 * |------------------------|----|
 * |			a				 |
 * |------------------------|----|
 * |						|	 |
 * |						|	 |
 * |						|	 |
 * |						|	 |
 * |			b			|  c |
 * |						|	 |
 * |						|	 |
 * |						|	 |
 * |------------------------|----|
 * |			d				 |
 * |------------------------|----|
 * 
 * Where:
 * a = Top Bar (see MacFrame)
 * b = CanvasPanel
 * c = Control Panel
 * d = Bottom Bar (see MacFrame)
 */class SimUI {
	MacFrame mf
	Component currentComponent = new JPanel()
	Component tracePanel = new JPanel()
	
	ControlHud controlHud
	
	PinkNoise pink
	
	public SimUI() {
		
		
		mf = new MacFrame(1200, 800)
		
		//Top bar setup
		
		//Bottom bar setup
		JLabel depthLabel = new JLabel("0.00 mm")
		depthLabel.font = new Font("Arial", Font.PLAIN, 30)
		mf.addBottombarComponentCenter(depthLabel)
		
		EPPanel noisePanel = new EPPanel()
		noisePanel.layout = new BorderLayout()
		Painter<Component> focusedPainter =
            new GradientPainter(
                    MacColorUtils.OS_X_BOTTOM_BAR_ACTIVE_TOP_COLOR,
                    MacColorUtils.OS_X_BOTTOM_BAR_ACTIVE_BOTTOM_COLOR);
		Painter<Component> unfocusedPainter =
            new GradientPainter(
                    MacColorUtils.OS_X_BOTTOM_BAR_INACTIVE_TOP_COLOR,
                    MacColorUtils.OS_X_BOTTOM_BAR_INACTIVE_BOTTOM_COLOR);

		Painter<Component> painter = new FocusStatePainter(focusedPainter, focusedPainter,
            unfocusedPainter);
		noisePanel.backgroundPainter = painter
		
		JSlider noiseSlider = new JSlider();
		noiseSlider.minimum = 0
		noiseSlider.maximum = 100
		noiseSlider.value = 0
		noiseSlider.addChangeListener({
			float val = noiseSlider.value as float
			val /= 100
			pink.amp = val
		} as ChangeListener)
		
		
		noisePanel.add(noiseSlider, BorderLayout.CENTER)
		noisePanel.add(new JLabel("Noise"), BorderLayout.WEST)
		mf.addBottombarComponentRight(noisePanel)
		
		Minim minim = new Minim(new PApplet());
		def canvasPanel = new CanvasPanel(minim, depthLabel)
		mf.content.add(canvasPanel, BorderLayout.CENTER)
		
		double one = (float) (1.1*Math.PI / 100);
		int sin_size = 100;
		float[] sin = new float[sin_size];
		for(int i=0; i < sin_size; i++) {
			sin[i] = (float) Math.sin(one * i);
		}
		
		SignalContainer container = new SignalContainer()

		AudioOutput out = minim.getLineOut(Minim.MONO);
		pink = new PinkNoise(0.0f);
		out.addSignal(pink)
		
		mf.frame.visible = true
		
		
		
		//mf.getContent().addKeyListener(new SimKeyListener());
		
		
		
	}
	
	public JComponent getCurrentComponent() {
		return currentComponent
	}
	
	public static void main(String[] args) {
		
		println "WORKING DIRECTORY: " + new File(".").absolutePath
		
		SwingUtilities.invokeLater({
			new SimUI()
		} as Runnable)
		
	}
}

class SimKeyListener extends KeyAdapter {
	public void keyPressed(KeyEvent evt) {
		println evt.keyCode
	}
}