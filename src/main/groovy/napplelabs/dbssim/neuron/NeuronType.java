package napplelabs.dbssim.neuron;

import java.awt.Color;

/**
 * Lightweight data-holder for neuron type information, including name, media
 * path, and color. Meant to be used to capture neuron information for creation
 * of a NeuronPath to put on the canvas.
 * 
 * @author zkim
 * 
 */
public class NeuronType {
	private String name = "";
	private String mediaPath = "";
	private Color color = Color.WHITE;
	
	public NeuronType() {
		
	}
	
	public NeuronType(String name, String mediaPath, Color color) {
		this.name = name;
		this.mediaPath = mediaPath;
		this.color = color;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setMediaPath(String mediaPath) {
		this.mediaPath = mediaPath;
	}

	public String getMediaPath() {
		return mediaPath;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
}
