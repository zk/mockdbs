package napplelabs.dbssim.example;

import javax.swing.JFrame;

import napplelabs.dbssim.NeuronSignal;
import napplelabs.dbssim.TracePanel;
import ddf.minim.AudioOutput;
import ddf.minim.Minim;
import ddf.minim.signals.PinkNoise;

public class ExampleNeuronSignal {
	private static AudioOutput out;
	private static NeuronSignal ns;
	private static PinkNoise pink;

	public static void main(String[] args) {
		JFrame frame = new JFrame("");
		frame.setSize(500, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Minim minim = new Minim(null);
		out = minim.getLineOut(Minim.MONO);
		
		double one = (float) (Math.PI / 100);
		int sin_size = 100;
		float[] sin = new float[sin_size];
		for(int i=0; i < sin_size; i++) {
			sin[i] = (float) Math.sin(one * i);
		}
		
		ns = new NeuronSignal(sin, 5, 50);
		
		out.addSignal(ns);
		out.addSignal(new PinkNoise(0.1f));
		
		TracePanel panel = new TracePanel(out);
		frame.setContentPane(panel);
		
		frame.setVisible(true);
		
		new Thread(panel).start();
	}
}
