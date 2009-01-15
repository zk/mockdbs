package napplelabs.dbssim.ui

import com.explodingpixels.macwidgets.HudWindowimport java.awt.Dimensionimport javax.swing.JPanelimport net.miginfocom.swing.MigLayoutimport javax.swing.JCheckBoximport java.awt.BorderLayoutimport com.explodingpixels.macwidgets.plaf.HudCheckBoxUIimport javax.swing.JDialogimport javax.swing.JDialogimport javax.swing.JFrameimport java.awt.Colorimport ddf.minim.signals.PinkNoiseimport java.awt.event.ActionListenerimport javax.swing.JSliderimport javax.swing.event.ChangeListenerclass ControlHud {
	
	PinkNoise pink
	float lastAmp = 0;
	
	public ControlHud(PinkNoise pink) {
		this.pink = pink
        
		HudWindow hud = new HudWindow("Control");
		JDialog dialog = hud.jDialog
		
		JPanel panel = new JPanel();
		
		hud.contentPane.layout = new BorderLayout()
		hud.contentPane.add(panel, BorderLayout.CENTER)
		
		
		panel.setLayout(new MigLayout("wrap 2"));
		
		JCheckBox pinkCheck = new JCheckBox("Pink Noise");
		pinkCheck.foreground = Color.WHITE
		//pinkCheck.setUI(new HudCheckBoxUI());
		pinkCheck.addActionListener({
			if(pinkCheck.selected) {
				pink.amp = lastAmp
			} else {
				lastAmp = pink.amp
				pink.amp = 0
			}
		} as ActionListener)
		
		panel.add(pinkCheck)
		
		JSlider pinkSlider = new JSlider();
		pinkSlider.minimum = 0;
		pinkSlider.maximum = 100;
		pinkSlider.addChangeListener({
			panel.revalidate()
			pink.amp = (pinkSlider.value / 100)
			println pink.amp
		} as ChangeListener)
		panel.add(pinkSlider)
		
		dialog.setSize(300, 400);
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
	}
}