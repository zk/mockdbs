package napplelabs.dbssim

import java.util.Listimport ddf.minim.AudioPlayerimport ddf.minim.AudioSignalimport ddf.minim.signals.PinkNoiseimport ddf.minim.Minimimport processing.core.PAppletimport ddf.minim.AudioOutputimport java.io.Fileclass SignalContainer {
	
	private List<RecordedSignal> recordedSignals = []
	PinkNoise pink = new PinkNoise(0.7f)
	AudioOutput out
	Minim minim
	
	public SignalContainer() {
		minim = new Minim(new PApplet());
		out = minim.getLineOut(Minim.MONO);
	}
	
	public void addSignal(String signal) {
//		final AudioPlayer player = minim.loadFile("/Users/zkim/Desktop/Rage Against The Machine - rage against the machine - 06 - Know Your Enemy.mp3");
		//final AudioPlayer player = minim.loadFile("/Users/zkim/Desktop/OUT.mp3");
		final AudioPlayer player = minim.loadFile(signal);
		player.gain = -80
		
		def rs = new RecordedSignal(name: new File(signal).name, player: player);
		add(rs)
		
		
		//final AudioPlayer player2 = minim.loadFile("/Users/zkim/Desktop/Rage Against The Machine - rage against the machine - 06 - Know Your Enemy.mp3");
		player.printControls()
		//player2.loop()
		
		//player.start()
		//player.start()
	}
	
	public void play() {
		recordedSignals.each {
			it.loop()
		}
		
		out.addSignal(pink)
	}
	
	public void pause() {
		audioPlayers.each {
			it.pause()
		}
		
		out.removeSignal(pink)
	}
	
	public float getGain() {
		return audioPlayers[0].gain
	}
	
	public void setGain(float gain) {
		audioPlayers.each {
			it.gain = gain
		}
	}
	
	public void close() {
		audioPlayers.each {
			it.close()
		}
		
		out.close()
	}
	
	private float lastAmp = 0.1;
	
	public void setMute(boolean b) {
		audioPlayers.each {
			if(b) it.mute()
			else it.unmute()
		}
		
		if(!b) {
			pink.amp = lastAmp
		} else {
			lastAmp = pink.amp
			pink.amp = 0
		}
		
	}
	
	public void add(RecordedSignal sig) {
		recordedSignals += sig
	}
	
	public void remove(AudioPlayer player) {
		audioPlayers -= player
	}
	
	public float[] getSamples() {
		float[] out = new float[1024]
		recordedSignals.each { sig ->
			if(!sig.player.muted) {
				for(int i=0; i < out.length; i++) {
					out[i] = out[i] + sig.player.left.get(i)
				}
			}
			
		}
		
		return out
	}
	
	public static void main(String[] args) {
		SignalContainer c = new SignalContainer();
		c.addSignal("/Users/zkim/napplelabs/dbssim/src/main/resources/15-PD-SNr.wav")
		
		c.play();
		
		Thread.sleep(2000)
		
		c.mute = true;
		
		Thread.sleep(2000)
		
		c.mute = false;
	}
	
}

class RecordedSignal {
	AudioPlayer player
	String name
	
	def loop() {
		player.loop()
	}
}