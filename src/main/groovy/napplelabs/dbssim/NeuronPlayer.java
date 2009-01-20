package napplelabs.dbssim;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.DataLine.Info;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import processing.core.PApplet;

import ddf.minim.AudioOutput;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import ddf.minim.signals.PinkNoise;
import ddf.minim.signals.SineWave;

public class NeuronPlayer {
	
	private PinkNoise pink;
	private SineWave sin;
	private AudioOutput out;

	public NeuronPlayer() throws LineUnavailableException, InterruptedException {
		
		JFrame frame = new JFrame("");
		frame.setSize(500, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Minim minim = new Minim(new PApplet());
		out = minim.getLineOut(Minim.MONO);
		pink = new PinkNoise(0.2f);
		
		//out.addSignal(pink);
		
		
		
		
		File file = new File("/Users/zkim/Desktop/Rage Against The Machine - rage against the machine - 06 - Know Your Enemy.mp3");
		System.out.println("Exists: " + file.exists());
		AudioPlayer player = minim.loadFile(file.getAbsolutePath());
		
		player.play();
		
		
		
		//TracePanel panel = new TracePanel(out);
		//frame.setContentPane(panel);
		
		frame.setVisible(true);
		
		//new Thread(panel).start();
	}
	
	public static void main(String[] args) throws LineUnavailableException, InterruptedException {
		new NeuronPlayer();
	}
}
