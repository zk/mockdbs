package napplelabs.dbssim;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.DataLine.Info;

public class NeuronPlayer {
	
	public NeuronPlayer() throws LineUnavailableException {
		Info info = new Info(SourceDataLine.class, new AudioFormat(22050f, 8, 1, true, false));
		SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
		
		System.out.println("Supported: " + AudioSystem.isLineSupported(info));
		
		int buff_size = 22100;
		
		PinkNoise noise = new PinkNoise(1, 5);

		
		line.open();
		line.start();
		
		byte[] buffer = new byte[buff_size];
		for(int i=0; i < buff_size; i++) {
			buffer[i] = (byte) ((noise.nextValue() * 255) - 127);
		}
		
		line.write(buffer, 0, buff_size);
		
		
		
		line.drain();
		
		line.stop();
		line.close();
	}
	
	public static void main(String[] args) throws LineUnavailableException {
		new NeuronPlayer();
	}
}
