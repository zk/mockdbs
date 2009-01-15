package napplelabs.dbssim.ui

import ddf.minim.Minimimport ddf.minim.AudioOutputimport napplelabs.dbssim.NeuronSignalimport napplelabs.dbssim.TracePanelimport ddf.minim.signals.PinkNoiseimport java.awt.BorderLayoutimport com.explodingpixels.macwidgets.MacButtonFactoryimport javax.swing.AbstractButtonimport javax.swing.JButtonimport javax.swing.ImageIconimport javax.swing.JToggleButtonimport javax.swing.ButtonGroupimport com.explodingpixels.macwidgets.LabeledComponentGroupclass SimUI {
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
		
		TracePanel panel = new TracePanel(out);
		
		mf.content.add(panel, BorderLayout.CENTER)
		
		AbstractButton playButton =
                MacButtonFactory.makeUnifiedToolBarButton(
                        new JButton("Play", new ImageIcon(SimUI.class.getResource(
                        "/napplelabs/resources/dotmac.png"))));
		
		mf.addToolbarComponentRight(playButton)
		
		
		JToggleButton leftButton = new JToggleButton("Control");
        
        leftButton.putClientProperty("JButton.buttonType", "segmentedTextured");
        leftButton.putClientProperty("JButton.segmentPosition", "first");
        leftButton.setFocusable(false);

        JToggleButton rightButton = new JToggleButton("Trace");
        rightButton.putClientProperty("JButton.buttonType", "segmentedTextured");
        rightButton.putClientProperty("JButton.segmentPosition", "last");
        rightButton.setFocusable(false);

        ButtonGroup group = new ButtonGroup();
        group.add(leftButton);
        group.add(rightButton);

        LabeledComponentGroup viewButtons = new LabeledComponentGroup(null, leftButton, rightButton);
        
        mf.addToolbarComponentCenter(viewButtons.component)
		
		mf.revalidate()
		
		new Thread(panel).start()
	}
	
	public static void main(String[] args) {
		new SimUI()
	}
}