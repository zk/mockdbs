package napplelabs.dbssim.ui

import ddf.minim.Minimimport ddf.minim.AudioOutputimport napplelabs.dbssim.NeuronSignalimport napplelabs.dbssim.TracePanelimport ddf.minim.signals.PinkNoiseimport java.awt.BorderLayoutimport com.explodingpixels.macwidgets.MacButtonFactoryimport javax.swing.AbstractButtonimport javax.swing.JButtonimport javax.swing.ImageIconimport javax.swing.JToggleButtonimport javax.swing.ButtonGroupimport com.explodingpixels.macwidgets.LabeledComponentGroupimport java.awt.event.ActionListenerimport javax.swing.SwingUtilitiesimport java.lang.Runnableimport javax.swing.JComponentimport javax.swing.JPanelimport javax.swing.JLabelimport com.explodingpixels.macwidgets.HudWindowimport java.awt.Dimensionclass SimUI {
	MacFrame mf
	JComponent currentComponent = new JPanel()
	
	public SimUI() {
		mf = new MacFrame()
		
		Minim minim = new Minim(null);
		AudioOutput out = minim.getLineOut(Minim.MONO);
		PinkNoise pink
		
		double one = (float) (1.1*Math.PI / 100);
		int sin_size = 100;
		float[] sin = new float[sin_size];
		for(int i=0; i < sin_size; i++) {
			sin[i] = (float) Math.sin(one * i);
		}
		
		
		
		NeuronSignal ns = new NeuronSignal(sin, 5, 50);
		
		out.addSignal(ns);
		pink = new PinkNoise(0.1f);
		out.addSignal(pink);
		
		final TracePanel panel = new TracePanel(out);
		panel.start()
		
		//mf.content.add(panel, BorderLayout.CENTER)
		
		AbstractButton playButton =
                MacButtonFactory.makeUnifiedToolBarButton(
                        new JButton("Control", new ImageIcon(SimUI.class.getResource(
                        "/napplelabs/resources/dotmac.png"))));
		
		playButton.addActionListener({
			new ControlHud(pink)
		} as ActionListener)
		
		mf.addToolbarComponentRight(playButton)
		
		final JPanel controlPanel = new JPanel()
		controlPanel.add(new JLabel("hello"))
		
		JToggleButton control = new JToggleButton("Control");
		control.putClientProperty("JButton.buttonType", "segmentedTextured");
		control.putClientProperty("JButton.segmentPosition", "first");
		control.setFocusable(false);
		control.addActionListener({
			currentComponent = controlPanel
			SwingUtilities.invokeLater({
				mf.content.repaint()
				mf.revalidate()
			} as Runnable)
		} as ActionListener);
		
		JToggleButton trace = new JToggleButton("Trace");
		trace.putClientProperty("JButton.buttonType", "segmentedTextured");
		trace.putClientProperty("JButton.segmentPosition", "last");
		trace.setFocusable(false);
		trace.addActionListener({
			currentComponent = panel
			SwingUtilities.invokeLater({
				mf.content.repaint()
				mf.revalidate()
			} as Runnable)
		} as ActionListener);
		
		ButtonGroup group = new ButtonGroup();
		group.add(control)
		group.add(trace)
		
		LabeledComponentGroup viewButtons = new LabeledComponentGroup(null, control, trace);
		//mf.addToolbarComponentCenter(viewButtons.component)
		
		mf.content.add(panel, BorderLayout.CENTER)
		
		mf.frame.visible = true
		
		new ControlHud(pink)
		
	}
	
	public void setCurrentComponent(JComponent component) {
		mf.content.remove(currentComponent)
		currentComponent = component
		mf.content.add(currentComponent, BorderLayout.CENTER)
		
		
	}
	
	public JComponent getCurrentComponent() {
		return currentComponent
	}
	
	public static void main(String[] args) {
		new SimUI()
	}
}