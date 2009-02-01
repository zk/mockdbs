package napplelabs.dbssim;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ddf.minim.AudioListener;
import ddf.minim.AudioOutput;
import ddf.minim.AudioPlayer;
import ddf.minim.AudioSignal;

public class TracePanel extends JPanel implements Runnable {
	
	private int counter = 0;
	private float[] trace = new float[50000];
	
	private boolean done = false;
	private boolean resizeFlag = false;


	private final SignalContainer container;

	public TracePanel(SignalContainer container) {
		this.container = container;
		
		setOpaque(true);
		setBackground(Color.black);
		
		addComponentListener(new ComponentAdapter() {
			

			@Override
			public void componentResized(ComponentEvent e) {
				resizeFlag = true;
			}
		});
		
	}
	
	
	public void paint(Graphics _g) {
		
		Graphics2D g = (Graphics2D) _g;
		if(resizeFlag) {
			System.out.println("FILL BLACK");
			g.setColor(Color.black);
			g.fillRect(0, 0, getWidth(), getHeight());
			resizeFlag = false;
		}
		
		g.setColor(Color.WHITE);
		float[] samples = container.getSamples();
		
		//counter = counter + samples.length;
		
		for(int i = 0; i < samples.length-1; i++) {
			
			double one = (double) getWidth() / (double) trace.length;
			double diff = i * one + counter * one;
			
			trace[counter] = samples[i];
			
			g.drawLine((int) diff, (int) (getHeight()/2 + -(samples[i])*getHeight()/4), (int) (diff + one), (int) (getHeight()/2 + -(samples[i+1])*getHeight()/4));
			
			counter++;
			
			if(counter >= trace.length) {
				System.out.println("COUNTER = 0");
				resizeFlag = true;
				counter = 0;
			}
				
		}
		
		
	}


	public void setDone(boolean done) {
		this.done = done;
	}


	public boolean isDone() {
		return done;
	}
	
	public void run() {
		done = false;
		while(!isDone()) {
		SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					repaint();
				}
			});

			try {
				Thread.sleep(1000 / 30);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void start() {
		new Thread(this).start();
	}
	
	public void stop() {
		this.done = true;
	}
}
