package napplelabs.dbssim.ui

import com.explodingpixels.macwidgets.HudWindowimport java.awt.Dimensionimport javax.swing.JPanelimport net.miginfocom.swing.MigLayoutimport javax.swing.JCheckBoximport java.awt.BorderLayoutimport com.explodingpixels.macwidgets.plaf.HudCheckBoxUIimport javax.swing.JDialogimport javax.swing.JDialogimport javax.swing.JFrameimport java.awt.Colorimport ddf.minim.signals.PinkNoiseimport java.awt.event.ActionListenerimport javax.swing.JSliderimport javax.swing.event.ChangeListenerimport napplelabs.dbssim.NeuronSignalimport com.google.inject.Injectorimport com.google.inject.Guiceimport com.google.inject.Binderimport com.google.inject.Moduleimport com.google.inject.Injectimport com.explodingpixels.macwidgets.plaf.HudButtonUIimport ddf.minim.AudioOutputimport napplelabs.dbssim.SignalContainerclass ControlHud {
	
	JSlider pinkSlider
	float lastAmp = 0;
	JPanel panel
	JDialog dialog
	JSlider minSlider, maxSlider
	AudioOutput out
	SignalContainer container
	
	@Inject
	public ControlHud(SignalContainer container) {
		this.container = container
        
		HudWindow hud = new HudWindow("Control");
		dialog = hud.jDialog
		
		panel = new JPanel();
		panel.opaque = false
		
		hud.contentPane.layout = new BorderLayout()
		hud.contentPane.add(panel, BorderLayout.CENTER)
		
		panel.setLayout(new MigLayout("wrap 2, fill"));
		//createMasterControls()
		createPinkNoiseControls()
		createNeuronControls()
		
		
		dialog.setSize(400, 700);
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	def setVisible(boolean vis) {
		dialog.visible = true
	}
	
	def createMasterControls() {
		
		JCheckBox masterCheckbox = new JCheckBox("Master")
		masterCheckbox.name = "master_checkbox"
		masterCheckbox.uI = new HudCheckBoxUI()
		masterCheckbox.addActionListener({
			container.mute = !masterCheckbox.selected
		} as ActionListener)
		masterCheckbox.selected = true;
		
		JSlider masterSlider = new JSlider()
		masterSlider.name = "master_slider"
		masterSlider.minimum = -80
		masterSlider.maximum = 14
		masterSlider.addChangeListener({
			container.gain = masterSlider.value
		} as ChangeListener)
		
		panel.add(masterCheckbox)
		panel.add(masterSlider)
		
	}
	
	def createNeuronControls() {
		
		container.recordedSignals.each { sig ->
			JCheckBox masterCheckbox = new JCheckBox(sig.name)
			masterCheckbox.name = "master_checkbox"
			masterCheckbox.uI = new HudCheckBoxUI()
			masterCheckbox.addActionListener({
				if(!masterCheckbox.selected) {
					sig.player.mute()
				} else {
					sig.player.unmute()
				}
			} as ActionListener)
			masterCheckbox.selected = true;
			
			JSlider masterSlider = new JSlider()
			masterSlider.name = "master_slider"
			masterSlider.minimum = -80
			masterSlider.maximum = 14
			masterSlider.value = sig.player.gain
			masterSlider.addChangeListener({
				sig.player.gain = masterSlider.value
			} as ChangeListener)
			
			panel.add(masterCheckbox)
			panel.add(masterSlider)
		}
		
		
		
	}
	
	def createPinkNoiseControls() {
		JCheckBox pinkCheck = new JCheckBox("Pink Noise");
		pinkCheck.foreground = Color.WHITE
		pinkCheck.setUI(new HudCheckBoxUI());
		pinkCheck.addActionListener({
			if(pinkCheck.selected) {
				container.pink.amp = lastAmp
				pinkSlider.value = container.pink.amp * 100
				
			} else {
				lastAmp = container.pink.amp
				container.pink.amp = 0
				pinkSlider.value = 0
			}
		} as ActionListener)
		
		panel.add(pinkCheck)
		
		pinkSlider = new JSlider();
		pinkSlider.minimum = 0;
		pinkSlider.maximum = 100;
		pinkSlider.addChangeListener({
			
			container.pink.amp = (pinkSlider.value / 100)
			if(container.pink.amp > 0) {
				pinkCheck.selected = true;
			}
			
			dialog.contentPane.repaint()
			dialog.contentPane.revalidate()
			
		} as ChangeListener)
		panel.add(pinkSlider)
		
		pinkSlider.value = container.pink.amp * 100
		if(container.pink.amp > 0) {
			pinkCheck.selected = true
			
		}
	}
	
	public static void main(String[] args) {
		Injector i = Guice.createInjector({ Binder binder ->
		
			float[] sig = new float[3];
			sig[0] = 10
			sig[1] = 20
			sig[2] = 30
			binder.bind(NeuronSignal.class).toInstance(new NeuronSignal(sig, 10, 50))
			
			
		} as Module)
		
		ControlHud h = i.getInstance(ControlHud.class)
		h.visible = true
	}
}