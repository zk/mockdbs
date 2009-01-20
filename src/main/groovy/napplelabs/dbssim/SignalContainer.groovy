package napplelabs.dbssim

import java.util.List
	
	private List<AudioPlayer> audioPlayers = []
	PinkNoise pink = new PinkNoise(0.1f)
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
		add(player)
		//final AudioPlayer player2 = minim.loadFile("/Users/zkim/Desktop/Rage Against The Machine - rage against the machine - 06 - Know Your Enemy.mp3");
		player.printControls()
		//player2.loop()
		
		//player.start()
		//player.start()
	}
	
	public void play() {
		audioPlayers.each {
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
	
	public void add(AudioPlayer player) {
		audioPlayers += player
	}
	
	public void remove(AudioPlayer player) {
		audioPlayers -= player
	}
	
	public float[] getSamples() {
		float[] out = new float[1024]
		audioPlayers.each { player ->
			if(!player.muted) {
				for(int i=0; i < out.length; i++) {
					out[i] = out[i] + player.left.get(i)
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