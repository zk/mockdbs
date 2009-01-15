package napplelabs.dbssim.ui

import ddf.minim.Minimimport ddf.minim.AudioOutputimport napplelabs.dbssim.NeuronSignalimport napplelabs.dbssim.TracePanelimport ddf.minim.signals.PinkNoiseimport java.awt.BorderLayoutimport com.explodingpixels.macwidgets.MacButtonFactoryimport javax.swing.AbstractButtonimport javax.swing.JButtonimport javax.swing.ImageIconimport javax.swing.JToggleButtonimport javax.swing.ButtonGroupimport com.explodingpixels.macwidgets.LabeledComponentGroupimport java.awt.event.ActionListenerimport javax.swing.SwingUtilitiesimport java.lang.Runnableclass SimUI {
	MacFrame mf
	public SimUI() {
		mf = new MacFrame()
		
		Minim minim = new Minim(null);
		AudioOutput out = minim.getLineOut(Minim.MONO);
		
		double one = (float) (Math.PI / 100);
		int sin_size = 100;
		float[] sin = new float[sin_size];
		for(int i=0; i < sin_size; i++) {
			sin[i] = (float) Math.sin(one * i);
		}
		
		NeuronSignal ns = new NeuronSignal(sin, 5, 50);
		
		out.addSignal(ns);
		out.addSignal(new PinkNoise(0.1f));
		
		final TracePanel panel = new TracePanel(out);
		panel.start()
		
		mf.content.add(panel, BorderLayout.CENTER)
		
		AbstractButton playButton =
                MacButtonFactory.makeUnifiedToolBarButton(
                        new JButton("Play", new ImageIcon(SimUI.class.getResource(
                        "/napplelabs/resources/dotmac.png"))));
		
		mf.addToolbarComponentRight(playButton)
		
		JButton control = new JButton("Control");
		control.putClientProperty("JButton.buttonType", "segmentedTextured");
		control.putClientProperty("JButton.segmentPosition", "first");
		control.setFocusable(false);
		control.addActionListener({
			mf.content.remove(panel)
			SwingUtilities.invokeLater({
				mf.content.repaint()
				mf.revalidate()
			} as Runnable)
		} as ActionListener);
		
		JButton trace = new JButton("Trace");
		trace.putClientProperty("JButton.buttonType", "segmentedTextured");
		trace.putClientProperty("JButton.segmentPosition", "last");
		trace.setFocusable(false);
		trace.addActionListener({
			mf.content.add(panel, BorderLayout.CENTER)
			SwingUtilities.invokeLater({
				mf.content.repaint()
				mf.revalidate()
			} as Runnable)
		} as ActionListener);
		
		ButtonGroup group = new ButtonGroup();
		group.add(control)
		group.add(trace)
		
		LabeledComponentGroup viewButtons = new LabeledComponentGroup(null, control, trace);
		mf.addToolbarComponentCenter(viewButtons.component)
		
		mf.revalidate()
		
		
	}
	
	public static void main(String[] args) {
		new SimUI()
	}
}