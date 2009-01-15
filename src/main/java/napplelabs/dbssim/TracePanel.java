package napplelabs.dbssim;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ddf.minim.AudioOutput;
import ddf.minim.AudioSignal;

public class TracePanel extends JPanel implements Runnable {
	
	private AudioOutput signal;
	
	private boolean done = false;

	public TracePanel(AudioOutput signal) {
		this.signal = signal;
		
	}
	
	
	public void paint(Graphics _g) {
		
		Graphics2D g = (Graphics2D) _g;
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.WHITE);
		int leftSize = signal.left.size();
		for(int i = 0; i < signal.left.size() - 1; i++) {
			double one = (double) getWidth() / (double) leftSize;
			double diff = i * one;
			
			g.drawLine((int) diff, (int) (getHeight()/2 + -signal.left.get(i)*getHeight()/2), (int) (diff + one), (int) (getHeight()/2 + -signal.left.get(i+1)*getHeight()/2));
		}
	}


	public void setDone(boolean done) {
		this.done = done;
	}


	public boolean isDone() {
		return done;
	}
	
	public void run() {
		while(!isDone()) {
		SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					repaint();
				}
			});

			try {
				Thread.sleep(1000 / 100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
