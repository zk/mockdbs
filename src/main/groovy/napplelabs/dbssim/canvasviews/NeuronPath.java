package napplelabs.dbssim.canvasviews;

import java.awt.Color;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;

public class NeuronPath extends PNode {
	Color color;
	private String mediaFile;
	private Minim minim;
	private AudioPlayer player;
	private PPath path;

	public NeuronPath(Color color, String mediaFile, Minim minim) {
		this.color = color;
		this.mediaFile = mediaFile;
		this.minim = minim;
		init();
	}

	public void init() {
		float d = 50f;
		path = PPath.createEllipse(-d/2, -d/2, d, d);
		

		Color c = new Color(color.getRed(), color.getGreen(), color.getBlue(), 190);
		getPath().setPaint(c);

		addChild(getPath());
		
		player = minim.loadFile(mediaFile);
		player.loop();
		setLevel(0f);
		
		//player.play();
	}
	
	public void setLevel(float level) {
		float gain = (level * (80 + 14)) - 80;
		player.setGain(gain);
	}
	public void setPlaying(boolean playing) {
		if(playing)
			player.play();
		else
			player.pause();
	}
	public boolean isPlaying() {
		return player.isPlaying();
	}

	public void setPath(PPath path) {
		this.path = path;
	}

	public PPath getPath() {
		return path;
	}

	public Color getColor() {
		return color;
	}

	public String getMediaFile() {
		return mediaFile;
	}
}

