package napplelabs.dbssim.ui;

import napplelabs.dbssim.SignalContainer;
import processing.core.PApplet;

public class TracePApplet extends PApplet {
	private SignalContainer container;
	private float[] trace = new float[50000];
	private int counter = 0;

	public TracePApplet(SignalContainer container) {
		this.container = container;
	}
	
	@Override
	public void setup() {
		
	}
	
	@Override
	public void draw() {
		float[] samples = container.getSamples();
		
		//counter = counter + samples.length;
		
		for(int i = 0; i < samples.length-1; i++) {
			
			double one = (double) getWidth() / (double) trace.length;
			double diff = i * one + counter * one;
			
			trace[counter] = samples[i];
			
			line((int) diff, (int) (getHeight()/2 + -(samples[i])*getHeight()/4), (int) (diff + one), (int) (getHeight()/2 + -(samples[i+1])*getHeight()/4));
			
			counter++;
			
			if(counter >= trace.length) {
				counter = 0;
				rect(0, 0, width, height);
			}
				
		}
	}
}
